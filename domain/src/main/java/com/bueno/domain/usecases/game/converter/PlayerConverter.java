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

import com.bueno.domain.entities.player.Player;
import com.bueno.domain.usecases.game.dtos.PlayerDto;
import com.bueno.domain.usecases.intel.converters.CardConverter;

import java.util.ArrayList;

public class PlayerConverter {

    private PlayerConverter(){}

    public static PlayerDto toDto(Player player){
        if(player == null) return null;
        return  new PlayerDto(
                player.getUsername(),
                player.getUuid(),
                player.getScore(),
                player.isBot(),
                player.getCards().stream().map(CardConverter::toDto).toList());
    }

    public static Player fromDto(PlayerDto dto){
        if(dto == null) return null;
        return new Player(
                new ArrayList<>(dto.cards().stream().map(CardConverter::fromDto).toList()),
                dto.username(),
                dto.uuid(),
                dto.score(),
                dto.isBot());
    }
}
