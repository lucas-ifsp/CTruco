/*
 *  Copyright (C) 2022 Lucas B. R. de Oliveira - IFSP/SCL
 *  Contact: lucas <dot> oliveira <at> ifsp <dot> edu <dot> br
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

package com.bueno.impl.mineirobot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.bueno.spi.model.GameIntel.RoundResult.LOST;
import static com.bueno.spi.model.GameIntel.RoundResult.WON;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayingStrategyTest {

    @Mock GameIntel intel;

    @Test
    @DisplayName("Should create the first round strategy if number of round results is zero")
    void shouldCreateTheFirstRoundStrategyIfNumberOfRoundResultsIsZero() {
        assertEquals(FirstRoundStrategy.class, PlayingStrategy.of(intel).getClass());
    }

    @Test
    @DisplayName("Should create the second round strategy if number of round results is one")
    void shouldCreateTheSecondRoundStrategyIfNumberOfRoundResultsIsOne() {
        when(intel.getRoundResults()).thenReturn(List.of(WON));
        assertEquals(SecondRoundStrategy.class, PlayingStrategy.of(intel).getClass());
    }

    @Test
    @DisplayName("Should create the third round strategy if number of round results is two")
    void shouldCreateTheThirdRoundStrategyIfNumberOfRoundResultsIsTwo() {
        when(intel.getRoundResults()).thenReturn(List.of(WON, WON));
        assertEquals(ThirdRoundStrategy.class, PlayingStrategy.of(intel).getClass());
    }

    @Test
    @DisplayName("Should throw if requests a strategy for a hand with three round results")
    void shouldThrowIfRequestsAStrategyForAHandWithThreeRoundResults() {
        when(intel.getRoundResults()).thenReturn(List.of(WON, LOST, WON));
        assertThrows(IllegalStateException.class, () -> PlayingStrategy.of(intel));
    }

    @Test
    @DisplayName("Should accept mao de onze if has one card higher than 10 and remaining worth at least 14")
    void shouldAcceptMaoDeOnzeIfHasOneCardHigherThan10AndRemainingWorthAtLeast14() {
        final var botCards = List.of(TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS));

        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS));
        when(intel.getCards()).thenReturn(botCards);

        assertTrue(PlayingStrategy.of(intel).getMaoDeOnzeResponse(intel));
    }

    @Test
    @DisplayName("Should accept mao de onze if has one card higher than 10 and remaining worth at least 15 if opponent has 8 or more points")
    void shouldAcceptMaoDeOnzeIfHasOneCardHigherThan11AndRemainingWorthAtLeast14IfOpponentHas8OrMorePoints() {
        final var botCards = List.of(TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.FIVE, CardSuit.SPADES));

        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS));
        when(intel.getCards()).thenReturn(botCards);
        when(intel.getOpponentScore()).thenReturn(8);

        assertTrue(PlayingStrategy.of(intel).getMaoDeOnzeResponse(intel));
    }

    @Test
    @DisplayName("Should accept mao de onze if has 27 in cards")
    void shouldAcceptMaoDeOnzeIfHas27InCards() {
        final var botCards = List.of(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.THREE, CardSuit.SPADES));

        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS));
        when(intel.getCards()).thenReturn(botCards);

        assertTrue(PlayingStrategy.of(intel).getMaoDeOnzeResponse(intel));
    }

    @Test
    @DisplayName("Should not accept mao de onze if has no manilha")
    void shouldNotAcceptMaoDeOnzeIfHasManilhaAndRemainingWorthLessThan14() {
        final var botCards = List.of(TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.FIVE, CardSuit.SPADES));

        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS));
        when(intel.getCards()).thenReturn(botCards);

        assertFalse(PlayingStrategy.of(intel).getMaoDeOnzeResponse(intel));
    }


    @Test
    @DisplayName("Should correctly get enough card to draw for regular cards")
    void shouldCorrectlyGetEnoughCardToDrawForRegularCards() {
        final var vira = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);
        final var opponentCard = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
        final var botCards = List.of(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.FIVE, CardSuit.SPADES));

        final var expected = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);
        final var actual = PlayingStrategy.of(intel).getPossibleCardToDraw(botCards, vira, opponentCard).orElse(null);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Should get no card to draw if opponent card is a manilha")
    void shouldGetNoCardToDrawIfOpponentCardIsAManilha() {
        final var vira = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);
        final var opponentCard = TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS);
        final var botCards = List.of(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.FIVE, CardSuit.SPADES));

        final var actual = PlayingStrategy.of(intel).getPossibleCardToDraw(botCards, vira, opponentCard);
        assertEquals(Optional.empty(), actual);
    }

    @Test
    @DisplayName("Should get no card to draw if there is no card to draw")
    void shouldGetNoCardToDrawIfThereIsNoCardToDraw() {
        final var vira = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);
        final var opponentCard = TrucoCard.of(CardRank.SIX, CardSuit.CLUBS);
        final var botCards = List.of(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.FIVE, CardSuit.SPADES));

        final var actual = PlayingStrategy.of(intel).getPossibleCardToDraw(botCards, vira, opponentCard);
        assertEquals(Optional.empty(), actual);
    }

    @Test
    @DisplayName("Should correctly get enough card to win for regular cards")
    void shouldCorrectlyGetEnoughCardToWinForRegularCards() {
        final var vira = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);
        final var opponentCard = TrucoCard.of(CardRank.SIX, CardSuit.CLUBS);
        final var botCards = List.of(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.FIVE, CardSuit.SPADES));

        final var actual = PlayingStrategy.of(intel).getPossibleEnoughCardToWin(botCards, vira, opponentCard).orElse(null);
        final TrucoCard expected = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Should get enough card to win if opponent card is a manilha")
    void shouldGetEnoughCardToWinIfOpponentCardIsAManilha() {
        final var vira = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);
        final var opponentCard = TrucoCard.of(CardRank.FIVE, CardSuit.SPADES);
        final var botCards = List.of(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS));

        final var actual = PlayingStrategy.of(intel).getPossibleEnoughCardToWin(botCards, vira, opponentCard).orElse(null);
        final TrucoCard expected = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Should get no card if can not win")
    void shouldGetNoCardIfCanNotWin() {
        final var vira = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);
        final var opponentCard = TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS);
        final var botCards = List.of(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS));

        final var actual = PlayingStrategy.of(intel).getPossibleEnoughCardToWin(botCards, vira, opponentCard);
        assertEquals(Optional.empty(), actual);
    }

    @Test
    @DisplayName("Should get correct card value for regular cards")
    void shouldGetCorrectCardValueForRegularCards() {
        final var vira = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);
        final var card = TrucoCard.of(CardRank.THREE, CardSuit.CLUBS);
        assertEquals(9, PlayingStrategy.getCardValue(intel.getOpenCards(), card, vira));
    }

    @Test
    @DisplayName("Should increment lower card value if four cards of higher card have been already played")
    void shouldIncrementLowerCardValueIfFourCardsOfHigherCardHaveBeenAlreadyPlayed() {
        final var vira = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);
        final var card = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);
        final List<TrucoCard> openCards = List.of(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.THREE, CardSuit.SPADES), TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS));

        when(intel.getOpenCards()).thenReturn(openCards);
        assertEquals(9, PlayingStrategy.getCardValue(intel.getOpenCards(), card, vira));
    }

    @Test
    @DisplayName("Should value of rank three be 13 if all manilhas have been played")
    void shouldValueOfRankThreeBe13IfAllManilhasHaveBeenPlayed() {
        final var vira = TrucoCard.of(CardRank.KING, CardSuit.CLUBS);
        final var card = TrucoCard.of(CardRank.THREE, CardSuit.CLUBS);
        final List<TrucoCard> openCards = List.of(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.ACE, CardSuit.SPADES), TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));

        when(intel.getOpenCards()).thenReturn(openCards);
        assertEquals(13, PlayingStrategy.getCardValue(intel.getOpenCards(), card, vira));
    }

    @Test
    @DisplayName("Should ouros value be 11 if zap has already been played")
    void shouldOurosValueBe11IfZapHasAlreadyBeenPlayed() {
        final var vira = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);
        final var ouros = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
        final List<TrucoCard> openCards = List.of(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
        assertEquals(11, PlayingStrategy.getCardValue(openCards, ouros, vira));
    }

    /*@ParameterizedTest
    @CsvSource({"FOUR,0", "FIVE,1", "SIX,2", "SEVEN,3", "QUEEN,4", "KING,5", "ACE,6", "TWO,7", "THREE,8", "JACK,9"})
    @DisplayName("Should get correct card values")
    void shouldGetCorrectCardValues(CardRank rank, int value) {
        final var vira = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);
        final TrucoCard card = TrucoCard.of(rank, CardSuit.DIAMONDS);
        assertEquals(value, PlayingStrategy.getCardValue(List.of(), card, vira));
    }*/

}