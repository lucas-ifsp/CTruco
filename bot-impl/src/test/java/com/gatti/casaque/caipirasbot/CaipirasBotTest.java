package com.gatti.casaque.caipirasbot;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CaipirasBotTest {
    @InjectMocks
    CaipirasBot caipirasBot;

    @BeforeEach
    void setUp() {caipirasBot = new CaipirasBot();}

    @DisplayName("Testa se tem ouros na mão")
    @ParameterizedTest
    @MethodSource(value = "provideToCheckExistenceOfDiamondManilha")
    void testCheckExistanceDiamondManilha(List<TrucoCard> cards, TrucoCard vira, Boolean exist){
        GameIntel intel = mock(GameIntel.class);

        when(intel.getVira()).thenReturn(vira);
        when(intel.getCards()).thenReturn(cards);

        assertThat(caipirasBot.checkExistenceDiamondManilha(cards, vira)).isEqualTo(exist);
    }

    @DisplayName("Testa se joga ouro na prmeira rodada")
    @ParameterizedTest
    @MethodSource(value = "provideToCheckIfChoosedDiamondInFirstRound")
    void testChooseDiamondInFirstRound(List<TrucoCard> cards, TrucoCard vira, TrucoCard expectedCard){
        GameIntel intel = mock(GameIntel.class);

        when(intel.getRoundResults()).thenReturn(List.of());
        when(intel.getVira()).thenReturn(vira);
        when(intel.getCards()).thenReturn(cards);

        assertThat(caipirasBot.chooseDiamondInFirstRound(cards, vira)).isEqualTo(expectedCard);
    }

    public static Stream<Arguments> provideToCheckExistenceOfDiamondManilha() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)
                        ),
                        TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                        true
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                                TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                                TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS)
                        ),
                        TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                        false
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                                TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS),
                                TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS)
                        ),
                        TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                        true
                )
        );
    }

    public static Stream<Arguments> provideToCheckIfChoosedDiamondInFirstRound() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)
                        ),
                        TrucoCard.of(CardRank.KING, CardSuit.SPADES),
                        TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS)
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.KING, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
                        ),
                        TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                        TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS)
                        ),
                        TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                        TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)
                )
        );
    }
}
