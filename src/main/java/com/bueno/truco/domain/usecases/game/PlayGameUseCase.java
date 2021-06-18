package com.bueno.truco.domain.usecases.game;

import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.deck.Deck;
import com.bueno.truco.domain.usecases.hand.HandResult;
import com.bueno.truco.domain.usecases.hand.PlayHandUseCase;
import com.bueno.truco.domain.entities.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

// TODO Solve flickering test caused by taking random cards
public class PlayGameUseCase {

    private Player firstToPlay;
    private Player lastToPlay;

    private static final Logger logger = LoggerFactory.getLogger(PlayGameUseCase.class.getSimpleName());


    public PlayGameUseCase(Player player1, Player player2){
        firstToPlay = player1;
        lastToPlay = player2;
    }

    public Player play() {
        while(true){
            playHand(prepareHand());
            if(getWinner().isPresent()) return getWinner().get();
            changePlayingOrder();
        }
    }

    private Optional<Player> getWinner() {
        if(firstToPlay.getScore() == Player.MAX_SCORE) return Optional.of(firstToPlay);
        if(lastToPlay.getScore() == Player.MAX_SCORE) return Optional.of(lastToPlay);
        return Optional.empty();
    }

    private void playHand(PlayHandUseCase hand) {
        HandResult result = hand.play();
        updateGameStatus(result);

    }

    private void updateGameStatus(HandResult result) {
        Optional<Player> winner = result.getWinner();
        if(winner.isEmpty()) {
            logger.info("Result: draw | Player {}: {} points | Player {}: {} points",
                    firstToPlay, firstToPlay.getScore(), lastToPlay, lastToPlay.getScore() );
            return;
        }

        if(winner.get().equals(firstToPlay))
            firstToPlay.incrementScoreBy(result.getPoints());
        else
            lastToPlay.incrementScoreBy(result.getPoints());

        logger.info("Result: {} wins | Player {}: {} points | Player {}: {} points",
                winner.get(), firstToPlay, firstToPlay.getScore(), lastToPlay, lastToPlay.getScore() );
    }


    private PlayHandUseCase prepareHand() {
        Deck deck = new Deck();
        deck.shuffle();

        firstToPlay.setCards(deck.take(3));
        lastToPlay.setCards(deck.take(3));
        Card vira = deck.takeOne();

        return new PlayHandUseCase(firstToPlay, lastToPlay, vira);
    }

    private void changePlayingOrder() {
        Player swap = firstToPlay;
        firstToPlay = lastToPlay;
        lastToPlay = swap;
    }

}
