/*
 *  Copyright (C) 2023 Mauricio Brito Teixeira - IFSP/SCL and Vinicius Eduardo Alves Macena - IFSP/SCL
 *  Contact: brito <dot> mauricio <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: macena <dot> v <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.local.brito.macena.boteco.intel.profiles;

import com.local.brito.macena.boteco.utils.Status;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.local.brito.macena.boteco.interfaces.ProfileBot;
import com.local.brito.macena.boteco.utils.Game;
import com.local.brito.macena.boteco.utils.MyHand;

import java.util.List;
import java.util.Optional;


public class Passive extends ProfileBot {
    private final GameIntel intel;
    private final Status status;
    private final TrucoCard vira;
    private final TrucoCard bestCard;
    private final TrucoCard secondBestCard;
    private final TrucoCard worstCard;

    public Passive(GameIntel intel, Status status) {
        this.intel = intel;
        this.status = status;
        vira = intel.getVira();

        MyHand myHand = new MyHand(intel.getCards(), intel.getVira());

        bestCard = myHand.getBestCard();
        secondBestCard = myHand.getSecondBestCard();
        worstCard = myHand.getWorstCard();
    }

    @Override
    public CardToPlay firstRoundChoose() {

        if (intel.getOpponentCard().isPresent()) {
            int opponentCardOnTableValue = intel.getOpponentCard().get().relativeValue(vira);

            if (worstCard.relativeValue(vira) >= opponentCardOnTableValue) return CardToPlay.of(worstCard);
            if (secondBestCard.relativeValue(vira) >= opponentCardOnTableValue) return CardToPlay.of(secondBestCard);
            if (bestCard.relativeValue(vira) > opponentCardOnTableValue) return CardToPlay.of(bestCard);
        }

        if (haveAtLeastTwoManilhas()) {
            if (secondBestCard.relativeValue(vira) == 10) return CardToPlay.of(secondBestCard);
            if (secondBestCard.relativeValue(vira) >= 11) return CardToPlay.of(worstCard);
        }

        if (haveAtLeastOneManilha()) {
            if (bestCard.relativeValue(vira) >= 11) return CardToPlay.of(secondBestCard);
            if (bestCard.relativeValue(vira) == 10) return CardToPlay.of(bestCard);

        }
        long handPower = powerOfTheTwoBestCards();

        if (handPower >= 15 && secondBestCard.relativeValue(vira) >= 7) return CardToPlay.of(secondBestCard);
        return CardToPlay.of(bestCard);
    }

    @Override
    public CardToPlay secondRoundChoose() {
        if (Game.wonFirstRound(intel)) {
            if (status == Status.EXCELLENT) return CardToPlay.of(worstCard);
            return CardToPlay.of(bestCard);
        }

        if (Game.lostFirstRound(intel)) {
            Optional<TrucoCard> oppCard = intel.getOpponentCard();
            if (oppCard.isPresent()) {
                if (worstCard.compareValueTo(oppCard.get(), vira) > 0) return CardToPlay.of(worstCard);
            }
            return CardToPlay.of(bestCard);
        }

        return CardToPlay.of(bestCard);
    }

    @Override
    public CardToPlay thirdRoundChoose() {
        return CardToPlay.of(bestCard);
    }

    private boolean haveAtLeastTwoManilhas() {
        return getManilhaAmount() >= 2;
    }

    private boolean haveAtLeastOneManilha() {
        return getManilhaAmount() >= 1;
    }

    private long getManilhaAmount() {
        List<TrucoCard> myCards = intel.getCards();
        return myCards.stream()
                .filter(card -> card.isManilha(intel.getVira()))
                .count();
    }

    private long powerOfTheTwoBestCards() {
        List<TrucoCard> myCards = intel.getCards();
        return myCards.stream()
                .mapToLong(card -> card.relativeValue(intel.getVira()))
                .sorted()
                .limit(2)
                .sum();
    }
}
