package com.bueno.truco.domain.entities.game;

import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.player.Player;

import java.util.Optional;

public class Round {

    private final Player firstToPlay;
    private final Player lastToPlay;
    private Player winner;
    private Card firstCard;
    private Card lastCard;
    private Game game;
    
    public Round(Player firstToPlay, Player lastToPlay, Game game) {
        validateConstructorInputs(firstToPlay, lastToPlay, game);
        this.firstToPlay = firstToPlay;
        this.lastToPlay = lastToPlay;
        this.game = game;
        game.setCardToPlayAgainst(null);
    }

    private void validateConstructorInputs(Player firstToPlay, Player lastToPlay, Game game) {
        if(firstToPlay == null || lastToPlay == null || game == null)
            throw new IllegalArgumentException("Parameters must not be null!");
    }

    public void play(){
        winner = null;

        firstCard = firstToPlay.playCard();
        game.setCardToPlayAgainst(firstCard);
        game.addOpenCard(firstCard);

        lastCard = lastToPlay.playCard();
        game.setCardToPlayAgainst(null);
        game.addOpenCard(lastCard);

        validateCards();
        Optional<Card> highestCard = getHighestCard();

        if(highestCard.isPresent())
            winner = (highestCard.get().equals(firstCard) ? firstToPlay : lastToPlay);
    }

    private void validateCards() {
        if(firstCard == null || lastCard == null || game.getCurrentVira() == null)
            throw new IllegalArgumentException("Parameters can not be null!");
        if(!firstCard.equals(Card.getClosedCard()) && firstCard.equals(lastCard))
            throw new GameRuleViolationException("Cards in the deck must be unique!");
        if(!firstCard.equals(Card.getClosedCard()) && firstCard.equals(game.getCurrentVira()))
            throw new GameRuleViolationException("Cards in the deck must be unique!");
        if(!lastCard.equals(Card.getClosedCard()) && lastCard.equals(game.getCurrentVira()))
            throw new GameRuleViolationException("Cards in the deck must be unique!");
    }

    public Optional<Card> getHighestCard() {
        if (firstCard.compareValueTo(lastCard, game.getCurrentVira()) == 0)
            return Optional.empty();
        return firstCard.compareValueTo(lastCard, game.getCurrentVira()) > 0 ?
                Optional.of(firstCard) : Optional.of(lastCard);
    }

    public Optional<Player> getWinner() {
        return Optional.ofNullable(winner);
    }
}
