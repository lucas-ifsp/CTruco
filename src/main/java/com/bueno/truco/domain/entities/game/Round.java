package com.bueno.truco.domain.entities.game;

import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.player.Player;

import java.util.Optional;

public class Round {

    private final Card vira;
    private final Player firstToPlay;
    private final Player lastToPlay;
    private Player winner;
    private Card firstCard;
    private Card lastCard;
    
    public Round(Player firstToPlay, Player lastToPlay, Card vira) {
        this.firstToPlay = firstToPlay;
        this.lastToPlay = lastToPlay;
        this.vira = vira;
    }

    public void play(){
        firstCard = firstToPlay.playCard();
        lastCard = lastToPlay.playCard();
        validateCards();
        Optional<Card> highestCard = getHighestCard();
        if(highestCard.isPresent())
            winner = (highestCard.get().equals(firstCard) ? firstToPlay : lastToPlay);
        winner = null;

    }

    private void validateCards() {
        if(firstCard == null || lastCard == null || vira == null)
            throw new IllegalArgumentException("Parameters can not be null!");
        if(!firstCard.equals(Card.getClosedCard()) && firstCard.equals(lastCard))
            throw new GameRuleViolationException("Cards in the deck must be unique!");
        if(!firstCard.equals(Card.getClosedCard()) && firstCard.equals(vira))
            throw new GameRuleViolationException("Cards in the deck must be unique!");
        if(!lastCard.equals(Card.getClosedCard()) && lastCard.equals(vira))
            throw new GameRuleViolationException("Cards in the deck must be unique!");
    }

    public Optional<Card> getHighestCard() {
        if (firstCard.compareValueTo(lastCard, vira) == 0)
            return Optional.empty();
        return firstCard.compareValueTo(lastCard, vira) > 0 ? Optional.of(firstCard) : Optional.of(lastCard);
    }

    public Optional<Player> getWinner() {
        return Optional.ofNullable(winner);
    }
}
