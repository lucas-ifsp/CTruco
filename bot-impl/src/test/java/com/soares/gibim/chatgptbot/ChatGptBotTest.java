package com.soares.gibim.chatgptbot;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;



import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChatGptBotTest {

    private ChatGptBot sut;

    GameIntel.StepBuilder intel;

    @BeforeEach
    void setUp(){sut = new ChatGptBot(); }

    @Nested
    @DisplayName("Testing chooseCard")
    class chooseCardTest {
        @Nested
        @DisplayName("When is the first Round")
        class FirstRound {
            @Nested
            @DisplayName("When bot is the first to play")
            class FirstToPlay {
                @Test
                @DisplayName("If only have bad cards then discard the one with lower value")
                void IfOnlyHaveBadCardsThenDiscardTheOneWithLowerValue() {
                    TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

                    List<TrucoCard> botCards = Arrays.asList(
                            TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
                    );

                    List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(
                            CardRank.ACE, CardSuit.DIAMONDS)
                    );
                    intel = GameIntel.StepBuilder.with()
                            .gameInfo(List.of(), openCards, vira, 1)
                            .botInfo(botCards, 0)
                            .opponentScore(0);

                    assertThat(sut.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(2)));
                }

                @Test
                @DisplayName("If only as middle cards then use the one with highest value")
                void IfOnlyHaveMiddleCardsThenUseTheOneWithHighestValue() {
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
                            .gameInfo(List.of(), openCards, vira, 1)
                            .botInfo(botCards, 0)
                            .opponentScore(0);

                    assertThat(sut.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(2)));
                }

                @Test
                @DisplayName("If only have high cards then use the one with highest value")
                void IfOnlyHaveHighCardsThenUseTheOneWithHighestValue() {
                    TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

                    List<TrucoCard> botCards = Arrays.asList(
                            TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)
                    );

                    List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(
                            CardRank.ACE, CardSuit.DIAMONDS)
                    );
                    intel = GameIntel.StepBuilder.with()
                            .gameInfo(List.of(), openCards, vira, 1)
                            .botInfo(botCards, 0)
                            .opponentScore(0);

                    assertThat(sut.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(2)));
                }

                @Test
                @DisplayName("If has manilha and good cards use the highest card except the manilha")
                void IfHasManilhaAndGoodCardsUseTheHighestCardExceptTheManilha() {
                    TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

                    List<TrucoCard> botCards = Arrays.asList(
                            TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)
                    );

                    List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(
                            CardRank.ACE, CardSuit.DIAMONDS)
                    );
                    intel = GameIntel.StepBuilder.with()
                            .gameInfo(List.of(), openCards, vira, 1)
                            .botInfo(botCards, 0)
                            .opponentScore(0);

                    assertThat(sut.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(2)));
                }

                @Test
                @DisplayName("If has manilha and bad cards use the manilha")
                void IfHasManilhaAndBadCardsUseTheManilha() {
                    TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

                    List<TrucoCard> botCards = Arrays.asList(
                            TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS)
                    );

                    List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(
                            CardRank.ACE, CardSuit.DIAMONDS)
                    );
                    intel = GameIntel.StepBuilder.with()
                            .gameInfo(List.of(), openCards, vira, 1)
                            .botInfo(botCards, 0)
                            .opponentScore(0);

                    assertThat(sut.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(0)));
                }
            }
            @Nested
            @DisplayName("When bot is the second to play")
            class SecondToPlay {
                @Test
                @DisplayName("Try to kill opponent card with the weakest card")
                void TryToKillOpponentCardWithTheWeakestCard(){
                    TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

                    List<TrucoCard> botCards = Arrays.asList(
                            TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS)
                    );

                    TrucoCard opponentCard = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

                    List<TrucoCard> openCards = Arrays.asList(
                            TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS)
                    );

                    intel = GameIntel.StepBuilder.with()
                            .gameInfo(List.of(), openCards, vira, 1)
                            .botInfo(botCards, 0)
                            .opponentScore(0)
                            .opponentCard(opponentCard);

                    assertThat(sut.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(1)));
                }

                @Test
                @DisplayName("Discard weakest card when has bad cards and cant kill opponent card")
                void DiscardWeakestCardWhenHasBadCardsAndCantKillOpponentCard(){
                    TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

                    List<TrucoCard> botCards = Arrays.asList(
                            TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS)
                    );

                    TrucoCard opponentCard = TrucoCard.of(CardRank.THREE, CardSuit.HEARTS);

                    List<TrucoCard> openCards = Arrays.asList(
                            TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.THREE, CardSuit.HEARTS)
                    );

                    intel = GameIntel.StepBuilder.with()
                            .gameInfo(List.of(), openCards, vira, 1)
                            .botInfo(botCards, 0)
                            .opponentScore(0)
                            .opponentCard(opponentCard);

                    assertThat(sut.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(0)));
                }

                @Test
                @DisplayName("Should try to draw round if unable to opponent card")
                void ShouldTryToDrawRoundIfUnableToOpponentCard(){
                    TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

                    List<TrucoCard> botCards = Arrays.asList(
                            TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS)
                    );

                    TrucoCard opponentCard = TrucoCard.of(CardRank.SIX, CardSuit.HEARTS);

                    List<TrucoCard> openCards = Arrays.asList(
                            TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.SIX, CardSuit.HEARTS)
                    );

                    intel = GameIntel.StepBuilder.with()
                            .gameInfo(List.of(), openCards, vira, 1)
                            .botInfo(botCards, 0)
                            .opponentScore(0)
                            .opponentCard(opponentCard);

                    assertThat(sut.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(2)));
                }

                @Test
                @DisplayName("When has good cards and manilha should try to kill opponent card with a weak card")
                void WhenHasGoodCardsAndManilhaShouldTryToKillOpponentCardWithAWeakCard(){
                    TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

                    List<TrucoCard> botCards = Arrays.asList(
                            TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)
                    );

                    TrucoCard opponentCard = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);

                    List<TrucoCard> openCards = Arrays.asList(
                            TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.KING, CardSuit.HEARTS)
                    );

                    intel = GameIntel.StepBuilder.with()
                            .gameInfo(List.of(), openCards, vira, 1)
                            .botInfo(botCards, 0)
                            .opponentScore(0)
                            .opponentCard(opponentCard);

                    assertThat(sut.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(1)));
                }

                @Test
                @DisplayName("When has bad cards and manilha should try to kill opponent card with manilha")
                void WhenHasBadCardsAndManilhaShouldTryToKillOpponentCardWithManilha(){
                    TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

                    List<TrucoCard> botCards = Arrays.asList(
                            TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS)
                    );

                    TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);

                    List<TrucoCard> openCards = Arrays.asList(
                            TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.KING, CardSuit.HEARTS)
                    );

                    intel = GameIntel.StepBuilder.with()
                            .gameInfo(List.of(), openCards, vira, 1)
                            .botInfo(botCards, 0)
                            .opponentScore(0)
                            .opponentCard(opponentCard);

                    assertThat(sut.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(0)));
                }
            }
        }

        @Nested
        @DisplayName("When is the second Round")
        class SecondRound {
            @Nested
            @DisplayName("When won first Round")
            class WonFirstRound {
                @Test
                @DisplayName("If only has bad cards should use the strongest")
                void IfOnlyHasBadCardsShouldUseTheStrongest(){
                    TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

                    List<TrucoCard> botCards = Arrays.asList(
                            TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS)
                    );

                    List<TrucoCard> openCards = Collections.singletonList(
                            TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS)
                    );

                    intel = GameIntel.StepBuilder.with()
                            .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                            .botInfo(botCards, 0)
                            .opponentScore(0);

                    assertThat(sut.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(1)));
                }

                @Test
                @DisplayName("If only has middle cards should use the strongest")
                void IfOnlyHasMiddleCardsShouldUseTheStrongest(){
                    TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

                    List<TrucoCard> botCards = Arrays.asList(
                            TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS)
                    );

                    List<TrucoCard> openCards = Collections.singletonList(
                            TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS)
                    );

                    intel = GameIntel.StepBuilder.with()
                            .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                            .botInfo(botCards, 0)
                            .opponentScore(0);

                    assertThat(sut.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(0)));
                }

                @Test
                @DisplayName("If only has good cards should use the second strongest")
                void IfOnlyHasGoodCardsShouldUseTheSecondStrongest(){
                    TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

                    List<TrucoCard> botCards = Arrays.asList(
                            TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)
                    );

                    List<TrucoCard> openCards = Collections.singletonList(
                            TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS)
                    );

                    intel = GameIntel.StepBuilder.with()
                            .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                            .botInfo(botCards, 0)
                            .opponentScore(0);

                    assertThat(sut.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(0)));
                }

                @Test
                @DisplayName("If has manilha should use the weakest card")
                void IfHasManilhaShouldUseTheWeakestCard(){
                    TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

                    List<TrucoCard> botCards = Arrays.asList(
                            TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS)
                    );

                    List<TrucoCard> openCards = Collections.singletonList(
                            TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS)
                    );

                    intel = GameIntel.StepBuilder.with()
                            .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                            .botInfo(botCards, 0)
                            .opponentScore(0);

                    assertThat(sut.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(0)));
                }
            }

            @Nested
            @DisplayName("When lost first Round")
            class LostFirstRound {
                @Test
                @DisplayName("If only has bad cards should try ot kill opponent card with the weakest")
                void IfOnlyHasBadCardsShouldTryToKillOpponentCardWithTheWeakest(){
                    TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

                    List<TrucoCard> botCards = Arrays.asList(
                            TrucoCard.of(CardRank.SIX, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS)
                    );

                    TrucoCard opponentCard = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

                    List<TrucoCard> openCards = Arrays.asList(
                            TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS)
                    );

                    intel = GameIntel.StepBuilder.with()
                            .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                            .botInfo(botCards, 0)
                            .opponentScore(0)
                            .opponentCard(opponentCard);

                    assertThat(sut.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(1)));
                }

                @Test
                @DisplayName("If has manilha should try to kill opponent card with the weakest card")
                void IfHasManilhaShouldTryToKillOpponentCardWithTheWeakestCard(){
                    TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

                    List<TrucoCard> botCards = Arrays.asList(
                            TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)
                    );

                    TrucoCard opponentCard = TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS);

                    List<TrucoCard> openCards = Arrays.asList(
                            TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS)
                    );

                    intel = GameIntel.StepBuilder.with()
                            .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                            .botInfo(botCards, 0)
                            .opponentScore(0)
                            .opponentCard(opponentCard);

                    assertThat(sut.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(1)));
                }

                @Test
                @DisplayName("If has manilha and is unable to kill opponent card with the weakest should use the manilha")
                void IfHasManilhaAndIsUnableToKillOpponentCardWithTheWeakestShouldUseTheManilha(){
                    TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

                    List<TrucoCard> botCards = Arrays.asList(
                            TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS)
                    );

                    TrucoCard opponentCard = TrucoCard.of(CardRank.THREE, CardSuit.HEARTS);

                    List<TrucoCard> openCards = Arrays.asList(
                            TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.THREE, CardSuit.HEARTS)
                    );

                    intel = GameIntel.StepBuilder.with()
                            .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                            .botInfo(botCards, 0)
                            .opponentScore(0)
                            .opponentCard(opponentCard);

                    assertThat(sut.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(1)));
                }
            }
        }

        @Nested
        @DisplayName("When is the third Round")
        class ThirdRound {
            @Test
            @DisplayName("Just play with the last card when is the first to play")
            void JustPlayTheLastCardIfFirstToPlay(){
                TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

                List<TrucoCard> botCards = Collections.singletonList(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS));

                List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));

                intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0);

                assertThat(sut.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(0)));
            }

            @Test
            @DisplayName("Just play with the last card when is the second to play")
            void JustPlayTheLastCardIfSecondToPlay(){
                TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

                List<TrucoCard> botCards = Collections.singletonList(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS));

                TrucoCard opponentCard = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

                List<TrucoCard> openCards = Arrays.asList(
                        TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS)
                );


                intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0).
                        opponentCard(opponentCard);

                assertThat(sut.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(0)));
            }
        }
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

            List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));

            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(botCards, 11)
                    .opponentScore(0);

            assertFalse(sut.getMaoDeOnzeResponse(intel.build()));
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
    }

    @Nested
    @DisplayName("Testing decideIfRaises")
    class decideIfRaisesTest {
        @Test
        @DisplayName("Should ask for raise if won the first round")
        void ShouldAskForRaiseIfWonTheFirstRound(){
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)
            );

            List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));

            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(0);

            assertTrue(sut.decideIfRaises(intel.build()));
        }
    }
    @Test
    @DisplayName("If its the last hand and have zap then ask truco")
    void IfItsTheLastRoundAndHaveZapThenAskTruco() {
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Collections.singletonList(
                TrucoCard.of(CardRank.SIX, CardSuit.CLUBS)
        );

        List<TrucoCard> openCards = Collections.singletonList(
                TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS)
        );

        intel = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON, GameIntel.RoundResult.LOST), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0);

        assertTrue(sut.decideIfRaises(intel.build()));
    }

    @Test
    @DisplayName("Decide if you accept 'truco' in the first round with a 'manilha.")
    void TestAcceptTrucoFirstRoundWithManilha() {
        TrucoCard vira = TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Collections.singletonList(
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
        );
        List<TrucoCard> openCards = Collections.singletonList(
                TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)
        );

        intel = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0);

        assertEquals(0, sut.getRaiseResponse(intel.build()));
    }
    @Test
    @DisplayName("Accept 'truco' in the first round if the sum of the card values is greater than 24.")
    void TestAcceptTrucoFirstRoundWithValue() {
        TrucoCard vira = TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.TWO,CardSuit.CLUBS),
                TrucoCard.of(CardRank.THREE,CardSuit.HEARTS));

        List<TrucoCard> openCards = Collections.singletonList(
                TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)
        );

        intel = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0);

        assertEquals(0, sut.getRaiseResponse(intel.build()));
    }

    @Test
    @DisplayName("Decide if you accept 'truco' in the second round with a 'manilha")
    void TestAcceptTrucoSecondRoundWithManilha() {
        TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);

        List<TrucoCard> botCards = Collections.singletonList(
                TrucoCard.of(CardRank.JACK, CardSuit.SPADES)
        );
        List<TrucoCard> openCards = Collections.singletonList(
                TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS)
        );


        intel = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0);

        assertEquals(0, sut.getRaiseResponse(intel.build()));
    }

    @Test
    @DisplayName("Accept 'truco' in the second round if the sum of the card values is greater than 16.")
    void TestAcceptTrucoSecondRoundWithValue() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(
                TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.TWO,CardSuit.CLUBS));

        List<TrucoCard> openCards = Collections.singletonList(
                TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS)
        );

        intel = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0);

        assertEquals(0, sut.getRaiseResponse(intel.build()));
    }

}

