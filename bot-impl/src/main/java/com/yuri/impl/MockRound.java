package com.yuri.impl;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static com.bueno.spi.model.GameIntel.RoundResult.*;

public class MockRound {

    public static final String INVALID_NUMBER_OF_CARDS_MSG = "";
    public static final String INVALID_PLAY_STATE_MSG = "";

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
        private Character lastPlayed;
        private Character shoudPlay;
        private TrucoCard lastPlayedA;
        private TrucoCard lastPlayedB;
        private int cardsGivenToA;
        private int cardsGivenToB;
        private int scoreGivenToA = 0;
        private int scoreGivenToB = 0;
        private int handPoints = 1;

        private Builder(TrucoCard vira) {
            this.vira = vira;
        }

        public Builder giveScoreA(int score) {
            scoreGivenToA += score;
            return this;
        }

        public Builder giveScoreB(int score) {
            scoreGivenToB += score;
            return this;
        }

        public Builder hand(int points) {
            handPoints = points;
            return this;
        }

        public Builder giveA(CardRank rank, CardSuit suit) {
            return giveA(TrucoCard.of(rank, suit));
        }

        public Builder giveA(TrucoCard card) {
            deckA.push(card);
            last = A;
            cardsGivenToA += 1;
            return this;
        }

        public Builder giveB(CardRank rank, CardSuit suit) {
            return giveB(TrucoCard.of(rank, suit));
        }

        public Builder giveB(TrucoCard card) {
            deckB.push(card);
            last = B;
            cardsGivenToB += 1;
            return this;
        }

        public Builder play() {
            if (last == null) {
                throw new RuntimeException(INVALID_PLAY_STATE_MSG);
            }

            if (last == A) {
                if (lastPlayed == A) {
                    throw new RuntimeException(INVALID_PLAY_STATE_MSG);
                }

                TrucoCard card = deckA.pop();
                lastPlayedA = card;
                last = null;
                lastPlayed = A;
                played.add(card);
            }

            if (last == B) {
                if (lastPlayed == B) {
                    throw new RuntimeException(INVALID_PLAY_STATE_MSG);
                }

                TrucoCard card = deckB.pop();
                lastPlayedB = card;
                last = null;
                lastPlayed = B;
                played.add(card);
            }

            if (lastPlayedA != null && lastPlayedB != null) {
                var winner = lastPlayedA.compareValueTo(lastPlayedB, vira);

                if (winner > 0) {
                    results.add(WON);
                } else if (winner == 0) {
                    results.add(DREW);
                } else {
                    results.add(LOST);
                }

                if (lastPlayed == A) {
                    shoudPlay = B;
                } else {
                    shoudPlay = A;
                }

                lastPlayed = null;
                lastPlayedA = null;
                lastPlayedB = null;
            }

            return this;
        }

        public GameIntel build() {
            if (cardsGivenToA != 3 || cardsGivenToB != 3) {
                throw new RuntimeException(INVALID_NUMBER_OF_CARDS_MSG);
            }

            List<TrucoCard> openCards = new ArrayList<>();
            openCards.add(vira);
            openCards.addAll(played);

            return GameIntel.StepBuilder.with()
                .gameInfo(
                    results,
                    openCards,
                    vira,
                    handPoints
                )
                .botInfo(
                    deckA,
                    scoreGivenToA
                )
                .opponentScore(scoreGivenToB)
                .opponentCard(lastPlayedB)
                .build();
        }
    }
}
