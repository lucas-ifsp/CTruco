package com.felipe.fabiano.truccard;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
public interface GameRound {
    public boolean decideIfRaises(GameIntel intel);

    public int getRaiseResponse(GameIntel intel);

    CardToPlay chooseCard(GameIntel intel);
}
