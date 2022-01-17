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
import com.bueno.domain.entities.game.Intel;
import com.bueno.domain.entities.player.util.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class IntelPrinter implements Command<Void>{

    private final List<Intel> history;
    private final int delayInMilliseconds;
    private final Player player;

    public IntelPrinter(Player player, List<Intel> history, int delayInMilliseconds) {
        this.player = player;
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

    private void print(Intel intel) {
        clearAfter(delayInMilliseconds);
        printDelimiter(false);
        printGameMainInfo(intel);
        printRounds(intel);
        printCardsOpenInTable(intel);
        printVira(intel.vira());
        if(isPlayerTurn(intel)) {
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

    private void printGameMainInfo(Intel intel) {
        if(intel.currentPlayerUuid().isPresent()) {
            final String botUserName = !intel.currentPlayerUsername().equals(player.getUsername()) ?
                    intel.currentPlayerUsername() : intel.currentOpponentUsername();
            final int botScore = intel.currentPlayerScore() != player.getScore() ?
                    intel.currentPlayerScore() : intel.currentOpponentScore();
            System.out.println(" Placar: " + player.getUsername() + " " + player.getScore()
                    + " x " + botScore + " " + botUserName);
            System.out.println(" Vez do (a): " + intel.currentPlayerUsername());
        }
        System.out.println(" Ponto da mão: " + intel.handScore());
    }

    private void printRounds(Intel intel) {
        final List<Optional<String>> roundWinners = intel.roundWinners();
        if (roundWinners.size() > 0) {
            final String roundResults = roundWinners.stream()
                    .map(possibleWinner -> possibleWinner.orElse("Empate"))
                    .collect(Collectors.joining(" | ", "[ ", " ] "));
            System.out.println(" Ganhadores das Rodadas: " + roundResults);
        }
    }

    private void printCardsOpenInTable(Intel intel) {
        final List<Card> openCards = intel.openCards();
        if (openCards.size() > 0) {
            System.out.print(" Cartas na mesa: ");
            openCards.forEach(card -> System.out.print(card + " "));
            System.out.print("\n");
        }
    }

    private void printVira(Card vira) {
        System.out.println(" Vira: " + vira);
    }

    private boolean isPlayerTurn(Intel intel) {
        return intel.currentPlayerUuid().filter(uuid -> uuid.equals(player.getUuid())).isPresent();
    }

    private void printCardToPlayAgainst(Intel intel) {
        final Optional<Card> cardToPlayAgainst = intel.cardToPlayAgainst();
        cardToPlayAgainst.ifPresent(card -> System.out.println(" Carta do Oponente: " + card));
    }

    private void printOwnedCards() {
        System.out.print(" Cartas na mão: ");
        final List<Card> playerCards = player.getCards();
        for (int i = 0; i < playerCards.size(); i++) {
            System.out.print((i + 1) + ") " + playerCards.get(i) + "\t");
        }
        System.out.print("\n");
    }

    private void printResultIfAvailable(Intel intel) {
        final var possibleWinner = intel.handWinner();
        if (possibleWinner.isPresent()) {
            final String resultString = possibleWinner
                    .map(UUID::toString)
                    .map(String::toUpperCase)
                    .map(name -> name.concat(" VENCEU!"))
                    .orElse("EMPATE.");
            System.out.println(" RESULTADO: " + resultString);
        }
    }
}
