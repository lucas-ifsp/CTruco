package com.local.felipe.fabiano.truccard;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
public interface GameRound {
    boolean decideIfRaises(GameIntel intel);

    int getRaiseResponse(GameIntel intel);

    CardToPlay chooseCard(GameIntel intel);
}
