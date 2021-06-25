package com.bueno.truco.domain.usecases.hand;

import com.bueno.truco.domain.entities.game.Game;
import com.bueno.truco.domain.entities.game.Hand;
import com.bueno.truco.domain.entities.game.HandResult;
import com.bueno.truco.domain.entities.game.Round;
import com.bueno.truco.domain.entities.player.Player;
import com.bueno.truco.domain.usecases.truco.RequestTrucoUseCase;
import com.bueno.truco.domain.usecases.truco.TrucoResult;

import java.util.Optional;

public class PlayHandUseCase {

    private Game game;
    private Hand hand;

    private Player firstToPlay;
    private Player lastToPlay;

    private Player handPointsRequester;

    private final RequestTrucoUseCase trucoUseCase = new RequestTrucoUseCase();

    public PlayHandUseCase(Game game) {
        this.game = game;
    }

    public Hand play() {
        hand = new Hand();

        hand.addPlayedRound(playRound());
        if (hand.hasWinner()) return hand;

        hand.addPlayedRound(playRound());
        if (hand.hasWinner()) return hand;

        hand.checkForWinnerAfterTwoRounds();
        if (hand.hasWinner()) return hand;

        hand.addPlayedRound(playRound());
        if (hand.hasWinner()) return hand;

        hand.checkForWinnerAfterThirdRound();
        return hand;
    }

    private Round playRound() {
        definePlayingOrder();
        if(isFirstToPlayAbleToRequestPointsRise()) {
            Optional<HandResult> handResult = handleTruco();
            if (handResult.isPresent()){
                hand.setResult(handResult.get());
                return new Round(firstToPlay, lastToPlay, game.getCurrentVira());
            }
        }
        Round round = new Round(firstToPlay, lastToPlay, game.getCurrentVira());
        round.play();
        return round;
    }

    private void definePlayingOrder() {
        if (hand.getLastRoundWinner().isPresent() && game.getLastToPlay().equals(hand.getLastRoundWinner().get())) {
            firstToPlay = game.getLastToPlay();
            lastToPlay = game.getFirstToPlay();
        } else {
            firstToPlay = game.getFirstToPlay();
            lastToPlay = game.getLastToPlay();
        }
    }

    private boolean isFirstToPlayAbleToRequestPointsRise() {
        return handPointsRequester == null || !handPointsRequester.equals(firstToPlay);
    }

    private Optional<HandResult> handleTruco() {
        final TrucoResult trucoResult = trucoUseCase.handle(firstToPlay, lastToPlay, hand.getHandPoints());
        HandResult handResult = null;

        if(trucoResult.hasWinner())
            handResult = new HandResult(trucoResult.getWinner().get(), trucoResult.getPoints());

        trucoResult.getLastRequester().ifPresent(requester -> handPointsRequester = requester);

        hand.setHandPoints(trucoResult.getPoints());
        return Optional.ofNullable(handResult);
    }
}
