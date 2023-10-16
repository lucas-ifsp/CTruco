package com.gatti.casaque.caipirasbot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
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
    @MethodSource(value = "provideToCheckExistanceOfDiamondManilha")
    void testCheckExistanceDiamondManilha(List<TrucoCard> cards, TrucoCard vira, Boolean exist){
        GameIntel intel = mock(GameIntel.class);

        when(intel.getVira()).thenReturn(vira);
        when(intel.getCards()).thenReturn(cards);

        assertThat(caipirasBot.checkExistanceDiamondManilha(cards, vira)).isEqualTo(exist);
    }


    public static Stream<Arguments> provideToCheckExistanceOfDiamondManilha() {
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

}
