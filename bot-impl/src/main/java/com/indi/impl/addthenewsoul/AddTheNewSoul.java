package com.indi.impl.addthenewsoul;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Optional;

public class AddTheNewSoul implements BotServiceProvider {

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

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
        TrucoCard smallestCardCapableOfWinning = chooseSmallestCardCapableOfWinning(intel);
        if(smallestCardCapableOfWinning == null)
            return CardToPlay.of(getSmallestCardOnHand(intel));

        return CardToPlay.of(smallestCardCapableOfWinning);
    }

    private TrucoCard chooseSmallestCardCapableOfWinning(GameIntel intel) {
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();
        TrucoCard smallestCardCapableOfWinning = null;
        TrucoCard vira = intel.getVira();

        if (opponentCard.isPresent()) {
            TrucoCard opponentCardValue = opponentCard.get();
            for (TrucoCard card : intel.getCards()) {
                if (card.relativeValue(vira) > opponentCardValue.relativeValue(vira)) {
                    if(smallestCardCapableOfWinning == null)
                        smallestCardCapableOfWinning = card;
                    else if(card.relativeValue(vira) < smallestCardCapableOfWinning.relativeValue(vira))
                        smallestCardCapableOfWinning = card;
                }
            }
        }

        return smallestCardCapableOfWinning;
    }

    private TrucoCard getSmallestCardOnHand(GameIntel intel) {
        TrucoCard smallestCard = null;
        for (TrucoCard card : intel.getCards()) {
            if (smallestCard == null || card.relativeValue(intel.getVira()) < smallestCard.relativeValue(intel.getVira())) {
                smallestCard = card;
            }
        }
        return smallestCard;
    }
}
