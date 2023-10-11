package com.tatayrapha.leonardabot;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LeonardaBotTest {
    @Mock
    GameIntel intel;

    LeonardaBot leonardaBot;

    @BeforeEach
    void setUp() {
        leonardaBot = new LeonardaBot();
    }

    @Test
    @DisplayName("Should throw a random card.")
    void shouldThrowRandomCard() {
        final List<TrucoCard> trucoCardList = Arrays.asList(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), TrucoCard.of(CardRank.TWO, CardSuit.CLUBS), TrucoCard.of(CardRank.THREE, CardSuit.CLUBS));
        when(intel.getCards()).thenReturn(trucoCardList);
        CardToPlay chosenCard = leonardaBot.chooseCard(intel);
        assertThat(trucoCardList).contains(chosenCard.value());
    }

    @Test
    @DisplayName("Should ask for a raise when having a zap.")
    void shouldAskForRaiseWithZap() {
        final List<TrucoCard> trucoCardList = Arrays.asList(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), TrucoCard.of(CardRank.TWO, CardSuit.CLUBS), TrucoCard.of(CardRank.THREE, CardSuit.CLUBS));
        final TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
        when(intel.getVira()).thenReturn(vira);
        when(intel.getCards()).thenReturn(trucoCardList);
        assertThat(leonardaBot.decideIfRaises(intel)).isTrue();
    }

    @Test
    @DisplayName("Should not ask for a raise when not having a zap.")
    void shouldNotAskForRaiseWithoutZap() {
        final List<TrucoCard> trucoCardList = Arrays.asList(TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS), TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS), TrucoCard.of(CardRank.SIX, CardSuit.SPADES));
        final TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
        when(intel.getVira()).thenReturn(vira);
        when(intel.getCards()).thenReturn(trucoCardList);
        assertThat(leonardaBot.decideIfRaises(intel)).isFalse();
    }

    @Test
    @DisplayName("Should return a valid card.")
    void shouldReturnValidCard() {
        final List<TrucoCard> trucoCardList = Arrays.asList(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), TrucoCard.of(CardRank.TWO, CardSuit.CLUBS), TrucoCard.of(CardRank.THREE, CardSuit.CLUBS));
        when(intel.getCards()).thenReturn(trucoCardList);
        CardToPlay chosenCard = leonardaBot.chooseCard(intel);
        assertThat(trucoCardList).contains(chosenCard.content());
    }

    @ParameterizedTest
    @ValueSource(ints = {9, 10, 11})
    @DisplayName("Should ask for a raise when having a score of at least 9 points.")
    void shouldAskForRaiseWithScoreOfAtLeast10Points(int playerScore) {
        when(intel.getScore()).thenReturn(playerScore);
        assertThat(leonardaBot.decideIfRaises(intel)).isTrue();
    }

    @Test
    @DisplayName("Should not ask for a raise when having a weak hand and low opponent score.")
    void shouldNotAskForRaiseWithWeakHandAndLowOpponentScore() {
        final List<TrucoCard> trucoCardList = Arrays.asList(TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS), TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));
        final TrucoCard vira = TrucoCard.of(CardRank.THREE, CardSuit.CLUBS);
        when(intel.getVira()).thenReturn(vira);
        when(intel.getCards()).thenReturn(trucoCardList);
        assertThat(leonardaBot.decideIfRaises(intel)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8})
    @DisplayName("Should ask for a raise with a strong hand and varying opponent scores.")
    void shouldAskForRaiseWithStrongHandAndVaryingOpponentScores(int opponentScore) {
        final List<TrucoCard> trucoCardList = Arrays.asList(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), TrucoCard.of(CardRank.TWO, CardSuit.CLUBS), TrucoCard.of(CardRank.THREE, CardSuit.CLUBS));
        final TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);
        when(intel.getVira()).thenReturn(vira);
        when(intel.getCards()).thenReturn(trucoCardList);
        when(intel.getScore()).thenReturn(opponentScore);
        if (opponentScore >= 9 || trucoCardList.stream().anyMatch(card -> card.isZap(vira))) {
            assertThat(leonardaBot.decideIfRaises(intel)).isTrue();
        } else {
            assertThat(leonardaBot.decideIfRaises(intel)).isFalse();
        }
    }

    @Test
    @DisplayName("Should recognize manilhas in hand.")
    void shouldRecognizeManilhasInHand() {
        final List<TrucoCard> trucoCardList = Arrays.asList(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS), TrucoCard.of(CardRank.KING, CardSuit.SPADES));
        when(intel.getCards()).thenReturn(trucoCardList);
        CardToPlay chosenCard = leonardaBot.chooseCard(intel);
        assertThat(trucoCardList).contains(chosenCard.content());
    }

    @Test
    @DisplayName("Should play the lowest card if no zaps are available.")
    void shouldPlayLowestCardWithoutZaps() {
        final List<TrucoCard> trucoCardList = Arrays.asList(TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS), TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS), TrucoCard.of(CardRank.THREE, CardSuit.SPADES));
        when(intel.getCards()).thenReturn(trucoCardList);
        CardToPlay chosenCard = leonardaBot.chooseCard(intel);
        TrucoCard lowestCard = trucoCardList.stream().min(Comparator.comparing(TrucoCard::getRank)).orElse(null);
        assertThat(chosenCard).isNotNull();
        assertThat(chosenCard.value()).isEqualTo(lowestCard);
    }

    @Test
    @DisplayName("Should return a valid response for a raise.")
    void shouldReturnRaiseResponse() {
        when(intel.getScore()).thenReturn(9);
        int botResponse = leonardaBot.getRaiseResponse(intel);
        assertThat(botResponse).isIn(0, 1);
    }

    @Test
    @DisplayName("Should return -1 to quit a raise request.")
    void shouldReturnMinusOneToQuitRaiseRequest() {
        when(intel.getScore()).thenReturn(0);
        int botResponse = leonardaBot.getRaiseResponse(intel);
        assertThat(botResponse).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should return 0 to accept a raise request.")
    void shouldReturnZeroToAcceptRaiseRequest() {
        when(intel.getScore()).thenReturn(9);
        int botResponse = leonardaBot.getRaiseResponse(intel);
        assertThat(botResponse).isEqualTo(0);
    }

    /*
    @Test
    @DisplayName("Should return 1 to re-raise in response to a raise request.")
    void shouldReturnOneToReRaise() {
        when(intel.getScore()).thenReturn(11);
        when(intel.getCards()).thenReturn(Collections.singletonList(TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS)));
        int botResponse = leonardaBot.getRaiseResponse(intel);
        assertThat(botResponse).isEqualTo(1);
    }
    */

    @Test
    @DisplayName("Should return 0 to accept and play a raise request with a strong hand.")
    void shouldReturnZeroToAcceptRaiseWithStrongHand() {
        when(intel.getScore()).thenReturn(11);
        int botResponse = leonardaBot.getRaiseResponse(intel);
        assertThat(botResponse).isEqualTo(0);
    }

    /*
    @Test
    @DisplayName("Should return 0 to accept a raise request with a weak hand.")
    void shouldReturnZeroToAcceptRaiseWithWeakHand() {
        when(intel.getScore()).thenReturn(5);
        when(intel.getCards()).thenReturn(Collections.emptyList());
        int botResponse = leonardaBot.getRaiseResponse(intel);
        assertThat(botResponse).isEqualTo(0);
    }
    */

}
