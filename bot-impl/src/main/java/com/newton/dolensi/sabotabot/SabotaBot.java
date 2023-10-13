package com.newton.dolensi.sabotabot;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;

public class SabotaBot implements BotServiceProvider {

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
        var hand = intel.getCards();
        var opponentCard = intel.getOpponentCard();
        CardToPlay cardToPlay = CardToPlay.of(hand.get(0));

        if (opponentCard.isEmpty()) {
            for (TrucoCard card : hand) {
                if (card.isOuros(intel.getVira())) {
                    return CardToPlay.of(card);
                }
            }
            cardToPlay = CardToPlay.of(getGreatestCard(intel, hand));
        } else if (opponentHasStrongCard(opponentCard.get()))
            cardToPlay = CardToPlay.of(getWeakestCard(intel, hand));

        return cardToPlay;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    private TrucoCard getGreatestCard(GameIntel intel, List<TrucoCard> hand) {
        if (hand.size() == 3) {
            if (hand.get(0).compareValueTo(hand.get(1), intel.getVira()) > 0) {
                if (hand.get(0).compareValueTo(hand.get(2), intel.getVira()) > 0)
                    return hand.get(0);
                else
                    return hand.get(2);
            } else if (hand.get(1).compareValueTo(hand.get(2), intel.getVira()) > 0)
                return hand.get(1);
            else
                return hand.get(2);
        } else if (hand.size() == 2) {
            if (hand.get(0).compareValueTo(hand.get(1), intel.getVira()) > 0)
                return hand.get(0);
            else
                return hand.get(1);
        }
        return hand.get(0);
    }

    private TrucoCard getWeakestCard(GameIntel intel, List<TrucoCard> hand) {
        if (hand.size() == 3) {
            if (hand.get(0).compareValueTo(hand.get(1), intel.getVira()) < 0) {
                if (hand.get(0).compareValueTo(hand.get(2), intel.getVira()) < 0)
                    return hand.get(0);
                else
                    return hand.get(2);
            } else if (hand.get(1).compareValueTo(hand.get(2), intel.getVira()) < 0)
                return hand.get(1);
            else
                return hand.get(2);
        } else if (hand.size() == 2) {
            if (hand.get(0).compareValueTo(hand.get(1), intel.getVira()) < 0)
                return hand.get(0);
            else
                return hand.get(1);
        }
        return hand.get(0);
    }

    private boolean opponentHasStrongCard(TrucoCard opponentCard) {
        int KING_VALUE = 7;
        return opponentCard.getRank().value() > KING_VALUE;
    }
}
