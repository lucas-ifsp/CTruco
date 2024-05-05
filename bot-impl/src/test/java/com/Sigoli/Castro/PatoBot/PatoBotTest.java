package com.Sigoli.Castro.PatoBot;
import java.util.*;


import com.bueno.spi.model.*;
import org.junit.jupiter.api.Test;
import com.bueno.spi.model.GameIntel;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class PatoBotTest {
    PatoBot patoBot;

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
    @DisplayName("Should Return one if got one card in hand")
    void shouldReturnOneIfGotOneCardInHand(){
        int numberOfCards = 1;
        GameIntel intel  = mock(GameIntel.class);
        TrucoCard card1 = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);
        when(intel.getCards()).thenReturn(Collections.singletonList(card1));

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
    @DisplayName("Should play a strong card, excluding Copas")
    public void shouldPlayStrongCardExcludingCopas() {
        GameIntel intel = mock(GameIntel.class);
        TrucoCard card1 = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
        TrucoCard card2 = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
        TrucoCard card3 = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES);
        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);
        TrucoCard expected = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);

        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2, card3));
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
    @DisplayName("Should play lowest winning card if can defeat opponent and is the second to Play on first Round")
    public void shouldPlayLowestWinningCardIfCanDefeatOpponentAndIsSecondToPLayonFirstRound(){

        GameIntel intel  = mock(GameIntel.class);
        TrucoCard card1 = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
        TrucoCard card2 = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
        TrucoCard card3 = TrucoCard.of(CardRank.QUEEN,CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of (CardRank.SEVEN, CardSuit.SPADES);
        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);
        CardToPlay expected = CardToPlay.of(card1);

        when(intel.getVira()).thenReturn(vira);
        when(intel.getCards()).thenReturn(Arrays.asList(card1,card2,card3));
        when(intel.getOpponentCard()).thenReturn(Optional.ofNullable(opponentCard));
        assertThat(patoBot.chooseCard(intel)).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should play a weaker card if unable to defeat opponent's card and is second to Play First Round ")
    public void shouldPlayWeakerCardIfCannotDefeatOpponentAndIsSecondToPLayFirstRound() {
        GameIntel intel  = mock(GameIntel.class);
        TrucoCard card1 = TrucoCard.of(CardRank.TWO, CardSuit.SPADES);
        TrucoCard card2 = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
        TrucoCard card3 = TrucoCard.of(CardRank.SIX,CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of (CardRank.SEVEN, CardSuit.SPADES);
        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);
        CardToPlay expected = CardToPlay.of(card3);
        when(intel.getCards()).thenReturn(Arrays.asList(card1,card2,card3));
        when(intel.getVira()).thenReturn(vira);
        when(intel.getOpponentCard()).thenReturn(Optional.ofNullable(opponentCard));
        assertThat(patoBot.chooseCard(intel)).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should play the stronger card excluding Zap if is First To Play First Round")
    public void shouldPlayTheStrongerCardExcludingZapIfIsFirstToPlayFirstRound() {
        GameIntel intel = mock(GameIntel.class);
        TrucoCard card1 = TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS);
        TrucoCard card2 = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);
        TrucoCard card3 = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
        CardToPlay expected = CardToPlay.of(card2);
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2, card3));
        when(intel.getVira()).thenReturn(vira);
        when(intel.getOpponentCard()).thenReturn(Optional.empty());
        assertThat(patoBot.chooseCard(intel)).isEqualTo(expected);
    }
    @Test
    @DisplayName("Should play stronger card in hand if first to play first round")
    public void shouldPlayStrongerCardInHandIfFirstToPlayFirstRound(){
        GameIntel intel = mock(GameIntel.class);
        TrucoCard card1 = TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS);
        TrucoCard card2 = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);
        TrucoCard card3 = TrucoCard.of(CardRank.THREE, CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
        CardToPlay expected = CardToPlay.of(card2);
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2, card3));
        when(intel.getVira()).thenReturn(vira);
        when(intel.getOpponentCard()).thenReturn(Optional.empty());
        assertThat(patoBot.chooseCard(intel)).isEqualTo(expected);
}

    @Test
    @DisplayName("Should Play Lowest card to trick opponent")
    public  void  shouldPlayLowestToTrickOpponent(){
        GameIntel intel = mock(GameIntel.class);
        TrucoCard card1 = TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS);
        TrucoCard card2 = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);
        TrucoCard vira = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
        CardToPlay expected = CardToPlay.of(card1);
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2));
        when(intel.getVira()).thenReturn(vira);
        when(intel.getOpponentCard()).thenReturn(Optional.empty());
        assertThat(patoBot.chooseCard(intel)).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should play meadle card to take a strong card from opponent ")
    public void shouldPlayMiddleCardToCaptureStrongOpponentCard(){
        GameIntel intel = mock(GameIntel.class);
        TrucoCard card1 = TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS);
        TrucoCard card2 = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);
        TrucoCard vira = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
        CardToPlay expected = CardToPlay.of(card1);
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2));
        when(intel.getVira()).thenReturn(vira);
        when(intel.getOpponentCard()).thenReturn(Optional.empty());
        assertThat(patoBot.chooseCard(intel)).isEqualTo(expected);
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
    public void shouldNotAcceptMaoDeOnzeWhenMyCardsAreWeak() {
        GameIntel intel = mock(GameIntel.class);

        TrucoCard card1 = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
        TrucoCard card2 = TrucoCard.of(CardRank.QUEEN,CardSuit.CLUBS);
        TrucoCard card3 = TrucoCard.of(CardRank.ACE,CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of (CardRank.ACE, CardSuit.SPADES);

        when(intel.getCards()).thenReturn(Arrays.asList(card1,card2,card3));
        when(intel.getVira()).thenReturn(vira);

        assertFalse(patoBot.checkIfAcceptMaoDeOnze(intel));
    }

    @Test
    @DisplayName("Should reject 'Mão de Onze' if own cards are weak and opponent's points >= 8")
    public void shouldRejectMaoDeOnzeIfOwnCardsWeakAndOpponentPointsAtLeastEight() {
        GameIntel intel = mock(GameIntel.class);
        TrucoCard card1 = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
        TrucoCard card2 = TrucoCard.of(CardRank.QUEEN,CardSuit.CLUBS);
        TrucoCard card3 = TrucoCard.of(CardRank.ACE,CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of (CardRank.ACE, CardSuit.SPADES);
        int opponentScore = 8;
        when(intel.getOpponentScore()).thenReturn(opponentScore);
        when(intel.getCards()).thenReturn(Arrays.asList(card1,card2,card3));
        when(intel.getVira()).thenReturn(vira);

        assertFalse(patoBot.checkIfAcceptMaoDeOnze(intel));
    }
    @Test
    @DisplayName("Should Reject 'Mão de Onze' if stronger card is a two")
    public void shouldRejectMaoDeOnzeIfStrongerCardIsTwo(){
        GameIntel intel = mock(GameIntel.class);
        TrucoCard card1 = TrucoCard.of(CardRank.TWO, CardSuit.SPADES);
        TrucoCard card2 = TrucoCard.of(CardRank.QUEEN,CardSuit.CLUBS);
        TrucoCard card3 = TrucoCard.of(CardRank.ACE,CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of (CardRank.SIX, CardSuit.SPADES);
        int opponentScore = 5;
        when(intel.getOpponentScore()).thenReturn(opponentScore);
        when(intel.getCards()).thenReturn(Arrays.asList(card1,card2,card3));
        when(intel.getVira()).thenReturn(vira);

        assertFalse(patoBot.checkIfAcceptMaoDeOnze(intel));
    }
    @Test
    @DisplayName("Should Reject 'Mão de Onze' if stronger card is a three")
    public void shouldRejectMaoDeOnzeIfStrongerCardIsThree(){
        GameIntel intel = mock(GameIntel.class);
        TrucoCard card1 = TrucoCard.of(CardRank.TWO, CardSuit.SPADES);
        TrucoCard card2 = TrucoCard.of(CardRank.QUEEN,CardSuit.CLUBS);
        TrucoCard card3 = TrucoCard.of(CardRank.ACE,CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of (CardRank.SIX, CardSuit.SPADES);
        int opponentScore = 1;
        when(intel.getOpponentScore()).thenReturn(opponentScore);
        when(intel.getCards()).thenReturn(Arrays.asList(card1,card2,card3));
        when(intel.getVira()).thenReturn(vira);

        assertFalse(patoBot.checkIfAcceptMaoDeOnze(intel));
    }
    @Test
    @DisplayName("Should raise when holding a three and a manilha as strong as or stronger than spade")
    public void shouldRaiseIfGotThreeAndManilhaEqualsToOrStrongerThanSpade(){
        GameIntel intel = mock(GameIntel.class);
        TrucoCard card1 = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
        TrucoCard card2 = TrucoCard.of(CardRank.QUEEN,CardSuit.CLUBS);
        TrucoCard card3 = TrucoCard.of(CardRank.ACE,CardSuit.SPADES);
        TrucoCard vira = TrucoCard.of (CardRank.KING, CardSuit.SPADES);
        when(intel.getCards()).thenReturn(Arrays.asList(card1,card2,card3));
        when(intel.getVira()).thenReturn(vira);
        assertTrue(patoBot.checkBigHand(intel));
    }

    @Test
    @DisplayName("Should not Raise if got only one 'Manilha' and no threes or twos")
    public void shouldNotRaiseIfGotOnlyOneManilhaAndNoThreesorTwos(){
        GameIntel intel = mock((GameIntel.class));
        TrucoCard card1 = TrucoCard.of(CardRank.FIVE, CardSuit.SPADES);
        TrucoCard card2 = TrucoCard.of(CardRank.QUEEN,CardSuit.CLUBS);
        TrucoCard card3 = TrucoCard.of(CardRank.ACE,CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of (CardRank.KING, CardSuit.SPADES);
        when(intel.getCards()).thenReturn(Arrays.asList(card1,card2,card3));
        when(intel.getVira()).thenReturn(vira);
        assertFalse(patoBot.checkBigHand(intel));
    }








}



