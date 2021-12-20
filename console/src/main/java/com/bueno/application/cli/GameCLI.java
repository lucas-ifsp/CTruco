/*
 *  Copyright (C) 2021 Lucas B. R. de Oliveira - IFSP/SCL
 *  Contact: lucas <dot> oliveira <at> ifsp <dot> edu <dot> br
 *
 *  This file is part of CTruco (Truco game for didactic purpose).
 *
 *  CTruco is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CTruco is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CTruco.  If not, see <https://www.gnu.org/licenses/>
 */

package com.bueno.application.cli;

import com.bueno.application.cli.commands.*;
import com.bueno.application.standalone.InMemoryGameRepository;
import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.game.Game;
import com.bueno.domain.entities.game.Intel;
import com.bueno.domain.entities.game.PossibleActions;
import com.bueno.domain.entities.player.mineirobot.MineiroBot;
import com.bueno.domain.entities.player.util.Player;
import com.bueno.domain.usecases.game.CreateGameUseCase;
import com.bueno.domain.usecases.hand.PlayHandUseCase;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.logging.LogManager;

import static com.bueno.application.cli.commands.CardModeReader.CardMode.OPEN;
import static com.bueno.application.cli.commands.MaoDeOnzeResponseReader.MaoDeOnzeChoice.ACCEPT;
import static com.bueno.application.cli.commands.RaiseRequestReader.RaiseChoice.REQUEST;

public class GameCLI {

    private final InMemoryGameRepository repo;
    private final CreateGameUseCase gameUseCase;
    private final PlayHandUseCase playHandUseCase;
    private Intel lastIntel;
    private Player player;
    private Game game;
    private final UUID botUUID;
    private final UUID playerUUID;

    public static void main(String[] args) {
        LogManager.getLogManager().reset();
        final GameCLI cli = new GameCLI();
        cli.createGame();
        cli.play();
    }

    public GameCLI() {
        repo = new InMemoryGameRepository();
        gameUseCase = new CreateGameUseCase(repo);
        playHandUseCase = new PlayHandUseCase(repo);
        botUUID = UUID.randomUUID();
        playerUUID = UUID.randomUUID();
    }

    private void play(){
        while (!lastIntel.isGameDone()){
            int handsPlayed = game.handsPlayed();
            handleMaoDeOnzeResponse();
            if(isCurrentHandDone(handsPlayed)) continue;
            handleRaiseRequest();
            if(isCurrentHandDone(handsPlayed)) continue;
            handleCardPlaying();
            if(isCurrentHandDone(handsPlayed)) continue;
            handleRaiseResponse();
        }
    }

    private boolean isCurrentHandDone(int handsPlayed) {
        return game.handsPlayed() > handsPlayed;
    }

    private void createGame(){
        final UsernameReader usernameReader = new UsernameReader();
        final String username = usernameReader.execute();
        player = new CLIPlayer(username, playerUUID);
        game = gameUseCase.create(player, new MineiroBot(repo, botUUID));
        lastIntel = game.getIntel();
    }

    private void handleCardPlaying(){
        final EnumSet<PossibleActions> actions = EnumSet.of(PossibleActions.PLAY);
        if(canNotPerform(actions)) return;

        final CardReader cardReader = new CardReader(this, lastIntel.currentPlayerCards());
        final CardModeReader cardModeReader = new CardModeReader();
        final Card card = cardReader.execute();
        final CardModeReader.CardMode mode = cardModeReader.execute();

        if(mode == OPEN) playHandUseCase.playCard(playerUUID, card);
        else playHandUseCase.discard(playerUUID, card);
    }

    private void handleRaiseRequest(){
        final EnumSet<PossibleActions> actions = EnumSet.of(PossibleActions.RAISE);
        if(canNotPerform(actions)) return;

        RaiseRequestReader requestReader = new RaiseRequestReader(this, lastIntel.handScore().increase());
        if(requestReader.execute() == REQUEST) playHandUseCase.raiseBet(playerUUID);
    }

    private void handleRaiseResponse(){
        final EnumSet<PossibleActions> actions = EnumSet.of(PossibleActions.RAISE, PossibleActions.ACCEPT, PossibleActions.QUIT);
        if(canNotPerform(actions)) return;

        RaiseResponseReader responseReader = new RaiseResponseReader(this, lastIntel.handScore().increase());
        switch (responseReader.execute()){
            case QUIT -> playHandUseCase.quit(playerUUID);
            case ACCEPT -> playHandUseCase.accept(playerUUID);
            case RAISE -> playHandUseCase.raiseBet(playerUUID);
        };
    }

    private void handleMaoDeOnzeResponse(){
        if(!lastIntel.isMaoDeOnze()) return;

        MaoDeOnzeResponseReader responseReader = new MaoDeOnzeResponseReader(this);
        if(responseReader.execute() == ACCEPT) {playHandUseCase.accept(playerUUID);}
        playHandUseCase.quit(playerUUID);
    }

    private boolean canNotPerform(EnumSet<PossibleActions> actions) {
        return !lastIntel.currentPlayerUuid().equals(playerUUID) || !lastIntel.possibleActions().containsAll(actions);
    }

    public void printGameIntel(int delayInMilliseconds){
        final List<Intel> missingIntel = playHandUseCase.findIntelSince(playerUUID, lastIntel);
        if(missingIntel.isEmpty()) {
            missingIntel.add(lastIntel);
            delayInMilliseconds = 0;
        }
        IntelPrinter intelPrinter  = new IntelPrinter(player, missingIntel, delayInMilliseconds);
        intelPrinter.execute();
        lastIntel = missingIntel.get(missingIntel.size() - 1);
    }

    public String getOpponentUsername(){
        return lastIntel.currentOpponentUsername();
    }
}
