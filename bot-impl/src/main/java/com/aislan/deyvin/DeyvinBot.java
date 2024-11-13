/*
 *  Copyright (C) 2024 Aislan Pepi Rodrigues
 *  Contact: aislan <dot> pepi <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.aislan.deyvin;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;
import java.util.List;

public class DeyvinBot implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        List<TrucoCard> myCards = intel.getCards();
        myCards.sort(TrucoCard::relativeValue);

        TrucoCard worstCard = myCards.get(myCards.indexOf(myCards.get(0)));

        if(myCards.stream().anyMatch(trucoCard -> trucoCard.isManilha(vira))) return true;

        if(getDiffPoints(intel) >= 6)
            if(isGreaterThan(CardRank.QUEEN,worstCard,intel) && myCards.stream().anyMatch(trucoCard -> isGreaterThan(CardRank.THREE,trucoCard,intel))) return true;
        if(myCards.stream().anyMatch(trucoCard -> isGreaterThan(CardRank.THREE,trucoCard,intel)))
            return true;
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        List<TrucoCard> myCards = intel.getCards();
        TrucoCard vira = intel.getVira();
        if(isSecondRound(intel) && isWinnerInFirstRound(intel)) {
            if (myCards.stream().anyMatch(trucoCard -> trucoCard.isZap(vira))) return true;
        }
        if(myCards.stream().anyMatch(trucoCard -> trucoCard.isManilha(vira))){
            if(hasCasalMaior(intel)) return true;
            if(isLastRound(intel) && isWinnerInFirstRound(intel)) return true;
        }

        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {

        List<TrucoCard> myCards = intel.getCards();
        myCards.sort(TrucoCard::relativeValue);

        TrucoCard bestCard;
        if(myCards.size() > 1)
            bestCard = myCards.get(myCards.indexOf(myCards.get(myCards.size() - 1)));
        else
            bestCard = myCards.get(myCards.indexOf(myCards.get(0)));

        TrucoCard worstCard = myCards.get(myCards.indexOf(myCards.get(0)));

        TrucoCard vira = intel.getVira();

        if(isFirstRound(intel)){
            TrucoCard averageCard = myCards.get(1);
            if(myCards.get(myCards.indexOf(bestCard)).isZap(vira))
                return CardToPlay.of(averageCard);
            return CardToPlay.of(bestCard);
        }

        if(isSecondRound(intel)){
            if(isWinnerInFirstRound(intel)) {
                if (myCards.stream().allMatch(trucoCard -> trucoCard.isManilha(vira)))
                    return CardToPlay.of(bestCard);
                if (bestCard.isZap(vira)) return CardToPlay.of(bestCard);
                else return CardToPlay.discard(worstCard);
            }
            if(!isWinnerInFirstRound(intel)) return CardToPlay.of(bestCard);
        }

        return CardToPlay.of(bestCard);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {

        List<TrucoCard> myCards = intel.getCards();
        myCards.sort(TrucoCard::relativeValue);

        TrucoCard vira = intel.getVira();

        if(isWinning(intel))
            if(getDiffPoints(intel) >= 6)
                return 0;

        if(myCards.stream().allMatch(trucoCard -> trucoCard.isManilha(vira))) return 1;

        if(myCards.stream().anyMatch(trucoCard -> trucoCard.isZap(vira)))
            if (myCards.stream().allMatch(trucoCard -> isGreaterThan(CardRank.TWO,trucoCard,intel))) return 1;

        return -1;
    }

    private boolean isGreaterThan(CardRank rank,TrucoCard worstCard, GameIntel intel){
        TrucoCard vira = intel.getVira();
        return worstCard.compareValueTo(TrucoCard.of(rank,CardSuit.DIAMONDS),vira) >= 0;
    }

    private boolean isWinning(GameIntel intel){
        return intel.getScore() - intel.getOpponentScore() > 0;
    }

    private int getDiffPoints(GameIntel intel){
        return intel.getScore() - intel.getOpponentScore();
    }

    private boolean isFirstRound(GameIntel intel){
        return intel.getRoundResults().isEmpty();
    }
    private boolean isSecondRound(GameIntel intel){
        return intel.getRoundResults().size() == 1;
    }

    private boolean isLastRound(GameIntel intel){
        return intel.getRoundResults().size() == 2;
    }

    private boolean hasCasalMaior(GameIntel intel){
        TrucoCard vira = intel.getVira();
        List<TrucoCard> myCards = intel.getCards();
        return (myCards.stream().anyMatch(trucoCard -> trucoCard.isZap(vira))
                && myCards.stream().anyMatch(trucoCard -> trucoCard.isCopas(vira)));
    }

    private boolean isWinnerInFirstRound(GameIntel intel){
        return intel.getRoundResults().contains(GameIntel.RoundResult.WON);
    }
}
