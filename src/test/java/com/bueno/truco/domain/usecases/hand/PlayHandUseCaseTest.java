package com.bueno.truco.domain.usecases.hand;

import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.deck.Suit;
import com.bueno.truco.domain.entities.player.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class PlayHandUseCaseTest {

    @Test
    void shouldWinHandWinningFirstTwoRounds(){
        Player p1 = new PlayerMock("B", List.of(new Card(5, Suit.DIAMONDS), new Card(5, Suit.SPADES), new Card(5, Suit.HEARTS)));
        Player p2 = new PlayerMock("A", List.of(new Card(4, Suit.DIAMONDS), new Card(4, Suit.SPADES), new Card(4, Suit.HEARTS)));
        PlayHandUseCase hand = new PlayHandUseCase(p1, p2, new Card(6, Suit.DIAMONDS));
        HandResult result = hand.play();
        Assertions.assertEquals(p1, result.getWinner().orElse(null));
    }

    @Test
    void shouldWinHandTyingFirstAndWinningSecond(){
        Player p1 = new PlayerMock("A", List.of(new Card(4, Suit.DIAMONDS), new Card(4, Suit.SPADES), new Card(4, Suit.HEARTS)));
        Player p2 = new PlayerMock("B", List.of(new Card(4, Suit.CLUBS), new Card(5, Suit.SPADES), new Card(5, Suit.HEARTS)));
        PlayHandUseCase hand = new PlayHandUseCase(p1, p2, new Card(6, Suit.DIAMONDS));
        HandResult result = hand.play();
        Assertions.assertEquals(p2, result.getWinner().orElse(null));
    }

    @Test
    void shouldWinHandWinningFirstAndTyingSecond(){
        Player p1 = new PlayerMock("A", List.of(new Card(5, Suit.DIAMONDS), new Card(4, Suit.SPADES), new Card(4, Suit.HEARTS)));
        Player p2 = new PlayerMock("B", List.of(new Card(6, Suit.CLUBS), new Card(4, Suit.DIAMONDS), new Card(5, Suit.HEARTS)));
        PlayHandUseCase hand = new PlayHandUseCase(p1, p2, new Card(4, Suit.CLUBS));
        HandResult result = hand.play();
        Assertions.assertEquals(p1, result.getWinner().orElse(null));
    }

    @Test
    void shouldDrawHandWithThreeTiedRounds(){
        Player p1 = new PlayerMock("A", List.of(new Card(4, Suit.DIAMONDS), new Card(5, Suit.SPADES), new Card(6, Suit.HEARTS)));
        Player p2 = new PlayerMock("B", List.of(new Card(4, Suit.CLUBS), new Card(5, Suit.DIAMONDS), new Card(6, Suit.CLUBS)));
        PlayHandUseCase hand = new PlayHandUseCase(p1, p2, new Card(7, Suit.DIAMONDS));
        HandResult result = hand.play();
        Assertions.assertTrue(result.getWinner().isEmpty());
    }

    @Test
    void shouldWinHandByBestOfThree(){
        Player p1 = new PlayerMock("A", List.of(new Card(4, Suit.DIAMONDS), new Card(7, Suit.SPADES), new Card('Q', Suit.HEARTS)));
        Player p2 = new PlayerMock("B", List.of(new Card(5, Suit.CLUBS), new Card(6, Suit.DIAMONDS), new Card('Q', Suit.CLUBS)));
        PlayHandUseCase hand = new PlayHandUseCase(p1, p2, new Card(7, Suit.DIAMONDS));
        HandResult result = hand.play();
        Assertions.assertEquals(p2, result.getWinner().orElse(null));
    }

    @Test
    void shouldWinWinningFirstAndTyingThird(){
        Player p1 = new PlayerMock("A", List.of(new Card(4, Suit.DIAMONDS), new Card(7, Suit.SPADES), new Card('Q', Suit.HEARTS)));
        Player p2 = new PlayerMock("B", List.of(new Card(5, Suit.CLUBS), new Card(6, Suit.DIAMONDS), new Card('Q', Suit.CLUBS)));
        PlayHandUseCase hand = new PlayHandUseCase(p1, p2, new Card('Q', Suit.DIAMONDS));
        HandResult result = hand.play();
        Assertions.assertEquals(p2, result.getWinner().orElse(null));
    }
}