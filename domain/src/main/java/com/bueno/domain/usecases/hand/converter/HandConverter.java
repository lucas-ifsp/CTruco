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

import com.bueno.domain.entities.hand.Hand;
import com.bueno.domain.entities.hand.HandResult;
import com.bueno.domain.entities.intel.PossibleAction;
import com.bueno.domain.usecases.game.converter.PlayerConverter;
import com.bueno.domain.usecases.hand.dtos.HandDto;
import com.bueno.domain.usecases.intel.converters.CardConverter;
import com.bueno.domain.usecases.intel.converters.IntelConverter;

import java.util.stream.Collectors;

public class HandConverter {

    private HandConverter(){}

    public static HandDto toDto(Hand hand){
        if(hand == null) return  null;
        return new HandDto(
                CardConverter.toDto(hand.getVira()),
                hand.getDealtCards().stream().map(CardConverter::toDto).toList(),
                hand.getOpenCards().stream().map(CardConverter::toDto).toList(),
                hand.getRoundsPlayed().stream().map(RoundConverter::toDto).toList(),
                hand.getIntelHistory().stream().map(IntelConverter::toDto).toList(),
                hand.getPossibleActions().stream().map(PossibleAction::toString).collect(Collectors.toSet()),
                PlayerConverter.toDto(hand.getFirstToPlay()),
                PlayerConverter.toDto(hand.getLastToPlay()),
                PlayerConverter.toDto(hand.getCurrentPlayer()),
                PlayerConverter.toDto(hand.getLastBetRaiser()),
                PlayerConverter.toDto(hand.getEventPlayer()),
                hand.getCardToPlayAgainst().map(CardConverter::toDto).orElse(null),
                hand.getPoints().get(),
                hand.getPointsProposal() != null ? hand.getPointsProposal().get() : 0,
                PlayerConverter.toDto(hand.getResult().flatMap(HandResult::getWinner).orElse(null)),
                hand.getState().toString()
        );
    }
}
