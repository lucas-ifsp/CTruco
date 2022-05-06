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

package com.bueno.domain.usecases.intel.converters;

import com.bueno.domain.entities.intel.Intel;
import com.bueno.domain.usecases.intel.dtos.IntelDto;

import java.util.List;
import java.util.stream.Collectors;

public class IntelConverter {
    public static IntelDto of(Intel intel){
        final List<IntelDto.PlayerInfo> playersIntel = intel.players().stream()
                .map(IntelConverter::ofPlayerIntel)
                .collect(Collectors.toList());

        return IntelDto.builder()
                .timestamp(intel.timestamp())
                .isGameDone(intel.isGameDone())
                .gameWinner(intel.gameWinner().orElse(null))
                .isMaoDeOnze(intel.isMaoDeOnze())

                .vira(CardConverter.toEntity(intel.vira()))
                .openCards(intel.openCards().stream().map(CardConverter::toEntity).collect(Collectors.toList()))
                .roundsPlayed(intel.roundsPlayed())
                .roundWinnersUsernames(intel.roundWinnersUsernames())
                .roundWinnersUuid(intel.roundWinnersUuid())
                .handPoints(intel.handPoints())
                .handPointsProposal(intel.pointsProposal().orElse(null))
                .handWinner(intel.handWinner().orElse(null))

                .currentPlayerUuid(intel.currentPlayerUuid().orElse(null))
                .currentPlayerUsername(intel.currentPlayerUsername())
                .currentPlayerScore(intel.currentPlayerScore())
                .currentOpponentUsername(intel.currentOpponentUsername())
                .currentOpponentScore(intel.currentOpponentScore())
                .cardToPlayAgainst(intel.cardToPlayAgainst().map(CardConverter::toEntity).orElse(null))
                .players(playersIntel)

                .event(intel.event().orElse(null))
                .eventPlayerUUID(intel.eventPlayerUuid().orElse(null))
                .eventPlayerUsername(intel.eventPlayerUsername().orElse(null))
                .possibleActions(intel.possibleActions())

                .build();
    }

    private static IntelDto.PlayerInfo ofPlayerIntel(Intel.PlayerIntel playerIntel){
        final var playerCards = playerIntel.getCards().stream()
                .map(CardConverter::toEntity)
                .collect(Collectors.toList());

        return IntelDto.PlayerInfo.builder()
                .username(playerIntel.getUsername())
                .uuid(playerIntel.getUuid())
                .score(playerIntel.getScore())
                .cards(playerCards)
                .build();
    }
}
