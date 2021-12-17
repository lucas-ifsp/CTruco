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

import com.bueno.application.cli.PlayCLI2;
import com.bueno.domain.entities.deck.Card;
import com.google.common.primitives.Ints;

import java.util.List;
import java.util.Scanner;

public class CardReader implements Command<Card> {

    private final PlayCLI2 mainCli;
    private final List<Card> userCards;

    public CardReader(PlayCLI2 mainCli, List<Card> userCards) {
        this.mainCli = mainCli;
        this.userCards = userCards;
    }

    @Override
    public Card execute() {
        while (true){
            cls();
            mainCli.printGameIntel();

            Scanner scanner = new Scanner(System.in);
            System.out.print("Carta a jogar [índice] > ");

            final Integer choice = Ints.tryParse(scanner.nextLine());
            if(choice == null) continue;
            final int cardIndex = choice - 1;

            if (cardIndex < 0 || cardIndex > userCards.size() - 1) {
                printErrorMessage("Valor inválido!");
                continue;
            }
            return userCards.get(cardIndex);
        }
    }
}
