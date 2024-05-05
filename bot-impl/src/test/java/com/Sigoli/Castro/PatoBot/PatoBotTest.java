package com.Sigoli.Castro.PatoBot;
import com.bueno.spi.model.*;
import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import com.bueno.spi.model.GameIntel;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class PatoBotTest {
    PatoBot patoBot;
    private GameIntel.StepBuilder stepBuilder;

    @BeforeEach
    public void createPatoBot(){
        patoBot = new PatoBot();
    }

    @Test
    @DisplayName("Should return true id opponent is first to play")
    void shoulReturnTrueIfOpponentIsFirstToPlay() {
        TrucoCard opponentCard = TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS);
        GameIntel intel = mock(GameIntel.class);
        when(intel.getOpponentCard()).thenReturn(Optional.of(opponentCard));
        assertThat( patoBot.checkIfOpponentIsFirstToPlay(intel.getOpponentCard())).isTrue();
    }

   @Test
   @DisplayName("Should return false if opponent is not first to play")
    void shouldReturnFalseIfOpponentIsNotFirstToPlay(){
        GameIntel intel = mock(GameIntel.class);
        when(intel.getOpponentCard()).thenReturn(Optional.empty());
        assertThat(patoBot.checkIfOpponentIsFirstToPlay(intel.getOpponentCard())).isFalse();
    }

    @Test
    @DisplayName("Should Return three if got three cards in hand")
    void shouldReturnThreeIfGotThreeCardsInHand(){
        int numberOfCards = 3;
        GameIntel intel  = mock(GameIntel.class);
        TrucoCard card1 = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);
        TrucoCard card2 =  TrucoCard.of(CardRank.ACE,CardSuit.HEARTS);
        TrucoCard card3 = TrucoCard.of(CardRank.THREE,CardSuit.CLUBS);
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2, card3));

        assertThat(patoBot.getNumberOfCardsInHand(intel)).isEqualTo(numberOfCards);
    }

    @Test
    @DisplayName("Should Return one if got one cards in hand")
    void shouldReturnOneIfGotOneCardInHand(){
        int numberOfCards = 1;
        GameIntel intel  = mock(GameIntel.class);
        TrucoCard card1 = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);
        when(intel.getCards()).thenReturn(Arrays.asList(card1));

        assertThat(patoBot.getNumberOfCardsInHand(intel)).isEqualTo(numberOfCards);
    }

    @Test
    @DisplayName("Should play lowest winning card if can defeat opponent")
    public void shouldPlayLowestWinningCardIfCanDefeatOpponent() {
        GameIntel intel  = mock(GameIntel.class);
        TrucoCard card1 = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);
        TrucoCard card2 = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
        TrucoCard card3 = TrucoCard.of(CardRank.QUEEN,CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of (CardRank.SEVEN, CardSuit.SPADES);
        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);
        TrucoCard expected = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);

        when(intel.getCards()).thenReturn(Arrays.asList(card1,card2,card3));
        when(intel.getVira()).thenReturn(vira);
        when(intel.getOpponentCard()).thenReturn(Optional.ofNullable(opponentCard));
        assertThat(patoBot.attemptToBeatOpponentCard(intel)).isEqualTo(expected);

    }

    @Test
    @DisplayName("Should play a weaker card if unable to defeat opponent's card")
    public void shouldPlayWeakerCardIfCannotDefeatOpponent() {
        GameIntel intel  = mock(GameIntel.class);
        TrucoCard card1 = TrucoCard.of(CardRank.TWO, CardSuit.SPADES);
        TrucoCard card2 = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
        TrucoCard card3 = TrucoCard.of(CardRank.SIX,CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of (CardRank.SEVEN, CardSuit.SPADES);
        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);
        TrucoCard expected = TrucoCard.of(CardRank.SIX,CardSuit.CLUBS);

        when(intel.getCards()).thenReturn(Arrays.asList(card1,card2,card3));
        when(intel.getVira()).thenReturn(vira);
        when(intel.getOpponentCard()).thenReturn(Optional.ofNullable(opponentCard));

        assertThat(patoBot.attemptToBeatOpponentCard(intel)).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should play a strong card, excluding Zap")
    public void shouldPlayStrongCardExcludingZap() {
        GameIntel intel  = mock(GameIntel.class);
        TrucoCard card1 = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
        TrucoCard card2 = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
        TrucoCard card3 = TrucoCard.of(CardRank.QUEEN,CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of (CardRank.SEVEN, CardSuit.SPADES);
        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);
        TrucoCard expected = TrucoCard.of(CardRank.THREE,CardSuit.SPADES);

        when(intel.getCards()).thenReturn(Arrays.asList(card1,card2,card3));
        when(intel.getVira()).thenReturn(vira);
        when(intel.getOpponentCard()).thenReturn(Optional.ofNullable(opponentCard));
        assertThat(patoBot.selectStrongerCardExcludingZapAndCopas(intel)).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should discard the lowest card if my hand doesn't have a card that wins the second round")
    public void shouldDiscardTheLowestCardIfMyHandDoesntHaveACardThatWinsTheSecondRound() {
        GameIntel intel  = mock(GameIntel.class);
        TrucoCard card1 = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
        TrucoCard card2 = TrucoCard.of(CardRank.QUEEN,CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of (CardRank.ACE, CardSuit.SPADES);
        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);
        TrucoCard expected = TrucoCard.of(CardRank.QUEEN,CardSuit.CLUBS);

        when(intel.getCards()).thenReturn(Arrays.asList(card1,card2));
        when(intel.getVira()).thenReturn(vira);
        when(intel.getOpponentCard()).thenReturn(Optional.ofNullable(opponentCard));

        assertThat(patoBot.attemptToBeatOpponentCard(intel)).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should accept Mão de Onze when my cards are stronger")
    public void shouldAcceptMaoDeOnzeWhenMyCardsAreStronger(){
        GameIntel intel = mock(GameIntel.class);

        TrucoCard card1 = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
        TrucoCard card2 = TrucoCard.of(CardRank.QUEEN,CardSuit.CLUBS);
        TrucoCard card3 = TrucoCard.of(CardRank.TWO,CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of (CardRank.ACE, CardSuit.SPADES);

        when(intel.getCards()).thenReturn(Arrays.asList(card1,card2,card3));
        when(intel.getVira()).thenReturn(vira);

        assertTrue(patoBot.checkIfAcceptMaoDeOnze(intel));
    }

    @Test
    @DisplayName("Should not accept Mão de Onze when my cards are weak")
    public void shouldNotAcceptMaoDeOnzeWhenMyCardsAreWeak(){
        GameIntel intel = mock(GameIntel.class);

        TrucoCard card1 = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
        TrucoCard card2 = TrucoCard.of(CardRank.QUEEN,CardSuit.CLUBS);
        TrucoCard card3 = TrucoCard.of(CardRank.ACE,CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of (CardRank.ACE, CardSuit.SPADES);

        when(intel.getCards()).thenReturn(Arrays.asList(card1,card2,card3));
        when(intel.getVira()).thenReturn(vira);

        assertFalse(patoBot.checkIfAcceptMaoDeOnze(intel));
    }

}



