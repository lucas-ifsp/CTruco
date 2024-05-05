package com.Sigoli.Castro.PatoBot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import javax.lang.model.type.NullType;
import java.util.List;
import java.util.Optional;

public class PatoBot implements BotServiceProvider {
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



    public Boolean checkIfOpponentIsFirstToPlay(Optional<TrucoCard> opponentCard) {
        return opponentCard.isPresent();
    }

    public int getNumberOfCardsInHand(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        return cards.size();
    }

    public TrucoCard attemptToBeatOpponentCard(GameIntel intel) {
        TrucoCard cardToPlay = null;
        TrucoCard vira = intel.getVira();
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();
        for (TrucoCard card : intel.getCards()) {
            if (card.compareValueTo(opponentCard.get(), vira) > 0) {
                if (cardToPlay == null || card.compareValueTo(cardToPlay, vira) < 0) {
                    cardToPlay = card;
                }
            }
        }
        return cardToPlay != null ? cardToPlay : intel.getCards().get(0);
    }
}
