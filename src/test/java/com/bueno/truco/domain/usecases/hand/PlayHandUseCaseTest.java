package com.bueno.truco.domain.usecases.hand;

import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.deck.Suit;
import com.bueno.truco.domain.entities.player.Player;
import com.bueno.truco.domain.usecases.game.PlayGameUseCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayHandUseCaseTest {

    @Mock
    private Player p1;
    @Mock
    private Player p2;

    @Test
    void shouldWinHandWinningFirstTwoRounds(){
        when(p1.playCard()).thenReturn(new Card(5, Suit.DIAMONDS)).thenReturn(new Card(5, Suit.SPADES)).thenReturn(new Card(5, Suit.HEARTS));
        when(p2.playCard()).thenReturn(new Card(4, Suit.DIAMONDS)).thenReturn(new Card(4, Suit.SPADES)).thenReturn(new Card(4, Suit.HEARTS));
        PlayHandUseCase hand = new PlayHandUseCase(p1, p2, new Card(6, Suit.DIAMONDS));
        HandResult result = hand.play();
        Assertions.assertEquals(p1, result.getWinner().orElse(null));
    }

    @Test
    void shouldWinHandTyingFirstAndWinningSecond(){
        when(p1.playCard()).thenReturn(new Card(4, Suit.DIAMONDS)).thenReturn(new Card(4, Suit.SPADES)).thenReturn(new Card(4, Suit.HEARTS));
        when(p2.playCard()).thenReturn(new Card(4, Suit.CLUBS)).thenReturn(new Card(5, Suit.SPADES)).thenReturn(new Card(5, Suit.HEARTS));
        PlayHandUseCase hand = new PlayHandUseCase(p1, p2, new Card(6, Suit.DIAMONDS));
        HandResult result = hand.play();
        Assertions.assertEquals(p2, result.getWinner().orElse(null));
    }

    @Test
    void shouldWinHandWinningFirstAndTyingSecond(){
        when(p1.playCard()).thenReturn(new Card(5, Suit.DIAMONDS)).thenReturn(new Card(4, Suit.SPADES)).thenReturn(new Card(4, Suit.HEARTS));
        when(p2.playCard()).thenReturn(new Card(6, Suit.CLUBS)).thenReturn(new Card(4, Suit.DIAMONDS)).thenReturn(new Card(5, Suit.HEARTS));
        PlayHandUseCase hand = new PlayHandUseCase(p1, p2, new Card(4, Suit.CLUBS));
        HandResult result = hand.play();
        Assertions.assertEquals(p1, result.getWinner().orElse(null));
    }

    @Test
    void shouldDrawHandWithThreeTiedRounds(){
        when(p1.playCard()).thenReturn(new Card(4, Suit.CLUBS)).thenReturn(new Card(5, Suit.DIAMONDS)).thenReturn(new Card(6, Suit.CLUBS));
        when(p2.playCard()).thenReturn(new Card(4, Suit.DIAMONDS)).thenReturn(new Card(5, Suit.SPADES)).thenReturn(new Card(6, Suit.HEARTS));
        PlayHandUseCase hand = new PlayHandUseCase(p1, p2, new Card(7, Suit.DIAMONDS));
        HandResult result = hand.play();
        Assertions.assertTrue(result.getWinner().isEmpty());
    }

    @Test
    void shouldWinHandByBestOfThree(){
        when(p1.playCard()).thenReturn(new Card(4, Suit.DIAMONDS)).thenReturn(new Card(7, Suit.SPADES)).thenReturn( new Card('Q', Suit.HEARTS));
        when(p2.playCard()).thenReturn(new Card(5, Suit.CLUBS)).thenReturn(new Card(6, Suit.DIAMONDS)).thenReturn(new Card('Q', Suit.CLUBS));
        PlayHandUseCase hand = new PlayHandUseCase(p1, p2, new Card(7, Suit.DIAMONDS));
        HandResult result = hand.play();
        Assertions.assertEquals(p2, result.getWinner().orElse(null));
    }

    @Test
    void shouldWinWinningFirstAndTyingThird(){
        when(p1.playCard()).thenReturn(new Card(4, Suit.DIAMONDS)).thenReturn(new Card(7, Suit.SPADES)).thenReturn(new Card('Q', Suit.HEARTS));
        when(p2.playCard()).thenReturn(new Card(5, Suit.CLUBS)).thenReturn(new Card(6, Suit.DIAMONDS)).thenReturn(new Card('Q', Suit.CLUBS));
        PlayHandUseCase hand = new PlayHandUseCase(p1, p2, new Card('Q', Suit.DIAMONDS));
        HandResult result = hand.play();
        Assertions.assertEquals(p2, result.getWinner().orElse(null));
    }
}