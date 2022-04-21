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
import com.bueno.domain.usecases.bot.BotUseCase;
import com.bueno.domain.usecases.game.CreateForUserAndBotRequestModel;
import com.bueno.domain.usecases.game.CreateGameUseCase;
import com.bueno.domain.usecases.game.GameRepository;
import com.bueno.domain.usecases.hand.PlayCardRequestModel;
import com.bueno.domain.usecases.hand.PlayCardUseCase;
import com.bueno.domain.usecases.hand.PointsProposalUseCase;
import com.bueno.domain.usecases.utils.dtos.CardDto;
import com.bueno.domain.usecases.intel.HandleIntelUseCase;
import com.bueno.domain.usecases.utils.dtos.IntelDto;
import com.bueno.domain.usecases.user.CreateUserUseCase;
import com.bueno.domain.usecases.user.UserRepository;
import com.bueno.domain.usecases.user.UserRequestModel;
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

    private final List<IntelDto> missingIntel;
    private IntelDto lastIntel;
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

    private boolean isCurrentHandDone(IntelDto intel) {
        final String event = intel.getEvent();
        if(event == null) return false;
        return event.equals("NEW_HAND");
    }

    private void createAccount(){
        final var usernameReader = new UsernameReader();
        final var username = usernameReader.execute();
        final var requestModel = new UserRequestModel(username, "unused@email.com");
        final var response = createUserUseCase.create(requestModel);
        userUUID = response.getUuid();
    }

    private void createGame(){
        final var botNames = BotUseCase.availableBots();
        final var bot = readBotName(botNames);
        final var requestModel = new CreateForUserAndBotRequestModel(userUUID, bot);
        lastIntel = gameUseCase.createForUserAndBot(requestModel);
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

        final var ownedCards = handleIntelUseCase.ownedCards(userUUID).getCards();
        final var cardReader = new CardReader(this, ownedCards);
        final var cardModeReader = new CardModeReader();
        final var card = cardReader.execute();
        final var mode = cardModeReader.execute();

        final var requestModel = new PlayCardRequestModel(userUUID, card);
        if(mode == OPEN) playCardUseCase.playCard(requestModel);
        else playCardUseCase.discard(requestModel);
    }

    private void updateIntel() {
        var responseModel = handleIntelUseCase.findIntelSince(userUUID, lastIntel.getTimestamp());
        missingIntel.addAll(responseModel.getIntelSince());
        if(missingIntel.isEmpty()) missingIntel.add(lastIntel);
        else lastIntel = missingIntel.get(missingIntel.size() - 1);
    }

    private boolean canNotPerform(Set<String> allowedActions, Set<String> notAllowedActions) {
        final UUID possibleUuid = lastIntel.getCurrentPlayerUuid();
        if(possibleUuid == null) return true;
        final boolean isCurrentPlayer = possibleUuid.equals(userUUID);
        final boolean isPerformingAllowedAction = lastIntel.getPossibleActions().containsAll(allowedActions);
        final boolean isNotPerformingAnyNotAllowed = Collections.disjoint(lastIntel.getPossibleActions(), notAllowedActions);
        return !isCurrentPlayer || !isPerformingAllowedAction || !isNotPerformingAnyNotAllowed;
    }

    private void handleRaiseRequest(){
        final Set<String> allowedActions = Set.of("RAISE");
        final Set<String> notAllowedActions = Set.of("QUIT");

        updateIntel();
        if(canNotPerform(allowedActions, notAllowedActions)) return;

        var requestReader = new RaiseRequestReader(this, nextScore(lastIntel.getHandPoints()));
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

        var responseReader = new RaiseResponseReader(this,  nextScore(lastIntel.getHandPoints()));
        switch (responseReader.execute()){
            case QUIT -> pointsProposalUseCase.quit(userUUID);
            case ACCEPT -> pointsProposalUseCase.accept(userUUID);
            case RAISE -> pointsProposalUseCase.raise(userUUID);
        }
    }

    private void handleMaoDeOnzeResponse(){
        updateIntel();
        if(!lastIntel.isMaoDeOnze()) return;

        var responseReader = new MaoDeOnzeResponseReader(this);
        if(responseReader.execute() == ACCEPT) {
            pointsProposalUseCase.accept(userUUID);}
        pointsProposalUseCase.quit(userUUID);
    }

    public void printGameIntel(int delayInMilliseconds){
        if(missingIntel.size() == 1) delayInMilliseconds = 0;
        final List<CardDto> ownedCards = handleIntelUseCase.ownedCards(userUUID).getCards();
        var intelPrinter  = new IntelPrinter(userUUID, ownedCards, missingIntel, delayInMilliseconds);
        intelPrinter.execute();
    }

    public String getOpponentUsername(){
        return lastIntel.getCurrentOpponentUsername();
    }
}
