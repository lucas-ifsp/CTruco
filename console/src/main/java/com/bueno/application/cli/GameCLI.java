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
import com.bueno.domain.entities.intel.Intel;
import com.bueno.domain.usecases.bot.BotUseCase;
import com.bueno.domain.usecases.game.CreateGameUseCase;
import com.bueno.domain.usecases.game.GameRepository;
import com.bueno.domain.usecases.intel.HandleIntelUseCase;
import com.bueno.domain.usecases.hand.usecases.PlayCardUseCase;
import com.bueno.domain.usecases.hand.usecases.PlayCardUseCase.RequestModel;
import com.bueno.domain.usecases.hand.usecases.PointsProposalUseCase;
import com.bueno.domain.usecases.user.CreateUserUseCase;
import com.bueno.domain.usecases.user.UserRepository;
import com.bueno.persistence.inmemory.InMemoryGameRepository;
import com.bueno.persistence.inmemory.InMemoryUserRepository;
import com.google.common.primitives.Ints;

import java.util.*;
import java.util.logging.LogManager;

import static com.bueno.application.cli.commands.CardModeReader.CardMode.OPEN;
import static com.bueno.application.cli.commands.MaoDeOnzeResponseReader.MaoDeOnzeChoice.ACCEPT;
import static com.bueno.application.cli.commands.RaiseRequestReader.RaiseChoice.REQUEST;

@SuppressWarnings("UnstableApiUsage")
public class GameCLI {

    private final CreateUserUseCase createUserUseCase;
    private final CreateGameUseCase gameUseCase;
    private final PlayCardUseCase playCardUseCase;
    private final PointsProposalUseCase pointsProposalUseCase;
    private final HandleIntelUseCase handleIntelUseCase;

    private final List<Intel> missingIntel;
    private Intel lastIntel;
    private UUID userUUID;

    public static void main(String[] args) {
        LogManager.getLogManager().reset();
        final var cli = new GameCLI();
        cli.createAccount();
        cli.createGame();
        cli.play();
    }

    public GameCLI() {
        final GameRepository gameRepo = new InMemoryGameRepository();
        final UserRepository userRepo = new InMemoryUserRepository();

        createUserUseCase = new CreateUserUseCase(userRepo);
        gameUseCase = new CreateGameUseCase(gameRepo, userRepo);
        playCardUseCase = new PlayCardUseCase(gameRepo);
        pointsProposalUseCase = new PointsProposalUseCase(gameRepo);
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
        final List<String> botNames = BotUseCase.availableBots();
        final String bot = readBotName(botNames);
        lastIntel = gameUseCase.createWithUserAndBot(userUUID, bot);
        missingIntel.add(lastIntel);
    }

    private String readBotName(List<String> botNames) {
        Integer botId;
        while (true){
            for (int i = 0; i < botNames.size(); i++) {
                System.out.print("[" + (i + 1) + "] " + botNames.get(i) + "\t");
            }
            System.out.print("\n");
            System.out.print("Selecione um bot pelo número: ");
            final Scanner scanner = new Scanner(System.in);
            botId = Ints.tryParse(scanner.nextLine());
            if (botId == null || botId < 1 || botId > botNames.size()){
                System.out.println("Invalid input!");
                continue;
            }
            break;
        }
        return botNames.get(botId - 1);
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

        RaiseRequestReader requestReader = new RaiseRequestReader(this, nextScore(lastIntel.handPoints()));
        if(requestReader.execute() == REQUEST) pointsProposalUseCase.raise(userUUID);
    }

    private int nextScore(int score){
        return score == 1 ? 3 : score + 3;
    }

    private void handleRaiseResponse(){
        final Set<String> allowedActions = Set.of("RAISE", "ACCEPT", "QUIT");
        final Set<String> notAllowedActions = Set.of();

        updateIntel();
        if(canNotPerform(allowedActions, notAllowedActions)) return;

        RaiseResponseReader responseReader = new RaiseResponseReader(this,  nextScore(lastIntel.handPoints()));
        switch (responseReader.execute()){
            case QUIT -> pointsProposalUseCase.quit(userUUID);
            case ACCEPT -> pointsProposalUseCase.accept(userUUID);
            case RAISE -> pointsProposalUseCase.raise(userUUID);
        }
    }

    private void handleMaoDeOnzeResponse(){
        updateIntel();
        if(!lastIntel.isMaoDeOnze()) return;

        MaoDeOnzeResponseReader responseReader = new MaoDeOnzeResponseReader(this);
        if(responseReader.execute() == ACCEPT) {
            pointsProposalUseCase.accept(userUUID);}
        pointsProposalUseCase.quit(userUUID);
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
