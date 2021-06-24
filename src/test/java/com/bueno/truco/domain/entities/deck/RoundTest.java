package com.bueno.truco.domain.entities.deck;

import com.bueno.truco.domain.entities.game.Round;
import com.bueno.truco.domain.entities.game.GameRuleViolationException;
import com.bueno.truco.domain.entities.player.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;lkll
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoundTest {

    @Mock
    private Player p1;
    @Mock
    private Player p2;

    @ParameterizedTest
    @CsvSource({"4,5,6,5", "5,4,6,5", "7,1,5,1", "1,3,5,3"})
    void shouldReturnCorrectWinnerForSimpleCards(int card1, int card2, int vira, int winner) {
        when(p1.playCard()).thenReturn(new Card(card1, Suit.SPADES));
        when(p2.playCard()).thenReturn(new Card(card2, Suit.SPADES));
        var round = new Round(p1, p2, new Card(vira, Suit.SPADES));
        Assertions.assertEquals(new Card(winner, Suit.SPADES), round.getHighestCard().orElse(null));
    }

    @ParameterizedTest
    @CsvSource({"4,DIAMONDS,4,HEARTS,3,4,HEARTS", "5,CLUBS,5,HEARTS,4,5,CLUBS",
            "13,SPADES,13,HEARTS,12,13,HEARTS", "13,SPADES,12,HEARTS,12,13,SPADES"})
    void shouldReturnCorrectWinnerForManilhas(int rank1, Suit suit1, int rank2, Suit suit2, int vira, int winnerRank, Suit winnerSuit) {
        when(p1.playCard()).thenReturn(new Card(rank1, suit1));
        when(p2.playCard()).thenReturn(new Card(rank2, suit2));

        var round = new Round(p1, p2, new Card(vira, Suit.SPADES));
        Assertions.assertEquals(new Card(winnerRank, winnerSuit), round.getHighestCard().orElse(null));
    }

    @Test
    void shouldDrawWhenComparingEqualSimpleCards() {
        when(p1.playCard()).thenReturn(new Card(4, Suit.SPADES));
        when(p2.playCard()).thenReturn(new Card(4, Suit.CLUBS));

        var round = new Round(p1, p2, new Card(6, Suit.SPADES));
        Assertions.assertTrue(round.getHighestCard().isEmpty());
    }

    @Test
    void shouldNotDrawWhenComparingManilhas() {
        when(p1.playCard()).thenReturn(new Card(4, Suit.SPADES));
        when(p2.playCard()).thenReturn(new Card(4, Suit.CLUBS));

        var round = new Round(p1, p2, new Card(3, Suit.SPADES));
        Assertions.assertTrue(round.getHighestCard().isPresent());
    }

    @Test
    void shouldThrowIfCreateRoundWithRepeatedCards() {
        when(p1.playCard()).thenReturn(new Card(4, Suit.SPADES));
        when(p2.playCard()).thenReturn(new Card(4, Suit.SPADES));
        Assertions.assertThrows(GameRuleViolationException.class,
                () -> new Round(p1, p2, new Card(3, Suit.SPADES)));
    }

    @Test
    void shouldThrowIfConstructorParameterIsNull() {
        when(p1.playCard()).thenReturn(null);
        when(p2.playCard()).thenReturn(null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Round(p1, p2, null));
    }
}