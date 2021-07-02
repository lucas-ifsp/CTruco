package com.bueno.truco.domain.entities.game;

import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.player.Player;
import com.bueno.truco.domain.usecases.truco.RequestTrucoUseCase;
import com.bueno.truco.domain.usecases.truco.TrucoResult;

import java.util.Optional;

public class Round {

    private final Player firstToPlay;
    private final Player lastToPlay;
    private Player winner;
    private Card firstCard;
    private Card lastCard;
    private Game game;
    private Card vira;
    private Hand hand;

    private final RequestTrucoUseCase trucoUseCase = new RequestTrucoUseCase();

    public Round(Player firstToPlay, Player lastToPlay, Game game) {
        validateConstructorInputs(firstToPlay, lastToPlay, game);
        this.firstToPlay = firstToPlay;
        this.lastToPlay = lastToPlay;
        this.game = game;
        this.vira = game.getCurrentVira();
        this.hand = game.getCurrentHand();
        this.game.setCardToPlayAgainst(null);
    }

    private void validateConstructorInputs(Player firstToPlay, Player lastToPlay, Game game) {
        if(firstToPlay == null || lastToPlay == null || game == null)
            throw new IllegalArgumentException("Parameters must not be null!");
        if(game.getCurrentVira() == null)
            throw new IllegalArgumentException("You must have a vira before creating a round!");
        if(game.getCurrentHand() == null)
            throw new IllegalArgumentException("You must have a hand in order to create a round!");
    }

    public void play(){
        winner = null;

        if(isPlayerAbleToRequestPointsRise(firstToPlay)) {
            final boolean hasWinnerByRun = handleTruco(firstToPlay, lastToPlay).isPresent();
            if (hasWinnerByRun) return;
        }

        firstCard = firstToPlay.playCard();
        game.setCardToPlayAgainst(firstCard);
        game.addOpenCard(firstCard);

        if(isPlayerAbleToRequestPointsRise(lastToPlay)) {
            final boolean hasWinnerByRun = handleTruco(lastToPlay, firstToPlay).isPresent();
            if (hasWinnerByRun) return;
        }

        lastCard = lastToPlay.playCard();
        game.setCardToPlayAgainst(null);
        game.addOpenCard(lastCard);

        validateCards();
        Optional<Card> highestCard = getHighestCard();

        if(highestCard.isPresent())
            winner = (highestCard.get().equals(firstCard) ? firstToPlay : lastToPlay);
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

    private boolean isPlayerAbleToRequestPointsRise(Player requester) {
        return hand.getPointsRequester() == null || ! hand.getPointsRequester().equals(requester);
    }

    private Optional<HandResult> handleTruco(Player requester, Player responder) {
        final TrucoResult trucoResult = trucoUseCase.handle(requester, responder, hand.getHandPoints());
        HandResult handResult = null;

        if(trucoResult.hasWinner()) {
            handResult = new HandResult(trucoResult.getWinner().get(), trucoResult.getPoints());
            hand.setResult(handResult);
        }

        trucoResult.getLastRequester().ifPresent(lastRequester -> hand.setPointsRequester(lastRequester));
        hand.setHandPoints(trucoResult.getPoints());
        game.updateCurrentHandPoints();

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
