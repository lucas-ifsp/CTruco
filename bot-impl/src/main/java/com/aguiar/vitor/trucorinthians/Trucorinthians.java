package com.aguiar.vitor.trucorinthians;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;

public class Trucorinthians implements BotServiceProvider {
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
        List<TrucoCard> hand = intel.getCards();

        if (hand.isEmpty()) {
            return CardToPlay.discard(TrucoCard.closed());
        }

        return CardToPlay.of(hand.get(0));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }
}
