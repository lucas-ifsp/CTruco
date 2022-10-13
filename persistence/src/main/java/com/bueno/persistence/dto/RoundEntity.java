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

package com.bueno.persistence.dto;

import com.bueno.domain.usecases.game.dtos.PlayerDto;
import com.bueno.domain.usecases.hand.dtos.RoundDto;
import com.bueno.domain.usecases.intel.dtos.CardDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoundEntity {
    private UUID firstToPlay;
    private UUID lastToPlay;
    private UUID winner;
    private String vira;
    private String firstCard;
    private String lastCard;

    public static RoundEntity from(RoundDto dto){
        return RoundEntity.builder()
                .firstToPlay(dto.firstToPlay().uuid())
                .lastToPlay(dto.lastToPlay().uuid())
                .winner(dto.winner() != null ? dto.winner().uuid() : null)
                .vira(dto.vira().toString())
                .firstCard(dto.firstCard().toString())
                .lastCard(dto.lastCard().toString())
                .build();
    }

    public RoundDto toDto(Map<UUID, PlayerDto> players){
         final Function<String, CardDto> toCardDto = card -> card != null?
                new CardDto(card.substring(0, 1), card.substring(1, 2)) : null;
        final Function<UUID, PlayerDto> toPlayerDtoOrNull = uuid -> uuid != null ? players.get(uuid) : null;

        return new RoundDto(
                players.get(firstToPlay),
                players.get(lastToPlay),
                toPlayerDtoOrNull.apply(winner),
                toCardDto.apply(vira),
                toCardDto.apply(firstCard),
                toCardDto.apply(lastCard));
    }
}
