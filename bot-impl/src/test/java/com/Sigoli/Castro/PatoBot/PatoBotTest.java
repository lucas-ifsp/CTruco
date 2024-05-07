package com.Sigoli.Castro.PatoBot;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class PatoBotTest {
    PatoBot patoBot;

    @BeforeEach
    public void createPatoBot() {
        patoBot = new PatoBot();
    }

    GameIntel intel = mock(GameIntel.class);

    @Test
    @DisplayName("Should return true if opponent is first to play")
    void shoulReturnTrueIfOpponentIsFirstToPlay() {
        TrucoCard opponentCard = TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS);
        when(intel.getOpponentCard()).thenReturn(Optional.of(opponentCard));
        assertThat(patoBot.checkIfOpponentIsFirstToPlay(intel.getOpponentCard())).isTrue();
    }

    @Test
    @DisplayName("Should return false if opponent is not first to play")
    void shouldReturnFalseIfOpponentIsNotFirstToPlay() {
        when(intel.getOpponentCard()).thenReturn(Optional.empty());
        assertThat(patoBot.checkIfOpponentIsFirstToPlay(intel.getOpponentCard())).isFalse();
    }

    @Test
    @DisplayName("Should Return three if got three cards in hand")
    void shouldReturnThreeIfGotThreeCardsInHand() {
        int numberOfCards = 3;
        TrucoCard card1 = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);
        TrucoCard card2 = TrucoCard.of(CardRank.ACE, CardSuit.HEARTS);
        TrucoCard card3 = TrucoCard.of(CardRank.THREE, CardSuit.CLUBS);
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2, card3));
        assertThat(patoBot.getNumberOfCardsInHand(intel)).isEqualTo(numberOfCards);
    }

    @Test
    @DisplayName("Should Return one if got one card in hand")
    void shouldReturnOneIfGotOneCardInHand() {
        int numberOfCards = 1;
        TrucoCard card1 = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);
        when(intel.getCards()).thenReturn(Collections.singletonList(card1));
        assertThat(patoBot.getNumberOfCardsInHand(intel)).isEqualTo(numberOfCards);
    }

    @Test
    @DisplayName("Should play lowest winning card if can defeat opponent")
    public void shouldPlayLowestWinningCardIfCanDefeatOpponent() {
        TrucoCard card1 = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);
        TrucoCard card2 = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
        TrucoCard card3 = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES);
        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);
        TrucoCard expected = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2, card3));
        when(intel.getVira()).thenReturn(vira);
        when(intel.getOpponentCard()).thenReturn(Optional.ofNullable(opponentCard));
        assertThat(patoBot.attemptToBeatOpponentCard(intel)).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should play lowest winning card if can defeat opponent in second round")
    public void shouldPlayLowestWinningCardIfCanDefeatOpponentInSecondRound() {
        TrucoCard card2 = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
        TrucoCard card3 = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES);
        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);
        CardToPlay expected = CardToPlay.of(card2);
        when(intel.getCards()).thenReturn(Arrays.asList(card2, card3));
        when(intel.getVira()).thenReturn(vira);
        when(intel.getOpponentCard()).thenReturn(Optional.ofNullable(opponentCard));
        assertThat(patoBot.chooseCard(intel)).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should play a weaker card if unable to defeat opponent's card")
    public void shouldPlayWeakerCardIfCannotDefeatOpponent() {
        TrucoCard card1 = TrucoCard.of(CardRank.TWO, CardSuit.SPADES);
        TrucoCard card2 = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
        TrucoCard card3 = TrucoCard.of(CardRank.SIX, CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES);
        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);
        TrucoCard expected = TrucoCard.of(CardRank.SIX, CardSuit.CLUBS);
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2, card3));
        when(intel.getVira()).thenReturn(vira);
        when(intel.getOpponentCard()).thenReturn(Optional.ofNullable(opponentCard));
        assertThat(patoBot.attemptToBeatOpponentCard(intel)).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should play a strong card, excluding Zap")
    public void shouldPlayStrongCardExcludingZap() {
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
    @DisplayName("Should play a strong card, excluding Copas")
    public void shouldPlayStrongCardExcludingCopas() {
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
        TrucoCard card1 = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
        TrucoCard card2 = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);
        TrucoCard expected = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2));
        when(intel.getVira()).thenReturn(vira);
        when(intel.getOpponentCard()).thenReturn(Optional.ofNullable(opponentCard));
        assertThat(patoBot.attemptToBeatOpponentCard(intel)).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should play the lowest card which winning the round in the second round")
    public void shouldPlayTheLowestCardWhichWinsTheSecondRoundInTheSecondRound() {
        TrucoCard card1 = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
        TrucoCard card2 = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);
        CardToPlay expected = CardToPlay.of(card1);
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2));
        when(intel.getVira()).thenReturn(vira);
        when(intel.getOpponentCard()).thenReturn(Optional.ofNullable(opponentCard));
        assertThat(patoBot.chooseCard(intel)).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should play lowest winning card if can defeat opponent and is the second to Play on first Round")
    public void shouldPlayLowestWinningCardIfCanDefeatOpponentAndIsSecondToPlayonFirstRound() {
        TrucoCard card1 = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
        TrucoCard card2 = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
        TrucoCard card3 = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES);
        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);
        CardToPlay expected = CardToPlay.of(card1);
        when(intel.getVira()).thenReturn(vira);
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2, card3));
        when(intel.getOpponentCard()).thenReturn(Optional.ofNullable(opponentCard));
        assertThat(patoBot.chooseCard(intel)).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should play a weaker card if unable to defeat opponent's card and is second to Play First Round ")
    public void shouldPlayWeakerCardIfCannotDefeatOpponentAndIsSecondToPLayFirstRound() {
        TrucoCard card1 = TrucoCard.of(CardRank.TWO, CardSuit.SPADES);
        TrucoCard card2 = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
        TrucoCard card3 = TrucoCard.of(CardRank.SIX, CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES);
        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);
        CardToPlay expected = CardToPlay.of(card3);
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2, card3));
        when(intel.getVira()).thenReturn(vira);
        when(intel.getOpponentCard()).thenReturn(Optional.ofNullable(opponentCard));
        assertThat(patoBot.chooseCard(intel)).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should play a weaker card if unable to defeat opponent's card and is second to play the second round")
    public void shouldPlayAWeakerCardIfUnableToDeafeatOpponentsCardAndIsSecondToPlayTheSecondRound(){
        TrucoCard card1 = TrucoCard.of(CardRank.TWO, CardSuit.SPADES);
        TrucoCard card2 = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
        TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS);
        TrucoCard opponentCard = TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS);
        CardToPlay expected = CardToPlay.of(card2);
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2));
        when(intel.getVira()).thenReturn(vira);
        when(intel.getOpponentCard()).thenReturn(Optional.ofNullable(opponentCard));
        when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.LOST));
        assertThat(patoBot.chooseCard(intel)).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should play the stronger card excluding Zap if is First To Play First Round")
    public void shouldPlayTheStrongerCardExcludingZapIfIsFirstToPlayFirstRound() {
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
    public void shouldPlayStrongerCardInHandIfFirstToPlayFirstRound() {
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
    public void shouldPlayLowestToTrickOpponent() {
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
    @DisplayName("Should play middle card to take a strong card from opponent ")
    public void shouldPlayMiddleCardToCaptureStrongOpponentCard() {
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
    public void shouldAcceptMaoDeOnzeWhenMyCardsAreStronger() {
        TrucoCard card1 = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
        TrucoCard card2 = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);
        TrucoCard card3 = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2, card3));
        when(intel.getVira()).thenReturn(vira);
        assertTrue(patoBot.getMaoDeOnzeResponse(intel));
    }

    @Test
    @DisplayName("Should Accept Mao de Onze when Cards are okay and opponent score is <= 7")
    public void shouldAcceptMaoDeOnzeWhenCardsAreOkayAndOpponentScoreIsLessThanOrEqualToSeven() {
        TrucoCard card1 = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
        TrucoCard card2 = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
        TrucoCard card3 = TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS);
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
        int opponentScore = 6;
        when(intel.getOpponentScore()).thenReturn(opponentScore);
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2, card3));
        when(intel.getVira()).thenReturn(vira);
        assertTrue(patoBot.getMaoDeOnzeResponse(intel));
    }


    @Test
    @DisplayName("Should not accept Mão de Onze when my cards are weak")
    public void shouldNotAcceptMaoDeOnzeWhenMyCardsAreWeak() {
        TrucoCard card1 = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
        TrucoCard card2 = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);
        TrucoCard card3 = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2, card3));
        when(intel.getVira()).thenReturn(vira);
        assertFalse(patoBot.getMaoDeOnzeResponse(intel));
    }

    @Test
    @DisplayName("Should reject 'Mão de Onze' if own cards are weak and opponent's points >= 8")
    public void shouldRejectMaoDeOnzeIfOwnCardsWeakAndOpponentPointsAtLeastEight() {
        TrucoCard card1 = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
        TrucoCard card2 = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);
        TrucoCard card3 = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
        int opponentScore = 8;
        when(intel.getOpponentScore()).thenReturn(opponentScore);
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2, card3));
        when(intel.getVira()).thenReturn(vira);
        assertFalse(patoBot.getMaoDeOnzeResponse(intel));
    }

    @Test
    @DisplayName("Should Reject 'Mão de Onze' if stronger card is a two")
    public void shouldRejectMaoDeOnzeIfStrongerCardIsTwo() {
        TrucoCard card1 = TrucoCard.of(CardRank.TWO, CardSuit.SPADES);
        TrucoCard card2 = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);
        TrucoCard card3 = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.SPADES);
        int opponentScore = 5;
        when(intel.getOpponentScore()).thenReturn(opponentScore);
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2, card3));
        when(intel.getVira()).thenReturn(vira);
        assertFalse(patoBot.getMaoDeOnzeResponse(intel));
    }

    @Test
    @DisplayName("Should Reject 'Mão de Onze' if stronger card is a three")
    public void shouldRejectMaoDeOnzeIfStrongerCardIsThree() {
        TrucoCard card1 = TrucoCard.of(CardRank.TWO, CardSuit.SPADES);
        TrucoCard card2 = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);
        TrucoCard card3 = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.SPADES);
        int opponentScore = 1;
        when(intel.getOpponentScore()).thenReturn(opponentScore);
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2, card3));
        when(intel.getVira()).thenReturn(vira);
        assertFalse(patoBot.getMaoDeOnzeResponse(intel));
    }

    @Test
    @DisplayName("Should raise when holding a three and a manilha as strong as or stronger than spade")
    public void shouldRaiseIfGotThreeAndManilhaEqualsToOrStrongerThanSpade() {
        TrucoCard card1 = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
        TrucoCard card2 = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);
        TrucoCard card3 = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
        TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2, card3));
        when(intel.getVira()).thenReturn(vira);
        assertTrue(patoBot.checkIfRaiseGame(intel));
    }

    @Test
    @DisplayName("Should Raise when holding a Two and Manilha")
    public void shouldRaiseWhenHoldingTwoAndManilha() {
        GameIntel intel = mock((GameIntel.class));
        TrucoCard card1 = TrucoCard.of(CardRank.FIVE, CardSuit.SPADES);
        TrucoCard card2 = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);
        TrucoCard card3 = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2, card3));
        when(intel.getVira()).thenReturn(vira);
        assertTrue(patoBot.checkIfRaiseGame(intel));
    }

    @Test
    @DisplayName("Should Raise when holds manilha on last hand")
    public void shouldRaiseWhenHoldsManilhaonLastHand() {
        GameIntel intel = mock((GameIntel.class));
        TrucoCard card1 = TrucoCard.of(CardRank.ACE, CardSuit.HEARTS);
        TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
        when(intel.getCards()).thenReturn(Collections.singletonList(card1));
        when(intel.getVira()).thenReturn(vira);
        assertTrue(patoBot.checkIfRaiseGame(intel));
    }

    @Test
    @DisplayName("Should not Raise if got only one 'Manilha' and no threes or twos")
    public void shouldNotRaiseIfGotOnlyOneManilhaAndNoThreesorTwos() {
        GameIntel intel = mock((GameIntel.class));
        TrucoCard card1 = TrucoCard.of(CardRank.FIVE, CardSuit.SPADES);
        TrucoCard card2 = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);
        TrucoCard card3 = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2, card3));
        when(intel.getVira()).thenReturn(vira);
        assertFalse(patoBot.checkIfRaiseGame(intel));
    }

    @Test
    @DisplayName("Should not raise if holds only bad cards")
    public void shouldNotRaiseIfHoldsOnlyBadCards() {
        GameIntel intel = mock((GameIntel.class));
        TrucoCard card1 = TrucoCard.of(CardRank.FIVE, CardSuit.SPADES);
        TrucoCard card2 = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2));
        when(intel.getVira()).thenReturn(vira);
        assertFalse(patoBot.checkIfRaiseGame(intel));
    }

    @Test
    @DisplayName("Should raise game if I've won the first round and have strong cards")
    public void shouldRaiseGameIfIveWonTheFirstRoundAndHaveStrongCards() {
        TrucoCard card1 = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
        TrucoCard card2 = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2));
        when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON));
        when(intel.getVira()).thenReturn(vira);
        assertTrue(patoBot.checkIfRaiseGame(intel));
    }

    @Test
    @DisplayName("Should not raise if I lost the first round")
    public void shouldNotRaiseIfIsFirstRound() {
        when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.LOST));
        assertFalse(patoBot.decideIfRaises(intel));
    }

    @Test
    @DisplayName("Should not Raise if is playing Mao de Onze")
    public void shouldNotRaiseIfIsPlayingMaoDeOnze() {
        int playerPoints = 11;
        when(intel.getScore()).thenReturn(playerPoints);
        assertFalse(patoBot.decideIfRaises(intel));
    }

    @Test
    @DisplayName("Should not Raise if opponent is playing Mao de Onze")
    public void shouldNotRaiseIfOpponentIsPlayingMaoDeOnze() {
        int opponentScore = 11;
        when(intel.getOpponentScore()).thenReturn(opponentScore);
        assertFalse(patoBot.decideIfRaises(intel));
    }

    @Test
    @DisplayName("Should Raise if 'Truco' is called in first round")
    public void shouldRaiseIfTrucoIsCalledInFirstRound() {
        TrucoCard card1 = TrucoCard.of(CardRank.FIVE, CardSuit.SPADES);
        TrucoCard card2 = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);
        TrucoCard card3 = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2, card3));
        assertThat(patoBot.getRaiseResponse(intel)).isEqualTo(1);
    }

    @Test
    @DisplayName("Should Raise if hold only good cards ")
    public void shouldRaiseifholdsOnlyGoodcards() {
        TrucoCard card1 = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
        TrucoCard card2 = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);
        TrucoCard card3 = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS);
        when(intel.getVira()).thenReturn(vira);
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2, card3));
        assertTrue(patoBot.decideIfRaises(intel));
    }

    @Test
    @DisplayName("Should Raise if holds middle cards and won first round ")
    public void shouldRaiseIfHoldsMiddleCardsAndWonFirstRound() {
        TrucoCard card1 = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
        TrucoCard card2 = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS);
        when(intel.getVira()).thenReturn(vira);
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2));
        when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON));
        assertTrue(patoBot.decideIfRaises(intel));

    }

    @Test
    @DisplayName("Should raise if I've won the first round, lost second round and have a 'Manilha'")
    public void shouldRaiseIfIvieWonTheFirstRoundLostSecondRoundAndHaveAManilha() {
        TrucoCard card1 = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);
        TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS);
        when(intel.getVira()).thenReturn(vira);
        when(intel.getCards()).thenReturn(Collections.singletonList(card1));
        when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON, GameIntel.RoundResult.LOST));
        assertTrue(patoBot.decideIfRaises(intel));
    }

    @Test
    @DisplayName("Should play lowest card if can't defeat opponent card being second to play on second Round")
    public void shouldPlayLowestCardWhenCannotDefeatOpponentAsSecondPlayerInSecondRound() {
        TrucoCard card1 = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
        TrucoCard card2 = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS);
        TrucoCard opponentCard = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
        CardToPlay expected = CardToPlay.of(card2);
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2));
        when(intel.getVira()).thenReturn(vira);
        when(intel.getOpponentCard()).thenReturn(Optional.ofNullable(opponentCard));
        assertThat(patoBot.chooseCard(intel)).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should not Raise on first round if highest card is a Three")
    void shouldNotRaiseOnFirstRoundIfHighestCardIsThree() {
        TrucoCard card1 = TrucoCard.of(CardRank.THREE, CardSuit.HEARTS);
        TrucoCard card2 = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);
        TrucoCard card3 = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);
        TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES);
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2, card3));
        when(intel.getVira()).thenReturn(vira);
        when(intel.getScore()).thenReturn(0);  // O score inicial do jogo
        assertFalse(patoBot.decideIfRaises(intel));
    }

    @Test
    @DisplayName("Should raise if opponents request 'Truco' and I've got 'Zap' and win the first round")
    public void shouldRaiseIfOpponentsRequestTrucoAndIveGotZapAndWinFirstRound() {
        TrucoCard card1 = TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS);
        TrucoCard card2 = TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS);
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);
        when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON));
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2));
        when(intel.getVira()).thenReturn(vira);
        assertThat(patoBot.getRaiseResponse(intel)).isEqualTo(1);
    }

    @Test
    @DisplayName("Should accept 'Truco' if i have a Three and 'Manilha' in hand and win the first round")
    public void shouldAcceptTrucoIfIHaveAThreeAndManilhaInHandAndWinTheFirstRound() {
        TrucoCard card1 = TrucoCard.of(CardRank.THREE, CardSuit.CLUBS);
        TrucoCard card2 = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);
        when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON));
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2));
        when(intel.getVira()).thenReturn(vira);
        assertThat(patoBot.getRaiseResponse(intel)).isEqualTo(0);
    }

    @Test
    @DisplayName("Should decline 'Truco' if i have a Three and 'Manilha' in hand and lost the first round")
    public void shouldDeclineTrucoIfIHaveAThreeAndManilhaInHandAndLostTheFirstRound() {
        TrucoCard card1 = TrucoCard.of(CardRank.THREE, CardSuit.CLUBS);
        TrucoCard card2 = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);
        when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.LOST));
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2));
        when(intel.getVira()).thenReturn(vira);
        assertThat(patoBot.getRaiseResponse(intel)).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should refuse 'Truco' if my hand is weak and lost first round")
    public void shouldRefuseTrucoIfMyHandIsWeakAndLostFirstRound() {
        TrucoCard card1 = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);
        TrucoCard card2 = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);
        when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.LOST));
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2));
        when(intel.getVira()).thenReturn(vira);
        assertThat(patoBot.getRaiseResponse(intel)).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should decline 'Truco' if my cards are weak and i won the first round")
    public void shouldRefuseTrucoIfMyHandIsWeakAndIWinFirstRound() {
        TrucoCard card1 = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);
        TrucoCard card2 = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.SPADES);
        when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON));
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2));
        when(intel.getVira()).thenReturn(vira);
        assertThat(patoBot.getRaiseResponse(intel)).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should decline 'Truco' if i have middle cards and lost the first round")
    public void shouldAcceptTrucoIfIHaveOurosAndLostTheFirstRound() {
        TrucoCard card1 = TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS);
        TrucoCard card2 = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
        when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.LOST));
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2));
        when(intel.getVira()).thenReturn(vira);
        assertThat(patoBot.getRaiseResponse(intel)).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should decline 'Truco' if i don't have cards in hand and opponent win one round")
    public void shouldDeclineTrucoIfIDontHaveCardsInHandAndOpponentWinOneRound() {
        when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.LOST));
        when(intel.getCards()).thenReturn(List.of());
        assertThat(patoBot.getRaiseResponse(intel)).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should accept 'Truco' if i have strong cards in hand and win the first round")
    public void shouldAcceptTrucoIfIHaveStrongCardsInHandAndWinTheFirstRound() {
        TrucoCard card1 = TrucoCard.of(CardRank.THREE, CardSuit.CLUBS);
        TrucoCard card2 = TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS);
        TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.SPADES);
        when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON));
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2));
        when(intel.getVira()).thenReturn(vira);
        assertThat(patoBot.getRaiseResponse(intel)).isEqualTo(0);
    }

    @Test
    @DisplayName("Should decline 'Truco' if i only have only 'Copas' and lost the first round")
    public void shouldDeclineTrucoIfIOnlyHaveCopasAndLostTheFirstRound() {
        TrucoCard card1 = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);
        TrucoCard card2 = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);
        TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.SPADES);
        when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.LOST));
        when(intel.getCards()).thenReturn(Arrays.asList(card1, card2));
        when(intel.getVira()).thenReturn(vira);
        assertThat(patoBot.getRaiseResponse(intel)).isEqualTo(-1);
    }
}
