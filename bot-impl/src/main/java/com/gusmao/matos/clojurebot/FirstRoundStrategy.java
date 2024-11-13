package com.gusmao.matos.clojurebot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

public final class FirstRoundStrategy implements RoundStrategy {
    private final GameIntel gameIntel;
    private final TrucoCard vira;

    public FirstRoundStrategy(GameIntel gameIntel) {
        this.gameIntel = gameIntel;
        this.vira = gameIntel.getVira();
    }

    @Override
    public boolean getMaoDeOnzeResponse() {
        final int handPower = HandUtils.getHandPower(gameIntel, vira);
        final int pointsDifference = gameIntel.getScore() - gameIntel.getOpponentScore();

        if (handPower > 22) return true;

        return pointsDifference > 9 && handPower > 18;
    }

    @Override
    public boolean decideIfRaises() {
        return false;
    }

    @Override
    public CardToPlay chooseCard() {
        return null;
    }

    @Override
    public int getRaiseResponse() {
        return 0;
    }
}
