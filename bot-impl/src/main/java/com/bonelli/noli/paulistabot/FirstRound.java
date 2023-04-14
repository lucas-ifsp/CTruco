package com.bonelli.noli.paulistabot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FirstRound implements Strategy {

    private final TrucoCard vira;

    private final List<TrucoCard> cardList;

    private final List<TrucoCard> openCards;

    private final GameIntel intel;

    public FirstRound(GameIntel intel) {
        this.vira = intel.getVira();
        this.cardList = new ArrayList<>(intel.getCards());
        this.openCards = new ArrayList<>(intel.getOpenCards());
        this.intel = intel;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return null;
    }

    public Optional<TrucoCard> getWhichBotShouldPlayFirst () {
        if (this.intel.getOpponentCard().isEmpty())
            return Optional.empty();
        return Optional.of(this.intel.getOpponentCard().get());
    }
}
