package com.yuri.impl;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MockRound {

    private static final Character A = 'A';
    private static final Character B = 'B';

    public static Builder vira(TrucoCard vira) {
        return new Builder(vira);
    }

    public static Builder vira(CardRank rank, CardSuit suit) {
        return new Builder(TrucoCard.of(rank, suit));
    }

    public static class Builder {

        private final TrucoCard vira;
        private final Stack<TrucoCard> deckA = new Stack<>();
        private final Stack<TrucoCard> deckB = new Stack<>();

        private Builder(TrucoCard vira) {
            this.vira = vira;
        }

        public Builder giveA(CardRank rank, CardSuit suit) {
            return giveA(TrucoCard.of(rank, suit));
        }

        public Builder giveA(TrucoCard card) {
            deckA.push(card);
            return this;
        }

        public Builder giveB(CardRank rank, CardSuit suit) {
            return giveB(TrucoCard.of(rank, suit));
        }

        public Builder giveB(TrucoCard card) {
            deckB.push(card);
            return this;
        }

        public Builder play() {
            return this;
        }

        public GameIntel build() {
            return GameIntel.StepBuilder.with()
                .gameInfo(
                    new ArrayList<>(),
                    List.of(vira),
                    vira,
                    1
                )
                .botInfo(
                    deckA,
                    0
                )
                .opponentScore(0)
                .opponentCard(null)
                .build();
        }
    }
}
