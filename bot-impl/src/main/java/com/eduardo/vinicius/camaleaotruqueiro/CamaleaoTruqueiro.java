package com.eduardo.vinicius.camaleaotruqueiro;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Arrays;
import java.util.List;

public class CamaleaoTruqueiro implements BotServiceProvider {
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
        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    @Override
    public String getName() {
        return BotServiceProvider.super.getName();
    }

    public TrucoCard getGreatestCard(List<TrucoCard> cards, TrucoCard vira) {

        int compareCard01WithCard02 = cards.get(0).compareValueTo(cards.get(1), vira);

        if (compareCard01WithCard02 <= 0) {
            int compareCard02WithCard03 = cards.get(1).compareValueTo(cards.get(2), vira);
            if (compareCard02WithCard03 <= 0) return cards.get(2);
            else return cards.get(1);
        } else {
            int compareCard01WithCard03 = cards.get(0).compareValueTo(cards.get(2), vira);
            if (compareCard01WithCard03 <= 0) return cards.get(2);
            else return cards.get(0);
        }
    }

    public TrucoCard getLowestCard(List<TrucoCard> cards, TrucoCard vira) {

        int compareCard01WithCard02 = cards.get(0).compareValueTo(cards.get(1), vira);
        int compareCard02WithCard03 = cards.get(1).compareValueTo(cards.get(2), vira);
        int compareCard01WithCard03 = cards.get(0).compareValueTo(cards.get(2), vira);

        if (compareCard01WithCard02 > 0) {
            if (compareCard02WithCard03 > 0) return cards.get(2);
            else return cards.get(1);
        }
        if (compareCard01WithCard03 > 0) return cards.get(2);

        return cards.get(0);
    }

    public int numberOfManilhas(List<TrucoCard> cards, TrucoCard vira) {
        int numberOfManilhas;
        numberOfManilhas = cards.stream().filter(card -> card.isManilha(vira)).toList().size();
        return numberOfManilhas;
    }

    public int getNumberOfHighCards(List<TrucoCard> handCards, TrucoCard vira) {
        int numberOfHighCards = 0;
        for (TrucoCard handCard : handCards) {
            if(handCard.getRank().value()>7 || handCard.isManilha(vira)) numberOfHighCards++;
        }
        return numberOfHighCards;
    }
    public boolean isWinning(int myScore, int opponentScore){
        return myScore > opponentScore;
    }

    public Float getProbabilityOfAbsolutVictoryHand(List<TrucoCard> playersHand, TrucoCard vira) {
        // absolut victory ( when a players cards play againts an opponent hand´s
        // that can´t win or draw in any possible Hand´s played in a Round)

        // is when the median value player´s card is greater or
        // equal than the greater than the greater card of the opponent
        // and the highest player card is greater than the media

        TrucoCard medianCard = getMedianCard(playersHand, vira);

        int numberOfCard = getNumberOfCardWorstThanMedianCard(medianCard, vira);

        return ((float) (numberOfCard * (numberOfCard - 1) * (numberOfCard - 2)) /(38*37*36));
    }

    public TrucoCard getMedianCard(List<TrucoCard> playersHand, TrucoCard vira){
        TrucoCard greatestCard = getGreatestCard(playersHand, vira);
        TrucoCard lowestCard = getLowestCard(playersHand, vira);
        if(playersHand.get(0).equals(greatestCard) && playersHand.get(1).equals(lowestCard))
            return playersHand.get(2);
        if(playersHand.get(0).equals(greatestCard) && playersHand.get(2).equals(lowestCard))
            return playersHand.get(1);
        return playersHand.get(0);
    }

    public int getNumberOfCardWorstThanMedianCard(TrucoCard medianCard, TrucoCard vira) {
        int medianCardRank = medianCard.relativeValue(vira);
        if (medianCard.isManilha(vira)) {
            switch (medianCardRank) {
                case 12: return 38;
                case 11: return 37;
                case 10: return 36;
            }
        }
        if(medianCardRank == 1)
            return 0;
        if (vira.relativeValue(vira) >= medianCardRank) {
            return ( medianCardRank - 1) * 4 ;
        }
        return ( medianCardRank * 4) - 5;
    }

    public boolean theBotPlaysFirst(GameIntel intel) {
        return intel.getOpponentCard().isEmpty();
    }

    public boolean isTheFirstRound(GameIntel intel) {
        return intel.getRoundResults().isEmpty();
    }

    public boolean isTheSecondRound(GameIntel build) {
        return build.getRoundResults().size() == 1;
    }
}
