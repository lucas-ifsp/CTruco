package com.bueno.truco.domain.entities.game;

import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.deck.Suit;
import com.bueno.truco.domain.entities.game.Round;
import com.bueno.truco.domain.entities.game.GameRuleViolationException;
import com.bueno.truco.domain.entities.player.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoundTest {

    @Mock
    private Player p1;
    @Mock
    private Player p2;
    @Mock
    private Game game;

    @ParameterizedTest
    @CsvSource({"4,5,6,5", "5,4,6,5", "7,1,5,1", "1,3,5,3"})
    void shouldReturnCorrectWinnerForSimpleCards(int card1, int card2, int vira, int winner) {
        when(p1.playCard()).thenReturn(new Card(card1, Suit.SPADES));
        when(p2.playCard()).thenReturn(new Card(card2, Suit.SPADES));
        when(game.getCurrentVira()).thenReturn(new Card(vira, Suit.SPADES));

        var round = new Round(p1, p2, game);
        round.play();

        Assertions.assertEquals(new Card(winner, Suit.SPADES), round.getHighestCard().orElse(null));
    }

    @ParameterizedTest
    @CsvSource({"4,DIAMONDS,4,HEARTS,3,4,HEARTS", "5,CLUBS,5,HEARTS,4,5,CLUBS",
            "13,SPADES,13,HEARTS,12,13,HEARTS", "13,SPADES,12,HEARTS,12,13,SPADES"})
    void shouldReturnCorrectCardWinnerForManilhas(int rank1, Suit suit1, int rank2, Suit suit2, int vira, int winnerRank, Suit winnerSuit) {
        when(p1.playCard()).thenReturn(new Card(rank1, suit1));
        when(p2.playCard()).thenReturn(new Card(rank2, suit2));
        when(game.getCurrentVira()).thenReturn(new Card(vira, Suit.SPADES));

        var round = new Round(p1, p2, game);
        round.play();

        Assertions.assertEquals(new Card(winnerRank, winnerSuit), round.getHighestCard().orElse(null));
    }

    @Test
    void shouldDrawWhenComparingEqualSimpleCards() {
        when(p1.playCard()).thenReturn(new Card(4, Suit.SPADES));
        when(p2.playCard()).thenReturn(new Card(4, Suit.CLUBS));
        when(game.getCurrentVira()).thenReturn(new Card(6, Suit.SPADES));

        var round = new Round(p1, p2, game);
        round.play();

        Assertions.assertTrue(round.getWinner().isEmpty());
    }

    @Test
    void shouldNotDrawWhenComparingManilhas() {
        when(p1.playCard()).thenReturn(new Card(4, Suit.SPADES));
        when(p2.playCard()).thenReturn(new Card(4, Suit.CLUBS));
        when(game.getCurrentVira()).thenReturn(new Card(3, Suit.SPADES));

        var round = new Round(p1, p2, game);
        round.play();

        Assertions.assertEquals(p2, round.getWinner().get());
    }

    @Test
    void shouldThrowIfConstructorParameterIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Round(p1, p2, null));
    }

    @Test
    void shouldThrowIfRoundCardIsNull() {
        when(p1.playCard()).thenReturn(null);
        when(p2.playCard()).thenReturn(null);

        var round = new Round(p1, p2, game);
        Assertions.assertThrows(IllegalArgumentException.class, round::play);
    }

    @ParameterizedTest
    @CsvSource({"4,4,5","4,5,4","5,4,4"})
    void shouldThrowIfRoundHasDuplicatedCard(int rank1, int rank2, int rankVira) {
        when(p1.playCard()).thenReturn(new Card(rank1, Suit.SPADES));
        when(p2.playCard()).thenReturn(new Card(rank2, Suit.SPADES));
        when(game.getCurrentVira()).thenReturn(new Card(rankVira, Suit.SPADES));

        var round = new Round(p1, p2, game);
        Assertions.assertThrows(GameRuleViolationException.class, round::play);
    }

}