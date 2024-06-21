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
import com.bueno.domain.usecases.intel.dtos.CardDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerEntity {
    private UUID id;
    private String username;
    private int score;
    private boolean isBot;
    private List<String> cards;

    public static PlayerEntity from(PlayerDto dto) {
        return PlayerEntity.builder()
                .id(dto.uuid())
                .username(dto.username())
                .score(dto.score())
                .isBot(dto.isBot())
                .cards(dto.cards().stream().map(CardDto::toString).toList())
                .build();
    }

    public PlayerDto toDto() {
        final List<CardDto> cardDtos = cards.stream()
                .map(card -> new CardDto(card.substring(0, 1), card.substring(1, 2)))
                .toList();
        return new PlayerDto(username, id, score, isBot, cardDtos);
    }
}
