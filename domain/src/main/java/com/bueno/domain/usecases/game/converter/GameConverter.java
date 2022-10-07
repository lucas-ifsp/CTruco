/*
 *  Copyright (C) 2022 Lucas B. R. de Oliveira - IFSP/SCL
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

package com.bueno.domain.usecases.game.converter;

import com.bueno.domain.entities.game.Game;
import com.bueno.domain.usecases.game.dtos.GameDto;
import com.bueno.domain.usecases.hand.converter.HandConverter;

public class GameConverter {

    private GameConverter(){}

    public static GameDto toDto(Game game){
        if(game == null) return null;
        return new GameDto(
                game.getUuid(),
                game.getTimestamp(),
                PlayerConverter.toDto(game.getPlayer1()),
                PlayerConverter.toDto(game.getPlayer2()),
                PlayerConverter.toDto(game.getFirstToPlay()),
                PlayerConverter.toDto(game.getLastToPlay()),
                game.getHands().stream().map(HandConverter::toDto).toList()
        );
    }
}
