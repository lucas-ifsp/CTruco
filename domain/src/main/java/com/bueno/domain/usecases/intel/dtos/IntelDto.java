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

package com.bueno.domain.usecases.intel.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.*;

@Getter
@Builder
@ToString
public final class IntelDto {
    private Instant timestamp;

    private boolean isGameDone;
    private UUID gameWinner;
    private boolean isMaoDeOnze;

    private Integer handPoints;
    private Integer handPointsProposal;
    private List<Optional<String>> roundWinnersUsernames;
    private List<Optional<UUID>> roundWinnersUuid;
    private int roundsPlayed;
    private CardDto vira;
    private List<CardDto> openCards;
    private String handWinner;

    private UUID currentPlayerUuid;
    private int currentPlayerScore;
    private String currentPlayerUsername;
    private int currentOpponentScore;
    private String currentOpponentUsername;
    private CardDto cardToPlayAgainst;
    private List<PlayerInfo> players;

    private String event;
    private UUID eventPlayerUUID;
    private String eventPlayerUsername;
    private Set<String> possibleActions;

    @Builder
    @Getter
    @ToString
    public static class PlayerInfo {
        private final UUID uuid;
        private final String username;
        private final int score;
        private final List<CardDto> cards;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PlayerInfo that = (PlayerInfo) o;
            return uuid.equals(that.uuid) && username.equals(that.username);
        }

        @Override
        public int hashCode() {
            return Objects.hash(uuid, username);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntelDto that = (IntelDto) o;
        return timestamp.equals(that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp);
    }
}
