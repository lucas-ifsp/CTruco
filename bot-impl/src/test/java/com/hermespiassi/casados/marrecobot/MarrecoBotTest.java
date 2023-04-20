package com.hermespiassi.casados.marrecobot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;
import static org.junit.jupiter.api.Assertions.*;


class MarrecoBotTest {
    private GameIntel.StepBuilder stepBuilder;
    private List<GameIntel.RoundResult> results;
    private List<TrucoCard> openCards;
    private List<TrucoCard> botCards;
    private TrucoCard vira;

    @Nested
    @DisplayName("Test logic of first round")
    class FirstRound {
        @Nested
        @DisplayName("Tests bot logic when opponent cards are manilha")
        class OpponentCardIsManilha {
            @BeforeEach
            void beforeEach() {
                results = List.of();
                vira = TrucoCard.of(ACE, DIAMONDS);
            }

            @Test
            @DisplayName("Should return single manilha that bot has when opponent card is of diamond")
            void shouldReturnSingleManilhaThatBotHasWhenOpponentCardIsOfDiamond() {
                botCards = List.of(TrucoCard.of(FOUR, DIAMONDS), TrucoCard.of(TWO, SPADES), TrucoCard.of(THREE, CLUBS));
                openCards = List.of(vira, TrucoCard.of(TWO, DIAMONDS));
                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(results, openCards, vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0)
                        .opponentCard(TrucoCard.of(TWO, DIAMONDS));

                CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());
                assertThat(cardToPlay.value().getSuit()).isEqualTo(SPADES);
            }

            @Test
            @DisplayName("Should return less manilha if opponent card is pica-fumo and bot have two manilhas")
            void shouldReturnLessManilhaIfOpponentCardIsPicaFumoAndBotHaveTwoManilhas() {
                results = List.of();
                botCards = List.of(
                        TrucoCard.of(TWO, HEARTS),
                        TrucoCard.of(FIVE, CLUBS),
                        TrucoCard.of(TWO, SPADES)
                );
                vira = TrucoCard.of(ACE, HEARTS);
                openCards = List.of(vira, TrucoCard.of(TWO, DIAMONDS));
                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(results, openCards, vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0)
                        .opponentCard(TrucoCard.of(TWO, DIAMONDS));

                CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());

                assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(TWO, SPADES));
            }

            @Test
            @DisplayName("Should return greater manilha if opponent card is pica-fumo and bot have three manilhas")
            void shouldReturnGreaterManilhaIfOpponentCardIsPicaFumoAndBotHaveThreeManilhas() {
                results = List.of();
                botCards = List.of(
                        TrucoCard.of(TWO, HEARTS),
                        TrucoCard.of(TWO, CLUBS),
                        TrucoCard.of(TWO, SPADES)
                );
                vira = TrucoCard.of(ACE, HEARTS);
                openCards = List.of(vira, TrucoCard.of(TWO, DIAMONDS));
                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(results, openCards, vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0)
                        .opponentCard(TrucoCard.of(TWO, DIAMONDS));

                CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());

                assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(TWO, CLUBS));
            }

            @Test
            @DisplayName("Should return manilha if opponent card is spades and bot have a greater manilha")
            void shouldReturnManilhaIfOpponentCardIsSpadesAndBotHaveAGreaterManilha() {
                results = List.of();
                botCards = List.of(
                        TrucoCard.of(THREE, DIAMONDS),
                        TrucoCard.of(TWO, HEARTS),
                        TrucoCard.of(FOUR, SPADES)
                );
                vira = TrucoCard.of(ACE, HEARTS);
                openCards = List.of(vira, TrucoCard.of(TWO, SPADES));
                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(results, openCards, vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0)
                        .opponentCard(TrucoCard.of(TWO, SPADES));

                CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());

                assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(TWO, HEARTS));
            }

            @Test
            @DisplayName("Should return greater manilha if opponent card is spades and bot have one greater manilha")
            void shouldReturnGreaterManilhaIfOpponentCardIsSpadesAndBotHaveOneGreaterManilha() {
                results = List.of();
                botCards = List.of(
                        TrucoCard.of(FOUR, DIAMONDS),
                        TrucoCard.of(TWO, DIAMONDS),
                        TrucoCard.of(TWO, CLUBS)
                );
                vira = TrucoCard.of(ACE, HEARTS);
                openCards = List.of(vira, TrucoCard.of(TWO, SPADES));
                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(results, openCards, vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0)
                        .opponentCard(TrucoCard.of(TWO, SPADES));

                CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());

                assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(TWO, CLUBS));
            }

            @Test
            @DisplayName("Should return less manilha if opponent card is spades and bot have two greater manilha")
            void shouldReturnLessManilhaIfOpponentCardIsSpadesAndBotHaveTwoGreaterManilha() {
                results = List.of();
                botCards = List.of(
                        TrucoCard.of(TWO, CLUBS),
                        TrucoCard.of(FIVE, DIAMONDS),
                        TrucoCard.of(TWO, HEARTS)
                );
                vira = TrucoCard.of(ACE, HEARTS);
                openCards = List.of(vira, TrucoCard.of(TWO, SPADES));
                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(results, openCards, vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0)
                        .opponentCard(TrucoCard.of(TWO, SPADES));

                CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());

                assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(TWO, HEARTS));
            }

            @Test
            @DisplayName("Should return card of clubs that bot has when opponent card is of hearts")
            void ShouldReturnCardOfClubsThatBotHasWhenOpponentCardIsOfHearts() {
                results = List.of();
                botCards = List.of(
                        TrucoCard.of(THREE, HEARTS),
                        TrucoCard.of(FIVE, DIAMONDS),
                        TrucoCard.of(TWO, CLUBS)
                );
                vira = TrucoCard.of(ACE, HEARTS);
                openCards = List.of(vira, TrucoCard.of(TWO, HEARTS));
                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(results, openCards, vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0)
                        .opponentCard(TrucoCard.of(TWO, HEARTS));

                CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());

                assertThat(cardToPlay.value().getSuit()).isEqualTo(CLUBS);
            }
        }

        @Test
        @DisplayName("Should return pica-fumo in first raise if bot has a pica-fumo")
        void shouldReturnPicaFumoInFirstRaiseIfBotHasAPicaFumo() {
            results = List.of();
            botCards = List.of(TrucoCard.of(TWO, HEARTS), TrucoCard.of(FIVE, CLUBS), TrucoCard.of(TWO, DIAMONDS));
            vira = TrucoCard.of(ACE, HEARTS);
            openCards = List.of(vira);
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(results, openCards, vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(0);

            CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());
            assertThat(cardToPlay.value().getSuit()).isEqualTo(DIAMONDS);
        }

        @Test
        @DisplayName("Should not return pica-fumo if opponent card is manilha")
        void shouldNotReturnPicaFumoIfOpponentCardIsManilha() {
            results = List.of();
            botCards = List.of(
                    TrucoCard.of(TWO, HEARTS),
                    TrucoCard.of(FIVE, CLUBS),
                    TrucoCard.of(TWO, DIAMONDS)
            );
            vira = TrucoCard.of(ACE, HEARTS);
            openCards = List.of(vira, TrucoCard.of(TWO, SPADES));
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(results, openCards, vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(0)
                    .opponentCard(TrucoCard.of(TWO, SPADES));

            CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());

            assertThat(cardToPlay.value().getSuit()).isNotEqualTo(DIAMONDS);
        }

        @Test
        @DisplayName("Should return pica-fumo if opponent card is not manilha")
        void shouldReturnPicaFumoIfOpponentCardIsNotManilha() {
            results = List.of();
            botCards = List.of(
                    TrucoCard.of(TWO, HEARTS),
                    TrucoCard.of(FIVE, CLUBS),
                    TrucoCard.of(TWO, DIAMONDS)
            );
            vira = TrucoCard.of(ACE, HEARTS);
            openCards = List.of(vira, TrucoCard.of(THREE, CLUBS));
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(results, openCards, vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(0)
                    .opponentCard(TrucoCard.of(THREE, CLUBS));

            CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());

            assertThat(cardToPlay.value().getSuit()).isEqualTo(DIAMONDS);
        }
    }
}
