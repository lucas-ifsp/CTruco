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

public class MaoDeOnzeResponseReader implements Command<MaoDeOnzeResponseReader.MaoDeOnzeChoice> {

    private final GameCLI mainCli;
    public enum MaoDeOnzeChoice {QUIT, ACCEPT}

    public MaoDeOnzeResponseReader(GameCLI mainCli) {
        this.mainCli = mainCli;
    }

    @Override
    public MaoDeOnzeChoice execute() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            mainCli.printGameIntel(0);
            System.out.print("O jogo está em mão de onze. Você aceita [s, n]: ");
            final String choice = scanner.nextLine().toLowerCase();

            if (isValidChoice(choice, "s", "n")) {
                printErrorMessage("Valor inválido!");
                continue;
            }
            return choice.equalsIgnoreCase("s") ? MaoDeOnzeChoice.ACCEPT : MaoDeOnzeChoice.QUIT;
        }
    }
}
