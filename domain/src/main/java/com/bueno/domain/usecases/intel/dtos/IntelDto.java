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

import java.time.Instant;
import java.util.*;

public record IntelDto(Instant timestamp, boolean isGameDone, UUID gameWinner, boolean isMaoDeOnze,
                       Integer handPoints, Integer handPointsProposal,
                       List<Optional<String>> roundWinnersUsernames,
                       List<Optional<UUID>> roundWinnersUuid, int roundsPlayed,
                       CardDto vira,
                       List<CardDto> openCards,
                       String handWinner, UUID currentPlayerUuid, int currentPlayerScore,
                       String currentPlayerUsername, int currentOpponentScore,
                       String currentOpponentUsername,
                       CardDto cardToPlayAgainst,
                       List<PlayerInfo> players,
                       String event, UUID eventPlayerUUID, String eventPlayerUsername,
                       Set<String> possibleActions) {

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

    public record PlayerInfo(UUID uuid, String username, int score, List<CardDto> cards) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PlayerInfo that = (PlayerInfo) o;
            return uuid.equals(that.uuid);
        }

        @Override
        public int hashCode() {
            return Objects.hash(uuid);
        }
    }
}
