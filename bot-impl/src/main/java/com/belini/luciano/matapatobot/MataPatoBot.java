package com.belini.luciano.matapatobot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;
import java.util.Optional;

public class MataPatoBot implements BotServiceProvider{

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

    public Boolean checkFirstPlay (Optional<TrucoCard> opponentCard){
        return opponentCard.isPresent();
    }

    public TrucoCard KillingOpponentCard(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();
        TrucoCard cardToPlay = null;

        if (!opponentCard.isPresent()) {
            return null;
        }

        TrucoCard lowestCard = intel.getCards().get(0);

        for (TrucoCard card : intel.getCards()) {
            if (card.compareValueTo(lowestCard, vira) < 0) {
                lowestCard = card;
            }
            if (card.compareValueTo(opponentCard.get(), vira) > 0) {
                if (cardToPlay == null || card.compareValueTo(cardToPlay, vira) < 0) {
                    cardToPlay = card;
                }
            }
        }

        return cardToPlay != null ? cardToPlay : lowestCard;
    }

    public String RoundCheck(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        int cardCount = cards.size();

        switch (cardCount) {
            case 3:
                return "Round 1";
            case 2:
                return "Round 2";
            case 1:
                return "Round 3";
            default:
                return "No cards";
        }
    }
}
