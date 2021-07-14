package com.bueno.truco.domain.usecases.game;

import com.bueno.truco.domain.entities.game.Game;
import com.bueno.truco.domain.entities.game.GameIntel;
import com.bueno.truco.domain.entities.game.Hand;
import com.bueno.truco.domain.usecases.hand.PlayHandUseCase;
import com.bueno.truco.domain.entities.player.Player;

import java.util.Optional;

public class PlayGameUseCase {

    private Game game;

    public PlayGameUseCase(Player player1, Player player2){
        game = new Game(player1, player2);
    }

    public Player play() {
        while (true){
            if(playNewHand() == null)
                return game.getWinner().get();
        }
    }

    public GameIntel playNewHand(){
        GameIntel gameIntel = null;

        if(game.getWinner().isEmpty()) {
            game.dealCards();
            final Hand playedHand = new PlayHandUseCase(game).play();
            gameIntel = playedHand.getGameIntel();
            game.updateScores();
        }
        return gameIntel;
    }
}
