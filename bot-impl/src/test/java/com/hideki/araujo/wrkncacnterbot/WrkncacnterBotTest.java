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

    // Falta parametrizar
    @DisplayName("Testa se o bot aceita a mao de onze de acordo com as cartas na sua mao")
    @Test
    void testAcceptMaoDeOnze() {
        GameIntel intel = mock(GameIntel.class);

        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
        when(intel.getCards()).thenReturn(List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.SIX, CardSuit.SPADES),
                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS)
        ));

        assertThat(wrkncacnterBot.getMaoDeOnzeResponse(intel)).isEqualTo(true);
    }

    // Falta parametrizar
    @DisplayName("Testa se o bot rejeita a mao de onze de acordo com as cartas na sua mao")
    @Test
    void testRejectMaoDeOnze() {
        GameIntel intel = mock(GameIntel.class);

        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
        when(intel.getCards()).thenReturn(List.of(
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)
        ));

        assertThat(wrkncacnterBot.getMaoDeOnzeResponse(intel)).isEqualTo(false);
    }

    @DisplayName("Testa responde truco se tiver cartas fortes")
    @ParameterizedTest
    @MethodSource(value = "provideDataToResponseStrongerCards")
    void testIfRaiseReponseWithStrongCards(List<TrucoCard> cards, TrucoCard vira, int myScore, int opponentScore, int expectedResponse) {
        GameIntel intel = mock(GameIntel.class);

        when(intel.getScore())
                .thenReturn(myScore);

        when(intel.getOpponentScore())
                .thenReturn(opponentScore);

        when(intel.getVira())
                .thenReturn(vira);

        when(intel.getCards())
                .thenReturn(cards);

        assertThat(wrkncacnterBot.getRaiseResponse(intel)).isEqualTo(expectedResponse);
    }

    @DisplayName("Testa responde truco se tiver cartas fracas")
    @ParameterizedTest
    @MethodSource(value = "provideDataToResponseWeakerCards")
    void testifRaiseResponseWithWeakerCards(List<TrucoCard> cards, int myScore, int opponentScore, int expectedResponse){
        GameIntel intel = mock(GameIntel.class);

        when(intel.getScore())
                .thenReturn(myScore);

        when(intel.getOpponentScore())
                .thenReturn(opponentScore);

        when(intel.getVira())
                .thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));


        when(intel.getCards())
                .thenReturn(cards);

        assertThat(wrkncacnterBot.getRaiseResponse(intel)).isEqualTo(expectedResponse);
    }

    // Falta parametrizar
    @DisplayName("Testa escolher carta que mata a do oponente")
    @Test
    void testKillOpponentCard(){
        GameIntel intel = mock(GameIntel.class);

        when(intel.getOpponentCard()).thenReturn(Optional.ofNullable(TrucoCard.of(CardRank.JACK, CardSuit.HEARTS)));

        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.JACK, CardSuit.CLUBS));

        when(intel.getCards()).thenReturn(List.of(
                TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                TrucoCard.of(CardRank.TWO, CardSuit.SPADES)
        ));

        assertThat(wrkncacnterBot.chooseKillCard(intel).orElseThrow()).isEqualTo(TrucoCard.of(CardRank.TWO, CardSuit.SPADES));
    }

    // Falta parametrizar
    @DisplayName("Testa se o método retorna a carta mais fraca da mão")
    @Test
    void testChooseWeakestCard() {
        GameIntel intel = mock(GameIntel.class);

        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.JACK, CardSuit.CLUBS));

        when(intel.getCards()).thenReturn(List.of(
                TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                TrucoCard.of(CardRank.KING, CardSuit.SPADES)
        ));

        assertThat(wrkncacnterBot.chooseWeakestCard(intel).orElseThrow()).isEqualTo(TrucoCard.of(CardRank.JACK, CardSuit.SPADES));
    }

    // Falta parametrizar
    @DisplayName("Testa o bot se ele tenta ganhar a segunda rodada")
    @Test
    void testAttemptToWinSecondRound() {
        GameIntel intel = mock(GameIntel.class);

        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.JACK, CardSuit.CLUBS));

        when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.ACE, CardSuit.HEARTS)));

        when(intel.getCards()).thenReturn(List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.ACE, CardSuit.CLUBS)
        ));

        when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON));

        assertThat(wrkncacnterBot.chooseCard(intel).content()).isEqualTo(TrucoCard.of(CardRank.THREE, CardSuit.SPADES));
    }

    // Falta parametrizar
    @DisplayName("Testa a estratégia de ganhar 6 pontos(ter ganhado uma rodada, trucar na segunda, jogar uma carta fraca e depois jogar a mais forte)")
    @Test
    void testSixPointsStrategy() {
        GameIntel intel = mock(GameIntel.class);

        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.JACK, CardSuit.CLUBS));

        when(intel.getCards()).thenReturn(List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.ACE, CardSuit.CLUBS)
        ));

        when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON));

        assertTrue(wrkncacnterBot.decideIfRaises(intel));

        assertThat(wrkncacnterBot.chooseCard(intel)).isEqualTo(TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS));

        assertThat(wrkncacnterBot.chooseCard(intel)).isEqualTo(TrucoCard.of(CardRank.THREE, CardSuit.SPADES));
    }

    // Falta parametrizar
    @DisplayName("Testa quantas manilhas existe na mao")
    @Test
    void testCalculateNumberOfManilhas() {
        GameIntel intel = mock(GameIntel.class);

        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));

        when(intel.getCards()).thenReturn(List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
        ));

        assertThat(wrkncacnterBot.calculateNumberOfManilhas(intel)).isEqualTo(2);
    }

    // Falta parametrizar
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
    @Test
    void testForceTieGame(){
        GameIntel intel = mock(GameIntel.class);

        when(intel.getOpponentCard()).thenReturn(Optional.ofNullable(TrucoCard.of(CardRank.JACK, CardSuit.HEARTS)));

        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.JACK, CardSuit.CLUBS));

        when(intel.getCards()).thenReturn(List.of(
                TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                TrucoCard.of(CardRank.KING, CardSuit.CLUBS)
        ));

        assertEquals(wrkncacnterBot.chooseCard(intel).content(), TrucoCard.of(CardRank.JACK, CardSuit.SPADES));
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
                        20,
                        0,
                        -1
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                                TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                        10,
                        10,
                        0
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                        0,
                        20,
                        1
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
                        20,
                        0,
                        -1
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)
                        ),
                        10,
                        10,
                        0
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)
                        ),
                        0,
                        20,
                        1
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)
                        ),
                        10,
                        10,
                        0
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)
                        ),
                        0,
                        20,
                        1
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)
                        ),
                        0,
                        20,
                        1
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