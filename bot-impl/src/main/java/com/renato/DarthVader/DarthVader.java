package com.renato.DarthVader;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;
import java.util.*;

public class DarthVader implements BotServiceProvider {

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {

        if(countManilhasAndHighCards(intel) >= 2)
        {
            return true;
        }

        if(getNumberManilhas(intel) >= 2)
        {
            return true;
        }
        if((getCountGoodCards(intel) >= 1) && (intel.getScore() - intel.getOpponentScore() == 4))
        {
            return true;
        }

        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if(getNumberManilhas(intel) >= 2)
        {
            return 1;
        }
        if(countManilhasAndHighCards(intel) >= 2)
        {
            return 0;
        }
        return -1;
    }

    public Map<CardClassification, Integer> countCardClassifications(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();

        Map<CardClassification, Integer> countMap = new HashMap<>();

        countMap.put(CardClassification.VERY_GOOD, 0);
        countMap.put(CardClassification.GOOD, 0);
        countMap.put(CardClassification.AVERAGE, 0);
        countMap.put(CardClassification.BAD, 0);

        for (TrucoCard card : cards) {
            CardClassification classification;

            if (card.isManilha(vira)) {
                classification = CardClassification.VERY_GOOD;
            } else if (isHighCard(card)) {
                classification = CardClassification.GOOD;
            } else if (isAverageCard(card)) {
                classification = CardClassification.AVERAGE;
            } else {
                classification = CardClassification.BAD;
            }

            countMap.put(classification, countMap.get(classification) + 1);
        }

        return countMap;
    }


    public TrucoCard chooseTheMinorCard(GameIntel intel) {
        TrucoCard opponentCard = intel.getOpponentCard().orElseThrow(() -> new NoSuchElementException("Card not found"));
        TrucoCard minorCard = null;
        CardClassification minClassification = null;
        boolean verifica = verifyMyHand(intel);
        Map<TrucoCard, CardClassification> classificationsMap = classifyMyCards(intel);
        CardClassification opponentClassification = classifyOpponentCard(intel);

        for (Map.Entry<TrucoCard, CardClassification> entry : classificationsMap.entrySet()) {
            TrucoCard card = entry.getKey();
            CardClassification classification = entry.getValue();

            if (classification == opponentClassification) {
                if (card.compareValueTo(opponentCard, intel.getVira()) > 0) {
                    return card;
                }
                else
                {
                    if(verifica)
                    {
                        return getaSmallerCardStrongerThanTheOpponent(intel);
                    }
                    else
                    {
                        return getSmallerCard(intel);
                    }

                }
            }
            else
            {
                if(verifica)
                {
                    return getaSmallerCardStrongerThanTheOpponent(intel);
                }
                else
                {
                    return getSmallerCard(intel);
                }
            }
        }


        return minorCard;
    }


    public Map<TrucoCard, CardClassification> classifyMyCards(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();

        Map<TrucoCard, CardClassification> classificationsMap = new HashMap<>();

        for (TrucoCard card : cards) {
            CardClassification classification;

            if (card.isManilha(vira)) {
                classification = CardClassification.VERY_GOOD;
            } else if (isHighCard(card)) {
                classification = CardClassification.GOOD;
            } else if (isAverageCard(card)) {
                classification = CardClassification.AVERAGE;
            } else {
                classification = CardClassification.BAD;
            }

            classificationsMap.put(card, classification);
        }

        return classificationsMap;
    }

    public int getNumberManilhas(GameIntel intel) {
        int manilhas = 0;
        for (TrucoCard card : intel.getCards()) {
            if (card.isManilha(intel.getVira())) {
                manilhas++;
            }
        }
        return manilhas;
    }

    public int roundNumber(GameIntel intel) {
        int numeroRodada = intel.getRoundResults().size() +1;
        return numeroRodada;
    }

    public boolean verifyMyHand(GameIntel intel) {
        Map<CardClassification, Integer> countMap = countCardClassifications(intel);
        int veryGoodCount = countMap.getOrDefault(CardClassification.VERY_GOOD, 0);
        int goodCount = countMap.getOrDefault(CardClassification.GOOD, 0);
        int averageCount = countMap.getOrDefault(CardClassification.AVERAGE, 0);
        int badCount = countMap.getOrDefault(CardClassification.BAD, 0);

        if(veryGoodCount +goodCount > averageCount + badCount)
        {
            return true;
        }
        return false;
    }

    public TrucoCard getaSmallerCardStrongerThanTheOpponent(GameIntel intel) {
        TrucoCard opponentCard = intel.getOpponentCard().orElseThrow(() -> new NoSuchElementException("Card not found"));

        List<TrucoCard> cards = intel.getCards();
        TrucoCard smallestCardStronger = null;

        for (TrucoCard card : cards) {
            if (card.compareValueTo(opponentCard, intel.getVira()) > 0 && (smallestCardStronger == null || card.compareValueTo(smallestCardStronger, intel.getVira()) < 0)) {
                smallestCardStronger = card;
            }
        }

        return smallestCardStronger;
    }

    public int getCountGoodCards(GameIntel intel) {
        int goodCards = 0;
        for (TrucoCard card : intel.getCards()) {
            if(isHighCard(card))
            {
                goodCards++;
            }
        }
        return goodCards;
    }


    public enum CardClassification {
        VERY_GOOD,
        GOOD,
        AVERAGE,
        BAD
    }

    public boolean isHighCard(TrucoCard card) {
        CardRank rank = card.getRank();
        return rank == CardRank.THREE || rank == CardRank.TWO || rank == CardRank.ACE;
    }

    public boolean isAverageCard(TrucoCard card) {
        CardRank rank = card.getRank();
        return rank == CardRank.KING || rank == CardRank.JACK || rank == CardRank.QUEEN;
    }

    public boolean isLowCard(TrucoCard card) {
        CardRank rank = card.getRank();
        return rank == CardRank.SEVEN || rank == CardRank.SIX || rank == CardRank.FIVE || rank == CardRank.FOUR;
    }

    public int countManilhasAndHighCards(GameIntel intel) {
        int count = 0;
        TrucoCard vira = intel.getVira();
        List<TrucoCard> cards = intel.getCards();

        for (TrucoCard card : cards) {
            if (card.isManilha(vira) || isHighCard(card)) {
                count++;
            }
        }

        return count;
    }

    public TrucoCard getSmallerCard(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();

        TrucoCard smallestCard = null;

        for (TrucoCard card : cards) {
            if (smallestCard == null || card.compareValueTo(smallestCard, intel.getVira()) < 0) {
                smallestCard = card;
            } else if (card.compareValueTo(smallestCard, intel.getVira()) == 0 && card.getSuit().ordinal() < smallestCard.getSuit().ordinal()) {
                smallestCard = card;
            }
        }
        return smallestCard;
    }

    public TrucoCard getStrongCard(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();

        TrucoCard strongCard = null;

        for (TrucoCard card : cards) {
            if(strongCard == null || card.compareValueTo(strongCard, intel.getVira()) > 0) {
                strongCard = card;
            }
            else if(card.compareValueTo(strongCard,intel.getVira()) == 0 && card.getSuit().ordinal() > strongCard.getSuit().ordinal())
            {
                strongCard = card;
            }
        }
        return strongCard;
    }

    public TrucoCard getStrongCardwithTheLowestSuit(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();

        TrucoCard strongCard = null;

        for (TrucoCard card : cards) {
            if(strongCard == null || card.compareValueTo(strongCard, intel.getVira()) > 0) {
                strongCard = card;
            }
            else if(card.compareValueTo(strongCard,intel.getVira()) == 0 && card.getSuit().ordinal() < strongCard.getSuit().ordinal())
            {
                strongCard = card;
            }
        }
        return strongCard;
    }


    public TrucoCard getTheSmallestManilha(GameIntel intel) {
        List<TrucoCard> manilhas = new ArrayList<>();

        for (Map.Entry<TrucoCard, CardClassification> entry : classifyMyCards(intel).entrySet()) {
            if (entry.getValue() == CardClassification.VERY_GOOD) {
                manilhas.add(entry.getKey());
            }
        }

        if (manilhas.isEmpty()) {
            return null;
        }

        manilhas.sort(Comparator.comparingInt(card -> card.getSuit().ordinal()));

        return manilhas.get(0);
    }

    public TrucoCard getTheStrongestManilha(GameIntel intel) {
        List<TrucoCard> manilhas = new ArrayList<>();

        for (Map.Entry<TrucoCard, CardClassification> entry : classifyMyCards(intel).entrySet()) {
            if (entry.getValue() == CardClassification.VERY_GOOD) {
                manilhas.add(entry.getKey());
            }
        }

        if (manilhas.isEmpty()) {
            return null;
        }

        manilhas.sort((card1, card2) -> card2.getSuit().compareTo(card1.getSuit()));

        return manilhas.get(0);
    }

    public TrucoCard getMediumCard(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();

        TrucoCard mediumCard = null;
        TrucoCard smallestCard = getSmallerCard(intel);
        TrucoCard strongestCard = getStrongCard(intel);

        for (TrucoCard card : cards) {
            if (!card.equals(smallestCard) && !card.equals(strongestCard)) {
                mediumCard = card;
                break;
            }
        }

        return mediumCard;
    }

    public CardClassification classifyOpponentCard(GameIntel intel) {
        TrucoCard opponentCard = intel.getOpponentCard().orElseThrow(()->new NoSuchElementException("Card not found"));
        TrucoCard vira = intel.getVira();

        if(opponentCard.isManilha(vira))
        {
            return CardClassification.VERY_GOOD;
        }
        else if(isHighCard(opponentCard))
        {
            return CardClassification.GOOD;
        }
        else if (isAverageCard(opponentCard))
        {
            return CardClassification.AVERAGE;
        }
        else
        {
            return CardClassification.BAD;
        }
    }

}
