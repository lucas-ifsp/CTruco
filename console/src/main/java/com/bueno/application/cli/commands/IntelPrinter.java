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
import com.bueno.domain.entities.game.HandResult;
import com.bueno.domain.entities.game.Intel;
import com.bueno.domain.entities.player.util.Player;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class IntelPrinter implements Command<Void>{

    private final Intel intel;
    private final int delayInMilliseconds;
    private final Player player;

    public IntelPrinter(Player player, Intel intel, int delayInMilliseconds) {
        this.player = player;
        this.intel = intel;
        this.delayInMilliseconds = delayInMilliseconds;
    }

    @Override
    public Void execute() {
        cls();
        System.out.println("+=======================================+");
        printGameMainInfo();
        printRounds(intel);
        printCardsOpenInTable(intel);
        printVira(intel.vira());
        printOpponentCardIfAvailable(intel);
        printOwnedCards();
        printResultIfAvailable();
        System.out.println("+=======================================+\n");

        if(delayInMilliseconds > 0) {
            try {
                TimeUnit.MILLISECONDS.sleep(delayInMilliseconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void printGameMainInfo() {
        System.out.println(" Vez do: " + player.getUsername());
        System.out.println(" Ponto da mão: " + intel.getHandScore().get());
        System.out.println(" Placar: " + player.getUsername() + " " + player.getScore()
                + " x " + intel.getOpponentUsername(player.getUuid()) + " " + intel.getOpponentScore(player.getUuid()));
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

    private void printOpponentCardIfAvailable(Intel intel) {
        final Optional<Card> cardToPlayAgainst = intel.getCardToPlayAgainst();
        cardToPlayAgainst.ifPresent(card -> System.out.println(" Carta do Oponente: " + card));
    }

    private void printOwnedCards() {
        System.out.print(" Cartas na mão: ");
        final List<Card> playerCards = intel.ownedCards(player.getUuid());
        for (int i = 0; i < playerCards.size(); i++) {
            System.out.print((i + 1) + ") " + playerCards.get(i) + "\t");
        }
        System.out.print("\n");
    }

    private void printResultIfAvailable() {
        final Optional<HandResult> potentialResult = intel.getResult();
        if (potentialResult.isPresent()) {
            final String resultString = potentialResult.get().getWinner()
                    .map(winner -> winner.getUsername().concat(" VENCEU!").toUpperCase())
                    .orElse("EMPATE.");
            System.out.println(" RESULTADO: " + resultString);
        }
    }
}
