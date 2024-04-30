package com.renato.DarthVader;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;

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


    public boolean isHighCard(TrucoCard card) {
        CardRank rank = card.getRank();
        return rank == CardRank.THREE || rank == CardRank.TWO || rank == CardRank.ACE;
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
        }
        return strongCard;
    }
}
