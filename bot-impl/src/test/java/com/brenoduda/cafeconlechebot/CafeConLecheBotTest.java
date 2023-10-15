/*
 *  Copyright (C) 2023 Breno Nascimento Lopes, Maria Eduarda Santos - IFSP/SCL
 *  Contact: breno <dot> lopes <at> aluno <dot> ifsp <dot> edu <dot> br, santos <dot> maria2 <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.brenoduda.cafeconlechebot;

import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;
import static org.assertj.core.api.Assertions.assertThat;

public class CafeConLecheBotTest {
    @Nested
    @DisplayName("Test of the bot logic to decide if raises")
    class ShouldRaise {
        @Test
        @DisplayName("Should raise when has 3 manilhas")
        void shouldRaiseWhenHas3Manilhas() {
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(ACE, HEARTS),
                    TrucoCard.of(ACE, SPADES),
                    TrucoCard.of(ACE, CLUBS)
            );
            TrucoCard vira = TrucoCard.of(KING, DIAMONDS);

            GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(vira), vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(0);

            boolean decideIfRaises = new CafeConLecheBot().decideIfRaises(stepBuilder.build());
            assertThat(decideIfRaises).isEqualTo(true);
        }

        @Test
        @DisplayName("Should raise when has clubs and hearts")
        void shouldRaiseWhenHasClubsAndHearts() {
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(FOUR, HEARTS),
                    TrucoCard.of(SEVEN, SPADES),
                    TrucoCard.of(KING, CLUBS)
            );
            TrucoCard vira = TrucoCard.of(THREE, DIAMONDS);

            GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(vira), vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(0);

            boolean decideIfRaises = new CafeConLecheBot().decideIfRaises(stepBuilder.build());
            assertThat(decideIfRaises).isEqualTo(true);
        }
    }
}
