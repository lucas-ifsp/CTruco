package com.local.gatti.casaque.caipirasbot;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CaipirasBotTest {
    @InjectMocks
    CaipirasBot caipirasBot;

    @BeforeEach
    void setUp() {caipirasBot = new CaipirasBot();}

    @DisplayName("Testa se mata a carta do oponente")
    @ParameterizedTest
    @MethodSource("provideToKillOpponentCard")
    void testKillOpponentCard(List<TrucoCard> cards, TrucoCard vira, TrucoCard opponentCard, TrucoCard expected) {
        GameIntel intel = mock(GameIntel.class);

        when(intel.getCards()).thenReturn(cards);
        when(intel.getVira()).thenReturn(vira);
        when(intel.getOpponentCard()).thenReturn(Optional.ofNullable(opponentCard));

        assertThat(caipirasBot.tryToKillOpponentCard(intel)).isEqualTo(expected);
    }

    @DisplayName("Testa se o bot aceita jogar a mão de onze")
    @ParameterizedTest
    @MethodSource(value = "provideToPlayMaoDeOnzeCondition")
    void testPlayMaoDeOnzeCondition(List<TrucoCard> cards, TrucoCard vira) {
        GameIntel intel = mock(GameIntel.class);

        when(intel.getCards()).thenReturn(cards);
        when(intel.getVira()).thenReturn(vira);

        assertTrue(caipirasBot.playMaoDeOnzeCondition(intel));
    }

    @DisplayName("Testa se aceita o truco do oponente tendo ganho a primeira rodada")
    @ParameterizedTest
    @MethodSource(value = "ProvideToAcceptRaiseByOpponentIfFirstRoundWon")
    void testAcceptRaiseByOpponentIfFirstRoundWon(List<TrucoCard> cards, TrucoCard vira, boolean expected) {
        GameIntel intel = mock(GameIntel.class);

        when(intel.getRoundResults()).thenReturn(List.of(
                GameIntel.RoundResult.WON
        ));
        when(intel.getCards()).thenReturn(cards);
        when(intel.getVira()).thenReturn(vira);

        assertThat(caipirasBot.acceptRaiseByOpponentIfFirstRoundWon(intel)).isEqualTo(expected);
    }

    @DisplayName("Testa se tem ouros na mão")
    @ParameterizedTest
    @MethodSource(value = "provideToCheckExistenceOfDiamondManilha")
    void testCheckExistenceDiamondManilha(List<TrucoCard> cards, TrucoCard vira, Boolean exist){
        GameIntel intel = mock(GameIntel.class);

        when(intel.getVira()).thenReturn(vira);
        when(intel.getCards()).thenReturn(cards);

        assertThat(caipirasBot.checkExistenceDiamondManilha(cards, vira)).isEqualTo(exist);
    }

    @DisplayName("Testa se tem espadilha na mão")
    @ParameterizedTest
    @MethodSource(value = "provideToCheckExistenceOfSpadesManilha")
    void testCheckExistenceSpadesManilha(List<TrucoCard> cards, TrucoCard vira, Boolean exist){
        GameIntel intel = mock(GameIntel.class);

        when(intel.getVira()).thenReturn(vira);
        when(intel.getCards()).thenReturn(cards);

        assertThat(caipirasBot.checkExistenceSpadesManilha(cards, vira)).isEqualTo(exist);
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

    @DisplayName("Testa se teve casal maior no jogo")
    @ParameterizedTest
    @MethodSource(value = "provideToCheckExistenceOfCasalMaior")
    void testCheckExistenceCasalMaior(List<TrucoCard> openCards, Boolean exist){
        GameIntel intel = mock(GameIntel.class);

        when(intel.getOpenCards()).thenReturn(openCards);

        assertThat(caipirasBot.checkExistenceCasalMaior(openCards)).isEqualTo(exist);
    }

    @DisplayName("Testa se blefa truco ao ver a terceira carta do oponente antes de jogar")
    @ParameterizedTest
    @MethodSource(value = "provideToBluffWhenOpponentThirdCardIsKnown")
    void testBluffWhenOpponentThirdCardIsKnown(TrucoCard opponentCard, TrucoCard vira) {
        GameIntel intel = mock(GameIntel.class);

        when(intel.getRoundResults()).thenReturn(List.of(
                GameIntel.RoundResult.WON,
                GameIntel.RoundResult.LOST
        ));
        when(intel.getOpponentCard()).thenReturn(Optional.ofNullable(opponentCard));
        when(intel.getVira()).thenReturn(vira);

        assertTrue(caipirasBot.bluffWhenOpponentThirdCardIsKnown(intel.getRoundResults(), intel.getOpenCards()));
    }

    @DisplayName("Testa se tem manilha e uma carta melhor que 2")
    @ParameterizedTest
    @MethodSource(value = "provideToManilhaAndStronger")
    void testCheckExistenceManilhaAndTwoStronger(List<TrucoCard> cards, TrucoCard vira, Boolean exist) {
        GameIntel intel = mock(GameIntel.class);

        when(intel.getOpenCards()).thenReturn(cards);
        when(intel.getVira()).thenReturn(vira);

        assertThat(caipirasBot.checkExistenceManilhaAndStronger(cards, vira)).isEqualTo(exist);
    }

    @DisplayName("Testa se joga a menor carta na primeira rodada")
    @ParameterizedTest
    @MethodSource(value = "provideToChooseWeakInFirstRound")
    void testChooseWeakInFirstRound(List<TrucoCard> cards, TrucoCard vira, TrucoCard weakCard) {
        GameIntel intel = mock(GameIntel.class);

        when(intel.getRoundResults()).thenReturn(List.of());
        when(intel.getVira()).thenReturn(vira);
        when(intel.getOpenCards()).thenReturn(cards);

        assertThat(caipirasBot.chooseWeakInFirstRound(cards, vira)).isEqualTo(weakCard);
    }

    @DisplayName("Testa se o método chooseCard joga ouro na prmeira rodada")
    @ParameterizedTest
    @MethodSource(value = "provideToCheckIfChoosedDiamondInFirstRound")
    void testChooseCardDiamondInFirstRound(List<TrucoCard> cards, TrucoCard vira, TrucoCard expectedCard){
        GameIntel intel = mock(GameIntel.class);

        when(intel.getRoundResults()).thenReturn(List.of());
        when(intel.getVira()).thenReturn(vira);
        when(intel.getCards()).thenReturn(cards);

        assertThat(caipirasBot.chooseCard(intel)).isEqualTo(CardToPlay.of(expectedCard));
    }

    @DisplayName("Testa se o método chooseCard joga a menor na primeira rodada, apenas quando tiver copa/zap e maior que 2")
    @ParameterizedTest
    @MethodSource(value = "provideToChooseWeakInFirstRound")
    void testChooseCardWeakInFirstRound(List<TrucoCard> cards, TrucoCard vira, TrucoCard expectedCard){
        GameIntel intel = mock(GameIntel.class);

        when(intel.getRoundResults()).thenReturn(List.of());
        when(intel.getVira()).thenReturn(vira);
        when(intel.getCards()).thenReturn(cards);

        assertThat(caipirasBot.chooseCard(intel)).isEqualTo(CardToPlay.of(expectedCard));
    }


    @DisplayName("Testa se o método chooseCard jogo espadilha na primeira rodada")
    @ParameterizedTest
    @MethodSource(value = "provideToCheckIfChoosedSpadilhaInFirstRound")
    void testChooseCardSpadesInFirstRound(List<TrucoCard> cards, TrucoCard vira, TrucoCard expectedCard) {
        GameIntel intel = mock(GameIntel.class);

        when(intel.getRoundResults()).thenReturn(List.of());
        when(intel.getVira()).thenReturn(vira);
        when(intel.getCards()).thenReturn(cards);

        assertThat(caipirasBot.chooseCard(intel)).isEqualTo(CardToPlay.of(expectedCard));
    }

    @DisplayName("Testa se o adversário é o primeiro jogador")
    @ParameterizedTest
    @MethodSource(value = "provideToCheckFirstPLayer")
    void testCheckEnemyIsFirstPLayer(Optional<TrucoCard> enemyCard, Boolean validate){
        GameIntel intel = mock(GameIntel.class);

        when(intel.getOpponentCard()).thenReturn(enemyCard);

        assertThat(caipirasBot.checkEnemyIsFirstPLayer(enemyCard).equals(validate));
    }

    @DisplayName("Testa se tem zap seco")
    @ParameterizedTest
    @MethodSource(value = "provideToCheckOnlyZap")
    void testCheckOnlyZap(List<TrucoCard> cards, TrucoCard vira, Boolean validate){
        GameIntel intel = mock(GameIntel.class);

        when(intel.getCards()).thenReturn(cards);
        when(intel.getVira()).thenReturn(vira);

        assertThat(caipirasBot.checkOnlyZap(cards, vira).equals(validate));
    }

    @DisplayName("Testa se ele truca no primeiro round")
    @ParameterizedTest
    @MethodSource(value = "provideToCheckRaiseInFirstRound")
    void testCheckRaiseInFirstRound(List<TrucoCard> cards, TrucoCard vira, Boolean validate){
        GameIntel intel = mock(GameIntel.class);

        when(intel.getRoundResults()).thenReturn(List.of());
        when(intel.getVira()).thenReturn(vira);
        when(intel.getCards()).thenReturn(cards);

        assertThat(caipirasBot.checkRaiseInFirstRound(cards, vira)).isEqualTo(validate);
    }

    @DisplayName("Testa se ele joga a segunda melhor carta na primeira")
    @ParameterizedTest
    @MethodSource(value = "chooseMiddleCard")
    void testChooseMiddleCard(List<TrucoCard> cards, TrucoCard vira, TrucoCard expectedCard){
        GameIntel intel = mock(GameIntel.class);

        when(intel.getRoundResults()).thenReturn(List.of());
        when(intel.getVira()).thenReturn(vira);
        when(intel.getCards()).thenReturn(cards);

        assertThat(caipirasBot.chooseMiddleCart(cards, vira, List.of())).isEqualTo(expectedCard);
    }

    static Stream<Arguments> provideToKillOpponentCard() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                                TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                                TrucoCard.of(CardRank.TWO, CardSuit.HEARTS)
                        ),
                        TrucoCard.of(CardRank.JACK, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.ACE, CardSuit.SPADES)
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                                TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.JACK, CardSuit.SPADES)
                        ),
                        TrucoCard.of(CardRank.JACK, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS)
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                                TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                                TrucoCard.of(CardRank.TWO, CardSuit.SPADES)
                        ),
                        TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.JACK, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.KING, CardSuit.CLUBS)
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                                TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                                TrucoCard.of(CardRank.SIX, CardSuit.HEARTS)
                        ),
                        TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.FOUR, CardSuit.SPADES)
                )
        );
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

    public static Stream<Arguments> provideToCheckExistenceOfSpadesManilha() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                                TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)
                        ),
                        TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                        true
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                                TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS)
                        ),
                        TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                        false
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                                TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS),
                                TrucoCard.of(CardRank.SIX, CardSuit.SPADES)
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
    public static Stream<Arguments> provideToCheckExistenceOfCasalMaior() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                                TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                                TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS)
                        ),
                        false
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.SIX, CardSuit.CLUBS),
                                TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS),
                                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS)
                        ),
                        true
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                                TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS),
                                TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)
                        ),
                        true
                )
        );
    }

    public static Stream<Arguments> provideToBluffWhenOpponentThirdCardIsKnown() {
        return Stream.of(
                Arguments.of(
                        TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.TWO, CardSuit.SPADES)
                ),
                Arguments.of(
                        TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.TWO, CardSuit.SPADES)
                ),
                Arguments.of(
                        TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.TWO, CardSuit.SPADES)
                ),
                Arguments.of(
                        TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.TWO, CardSuit.SPADES)
                )
        );
    }

    public static Stream<Arguments> provideToManilhaAndStronger() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS)
                        ),
                        TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                        true
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                                TrucoCard.of(CardRank.TWO, CardSuit.HEARTS)
                        ),
                        TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                        true
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS)
                        ),
                        TrucoCard.of(CardRank.KING, CardSuit.SPADES),
                        false
                )
        );
    }

    public static Stream<Arguments> provideToCheckIfChoosedSpadilhaInFirstRound() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                                TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS)
                        ),
                        TrucoCard.of(CardRank.KING, CardSuit.SPADES),
                        TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS)
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                                TrucoCard.of(CardRank.FOUR, CardSuit.SPADES)
                        ),
                        TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                        TrucoCard.of(CardRank.TWO, CardSuit.SPADES)
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.JACK, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.KING, CardSuit.SPADES),
                                TrucoCard.of(CardRank.FOUR, CardSuit.SPADES)
                        ),
                        TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                        TrucoCard.of(CardRank.FOUR, CardSuit.SPADES)
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.SIX, CardSuit.SPADES),
                                TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS)
                        ),
                        TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                        TrucoCard.of(CardRank.SIX, CardSuit.SPADES)
                )
        );
    }

    public static Stream<Arguments> provideToChooseWeakInFirstRound() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS)
                        ),
                        TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                        TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                        TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS)
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                                TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS)
                        ),
                        TrucoCard.of(CardRank.KING, CardSuit.SPADES),
                        TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS)
                )
        );
    }

    public static Stream<Arguments> provideToCheckFirstPLayer() {
        return Stream.of(
                Arguments.of(
                        Optional.of(TrucoCard.of(CardRank.TWO, CardSuit.SPADES)),
                        true
                ),
                Arguments.of(
                        Optional.empty(),
                        false
                )
        );
    }

    public static Stream<Arguments> provideToCheckOnlyZap() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                                TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                        true
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                                TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                        false
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                                TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                        false
                )
        );
    }

    public static Stream<Arguments> provideToPlayMaoDeOnzeCondition() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                                TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.TWO, CardSuit.SPADES)
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.KING, CardSuit.SPADES),
                                TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.JACK, CardSuit.SPADES)
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                                TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.ACE, CardSuit.SPADES)
                )
        );
    }

    public static Stream<Arguments> provideToCheckRaiseInFirstRound() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                                TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                        true
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                                TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                                TrucoCard.of(CardRank.ACE, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                        false
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                                TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                        false
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                                TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                        true
                )
        );
    }

    public static Stream<Arguments> chooseMiddleCard() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                                TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                        TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                                TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                                TrucoCard.of(CardRank.ACE, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                        TrucoCard.of(CardRank.ACE, CardSuit.CLUBS)
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                                TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                                TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                        TrucoCard.of(CardRank.JACK, CardSuit.SPADES)
                )
        );
    }

    public static Stream<Arguments> ProvideToAcceptRaiseByOpponentIfFirstRoundWon() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                        true
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                        false
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                        true
                ),
                Arguments.of(
                        List.of(
                                TrucoCard.of(CardRank.JACK, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.ACE, CardSuit.CLUBS)
                        ),
                        TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                        false
                )
        );
    }
}
