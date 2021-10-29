/*
 *  Copyright (C) 2021 Lucas B. R. de Oliveira - IFSP/SCL
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

package com.bueno.domain.entities.hand;


public class HandScore {
    private final int score;

    private HandScore(int score){
        if(!isValid(score)) throw new HandScoreException("Invalid score value: " + score);
        this.score = score;
    }

    public static HandScore of(int score) {
        return new HandScore(score);
    }

    public static HandScore of(HandScore handScore) {
        if(handScore == null) throw new NullPointerException("Source hand score must not be null.");
        return new HandScore(handScore.get());
    }

    public HandScore increase() {
        if(score == 0) throw new HandScoreException("Can not increase score from 0 (zero).");
        int nextScore = score == 1 ? 3 : score + 3;
        return new HandScore(nextScore);
    }

    public boolean canIncrease() {
        return score != 0 && score != 12;
    }

    private boolean isValid(int score) {
        return score == 0 || score == 1 || (score % 3 == 0 && score >= 3 && score <= 12);
    }

    public int get() {
        return score;
    }

    @Override
    public String toString() {
        return "HandScore{score=" + score + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HandScore handScore = (HandScore) o;

        return score == handScore.score;
    }

    @Override
    public int hashCode() {
        return score;
    }
}
