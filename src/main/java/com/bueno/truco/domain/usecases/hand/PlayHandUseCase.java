package com.bueno.truco.domain.usecases.hand;

import com.bueno.truco.domain.entities.game.Game;
import com.bueno.truco.domain.entities.hand.Hand;
import com.bueno.truco.domain.entities.hand.HandResult;
import com.bueno.truco.domain.entities.hand.HandScore;
import com.bueno.truco.domain.entities.player.Player;

public class PlayHandUseCase {

    private Game game;
    private Hand hand;

    private Player firstToPlay;
    private Player lastToPlay;

    public PlayHandUseCase(Game game) {
        this.game = game;
    }

    public Hand play() {
        hand = game.prepareNewHand();

        if(game.isMaoDeOnze()){
            handleMaoDeOnze();
            if (hand.hasWinner()) return hand;
        }

        playRound();
        if (hand.hasWinner()) return hand;

        playRound();
        if (hand.hasWinner()) return hand;

        hand.checkForWinnerAfterSecondRound();
        if (hand.hasWinner()) return hand;

        playRound();
        if (hand.hasWinner()) return hand;

        hand.checkForWinnerAfterThirdRound();
        return hand;
    }

    private void handleMaoDeOnze() {
        Player playerInMaoDeOnze = game.getPlayer1().getScore() == 11? game.getPlayer1() : game.getPlayer2();
        Player otherPlayer = game.getPlayer1().getScore() == 11? game.getPlayer2() : game.getPlayer1();

        if(playerInMaoDeOnze.getMaoDeOnzeResponse())
            hand.setScore(HandScore.of(3));
        else
            hand.setResult(new HandResult(otherPlayer, HandScore.of(1)));
    }

    private void playRound() {
        definePlayingOrder();
        hand.playNewRound(firstToPlay, lastToPlay);
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
