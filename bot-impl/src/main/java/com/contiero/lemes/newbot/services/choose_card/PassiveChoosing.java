package com.contiero.lemes.newbot.services.choose_card;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;

public class PassiveChoosing implements Choosing {
    @Override
    public CardToPlay choose(GameIntel intel) {
        return null;
    }
}
