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

    public PlayHandUseCase(Game game) {
        this.game = game;
    }

    public Hand play() {
        hand = new Hand();
        game.setCurrentHand(hand);

        hand.addPlayedRound(playRound());
        if (hand.hasWinner()) return hand;

        hand.addPlayedRound(playRound());
        if (hand.hasWinner()) return hand;

        hand.checkForWinnerAfterSecondRound();
        if (hand.hasWinner()) return hand;

        hand.addPlayedRound(playRound());
        if (hand.hasWinner()) return hand;

        hand.checkForWinnerAfterThirdRound();
        return hand;
    }

    private Round playRound() {
        definePlayingOrder();
        Round round = new Round(firstToPlay, lastToPlay, game);
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
}
