/*
 *  Copyright (C) 2024 Luigi A. L. Vanzella
 *  Contact: luigi <dot> vanzella <at> ifsp <dot> edu <dot> br
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

package com.luigivanzella.triathlonBot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;

public interface EstrategiaRound {

    CardToPlay chooseCard(GameIntel intel);

    boolean decideIfRaises(GameIntel intel);

    int getRaiseResponse(GameIntel intel);

    boolean getMaoDeOnzeResponse(GameIntel intel);

}
