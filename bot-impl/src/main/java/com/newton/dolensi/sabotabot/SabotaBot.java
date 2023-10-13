package com.newton.dolensi.sabotabot;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

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
        TrucoCard greatestCard;

        if (intel.getOpponentCard().isEmpty()) {
            if (hand.get(0).compareValueTo(hand.get(1), intel.getVira()) > 0) {
                if (hand.get(0).compareValueTo(hand.get(2), intel.getVira()) > 0)
                    greatestCard = hand.get(0);
                else
                    greatestCard = hand.get(2);
            } else if (hand.get(1).compareValueTo(hand.get(2), intel.getVira()) > 0)
                greatestCard = hand.get(1);
            else
                greatestCard = hand.get(2);
        } else
            greatestCard = hand.get(0);

        return CardToPlay.of(greatestCard);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }
}
