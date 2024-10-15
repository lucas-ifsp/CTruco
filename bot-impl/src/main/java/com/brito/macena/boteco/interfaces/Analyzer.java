package com.brito.macena.boteco.interfaces;

import com.brito.macena.boteco.utils.Status;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.List;

public abstract class Analyzer {
    public Status myHand(GameIntel intel) {
        List<TrucoCard> myCards = intel.getCards();
        int size = myCards.size();
        if (size == 3) return threeCardsHandler(myCards);
        if (size == 2) return twoCardsHandler(myCards);
        if (size == 1) return oneCardHandler();
        return Status.EXCELLENT;
    }

    public abstract Status threeCardsHandler();
    public abstract Status twoCardsHandler();
    public abstract Status oneCardHandler();
}
