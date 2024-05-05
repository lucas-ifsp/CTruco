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

import static com.bueno.spi.model.CardRank.FOUR;
import static com.bueno.spi.model.CardRank.TWO;
import static com.bueno.spi.model.CardSuit.HEARTS;
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
}
