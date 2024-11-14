package com.gusmao.matos.clojurebot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.ArrayList;
import java.util.List;

public final class FirstRoundStrategy implements RoundStrategy {
    private final GameIntel gameIntel;
    private final TrucoCard vira;
    private final List<TrucoCard> myCards;
    private final int handPower;

    public FirstRoundStrategy(GameIntel gameIntel) {
        this.gameIntel = gameIntel;
        this.vira = gameIntel.getVira();
        this.myCards = new ArrayList<>(gameIntel.getCards());
        this.handPower = HandUtils.getHandPower(gameIntel, vira);

        myCards.sort((c1, c2) -> c2.compareValueTo(c1, this.vira));
    }

    @Override
    public boolean getMaoDeOnzeResponse() {
        final int pointsDifference = gameIntel.getScore() - gameIntel.getOpponentScore();

        if (handPower > 22) return true;

        return pointsDifference > 9 && handPower > 18;
    }

    @Override
    public boolean decideIfRaises() {
        return handPower >= 34;
    }

    @Override
    public CardToPlay chooseCard() {
        final List<TrucoCard> playedCards = gameIntel.getOpenCards();

        if (playedCards.isEmpty()) {
            return CardToPlay.of(myCards.get(2));
        }

        return HandUtils.getLessCardToWin(myCards, playedCards.get(0), vira);
    }

    @Override
    public int getRaiseResponse() {
        if (handPower >= 34) return 1;
        if (handPower > 26) return 0;

        return -1;
    }
}
