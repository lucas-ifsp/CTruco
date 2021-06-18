package com.bueno.truco.domain.usecases.game;

import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.deck.Suit;
import com.bueno.truco.domain.entities.player.Player;
import com.bueno.truco.domain.usecases.hand.PlayerMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class PlayGameUseCaseTest {

    private Player player1;
    private Player player2;
    private PlayGameUseCase sut;

    @BeforeEach
    void setUp() {
        player1 = new PlayerMock("A", List.of(new Card(4, Suit.DIAMONDS), new Card(4, Suit.SPADES), new Card(4, Suit.HEARTS)));
        player2 = new PlayerMock("B", List.of(new Card(5, Suit.DIAMONDS), new Card(5, Suit.SPADES), new Card(5, Suit.HEARTS)));
        sut = new PlayGameUseCase(player1, player2);
    }

    @AfterEach
    void tearDown() {
        player1 = null;
        player2 = null;
        sut = null;
    }

    /*@Test
    void shouldWinGameWinningFirst12SimpleHands(){
        Player winner = sut.play();
        Assertions.assertEquals(player2, winner);
    }*/
}