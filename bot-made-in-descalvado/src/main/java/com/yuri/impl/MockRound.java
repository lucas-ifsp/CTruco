package com.yuri.impl;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;

import static com.bueno.spi.model.GameIntel.RoundResult.*;

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
        private final Stack<TrucoCard> played = new Stack<>();
        private final List<GameIntel.RoundResult> results = new ArrayList<>();

        private Character last;
        private TrucoCard lastPlayedA;
        private TrucoCard lastPlayedB;

        private Builder(TrucoCard vira) {
            this.vira = vira;
        }

        public Builder giveA(CardRank rank, CardSuit suit) {
            return giveA(TrucoCard.of(rank, suit));
        }

        public Builder giveA(TrucoCard card) {
            deckA.push(card);
            last = A;
            return this;
        }

        public Builder giveB(CardRank rank, CardSuit suit) {
            return giveB(TrucoCard.of(rank, suit));
        }

        public Builder giveB(TrucoCard card) {
            deckB.push(card);
            last = B;
            return this;
        }

        public Builder play() {
            if (last == A) {
                TrucoCard card = deckA.pop();
                lastPlayedA = card;
                last = null;
                played.add(card);
            }

            if (last == B) {
                TrucoCard card = deckB.pop();
                lastPlayedB = card;
                last = null;
                played.add(card);
            }

            if (lastPlayedA != null && lastPlayedB != null) {
                var winner = lastPlayedA.compareValueTo(lastPlayedB, vira);

                if (winner == 1) {
                    results.add(WON);
                } else if (winner == 0) {
                    results.add(DREW);
                } else {
                    results.add(LOST);
                }

                lastPlayedA = null;
                lastPlayedB = null;
            }

            return this;
        }

        public GameIntel build() {
            List<TrucoCard> openCards = new ArrayList<>();
            openCards.add(vira);
            openCards.addAll(played);

            return GameIntel.StepBuilder.with()
                .gameInfo(
                    results,
                    openCards,
                    vira,
                    1
                )
                .botInfo(
                    deckA,
                    0
                )
                .opponentScore(0)
                .opponentCard(lastPlayedB)
                .build();
        }
    }
}
