package com.local.contiero.lemes.atrasabot.interfaces;

import com.bueno.spi.model.CardToPlay;

public interface Choosing {
    CardToPlay firstRoundChoose();
    CardToPlay secondRoundChoose();
    CardToPlay thirdRoundChoose();
}
