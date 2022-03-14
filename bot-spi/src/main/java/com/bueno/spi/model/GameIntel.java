/*
 *  Copyright (C) 2022 Lucas B. R. de Oliveira - IFSP/SCL
 *  Contact: lucas <dot> oliveira <at> ifsp <dot> edu <dot> br
 *
 *  This file is part of CTruco (Truco game for didactic purpose).
 *
 *  CTruco is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CTruco is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CTruco.  If not, see <https://www.gnu.org/licenses/>
 */

package com.bueno.spi.model;

import java.util.List;
import java.util.Optional;

public class GameIntel {

    public enum RoundResult {WON, DREW, LOST}

    private final List<TrucoCard> cards;
    private final List<TrucoCard> openCards;
    private final TrucoCard vira;
    private final TrucoCard opponentCard;
    private final List<RoundResult> roundResults;
    private final int score;
    private final int opponentScore;
    private final int handPoints;

    private GameIntel(List<TrucoCard> cards, List<TrucoCard> openCards, TrucoCard vira, TrucoCard opponentCard,
                      List<RoundResult> roundResults, int score, int opponentScore, int handPoints) {
        this.cards = cards;
        this.openCards = openCards;
        this.vira = vira;
        this.opponentCard = opponentCard;
        this.roundResults = roundResults;
        this.score = score;
        this.opponentScore = opponentScore;
        this.handPoints = handPoints;
    }

    public List<TrucoCard> getCards() {
        return cards;
    }

    public List<TrucoCard> getOpenCards() {
        return openCards;
    }

    public TrucoCard getVira() {
        return vira;
    }

    public Optional<TrucoCard> getOpponentCard() {
        return Optional.ofNullable(opponentCard);
    }

    public List<RoundResult> getRoundResults() {
        return roundResults;
    }

    public int getScore() {
        return score;
    }

    public int getOpponentScore() {
        return opponentScore;
    }

    public int getHandPoints() {
        return handPoints;
    }

    public interface GeneralIntel{
        BotIntel gameInfo(List<RoundResult> roundResults, List<TrucoCard> openCards, TrucoCard vira, int handPoints);
    }

    public interface BotIntel{
        OpponentIntel botInfo(List<TrucoCard> cards, int score);
    }

    public interface OpponentIntel{
        StepBuilder opponentScore(int opponentScore);
    }


    public static final class StepBuilder implements  GeneralIntel, BotIntel, OpponentIntel{
        private List<TrucoCard> cards;
        private List<TrucoCard> openCards;
        private TrucoCard vira;
        private TrucoCard opponentCard;
        private List<RoundResult> roundResults;
        private int score;
        private int opponentScore;
        private int handPoints;

        private StepBuilder(){}

        public static GeneralIntel with(){
            return new StepBuilder();
        }

        @Override
        public BotIntel gameInfo(List<RoundResult> roundResults, List<TrucoCard> openCards, TrucoCard vira, int handPoints) {
            this.roundResults = List.copyOf((roundResults));
            this.openCards = List.copyOf(openCards);
            this.vira = vira;
            this.handPoints = handPoints;
            return this;
        }

        @Override
        public OpponentIntel botInfo(List<TrucoCard> cards, int score) {
            this.cards = cards;
            this.score = score;
            return this;
        }

        @Override
        public StepBuilder opponentScore(int opponentScore) {
            this.opponentScore = opponentScore;
            return this;
        }

        public StepBuilder opponentCard(TrucoCard card){
            this.opponentCard = card;
            return this;
        }

        public GameIntel build(){
            return new GameIntel(cards, openCards, vira, opponentCard, roundResults, score, opponentScore, handPoints);
        }
    }
}
