package com.hermespiassi.casados.marrecobot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;
import static org.assertj.core.api.Assertions.assertThat;


class MarrecoBotTest {
    private GameIntel.StepBuilder stepBuilder;
    private List<GameIntel.RoundResult> results;
    private List<TrucoCard> openCards;
    private List<TrucoCard> botCards;
    private TrucoCard vira;

        @Nested
        @DisplayName("Tests bot logic when bot have manilha and opponent cards are manilha")
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

            @Nested
            @DisplayName("Opponent card is manilha of clubs")
            class ManilhaOfClubs {
                @BeforeEach
                void beforeEach() {
                    openCards = List.of(vira, TrucoCard.of(TWO, CLUBS));
                }
                @Test
                @DisplayName("Should return weak card when bot has one manilha")
                void shouldReturnWeakCardWhenBotHasOneManilha() {
                    botCards = List.of(TrucoCard.of(KING, CLUBS), TrucoCard.of(TWO, SPADES), TrucoCard.of(SEVEN, DIAMONDS));
                    stepBuilder = GameIntel.StepBuilder.with()
                            .gameInfo(results, openCards, vira, 1)
                            .botInfo(botCards, 0)
                            .opponentScore(0)
                            .opponentCard(TrucoCard.of(TWO, CLUBS));

                    CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());
                    assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(SEVEN, DIAMONDS));
                }

                @Test
                @DisplayName("Should return single weak card when bot has two manilhas")
                void shouldReturnSingleWeakCardWhenBotHasTwo() {
                    botCards = List.of(TrucoCard.of(TWO, DIAMONDS), TrucoCard.of(TWO, SPADES), TrucoCard.of(SEVEN, DIAMONDS));
                    stepBuilder = GameIntel.StepBuilder.with()
                            .gameInfo(results, openCards, vira, 1)
                            .botInfo(botCards, 0)
                            .opponentScore(0)
                            .opponentCard(TrucoCard.of(TWO, CLUBS));

                    CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());
                    assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(SEVEN, DIAMONDS));
                }

                @Test
                @DisplayName("Should return weak manilha when boy has three manilhas")
                void shouldReturnWeakManilhaWhenBoyHasThreeManilhas() {
                    botCards = List.of(TrucoCard.of(TWO, SPADES), TrucoCard.of(TWO, DIAMONDS), TrucoCard.of(TWO, HEARTS));
                    stepBuilder = GameIntel.StepBuilder.with()
                            .gameInfo(results, openCards, vira, 1)
                            .botInfo(botCards, 0)
                            .opponentScore(0)
                            .opponentCard(TrucoCard.of(TWO, CLUBS));

                    CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());
                    assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(TWO, DIAMONDS));
                }
            }
        }

        @Nested
        @DisplayName("Test bot logic when bot have manilha and opponent cards are not manilha")
        class OpponentCardIsNotManilha {
            @BeforeEach
            void beforeEach() {
                results = List.of();
                vira = TrucoCard.of(ACE, DIAMONDS);
            }

            @Nested
            @DisplayName("Bot has only manilha of diamond")
            class BotManilhaOfDiamond {
                @Test
                @DisplayName("Should return manilha of diamond when opponent card is not manilha but is greater other bot cards")
                void shouldReturnManilhaOfDiamondWhenOpponentCardIsNotManilhaButIsGreaterOtherBotCards() {
                    botCards = List.of(TrucoCard.of(FOUR, CLUBS), TrucoCard.of(TWO, DIAMONDS), TrucoCard.of(KING, DIAMONDS));
                    openCards = List.of(vira, TrucoCard.of(THREE, CLUBS));
                    stepBuilder = GameIntel.StepBuilder.with()
                            .gameInfo(results, openCards, vira, 1)
                            .botInfo(botCards, 0)
                            .opponentScore(0)
                            .opponentCard(TrucoCard.of(THREE, CLUBS));

                    CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());
                    assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(TWO, DIAMONDS));
                }

                @Test
                @DisplayName("Should return greater card that bot has when opponent card is greater but not is manilha")
                void shouldReturnGreaterCardThatBotHasWhenOpponentCardIsGreaterButNotIsManilha() {
                    botCards = List.of(TrucoCard.of(FOUR, CLUBS), TrucoCard.of(TWO, DIAMONDS), TrucoCard.of(SIX, DIAMONDS));
                    openCards = List.of(vira, TrucoCard.of(FIVE, DIAMONDS));
                    stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(results, openCards, vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0)
                        .opponentCard(TrucoCard.of(FIVE, DIAMONDS));

                    CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());
                    assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(SIX, DIAMONDS));
                }

                @Test
                @DisplayName("Should return weak card of greater cards, but not manilha ")
                void shouldReturnWeakCardOfGreaterCardsButNotManilha() {
                    botCards = List.of(TrucoCard.of(QUEEN, CLUBS), TrucoCard.of(TWO, DIAMONDS), TrucoCard.of(SIX, SPADES));
                    openCards = List.of(vira, TrucoCard.of(FIVE, DIAMONDS));
                    stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(results, openCards, vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0)
                        .opponentCard(TrucoCard.of(FIVE, DIAMONDS));

                    CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());
                    assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(SIX, SPADES));
                }
            }
        }

//        @Test
//        @DisplayName("Should return weak card that bot has when opponent card is manilha and bot dont have manilha")
//        void shouldReturnWeakCardThatBotHasWhenOpponentCardIsManilhaAndBotDontHaveManilha() {
//            botCards = List.of(TrucoCard.of(THREE, CLUBS), TrucoCard.of(QUEEN, HEARTS), TrucoCard.of(FOUR, SPADES));
//            openCards = List.of(vira, TrucoCard.of(TWO, DIAMONDS));
//            stepBuilder = GameIntel.StepBuilder.with()
//                    .gameInfo(results, openCards, vira, 1)
//                    .botInfo(botCards, 0)
//                    .opponentScore(0)
//                    .opponentCard(TrucoCard.of(TWO, DIAMONDS));
//
//            CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());
//            assertThat(cardToPlay.value().getRank()).isEqualTo(FOUR);
//        }

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
}
