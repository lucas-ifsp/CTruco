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

        return CardToPlay.of(getGreatestCard(intel, hand));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    private TrucoCard getGreatestCard(GameIntel intel, List<TrucoCard> hand) {
        if (intel.getOpponentCard().isEmpty()) {
            if (hand.get(0).compareValueTo(hand.get(1), intel.getVira()) > 0) {
                if (hand.get(0).compareValueTo(hand.get(2), intel.getVira()) > 0)
                    return hand.get(0);
                else
                    return hand.get(2);
            } else if (hand.get(1).compareValueTo(hand.get(2), intel.getVira()) > 0)
                return hand.get(1);
            else
                return hand.get(2);
        } else
            return hand.get(0);
    }
}
