package com.bueno.truco.domain.entities.game;

import com.bueno.truco.domain.entities.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Hand {

    private final List<Round> roundsPlayed;
    private final List<Player> winners = new ArrayList<>();
    private int handPoints;

    private HandResult result;

    public Hand(){
        roundsPlayed = new ArrayList<>();
        handPoints = 1;
    }

    public void playRound(Round round){
        if(roundsPlayed.size() == 3)
            throw new GameRuleViolationException("The number of rounds exceeded the maximum of three.");
        roundsPlayed.add(round);
        winners.add(round.getWinner().orElse(null)); // TODO remove Winner List
    }

    public void checkForWinnerAfterTwoRounds() {
        if (winners.get(0) == null && winners.get(1) != null)
            result = new HandResult(winners.get(1), handPoints);
        else if (winners.get(0) != null && winners.get(1) == null)
            result =  new HandResult(winners.get(0), handPoints);
        else if (winners.get(1) != null && winners.get(1).equals(winners.get(0)))
            result = new HandResult(winners.get(1), handPoints);
    }

    public void checkForWinnerAfterThirdRound() {
        if (winners.get(2) == null && winners.get(0) != null)
            result = new HandResult(winners.get(0), handPoints);
        else if (winners.get(2) != null)
            result = new HandResult(winners.get(2), handPoints);
        else result = new HandResult(null, 0);
    }

    public boolean hasWinner(){
        return result != null;
    }

    public Optional<HandResult> getResult() {
        return Optional.ofNullable(result);
    }

    public void setResult(HandResult result) {
        this.result = result;
    }

    public void setHandPoints(int handPoints) {
        this.handPoints = handPoints;
    }

    public int getHandPoints() {
        return handPoints;
    }

    public Optional<Player> getLastRoundWinner(){
        if(winners.isEmpty()) return Optional.empty();
        Player lastRoundWinner = winners.get(winners.size() - 1);
        return Optional.ofNullable(lastRoundWinner);
    }
}
