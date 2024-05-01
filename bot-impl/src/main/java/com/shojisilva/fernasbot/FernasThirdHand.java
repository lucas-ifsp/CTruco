package com.shojisilva.fernasbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;

public class FernasThirdHand implements FernasStrategy {
    public static final int HAND_MAX_VALUE = 13;
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return true;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        int handValue = getHandValue(intel);
        return handValue >= HAND_MAX_VALUE * 0.69;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return CardToPlay.of(intel.getCards().get(0));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        int handValue = getHandValue(intel);
        if (handValue >= HAND_MAX_VALUE * 0.75) return 1;
        if (handValue >= HAND_MAX_VALUE * 0.69) return 0;
        return -1;
    }
}
