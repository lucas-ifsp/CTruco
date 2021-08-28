package com.bueno.truco.domain.entities.hand;


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
        if(score == 0 || score == 12) return false;
        return true;
    }

    private boolean isValid(int score) {
        if(score == 0 || score == 1 || (score % 3 == 0 && score >= 3 && score <=12)) return  true;
        return  false;
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
