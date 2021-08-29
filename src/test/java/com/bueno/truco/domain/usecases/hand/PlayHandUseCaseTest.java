package com.bueno.truco.domain.usecases.hand;

import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.deck.Suit;
import com.bueno.truco.domain.entities.game.Game;
import com.bueno.truco.domain.entities.hand.Hand;
import com.bueno.truco.domain.entities.hand.HandResult;
import com.bueno.truco.domain.entities.hand.HandScore;
import com.bueno.truco.domain.entities.player.Player;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

//TODO solve flickering test failing because vira is random.

@ExtendWith(MockitoExtension.class)
class PlayHandUseCaseTest {

    private Hand hand;
    private PlayHandUseCase sut;

    @Mock
    private Game game;
    @Mock
    private Player p1;
    @Mock
    private Player p2;

    @BeforeEach
    void setUp() {
        hand = new Hand(p1, p2);//new Card(7, Suit.CLUBS)
        when(game.prepareNewHand()).thenReturn(hand);
        sut  = new PlayHandUseCase(game);
    }

    @AfterEach
    void tearDown() {
        sut = null;
        hand = null;
    }

   @Test
    void shouldHaveHandWinnerAfterTwoRounds(){
       when(p1.playCard()).thenReturn(new Card(3,Suit.SPADES)).thenReturn(new Card(3,Suit.HEARTS));
       when(p2.playCard()).thenReturn(new Card(4,Suit.SPADES)).thenReturn(new Card(4,Suit.HEARTS));
       sut.play();
       assertEquals(p1, getWinner(hand));
   }

    @Test
    void shouldHaveHandWinnerAfterThreeRounds(){
        when(p1.playCard()).thenReturn(new Card(3,Suit.SPADES)).thenReturn(new Card(4,Suit.HEARTS)).thenReturn(new Card(3, Suit.DIAMONDS));
        when(p2.playCard()).thenReturn(new Card(4,Suit.SPADES)).thenReturn(new Card(3,Suit.HEARTS)).thenReturn(new Card(4, Suit.DIAMONDS));
        sut.play();
        assertEquals(p1, getWinner(hand));
    }

    @Test
    @DisplayName("ShouldWinnerReceiveOnePointInMaoDeOnzeRun")
    void shouldWinnerReceiveOnePointInMaoDeOnzeRun() {
        when(game.getPlayer1()).thenReturn(p1);
        when(game.getPlayer2()).thenReturn(p2);
        when(game.isMaoDeOnze()).thenReturn(true);
        when(p1.getScore()).thenReturn(11);
        when(p1.getMaoDeOnzeResponse()).thenReturn(false);
        sut.play();
        assertAll(
                () -> assertEquals(p2, getWinner(hand)),
                () -> assertEquals(HandScore.of(1), hand.getScore())
        );
    }

    @Test
    @DisplayName("ShouldWinnerReceiveThreePointsForPlayedMaoDeOnze")
    void shouldWinnerReceiveThreePointsForPlayedMaoDeOnze() {
        when(game.getPlayer1()).thenReturn(p1);
        when(p1.playCard()).thenReturn(new Card(3,Suit.SPADES));
        when(p2.playCard()).thenReturn(new Card(4,Suit.SPADES));
        when(game.isMaoDeOnze()).thenReturn(true);
        when(p1.getScore()).thenReturn(11);
        when(p1.getMaoDeOnzeResponse()).thenReturn(true);
        sut.play();
        assertEquals(HandScore.of(3), hand.getScore());
    }


    private Player getWinner(Hand hand) {
        Optional<HandResult> handResult = hand.getResult();
        return handResult.flatMap(HandResult::getWinner).orElse(null);
    }
}