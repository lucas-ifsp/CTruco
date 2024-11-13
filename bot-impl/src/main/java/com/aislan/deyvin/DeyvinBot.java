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
        if(intel.getRoundResults().contains(GameIntel.RoundResult.WON)){
            if(myCards.stream().anyMatch(trucoCard -> trucoCard.isZap(vira))) return true;
            if(myCards.stream().allMatch(trucoCard -> trucoCard.isManilha(vira))) return true;
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
            if(intel.getRoundResults().contains(GameIntel.RoundResult.WON)) {
                if (myCards.stream().allMatch(trucoCard -> trucoCard.isManilha(vira)))
                    return CardToPlay.of(bestCard);
                if (bestCard.isZap(vira)) return CardToPlay.of(bestCard);
                else return CardToPlay.discard(worstCard);
            }
            if(intel.getRoundResults().contains(GameIntel.RoundResult.LOST)) return CardToPlay.of(bestCard);
            if(intel.getRoundResults().contains(GameIntel.RoundResult.DREW)) return CardToPlay.of(bestCard);
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
}
