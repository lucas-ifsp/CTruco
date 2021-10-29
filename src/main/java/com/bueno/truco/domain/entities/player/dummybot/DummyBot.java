/*
 * Copyright (C) 2021 Lucas B. R. de Oliveira
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
 *  along with Foobar.  If not, see <https://www.gnu.org/licenses/>
 */

package com.bueno.truco.domain.entities.player.dummybot;

import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.hand.HandScore;
import com.bueno.truco.domain.entities.player.util.Player;

public class DummyBot extends Player {

    public DummyBot() {
        super("DummyBot");
    }

    @Override
    public Card playCard() {
        return cards.remove(0);
    }

    @Override
    public boolean requestTruco() {
        return false;
    }

    @Override
    public int getTrucoResponse(HandScore newHandScore) {
        return 0;
    }

    @Override
    public boolean getMaoDeOnzeResponse() {
        return false;
    }


}
