/*
 *  Copyright (C) 2022 Yuri Soares Menon
 *  Contact: y <dot> menon <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.local.yuri.impl;

import com.bueno.spi.model.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;
import static org.junit.jupiter.api.Assertions.*;

public class BotMadeInDescalvadoTest {

    // CardScore High => THREE TWO TWO => score >= 9.0
    // CardScore Enough => TWO TWO ACE => score >= 8.5

    // OpponentScore High => score >= 8

    // HandPoints High => points >= 6

    @Test
    @DisplayName("\"getRaiseResponse\" should re-raise when card score is high and opponent has low score")
    void getRaiseResponse_ShouldReRaise_WhenCardScoreIsHigh_AndOpponentHasLowScore() {
        BotMadeInDescalvado bot = new BotMadeInDescalvado();

        GameIntel intel = MockRound
            .vira(FOUR, CLUBS)
            .giveScoreB(7)
            .giveA(THREE, CLUBS) // 10
            .giveA(TWO, HEARTS) // 9
            .giveA(TWO, SPADES) // 9
            .giveB(FIVE, CLUBS)
            .giveB(FIVE, HEARTS)
            .giveB(FIVE, SPADES)
            .build();

        assertEquals(1, bot.getRaiseResponse(intel));
    }

    @Test
    @DisplayName("\"getRaiseResponse\" should re-raise when card score is enough and opponent has low score")
    void getRaiseResponse_ShouldReRaise_WhenCardScoreIsEnough_AndOpponentHasLowScore() {
        BotMadeInDescalvado bot = new BotMadeInDescalvado();

        GameIntel intel = MockRound
            .vira(FOUR, CLUBS)
            .giveScoreB(7)
            .giveA(TWO, CLUBS) // 9
            .giveA(TWO, HEARTS) // 9
            .giveA(ACE, SPADES) // 8
            .giveB(FIVE, CLUBS)
            .giveB(FIVE, HEARTS)
            .giveB(FIVE, SPADES)
            .build();

        assertEquals(1, bot.getRaiseResponse(intel));
    }

    @Test
    @DisplayName("\"getRaiseResponse\" should accept when card score is high and opponent has high score")
    void getRaiseResponse_ShouldAccept_WhenCardScoreIsHigh_AndOpponentHasHighScore() {
        BotMadeInDescalvado bot = new BotMadeInDescalvado();

        GameIntel intel = MockRound
            .vira(FOUR, CLUBS)
            .giveScoreB(8)
            .giveA(THREE, CLUBS) // 10
            .giveA(TWO, HEARTS) // 9
            .giveA(TWO, SPADES) // 9
            .giveB(FIVE, CLUBS)
            .giveB(FIVE, HEARTS)
            .giveB(FIVE, SPADES)
            .build();

        assertEquals(0, bot.getRaiseResponse(intel));
    }

    @Test
    @DisplayName("\"getRaiseResponse\" should accept when card score is enough and opponent has low score")
    void getRaiseResponse_ShouldAccept_WhenCardScoreIsEnough_AndOpponentHasLowScore_AndHandPointsIsHigh() {
        BotMadeInDescalvado bot = new BotMadeInDescalvado();

        GameIntel intel = MockRound
            .vira(FOUR, CLUBS)
            .hand(6)
            .giveScoreB(7)
            .giveA(TWO, CLUBS) // 9
            .giveA(TWO, HEARTS) // 9
            .giveA(ACE, SPADES) // 8
            .giveB(FIVE, CLUBS)
            .giveB(FIVE, HEARTS)
            .giveB(FIVE, SPADES)
            .build();

        assertEquals(0, bot.getRaiseResponse(intel));
    }

    @Test
    @DisplayName("\"getRaiseResponse\" should quit when card score is low")
    void getRaiseResponse_ShouldQuit_WhenCardScoreIsLow() {
        BotMadeInDescalvado bot = new BotMadeInDescalvado();

        GameIntel intel = MockRound
            .vira(FOUR, CLUBS)
            .giveA(KING, CLUBS) // 7
            .giveA(KING, HEARTS) // 7
            .giveA(KING, SPADES) // 7
            .giveB(FIVE, CLUBS)
            .giveB(FIVE, HEARTS)
            .giveB(FIVE, SPADES)
            .build();

        assertEquals(-1, bot.getRaiseResponse(intel));
    }

    @Test
    @DisplayName("getMaoDeOnzeResponse should return true when score is enough")
    void getMaoDeOnzeResponse_ShouldReturnTrue_WhenOpponentScoreIsHigh_AndCardScoreIsHigh() {
        BotMadeInDescalvado bot = new BotMadeInDescalvado();

        GameIntel intel = MockRound
            .vira(FOUR, CLUBS)
            .giveScoreB(8)
            .giveA(THREE, CLUBS) // 10
            .giveA(TWO, HEARTS) // 9
            .giveA(TWO, SPADES) // 9
            .giveB(FIVE, CLUBS)
            .giveB(FIVE, HEARTS)
            .giveB(FIVE, SPADES)
            .build();

        assertTrue(bot.getMaoDeOnzeResponse(intel));
    }

    @Test
    @DisplayName("getMaoDeOnzeResponse should return true when score is enough")
    void getMaoDeOnzeResponse_ShouldReturnTrue_WhenOpponentScoreIsHigh_AndCardScoreIsLow() {
        BotMadeInDescalvado bot = new BotMadeInDescalvado();

        GameIntel intel = MockRound
            .vira(FOUR, CLUBS)
            .giveScoreB(8)
            .giveA(KING, CLUBS) // 7
            .giveA(KING, HEARTS) // 7
            .giveA(KING, SPADES) // 7
            .giveB(FIVE, CLUBS)
            .giveB(FIVE, HEARTS)
            .giveB(FIVE, SPADES)
            .build();

        assertTrue(bot.getMaoDeOnzeResponse(intel));
    }

    @Test
    @DisplayName("getMaoDeOnzeResponse should return false when score is low")
    void getMaoDeOnzeResponse_ShouldReturnTrue_WhenOpponentScoreIsLow_AndCardScoreIsEnough() {
        BotMadeInDescalvado bot = new BotMadeInDescalvado();

        GameIntel intel = MockRound
            .vira(FOUR, CLUBS)
            .giveScoreB(7)
            .giveA(THREE, CLUBS) // 10
            .giveA(TWO, HEARTS) // 9
            .giveA(TWO, SPADES) // 9
            .giveB(FIVE, CLUBS)
            .giveB(FIVE, HEARTS)
            .giveB(FIVE, SPADES)
            .build();

        assertTrue(bot.getMaoDeOnzeResponse(intel));
    }

    @Test
    @DisplayName("getMaoDeOnzeResponse should return false when score is low")
    void getMaoDeOnzeResponse_ShouldReturnFalse_WhenOpponentScoreIsLow_AndCardScoreIsLow() {
        BotMadeInDescalvado bot = new BotMadeInDescalvado();

        GameIntel intel = MockRound
            .vira(FOUR, CLUBS)
            .giveScoreB(7)
            .giveA(KING, CLUBS) // 7
            .giveA(KING, HEARTS) // 7
            .giveA(KING, SPADES) // 7
            .giveB(FIVE, CLUBS)
            .giveB(FIVE, HEARTS)
            .giveB(FIVE, SPADES)
            .build();

        assertFalse(bot.getMaoDeOnzeResponse(intel));
    }

    @Test
    @DisplayName("\"decideIfRaises\" should raise when it is the first round if the card score is enough")
    void decideIfRaises_ShouldRaise_WhenFirstRound_IfCardScoreIsEnough() {
        BotMadeInDescalvado bot = new BotMadeInDescalvado();

        GameIntel intel = MockRound
            .vira(FOUR, CLUBS)
            .giveA(TWO, CLUBS) // 9
            .giveA(TWO, HEARTS) // 9
            .giveA(ACE, SPADES) // 8
            .giveB(FIVE, CLUBS)
            .giveB(FIVE, HEARTS)
            .giveB(FIVE, SPADES)
            .build();

        assertTrue(bot.decideIfRaises(intel));
    }

    @Test
    @DisplayName("\"decideIfRaises\" should not raise when it is the first round if the card score is low")
    void decideIfRaises_ShouldNotRaise_WhenFirstRound_IfCardScoreIsLow() {
        BotMadeInDescalvado bot = new BotMadeInDescalvado();

        GameIntel intel = MockRound
            .vira(FOUR, CLUBS)
            .giveA(KING, CLUBS) // 7
            .giveA(KING, HEARTS) // 7
            .giveA(KING, SPADES) // 7
            .giveB(FIVE, CLUBS)
            .giveB(FIVE, HEARTS)
            .giveB(FIVE, SPADES)
            .build();

        assertFalse(bot.decideIfRaises(intel));
    }

    @Test
    @DisplayName("\"decideIfRaises\" should raise when it is the second round if it won the first round and the card score is enough")
    void decideIfRaises_ShouldRaise_WhenSecondRound_IfWonFirst_AndCardScoreIsEnough() {
        BotMadeInDescalvado bot = new BotMadeInDescalvado();

        GameIntel intel = MockRound
            .vira(FOUR, CLUBS)
            // First Round
            .giveA(TWO, CLUBS).play()
            .giveB(SIX, CLUBS).play()
            //
            .giveA(TWO, HEARTS) // 9
            .giveA(ACE, CLUBS) // 8
            .giveB(FIVE, HEARTS)
            .giveB(FIVE, SPADES)
            .build();

        assertTrue(bot.decideIfRaises(intel));
    }

    @Test
    @DisplayName("\"decideIfRaises\" should raise when it is the second round if it lost the first round and the card score is enough")
    void decideIfRaises_ShouldRaise_WhenSecondRound_IfLostFirst_AndCardScoreIsEnough() {
        BotMadeInDescalvado bot = new BotMadeInDescalvado();

        GameIntel intel = MockRound
            .vira(FOUR, CLUBS)
            // First Round
            .giveA(SIX, CLUBS).play()
            .giveB(TWO, CLUBS).play()
            //
            .giveA(TWO, HEARTS) // 9
            .giveA(ACE, CLUBS) // 8
            .giveB(FIVE, HEARTS)
            .giveB(FIVE, SPADES)
            .build();

        assertTrue(bot.decideIfRaises(intel));
    }

    @Test
    @DisplayName("\"decideIfRaises\" should not raise when it is the second round if it won the first round and the card score is low")
    void decideIfRaises_ShouldNotRaise_WhenSecondRound_IfWonFirst_AndScoreIsLow() {
        BotMadeInDescalvado bot = new BotMadeInDescalvado();

        GameIntel intel = MockRound
            .vira(FOUR, CLUBS)
            // First Round
            .giveA(TWO, CLUBS).play()
            .giveB(SIX, CLUBS).play()
            //
            .giveA(ACE, HEARTS) // 8
            .giveA(ACE, CLUBS) // 8
            .giveB(FIVE, HEARTS)
            .giveB(FIVE, SPADES)
            .build();

        assertFalse(bot.decideIfRaises(intel));
    }

    @Test
    @DisplayName("\"decideIfRaises\" should not raise when it is the second round if it lost the first round and the card score is low")
    void decideIfRaises_ShouldNotRaise_WhenSecondRound_IfLostFirst_AndScoreIsLow() {
        BotMadeInDescalvado bot = new BotMadeInDescalvado();

        GameIntel intel = MockRound
            .vira(FOUR, CLUBS)
            // First Round
            .giveA(SIX, CLUBS).play()
            .giveB(TWO, CLUBS).play()
            //
            .giveA(ACE, HEARTS) // 8
            .giveA(ACE, CLUBS) // 8
            .giveB(FIVE, HEARTS)
            .giveB(FIVE, SPADES)
            .build();

        assertFalse(bot.decideIfRaises(intel));
    }

    @Test
    @DisplayName("\"decideIfRaises\" should raise when it is the third round if the card score is enough")
    void decideIfRaises_ShouldRaise_WhenThirdRound_IfScoreIsEnough() {
        BotMadeInDescalvado bot = new BotMadeInDescalvado();

        GameIntel intel = MockRound
            .vira(FOUR, CLUBS)
            // First Round
            .giveA(TWO, CLUBS).play()
            .giveB(SIX, CLUBS).play()
            // Second Round
            .giveB(TWO, HEARTS).play()
            .giveA(SIX, HEARTS).play()
            //
            .giveA(TWO, CLUBS) // 9
            .giveB(FIVE, SPADES)
            .build();

        assertTrue(bot.decideIfRaises(intel));
    }

    @Test
    @DisplayName("\"decideIfRaises\" should not raise when it is the third round if the card score is low")
    void decideIfRaises_ShouldNotRaise_WhenThirdRound_IfScoreIsLow() {
        BotMadeInDescalvado bot = new BotMadeInDescalvado();

        GameIntel intel = MockRound
            .vira(FOUR, CLUBS)
            // First Round
            .giveA(TWO, CLUBS).play()
            .giveB(SIX, CLUBS).play()
            // Second Round
            .giveB(TWO, HEARTS).play()
            .giveA(SIX, HEARTS).play()
            //
            .giveA(ACE, CLUBS) // 8
            .giveB(FIVE, SPADES)
            .build();

        assertFalse(bot.decideIfRaises(intel));
    }

    @Test
    @DisplayName("\"chooseCard\" should discard weakest when it is the first round and it plays first and it has at least two manilhas")
    void chooseCard_ShouldDiscardWeakest_WhenFirstRound_AndPlaysFirst_AndHasAtLeastTwoManilhas() {
        BotMadeInDescalvado bot = new BotMadeInDescalvado();

        GameIntel intel = MockRound
            .vira(THREE, CLUBS)
            .giveA(FOUR, CLUBS) // Manilha
            .giveA(FOUR, HEARTS) // Manilha
            .giveA(FIVE, CLUBS)
            .giveB(SIX, CLUBS)
            .giveB(SIX, HEARTS)
            .giveB(SIX, SPADES)
            .build();

        CardToPlay playedCard = bot.chooseCard(intel);

        assertEquals(TrucoCard.of(FIVE, CLUBS), playedCard.content());
        assertTrue(playedCard.isDiscard());
    }

    @Test
    @DisplayName("\"chooseCard\" should play strongest when it is the first round and it plays first and it has less then two manilhas")
    void chooseCard_ShouldPlayStrongest_WhenFirstRound_AndPlaysFirst_AndHasLessThenTwoManilhas() {
        BotMadeInDescalvado bot = new BotMadeInDescalvado();

        GameIntel intel = MockRound
            .vira(THREE, CLUBS)
            .giveA(FOUR, CLUBS) // Manilha
            .giveA(FIVE, CLUBS)
            .giveA(FIVE, HEARTS)
            .giveB(SIX, CLUBS)
            .giveB(SIX, HEARTS)
            .giveB(SIX, SPADES)
            .build();

        CardToPlay playedCard = bot.chooseCard(intel);

        assertEquals(TrucoCard.of(FOUR, CLUBS), playedCard.content());
        assertFalse(playedCard.isDiscard());
    }

    @Test
    @DisplayName("\"chooseCard\" should play weakest that wins when it is the first round and it plays second")
    void chooseCard_ShouldPlayWeakest_ThatWins_WhenFirstRound_AndPlaysSecond() {
        BotMadeInDescalvado bot = new BotMadeInDescalvado();

        GameIntel intel = MockRound
            .vira(FOUR, CLUBS)
            .giveA(THREE, CLUBS)
            .giveA(TWO, CLUBS)
            .giveA(ACE, CLUBS)
            .giveB(ACE, HEARTS).play()
            .giveB(ACE, SPADES)
            .giveB(ACE, DIAMONDS)
            .build();

        CardToPlay playedCard = bot.chooseCard(intel);

        assertEquals(TrucoCard.of(TWO, CLUBS), playedCard.content());
        assertFalse(playedCard.isDiscard());
    }

    @Test
    @DisplayName("\"chooseCard\" should play weakest that draws when it is impossible to win and it is the first round and it plays second")
    void chooseCard_ShouldPlayWeakest_ThatDraws_WhenImpossibleToWin_AndFirstRound_AndPlaysSecond() {
        BotMadeInDescalvado bot = new BotMadeInDescalvado();

        GameIntel intel = MockRound
            .vira(THREE, CLUBS)
            .giveA(TWO, CLUBS)
            .giveA(ACE, CLUBS)
            .giveA(KING, CLUBS)
            .giveB(TWO, SPADES).play()
            .giveB(ACE, SPADES)
            .giveB(ACE, DIAMONDS)
            .build();

        CardToPlay playedCard = bot.chooseCard(intel);

        assertEquals(TrucoCard.of(TWO, CLUBS), playedCard.content());
        assertFalse(playedCard.isDiscard());
    }

    @Test
    @DisplayName("\"chooseCard\" should play weakest when it is impossible to win or draw and it is the first round and it plays second")
    void chooseCard_ShouldPlayWeakest_WhenImpossibleToWinOrDraw_AndFirstRound_AndPlaysSecond() {
        BotMadeInDescalvado bot = new BotMadeInDescalvado();

        GameIntel intel = MockRound
            .vira(FOUR, CLUBS)
            .giveA(KING, CLUBS)
            .giveA(JACK, CLUBS)
            .giveA(QUEEN, CLUBS)
            .giveB(THREE, CLUBS)
            .giveB(TWO, CLUBS)
            .giveB(ACE, CLUBS).play()
            .build();

        CardToPlay playedCard = bot.chooseCard(intel);

        assertEquals(TrucoCard.of(QUEEN, CLUBS), playedCard.content());
        assertFalse(playedCard.isDiscard());
    }

    @Test
    @DisplayName("\"chooseCard\" should discard weakest when it won the first round and plays first")
    void chooseCard_ShouldDircardWeakest_WhenWonFirstRound_AndPlaysFirst() {
        BotMadeInDescalvado bot = new BotMadeInDescalvado();

        GameIntel intel = MockRound
            .vira(THREE, CLUBS)
            // First Round
            .giveB(FIVE, CLUBS).play()
            .giveA(FOUR, CLUBS).play() // Manilha
            //
            .giveA(SIX, CLUBS)
            .giveA(FIVE, HEARTS)
            .giveB(SEVEN, CLUBS)
            .giveB(SEVEN, HEARTS)
            .build();

        CardToPlay playedCard = bot.chooseCard(intel);

        assertEquals(TrucoCard.of(FIVE, HEARTS), playedCard.content());
        assertTrue(playedCard.isDiscard());
    }

    @Test
    @DisplayName("\"chooseCard\" should play strongest when it won the first round and plays second")
    void chooseCard_ShouldPlayStrongest_WhenWonFirstRound_AndPlaysSecond() {
        BotMadeInDescalvado bot = new BotMadeInDescalvado();

        GameIntel intel = MockRound
            .vira(THREE, CLUBS)
            // First Round
            .giveA(FOUR, CLUBS).play() // Manilha
            .giveB(FIVE, CLUBS).play()
            // Second Round
            .giveB(SIX, CLUBS).play()
            //
            .giveA(FOUR, HEARTS) // Manilha
            .giveA(ACE, CLUBS)
            .giveB(ACE, HEARTS)
            .build();

        CardToPlay playedCard = bot.chooseCard(intel);

        assertEquals(TrucoCard.of(FOUR, HEARTS), playedCard.content());
        assertFalse(playedCard.isDiscard());
    }

    @Test
    @DisplayName("\"chooseCard\" should play weakest if impossible to win when it won the first round and plays second")
    void chooseCard_ShouldPlayWeakest_IfImpossibleToWin_WhenWonFirstRound_AndPlaySecond() {
        BotMadeInDescalvado bot = new BotMadeInDescalvado();

        GameIntel intel = MockRound
            .vira(THREE, CLUBS)
            // First Round
            .giveA(FOUR, CLUBS).play() // Manilha
            .giveB(FIVE, CLUBS).play()
            // Second Round
            .giveB(SEVEN, CLUBS).play()
            //
            .giveA(SIX, CLUBS)
            .giveA(FIVE, HEARTS)
            .giveB(ACE, HEARTS)
            .build();

        CardToPlay playedCard = bot.chooseCard(intel);

        assertEquals(TrucoCard.of(FIVE, HEARTS), playedCard.content());
        assertFalse(playedCard.isDiscard());
    }

    @Test
    @DisplayName("\"chooseCard\" should play strongest when it lost the first round and plays first")
    void chooseCard_ShouldPlayStrongest_WhenLostFirstRound_AndPlaysFirst() {
        BotMadeInDescalvado bot = new BotMadeInDescalvado();

        GameIntel intel = MockRound
            .vira(THREE, CLUBS)
            // First Round
            .giveB(FOUR, CLUBS).play() // Manilha
            .giveA(FIVE, CLUBS).play()
            //
            .giveA(SIX, CLUBS)
            .giveA(FIVE, HEARTS)
            .giveB(ACE, HEARTS)
            .giveB(ACE, SPADES)
            .build();

        CardToPlay playedCard = bot.chooseCard(intel);

        assertEquals(TrucoCard.of(SIX, CLUBS), playedCard.content());
        assertFalse(playedCard.isDiscard());
    }

    @Test
    @DisplayName("\"chooseCard\" should play weakest to win when it lost the first round and plays second")
    void chooseCard_ShouldPlayWeakestToWin_WhenLostFirstRound_AndPlaysSecond() {
        BotMadeInDescalvado bot = new BotMadeInDescalvado();

        GameIntel intel = MockRound
            .vira(THREE, CLUBS)
            // First Round
            .giveA(FIVE, CLUBS).play()
            .giveB(FOUR, CLUBS).play() // Manilha
            // Second Round
            .giveB(ACE, HEARTS).play()
            //
            .giveA(TWO, CLUBS)
            .giveA(THREE, HEARTS)
            .giveB(ACE, SPADES)
            .build();

        CardToPlay playedCard = bot.chooseCard(intel);

        assertEquals(TrucoCard.of(TWO, CLUBS), playedCard.content());
        assertFalse(playedCard.isDiscard());
    }

    @Test
    @DisplayName("\"chooseCard\" should play strongest when it drew first round")
    void chooseCard_ShouldPlayStrongest_WhenDrewFirstRound() {
        BotMadeInDescalvado bot = new BotMadeInDescalvado();

        GameIntel intel = MockRound
            .vira(THREE, CLUBS)
            // First Round
            .giveA(FIVE, CLUBS).play()
            .giveB(FIVE, HEARTS).play()
            // Second Round
            .giveA(FIVE, SPADES)
            .giveA(SIX, SPADES)
            .giveB(FOUR, HEARTS)
            .giveB(FOUR, DIAMONDS)
            .build();

        CardToPlay playedCard = bot.chooseCard(intel);

        assertEquals(TrucoCard.of(SIX, SPADES), playedCard.content());
        assertFalse(playedCard.isDiscard());
    }

    @Test
    @DisplayName("\"chooseCard\" should play anything when it is at the third round")
    void chooseCard_ShouldPlayAnything_WhenAtThirdRound() {
        BotMadeInDescalvado bot = new BotMadeInDescalvado();

        GameIntel intel = MockRound
            .vira(THREE, CLUBS)
            // First Round
            .giveA(FOUR, CLUBS).play() // Manilha
            .giveB(FIVE, CLUBS).play()
            // Second Round
            .giveB(FOUR, HEARTS).play() // Manilha
            .giveA(FIVE, HEARTS).play()
            //
            .giveA(FIVE, SPADES)
            .giveB(FIVE, DIAMONDS)
            .build();

        CardToPlay playedCard = bot.chooseCard(intel);

        assertEquals(TrucoCard.of(FIVE, SPADES), playedCard.content());
        assertFalse(playedCard.isDiscard());
    }
}
