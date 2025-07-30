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

import com.bueno.domain.usecases.game.dtos.GameDto;
import com.bueno.domain.usecases.game.dtos.PlayerDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameEntity {
    private UUID id;
    private LocalDateTime timestamp;
    private UUID player1;
    private UUID player2;
    private UUID firstToPlay;
    private UUID lastToPlay;
    private List<HandEntity> hands;

    public static GameEntity from(GameDto dto) {
        return GameEntity.builder()
                .id(dto.gameUuid())
                .timestamp(dto.timestamp())
                .player1(dto.player1().uuid())
                .player2(dto.player2().uuid())
                .firstToPlay(dto.firstToPlay().uuid())
                .lastToPlay(dto.lastToPlay().uuid())
                .hands(dto.hands().stream().map(HandEntity::from).toList())
                .build();
    }

    public GameDto toDto(Map<UUID, PlayerDto> players) {
        return new GameDto(
                id,
                timestamp,
                players.get(player1),
                players.get(player2),
                players.get(firstToPlay),
                players.get(lastToPlay),
                hands.stream().map(hand -> hand.toDto(players)).toList()
        );
    }
}
