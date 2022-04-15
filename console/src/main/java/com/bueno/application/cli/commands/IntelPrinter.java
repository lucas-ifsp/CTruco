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

package com.bueno.application.cli.commands;

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.usecases.intel.IntelResponseModel;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class IntelPrinter implements Command<Void>{

    private final List<IntelResponseModel> history;
    private final int delayInMilliseconds;
    private final UUID userUUID;
    private final List<Card> userCards;

    public IntelPrinter(UUID userUUID, List<Card> userCards, List<IntelResponseModel> history, int delayInMilliseconds) {
        this.userUUID = userUUID;
        this.userCards = List.copyOf(userCards);
        this.history = history;
        this.delayInMilliseconds = delayInMilliseconds;
    }

    @Override
    public Void execute() {
        while (!history.isEmpty()){
            print(history.remove(0));
        }
        return null;
    }

    private void print(IntelResponseModel intel) {
        clearAfter(delayInMilliseconds);
        printDelimiter(false);
        printGameMainInfo(intel);
        printRounds(intel);
        printCardsOpenInTable(intel);
        printVira(intel.getVira());
        if(isUserTurn(intel)) {
            printCardToPlayAgainst(intel);
            printOwnedCards();
        }
        printResultIfAvailable(intel);
        printDelimiter(true);
    }

    private void clearAfter(int delayInMilliseconds) {
        if(delayInMilliseconds > 0) {
            try {TimeUnit.MILLISECONDS.sleep(delayInMilliseconds);}
            catch (InterruptedException e) {e.printStackTrace();}
        }
        cls();
    }

    private void printDelimiter(boolean blankLine) {
        System.out.println("+==========================================================+" + (blankLine ? "\n" : ""));
    }

    private void printGameMainInfo(IntelResponseModel intel) {
        if(intel.getCurrentPlayerUuid() != null) {
            final var user = intel.getPlayers().stream()
                    .filter(p -> p.getUuid().equals(userUUID)).findAny().orElseThrow();
            final var bot = intel.getPlayers().stream()
                    .filter(p -> !p.getUuid().equals(userUUID)).findAny().orElseThrow();

            System.out.println(" Placar: " + user.getUsername() + " " + user.getScore() + " x "
                    + bot.getScore() + " " + bot.getUsername());

            System.out.println(" Vez do (a): " + intel.getCurrentPlayerUsername());
        }
        System.out.println(" Pontos da mão: " + intel.getHandPoints());
    }

    private void printRounds(IntelResponseModel intel) {
        final var roundWinners = intel.getRoundWinnersUsernames();
        if (roundWinners.size() > 0) {
            final var roundResults = roundWinners.stream()
                    .map(possibleWinner -> possibleWinner.orElse("Empate"))
                    .collect(Collectors.joining(" | ", "[ ", " ] "));
            System.out.println(" Ganhadores das Rodadas: " + roundResults);
        }
    }

    private void printCardsOpenInTable(IntelResponseModel intel) {
        final var openCards = intel.getOpenCards();
        if (openCards.size() > 0) {
            System.out.print(" Cartas na mesa: ");
            openCards.forEach(card -> System.out.print(card + " "));
            System.out.print("\n");
        }
    }

    private void printVira(Card vira) {
        System.out.println(" Vira: " + vira);
    }

    private boolean isUserTurn(IntelResponseModel intel) {
        return userUUID.equals(intel.getCurrentPlayerUuid());
    }

    private void printCardToPlayAgainst(IntelResponseModel intel) {
        final var cardToPlayAgainst = intel.getCardToPlayAgainst();
        if(cardToPlayAgainst != null) System.out.println(" Carta do Oponente: " + cardToPlayAgainst);
    }

    private void printOwnedCards() {
        System.out.print(" Cartas na mão: ");
        for (int i = 0; i < userCards.size(); i++) {
            System.out.print((i + 1) + ") " + userCards.get(i) + "\t");
        }
        System.out.print("\n");
    }

    private void printResultIfAvailable(IntelResponseModel intel) {
        final var possibleWinner = intel.getHandWinner();
        if (possibleWinner == null) return;
        final String resultString = possibleWinner.toUpperCase().concat(" VENCEU!");
        System.out.println(" RESULTADO: " + resultString);
    }
}
