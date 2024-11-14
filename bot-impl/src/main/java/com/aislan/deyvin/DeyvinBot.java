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

        List<TrucoCard> myCards = intel.getCards();
        myCards.sort(TrucoCard::relativeValue);
        TrucoCard worstCard = myCards.get(myCards.indexOf(myCards.get(0)));

        if(countManilhas(intel) >= 2) return true;
        if(hasCasalMaior(intel)) return true;
        if(myCards.stream().allMatch(trucoCard -> isGreaterThan(CardRank.ACE,trucoCard,intel))) return true;

        if(getDiffPoints(intel) > 6)
            if(isGreaterThan(CardRank.QUEEN,worstCard,intel) && myCards.stream().anyMatch(trucoCard -> isGreaterThan(CardRank.TWO,trucoCard,intel)))
                return true;

        return intel.getOpponentScore() == 11;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {

        if(isLastRound(intel) && isWinnerInFirstRound(intel)) {
            if(hasZap(intel)) return true;
        }
        if(isSecondRound(intel) && isWinnerInFirstRound(intel)) {
            return hasCasalMaior(intel);
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
                if (countManilhas(intel) >= 2)
                    return CardToPlay.of(bestCard);
                return CardToPlay.discard(worstCard);
            }
            if(!isWinnerInFirstRound(intel)) return CardToPlay.of(bestCard);
        }

        return CardToPlay.of(bestCard);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {

        List<TrucoCard> myCards = intel.getCards();
        myCards.sort(TrucoCard::relativeValue);

        if(isWinning(intel))
            if(getDiffPoints(intel) >= 6 && myCards.stream()
                    .allMatch(trucoCard -> isGreaterThan(CardRank.JACK,trucoCard,intel)))
                return 0;

        if(countManilhas(intel) >= 2) return 1;

        if(myCards.stream().allMatch(trucoCard -> isGreaterThan(CardRank.ACE,trucoCard,intel))) return 0;

        if(hasZap(intel))
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

    private long countManilhas(GameIntel intel){
        List<TrucoCard> myCards = intel.getCards();
        return myCards.stream().filter(trucoCard -> trucoCard.isManilha(intel.getVira())).count();
    }

    private boolean hasZap(GameIntel intel){
        List<TrucoCard> myCards = intel.getCards();
        return myCards.stream().anyMatch(trucoCard -> trucoCard.isZap(intel.getVira()));
    }
}
