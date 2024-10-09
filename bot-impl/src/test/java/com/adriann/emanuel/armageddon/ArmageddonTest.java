/*
 *  Copyright (C) 2023 Adriann Paranhos - IFSP/SCL and Emanuel José da Silva - IFSP/SCL
 *  Contact: adriann <dot> paranhos <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: emanuel <dot> silva <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.adriann.emanuel.armageddon;

import com.bueno.spi.model.CardSuit;
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
import static com.bueno.spi.model.GameIntel.RoundResult.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArmageddonTest {

    private Armageddon armageddon;
    private GameIntel.StepBuilder intel;
    private TrucoCard vira;
    private TrucoCard opponentCard;
    private List<TrucoCard> botCards;
    private List<TrucoCard> openCards;

    private GameIntel.StepBuilder maoDeOnze(List<TrucoCard> botCards, TrucoCard vira){
        return GameIntel.StepBuilder.with().gameInfo(List.of(),List.of(),vira,1)
                .botInfo(botCards,11).opponentScore(0);
    }

    private GameIntel.StepBuilder firstRoundFirstToPlay(List<TrucoCard> botCards, TrucoCard vira){
        return GameIntel.StepBuilder.with().gameInfo(List.of(),List.of(),vira,1)
                .botInfo(botCards,1).opponentScore(1);
    }

    private GameIntel.StepBuilder firstRoundSecondToPlay(List<TrucoCard> botCards,
                                                         TrucoCard vira, TrucoCard opponentCard){

        return GameIntel.StepBuilder.with().gameInfo(List.of(),List.of(),vira,1)
                .botInfo(botCards,1).opponentScore(1).opponentCard(opponentCard);
    }

    private GameIntel.StepBuilder secondRoundWonFirstRound(
            List<TrucoCard> botCards, List<TrucoCard> openCards, TrucoCard vira){

        return GameIntel.StepBuilder.with().gameInfo(List.of(WON),openCards,vira,1)
                .botInfo(botCards,0).opponentScore(0);
    }

    private GameIntel.StepBuilder secondRoundLostFirstRound(
            List<TrucoCard> botCards, List<TrucoCard> openCards, TrucoCard vira, TrucoCard opponentCard){

        return GameIntel.StepBuilder.with().gameInfo(List.of(LOST),openCards,vira,1)
                .botInfo(botCards,0).opponentScore(0).opponentCard(opponentCard);
    }

    @BeforeEach
    void setUp(){
        armageddon = new Armageddon();
    }

    @Nested
    @DisplayName("Tests to implement mao de onze logic")
    class MaoDeOnzeTest{
        @Test
        @DisplayName("Should refuse mao de onze when the hand is weak")
        void shouldRefuseMaoDeOnzeHandWeak(){
            vira = TrucoCard.of(KING,DIAMONDS);
            botCards = List.of(
                    TrucoCard.of(FOUR,DIAMONDS),
                    TrucoCard.of(FIVE,HEARTS),
                    TrucoCard.of(SIX,SPADES)
            );
            intel = maoDeOnze(botCards,vira);

            assertThat(armageddon.getMaoDeOnzeResponse(intel.build())).isFalse();
        }

        @Test
        @DisplayName("Should accept mao de onze when the hand is strong")
        void shouldAcceptMaoDeOnzeHandStrong(){
            vira = TrucoCard.of(KING,DIAMONDS);
            botCards = List.of(
                    TrucoCard.of(ACE,DIAMONDS),
                    TrucoCard.of(THREE,SPADES),
                    TrucoCard.of(ACE,CLUBS)
            );
            intel = maoDeOnze(botCards,vira);

            assertThat(armageddon.getMaoDeOnzeResponse(intel.build())).isTrue();
        }

        @Test
        @DisplayName("Should refuse mao de onze when don't have manilha or three")
        void shouldRefuseMaoDeOnzeWithoutThreeOrManilha(){
            vira = TrucoCard.of(FOUR,SPADES);
            botCards = List.of(
                    TrucoCard.of(SEVEN,DIAMONDS),
                    TrucoCard.of(TWO,SPADES),
                    TrucoCard.of(TWO,CLUBS)
            );
            intel = maoDeOnze(botCards,vira);

            assertThat(armageddon.getMaoDeOnzeResponse(intel.build())).isFalse();
        }

        @Test
        @DisplayName("Should accept mao de onze when have two manilhas")
        void shouldAcceptMaoDeOnzeWithTwoManilhas(){
            vira = TrucoCard.of(TWO,CLUBS);
            botCards = List.of(
                    TrucoCard.of(THREE,HEARTS),
                    TrucoCard.of(THREE,SPADES),
                    TrucoCard.of(QUEEN,CLUBS)
            );
            intel = maoDeOnze(botCards,vira);

            assertThat(armageddon.getMaoDeOnzeResponse(intel.build())).isTrue();
        }

        @Test
        @DisplayName("Should accept mao de onze when have manilha and three")
        void shouldAcceptMaoDeOnzeWithManilhaAndThree(){
            vira = TrucoCard.of(JACK,CLUBS);
            botCards = List.of(
                    TrucoCard.of(THREE,HEARTS),
                    TrucoCard.of(KING,SPADES),
                    TrucoCard.of(FIVE,DIAMONDS)
            );
            intel = maoDeOnze(botCards,vira);

            assertThat(armageddon.getMaoDeOnzeResponse(intel.build())).isTrue();
        }

        @Test
        @DisplayName("Should accept mao de onze when the hand contains two and three with three being manilha")
        void shouldAcceptMaoDeOnzeWithTwoAndThreeWhenThreeIsManilha(){
            vira = TrucoCard.of(TWO,CLUBS);
            botCards = List.of(
                    TrucoCard.of(THREE,DIAMONDS),
                    TrucoCard.of(TWO,HEARTS),
                    TrucoCard.of(SEVEN,DIAMONDS)
            );
            intel = maoDeOnze(botCards,vira);

            assertThat(armageddon.getMaoDeOnzeResponse(intel.build())).isTrue();
        }

        @Test
        @DisplayName("should refuse mao de onze when the hand contains only one three")
        void shouldRefuseMaoDeOnzeWithOnlyOneThree(){
            vira = TrucoCard.of(SEVEN,CLUBS);
            botCards = List.of(
                    TrucoCard.of(THREE,DIAMONDS),
                    TrucoCard.of(TWO,HEARTS),
                    TrucoCard.of(SEVEN,DIAMONDS)
            );
            intel = maoDeOnze(botCards,vira);

            assertThat(armageddon.getMaoDeOnzeResponse(intel.build())).isFalse();
        }

        @Test
        @DisplayName("should refuse mao de onze when the hand contains only one manilha")
        void shouldRefuseMaoDeOnzeWithOnlyOneManilha(){
            vira = TrucoCard.of(JACK,SPADES);
            botCards = List.of(
                    TrucoCard.of(KING,SPADES),
                    TrucoCard.of(SIX,HEARTS),
                    TrucoCard.of(QUEEN,DIAMONDS)
            );
            intel = maoDeOnze(botCards,vira);

            assertThat(armageddon.getMaoDeOnzeResponse(intel.build())).isFalse();
        }

        @Test
        @DisplayName("should acept mao de onze with three cards three")
        void shouldAceptMaoDeOnzeWithThreeCardsThree(){
            vira = TrucoCard.of(JACK,SPADES);
            botCards = List.of(
                    TrucoCard.of(THREE,SPADES),
                    TrucoCard.of(THREE,HEARTS),
                    TrucoCard.of(THREE,DIAMONDS)
            );
            intel = maoDeOnze(botCards,vira);

            assertThat(armageddon.getMaoDeOnzeResponse(intel.build())).isTrue();
        }

        @Test
        @DisplayName("should acept mao de onze with Minor Couple")
        void shouldAcceptMaoDeOnzeWithMinorCouple(){
            vira = TrucoCard.of(ACE,SPADES);
            botCards = List.of(
                    TrucoCard.of(TWO,DIAMONDS),
                    TrucoCard.of(TWO,SPADES),
                    TrucoCard.of(SIX,CLUBS)
            );
            intel = maoDeOnze(botCards,vira);

            assertThat(armageddon.getMaoDeOnzeResponse(intel.build())).isTrue();
        }

        @Test
        @DisplayName("should refuse mao de onze with three aces")
        void shouldRefuseMaoDeOnzeWithThreeAces(){
            vira = TrucoCard.of(ACE,SPADES);
            botCards = List.of(
                    TrucoCard.of(ACE,DIAMONDS),
                    TrucoCard.of(ACE,SPADES),
                    TrucoCard.of(ACE,CLUBS)
            );
            intel = maoDeOnze(botCards,vira);

            assertThat(armageddon.getMaoDeOnzeResponse(intel.build())).isFalse();
        }

    }

    @Nested
    @DisplayName("Tests to implement choose card logic")
    class ChooseCardTest{

        @Nested
        @DisplayName("Tests to implement logic of choose card in the first round")
        class FirstRoundChoose{

            @Nested
            @DisplayName("Tests to implement logic of choose card in the first round when is first to play")
            class FirstRoundFirstToPlayChoose{

                @Test
                @DisplayName("Should play the weakest card when have a higher couple")
                void shouldPlayWeakestWithHigherCouple(){
                    vira = TrucoCard.of(JACK,DIAMONDS);
                    botCards = List.of(
                            TrucoCard.of(KING,HEARTS),
                            TrucoCard.of(KING,CLUBS),
                            TrucoCard.of(SIX,DIAMONDS));

                    intel = firstRoundFirstToPlay(botCards,vira);

                    assertThat(armageddon.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(2)));
                }

                @Test
                @DisplayName("Should play the strongest card when the the hand is weak")
                void shouldPlayStrongestCardWeakHand(){
                    vira = TrucoCard.of(ACE,CLUBS);
                    botCards = List.of(
                            TrucoCard.of(FIVE,HEARTS),
                            TrucoCard.of(FOUR,CLUBS),
                            TrucoCard.of(SIX,DIAMONDS));

                    intel = firstRoundFirstToPlay(botCards,vira);

                    assertThat(armageddon.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(2)));
                }

                @Test
                @DisplayName("Should play the strongest card when the hand is average and don't have manilha")
                void shouldPlayStrongestMidHand(){
                    vira = TrucoCard.of(SEVEN,CLUBS);
                    botCards = List.of(
                            TrucoCard.of(SEVEN,HEARTS),
                            TrucoCard.of(KING,CLUBS),
                            TrucoCard.of(TWO,DIAMONDS));

                    intel = firstRoundFirstToPlay(botCards,vira);

                    assertThat(armageddon.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(2)));
                }

                @Test
                @DisplayName("Should play three when have this and manilha at same hand")
                void shouldPlayThreeWhenHaveThreeAndManilha(){
                    vira = TrucoCard.of(QUEEN,DIAMONDS);
                    botCards = List.of(
                            TrucoCard.of(JACK,HEARTS),
                            TrucoCard.of(THREE,CLUBS),
                            TrucoCard.of(SIX,DIAMONDS));

                    intel = firstRoundFirstToPlay(botCards,vira);

                    assertThat(armageddon.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(1)));
                }

                @Test
                @DisplayName("Should play the middle card when have three as manilha")
                void shouldPlayMiddleWhenThreeIsManilha(){
                    vira = TrucoCard.of(TWO,DIAMONDS);
                    botCards = List.of(
                            TrucoCard.of(ACE,SPADES),
                            TrucoCard.of(THREE,CLUBS),
                            TrucoCard.of(FIVE,DIAMONDS));

                    intel = firstRoundFirstToPlay(botCards,vira);

                    assertThat(armageddon.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(0)));
                }

                @Test
                @DisplayName("Should play the weakest manilha when have two")
                void shouldPlayWeakestManilha(){
                    vira = TrucoCard.of(SIX,DIAMONDS);
                    botCards = List.of(
                            TrucoCard.of(SEVEN,DIAMONDS),
                            TrucoCard.of(QUEEN,CLUBS),
                            TrucoCard.of(SEVEN,HEARTS));

                    intel = firstRoundFirstToPlay(botCards,vira);

                    assertThat(armageddon.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(0)));
                }
            }

            @Nested
            @DisplayName("Tests to implement logic of choose card in the first round when is second to play")
            class FirstRoundSecondToPlayChoose{

                @Test
                @DisplayName("Draw the point when have zap")
                void shouldDrawWhenZap(){
                    vira = TrucoCard.of(ACE,DIAMONDS);
                    opponentCard = TrucoCard.of(SEVEN,CLUBS);
                    botCards = List.of(
                            TrucoCard.of(SEVEN,HEARTS),
                            TrucoCard.of(TWO,CLUBS),
                            TrucoCard.of(FOUR,DIAMONDS));

                    intel = firstRoundSecondToPlay(botCards,vira,opponentCard);

                    assertThat(armageddon.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(0)));
                }

                @Test
                @DisplayName("Should play the weakest card when can't beat the opponent card")
                void shouldPlayWeakestCardWhenCantWin(){
                    vira = TrucoCard.of(SEVEN,DIAMONDS);
                    opponentCard = TrucoCard.of(THREE,CLUBS);
                    botCards = List.of(
                            TrucoCard.of(FIVE,HEARTS),
                            TrucoCard.of(TWO,CLUBS),
                            TrucoCard.of(KING,DIAMONDS));

                    intel = firstRoundSecondToPlay(botCards,vira,opponentCard);

                    assertThat(armageddon.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(0)));
                }

                @Test
                @DisplayName("Should play the weakest card possible when can beat the opponent card")
                void shouldPlayWeakestCardWhenCanWin(){
                    vira = TrucoCard.of(FOUR,DIAMONDS);
                    opponentCard = TrucoCard.of(SIX,SPADES);
                    botCards = List.of(
                            TrucoCard.of(SEVEN,HEARTS),
                            TrucoCard.of(TWO,CLUBS),
                            TrucoCard.of(KING,DIAMONDS));

                    intel = firstRoundSecondToPlay(botCards,vira,opponentCard);

                    assertThat(armageddon.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(0)));
                }

                @Test
                @DisplayName("Should play the middle card when can beat and weakest card can't beat the opponent card")
                void shouldPlayMiddleWhenWeakestCardCantWin(){
                    vira = TrucoCard.of(FIVE,DIAMONDS);
                    opponentCard = TrucoCard.of(JACK,SPADES);
                    botCards = List.of(
                            TrucoCard.of(SEVEN,HEARTS),
                            TrucoCard.of(TWO,CLUBS),
                            TrucoCard.of(KING,DIAMONDS));

                    intel = firstRoundSecondToPlay(botCards,vira,opponentCard);

                    assertThat(armageddon.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(2)));
                }

                @Test
                @DisplayName("Should play the weakest card when have high couple")
                void shouldPlayWeakestWhenHaveHighCouple(){
                    vira = TrucoCard.of(SEVEN,DIAMONDS);
                    opponentCard = TrucoCard.of(TWO,CLUBS);
                    botCards = List.of(
                            TrucoCard.of(QUEEN,CLUBS),
                            TrucoCard.of(ACE,HEARTS),
                            TrucoCard.of(QUEEN,HEARTS));

                    intel = firstRoundSecondToPlay(botCards,vira,opponentCard);

                    assertThat(armageddon.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(1)));
                }

                @Test
                @DisplayName("Should draw when possible and has one manilha")
                void shouldDrawWhenHaveOneManilha(){
                    vira = TrucoCard.of(FOUR,DIAMONDS);
                    opponentCard = TrucoCard.of(ACE,SPADES);
                    botCards = List.of(
                            TrucoCard.of(KING,CLUBS),
                            TrucoCard.of(ACE,HEARTS),
                            TrucoCard.of(FIVE,SPADES));

                    intel = firstRoundSecondToPlay(botCards,vira,opponentCard);

                    assertThat(armageddon.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(1)));
                }

                @Test
                @DisplayName("Should play the weakest of two manilhas when can beat and the weakest card can't beat the opponent")
                void shouldPlayWeakestManilhaWhenCanBeatAndHasTwoManilhas(){
                    vira = TrucoCard.of(QUEEN,DIAMONDS);
                    opponentCard = TrucoCard.of(THREE,SPADES);
                    botCards = List.of(
                            TrucoCard.of(THREE,CLUBS),
                            TrucoCard.of(JACK,HEARTS),
                            TrucoCard.of(JACK,SPADES));

                    intel = firstRoundSecondToPlay(botCards,vira,opponentCard);

                    assertThat(armageddon.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(2)));
                }

                @Test
                @DisplayName("Should play the weakest card when opponent plays zap")
                void shouldPlayWeakestCardWhenOpponentPlaysZap(){
                    vira = TrucoCard.of(TWO,DIAMONDS);
                    opponentCard = TrucoCard.of(THREE,CLUBS);
                    botCards = List.of(
                            TrucoCard.of(THREE,DIAMONDS),
                            TrucoCard.of(FIVE,HEARTS),
                            TrucoCard.of(TWO,SPADES));

                    intel = firstRoundSecondToPlay(botCards,vira,opponentCard);

                    assertThat(armageddon.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(1)));
                }
            }
        }

        @Nested
        @DisplayName("Tests to implement logic of choose card in the second round")
        class SecondRoundChoose{

            @Nested
            @DisplayName("Tests to implement logic of choose card in the second round when won the first round")
            class SecondRoundWonFirstRoundChoose{
                @Test
                @DisplayName("Should play the weakest card when has zap")
                void shouldPlayWeakestCardWhenHasZap(){
                    vira = TrucoCard.of(TWO,DIAMONDS);
                    botCards = List.of(
                            TrucoCard.of(THREE,CLUBS),
                            TrucoCard.of(FIVE,HEARTS));
                    openCards = List.of(vira);

                    intel = secondRoundWonFirstRound(botCards,openCards,vira);

                    assertThat(armageddon.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(1)));
                }

                @Test
                @DisplayName("Should play the strongest card when has bad cards")
                void shouldPlayStrongestCardWhenHasBadCards(){
                    vira = TrucoCard.of(SEVEN,DIAMONDS);
                    botCards = List.of(
                            TrucoCard.of(SIX,SPADES),
                            TrucoCard.of(FOUR,CLUBS));
                    openCards = List.of(vira);

                    intel = secondRoundWonFirstRound(botCards,openCards,vira);

                    assertThat(armageddon.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(0)));
                }

                @Test
                @DisplayName("Should discard hearts when has higher couple")
                void shouldDiscardHeartsWhenHigherCouple(){
                    vira = TrucoCard.of(ACE,CLUBS);
                    botCards = List.of(
                            TrucoCard.of(TWO,HEARTS),
                            TrucoCard.of(TWO,CLUBS));
                    openCards = List.of(vira);

                    intel = secondRoundWonFirstRound(botCards,openCards,vira);

                    assertThat(armageddon.chooseCard(intel.build())).isEqualTo(CardToPlay.discard(botCards.get(0)));
                }

                @Test
                @DisplayName("Should play the strongest card when has middle cards")
                void shouldPlayStrongestWhenHasMiddle(){
                    vira = TrucoCard.of(FIVE,CLUBS);
                    botCards = List.of(
                            TrucoCard.of(QUEEN,HEARTS),
                            TrucoCard.of(SEVEN,CLUBS));
                    openCards = List.of(vira);

                    intel = secondRoundWonFirstRound(botCards,openCards,vira);

                    assertThat(armageddon.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(0)));
                }

                @Test
                @DisplayName("Should discard three when has zap")
                void shouldDiscardThreeWhenZap(){
                    vira = TrucoCard.of(JACK,SPADES);
                    botCards = List.of(
                            TrucoCard.of(KING,CLUBS),
                            TrucoCard.of(THREE,DIAMONDS));
                    openCards = List.of(vira);

                    intel = secondRoundWonFirstRound(botCards,openCards,vira);

                    assertThat(armageddon.chooseCard(intel.build())).isEqualTo(CardToPlay.discard(botCards.get(1)));
                }

                @Test
                @DisplayName("Should play the weakest of two manilhas when not having zap")
                void shouldPlayWeakestManilhaWhenNotZap(){
                    vira = TrucoCard.of(ACE,HEARTS);
                    botCards = List.of(
                            TrucoCard.of(TWO,HEARTS),
                            TrucoCard.of(TWO,DIAMONDS));
                    openCards = List.of(vira);

                    intel = secondRoundWonFirstRound(botCards,openCards,vira);

                    assertThat(armageddon.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(1)));
                }

                @Test
                @DisplayName("Should discard weakest manilha when haz zap")
                void shouldDiscardManilhaWhenZap(){
                    vira = TrucoCard.of(FIVE,HEARTS);
                    botCards = List.of(
                            TrucoCard.of(SIX,DIAMONDS),
                            TrucoCard.of(SIX,CLUBS));
                    openCards = List.of(vira);

                    intel = secondRoundWonFirstRound(botCards,openCards,vira);

                    assertThat(armageddon.chooseCard(intel.build())).isEqualTo(CardToPlay.discard(botCards.get(0)));
                }
            }

            @Nested
            @DisplayName("Tests to implement logic of choose card in the second round when lost the first round")
            class SecondRoundLostFirstRoundChoose{

                @Test
                @DisplayName("Should play the weakest card when opponent discard")
                void shouldPlayWeakestOpponentDiscard(){
                    vira = TrucoCard.of(ACE,DIAMONDS);
                    botCards = List.of(
                            TrucoCard.of(SIX,HEARTS),
                            TrucoCard.of(JACK,CLUBS));
                    opponentCard = TrucoCard.closed();
                    openCards = List.of(vira,opponentCard);

                    intel = secondRoundLostFirstRound(botCards,openCards,vira,opponentCard);

                    assertThat(armageddon.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(0)));
                }

                @Test
                @DisplayName("Should play the weakest card when the two cards beat the opponent")
                void shouldPlayWeakestWhenBeatOpponent(){
                    vira = TrucoCard.of(THREE,CLUBS);
                    botCards = List.of(
                            TrucoCard.of(FOUR,SPADES),
                            TrucoCard.of(ACE,DIAMONDS));
                    opponentCard = TrucoCard.of(KING,HEARTS);
                    openCards = List.of(vira,opponentCard);

                    intel = secondRoundLostFirstRound(botCards,openCards,vira,opponentCard);

                    assertThat(armageddon.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(1)));
                }

                @Test
                @DisplayName("Should play the strongest card when the weakest card can't beat the opponent")
                void shouldPlayStrongestWhenBeatOpponent(){
                    vira = TrucoCard.of(ACE,CLUBS);
                    botCards = List.of(
                            TrucoCard.of(ACE,DIAMONDS),
                            TrucoCard.of(SEVEN,SPADES));
                    opponentCard = TrucoCard.of(JACK,HEARTS);
                    openCards = List.of(vira,opponentCard);

                    intel = secondRoundLostFirstRound(botCards,openCards,vira,opponentCard);

                    assertThat(armageddon.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(0)));
                }
            }
        }
    }

    // Comecei aqui ----------------------------------------------------------------------
    @Nested
    @DisplayName("Tests to decide if raises")
    class DecideIfRaise {


        // Fist round --------------------------------------------------------------------------------------------------
        @Nested
        @DisplayName("Tests to implement logic of first round to decideIfRaises")
        class FirstRoundFirstToPlayTest {

            @Test
            @DisplayName("Should raise when the hand contains only one strong card")
            void shouldRaiseWithOneStrongCard() {
                vira = TrucoCard.of(ACE, HEARTS);
                botCards = List.of(
                        TrucoCard.of(FIVE, DIAMONDS),
                        TrucoCard.of(TWO, SPADES),
                        TrucoCard.of(FOUR, CLUBS)
                );


                intel = firstRoundFirstToPlay(botCards, vira);

                assertThat(armageddon.decideIfRaises(intel.build())).isTrue();
            }

            @Test
            @DisplayName("Should raise when the hand contains two strong cards: one manilha and one three")
            void shouldRaiseWithTwoStrongCards() {
                TrucoCard vira = TrucoCard.of(ACE, HEARTS);
                TrucoCard opponentCard = TrucoCard.of(TWO, HEARTS);
                List<TrucoCard> botCards = List.of(
                        TrucoCard.of(THREE, DIAMONDS),
                        TrucoCard.of(TWO, SPADES),
                        TrucoCard.of(ACE, SPADES)
                );

                List<TrucoCard> openCards = List.of(opponentCard);

                GameIntel intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 2)
                        .botInfo(botCards, 0)
                        .opponentScore(0)
                        .build();
                assertThat(armageddon.hasManilhaAndThree(botCards, vira)).isTrue();
            }

            @Test
            @DisplayName("Should ask for truco with one manilha in the first round")
            void shouldAskTrucoWithOneManilhaFirstRound() {
                TrucoCard vira = TrucoCard.of(ACE, HEARTS);
                List<TrucoCard> botCards = List.of(
                        TrucoCard.of(ACE, SPADES),
                        TrucoCard.of(TWO, SPADES),
                        TrucoCard.of(FIVE, CLUBS)
                );

                GameIntel intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), List.of(), vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0)
                        .build();

                assertThat(armageddon.decideIfRaises(intel)).isTrue();
            }

            @Test
            @DisplayName("Should ask for truco with two manilhas in the first round")
            void shouldAskTrucoWithTwoManilhasFirstRound() {
                TrucoCard vira = TrucoCard.of(SIX, DIAMONDS);
                List<TrucoCard> botCards = List.of(
                        TrucoCard.of(SEVEN, SPADES),
                        TrucoCard.of(SEVEN, CLUBS),
                        TrucoCard.of(TWO, HEARTS)
                );

                GameIntel intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), List.of(), vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0)
                        .build();

                assertThat(armageddon.decideIfRaises(intel)).isTrue();
            }

            @Test
            @DisplayName("choose the strongest card without seeing the opponent's card")
            void chooseTheStrongestCardWithoutSeeingTheOpponentsCard() {
                TrucoCard vira = TrucoCard.of(TWO, HEARTS);

                List<TrucoCard> botCards = List.of(
                        TrucoCard.of(THREE, SPADES),
                        TrucoCard.of(FOUR, CLUBS),
                        TrucoCard.of(FIVE, DIAMONDS)
                );

                List<TrucoCard> openCards = List.of();

                GameIntel intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), openCards, vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0)
                        .build();

                TrucoCard playedCard = armageddon.playBestCard(intel);

                assertThat(playedCard).isEqualTo(TrucoCard.of(THREE, SPADES));
            }

        }

        // First round second to play ---------------------------------------------------------------------------------
        @Nested
        @DisplayName("Tests to implement logic of first round second to play to decideIfRaises")
        class FirstRoundSecondToPlayTest {


            @Test
            @DisplayName("Choose Strongest Card Agains Opponent")
            void chooseStrongestCardAgainstOpponent() {
                TrucoCard vira = TrucoCard.of(TWO, HEARTS);

                List<TrucoCard> botCards = List.of(
                        TrucoCard.of(THREE, SPADES),
                        TrucoCard.of(FOUR, CLUBS),
                        TrucoCard.of(FIVE, DIAMONDS)
                );

                TrucoCard opponentCard = TrucoCard.of(TWO, SPADES);
                List<TrucoCard> openCards = List.of(opponentCard);

                GameIntel intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), openCards, vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0)
                        .build();

                TrucoCard playedCard = armageddon.playBestCard(intel);

                assertThat(playedCard).isEqualTo(TrucoCard.of(THREE, SPADES));
            }


        }

        // Second round won first round  ------------------------------------------------------------------------------
        @Nested
        @DisplayName("Tests to implement logic of second round won first round to decideIfRaises")
        class SecondRoundWonFirstRoundTest {

            @Test
            @DisplayName("Should call truco in the second round after winning the first round")
            void shouldCallTrucoInSecondRoundAfterWinningFirst() {
                TrucoCard vira = TrucoCard.of(ACE, HEARTS);

                List<TrucoCard> botCards = List.of(
                        TrucoCard.of(THREE, DIAMONDS),
                        TrucoCard.of(TWO, SPADES)
                );

                List<TrucoCard> openCards = List.of(
                        TrucoCard.of(FOUR, CLUBS),
                        TrucoCard.of(FIVE, HEARTS)
                );

                GameIntel intel = secondRoundWonFirstRound(botCards, openCards, vira).build();

                boolean shouldCallTruco = armageddon.shouldRequestTruco(intel);

                assertThat(shouldCallTruco).isTrue();
            }

            @Test
            @DisplayName("Should raise when the hand contains two strong cards after losing the first round")
            void shouldRaiseWithTwoStrongCardsAfterLosingFirstRound() {
                vira = TrucoCard.of(JACK, HEARTS);
                opponentCard = TrucoCard.of(THREE, SPADES);
                botCards = List.of(
                        TrucoCard.of(ACE, DIAMONDS),
                        TrucoCard.of(KING, CLUBS),
                        TrucoCard.of(TWO, HEARTS)
                );
                intel = secondRoundLostFirstRound(botCards, List.of(), vira, opponentCard);

                assertThat(armageddon.decideIfRaises(intel.build())).isTrue();
            }
        }


        @Nested
        @DisplayName("Tests to implement logic of second round drew first round")
        class SecondRoundDrewFirstRound {

            @Test
            @DisplayName("Should request truco if I drew the first round")
            void shouldRequestTrucoIfIDrewTheFirstRound() {
                TrucoCard vira = TrucoCard.of(THREE, HEARTS);

                List<TrucoCard> botCards = List.of(
                        TrucoCard.of(THREE, SPADES),
                        TrucoCard.of(SEVEN, SPADES)
                );

                TrucoCard opponentCard = TrucoCard.of(THREE, DIAMONDS);
                List<TrucoCard> openCards = List.of(opponentCard);

                GameIntel intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(DREW), openCards, vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0)
                        .build();

                boolean result = armageddon.shouldRequestTruco(intel);

                assertTrue(result);
            }
        }
    }

    @Nested
    @DisplayName("Tests to decide if not raises")
    class DecideIfNotRaises {

        // Fist round --------------------------------------------------------------------------------------------------
        @Nested
        @DisplayName("Tests to implement logic of first round to decideIfNotRaises")
        class FirstRound{


            @Test
            @DisplayName("Should refuse when all three cards are weak")
            void shouldRefuseWhenAllThreeCardsAreWeak() {
                TrucoCard vira = TrucoCard.of(FOUR, DIAMONDS);
                TrucoCard opponentCard = TrucoCard.of(FIVE, CLUBS);
                List<TrucoCard> botCards = List.of(
                        TrucoCard.of(SEVEN, DIAMONDS),
                        TrucoCard.of(SEVEN, HEARTS),
                        TrucoCard.of(SIX, SPADES)
                );

                List<TrucoCard> openCards = List.of(opponentCard);

                GameIntel.StepBuilder intel = firstRoundSecondToPlay(botCards, vira, opponentCard);

                int response = armageddon.getRaiseResponse(intel.build());

                assertThat(response).isEqualTo(-1);
            }

            @Test
            @DisplayName("should accept when have two manilhas")
            void shouldAceptWhenHaveTwoManilhas() {
                TrucoCard vira = TrucoCard.of(FIVE, DIAMONDS);
                TrucoCard opponentCard = TrucoCard.of(SIX, CLUBS);

                List<TrucoCard> botCards = List.of(
                        TrucoCard.of(SIX, DIAMONDS),
                        TrucoCard.of(SEVEN, HEARTS),
                        TrucoCard.of(SIX, SPADES)
                );

                List<TrucoCard> openCards = List.of(opponentCard);

                GameIntel.StepBuilder intel = firstRoundSecondToPlay(botCards, vira, opponentCard);

                int response = armageddon.getRaiseResponse(intel.build());

                assertThat(response).isEqualTo(1);

            }

            @Test
            @DisplayName("should accept when have has higher couple")
            void shouldAceptWhenHaveHasHigherCouple() {
                TrucoCard vira = TrucoCard.of(JACK, CLUBS);
                TrucoCard opponentCard = TrucoCard.of(QUEEN, DIAMONDS);

                List<TrucoCard> botCards = List.of(
                        TrucoCard.of(KING, CLUBS),
                        TrucoCard.of(KING, HEARTS),
                        TrucoCard.of(SIX, SPADES)
                );

                List<TrucoCard> openCards = List.of(opponentCard);

                intel = firstRoundSecondToPlay(botCards, vira, opponentCard);

                int response = armageddon.getRaiseResponse(intel.build());

                assertThat(response).isEqualTo(1);

            }

        }

        // Second round won first round --------------------------------------------------------------------------------
        @Nested
        @DisplayName("Tests to implement logic of second round won first round to decideIfNotRaises")
        class secondRoundWonFirstRoundTests { }


        @Nested
        @DisplayName("Tests to implement logic of second round lost first round to decideIfNotRaises")
        class secondRoundLostFirstRoundTest{

            @Test
            @DisplayName("Should refuse when all two cards are weak")
            void shouldRefuseWhenAllTwoCardsAreWeak() {
                TrucoCard vira = TrucoCard.of(FOUR, DIAMONDS);
                TrucoCard opponentCard = TrucoCard.of(FIVE, CLUBS);
                List<TrucoCard> botCards = List.of(
                        TrucoCard.of(SEVEN, DIAMONDS),
                        TrucoCard.of(SEVEN, HEARTS)
                );

                List<TrucoCard> openCards = List.of(opponentCard);

                GameIntel.StepBuilder intel = secondRoundWonFirstRound(botCards, openCards, vira);

                int response = armageddon.getRaiseResponse(intel.build());

                assertThat(response).isEqualTo(-1);
            }

            @Test
            @DisplayName("Choose the weakest card when opponents card is stronger")
            void chooseTheWeakestCardWhenOpponentsCardIsStronger() {
                TrucoCard vira = TrucoCard.of(FIVE, HEARTS);

                List<TrucoCard> botCards = List.of(
                        TrucoCard.of(SEVEN, CLUBS),
                        TrucoCard.of(SIX, DIAMONDS)
                );

                TrucoCard opponentCard = TrucoCard.of(SIX, SPADES);
                List<TrucoCard> openCards = List.of(opponentCard);

                GameIntel intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), openCards, vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0)
                        .build();

                TrucoCard playedCard = armageddon.playBestCard(intel);

                assertThat(playedCard).isEqualTo(TrucoCard.of(SIX, DIAMONDS));
            }

        }
    }
}

