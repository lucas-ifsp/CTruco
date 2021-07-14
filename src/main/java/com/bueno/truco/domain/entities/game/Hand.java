package com.bueno.truco.domain.entities.game;

import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.player.Player;
import com.bueno.truco.domain.entities.utils.Observable;
import com.bueno.truco.domain.entities.utils.Observer;

import java.util.*;

public class Hand implements Observable {

    private final Game game;
    private final Card vira;

    private final Set<Card> openCards;
    private final List<Round> roundsPlayed;
    private final List<Observer> observers;

    private Card cardToPlayAgainst;
    private Player pointsRequester;
    private int handPoints;
    private HandResult result;

    public Hand(Game game, Card vira){
        this.game = game;
        this.vira = vira;
        this.handPoints = 1;

        roundsPlayed = new ArrayList<>();
        openCards = new LinkedHashSet<>();
        this.openCards.add(vira);

        observers = new ArrayList<>();
        registerObserver(getPlayer1());
        registerObserver(getPlayer2());

        notifyObservers();
    }

    public void playNewRound(Player firstToPlay, Player lastToPlay){
        if(roundsPlayed.size() == 3)
            throw new GameRuleViolationException("The number of rounds exceeded the maximum of three.");

        Round round = new Round(firstToPlay, lastToPlay, this);
        round.play();
        roundsPlayed.add(round);
    }

    public void checkForWinnerAfterSecondRound() {
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
        else
            result = lastRoundWinner.
                    map(player -> new HandResult(player, handPoints))
                    .orElseGet(() -> new HandResult(null, 0));
    }

    @Override
    public void notifyObservers() {
        observers.forEach(observer -> observer.update(new GameIntel(this)));
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    public void setCardToPlayAgainst(Card cardToPlayAgainst) {
        this.cardToPlayAgainst = cardToPlayAgainst;
        notifyObservers();
    }

    public Optional<Card> getCardToPlayAgainst() {
        return Optional.ofNullable(cardToPlayAgainst);
    }

    public void addOpenCard(Card card){
        openCards.add(card);
        notifyObservers();
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

    public void setHandPoints(int handPoints) {
        this.handPoints = handPoints;
        notifyObservers();
    }

    public int getPoints() {
        return handPoints;
    }

    public Player getPointsRequester() {
        return pointsRequester;
    }

    public void setPointsRequester(Player pointsRequester) {
        this.pointsRequester = pointsRequester;
    }

    public Card getVira() {
        return vira;
    }

    public Set<Card> getOpenCards() {
        return openCards;
    }

    public Player getPlayer1(){
        return game.getPlayer1();
    }

    public Player getPlayer2(){
        return game.getPlayer2();
    }

    public GameIntel getGameIntel(){
        return new GameIntel(this);
    }
}
