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

package com.bueno.domain.usecases.hand.converter;

import com.bueno.domain.entities.hand.Round;
import com.bueno.domain.usecases.game.converter.PlayerConverter;
import com.bueno.domain.usecases.hand.dtos.RoundDto;
import com.bueno.domain.usecases.intel.converters.CardConverter;

public class RoundConverter {

    private RoundConverter(){}

    public static RoundDto toDto(Round round){
        if(round == null) return null;
        return new RoundDto(
                PlayerConverter.toDto(round.getFirstToPlay()),
                PlayerConverter.toDto(round.getLastToPlay()),
                PlayerConverter.toDto(round.getWinner().orElse(null)),
                CardConverter.toDto(round.getVira()),
                CardConverter.toDto(round.getFirstCard()),
                CardConverter.toDto(round.getLastCard())
        );
    }

    public static Round fromDto(RoundDto dto){
        if(dto == null) return null;
        return new Round(
                PlayerConverter.fromDto(dto.firstToPlay()),
                CardConverter.fromDto(dto.firstCard()),
                PlayerConverter.fromDto(dto.lastToPlay()),
                CardConverter.fromDto(dto.lastCard()),
                CardConverter.fromDto(dto.vira()),
                PlayerConverter.fromDto(dto.winner())
        );
    }
}
