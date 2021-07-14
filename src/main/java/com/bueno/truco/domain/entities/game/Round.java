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
    private Card vira;
    private Hand hand;

    public Round(Player firstToPlay, Player lastToPlay, Hand hand) {
        validateConstructorInputs(firstToPlay, lastToPlay, hand);
        this.firstToPlay = firstToPlay;
        this.lastToPlay = lastToPlay;
        this.hand = hand;
        this.vira = hand.getVira();
        this.hand.setCardToPlayAgainst(null);
    }

    private void validateConstructorInputs(Player firstToPlay, Player lastToPlay, Hand hand) {
        if(firstToPlay == null || lastToPlay == null || hand == null)
            throw new IllegalArgumentException("Parameters must not be null!");
    }

    public void play(){
        winner = null;

        if(isPlayerAbleToRequestPointsRise(firstToPlay)) {
            final boolean hasWinnerByRun = handleTruco(firstToPlay, lastToPlay).isPresent();
            if (hasWinnerByRun) return;
        }

        firstCard = firstToPlay.playCard();
        hand.setCardToPlayAgainst(firstCard);
        hand.addOpenCard(firstCard);

        if(isPlayerAbleToRequestPointsRise(lastToPlay)) {
            final boolean hasWinnerByRun = handleTruco(lastToPlay, firstToPlay).isPresent();
            if (hasWinnerByRun) return;
        }

        lastCard = lastToPlay.playCard();
        hand.setCardToPlayAgainst(null);
        hand.addOpenCard(lastCard);

        validateCards();
        Optional<Card> highestCard = getHighestCard();

        highestCard.ifPresent(card -> winner = (card.equals(firstCard) ? firstToPlay : lastToPlay));
    }

    private void validateCards() {
        if(firstCard == null || lastCard == null || vira == null)
            throw new GameRuleViolationException("Cards must not be null!");
        if(!firstCard.equals(Card.getClosedCard()) && firstCard.equals(lastCard))
            throw new GameRuleViolationException("Cards in the deck must be unique!");
        if(!firstCard.equals(Card.getClosedCard()) && firstCard.equals(vira))
            throw new GameRuleViolationException("Cards in the deck must be unique!");
        if(!lastCard.equals(Card.getClosedCard()) && lastCard.equals(vira))
            throw new GameRuleViolationException("Cards in the deck must be unique!");
    }

    private boolean isPlayerAbleToRequestPointsRise(Player requester) {
        return hand.getPointsRequester() == null || ! hand.getPointsRequester().equals(requester);
    }

    private Optional<HandResult> handleTruco(Player requester, Player responder) {
        final Truco truco = new Truco(requester, responder);
        final TrucoResult trucoResult = truco.handle(hand.getPoints());
        HandResult handResult = null;

        if(trucoResult.hasWinner()) {
            winner = trucoResult.getWinner().get();
            handResult = new HandResult(trucoResult);
            hand.setResult(handResult);
        }

        trucoResult.getLastRequester().ifPresent(lastRequester -> hand.setPointsRequester(lastRequester));
        hand.setHandPoints(trucoResult.getPoints());

        return Optional.ofNullable(handResult);
    }

    public Optional<Card> getHighestCard() {
        if (firstCard.compareValueTo(lastCard, vira) == 0)
            return Optional.empty();
        return firstCard.compareValueTo(lastCard, vira) > 0 ?
                Optional.of(firstCard) : Optional.of(lastCard);
    }

    public Optional<Player> getWinner() {
        return Optional.ofNullable(winner);
    }
}
