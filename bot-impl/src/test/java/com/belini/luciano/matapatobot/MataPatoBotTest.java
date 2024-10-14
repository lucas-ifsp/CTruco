package com.belini.luciano.matapatobot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;


import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MataPatoBotTest {

    @BeforeEach
    public void createPatoBot() {
        mataPatoBot = new MataPatoBot();
    }

    MataPatoBot mataPatoBot;
    private GameIntel.StepBuilder stepBuilder;

    @Test
    @DisplayName("If opponent plays first return true")
    void shouldReturnTrueForOpponentPlayFirst() {
        mataPatoBot = new MataPatoBot();
        TrucoCard opponentCard = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
        boolean opponentPlay = true;
        assertThat(mataPatoBot.checkFirstPlay(Optional.ofNullable(opponentCard)).equals(opponentPlay));
    }

    @Test
    @DisplayName("Should return false if we play firt")
    void shouldReturnFalseIfWePlayFirts() {
        mataPatoBot = new MataPatoBot();
        boolean opponentPlay = false;
        assertThat(mataPatoBot.checkFirstPlay(Optional.empty()).equals(opponentPlay));
    }

    @Test
    @DisplayName("Play the lowest card that kills opponent card")
    public void shouldPlayLowestWinningCardIfCanDefeatOpponent() {
        GameIntel intel = mock(GameIntel.class);
        TrucoCard card1 = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);
        TrucoCard card2 = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
        TrucoCard card3 = TrucoCard.of(CardRank.KING, CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);
        TrucoCard opponentCard = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
        TrucoCard expected = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);

        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2, card3));
        when(intel.getVira()).thenReturn(vira);
        when(intel.getOpponentCard()).thenReturn(Optional.ofNullable(opponentCard));

        assertThat(mataPatoBot.KillingOpponentCard(intel)).isEqualTo(expected);
    }

    @Test
    @DisplayName("Play the lowest card if no card can defeat opponent")
    public void shouldPlayLowestCardIfNoCardCanDefeatOpponent() {
        GameIntel intel = mock(GameIntel.class);

        TrucoCard card1 = TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS);
        TrucoCard card2 = TrucoCard.of(CardRank.FIVE, CardSuit.SPADES);
        TrucoCard card3 = TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS);

        TrucoCard vira = TrucoCard.of(CardRank.THREE, CardSuit.HEARTS);
        TrucoCard opponentCard = TrucoCard.of(CardRank.KING, CardSuit.CLUBS);

        TrucoCard expected = TrucoCard.of(CardRank.FIVE, CardSuit.SPADES);

        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2, card3));
        when(intel.getVira()).thenReturn(vira);
        when(intel.getOpponentCard()).thenReturn(Optional.ofNullable(opponentCard));

        assertThat(mataPatoBot.KillingOpponentCard(intel)).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should return Round 1 if the bot has three cards in hand")
    public void shouldReturnRound1IfBotHasThreeCards() {
        GameIntel intel = mock(GameIntel.class);

        TrucoCard card1 = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);
        TrucoCard card2 = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
        TrucoCard card3 = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2, card3));

        String round = String.valueOf(mataPatoBot.RoundCheck(intel));

        assertThat(round).isEqualTo("Round 1");
    }

    @Test
    @DisplayName("Should return Round 2 if the bot has two cards in hand")
    public void shouldReturnRound2IfBotHasTwoCards() {
        GameIntel intel = mock(GameIntel.class);

        TrucoCard card1 = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);
        TrucoCard card2 = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);

        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2));

        String round = String.valueOf(mataPatoBot.RoundCheck(intel));
        assertThat(round).isEqualTo("Round 2");
    }

    @Test
    @DisplayName("Should return Round 3 if the bot has one card in hand")
    public void shouldReturnRound3IfBotHasOneCard() {
        GameIntel intel = mock(GameIntel.class);

        TrucoCard card1 = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);

        when(intel.getCards()).thenReturn(Arrays.asList(card1));
        String round = String.valueOf(mataPatoBot.RoundCheck(intel));
        assertThat(round).isEqualTo("Round 3");
    }

    @Test
    @DisplayName("Should return 'No cards' if the bot has no cards in hand")
    public void shouldReturnNoCardsIfBotHasNoCards() {
        GameIntel intel = mock(GameIntel.class);
        when(intel.getCards()).thenReturn(Arrays.asList());
        String round = String.valueOf(mataPatoBot.RoundCheck(intel));

        assertThat(round).isEqualTo("No cards");
    }

    @Test
    @DisplayName("Play a strong card, excluding top 3")
    public void shouldPlayStrongCard() {
        GameIntel intel = mock(GameIntel.class);
        TrucoCard card1 = TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS);
        TrucoCard card2 = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
        TrucoCard card3 = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
        TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.SPADES);
        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);
        TrucoCard expected = TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS);

        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2, card3));
        when(intel.getVira()).thenReturn(vira);
        when(intel.getOpponentCard()).thenReturn(Optional.ofNullable(opponentCard));

        assertThat(mataPatoBot.shouldPlayStrongCard(intel)).isEqualTo(expected);
    }

    @Test
    @DisplayName("Play a top 3 card against manilha")
    public void killTheManilha() {
        GameIntel intel = mock(GameIntel.class);
        TrucoCard card1 = TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS);
        TrucoCard card2 = TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES);
        TrucoCard card3 = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);
        TrucoCard opponentCard = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
        TrucoCard expected = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2, card3));
        when(intel.getVira()).thenReturn(vira);
        when(intel.getOpponentCard()).thenReturn(Optional.ofNullable(opponentCard));

        assertThat(mataPatoBot.shouldPlayStrongCard(intel)).isEqualTo(expected);
    }


}