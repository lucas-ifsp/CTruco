package com.bueno.truco.domain.usecases.game;

import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.deck.Suit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RoundUseCaseTest {

    @ParameterizedTest
    @CsvSource({"4,5,6,5", "5,4,6,5", "7,1,5,1", "1,3,5,3"})
    void shouldReturnCorrectWinnerForSimpleCards(int card1, int card2, int vira, int winner){
        var round = new RoundUseCase(new Card(card1, Suit.SPADES), new Card(card2, Suit.SPADES), new Card(vira, Suit.SPADES));
        Assertions.assertEquals(new Card(winner, Suit.SPADES), round.getWinner().orElse(null));
    }

    @ParameterizedTest
    @CsvSource({"4,DIAMONDS,4,HEARTS,3,4,HEARTS", "5,CLUBS,5,HEARTS,4,5,CLUBS",
            "13,SPADES,13,HEARTS,12,13,HEARTS", "13,SPADES,12,HEARTS,12,13,SPADES"})
    void shouldReturnCorrectWinnerForManilhas(int rank1, Suit suit1, int rank2, Suit suit2, int vira, int winnerRank, Suit winnerSuit){
        var round = new RoundUseCase(new Card(rank1, suit1), new Card(rank2, suit2), new Card(vira, Suit.SPADES));
        Assertions.assertEquals(new Card(winnerRank, winnerSuit), round.getWinner().orElse(null));
    }

    @Test
    void shouldDrawWhenComparingEqualSimpleCards(){
        var round = new RoundUseCase(new Card(4, Suit.SPADES), new Card(4, Suit.SPADES), new Card(6, Suit.SPADES));
        Assertions.assertTrue(round.getWinner().isEmpty());
    }

    @Test
    void shouldNotDrawWhenComparingManilhas(){
        var round = new RoundUseCase(new Card(4, Suit.SPADES), new Card(4, Suit.CLUBS), new Card(3, Suit.SPADES));
        Assertions.assertTrue(round.getWinner().isPresent());
    }

    @ParameterizedTest
    @NullSource
    void shouldThrowIfConstructorParameterIsNull(Card card){
        Assertions.assertThrows(NullPointerException.class, () -> new RoundUseCase(card, card, card));
    }
}