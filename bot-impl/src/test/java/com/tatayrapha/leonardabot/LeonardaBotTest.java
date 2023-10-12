package com.tatayrapha.leonardabot;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    @DisplayName("Should play the highest card to win the first round.")
    void playHighestCardToWinFirstRound() {
        final List<TrucoCard> trucoCardList = Arrays.asList(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS), TrucoCard.of(CardRank.JACK, CardSuit.HEARTS));
        when(intel.getCards()).thenReturn(trucoCardList);
        when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON));
        CardToPlay chosenCard = leonardaBot.chooseCard(intel);
        TrucoCard expectedWinningCard = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
        assertThat(chosenCard).isNotNull();
        assertThat(chosenCard.value()).isEqualTo(expectedWinningCard);
    }

    @Test
    @DisplayName("Should play the second-highest card to win the second round.")
    void playSecondHighestCardToWinSecondRound() {
        final List<TrucoCard> trucoCardList = Arrays.asList(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS), TrucoCard.of(CardRank.JACK, CardSuit.HEARTS));
        when(intel.getCards()).thenReturn(trucoCardList);
        when(intel.getRoundResults()).thenReturn(Arrays.asList(GameIntel.RoundResult.WON, GameIntel.RoundResult.LOST));
        CardToPlay chosenCard = leonardaBot.chooseCard(intel);
        TrucoCard expectedWinningCard = TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS);
        assertThat(chosenCard).isNotNull();
        assertThat(chosenCard.value()).isEqualTo(expectedWinningCard);
    }

    @Test
    @DisplayName("Should raise aggressively in the first round.")
    void raiseAggressivelyInFirstRound() {
        when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON));
        int botResponse = leonardaBot.getRaiseResponse(intel);
        assertThat(botResponse).isEqualTo(1);
    }

    @Test
    @DisplayName("Should not raise in the second round.")
    void notRaiseInSecondRound() {
        when(intel.getRoundResults()).thenReturn(Arrays.asList(GameIntel.RoundResult.WON, GameIntel.RoundResult.LOST));
        int botResponse = leonardaBot.getRaiseResponse(intel);
        assertThat(botResponse).isEqualTo(0);
    }

    @Test
    @DisplayName("Should be aggressive with a strong hand in the second round.")
    void beAggressiveWithStrongHandInSecondRound() {
        when(intel.getRoundResults()).thenReturn(Arrays.asList(GameIntel.RoundResult.WON, GameIntel.RoundResult.LOST));
        when(intel.getScore()).thenReturn(10);
        int botResponse = leonardaBot.getRaiseResponse(intel);
        assertThat(botResponse).isEqualTo(1);
    }

    @Test
    @DisplayName("Should respond correctly in the Mão de Onze scenario.")
    void respondInMaoDeOnzeScenario() {
        when(intel.getScore()).thenReturn(11);
        boolean response = leonardaBot.getMaoDeOnzeResponse(intel);
        assertThat(response).isTrue();
    }

    @Test
    @DisplayName("Should play the strongest card and win the round in the Mão de Ferro scenario.")
    void winInTheMaoDeFerroScenario() {
        final List<TrucoCard> trucoCardList = Arrays.asList(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.ACE, CardSuit.HEARTS));
        final List<TrucoCard> opponentCards = Arrays.asList(TrucoCard.of(CardRank.ACE, CardSuit.SPADES), TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));
        when(intel.getCards()).thenReturn(trucoCardList);
        when(intel.getOpponentCard()).thenReturn(Optional.of(opponentCards.get(0)));
        when(intel.getRoundResults()).thenReturn(Arrays.asList(GameIntel.RoundResult.WON, GameIntel.RoundResult.LOST));
        CardToPlay chosenCard = leonardaBot.chooseCard(intel);
        TrucoCard expectedWinningCard = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);
        assertThat(chosenCard).isNotNull();
        assertThat(chosenCard.value()).isEqualTo(expectedWinningCard);
    }

    @Test
    @DisplayName("Should challenge the opponent with a 'Truco' request in the second round with a strong hand.")
    void challengeWithTrucoInSecondRoundWithStrongHand() {
        final List<TrucoCard> trucoCardList = Arrays.asList(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.ACE, CardSuit.HEARTS));
        Mockito.lenient().when(intel.getCards()).thenReturn(trucoCardList);
        Mockito.lenient().when(intel.getRoundResults()).thenReturn(Arrays.asList(GameIntel.RoundResult.WON, GameIntel.RoundResult.LOST));
        Mockito.lenient().when(intel.getScore()).thenReturn(10);
        int botResponse = leonardaBot.getRaiseResponse(intel);
        assertThat(botResponse).isEqualTo(1);
    }

    @Test
    @DisplayName("Should quit hand when getRaiseResponse returns -1.")
    void testQuitHandWithNegativeRaiseResponse() {
        when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.LOST, GameIntel.RoundResult.LOST));
        int botResponse = leonardaBot.getRaiseResponse(intel);
        assertThat(botResponse).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should accept point raise when getRaiseResponse returns 0.")
    void testAcceptPointRaiseWithZeroRaiseResponse() {
        when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON, GameIntel.RoundResult.WON));
        int botResponse = leonardaBot.getRaiseResponse(intel);
        assertThat(botResponse).isEqualTo(0);
    }

    @Test
    @DisplayName("Should re-raise when getRaiseResponse returns 1.")
    void testReRaiseWithPositiveRaiseResponse() {
        when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON, GameIntel.RoundResult.WON));
        when(intel.getScore()).thenReturn(10);
        int botResponse = leonardaBot.getRaiseResponse(intel);
        assertThat(botResponse).isEqualTo(1);
    }

    @Test
    @DisplayName("Should respond correctly in the opponent Mão de Onze scenario.")
    void testShouldRespondProperlyMaoDeOnze(){
        when(intel.getOpponentScore()).thenReturn(11);
        boolean response = leonardaBot.getMaoDeOnzeResponse(intel);
        assertThat(response).isTrue();
    }

    @Test
    @DisplayName("Should not ask for raise in opponent Mão de Onze scenario.")
    void testShouldNotRaiseInOpponentMaoDeOnze(){
        when(intel.getOpponentScore()).thenReturn(11);
        boolean response = leonardaBot.decideIfRaises(intel);
        assertThat(response).isFalse();
    }

    @Test
    @DisplayName("Should not ask for raise in Mão de Onze scenario.")
    void testShouldNotRaiseInMaoDeOnze(){
        when(intel.getScore()).thenReturn(11);
        boolean response = leonardaBot.decideIfRaises(intel);
        assertThat(response).isFalse();
    }

    @Test
    @DisplayName("Should ask for raise when having Higher Casal ind hand.")
    void testShouldRaiseWhenHavingHigherCasal(){
        final TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);
        final List<TrucoCard> trucoCardList = Arrays.asList(TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS), TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS), TrucoCard.of(CardRank.ACE, CardSuit.HEARTS));
        when(intel.getScore()).thenReturn(2);
        when(intel.getOpponentScore()).thenReturn(6);
        when(intel.getCards()).thenReturn(trucoCardList);
        when(intel.getVira()).thenReturn(vira);
        boolean response = leonardaBot.decideIfRaises(intel);
        assertThat(response).isTrue();
    }

    @Test
    @DisplayName("Should ask for re-raise when having Higher Casal ind hand.")
    void testShouldReRaiseWhenHavingHigherCasal(){
        final TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);
        final List<TrucoCard> trucoCardList = Arrays.asList(TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS), TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS), TrucoCard.of(CardRank.ACE, CardSuit.HEARTS));
        when(intel.getCards()).thenReturn(trucoCardList);
        when(intel.getVira()).thenReturn(vira);
        int response = leonardaBot.getRaiseResponse(intel);
        assertThat(response).isEqualTo(1);
    }

    @Test
    @DisplayName("Should play Mão de Onze when having casal in hand.")
    void testShouldPlayMaoDeOnzeWithCasalInHand(){
        final TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);
        final List<TrucoCard> trucoCardList = Arrays.asList(TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS), TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS), TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
        when(intel.getCards()).thenReturn(trucoCardList);
        when(intel.getVira()).thenReturn(vira);
        boolean response = leonardaBot.getMaoDeOnzeResponse(intel);
        assertThat(response).isTrue();
    }
}