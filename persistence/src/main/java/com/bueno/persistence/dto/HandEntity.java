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

import com.bueno.domain.usecases.hand.dtos.HandDto;
import com.bueno.domain.usecases.intel.dtos.CardDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HandEntity {
    private String vira;
    private List<String> dealtCard;
    private List<String> openCards;
    private List<RoundEntity> roundsPlayed;
    private List<IntelEntity> history;
    private Set<String> possibleActions;
    private UUID firstToPlay;
    private UUID lastToPlay;
    private UUID currentPlayer;
    private UUID lastBetRaiser;
    private UUID eventPlayer;
    private String cartToPlayAgainst;
    private int points;
    private int pointsProposal;
    private UUID winner;
    private String state;

    public static HandEntity from(HandDto dto){
        final Function<List<CardDto>, List<String>> mapToString = dtos -> dtos.stream().map(CardDto::toString).toList();
        final List<RoundEntity> roundEntities = dto.roundsPlayed().stream().map(RoundEntity::from).toList();
        final List<IntelEntity> history = dto.history().stream().map(IntelEntity::from).toList();
        return HandEntity.builder()
                .vira(dto.vira().toString())
                .dealtCard(mapToString.apply(dto.dealtCard()))
                .openCards(mapToString.apply(dto.openCards()))
                .roundsPlayed(roundEntities)
                .history(history)
                .possibleActions(dto.possibleActions())
                .firstToPlay(dto.firstToPlay())
                .lastToPlay(dto.lastToPlay())
                .currentPlayer(dto.currentPlayer())
                .lastBetRaiser(dto.lastBetRaiser())
                .eventPlayer(dto.lastBetRaiser())
                .cartToPlayAgainst(dto.cartToPlayAgainst() != null ? dto.cartToPlayAgainst().toString() : null)
                .points(dto.points())
                .pointsProposal(dto.pointsProposal())
                .winner(dto.winner())
                .state(dto.state())
                .build();
    }
}
