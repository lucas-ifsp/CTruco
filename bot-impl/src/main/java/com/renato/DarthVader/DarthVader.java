package com.renato.DarthVader;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;
import java.util.NoSuchElementException;

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

    public enum OpponentCardClassification {
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

    public OpponentCardClassification classifyOpponentCard(GameIntel intel) {
        TrucoCard opponentCard = intel.getOpponentCard().orElseThrow(()->new NoSuchElementException("Card not found"));
        TrucoCard vira = intel.getVira();

        if(isHighCard(opponentCard))
        {
            return OpponentCardClassification.GOOD;
        }else if (isAverageCard(opponentCard)) {
            return OpponentCardClassification.AVERAGE;
        }
        else {
            return OpponentCardClassification.BAD;
        }
    }

}
