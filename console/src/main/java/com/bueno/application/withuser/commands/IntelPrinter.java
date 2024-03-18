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

package com.bueno.application.withuser.commands;

import com.bueno.application.utils.Command;
import com.bueno.domain.usecases.intel.dtos.CardDto;
import com.bueno.domain.usecases.intel.dtos.IntelDto;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class IntelPrinter implements Command<Void> {

    private final List<IntelDto> history;
    private final int delayInMilliseconds;
    private final UUID userUUID;
    private final List<CardDto> userCards;

    public IntelPrinter(UUID userUUID, List<CardDto> userCards, List<IntelDto> history, int delayInMilliseconds) {
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

    private void print(IntelDto intel) {
        clearAfter(delayInMilliseconds);
        printDelimiter(false);
        printGameMainInfo(intel);
        printRounds(intel);
        printCardsOpenInTable(intel);
        printVira(intel.vira());
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

    private void printGameMainInfo(IntelDto intel) {
        if(intel.currentPlayerUuid() != null) {
            final var user = intel.players().stream()
                    .filter(p -> p.uuid().equals(userUUID)).findAny().orElseThrow();
            final var bot = intel.players().stream()
                    .filter(p -> !p.uuid().equals(userUUID)).findAny().orElseThrow();

            System.out.println(" Placar: " + user.username() + " " + user.score() + " x "
                    + bot.score() + " " + bot.username());

            System.out.println(" Vez do (a): " + intel.currentPlayerUsername());
        }
        System.out.println(" Pontos da mão: " + intel.handPoints());
    }

    private void printRounds(IntelDto intel) {
        final var roundWinners = intel.roundWinnersUsernames();
        if (roundWinners.size() > 0) {
            final var roundResults = roundWinners.stream()
                    .map(possibleWinner -> possibleWinner.orElse("Empate"))
                    .collect(Collectors.joining(" | ", "[ ", " ] "));
            System.out.println(" Ganhadores das Rodadas: " + roundResults);
        }
    }

    private void printCardsOpenInTable(IntelDto intel) {
        final var openCards = intel.openCards();
        if (openCards.size() > 0) {
            System.out.print(" Cartas na mesa: ");
            openCards.forEach(card -> System.out.print(formatCard(card) + " "));
            System.out.print("\n");
        }
    }

    private void printVira(CardDto vira) {
        System.out.println(" Vira: " + formatCard(vira));
    }

    private boolean isUserTurn(IntelDto intel) {
        return userUUID.equals(intel.currentPlayerUuid());
    }

    private void printCardToPlayAgainst(IntelDto intel) {
        final var cardToPlayAgainst = intel.cardToPlayAgainst();
        if(cardToPlayAgainst != null) System.out.println(" Carta do Oponente: " + formatCard(cardToPlayAgainst));
    }

    private void printOwnedCards() {
        System.out.print(" Cartas na mão: ");
        for (int i = 0; i < userCards.size(); i++) {
            System.out.print((i + 1) + ") " + formatCard(userCards.get(i)) + "\t");
        }
        System.out.print("\n");
    }

    private void printResultIfAvailable(IntelDto intel) {
        final var possibleWinner = intel.handWinner();
        if (possibleWinner == null) return;
        final String resultString = possibleWinner.toUpperCase().concat(" VENCEU!");
        System.out.println(" RESULTADO: " + resultString);
    }

    public String formatCard(CardDto card){
        final String rank = card.rank();
        final String suit = card.suit();
        final String rankSymbol = rank.equals("X") ? ":" : rank;
        final String suitSymbol = switch (suit){
            case "D" -> "\u2666";
            case "S" -> "\u2660";
            case "H" -> "\u2665";
            case "C" -> "\u2663";
            case "X" -> ":";
            default -> throw new IllegalStateException("Invalid card suit: " + suit);
        };
        return String.format("[%s%s]", rankSymbol, suitSymbol);
    }
}
