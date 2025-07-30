package com.local.pedro.herick.skilldiffbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

interface Strategy {
    /**
     * <p>Decides if the bot wants to request a hand points raise.</p>
     * @return {@code true} if it wants to request a hand points raise or {@code false} otherwise.
     */
    boolean decideIfRaises(GameIntel intel);

    /**
     * <p>Choose a card to be played or discarded. The card is represented by a {@link CardToPlay} object,
     * which wraps a {@link TrucoCard} and adds information about whether it should be played or discarded.</p>
     * @return a TrucoCard representing the card to be played or discarded.
     */
    CardToPlay chooseCard(GameIntel intel);
}
