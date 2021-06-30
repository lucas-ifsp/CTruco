package com.bueno.truco.domain.usecases.hand;

import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.deck.Suit;
import com.bueno.truco.domain.entities.game.Game;
import com.bueno.truco.domain.entities.game.HandResult;
import com.bueno.truco.domain.entities.player.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayHandUseCaseTest {

    @Mock
    private Game game;
    @Mock
    private Player p1;
    @Mock
    private Player p2;

    @Test
    void shouldHaveHandWinnerAfterTwoRounds(){
        when(p1.playCard()).thenReturn(new Card(5, Suit.DIAMONDS)).thenReturn(new Card(5, Suit.SPADES)).thenReturn(new Card(5, Suit.HEARTS));
        when(p2.playCard()).thenReturn(new Card(4, Suit.DIAMONDS)).thenReturn(new Card(4, Suit.SPADES)).thenReturn(new Card(4, Suit.HEARTS));
        configureGameMock(new Card(6, Suit.DIAMONDS));

        PlayHandUseCase hand = new PlayHandUseCase(game);
        HandResult result = hand.play().getResult().get();
        Assertions.assertEquals(p1, result.getWinner().orElse(null));
    }

    @Test
    void shouldHaveHandWinnerAfterThreeRounds(){
        when(p1.playCard()).thenReturn(new Card(4, Suit.DIAMONDS)).thenReturn(new Card(7, Suit.SPADES)).thenReturn( new Card('Q', Suit.HEARTS));
        when(p2.playCard()).thenReturn(new Card(5, Suit.CLUBS)).thenReturn(new Card(6, Suit.DIAMONDS)).thenReturn(new Card('Q', Suit.CLUBS));
        configureGameMock(new Card(7, Suit.DIAMONDS));

        PlayHandUseCase hand = new PlayHandUseCase(game);
        HandResult result = hand.play().getResult().get();
        Assertions.assertEquals(p2, result.getWinner().orElse(null));
    }

    @Test
    void shouldWinGameIfOpponentRuns(){
        when(p1.requestTruco()).thenReturn(true);
        when(p2.getTrucoResponse(anyInt())).thenReturn(-1);
        when(game.getFirstToPlay()).thenReturn(p1);
        when(game.getLastToPlay()).thenReturn(p2);

        PlayHandUseCase hand = new PlayHandUseCase(game);
        HandResult result = hand.play().getResult().get();
        Assertions.assertEquals(p1, result.getWinner().orElse(null));
    }

    private void configureGameMock(Card vira) {
        when(game.getFirstToPlay()).thenReturn(p1);
        when(game.getLastToPlay()).thenReturn(p2);
        when(game.getCurrentVira()).thenReturn(vira);
    }
}