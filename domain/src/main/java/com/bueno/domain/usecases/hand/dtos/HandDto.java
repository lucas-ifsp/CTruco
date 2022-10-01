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

package com.bueno.domain.usecases.hand.dtos;

import com.bueno.domain.entities.hand.Round;
import com.bueno.domain.usecases.intel.dtos.CardDto;
import com.bueno.domain.usecases.intel.dtos.IntelDto;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public record HandDto(CardDto vira, List<CardDto> dealtCard, List<CardDto> openCards,
                      List<Round> roundsPlayed, List<IntelDto> history, Set<String> possibleActions,
                      UUID firstToPlay, UUID lastToPlay, UUID currentPlayer, UUID lastBetRaiser, UUID eventPlayer,
                      CardDto cartToPlayAgainst, int points, int pointsProposal, UUID winner, String state) {
}