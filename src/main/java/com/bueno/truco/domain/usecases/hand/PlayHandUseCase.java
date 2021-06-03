package com.bueno.truco.domain.usecases.hand;

import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.game.Round;

import java.util.ArrayList;
import java.util.List;

public class PlayHandUseCase {

    private final Player player1;
    private final Player player2;
    private final Card vira;
    private final List<Player> winners = new ArrayList<>();

    public PlayHandUseCase(Player player1, Player player2, Card vira) {
        this.player1 = player1;
        this.player2 = player2;
        this.vira = vira;
    }

    public HandResult play() {
        playRoundAndSetResult(player1.playCard(), player2.playCard());
        playRoundAndSetResult(player1.playCard(), player2.playCard());

        HandResult winner = checkForWinnerAfterTwoRounds();
        if (winner != null)  return winner;

        playRoundAndSetResult(player1.playCard(), player2.playCard());
        return checkForWinnerAfterThirdRound();
    }

    private HandResult checkForWinnerAfterTwoRounds() {
        if(winners.get(0) == null && winners.get(1) != null)
            return(new HandResult(winners.get(1), 1));

        if(winners.get(0) != null && winners.get(1) == null)
            return(new HandResult(winners.get(0), 1));

        if(winners.get(1) != null && winners.get(1).equals(winners.get(0)))
            return(new HandResult(winners.get(1), 1));

        return null;
    }

    private HandResult checkForWinnerAfterThirdRound() {
        if(winners.get(2) == null && winners.get(0) != null)
            return(new HandResult(winners.get(0),1));

        if(winners.get(2) != null)
            return(new HandResult(winners.get(2),1));

        return new HandResult(null, 0);
    }

    private void playRoundAndSetResult(Card card1, Card card2) {
        var round = new Round(card1, card2, vira);
        round.getWinner().ifPresentOrElse(
                card -> winners.add((card.equals(card1)) ? player1 : player2),
                () -> winners.add(null));
    }
}
