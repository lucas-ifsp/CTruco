package com.belini.luciano.matapatobot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;



import java.util.List;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MataPatoBotTest {

    @BeforeEach
    public void createPatoBot(){
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
    void shouldReturnFalseIfWePlayFirts(){
        mataPatoBot = new MataPatoBot();
        boolean opponentPlay = false;
        assertThat(mataPatoBot.checkFirstPlay(Optional.empty()).equals(opponentPlay));
    }

    @Test
    @DisplayName("Play the lowest card that kills opponent card")
    public void shouldPlayLowestWinningCardIfCanDefeatOpponent() {
        GameIntel intel  = mock(GameIntel.class);
        TrucoCard card1 = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);
        TrucoCard card2 = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
        TrucoCard card3 = TrucoCard.of(CardRank.KING,CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of (CardRank.FOUR, CardSuit.CLUBS);
        TrucoCard opponentCard = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
        TrucoCard expected = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);

        when(intel.getCards()).thenReturn(Arrays.asList(card1,card2,card3));
        when(intel.getVira()).thenReturn(vira);
        when(intel.getOpponentCard()).thenReturn(Optional.ofNullable(opponentCard));

        assertThat(mataPatoBot.KillingOpponentCard(intel)).isEqualTo(expected);
    }

    @Test
    @DisplayName("Play the lowest card if no card can defeat opponent")
    public void shouldPlayLowestCardIfNoCardCanDefeatOpponent() {
        GameIntel intel  = mock(GameIntel.class);

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
    @DisplayName("Should return true if the bot has three cards in hand")
    public void shouldReturnTrueIfBotHasThreeCards() {
        GameIntel intel = mock(GameIntel.class);

        TrucoCard card1 = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);
        TrucoCard card2 = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
        TrucoCard card3 = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);

        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2, card3));

        //chama o método RoundCheck
        boolean hasThreeCards = mataPatoBot.RoundCheck(intel);

        assertThat(hasThreeCards).isTrue();
    }

    @Test
    @DisplayName("Should return true if the bot has two cards in hand")
    public void shouldReturnFalseIfBotHasTwoCards() {
        GameIntel intel = mock(GameIntel.class);

        TrucoCard card1 = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);
        TrucoCard card2 = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);

        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2));

        boolean hasThreeCards = mataPatoBot.RoundCheck(intel);

        assertThat(hasThreeCards).isTrue();
    }


}