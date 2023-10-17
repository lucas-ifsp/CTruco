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

package com.yuri.impl;

import com.bueno.spi.model.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;
import static org.junit.jupiter.api.Assertions.*;

public class BotMadeInDescalvadoTest {
    // int getRaiseResponse(GameIntel intel);
    // Answers a point raise request in a truco hand
    // -1 quit
    //  0 accept
    //  1 re-raise/call
    // > Tem que retornar no escopo, não pode retornar >= 2 ou <= -2

    // boolean getMaoDeOnzeResponse(GameIntel intel)
    // Choose if bot plays a mão de onze
    // false -> quit
    // true  -> play

    // boolean decideIfRaises(GameIntel intel)
    // Choose if bot starts a point raise request
    // false -> nothing
    // true  -> raise

    // CardToPlay chooseCard(GameIntel intel)
    // Provided the card will be played or discarded in the current round
    // > Tem que retornar uma carta que tem na mão

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

    // Second Round
    // Ganho, Joga Primeiro
        // Mais fraca para fazer gastar
    // Ganho, Joga Segundo
        // Se der pra ganhar ganha
            // Se não mais fraca para fazer gastar
    // Perdeu, Joga Primeiro
        // A mais forte
    // Perdeu, Joga Segundo
        // O minimo pra ganhar
            // Senão F

//    @Test
//    @DisplayName("\"chooseCard\" should weakest card when won first round and plays first")
//    void chooseCardShouldWeakestCardWhenWonFirstRoundAndPlaysFirst() {
//        BotMadeInDescalvado bot = new BotMadeInDescalvado();
//
//        TrucoCard expectedPlayedCard = TrucoCard.of(CardRank.TWO, CLUBS);
//
//        GameIntel intel = MockRound.builder()
//            .vira(TrucoCard.of(CardRank.FOUR, CLUBS))
//            .deckBot(
//                expectedPlayedCard,
//                TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
//                TrucoCard.of(CardRank.KING, CLUBS)
//            )
//            .deckOpponent(
//                TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
//                TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
//                TrucoCard.of(CardRank.ACE, CardSuit.SPADES)
//            )
//            .opponentPlays(0)
//            .build();
//
//        CardToPlay playedCard = bot.chooseCard(intel);
//
//        assertEquals(expectedPlayedCard, playedCard.content());
//        assertFalse(playedCard.isDiscard());
//    }

    // Third Round
        // Joga

    // boolean getMaoDeOnzeResponse(GameIntel intel)
    // 1  - Se eu tiver 3 manilha = true
    // 2  - Se eu tiver 2 manilha = true
    // 3  - Se eu tiver 1 manilha e dois 3 = true
    // 4  - Se eu tiver 1 manilha e um 3 = true
    // 5  - Se eu tiver Tres 3 = true
    // 6  - Se eu tiver Dois 3 e um Dois = true
    // 7  - Se eu NÃO tiver nenhuma manilha = false
    // 8  - Se eu tiver uma manilha e nenhum 3 = false
    // 9  - Se eu tiver apenas um 3 = false
    // 10 - Se eu não tiver com manilha ou 3 = false
}
