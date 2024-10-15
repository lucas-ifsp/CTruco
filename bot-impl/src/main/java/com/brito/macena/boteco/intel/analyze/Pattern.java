package com.brito.macena.boteco.intel.analyze;

import com.brito.macena.boteco.interfaces.Analyzer;
import com.brito.macena.boteco.utils.MyHand;
import com.brito.macena.boteco.utils.Status;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.List;

public class Pattern extends Analyzer {

    private final GameIntel intel;
    private final TrucoCard vira;
    private final int bestCardValue;
    private final int secondBestCardValue;


    public Pattern(GameIntel intel) {
        this.intel = intel;
        vira = intel.getVira();

        MyHand myCards = new MyHand(intel.getCards(),vira);

        bestCardValue = myCards.getBestCard().relativeValue(vira);
        secondBestCardValue = myCards.getSecondBestCard().relativeValue(vira);
    }


    @Override
    public Status threeCardsHandler(List<TrucoCard> myCards) {
        return null;
    }

    @Override
    public Status twoCardsHandler(List<TrucoCard> myCards) {
        return null;
    }

    @Override
    public Status oneCardHandler() {
        return null;
    }
}
