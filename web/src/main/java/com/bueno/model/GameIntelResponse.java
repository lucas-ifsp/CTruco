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

package com.bueno.model;

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.intel.Intel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Builder
@ToString
public class GameIntelResponse {
    private Instant timestamp;

    private boolean isGameDone;
    private UUID gameWinner;
    private boolean isMaoDeOnze;

    private Integer handPoints;
    private Integer handPointsProposal;
    private List<Optional<String>> roundWinnersUsernames;
    private List<Optional<UUID>> roundWinnersUuid;
    private int roundsPlayed;
    private Card vira;
    private List<Card> openCards;
    private String handWinner;

    private UUID currentPlayerUuid;
    private int currentPlayerScore;
    private String currentPlayerUsername;
    private int currentOpponentScore;
    private String currentOpponentUsername;
    private Card cardToPlayAgainst;
    private List<PlayerIntel> players;

    private String event;
    private UUID eventPlayerUUID;
    private String eventPlayerUsername;
    private Set<String> possibleActions;

    public static GameIntelResponse of(Intel intel){
        final List<PlayerIntel> playersIntel = intel.players().stream()
                .map(GameIntelResponse::toPlayerIntel)
                .collect(Collectors.toList());

        return GameIntelResponse.builder()
                .timestamp(intel.timestamp())
                .isGameDone(intel.isGameDone())
                .gameWinner(intel.gameWinner().orElse(null))
                .isMaoDeOnze(intel.isMaoDeOnze())

                .vira(intel.vira())
                .openCards(intel.openCards())
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
                .cardToPlayAgainst(intel.cardToPlayAgainst().orElse(null))
                .players(playersIntel)

                .event(intel.event().orElse(null))
                .eventPlayerUUID(intel.eventPlayerUuid().orElse(null))
                .eventPlayerUsername(intel.eventPlayerUsername().orElse(null))
                .possibleActions(intel.possibleActions())

                .build();
    }

    private static PlayerIntel toPlayerIntel(Intel.PlayerIntel playerIntel){
        return PlayerIntel.builder()
                .username(playerIntel.getUsername())
                .uuid(playerIntel.getUuid())
                .score(playerIntel.getScore())
                .cards(List.copyOf(playerIntel.getCards()))
                .build();
    }
}
