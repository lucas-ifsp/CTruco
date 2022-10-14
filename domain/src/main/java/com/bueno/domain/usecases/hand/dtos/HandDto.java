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

import com.bueno.domain.usecases.game.dtos.PlayerDto;
import com.bueno.domain.usecases.intel.dtos.CardDto;
import com.bueno.domain.usecases.intel.dtos.IntelDto;

import java.util.List;
import java.util.Set;

public record HandDto(CardDto vira, List<CardDto> dealtCards, List<CardDto> openCards,
                      List<RoundDto> roundsPlayed, List<IntelDto> history, Set<String> possibleActions,
                      PlayerDto firstToPlay, PlayerDto lastToPlay, PlayerDto currentPlayer, PlayerDto lastBetRaiser, PlayerDto eventPlayer,
                      CardDto cartToPlayAgainst, int points, int pointsProposal, PlayerDto winner, String state) {
}