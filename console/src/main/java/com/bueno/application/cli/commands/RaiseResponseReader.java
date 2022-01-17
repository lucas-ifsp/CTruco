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

import com.bueno.application.cli.GameCLI;

import java.util.Scanner;

public class RaiseResponseReader implements Command<RaiseResponseReader.RaiseResponseChoice> {
    private final GameCLI mainCli;
    private final int nextScore;
    public enum RaiseResponseChoice {QUIT, ACCEPT, RAISE}

    public RaiseResponseReader(GameCLI mainCli, int nextScore) {
        this.mainCli = mainCli;
        this.nextScore = nextScore;
    }

    @Override
    public RaiseResponseChoice execute() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(mainCli.getOpponentUsername() + " está pedindo " + toRequestString(nextScore)
                    + ". Escolha uma opção [(T)opa, (C)orre, (A)umenta]: ");

            final String choice = scanner.nextLine();
            if (isValidChoice(choice, "t", "c", "a")) {
                printErrorMessage("Valor inválido!");
                continue;
            }

            return switch (choice){
                case "c" -> RaiseResponseChoice.QUIT;
                case "t" -> RaiseResponseChoice.ACCEPT;
                case "a" -> RaiseResponseChoice.RAISE;
                default -> throw new IllegalStateException("Unexpected value: " + choice);
            };
        }
    }
}
