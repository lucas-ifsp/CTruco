package com.bueno.truco.domain.usecases.game;

import com.bueno.truco.domain.entities.game.Game;
import com.bueno.truco.domain.usecases.hand.PlayHandUseCase;
import com.bueno.truco.domain.entities.player.Player;



public class PlayGameUseCase {

    private Game game;

    public PlayGameUseCase(Player player1, Player player2){
        game = new Game(player1, player2);
    }

    public Player play() {
        while (game.getWinner().isEmpty()){
            game.dealCards();
            game.addHand(new PlayHandUseCase(game).play());
        }
        return game.getWinner().get();
    }
}
