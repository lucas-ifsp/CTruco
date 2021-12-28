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
import com.bueno.application.repository.InMemoryGameRepository;
import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.game.Game;
import com.bueno.domain.entities.game.Intel;
import com.bueno.domain.entities.game.PossibleActions;
import com.bueno.domain.entities.player.mineirobot.MineiroBot;
import com.bueno.domain.entities.player.util.Player;
import com.bueno.domain.usecases.game.CreateGameUseCase;
import com.bueno.domain.usecases.hand.BetUseCase;
import com.bueno.domain.usecases.hand.HandleIntelUseCase;
import com.bueno.domain.usecases.hand.PlayCardUseCase;
import com.bueno.domain.usecases.hand.PlayCardUseCase.RequestModel;

import java.util.*;
import java.util.logging.LogManager;

import static com.bueno.application.cli.commands.CardModeReader.CardMode.OPEN;
import static com.bueno.application.cli.commands.MaoDeOnzeResponseReader.MaoDeOnzeChoice.ACCEPT;
import static com.bueno.application.cli.commands.RaiseRequestReader.RaiseChoice.REQUEST;

public class GameCLI {

    private final CreateGameUseCase gameUseCase;
    private final PlayCardUseCase playCardUseCase;
    private final BetUseCase betUseCase;
    private final HandleIntelUseCase handleIntelUseCase;

    private final InMemoryGameRepository repo;
    private final List<Intel> missingIntel;
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
        playCardUseCase = new PlayCardUseCase(repo);
        betUseCase = new BetUseCase(repo);
        handleIntelUseCase = new HandleIntelUseCase(repo);

        botUUID = UUID.randomUUID();
        playerUUID = UUID.randomUUID();
        missingIntel = new ArrayList<>();
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
        missingIntel.add(lastIntel);
    }

    private void handleCardPlaying(){
        final EnumSet<PossibleActions> allowedActions = EnumSet.of(PossibleActions.PLAY);
        final EnumSet<PossibleActions> notAllowedActions = EnumSet.noneOf(PossibleActions.class);

        updateIntel();
        if(canNotPerform(allowedActions, notAllowedActions)) return;

        player.setCards(handleIntelUseCase.getOwnedCards(playerUUID));
        final CardReader cardReader = new CardReader(this, player.getCards());
        final CardModeReader cardModeReader = new CardModeReader();
        final Card card = cardReader.execute();
        final CardModeReader.CardMode mode = cardModeReader.execute();

        if(mode == OPEN) playCardUseCase.playCard(new RequestModel(playerUUID, card));
        else playCardUseCase.discard(new RequestModel(playerUUID, card));
    }

    private void updateIntel() {
        List<Intel> newIntel = handleIntelUseCase.findIntelSince(playerUUID, lastIntel);
        missingIntel.addAll(newIntel);
        if(missingIntel.isEmpty()) missingIntel.add(lastIntel);
        else lastIntel = missingIntel.get(missingIntel.size() - 1);
    }

    private boolean canNotPerform(EnumSet<PossibleActions> allowedActions, EnumSet<PossibleActions> notAllowedActions) {
        final Optional<UUID> possibleUuid = lastIntel.currentPlayerUuid();
        if(possibleUuid.isEmpty()) return true;
        final boolean isCurrentPlayer = possibleUuid.get().equals(playerUUID);
        final boolean isPerformingAllowedAction = lastIntel.possibleActions().containsAll(allowedActions);
        final boolean isNotPerformingAnyNotAllowed = Collections.disjoint(lastIntel.possibleActions(), notAllowedActions);
        return !isCurrentPlayer || !isPerformingAllowedAction || !isNotPerformingAnyNotAllowed;
    }

    private void handleRaiseRequest(){
        final EnumSet<PossibleActions> allowedActions = EnumSet.of(PossibleActions.RAISE);
        final EnumSet<PossibleActions> notAllowedActions = EnumSet.of(PossibleActions.QUIT);

        updateIntel();
        if(canNotPerform(allowedActions, notAllowedActions)) return;

        RaiseRequestReader requestReader = new RaiseRequestReader(this, lastIntel.handScore().increase());
        if(requestReader.execute() == REQUEST) betUseCase.raiseBet(playerUUID);
    }

    private void handleRaiseResponse(){
        final EnumSet<PossibleActions> allowedActions = EnumSet.of(PossibleActions.RAISE, PossibleActions.ACCEPT, PossibleActions.QUIT);
        final EnumSet<PossibleActions> notAllowedActions = EnumSet.noneOf(PossibleActions.class);

        updateIntel();
        if(canNotPerform(allowedActions, notAllowedActions)) return;

        RaiseResponseReader responseReader = new RaiseResponseReader(this, lastIntel.handScore().increase());
        switch (responseReader.execute()){
            case QUIT -> betUseCase.quit(playerUUID);
            case ACCEPT -> betUseCase.accept(playerUUID);
            case RAISE -> betUseCase.raiseBet(playerUUID);
        };
    }

    private void handleMaoDeOnzeResponse(){
        updateIntel();
        if(!lastIntel.isMaoDeOnze()) return;

        MaoDeOnzeResponseReader responseReader = new MaoDeOnzeResponseReader(this);
        if(responseReader.execute() == ACCEPT) {
            betUseCase.accept(playerUUID);}
        betUseCase.quit(playerUUID);
    }

    public void printGameIntel(int delayInMilliseconds){
        if(missingIntel.size() == 1) delayInMilliseconds = 0;
        IntelPrinter intelPrinter  = new IntelPrinter(player, missingIntel, delayInMilliseconds);
        intelPrinter.execute();
    }

    public String getOpponentUsername(){
        return lastIntel.currentOpponentUsername();
    }
}
