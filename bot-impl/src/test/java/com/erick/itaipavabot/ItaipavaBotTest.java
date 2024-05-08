/*
 *  Copyright (C) 2024 Erick Santinon Gomes - IFSP/SCL
 *  Contact: santinon <dot> gomes <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.erick.itaipavabot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class ItaipavaBotTest {
    private ItaipavaBot bot;
    private GameIntel.StepBuilder stepBuilder;

    @BeforeEach
    public void setUp() {
        bot = new ItaipavaBot();
    }

    @Nested
    @DisplayName("Mão de onze response test")
    class MaoDeOnzeTest {
        @Test
        @DisplayName("Should accept mão de onze if has 3 manilhas")
        void shouldAcceptMaoDeOnzeIfHas3Manilhas() {
            TrucoCard opponentCard = TrucoCard.of(CardRank.THREE, CardSuit.HEARTS);
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.SIX, CardSuit.SPADES),
                    TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.SIX, CardSuit.CLUBS)
            );
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                    .botInfo(myCards, 11)
                    .opponentScore(0)
                    .opponentCard(opponentCard);
            assertTrue(bot.getMaoDeOnzeResponse(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should accept mão de onze if has 1 manilha and one zap")
        void shouldAcceptMaoDeOnzeIfHas1ManilhaAndOneZap() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.SIX, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)
            );
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                    .botInfo(myCards, 11)
                    .opponentScore(0);
            assertTrue(bot.getMaoDeOnzeResponse(stepBuilder.build()));
        }
    }

    @Nested
    @DisplayName("Raise response test")
    class RaiseResponseTest {
        @Test
        @DisplayName("Should accept truco if has 2 manilhas and not zap")
        void shouldAcceptTrucoIfHas2ManilhasAndNotZap() {
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);
            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.QUEEN, HEARTS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.SPADES)
            );
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 3)
                    .botInfo(myCards, 1)
                    .opponentScore(0);
            assertEquals(0, bot.getRaiseResponse(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should raise truco if has casal maior")
        void shouldRaiseTrucoIfHasCasalMaior() {
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);
            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.SPADES)
            );
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 3)
                    .botInfo(myCards, 1)
                    .opponentScore(0);
            assertEquals(1, bot.getRaiseResponse(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should raise truco if has zap and a three")
        void shouldRaiseTrucoIfHasZapAndAThree() {
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);
            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.SPADES)
            );
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 3)
                    .botInfo(myCards, 1)
                    .opponentScore(0);
            assertEquals(1, bot.getRaiseResponse(stepBuilder.build()));
        }
        @Test
        @DisplayName("Should raise truco if has powerlevel greater or equal to 7.5")
        void shouldRaiseTrucoIfHasPowerlevelGreaterOrEqualThan75() {
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);
            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES)
            );
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 3)
                    .botInfo(myCards, 1)
                    .opponentScore(0);
            assertEquals(1, bot.getRaiseResponse(stepBuilder.build()));
        }
        @Test
        @DisplayName("Should decline raise if has powerlevel less or equal to 3.5")
        void shouldDeclineRaiseIfHasPowerlevelLessOrEqualThan35() {
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);
            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES)
            );
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 3)
                    .botInfo(myCards, 1)
                    .opponentScore(0);
            assertEquals(-1, bot.getRaiseResponse(stepBuilder.build()));
        }
        @Test
        @DisplayName("Should decline raise if hand points already at 12")
        void shouldDeclineRaiseIfHandPointsAlreadyAt12() {
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);
            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES)
            );
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 12)
                    .botInfo(myCards, 1)
                    .opponentScore(0);
            assertEquals(-1, bot.getRaiseResponse(stepBuilder.build()));
        }
    }

    @Nested
    @DisplayName("Decide if raises response test")
    class DecideIfRaiseResponseTest {
        @Test
        @DisplayName("Should trucar if has 3 manilhas")
        void shouldTrucarIfHas3Manilhas() {
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);
            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES)
            );
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(0);
            assertTrue(bot.decideIfRaises(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should not raise if opponent has more than 9 points")
        void shouldNotRaiseIfOpponentHasMoreThan7PointsAndHasLessThan9PowerLevel() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)
            );
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(10);
            assertFalse(bot.decideIfRaises(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should raise if has powerlevel more than 7.5")
        void shouldRaiseIfHasPowerlevelMoreThan75() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.SIX, CardSuit.HEARTS)
            );
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(0);
            assertTrue(bot.decideIfRaises(stepBuilder.build()));
        }
        @Test
        @DisplayName("Should raise if has casal maior")
        void shouldRaiseIfHasCasalMaior() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.SIX, CardSuit.CLUBS)
            );
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(10);
            assertTrue(bot.decideIfRaises(stepBuilder.build()));
        }
    }

    @Nested
    @DisplayName("Choose card response test")
    class ChooseCardResponseTest {
        @Test
        @DisplayName("Should win with only the necessary card")
        void shouldWinWithOnlyTheNecessaryCard() {
            TrucoCard opponentCard = TrucoCard.of(CardRank.ACE, CardSuit.HEARTS);
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)
            );
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 1)
                    .botInfo(myCards, 0)
                    .opponentScore(0)
                    .opponentCard(opponentCard);
            assertEquals(TrucoCard.of(TWO, HEARTS), bot.chooseCard(stepBuilder.build()).content());
        }
        @Test
        @DisplayName("Should discard the least valuable card when you cant win")
        void shouldDiscardTheLeastValuableCardWhenYouCantWin() {
            TrucoCard opponentCard = TrucoCard.of(CardRank.SIX, CardSuit.HEARTS);
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS)
            );
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(myCards, 0)
                    .opponentScore(0)
                    .opponentCard(opponentCard);
            assertEquals(TrucoCard.of(SEVEN, CLUBS), bot.chooseCard(stepBuilder.build()).content());
        }
        @Test
        @DisplayName("Should start with highest card")
        void shouldStartWithHighestCard() {
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);
            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES)
            );
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(0);
            assertEquals(TrucoCard.of(QUEEN, SPADES), bot.chooseCard(stepBuilder.build()).content());
        }
        @Test
        @DisplayName("Should play highest card if lost first round")
        void shouldPlayHighestCardIfLostFirstRound() {
            TrucoCard opponentCard = TrucoCard.of(CardRank.ACE, CardSuit.HEARTS);
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);
            List<TrucoCard> openCards = List.of(vira, opponentCard, TrucoCard.of(CardRank.TWO, CardSuit.HEARTS));
            List <TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)
            );
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(myCards, 0)
                    .opponentScore(0)
                    .opponentCard(opponentCard);
            assertEquals(TrucoCard.of(THREE, SPADES), bot.chooseCard(stepBuilder.build()).content());
        }
        @Test
        @DisplayName("Should play lowest card if won first round")
        void shouldPlayLowestCardIfWonFirstRound() {
            TrucoCard opponentCard = TrucoCard.of(CardRank.ACE, CardSuit.HEARTS);
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);
            List<TrucoCard> openCards = List.of(vira, opponentCard, TrucoCard.of(CardRank.TWO, CardSuit.HEARTS));
            List <TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)
            );
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                    .botInfo(myCards, 0)
                    .opponentScore(0)
                    .opponentCard(opponentCard);
            assertEquals(TrucoCard.of(FOUR, CLUBS), bot.chooseCard(stepBuilder.build()).content());
        }
    }

    @Nested
    @DisplayName("HasCard method test")
    class HasCardMethodTest {
        @Test
        @DisplayName("Should return true if has 3 in hand")
        void shouldReturnTrueIfHas3InHand() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
            List<TrucoCard> openCards = List.of(vira);
            List <TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.SIX, CardSuit.HEARTS)
            );
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(0);
            assertTrue(bot.hasCard(stepBuilder.build(), TrucoCard.of(CardRank.THREE, CardSuit.SPADES)));
        }

        @Test
        @DisplayName("Should return true when has zap in hand")
        void shouldReturnTrueWhenHasZapInHand() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
            List<TrucoCard> openCards = List.of(vira);
            List <TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.SIX, CardSuit.CLUBS)
            );
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(0);
            assertTrue(bot.hasCard(stepBuilder.build(), true));
        }
    }

    @Nested
    @DisplayName("Hand power level test")
    class HandPowerLevelTest {
        @Test
        @DisplayName("Should return 12 powerlevel when has zap copas e espada")
        void shouldReturn12PowerLevelWhenHasZapCopasEspada() {
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);
            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES)
            );
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(0);
            assertEquals(12, bot.handPowerLevel(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should return 1 if has 3 fours when vira is five")
        void shouldReturn1IfHas3FoursWhenViraIsFive() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES)
            );
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(0);
            assertEquals(1, bot.handPowerLevel(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should return 9 when have 3 threes on hand")
        void shouldReturn9WhenHasThreeOnHand() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(THREE, CardSuit.CLUBS),
                    TrucoCard.of(THREE, CardSuit.HEARTS),
                    TrucoCard.of(THREE, CardSuit.SPADES)
            );
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(0);
            assertEquals(9, bot.handPowerLevel(stepBuilder.build()));
        }
    }

    @Nested
    @DisplayName("Number of round method test")
    class NumberOfRoundMethodTest {
        @Test
        @DisplayName("Should return number 3 for third round")
        void shouldReturn3ForThirdRound() {
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);
            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES)
            );
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(0);
            assertEquals(3, bot.numberOfRound(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should return number 1 for first round")
        void shouldReturn1ForFirstRound() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES)
            );
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(0);
            assertEquals(1, bot.numberOfRound(stepBuilder.build()));
        }
    }

}
