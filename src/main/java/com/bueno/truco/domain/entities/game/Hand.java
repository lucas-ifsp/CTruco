package com.bueno.truco.domain.entities.game;

import com.bueno.truco.domain.entities.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Hand {

    private final List<Round> roundsPlayed;
    private int handPoints;

    private HandResult result;

    public Hand(){
        roundsPlayed = new ArrayList<>();
        handPoints = 1;
    }

    public void addPlayedRound(Round round){
        if(roundsPlayed.size() == 3)
            throw new GameRuleViolationException("The number of rounds exceeded the maximum of three.");
        roundsPlayed.add(round);
    }

    public void checkForWinnerAfterTwoRounds() {
        Optional<Player> firstRoundWinner = roundsPlayed.get(0).getWinner();
        Optional<Player> secondRoundWinner = roundsPlayed.get(1).getWinner();

        if (firstRoundWinner.isEmpty() && secondRoundWinner.isPresent())
            result = new HandResult(secondRoundWinner.get(), handPoints);
        else if (firstRoundWinner.isPresent() && secondRoundWinner.isEmpty())
            result =  new HandResult(firstRoundWinner.get(), handPoints);
        else if (secondRoundWinner.isPresent() && secondRoundWinner.get().equals(firstRoundWinner.get()))
            result = new HandResult(secondRoundWinner.get(), handPoints);
    }

    public void checkForWinnerAfterThirdRound() {
        Optional<Player> firstRoundWinner = roundsPlayed.get(0).getWinner();
        Optional<Player> lastRoundWinner = roundsPlayed.get(2).getWinner();

        if (lastRoundWinner.isEmpty() && firstRoundWinner.isPresent())
            result = new HandResult(firstRoundWinner.get(), handPoints);
        else if (lastRoundWinner.isPresent())
            result = new HandResult(lastRoundWinner.get(), handPoints);
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
        if(roundsPlayed.isEmpty()) return Optional.empty();
        return roundsPlayed.get(roundsPlayed.size() - 1).getWinner();
    }
}
