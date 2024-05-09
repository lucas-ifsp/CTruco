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

import com.bueno.application.withuser.PlayAgainstBots;
import com.bueno.application.utils.Command;

import java.util.Scanner;

public class RaiseRequestReader implements Command<RaiseRequestReader.RaiseChoice> {

    private final PlayAgainstBots mainCli;
    private final int nextScore;
    public enum RaiseChoice {REQUEST, NOT_REQUEST}

    public RaiseRequestReader(PlayAgainstBots mainCli, int nextScore) {
        this.mainCli = mainCli;
        this.nextScore = nextScore;
    }

    @Override
    public RaiseChoice execute() {
        var scanner = new Scanner(System.in);
        while (true) {
            mainCli.printGameIntel(3000);

            System.out.print("Pedir " + toRequestString(nextScore) + " [s, n]: ");

            var choice = scanner.nextLine().toLowerCase();
            if (isValidChoice(choice, "s", "n")) {
                printErrorMessage("Valor inválido!");
                continue;
            }
            return choice.equalsIgnoreCase("s") ? RaiseChoice.REQUEST : RaiseChoice.NOT_REQUEST;
        }
    }
}
