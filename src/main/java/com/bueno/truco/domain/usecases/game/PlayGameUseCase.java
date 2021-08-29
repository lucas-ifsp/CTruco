package com.bueno.truco.domain.usecases.game;

import com.bueno.truco.domain.entities.game.Game;
import com.bueno.truco.domain.entities.hand.Intel;
import com.bueno.truco.domain.entities.hand.Hand;
import com.bueno.truco.domain.usecases.hand.PlayHandUseCase;
import com.bueno.truco.domain.entities.player.Player;

public class PlayGameUseCase {

    private Game game;

    public PlayGameUseCase(Player player1, Player player2){
        game = new Game(player1, player2);
    }

    public Intel playNewHand(){
        Intel intel = null;

        if(game.getWinner().isEmpty()) {
            final Hand playedHand = new PlayHandUseCase(game).play();
            intel = playedHand.getIntel();
            game.updateScores();
        }
        return intel;
    }
}
