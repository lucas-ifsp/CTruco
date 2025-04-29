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


public class SecondRound implements Strategy{
    private TrucoCard vira;

    public SecondRound(){

    }

    private TrucoCard setBestCard(GameIntel intel){
        vira = intel.getVira();
        TrucoCard bestCard = intel.getCards().get(0);
        for (TrucoCard card : intel.getCards()) {
            if(card.relativeValue(vira) > bestCard.relativeValue(vira)) bestCard = card;
        }

        return bestCard;
    }
    private TrucoCard setWorstCard(GameIntel intel){
        vira = intel.getVira();
        TrucoCard worstCard = intel.getCards().get(0);
        for (TrucoCard card : intel.getCards()) {
            if(card.relativeValue(vira) < worstCard.relativeValue(vira)) worstCard = card;
        }

        return worstCard;
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        vira = intel.getVira();
        GameIntel.RoundResult firstRoundResult = intel.getRoundResults().get(0);

        boolean hasZap = intel.getCards().stream().anyMatch(card -> card.isZap(vira));
        boolean hasCopas = intel.getCards().stream().anyMatch(card -> card.isCopas(vira));
        boolean hasEspadilha = intel.getCards().stream().anyMatch(card -> card.isEspadilha(vira));
        if(intel.getOpponentScore() == 11 || intel.getScore() == 11) return false;


        if(hasZap && hasCopas) return true;
        if (firstRoundResult.equals(GameIntel.RoundResult.DREW) && (hasZap || hasCopas || hasEspadilha)) return true;

        if (firstRoundResult.equals(GameIntel.RoundResult.WON)) return true;
        if (intel.getCards().stream().anyMatch(card -> card.relativeValue(vira) > 6)) return true;
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        vira = intel.getVira();
        boolean isPe = intel.getOpponentCard().isPresent();
        GameIntel.RoundResult firstRoundResult = intel.getRoundResults().get(0);

        TrucoCard bestCard = setBestCard(intel);
        TrucoCard worstCard = setWorstCard(intel);

        if (isPe){
            if ((firstRoundResult.equals(GameIntel.RoundResult.WON))){
                if (worstCard.compareValueTo(intel.getOpponentCard().get(), vira) == 0){return CardToPlay.of(worstCard);}
                if (bestCard.compareValueTo(intel.getOpponentCard().get(), vira) == 0){return CardToPlay.of(bestCard);}
            }
            if(worstCard.compareValueTo(intel.getOpponentCard().get(), vira) > 0) return CardToPlay.of(worstCard);
            if(bestCard.compareValueTo(intel.getOpponentCard().get(), vira) > 0) return CardToPlay.of(bestCard);
            return CardToPlay.discard(worstCard);
        }
        // TORNANDO
        return CardToPlay.of(bestCard);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        vira = intel.getVira();
        GameIntel.RoundResult firstRoundResult = intel.getRoundResults().get(0);
        boolean hasZap = intel.getCards().stream().anyMatch(card -> card.isZap(vira));
        boolean hasCopas = intel.getCards().stream().anyMatch(card -> card.isCopas(vira));

        if (firstRoundResult.equals(GameIntel.RoundResult.DREW) && (hasZap || hasCopas)) return 1;

        if (firstRoundResult.equals(GameIntel.RoundResult.WON) && intel.getCards().stream()
                .anyMatch(card -> card.relativeValue(vira) > 8)) return 0;

        if (firstRoundResult.equals(GameIntel.RoundResult.LOST) && (hasZap && hasCopas)) return 1;
        if (firstRoundResult.equals(GameIntel.RoundResult.LOST) && intel.getCards().stream()
                .anyMatch(card -> card.relativeValue(vira) > 9)) return 0;

        // manobra contra engraçadinhos
        if (firstRoundResult.equals(GameIntel.RoundResult.WON)) return 0;
        if (intel.getCards().stream().anyMatch(card -> card.relativeValue(vira) > 6)) return 0;
        return -1;
    }

}
