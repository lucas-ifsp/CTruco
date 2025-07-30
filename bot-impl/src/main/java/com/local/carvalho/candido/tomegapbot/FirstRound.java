package com.local.carvalho.candido.tomegapbot;
/*
 *  Copyright (C) 2024 Matheus H. S. Carvalho and Pedro C. Salvio - IFSP/SCL
 *  Contact: souza <dot> carvalho1 <at> ifsp <dot> edu <dot> br or
 *  p <dot> candido <at> ifsp <dot> edu <dot> br
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
 *  along with CTruco. If not, see <https://www.gnu.org/licenses/>
 */
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.Objects;

public class FirstRound implements Strategy{
    private TrucoCard vira;
    private TrucoCard bestCard;
    private TrucoCard worstCard;

    public FirstRound(){

    }

    private TrucoCard setBestCard(GameIntel intel){
        vira = intel.getVira();
        this.bestCard = intel.getCards().get(0);
        for (TrucoCard card : intel.getCards()) {
            if(card.relativeValue(vira) > bestCard.relativeValue(vira)) bestCard = card;
        }

        return bestCard;
    }
    private TrucoCard setWorstCard(GameIntel intel){
        vira = intel.getVira();
        this.worstCard = intel.getCards().get(0);
        for (TrucoCard card : intel.getCards()) {
            if(card.relativeValue(vira) < worstCard.relativeValue(vira)) worstCard = card;
        }

        return worstCard;
    }

    private TrucoCard setAverageCard(GameIntel intel){
        vira = intel.getVira();
        TrucoCard averageCard = intel.getCards().get(0);
        for (TrucoCard card : intel.getCards()) {
            if(!Objects.equals(bestCard, card) && !Objects.equals(worstCard, card)) averageCard = card;
        }

        return averageCard;
    }
    private boolean tiePeRound(GameIntel intel){
        if (intel.getOpponentCard().isPresent()){
            return intel.getCards().stream().anyMatch(card -> card.compareValueTo(intel.getOpponentCard().get(), vira) == 0);
        }
        return false;
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        vira = intel.getVira();
        boolean isPe = intel.getOpponentCard().isPresent();

        TrucoCard bestCard = setBestCard(intel);
        TrucoCard averageCard = setAverageCard(intel);
        TrucoCard worstCard = setWorstCard(intel);

        long countManilha = intel.getCards().stream()
                .filter(card -> card.isManilha(vira))
                .count();

        boolean hasZap = intel.getCards().stream().anyMatch(card -> card.isZap(vira));
        boolean hasCopas = intel.getCards().stream().anyMatch(card -> card.isCopas(vira));
        boolean hasOuros = intel.getCards().stream().anyMatch(card -> card.isOuros(vira));

        if(hasZap && hasCopas) return CardToPlay.of(worstCard);

        if(isPe){
            if (tiePeRound(intel) && (hasZap || hasCopas)){
                if (worstCard.compareValueTo(intel.getOpponentCard().get(), vira) == 0){return CardToPlay.of(worstCard);}
                if (averageCard.compareValueTo(intel.getOpponentCard().get(), vira) == 0){return CardToPlay.of(averageCard);}
            }

            if(worstCard.compareValueTo(intel.getOpponentCard().get(), vira) > 0) return CardToPlay.of(worstCard);
            if(averageCard.compareValueTo(intel.getOpponentCard().get(), vira) > 0) return CardToPlay.of(averageCard);
            if (bestCard.compareValueTo(intel.getOpponentCard().get(), vira) > 0) return CardToPlay.of(bestCard);
            return CardToPlay.of(worstCard);
        }
        if (countManilha >= 1 && intel.getCards().stream().anyMatch(card -> card.relativeValue(vira) == 9)) return CardToPlay.of(averageCard);
        if(hasOuros){
            TrucoCard ourosCard = intel.getCards().stream().findAny().filter(card -> card.isOuros(vira)).orElse(bestCard);
            return CardToPlay.of(ourosCard);
        }
        return CardToPlay.of(bestCard);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        vira = intel.getVira();
        int handQuality = -2;
        int handPoints = intel.getCards().stream().mapToInt(card -> card.relativeValue(vira)).sum();

        if(handPoints > 22){
            handQuality = 1;
        } else if (handPoints > 15) {
            handQuality = 0;
        } else if (handPoints > 3) {
            handQuality = -1;
        }
        // manobra contra engraçadinhos
        if (handQuality >= 0) return 0;
        return -1;
    }
}
