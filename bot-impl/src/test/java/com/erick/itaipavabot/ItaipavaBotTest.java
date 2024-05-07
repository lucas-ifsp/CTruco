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

    @Test
    @DisplayName("Should win with only the necessary card")
    void shouldWinWithOnlyTheNecessaryCard() {
        TrucoCard opponentCard = TrucoCard.of(CardRank.ACE, CardSuit.HEARTS);
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
        List<TrucoCard> openCards = List.of(vira);
        List <TrucoCard> myCards = Arrays.asList(
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
        List <TrucoCard> myCards = Arrays.asList(
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
    @DisplayName("Should accept mão de onze if has 3 manilhas")
    void shouldAcceptMaoDeOnzeIfHas3Manilhas() {
        TrucoCard opponentCard = TrucoCard.of(CardRank.THREE, CardSuit.HEARTS);
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
        List<TrucoCard> openCards = List.of(vira);
        List <TrucoCard> myCards = Arrays.asList(
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
        List <TrucoCard> myCards = Arrays.asList(
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

    @Test
    @DisplayName("Should trucar if has 3 manilhas")
    void shouldTrucarIfHas3Manilhas() {
        TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);
        List<TrucoCard> openCards = List.of(vira);
        List <TrucoCard> myCards = Arrays.asList(
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
    @DisplayName("Should accept truco if has 2 manilhas")
    void shouldAcceptTrucoIfHas2Manilhas() {
        TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);
        List<TrucoCard> openCards = List.of(vira);
        List <TrucoCard> myCards = Arrays.asList(
                TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS),
                TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.ACE, CardSuit.SPADES)
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
        List <TrucoCard> myCards = Arrays.asList(
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
    @DisplayName("Should accept truco if has zap and a three")
    void shouldAcceptTrucoIfHasZapAndAThree() {
        TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);
        List<TrucoCard> openCards = List.of(vira);
        List <TrucoCard> myCards = Arrays.asList(
                TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS),
                TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.FIVE, CardSuit.SPADES)
        );
        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 3)
                .botInfo(myCards, 1)
                .opponentScore(0);
        assertEquals(0, bot.getRaiseResponse(stepBuilder.build()));
    }

    @Test
    @DisplayName("Should start with manilha")
    void shouldStartWithManilha() {
        TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);
        List<TrucoCard> openCards = List.of(vira);
        List <TrucoCard> myCards = Arrays.asList(
                TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.FIVE, CardSuit.SPADES)
        );
        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 3)
                .botInfo(myCards, 1)
                .opponentScore(0);
        assertEquals(TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES), bot.chooseCard(stepBuilder.build()).content());
    }

    @Test
    @DisplayName("Should return 12 powerlevel when has zap copas e espada")
    void shouldReturn12PowerLevelWhenHasZapCopasEspada() {
        TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);
        List<TrucoCard> openCards = List.of(vira);
        List <TrucoCard> myCards = Arrays.asList(
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
        List <TrucoCard> myCards = Arrays.asList(
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
    @DisplayName("Should raise if has powerlevel more than 9")
    void shouldRaiseIfHasPowerlevelMoreThan9() {
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
        List<TrucoCard> openCards = List.of(vira);
        List <TrucoCard> myCards = Arrays.asList(
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
