package com.bueno.truco.domain.usecases.game;


import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.deck.Suit;
import com.bueno.truco.domain.entities.hand.Intel;
import com.bueno.truco.domain.entities.player.Player;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

//TODO Solve flickering test due to rand vira card

@ExtendWith(MockitoExtension.class)
class PlayGameUseCaseTest {

    @Mock
    private Player p1;
    @Mock
    private Player p2;
    private PlayGameUseCase sut;

    @BeforeEach
    void setUp(){
        when(p1.playCard()).thenReturn(new Card(3, Suit.SPADES));
        when(p2.playCard()).thenReturn(new Card(2, Suit.SPADES));
        sut = new PlayGameUseCase(p1, p2);
    }

    @AfterEach
    void tearDown(){
        sut = null;
    }

    @Test
    @DisplayName("Should have no winner after single simple hand")
    void shouldHaveNoWinnerAfterSingleSimpleHand(){
        when(p1.requestTruco()).thenReturn(false);
        when(p2.requestTruco()).thenReturn(false);
        final Intel intel = sut.playNewHand();
        Assertions.assertAll(
                ()-> assertNotEquals(12, intel.getOpponentScore(p2)),
                ()-> assertNotEquals(12, intel.getOpponentScore(p1))
        );
    }
}