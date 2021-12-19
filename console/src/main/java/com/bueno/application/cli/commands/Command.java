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

import com.bueno.domain.entities.game.GameRuleViolationException;
import com.bueno.domain.entities.game.HandScore;

import java.util.Arrays;
import java.util.Scanner;

public interface Command <T>{

    T execute();

    default void printErrorMessage(String message) {
        Scanner scanner = new Scanner(System.in);
        cls();
        System.out.println(message);
        scanner.nextLine();
        cls();
    }

    default void cls() {
        for (int i = 0; i < 15; ++i) System.out.println();
    }

    default boolean isValidChoice(String choice, String... options) {
        return Arrays.stream(options).noneMatch(choice::equalsIgnoreCase);
    }

    default String toRequestString(HandScore nextScore) {
        return switch (nextScore) {
            case THREE -> "truco";
            case SIX -> "seis";
            case NINE -> "nove";
            case TWELVE -> "doze";
            default -> throw new GameRuleViolationException("Invalid hand value!");
        };
    }
}
