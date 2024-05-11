package com.contiero.lemes.newbot.services.choose_card;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.contiero.lemes.newbot.interfaces.Choosing;

public class PassiveChoosing implements Choosing {
    private final GameIntel intel;

    public PassiveChoosing(GameIntel intel) {
        this.intel = intel;
    }

    @Override
    public CardToPlay firstRoundChoose() {
        return null;
    }

    @Override
    public CardToPlay secondRoundChoose() {
        return null;
    }

    @Override
    public CardToPlay thirdRoundChoose() {
        return null;
    }
}
