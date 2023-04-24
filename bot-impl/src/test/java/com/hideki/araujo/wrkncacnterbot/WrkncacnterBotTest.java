/*
 *  Copyright (C) 2022 Lucas B. R. de Oliveira - IFSP/SCL
 *  Contact: x45danilo45x <at> gmail <dot> com or
 *  lucashideki87 <at> gmail <dot> com
 *
 *  This file is part of CTruco (Truco game for didactic purpose).
 *
 *  CTruco is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CTruco is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CTruco.  If not, see <https://www.gnu.org/licenses/>
 */

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

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @DisplayName("Testa se o bot aceita a mao de onze de acordo com as cartas na sua mao")
    @ParameterizedTest
    @MethodSource(value = "provideDataToMaoDeOnzeAccept")
    void testAcceptMaoDeOnze(List<TrucoCard> cards, TrucoCard vira) {
        GameIntel intel = mock(GameIntel.class);

        when(intel.getVira()).thenReturn(vira);
        when(intel.getCards()).thenReturn(cards);

        assertThat(wrkncacnterBot.getMaoDeOnzeResponse(intel)).isEqualTo(true);
    }

    // Falta parametrizar
    @DisplayName("Testa se o bot rejeita a mao de onze de acordo com as cartas na sua mao")
    @ParameterizedTest
    @MethodSource(value = "provideDataToMaoDeOnzeReject")
    void testRejectMaoDeOnze(List<TrucoCard> cards, TrucoCard vira) {
        GameIntel intel = mock(GameIntel.class);

        when(intel.getVira()).thenReturn(vira);
        when(intel.getCards()).thenReturn(cards);

        assertThat(wrkncacnterBot.getMaoDeOnzeResponse(intel)).isEqualTo(false);
    }

    @DisplayName("Testa responde truco se tiver cartas fortes")
    @ParameterizedTest
    @MethodSource(value = "provideDataToResponseStrongerCards")
    void testIfRaiseReponseWithStrongCards(List<TrucoCard> cards, TrucoCard vira, int expectedResponse) {
        GameIntel intel = mock(GameIntel.class);

        when(intel.getVira())
                .thenReturn(vira);

        when(intel.getCards())
                .thenReturn(cards);

        assertThat(wrkncacnterBot.getRaiseResponse(intel)).isEqualTo(expectedResponse);
    }

    @DisplayName("Testa responde truco se tiver cartas fracas")
    @ParameterizedTest
    @MethodSource(value = "provideDataToResponseWeakerCards")
    void testifRaiseResponseWithWeakerCards(List<TrucoCard> cards, int expectedResponse){
        GameIntel intel = mock(GameIntel.class);

        when(intel.getVira())
                .thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));


        when(intel.getCards())
                .thenReturn(cards);

        assertThat(wrkncacnterBot.getRaiseResponse(intel)).isEqualTo(expectedResponse);
    }

    @DisplayName("Testa escolher carta que mata a do oponente")
    @ParameterizedTest
    @MethodSource(value = "provideDataToKillOpponentCard")
    void testKillOpponentCard(List<TrucoCard> cards, TrucoCard vira, TrucoCard opponentCard, TrucoCard expected){
        GameIntel intel = mock(GameIntel.class);

        when(intel.getOpponentCard()).thenReturn(Optional.ofNullable(opponentCard));

        when(intel.getVira()).thenReturn(vira);

        when(intel.getCards()).thenReturn(cards);

        assertThat(wrkncacnterBot.chooseKillCard(intel).orElseThrow()).isEqualTo(expected);
    }

    @DisplayName("Testa se o método retorna a carta mais fraca da mão")
    @ParameterizedTest
    @MethodSource(value = "provideWeakestCardsToChoose")
    void testChooseWeakestCard(List<TrucoCard> cards, TrucoCard vira, TrucoCard expectedWeakest) {
        GameIntel intel = mock(GameIntel.class);

        when(intel.getVira()).thenReturn(vira);

        when(intel.getCards()).thenReturn(cards);

        assertThat(wrkncacnterBot.chooseWeakestCard(intel).orElseThrow()).isEqualTo(expectedWeakest);
    }

    // Falta parametrizar
//    @DisplayName("Testa a estratégia de ganhar 6 pontos(ter ganhado uma rodada, trucar na segunda, jogar uma carta fraca e depois jogar a mais forte)")
//    @Test
//    void testSixPointsStrategy() {
//        GameIntel intel = mock(GameIntel.class);
//
//        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.JACK, CardSuit.CLUBS));
//
//        when(intel.getCards()).thenReturn(List.of(
//                TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
//                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
//                TrucoCard.of(CardRank.ACE, CardSuit.CLUBS)
//        ));
//
//        when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON));
//
//        assertTrue(wrkncacnterBot.decideIfRaises(intel));
//
//        assertThat(wrkncacnterBot.chooseCard(intel)).isEqualTo(TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS));
//
//        assertThat(wrkncacnterBot.chooseCard(intel)).isEqualTo(TrucoCard.of(CardRank.THREE, CardSuit.SPADES));
//    }

    @DisplayName("Testa quantas manilhas existe na mao")
    @ParameterizedTest
    @MethodSource(value = "provideToCalculateNumberOfManilhas")
    void testCalculateNumberOfManilhas(List<TrucoCard> cards, TrucoCard vira, int numberOfManilhas) {
        GameIntel intel = mock(GameIntel.class);

        when(intel.getVira()).thenReturn(vira);

        when(intel.getCards()).thenReturn(cards);

        assertThat(wrkncacnterBot.calculateNumberOfManilhas(intel)).isEqualTo(numberOfManilhas);
    }

    @DisplayName("Testa se tem zap e manilha(copas) na mao")
    @Test
    void testHasZapAndManilhaClubs() {
        GameIntel intel = mock(GameIntel.class);

        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));

        when(intel.getCards()).thenReturn(List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS)
        ));

        assertTrue(wrkncacnterBot.hasZapAndManilhaHearts(intel));
    }

    @DisplayName("Testa de amarrar jogo")
    @ParameterizedTest
    @MethodSource(value = "provideToForceTieGame")
    void testForceTieGame(TrucoCard opponentCard, TrucoCard expectedCard){
        GameIntel intel = mock(GameIntel.class);

        when(intel.getOpponentCard())
                .thenReturn(Optional.ofNullable(opponentCard));

        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));

        when(intel.getCards()).thenReturn(List.of(
                TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.KING, CardSuit.CLUBS)
        ));

        assertThat(wrkncacnterBot.forceTieGame(intel).orElseThrow()).isEqualTo(expectedCard);
    }

    public static Stream<Arguments> provideToForceTieGame() {
        return Stream.of(
                Arguments.of(
                        TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.KING, CardSuit.CLUBS)
                ),
                Arguments.of(
                        TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.ACE, CardSuit.SPADES)
                ),
                Arguments.of(
                        TrucoCard.of(CardRank.JACK, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS)
                )
        );
    }

    public static Stream<Arguments> provideWeakestCardsToChoose() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                                TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                                TrucoCard.of(CardRank.KING, CardSuit.SPADES)
                        ),
                        TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                        TrucoCard.of(CardRank.JACK, CardSuit.SPADES)
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                                TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.SIX, CardSuit.SPADES),
                        TrucoCard.of(CardRank.FOUR, CardSuit.SPADES)
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                                TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                        TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS)
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                                TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                        TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
                )
        );
    }

    public static Stream<Arguments> provideToCalculateNumberOfManilhas() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                                TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                        2
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                        3
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                        0
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                                TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                                TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                        1
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                                TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                        2
                        ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                                TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                        1
                )
        );
    }

    static Stream<Arguments> provideDataToKillOpponentCard() {
        return Stream.of(
                Arguments.of(
                    List.of(
                            TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                            TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                            TrucoCard.of(CardRank.TWO, CardSuit.SPADES)
                    ),
                        TrucoCard.of(CardRank.JACK, CardSuit.CLUBS), // vira
                        TrucoCard.of(CardRank.JACK, CardSuit.HEARTS), // Opponent
                        TrucoCard.of(CardRank.ACE, CardSuit.SPADES) // Expected
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES),
                                TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                                TrucoCard.of(CardRank.TWO, CardSuit.SPADES)
                        ),
                        TrucoCard.of(CardRank.JACK, CardSuit.CLUBS), // vira
                        TrucoCard.of(CardRank.SIX, CardSuit.HEARTS), // Opponent
                        TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES) // Expected
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                                TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                                TrucoCard.of(CardRank.TWO, CardSuit.SPADES)
                        ),
                        TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS), // vira
                        TrucoCard.of(CardRank.JACK, CardSuit.HEARTS), // Opponent
                        TrucoCard.of(CardRank.KING, CardSuit.CLUBS) // Expected
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                                TrucoCard.of(CardRank.KING, CardSuit.SPADES),
                                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS)
                        ),
                        TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS), // vira
                        TrucoCard.of(CardRank.ACE, CardSuit.HEARTS), // Opponent
                        TrucoCard.of(CardRank.THREE, CardSuit.HEARTS) // Expected
                )
        );
    }

    static Stream<Arguments> provideDataToMaoDeOnzeAccept() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.SIX, CardSuit.SPADES),
                                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.KING, CardSuit.SPADES),
                                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                                TrucoCard.of(CardRank.KING, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.SIX, CardSuit.SPADES),
                                TrucoCard.of(CardRank.KING, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                                TrucoCard.of(CardRank.KING, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                                TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.KING, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.ACE, CardSuit.CLUBS)
                )
        );
    }

    static Stream<Arguments> provideDataToMaoDeOnzeReject() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                                TrucoCard.of(CardRank.JACK, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                                TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                                TrucoCard.of(CardRank.SIX, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                                TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                                TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)
                )
        );
    }

    static Stream<Arguments> provideDataToResponseStrongerCards() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                                TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                        -1
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                                TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                        1
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                        1
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                        1
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.SIX, CardSuit.SPADES),
                                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                        0
                )
        );
    }

    static Stream<Arguments> provideDataToResponseWeakerCards() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)
                        ),
                        -1
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)
                        ),
                        -1
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)
                        ),
                        -1
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)
                        ),
                        -1
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)
                        ),
                        -1
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)
                        ),
                        -1
                )
        );
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
                        23
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
                                TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                                TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                        36
                )
        );
    }

    static Stream<Arguments> provideDataToTrucoStrongerCards() { // List<Arguments> is ok too.
        return Stream.of(
                Arguments.of(
                        List.of(
                            TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                            TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.TWO, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.JACK, CardSuit.HEARTS)
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
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