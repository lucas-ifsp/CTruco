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

package com.bueno.domain.usecases.hand.converter;

import com.bueno.domain.entities.game.Game;
import com.bueno.domain.entities.hand.Hand;
import com.bueno.domain.entities.hand.HandResult;
import com.bueno.domain.entities.hand.Round;
import com.bueno.domain.entities.player.Player;
import com.bueno.domain.usecases.hand.dtos.HandResultDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class HandResultConverter {

    public static HandResultDto of(Game game){
        final String player1Type = game.getPlayer1().isBot() ? "BOT" : "USER";
        final String player2Type = game.getPlayer2().isBot() ? "BOT" : "USER";
        final String gameType = player1Type + "_" + player2Type;
        final Hand hand = game.currentHand();

        final UUID handWinner = hand.getResult()
                .flatMap(HandResult::getWinner)
                .map(Player::getUuid).orElse(null);

        final Function<Optional<Player>, UUID> mapToPlayerUuidOrNull =
                winner -> winner.map(Player::getUuid).orElse(null);

        final List<UUID> roundWinners = hand.getRoundsPlayed().stream()
                .map(Round::getWinner)
                .map(mapToPlayerUuidOrNull)
                .collect(Collectors.toList());

        final List<String> openCards = hand.getOpenCards().stream()
                .map(card -> card.getRank() + "" + card.getSuit())
                .collect(Collectors.toList());

        final int pointsProposal = hand.getPointsProposal() != null ? hand.getPointsProposal().get() : 0;
        return new HandResultDto(gameType, game.getUuid(),
                handWinner, hand.getPoints().get(), pointsProposal, roundWinners, openCards);
    }
}
