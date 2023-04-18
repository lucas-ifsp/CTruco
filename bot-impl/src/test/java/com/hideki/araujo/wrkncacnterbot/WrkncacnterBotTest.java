package com.hideki.araujo.wrkncacnterbot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.in;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class WrkncacnterBotTest {
    @InjectMocks
    WrkncacnterBot wrkncacnterBot;

    @BeforeEach
    void setUp() {
        wrkncacnterBot = new WrkncacnterBot();
    }

    @AfterEach
    void tearDown() {
    }

    @DisplayName("Testa se pede truco se tiver cartas fracas no round 1")
    @ParameterizedTest
    @MethodSource(value = "provideDataToTrucoWeakerCards")
    void testRaiseTrucoIfWeakerCardsInRound1(List<TrucoCard> cards) {
        GameIntel intel = mock(GameIntel.class);

        when(intel.getRoundResults()).thenReturn(List.of());

        when(intel.getCards()).thenReturn(cards);
        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.JACK, CardSuit.HEARTS));

        assertTrue(wrkncacnterBot.decideIfRaises(intel));
    }

    @DisplayName("Testa se pede truco se tiver cartas fortes no round 1")
    @ParameterizedTest
    @MethodSource(value = "provideDataToTrucoStrongerCards")
    void testRaiseTrucoIfStrongerCardsInRound1(List<TrucoCard> cards, TrucoCard vira) {
        GameIntel intel = mock(GameIntel.class);

        when(intel.getRoundResults()).thenReturn(List.of());

        when(intel.getCards()).thenReturn(cards);
        when(intel.getVira()).thenReturn(vira);

        assertTrue(wrkncacnterBot.decideIfRaises(intel));
    }

    @DisplayName("Testa o metodo de calcular valor da mao")
    @ParameterizedTest
    @MethodSource(value = "provideDataToCalculateDeckValues")
    void testDeckValue(List<TrucoCard> cards, TrucoCard vira, int expectedDeckValue) {
        GameIntel intel = mock(GameIntel.class);

        when(intel.getVira()).thenReturn(vira);
        when(intel.getCards()).thenReturn(cards);

        assertThat(wrkncacnterBot.calculateDeckValue(intel)).isEqualTo(expectedDeckValue);
    }

    static Stream<Arguments> provideDataToCalculateDeckValues() {
        return Stream.of(
                Arguments.of(
                    List.of(
                            TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                            TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)
                    ),
                        TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                        3
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                        4
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                        21
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                        27
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                        28
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                        30
                )
        );
    }

    static Stream<Arguments> provideDataToTrucoStrongerCards() { // List<Arguments> is ok too.
        return Stream.of(
                Arguments.of(
                        List.of(
                            TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                            TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.ACE, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.JACK, CardSuit.HEARTS)
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.JACK, CardSuit.HEARTS)
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.JACK, CardSuit.HEARTS)
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.JACK, CardSuit.HEARTS)
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.JACK, CardSuit.HEARTS)
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.JACK, CardSuit.HEARTS)
                )
        );
    }

    static Stream<Arguments> provideDataToTrucoWeakerCards() { // List<Arguments> is ok too.
        return Stream.of(
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)
                        )
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)
                        )
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)
                        )
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)
                        )
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)
                        )
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)
                        )
                )
        );
    }
}