package com.bueno.truco.domain.entities.hand;

import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.deck.Deck;
import com.bueno.truco.domain.entities.game.GameRuleViolationException;
import com.bueno.truco.domain.entities.round.Round;
import com.bueno.truco.domain.entities.player.Player;

import java.util.*;
import java.util.logging.Logger;

public class Hand {

    private Card vira;

    private final List<Card> openCards;
    private final List<Round> roundsPlayed;

    private Player firstToPlay;
    private Player lastToPlay;
    private Player lastScoreIncrementRequester;

    private Card cardToPlayAgainst;
    private HandScore score;
    private HandResult result;

    private final static Logger LOGGER = Logger.getLogger(Hand.class.getName());

    public Hand(Player firstToPlay, Player lastToPlay){
        this.firstToPlay = firstToPlay;
        this.lastToPlay = lastToPlay;
        dealCards();

        score = HandScore.of(1);
        roundsPlayed = new ArrayList<>();
        openCards = new ArrayList<>();
        addOpenCard(vira);
    }

    public void dealCards() {
        Deck deck = new Deck();
        deck.shuffle();

        firstToPlay.setCards(deck.take(3));
        lastToPlay.setCards(deck.take(3));
        vira = deck.takeOne();

        LOGGER.info("Vira: " + vira);
    }

    public void playNewRound(){
        if(roundsPlayed.size() == 3)
            throw new GameRuleViolationException("The number of rounds exceeded the maximum of three.");

        defineRoundPlayingOrder();
        Round round = new Round(firstToPlay, lastToPlay, this);
        round.play();

        firstToPlay.handleRoundConclusion();
        lastToPlay.handleRoundConclusion();
        roundsPlayed.add(round);
    }

    private void defineRoundPlayingOrder() {
        getLastRoundWinner().ifPresent(winner -> {
            if(winner.equals(lastToPlay))
                changePlayingOrder();
        });
    }

    private void changePlayingOrder() {
        Player referenceHolder = firstToPlay;
        firstToPlay = lastToPlay;
        lastToPlay = referenceHolder;
    }

    public void checkForWinnerAfterSecondRound() {
        Optional<Player> firstRoundWinner = roundsPlayed.get(0).getWinner();
        Optional<Player> secondRoundWinner = roundsPlayed.get(1).getWinner();

        if (firstRoundWinner.isEmpty() && secondRoundWinner.isPresent())
            result = new HandResult(secondRoundWinner.get(), score);
        else if (firstRoundWinner.isPresent() && secondRoundWinner.isEmpty())
            result =  new HandResult(firstRoundWinner.get(), score);
        else if (secondRoundWinner.isPresent() && secondRoundWinner.get().equals(firstRoundWinner.get()))
            result = new HandResult(secondRoundWinner.get(), score);
    }

    public void checkForWinnerAfterThirdRound() {
        Optional<Player> firstRoundWinner = roundsPlayed.get(0).getWinner();
        Optional<Player> lastRoundWinner = roundsPlayed.get(2).getWinner();

        if (lastRoundWinner.isEmpty() && firstRoundWinner.isPresent())
            result = new HandResult(firstRoundWinner.get(), score);
        else
            result = lastRoundWinner.
                    map(player -> new HandResult(player, score))
                    .orElseGet(HandResult::new);
    }

    private void updatePlayersIntel() {
        firstToPlay.setIntel(new Intel(this));
        lastToPlay.setIntel(new Intel(this));
    }

    public void setCardToPlayAgainst(Card cardToPlayAgainst) {
        this.cardToPlayAgainst = cardToPlayAgainst;
        updatePlayersIntel();
    }

    public Optional<Card> getCardToPlayAgainst() {
        return Optional.ofNullable(cardToPlayAgainst);
    }

    public void addOpenCard(Card card){
        openCards.add(card);
        updatePlayersIntel();
    }

    public Optional<Player> getLastRoundWinner(){
        if(roundsPlayed.isEmpty()) return Optional.empty();
        return roundsPlayed.get(roundsPlayed.size() - 1).getWinner();
    }

    public List<Round> getRoundsPlayed() {
        return new ArrayList<>(roundsPlayed);
    }

    public Optional<HandResult> getResult() {
        return Optional.ofNullable(result);
    }

    public void setResult(HandResult result) {
        this.result = result;
    }

    public boolean hasWinner(){
        return result != null;
    }

    public void setScore(HandScore score) {
        this.score = HandScore.of(score);
        updatePlayersIntel();
    }

    public Player getFirstToPlay() {
        return firstToPlay;
    }

    public Player getLastToPlay() {
        return lastToPlay;
    }

    public HandScore getScore() {
        return score;
    }

    public Player getLastScoreIncrementRequester() {
        return lastScoreIncrementRequester;
    }

    public void setLastScoreIncrementRequester(Player lastScoreIncrementRequester) {
        this.lastScoreIncrementRequester = lastScoreIncrementRequester;
    }

    public Card getVira() {
        return vira;
    }

    public List<Card> getOpenCards() {
        return openCards;
    }

    public Intel getIntel(){
        return new Intel(this);
    }
}
