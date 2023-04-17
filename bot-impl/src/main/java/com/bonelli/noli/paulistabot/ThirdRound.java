package com.bonelli.noli.paulistabot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.ArrayList;
import java.util.List;

public class ThirdRound implements Strategy {

    private final TrucoCard vira;

    private final List<TrucoCard> cardList;

    private final List<TrucoCard> openCards;

    private final GameIntel intel;

    public ThirdRound(GameIntel intel) {
        this.vira = intel.getVira();
        this.cardList = new ArrayList<>(intel.getCards());
        this.openCards = new ArrayList<>(intel.getOpenCards());
        this.intel = intel;
    }

    @Override
    public int getRaiseResponse() {
        return 0;
    }

    @Override
    public boolean getMaoDeOnzeResponse() {
        return false;
    }

    @Override
    public boolean decideIfRaises() {
        return false;
    }

    @Override
    public CardToPlay chooseCard() {
        return null;
    }
}
