package com.eduardo.vinicius.camaleaotruqueiro;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;

public class CamaleaoTruqueiro implements BotServiceProvider {
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

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    @Override
    public String getName() {
        return BotServiceProvider.super.getName();
    }

    public TrucoCard getGreaterCard(List<TrucoCard> cards, TrucoCard vira) {

        int compareCard01WithCard02 = cards.get(0).compareValueTo(cards.get(1), vira);

        if (compareCard01WithCard02 <= 0) {
            int compareCard02WithCard03 = cards.get(1).compareValueTo(cards.get(2), vira);
            if (compareCard02WithCard03 <= 0) return cards.get(2);
            else return cards.get(1);
        } else {
            int compareCard01WithCard03 = cards.get(0).compareValueTo(cards.get(2), vira);
            if (compareCard01WithCard03 <= 0) return cards.get(2);
            else return cards.get(0);
        }
    }

    public TrucoCard getLowestCard(List<TrucoCard> cards, TrucoCard vira) {

        int compareCard01WithCard02 = cards.get(0).compareValueTo(cards.get(1), vira);
        int compareCard02WithCard03 = cards.get(1).compareValueTo(cards.get(2), vira);
        int compareCard01WithCard03 = cards.get(0).compareValueTo(cards.get(2), vira);

        if (compareCard01WithCard02 > 0) {
            if (compareCard02WithCard03 > 0) return cards.get(2);
            else return cards.get(1);
        }
        if (compareCard01WithCard03 > 0) return cards.get(2);

        return cards.get(0);
    }
}
