package com.soares.gibim.chatgptbot;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChatGptBotTest {

    private ChatGptBot sut;
    private TrucoCard vira;
    private List<TrucoCard> openCards;

    GameIntel.StepBuilder intel;

    @BeforeEach
    void setUp(){sut = new ChatGptBot(); }

    @Nested
    @DisplayName("Testing chooseCard")
    class chooseCardTest {
        @Nested
        @DisplayName("When is the first Round")
        class FirstRound {

            @BeforeEach
            void setUp(){
                vira = TrucoCard.of(ACE, DIAMONDS);
            }

            @Nested
            @DisplayName("When bot is the first to play")
            class FirstToPlay {

                @BeforeEach
                void setUp(){
                    openCards = Collections.singletonList(TrucoCard.of(ACE, DIAMONDS));
                }
                @Test
                @DisplayName("If only have bad cards then discard the one with lower value")
                void IfOnlyHaveBadCardsThenDiscardTheOneWithLowerValue() {

                    List<TrucoCard> botCards = Arrays.asList(
                            TrucoCard.of(SEVEN, CLUBS),
                            TrucoCard.of(FIVE, HEARTS),
                            TrucoCard.of(FOUR, DIAMONDS)
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

                    List<TrucoCard> botCards = Arrays.asList(
                            TrucoCard.of(QUEEN, CLUBS),
                            TrucoCard.of(JACK, HEARTS),
                            TrucoCard.of(KING, DIAMONDS)
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

                    List<TrucoCard> botCards = Arrays.asList(
                            TrucoCard.of(ACE, CLUBS),
                            TrucoCard.of(TWO, HEARTS),
                            TrucoCard.of(THREE, DIAMONDS)
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

                    List<TrucoCard> botCards = Arrays.asList(
                            TrucoCard.of(TWO, CLUBS),
                            TrucoCard.of(ACE, HEARTS),
                            TrucoCard.of(THREE, DIAMONDS)
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

                    List<TrucoCard> botCards = Arrays.asList(
                            TrucoCard.of(TWO, CLUBS),
                            TrucoCard.of(FOUR, HEARTS),
                            TrucoCard.of(ACE, DIAMONDS)
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

                    List<TrucoCard> botCards = Arrays.asList(
                            TrucoCard.of(FOUR, CLUBS),
                            TrucoCard.of(FIVE, HEARTS),
                            TrucoCard.of(SIX, DIAMONDS)
                    );

                    TrucoCard opponentCard = TrucoCard.of(FOUR, HEARTS);

                    List<TrucoCard> openCards = Arrays.asList(
                            TrucoCard.of(ACE, DIAMONDS), TrucoCard.of(FOUR, HEARTS)
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

                    List<TrucoCard> botCards = Arrays.asList(
                            TrucoCard.of(FOUR, CLUBS),
                            TrucoCard.of(FIVE, HEARTS),
                            TrucoCard.of(SIX, DIAMONDS)
                    );

                    TrucoCard opponentCard = TrucoCard.of(THREE, HEARTS);

                    List<TrucoCard> openCards = Arrays.asList(
                            TrucoCard.of(ACE, DIAMONDS), TrucoCard.of(THREE, HEARTS)
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

                    List<TrucoCard> botCards = Arrays.asList(
                            TrucoCard.of(FOUR, CLUBS),
                            TrucoCard.of(FIVE, HEARTS),
                            TrucoCard.of(SIX, DIAMONDS)
                    );

                    TrucoCard opponentCard = TrucoCard.of(SIX, HEARTS);

                    List<TrucoCard> openCards = Arrays.asList(
                            TrucoCard.of(ACE, DIAMONDS), TrucoCard.of(SIX, HEARTS)
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

                    List<TrucoCard> botCards = Arrays.asList(
                            TrucoCard.of(TWO, CLUBS),
                            TrucoCard.of(ACE, HEARTS),
                            TrucoCard.of(THREE, DIAMONDS)
                    );

                    TrucoCard opponentCard = TrucoCard.of(KING, HEARTS);

                    List<TrucoCard> openCards = Arrays.asList(
                            TrucoCard.of(ACE, DIAMONDS), TrucoCard.of(KING, HEARTS)
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

                    List<TrucoCard> botCards = Arrays.asList(
                            TrucoCard.of(TWO, CLUBS),
                            TrucoCard.of(SIX, HEARTS),
                            TrucoCard.of(SEVEN, DIAMONDS)
                    );

                    TrucoCard opponentCard = TrucoCard.of(TWO, HEARTS);

                    List<TrucoCard> openCards = Arrays.asList(
                            TrucoCard.of(ACE, DIAMONDS), TrucoCard.of(KING, HEARTS)
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
                    TrucoCard vira = TrucoCard.of(ACE, DIAMONDS);

                    List<TrucoCard> botCards = Arrays.asList(
                            TrucoCard.of(FOUR, CLUBS),
                            TrucoCard.of(SIX, DIAMONDS)
                    );

                    List<TrucoCard> openCards = Collections.singletonList(
                            TrucoCard.of(ACE, DIAMONDS)
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
                    TrucoCard vira = TrucoCard.of(ACE, DIAMONDS);

                    List<TrucoCard> botCards = Arrays.asList(
                            TrucoCard.of(KING, CLUBS),
                            TrucoCard.of(QUEEN, HEARTS)
                    );

                    List<TrucoCard> openCards = Collections.singletonList(
                            TrucoCard.of(ACE, DIAMONDS)
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
                    TrucoCard vira = TrucoCard.of(ACE, DIAMONDS);

                    List<TrucoCard> botCards = Arrays.asList(
                            TrucoCard.of(ACE, HEARTS),
                            TrucoCard.of(THREE, DIAMONDS)
                    );

                    List<TrucoCard> openCards = Collections.singletonList(
                            TrucoCard.of(ACE, DIAMONDS)
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
                    TrucoCard vira = TrucoCard.of(ACE, DIAMONDS);

                    List<TrucoCard> botCards = Arrays.asList(
                            TrucoCard.of(SIX, HEARTS),
                            TrucoCard.of(TWO, DIAMONDS)
                    );

                    List<TrucoCard> openCards = Collections.singletonList(
                            TrucoCard.of(ACE, DIAMONDS)
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
                    TrucoCard vira = TrucoCard.of(ACE, DIAMONDS);

                    List<TrucoCard> botCards = Arrays.asList(
                            TrucoCard.of(SIX, CLUBS),
                            TrucoCard.of(FIVE, DIAMONDS)
                    );

                    TrucoCard opponentCard = TrucoCard.of(FOUR, HEARTS);

                    List<TrucoCard> openCards = Arrays.asList(
                            TrucoCard.of(ACE, DIAMONDS), TrucoCard.of(FOUR, HEARTS)
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
                    TrucoCard vira = TrucoCard.of(ACE, DIAMONDS);

                    List<TrucoCard> botCards = Arrays.asList(
                            TrucoCard.of(TWO, CLUBS),
                            TrucoCard.of(KING, DIAMONDS)
                    );

                    TrucoCard opponentCard = TrucoCard.of(QUEEN, HEARTS);

                    List<TrucoCard> openCards = Arrays.asList(
                            TrucoCard.of(ACE, DIAMONDS), TrucoCard.of(QUEEN, HEARTS)
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
                    TrucoCard vira = TrucoCard.of(ACE, DIAMONDS);

                    List<TrucoCard> botCards = Arrays.asList(
                            TrucoCard.of(KING, CLUBS),
                            TrucoCard.of(TWO, DIAMONDS)
                    );

                    TrucoCard opponentCard = TrucoCard.of(THREE, HEARTS);

                    List<TrucoCard> openCards = Arrays.asList(
                            TrucoCard.of(ACE, DIAMONDS), TrucoCard.of(THREE, HEARTS)
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
                TrucoCard vira = TrucoCard.of(ACE, DIAMONDS);

                List<TrucoCard> botCards = Collections.singletonList(TrucoCard.of(THREE, DIAMONDS));

                List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(ACE, DIAMONDS));

                intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0);

                assertThat(sut.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(0)));
            }

            @Test
            @DisplayName("Just play with the last card when is the second to play")
            void JustPlayTheLastCardIfSecondToPlay(){
                TrucoCard vira = TrucoCard.of(ACE, DIAMONDS);

                List<TrucoCard> botCards = Collections.singletonList(TrucoCard.of(THREE, DIAMONDS));

                TrucoCard opponentCard = TrucoCard.of(SIX, DIAMONDS);

                List<TrucoCard> openCards = Arrays.asList(
                        TrucoCard.of(ACE, DIAMONDS), TrucoCard.of(SIX, DIAMONDS)
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
            TrucoCard vira = TrucoCard.of(ACE, DIAMONDS);

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(FOUR, CLUBS),
                    TrucoCard.of(FIVE, HEARTS),
                    TrucoCard.of(SIX, DIAMONDS)
            );

            List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(ACE, DIAMONDS));

            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(botCards, 11)
                    .opponentScore(0);

            assertFalse(sut.getMaoDeOnzeResponse(intel.build()));
        }

        @Test
        @DisplayName("Should accept mao de onze if hand strengh is higher than 21")
        void ShouldAcceptMaoDeOnzeIfHandStrengthIsHigherThan21(){
            TrucoCard vira = TrucoCard.of(ACE, DIAMONDS);

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(KING, CLUBS),
                    TrucoCard.of(ACE, HEARTS),
                    TrucoCard.of(KING, DIAMONDS)
            );

            List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(ACE, DIAMONDS));

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
        @Nested
        @DisplayName("If Won First Round")
        class WonFirstRound {
            @Test
            @DisplayName("Should ask for raise")
            void ShouldAskForRaiseIfWonTheFirstRound(){
                TrucoCard vira = TrucoCard.of(ACE, DIAMONDS);

                List<TrucoCard> botCards = Arrays.asList(
                        TrucoCard.of(ACE, HEARTS),
                        TrucoCard.of(KING, DIAMONDS)
                );

                List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(ACE, DIAMONDS));

                intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0);

                assertTrue(sut.decideIfRaises(intel.build()));
            }
        }

        @Nested
        @DisplayName("If Lost First Round")
        class LostFirstRound {
            @Test
            @DisplayName("Should ask for raise if has 2 manilhas")
            void ShouldAskForRaiseIfHas2Manilhas(){
                TrucoCard vira = TrucoCard.of(ACE, DIAMONDS);

                List<TrucoCard> botCards = Arrays.asList(
                        TrucoCard.of(TWO, HEARTS),
                        TrucoCard.of(TWO, DIAMONDS)
                );

                TrucoCard opponentCard = TrucoCard.of(ACE, CLUBS);

                List<TrucoCard> openCards = Arrays.asList(
                        TrucoCard.of(ACE, DIAMONDS),
                        TrucoCard.of(ACE, CLUBS)
                );

                intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0)
                        .opponentCard(opponentCard);

                assertTrue(sut.decideIfRaises(intel.build()));
            }

            @Test
            @DisplayName("Should ask for raise if has 1 manilha and other card equal or higher than Two")
            void ShouldAskForRaiseIfHas1ManilhaAndOtherCardEqualOrHigherThanTwo(){
                TrucoCard vira = TrucoCard.of(KING, DIAMONDS);

                List<TrucoCard> botCards = Arrays.asList(
                        TrucoCard.of(ACE, HEARTS),
                        TrucoCard.of(TWO, DIAMONDS)
                );

                TrucoCard opponentCard = TrucoCard.of(ACE, CLUBS);

                List<TrucoCard> openCards = Arrays.asList(
                        TrucoCard.of(KING, DIAMONDS),
                        TrucoCard.of(ACE, CLUBS)
                );

                intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0)
                        .opponentCard(opponentCard);

                assertTrue(sut.decideIfRaises(intel.build()));
            }
        }

        @Nested
        @DisplayName("If Won first round but lost the second")
        class WonFirstRoundButLostSecond {
            @Test
            @DisplayName("Should ask for raise if has manilha")
            void ShouldAskForRaiseIfHasManilha(){
                TrucoCard vira = TrucoCard.of(ACE, DIAMONDS);

                List<TrucoCard> botCards = Collections.singletonList(
                        TrucoCard.of(TWO, HEARTS)
                );

                TrucoCard opponentCard = TrucoCard.of(ACE, CLUBS);

                List<TrucoCard> openCards = Arrays.asList(
                        TrucoCard.of(ACE, DIAMONDS),
                        TrucoCard.of(ACE, CLUBS)
                );

                intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(GameIntel.RoundResult.WON, GameIntel.RoundResult.LOST), openCards, vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0)
                        .opponentCard(opponentCard);

                assertTrue(sut.decideIfRaises(intel.build()));
            }

            @Test
            @DisplayName("Should ask for raise if able to draw")
            void ShouldAskForRaiseIfAbleToDraw(){
                TrucoCard vira = TrucoCard.of(KING, DIAMONDS);

                List<TrucoCard> botCards = Collections.singletonList(
                        TrucoCard.of(ACE, HEARTS)
                );

                TrucoCard opponentCard = TrucoCard.of(ACE, CLUBS);

                List<TrucoCard> openCards = Arrays.asList(
                        TrucoCard.of(ACE, DIAMONDS),
                        TrucoCard.of(ACE, CLUBS)
                );

                intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(GameIntel.RoundResult.WON, GameIntel.RoundResult.LOST), openCards, vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0)
                        .opponentCard(opponentCard);

                assertTrue(sut.decideIfRaises(intel.build()));
            }

            @Test
            @DisplayName("Should ask for raise if opponent used a card weaker than queen")
            void ShouldAskForRaiseIfOpponentUsedACardWeakerThanQueen(){
                TrucoCard vira = TrucoCard.of(KING, DIAMONDS);

                List<TrucoCard> botCards = Collections.singletonList(
                        TrucoCard.of(ACE, HEARTS)
                );

                TrucoCard opponentCard = TrucoCard.of(SEVEN, CLUBS);

                List<TrucoCard> openCards = Arrays.asList(
                        TrucoCard.of(ACE, DIAMONDS),
                        TrucoCard.of(SEVEN, CLUBS)
                );

                intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(GameIntel.RoundResult.WON, GameIntel.RoundResult.LOST), openCards, vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0)
                        .opponentCard(opponentCard);

                assertTrue(sut.decideIfRaises(intel.build()));
            }

            @Test
            @DisplayName("Should ask for raise if has any card higher than opponent's card")
            void ShouldAskForRaiseIfHasAnyCardHigherThanOpponentsCard(){
                TrucoCard vira = TrucoCard.of(KING, DIAMONDS);

                List<TrucoCard> botCards = Collections.singletonList(
                        TrucoCard.of(THREE, HEARTS)
                );

                TrucoCard opponentCard = TrucoCard.of(TWO, CLUBS);

                List<TrucoCard> openCards = Arrays.asList(
                        TrucoCard.of(KING, DIAMONDS),
                        TrucoCard.of(TWO, CLUBS)
                );

                intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(GameIntel.RoundResult.WON, GameIntel.RoundResult.LOST), openCards, vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0)
                        .opponentCard(opponentCard);

                assertTrue(sut.decideIfRaises(intel.build()));
            }
        }

        @Nested
        @DisplayName("If Lost first round but won the second")
        class LostFirstRoundButWonSecond {
            @Test
            @DisplayName("Should ask for raise if has manilha equal or higher than hearts")
            void ShouldAskForRaiseIfHasManilhaEqualToOrHigherThanHearts(){
                TrucoCard vira = TrucoCard.of(ACE, DIAMONDS);

                List<TrucoCard> botCards = Collections.singletonList(
                        TrucoCard.of(TWO, HEARTS)
                );

                List<TrucoCard> openCards = Collections.singletonList(
                        TrucoCard.of(ACE, DIAMONDS)
                );

                intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(GameIntel.RoundResult.LOST, GameIntel.RoundResult.WON), openCards, vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0);

                assertTrue(sut.decideIfRaises(intel.build()));
            }

            @Test
            @DisplayName("Should ask for raise if has a three")
            void ShouldAskForRaiseIfHasAThree(){
                TrucoCard vira = TrucoCard.of(ACE, DIAMONDS);

                List<TrucoCard> botCards = Collections.singletonList(
                        TrucoCard.of(THREE, HEARTS)
                );

                List<TrucoCard> openCards = Collections.singletonList(
                        TrucoCard.of(ACE, DIAMONDS)
                );

                intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(GameIntel.RoundResult.LOST, GameIntel.RoundResult.WON), openCards, vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0);

                assertTrue(sut.decideIfRaises(intel.build()));
            }
        }
    }

    @Nested
    @DisplayName("Testing getRaiseResponse")
    class GetRaiseResponseTest {

        @Test
        @DisplayName("Decide if you accept 'truco' in the first round with a 'manilha.")
        void TestAcceptTrucoFirstRoundWithManilha() {
            TrucoCard vira = TrucoCard.of(THREE, DIAMONDS);

            List<TrucoCard> botCards = Collections.singletonList(
                    TrucoCard.of(FOUR, DIAMONDS)
            );
            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(THREE, DIAMONDS)
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
            TrucoCard vira = TrucoCard.of(THREE, DIAMONDS);

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(ACE, DIAMONDS),
                    TrucoCard.of(TWO, CLUBS),
                    TrucoCard.of(THREE, HEARTS));

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(THREE, DIAMONDS)
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
            TrucoCard vira = TrucoCard.of(QUEEN, CLUBS);

            List<TrucoCard> botCards = Collections.singletonList(
                    TrucoCard.of(JACK, SPADES)
            );
            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(QUEEN, CLUBS)
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
            TrucoCard vira = TrucoCard.of(SIX, DIAMONDS);

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(TWO, DIAMONDS),
                    TrucoCard.of(TWO, CLUBS));

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(SIX, DIAMONDS)
            );

            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(0);

            assertEquals(0, sut.getRaiseResponse(intel.build()));
        }

        @Test
        @DisplayName("Accept 'truco' in the third round with a card higher than ACE.")
        void TestAcceptTrucoThirdRoundWithCardHigherThanAce() {
            TrucoCard vira = TrucoCard.of(FIVE, SPADES);

            List<TrucoCard> botCards = Collections.singletonList(
                    TrucoCard.of(ACE, HEARTS)
            );
            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(FIVE, SPADES)
            );


            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST, GameIntel.RoundResult.WON), openCards, vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(0);

            assertEquals(0, sut.getRaiseResponse(intel.build()));
        }

        @Test
        @DisplayName("Increase truco request in the first round with two manilhas.")
        void testTrucoRequestFirstRoundWithTwoManilhas() {
            TrucoCard vira = TrucoCard.of(FOUR, DIAMONDS);

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(FIVE, SPADES),
                    TrucoCard.of(FIVE, CLUBS));

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(FOUR, DIAMONDS)
            );

            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 3)
                    .botInfo(botCards, 0)
                    .opponentScore(0);

            assertEquals(1, sut.getRaiseResponse(intel.build()));
        }

        @Test
        @DisplayName("Increase truco request in the Second round with two manilhas.")
        void testTrucoRequestSecondRoundWithTwoManilhas() {
            TrucoCard vira = TrucoCard.of(TWO, SPADES);

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(THREE, DIAMONDS),
                    TrucoCard.of(THREE, HEARTS));

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(TWO, SPADES)
            );

            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 3)
                    .botInfo(botCards, 0)
                    .opponentScore(0);

            assertEquals(1, sut.getRaiseResponse(intel.build()));
        }

        @Test
        @DisplayName("Increases 'truco' in the first round if you have a 'manilha' of Spades or higher and a 2 or higher in hand.")
        void TestIncreasesTrucoFirstRoundHaveManilhaSpadesOrHigherAndTwoOrHigher() {
            TrucoCard vira = TrucoCard.of(QUEEN, SPADES);

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(JACK, SPADES),
                    TrucoCard.of(TWO, HEARTS));

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(QUEEN, SPADES)
            );

            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 3)
                    .botInfo(botCards, 0)
                    .opponentScore(0);

            assertEquals(1, sut.getRaiseResponse(intel.build()));
        }

        @Test
        @DisplayName("Increases 'truco' in the second round if you have a 'manilha' of Spades or higher and a 2 or higher in hand.")
        void TestIncreasesTrucoSecondRoundHaveManilhaSpadesOrHigherAndTwoOrHigher() {
            TrucoCard vira = TrucoCard.of(JACK, CLUBS);

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(KING, CLUBS),
                    TrucoCard.of(TWO, HEARTS));

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(QUEEN, SPADES)
            );

            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 3)
                    .botInfo(botCards, 0)
                    .opponentScore(0);

            assertEquals(1, sut.getRaiseResponse(intel.build()));
        }

        @Test
        @DisplayName("Increase the 'truco' in the third round with a 'manilha'.")
        void TestIncreasesTrucoThirdRoundHaveManilha() {
            TrucoCard vira = TrucoCard.of(FOUR, HEARTS);

            List<TrucoCard> botCards = Collections.singletonList(
                    TrucoCard.of(FIVE, SPADES));

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(FOUR, HEARTS)
            );

            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST, GameIntel.RoundResult.WON), openCards, vira, 3)
                    .botInfo(botCards, 0)
                    .opponentScore(0);

            assertEquals(1, sut.getRaiseResponse(intel.build()));
        }

        @Test
        @DisplayName("Add test to verify the increase of 'truco' in the third round, having won the first round and having a card higher than 3.")
        void TestincreaseTrucoThirdRoundWinningFirstAndCardHigherThanThree() {
            TrucoCard vira = TrucoCard.of(KING, SPADES);

            List<TrucoCard> botCards = Collections.singletonList(
                    TrucoCard.of(THREE, HEARTS));

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(KING, SPADES)
            );

            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON, GameIntel.RoundResult.LOST), openCards, vira, 3)
                    .botInfo(botCards, 0)
                    .opponentScore(0);

            assertEquals(1, sut.getRaiseResponse(intel.build()));
        }

        @Test
        @DisplayName("Should raise to 9 if has 2 manilhas in the first round")
        void ShouldRaiseTo9IfHas2ManilhasInTheFirstRound() {
            TrucoCard vira = TrucoCard.of(ACE, SPADES);

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(TWO, DIAMONDS),
                    TrucoCard.of(TWO, HEARTS),
                    TrucoCard.of(KING, HEARTS));

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(KING, SPADES)
            );

            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 6)
                    .botInfo(botCards, 0)
                    .opponentScore(0);

            assertEquals(1, sut.getRaiseResponse(intel.build()));
        }

        @Test
        @DisplayName("Should raise to 9 if has 1 manilhas higher or equal to spades and a two or higher in the first round")
        void ShouldRaiseTo9IfHas1ManilhaHigherOrEqualToSpadesAndATwoOrHigherInTheFirstRound() {
            TrucoCard vira = TrucoCard.of(KING, SPADES);

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(ACE, SPADES),
                    TrucoCard.of(TWO, HEARTS),
                    TrucoCard.of(KING, HEARTS));

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.KING, SPADES)
            );

            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 6)
                    .botInfo(botCards, 0)
                    .opponentScore(0);

            assertEquals(1, sut.getRaiseResponse(intel.build()));
        }

        @Test
        @DisplayName("Should raise to 9 if has 2 manilhas in the second round")
        void ShouldRaiseTo9IfHas2ManilhasInTheSecondRound() {
            TrucoCard vira = TrucoCard.of(KING, SPADES);

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(ACE, SPADES),
                    TrucoCard.of(ACE, HEARTS)
            );

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.KING, SPADES)
            );

            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 6)
                    .botInfo(botCards, 0)
                    .opponentScore(0);

            assertEquals(1, sut.getRaiseResponse(intel.build()));
        }

        @Test
        @DisplayName("Should raise to 9 if has 1 manilhas higher or equal to spades and a two or higher in the second round")
        void ShouldRaiseTo9IfHas1ManilhaHigherOrEqualToSpadesAndATwoOrHigherInTheSecondRound() {
            TrucoCard vira = TrucoCard.of(KING, SPADES);

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(ACE, SPADES),
                    TrucoCard.of(ACE, HEARTS)
            );

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.KING, SPADES)
            );

            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 6)
                    .botInfo(botCards, 0)
                    .opponentScore(0);

            assertEquals(1, sut.getRaiseResponse(intel.build()));
        }

        @Test
        @DisplayName("Should raise to 9 has won the first round and has a three or higher in the second round")
        void ShouldRaiseTo9HasWonTheFirstRoundAndHasAThreeOrHigher() {
            TrucoCard vira = TrucoCard.of(KING, SPADES);

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(THREE, SPADES),
                    TrucoCard.of(SIX, HEARTS));

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.KING, SPADES)
            );

            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 6)
                    .botInfo(botCards, 0)
                    .opponentScore(0);

            assertEquals(1, sut.getRaiseResponse(intel.build()));
        }

        @Test
        @DisplayName("Should raise to 9 if has manilha in the third round")
        void ShouldRaiseTo9IfHasManilhaInTheThirdRound() {
            TrucoCard vira = TrucoCard.of(KING, SPADES);

            List<TrucoCard> botCards = Collections.singletonList(
                    TrucoCard.of(ACE, SPADES));

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.KING, SPADES)
            );

            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON, GameIntel.RoundResult.LOST), openCards, vira, 6)
                    .botInfo(botCards, 0)
                    .opponentScore(0);

            assertEquals(1, sut.getRaiseResponse(intel.build()));
        }

        @Test
        @DisplayName("Should raise to 9 if has won first round and has a three or higher in the third round")
        void ShouldRaiseTo9IfHasWonFirstRoundAndHasAThreeOrHigherInTheThirdRound() {
            TrucoCard vira = TrucoCard.of(KING, SPADES);

            List<TrucoCard> botCards = Collections.singletonList(
                    TrucoCard.of(THREE, SPADES));

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.KING, SPADES)
            );

            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON, GameIntel.RoundResult.LOST), openCards, vira, 6)
                    .botInfo(botCards, 0)
                    .opponentScore(0);

            assertEquals(1, sut.getRaiseResponse(intel.build()));
        }

        @Test
        @DisplayName("Should raise to 12 if has 2 manilhas in the first round")
        void ShouldRaiseTo12IfHas2ManilhasInTheFirstRound() {
            TrucoCard vira = TrucoCard.of(KING, SPADES);

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(ACE, SPADES),
                    TrucoCard.of(ACE, HEARTS),
                    TrucoCard.of(KING, HEARTS)
            );

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.KING, SPADES)
            );

            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 9)
                    .botInfo(botCards, 0)
                    .opponentScore(0);

            assertEquals(1, sut.getRaiseResponse(intel.build()));
        }


    }

}

