package com.castro.calicchio.unamedbot;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;

public class UnamedBot implements BotServiceProvider{
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        
        int totalPoints = calculateTotalPoints(cards);
    
        if (totalPoints == 11) {
            return true;
        } else {
            return false;
        }
    }

    private int calculateTotalPoints(List<TrucoCard> cards) {
        int totalPoints = 0;
        for (TrucoCard card : cards) {
            totalPoints += card.getPoints();
        }
        return totalPoints;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        sortHand(intel);
        List<TrucoCard> cards = intel.getCards();
        
        int sum = cards.stream().mapToInt(TrucoCard::relativeValue).sum();
        
        long highValueCards = cards.stream().filter(card -> card.relativeValue() > 9).count();
        
        List<RoundResult> roundResults = intel.getRoundResults();
        int currentRound = roundResults.size();
        
        int raiseThreshold = 25;
        
        raiseThreshold += currentRound * 5;
        raiseThreshold += highValueCards * 2;
        
        return sum > raiseThreshold;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        sortHand(intel);
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();
        
        for (TrucoCard card : cards) {
            if (card.beats(vira)) {
                return new CardToPlay(card, PlayType.PLAY);
            }
        }
    
        TrucoCard lowestCard = cards.get(0);
        return new CardToPlay(lowestCard, PlayType.DISCARD);
    }


    @Override
    public int getRaiseResponse(GameIntel intel) {
        sortHand(intel);
        List<TrucoCard> cards = intel.getCards();
        int sum = cards.stream().mapToInt(TrucoCard::relativeValue).sum();
        if (sum > 20) {
            return 1;
        } else if (sum <= 20 && sum >= 10) {
            return 0; 
        } else {
            return -1;
        }
    }

    public void sortHand(GameIntel intel){
        List<TrucoCard> cards = intel.getCards();
        cards.sort(Comparator.comparingInt(TrucoCard::relativeValue));
    }
}
