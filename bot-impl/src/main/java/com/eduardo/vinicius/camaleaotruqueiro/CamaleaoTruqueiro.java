package com.eduardo.vinicius.camaleaotruqueiro;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import javax.smartcardio.Card;
import java.util.Arrays;
import java.util.List;

public class CamaleaoTruqueiro implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();
        if(getNumberOfHighCards(cards,vira) >= 2 && numberOfManilhas(cards,vira) >= 1) return true;
        else if (intel.getOpponentScore() < 9 && getNumberOfHighCards(cards,vira) >= 1) return true;
        else return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();
        if(isTheFirstRound(intel)) {
            if (theBotPlaysFirst(intel)) {
                if (getNumberOfHighCards(cards, vira) >= 2) {
                    TrucoCard playCard = getGreatestCard(cards, vira);
                    return CardToPlay.of(playCard);
                } else {
                    TrucoCard playCard = getLowestCard(cards, vira);
                    return CardToPlay.of(playCard);
                }
            } else {
                TrucoCard card = getGreatestCard(cards, vira);
                return CardToPlay.of(card);
            }
        }else {
            TrucoCard card = getGreatestCard(cards,vira);
            return CardToPlay.of(card);
        }
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
        TrucoCard greatestCard = cards.get(0);
        for (TrucoCard card : cards ) {
            if (card.compareValueTo(greatestCard, vira) > 0) greatestCard = card;
        }
        return greatestCard;
    }

    public TrucoCard getLowestCard(List<TrucoCard> cards, TrucoCard vira) {
        TrucoCard lowestCard = cards.get(0);
        for (TrucoCard card : cards ) {
            if (card.compareValueTo(lowestCard, vira) < 0) lowestCard = card;
        }
        return lowestCard;
    }

    public int numberOfManilhas(List<TrucoCard> cards, TrucoCard vira) {
        int numberOfManilhas;
        numberOfManilhas = cards.stream().filter(card -> card.isManilha(vira)).toList().size();
        return numberOfManilhas;
    }

    public int getNumberOfHighCards(List<TrucoCard> handCards, TrucoCard vira) {
        int numberOfHighCards = 0;
        for (TrucoCard handCard : handCards) {
            if(handCard.getRank().value()>8 || handCard.isManilha(vira)) numberOfHighCards++;
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

        TrucoCard medianCard = getMedianValue(playersHand, vira);

        int numberOfCard = getNumberOfCardWorstThanMedianCard(medianCard, vira);

        return ((float) (numberOfCard * (numberOfCard - 1) * (numberOfCard - 2)) /(38*37*36));
    }

    public TrucoCard getMedianValue(List<TrucoCard> playersHand, TrucoCard vira){
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

    public boolean isTheSecondRound(GameIntel intel) {
        return intel.getRoundResults().size() == 1;
    }

    public List<TrucoCard> haveStrongestCard(GameIntel intel, List<TrucoCard> myCards) {
        TrucoCard opponentCard = intel.getOpponentCard().get();
        TrucoCard vira = intel.getVira();
        List<TrucoCard> strongestCards = new java.util.ArrayList<>(List.of());
        for (TrucoCard myCard : myCards) {
            if(myCard.compareValueTo(opponentCard, vira) > 0) strongestCards.add(myCard);
        }
        return strongestCards;
    }

    public int getNumberOfMediumCards(List<TrucoCard> handCards, TrucoCard vira) {
        int numberOfMediumCards = 0;
        for (TrucoCard handCard : handCards) {
            if(handCard.getRank().value()<=7 && handCard.getRank().value()>4 && !handCard.isManilha(vira)) numberOfMediumCards++;
        }
        return numberOfMediumCards;
    }

    public int getNumberOfLowCards(List<TrucoCard> handCards, TrucoCard vira) {
        int numberOfLowCards = 0;
        for (TrucoCard handCard : handCards) {
            if(handCard.getRank().value()<=4 && !handCard.isManilha(vira)) numberOfLowCards++;
        }
        return numberOfLowCards;
    }

    public boolean winFistRound(GameIntel intel) {
        return intel.getRoundResults().get(0) == GameIntel.RoundResult.WON;
    }

    public boolean drewFistRound(GameIntel intel) {
        return intel.getRoundResults().get(0) == GameIntel.RoundResult.DREW;
    }

    public boolean lostFistRound(GameIntel intel) {
        return intel.getRoundResults().get(0) == GameIntel.RoundResult.LOST;
    }

    public boolean winSecondRound(GameIntel intel) {
        return intel.getRoundResults().get(1) == GameIntel.RoundResult.WON;
    }

    public boolean lostSecondRound(GameIntel intel) {
        return intel.getRoundResults().get(1) == GameIntel.RoundResult.LOST;
    }
}
