/*
 *  Copyright (C) 2023 João Pedro da Silva and Renan Brufato
 *  Contact: jps <dot> spj <at> gmail <dot> com 
 *  Contact: brufato17 <dot> renan <at> gmail <dot> com
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
package com.silvabrufato.impl.silvabrufatobot;

public class BotBluff {

    public enum Probability {
        P20, P40, P60, P80;
    }

    private Probability bluffProbability;

    private BotBluff(Probability bluffProbability) {
        this.bluffProbability = bluffProbability;
    }

    public static BotBluff of(Probability bluffProbability) {
        return new BotBluff(bluffProbability);
    }

    public boolean bluff() {
        int roundNumber = (int) Math.floor(Math.random() * 10);
        return switch (this.bluffProbability) {
            case P20 -> roundNumber > 8;
            case P40 -> roundNumber > 6;
            case P60 -> roundNumber > 4;
            case P80 -> roundNumber > 2;
            default -> false;      
        };
    }
    
}
