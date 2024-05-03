package com.contiero.lemes.newbot.interfaces;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;

public interface Choosing {
    CardToPlay choose(GameIntel intel);
}
