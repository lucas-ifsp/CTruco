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
import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.game.Intel;
import com.bueno.domain.usecases.game.CreateGameUseCase;
import com.bueno.domain.usecases.hand.HandleIntelUseCase;
import com.bueno.domain.usecases.hand.PlayCardUseCase;
import com.bueno.domain.usecases.hand.PlayCardUseCase.RequestModel;
import com.bueno.domain.usecases.hand.ScoreProposalUseCase;
import com.bueno.domain.usecases.player.CreateUserUseCase;
import com.bueno.persistence.inmemory.InMemoryGameRepository;
import com.bueno.persistence.inmemory.InMemoryUserRepository;

import java.util.*;
import java.util.logging.LogManager;

import static com.bueno.application.cli.commands.CardModeReader.CardMode.OPEN;
import static com.bueno.application.cli.commands.MaoDeOnzeResponseReader.MaoDeOnzeChoice.ACCEPT;
import static com.bueno.application.cli.commands.RaiseRequestReader.RaiseChoice.REQUEST;

public class GameCLI {

    private final CreateUserUseCase createUserUseCase;
    private final CreateGameUseCase gameUseCase;
    private final PlayCardUseCase playCardUseCase;
    private final ScoreProposalUseCase scoreProposalUseCase;
    private final HandleIntelUseCase handleIntelUseCase;

    private final InMemoryGameRepository gameRepo;
    private final InMemoryUserRepository userRepo;
    private final List<Intel> missingIntel;
    private Intel lastIntel;
    private UUID userUUID;

    public static void main(String[] args) {
        LogManager.getLogManager().reset();
        final GameCLI cli = new GameCLI();
        cli.createAccount();
        cli.createGame();
        cli.play();
    }

    public GameCLI() {
        gameRepo = new InMemoryGameRepository();
        userRepo = new InMemoryUserRepository();

        createUserUseCase = new CreateUserUseCase(userRepo);
        gameUseCase = new CreateGameUseCase(gameRepo, userRepo);
        playCardUseCase = new PlayCardUseCase(gameRepo);
        scoreProposalUseCase = new ScoreProposalUseCase(gameRepo);
        handleIntelUseCase = new HandleIntelUseCase(gameRepo);
        missingIntel = new ArrayList<>();
    }

    private void play(){
        while (!lastIntel.isGameDone()){
            handleMaoDeOnzeResponse();
            if(isCurrentHandDone(lastIntel)) continue;
            handleRaiseRequest();
            if(isCurrentHandDone(lastIntel)) continue;
            handleCardPlaying();
            if(isCurrentHandDone(lastIntel)) continue;
            handleRaiseResponse();
        }
    }

    private boolean isCurrentHandDone(Intel intel) {
        return intel.event().orElse("").equals("NEW_HAND");
    }

    private void createAccount(){
        final UsernameReader usernameReader = new UsernameReader();
        final String username = usernameReader.execute();
        userUUID = createUserUseCase.create(new CreateUserUseCase.RequestModel(username, "unused@email.com"));
    }

    private void createGame(){
        lastIntel = gameUseCase.create(userUUID, "MineiroBot");
        missingIntel.add(lastIntel);
    }

    private void handleCardPlaying(){
        final Set<String> allowedActions = Set.of("PLAY");
        final Set<String> notAllowedActions = Set.of();

        updateIntel();
        if(canNotPerform(allowedActions, notAllowedActions)) return;

        final List<Card> ownedCards = handleIntelUseCase.getOwnedCards(userUUID);
        final CardReader cardReader = new CardReader(this, ownedCards);
        final CardModeReader cardModeReader = new CardModeReader();
        final Card card = cardReader.execute();
        final CardModeReader.CardMode mode = cardModeReader.execute();

        if(mode == OPEN) playCardUseCase.playCard(new RequestModel(userUUID, card));
        else playCardUseCase.discard(new RequestModel(userUUID, card));
    }

    private void updateIntel() {
        List<Intel> newIntel = handleIntelUseCase.findIntelSince(userUUID, lastIntel);
        missingIntel.addAll(newIntel);
        if(missingIntel.isEmpty()) missingIntel.add(lastIntel);
        else lastIntel = missingIntel.get(missingIntel.size() - 1);
    }

    private boolean canNotPerform(Set<String> allowedActions, Set<String> notAllowedActions) {
        final Optional<UUID> possibleUuid = lastIntel.currentPlayerUuid();
        if(possibleUuid.isEmpty()) return true;
        final boolean isCurrentPlayer = possibleUuid.get().equals(userUUID);
        final boolean isPerformingAllowedAction = lastIntel.possibleActions().containsAll(allowedActions);
        final boolean isNotPerformingAnyNotAllowed = Collections.disjoint(lastIntel.possibleActions(), notAllowedActions);
        return !isCurrentPlayer || !isPerformingAllowedAction || !isNotPerformingAnyNotAllowed;
    }

    private void handleRaiseRequest(){
        final Set<String> allowedActions = Set.of("RAISE");
        final Set<String> notAllowedActions = Set.of("QUIT");

        updateIntel();
        if(canNotPerform(allowedActions, notAllowedActions)) return;

        RaiseRequestReader requestReader = new RaiseRequestReader(this, nextScore(lastIntel.handScore()));
        if(requestReader.execute() == REQUEST) scoreProposalUseCase.raise(userUUID);
    }

    private int nextScore(int score){
        return score == 1 ? 3 : score + 3;
    }

    private void handleRaiseResponse(){
        final Set<String> allowedActions = Set.of("RAISE", "ACCEPT", "QUIT");
        final Set<String> notAllowedActions = Set.of();

        updateIntel();
        if(canNotPerform(allowedActions, notAllowedActions)) return;

        RaiseResponseReader responseReader = new RaiseResponseReader(this,  nextScore(lastIntel.handScore()));
        switch (responseReader.execute()){
            case QUIT -> scoreProposalUseCase.quit(userUUID);
            case ACCEPT -> scoreProposalUseCase.accept(userUUID);
            case RAISE -> scoreProposalUseCase.raise(userUUID);
        }
    }

    private void handleMaoDeOnzeResponse(){
        updateIntel();
        if(!lastIntel.isMaoDeOnze()) return;

        MaoDeOnzeResponseReader responseReader = new MaoDeOnzeResponseReader(this);
        if(responseReader.execute() == ACCEPT) {
            scoreProposalUseCase.accept(userUUID);}
        scoreProposalUseCase.quit(userUUID);
    }

    public void printGameIntel(int delayInMilliseconds){
        if(missingIntel.size() == 1) delayInMilliseconds = 0;
        final List<Card> ownedCards = handleIntelUseCase.getOwnedCards(userUUID);
        IntelPrinter intelPrinter  = new IntelPrinter(userUUID, ownedCards, missingIntel, delayInMilliseconds);
        intelPrinter.execute();
    }

    public String getOpponentUsername(){
        return lastIntel.currentOpponentUsername();
    }
}
