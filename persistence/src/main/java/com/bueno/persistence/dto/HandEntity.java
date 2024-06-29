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
import com.bueno.domain.usecases.hand.dtos.HandDto;
import com.bueno.domain.usecases.hand.dtos.RoundDto;
import com.bueno.domain.usecases.intel.dtos.CardDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
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
    private List<RoundDto> roundsPlayed;
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
        final Function<PlayerDto, UUID> playerUuidOrNull = playerDto -> playerDto != null ? playerDto.uuid() : null;
        final List<RoundDto> roundEntities = dto.roundsPlayed();
        final List<IntelEntity> history = dto.history().stream().map(IntelEntity::from).toList();
        return HandEntity.builder()
                .vira(dto.vira().toString())
                .dealtCard(mapToString.apply(dto.dealtCards()))
                .openCards(mapToString.apply(dto.openCards()))
                .roundsPlayed(roundEntities)
                .history(history)
                .possibleActions(dto.possibleActions())
                .firstToPlay(dto.firstToPlay().uuid())
                .lastToPlay(dto.lastToPlay().uuid())
                .currentPlayer(playerUuidOrNull.apply(dto.currentPlayer()))
                .lastBetRaiser(playerUuidOrNull.apply(dto.lastBetRaiser()))
                .eventPlayer(playerUuidOrNull.apply(dto.eventPlayer()))
                .cartToPlayAgainst(dto.cartToPlayAgainst() != null ? dto.cartToPlayAgainst().toString() : null)
                .points(dto.points())
                .pointsProposal(dto.pointsProposal())
                .winner(dto.winner() != null ? dto.winner().uuid() : null)
                .state(dto.state())
                .build();
    }

    public HandDto toDto(Map<UUID, PlayerDto> players){
        final Function<String, CardDto> toCardDto = card -> card != null?
                new CardDto(card.substring(0, 1), card.substring(1, 2)) : null;
        final Function<UUID, PlayerDto> toPlayerDtoOrNull = uuid -> uuid != null ? players.get(uuid) : null;
        return new HandDto(
                toCardDto.apply(vira),
                dealtCard.stream().map(toCardDto).toList(),
                openCards.stream().map(toCardDto).toList(),
                roundsPlayed,
                history.stream().map(IntelEntity::toDto).toList(),
                possibleActions,
                players.get(firstToPlay),
                players.get(lastToPlay),
                toPlayerDtoOrNull.apply(currentPlayer),
                toPlayerDtoOrNull.apply(lastBetRaiser),
                toPlayerDtoOrNull.apply(eventPlayer),
                toCardDto.apply(cartToPlayAgainst),
                points,
                pointsProposal,
                toPlayerDtoOrNull.apply(winner),
                state);
    }
}
