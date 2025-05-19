package com.antonelli.gibim.degolabot;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class DegolaBotTest {

    private DegolaBot sut;

    GameIntel.StepBuilder intel;

    @BeforeEach
    void setUp(){sut = new DegolaBot(); }

    @Test
    public void testGetRaiseResponse() {
        GameIntel intel = mock(GameIntel.class);

        when(intel.getOpponentScore()).thenReturn(5);
        when(intel.getCards()).thenReturn(mock(List.class));

        FirstRound strategy = new FirstRound();
        int response = strategy.getRaiseResponse(intel);

        assertEquals(-1, response);
    }

    @Test
    public void testDecideIfRaises() {
        GameIntel intel = mock(GameIntel.class);

        when(intel.getOpponentCard()).thenReturn(Optional.empty());

        FirstRound strategy = new FirstRound();
        boolean result = strategy.decideIfRaises(intel);


        assertFalse(result);
    }
    @Nested
    @DisplayName("Testes ChooseCard")
    class ChooseCardTests {
        @Nested
        @DisplayName("Testes caso seja a primeira rodada")
        class FirstRoundTests {
            @Nested
            @DisplayName("Testes caso seja o primeiro a jogar")
            class FirstPlayerPlays {
                @Test
                @DisplayName("If only have bad cards then discard the one with lower value")
                    void ifOnlyHaveBadCardsThenDiscardTheOneWithLowerValue() {
                        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

                        List<TrucoCard> botCards = Arrays.asList(
                                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                                TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS)
                        );

                        List<TrucoCard> openCards = Collections.singletonList(vira);

                        intel = GameIntel.StepBuilder.with()
                                .gameInfo(List.of(), openCards, vira, 1)
                                .botInfo(botCards, 0)
                                .opponentScore(0);

                        assertThat(sut.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(0)));
                }
                @Test
                @DisplayName("Se tiver apenas cartas médias, usar a de maior valor")
                void IfOnlyHaveMiddleCardsThenUseTheOneWithHighestValue(){
                    TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

                    List<TrucoCard> botCards = Arrays.asList(
                            TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.JACK, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)
                    );

                    List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(
                            CardRank.ACE, CardSuit.DIAMONDS)
                    );
                    intel = GameIntel.StepBuilder.with()
                            .gameInfo(List.of(), openCards, vira, 0)
                            .botInfo(botCards, 0)
                            .opponentScore(0);

                    assertThat(sut.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(2)));
                }
        @Nested
        @DisplayName("Testes caso seja o segundo a jogar")
        class SecondPlayerPlays {

        }
            }
        }
    }
    @Nested
    @DisplayName("Testes getRaiseResponse")
    class GetRaiseResponseTests {}
    @Nested
    @DisplayName("Testes DecideIfRises")
    class DecideIfRaisesTests {
        @Nested
        @DisplayName("Se ganhar a primeira rodada")
        class WonFirstRound{

        }
    }
    @Nested
    @DisplayName("Testes getMaoDeOnzeResponse")
    class GetMaoDeOnzeResponseTests {}

    @Test
    public void testChooseCardReturnsValidCardToPlay() {
        TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.HEARTS);
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.SIX, CardSuit.HEARTS)
        );
        List<TrucoCard> openCards = List.of(vira);

        intel = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0);

        CardToPlay result = sut.chooseCard(intel.build());

        assertThat(botCards).contains(result.content());
    }

    @Test
    public void testChooseCardWithOnlyOneCard() {
        TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES);
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)
        );
        List<TrucoCard> openCards = List.of(vira);

        intel = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(1);

        CardToPlay result = sut.chooseCard(intel.build());

        assertEquals(botCards.get(0), result.content());
    }

    @Test
    public void testChooseCardDoesNotThrowWithBadCards() {
        TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.SIX, CardSuit.SPADES)
        );
        List<TrucoCard> openCards = List.of(vira);

        intel = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(3);

        assertDoesNotThrow(() -> sut.chooseCard(intel.build()));
    }

    @Test
    @DisplayName("Should accept mao de onze if hand strengh is higher than 21")
    void ShouldAcceptMaoDeOnzeIfHandStrengthIsHigherThan21(){
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(
                TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)
        );

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));

        intel = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), openCards, vira, 1)
                .botInfo(botCards, 11)
                .opponentScore(0);

        assertTrue(sut.getMaoDeOnzeResponse(intel.build()));
    }
    @Nested
    @DisplayName("Testing getMaoDeOnzeResponse")
    class getMaoDeOnzeResponseTest {
        @Test
        @DisplayName("Should refuse mao de onze if hand strengh is lower than 21")
        void ShouldRefuseMaoDeOnzeIfHandStrengthIsLowerThan21(){
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS)
            );

            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS)
            );

            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(botCards, 11)
                    .opponentScore(0);

            assertFalse(sut.getMaoDeOnzeResponse(intel.build()));
        }
    }
}

