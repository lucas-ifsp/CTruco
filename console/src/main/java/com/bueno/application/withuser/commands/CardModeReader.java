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

import java.util.Scanner;

import static com.bueno.application.withuser.commands.CardModeReader.CardMode.*;
import static com.bueno.application.withuser.commands.CardModeReader.CardMode.DISCARDED;

public class CardModeReader implements Command<CardModeReader.CardMode> {

    public enum CardMode{OPEN, DISCARDED}

    public CardModeReader() {
    }

    @Override
    public CardMode execute() {
        var scanner = new Scanner(System.in);
        while (true){
            System.out.print("Descartar [s, n] > ");

            final var choice = scanner.nextLine();
            if (isValidChoice(choice, "s", "n")) {
                printErrorMessage("Valor inválido!");
                continue;
            }
            if (choice.equalsIgnoreCase("n")) return OPEN;
            return DISCARDED;
        }
    }
}

