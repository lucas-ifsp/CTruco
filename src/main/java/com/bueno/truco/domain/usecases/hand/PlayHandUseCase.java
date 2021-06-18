package com.bueno.truco.domain.usecases.hand;

import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.game.Round;
import com.bueno.truco.domain.entities.player.Player;
import com.bueno.truco.domain.usecases.truco.RequestTrucoUseCase;
import com.bueno.truco.domain.usecases.truco.TrucoResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlayHandUseCase {

    private final Player player1;
    private final Player player2;

    private Player firstToPlay;
    private Player lastToPlay;

    private final Card vira;
    private final List<Player> winners = new ArrayList<>();
    private Player handPointsRequester;
    private int handPoints = 1;

    private final RequestTrucoUseCase trucoUseCase = new RequestTrucoUseCase();


    public PlayHandUseCase(Player player1, Player player2, Card vira) {
        this.player1 = player1;
        this.player2 = player2;
        this.vira = vira;
    }

    public HandResult play() {
        Optional<HandResult> handResult;

        definePlayingOrder(1);
        if(isFirstToPlayAbleToRequestPointsRise()) {
            handResult = handleTruco();
            if (handResult.isPresent())
                return handResult.get();
        }
        playAndSetResult();

        definePlayingOrder(2);
        if(isFirstToPlayAbleToRequestPointsRise()) {
            handResult = handleTruco();
            if (handResult.isPresent())
                return handResult.get();
        }
        playAndSetResult();

        handResult = checkForWinnerAfterTwoRounds();
        if (handResult.isPresent())
            return handResult.get();

        definePlayingOrder(3);
        if(isFirstToPlayAbleToRequestPointsRise()) {
            handResult = handleTruco();
            if (handResult.isPresent())
                return handResult.get();
        }
        playAndSetResult();

        return checkForWinnerAfterThirdRound();
    }

    private void definePlayingOrder(int roundNumber) {
        final int lastWinnerIndex = roundNumber - 2;
        if (lastWinnerIndex > 0 && player2.equals(winners.get(lastWinnerIndex))) {
            firstToPlay = player2;
            lastToPlay = player1;
        } else {
            firstToPlay = player1;
            lastToPlay = player2;
        }
    }

    private boolean isFirstToPlayAbleToRequestPointsRise() {
        return handPointsRequester == null || !handPointsRequester.equals(firstToPlay);
    }

    private Optional<HandResult> handleTruco() {
        TrucoResult trucoResult = trucoUseCase.handle(firstToPlay, lastToPlay, handPoints);
        HandResult handResult = null;
        if (trucoResult.getWinner().isPresent())
            handResult = new HandResult(trucoResult.getWinner().get(), trucoResult.getPoints());

        handPoints = trucoResult.getPoints();
        return Optional.ofNullable(handResult);
    }

    private void playAndSetResult() {
        Card fistCard = firstToPlay.playCard();
        Card lastCard = lastToPlay.playCard();
        var round = new Round(fistCard, lastCard, vira);
        round.getWinner().ifPresentOrElse(
                card -> winners.add((card.equals(fistCard)) ? firstToPlay : lastToPlay),
                () -> winners.add(null));
    }

    private Optional<HandResult> checkForWinnerAfterTwoRounds() {
        HandResult result = null;

        if (winners.get(0) == null && winners.get(1) != null)
            result = new HandResult(winners.get(1), handPoints);
        if (winners.get(0) != null && winners.get(1) == null)
            result = new HandResult(winners.get(0), handPoints);
        if (winners.get(1) != null && winners.get(1).equals(winners.get(0)))
            result = new HandResult(winners.get(1), handPoints);

        return Optional.ofNullable(result);
    }

    private HandResult checkForWinnerAfterThirdRound() {
        if (winners.get(2) == null && winners.get(0) != null)
            return (new HandResult(winners.get(0), handPoints));
        if (winners.get(2) != null)
            return (new HandResult(winners.get(2), handPoints));
        return new HandResult(null, 0);
    }
}
