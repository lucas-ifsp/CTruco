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

import com.bueno.domain.usecases.intel.dtos.CardDto;
import com.bueno.domain.usecases.intel.dtos.IntelDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IntelEntity {
    private Instant timestamp;
    private boolean isGameDone;
    private UUID gameWinner;
    private boolean isMaoDeOnze;
    private Integer handPoints;
    private Integer handPointsProposal;
    private List<String> roundWinnersUsernames;
    private List<UUID> roundWinnersUuid;
    private int roundsPlayed;
    private String vira;
    private List<String> openCards;
    private String handWinner;
    private UUID currentPlayerUuid;
    private int currentPlayerScore;
    private String currentPlayerUsername;
    private int currentOpponentScore;
    private String currentOpponentUsername;
    private String cardToPlayAgainst;
    private List<PlayerEntity> players;
    private String event;
    private UUID eventPlayerUUID;
    private String eventPlayerUsername;
    private Set<String> possibleActions;

    public static IntelEntity from(IntelDto dto){
        final List<String> winnerNames = dto.roundWinnersUsernames().stream()
                .map(optional -> optional.orElse(null)).toList();
        final List<UUID> winnerUuids = dto.roundWinnersUuid().stream()
                .map(optional -> optional.orElse(null)).toList();

        return IntelEntity.builder()
                .timestamp(dto.timestamp())
                .isGameDone(dto.isGameDone())
                .gameWinner(dto.gameWinner())
                .isMaoDeOnze(dto.isMaoDeOnze())
                .handPoints(dto.handPoints())
                .handPointsProposal(dto.handPointsProposal())
                .roundWinnersUsernames(winnerNames)
                .roundWinnersUuid(winnerUuids)
                .roundsPlayed(dto.roundsPlayed())
                .vira(dto.vira().toString())
                .openCards(dto.openCards().stream().map(CardDto::toString).toList())
                .handWinner(dto.handWinner())
                .currentPlayerUuid(dto.currentPlayerUuid())
                .currentPlayerScore(dto.currentPlayerScore())
                .currentPlayerUsername(dto.currentPlayerUsername())
                .currentOpponentScore(dto.currentOpponentScore())
                .currentOpponentUsername(dto.currentOpponentUsername())
                .cardToPlayAgainst(dto.cardToPlayAgainst() != null ? dto.cardToPlayAgainst().toString() : null)
                .players(dto.players().stream().map(PlayerEntity::from).toList())
                .event(dto.event())
                .eventPlayerUUID(dto.eventPlayerUUID())
                .eventPlayerUsername(dto.eventPlayerUsername())
                .possibleActions(dto.possibleActions())
                .build();
    }
}
