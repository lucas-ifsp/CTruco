package com.local.tatayrapha.leonardabot;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;
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
        final TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);
        final List<TrucoCard> trucoCardList = Arrays.asList(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS), TrucoCard.of(CardRank.JACK, CardSuit.HEARTS));
        when(intel.getVira()).thenReturn(vira);
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
        final TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);
        final List<TrucoCard> trucoCardList = Arrays.asList(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS), TrucoCard.of(CardRank.JACK, CardSuit.HEARTS));
        when(intel.getVira()).thenReturn(vira);
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
        final TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);
        final List<TrucoCard> trucoCardList = Arrays.asList(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.ACE, CardSuit.HEARTS));
        when(intel.getVira()).thenReturn(vira);
        when(intel.getCards()).thenReturn(trucoCardList);
        boolean response = leonardaBot.getMaoDeOnzeResponse(intel);
        assertThat(response).isTrue();
    }

    @Test
    @DisplayName("Should challenge the opponent with a 'Truco' request in the second round with a strong hand.")
    void challengeWithTrucoInSecondRoundWithStrongHand() {
        final TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);
        final List<TrucoCard> trucoCardList = Arrays.asList(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.ACE, CardSuit.HEARTS));
        when(intel.getVira()).thenReturn(vira);
        when(intel.getCards()).thenReturn(trucoCardList);
        when(intel.getRoundResults()).thenReturn(Arrays.asList(GameIntel.RoundResult.WON, GameIntel.RoundResult.LOST));
        when(intel.getScore()).thenReturn(10);
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
    void testShouldRespondProperlyMaoDeOnze() {
        when(intel.getOpponentScore()).thenReturn(11);
        boolean response = leonardaBot.getMaoDeOnzeResponse(intel);
        assertThat(response).isTrue();
    }

    @Test
    @DisplayName("Should not ask for raise in opponent's Mão de Onze scenario.")
    void testShouldNotRaiseInOpponentMaoDeOnze() {
        when(intel.getOpponentScore()).thenReturn(11);
        boolean response = leonardaBot.decideIfRaises(intel);
        assertThat(response).isFalse();
    }

    @Test
    @DisplayName("Should not ask for raise in Mão de Onze scenario.")
    void testShouldNotRaiseInMaoDeOnze() {
        when(intel.getScore()).thenReturn(11);
        boolean response = leonardaBot.decideIfRaises(intel);
        assertThat(response).isFalse();
    }

    @Test
    @DisplayName("Should ask for raise when having Higher Casal in hand.")
    void testShouldRaiseWhenHavingHigherCasal() {
        final TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);
        final List<TrucoCard> trucoCardList = Arrays.asList(TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS), TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS), TrucoCard.of(CardRank.ACE, CardSuit.HEARTS));
        when(intel.getCards()).thenReturn(trucoCardList);
        when(intel.getVira()).thenReturn(vira);
        when(intel.getScore()).thenReturn(2);
        when(intel.getOpponentScore()).thenReturn(6);
        boolean response = leonardaBot.decideIfRaises(intel);
        assertThat(response).isTrue();
    }

    @Test
    @DisplayName("Should ask for re-raise when having Higher Casal in hand.")
    void testShouldReRaiseWhenHavingHigherCasal() {
        final TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);
        final List<TrucoCard> trucoCardList = Arrays.asList(TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS), TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS), TrucoCard.of(CardRank.ACE, CardSuit.HEARTS));
        when(intel.getCards()).thenReturn(trucoCardList);
        when(intel.getVira()).thenReturn(vira);
        int response = leonardaBot.getRaiseResponse(intel);
        assertThat(response).isEqualTo(1);
    }

    @Test
    @DisplayName("Should play Mão de Onze when having Casal in hand.")
    void testShouldPlayMaoDeOnzeWithCasalInHand() {
        final TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);
        final List<TrucoCard> trucoCardList = Arrays.asList(TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS), TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS), TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
        when(intel.getCards()).thenReturn(trucoCardList);
        when(intel.getVira()).thenReturn(vira);
        boolean response = leonardaBot.getMaoDeOnzeResponse(intel);
        assertThat(response).isTrue();
    }

    @Test
    @DisplayName("Should not play Mão de Onze if having a weak hand.")
    void testShouldNotPlayMaoDeOnzeWithWeakHand() {
        final TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);
        final List<TrucoCard> trucoCardList = Arrays.asList(TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS), TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS), TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
        when(intel.getCards()).thenReturn(trucoCardList);
        when(intel.getVira()).thenReturn(vira);
        boolean response = leonardaBot.getMaoDeOnzeResponse(intel);
        assertThat(response).isFalse();
    }

    @Test
    @DisplayName("Should play the strongest card in hand if the first round was tied.")
    void testShouldPlayStrongestCardIfTied() {
        final TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);
        final List<TrucoCard> trucoCardList = Arrays.asList(TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS), TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS), TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS));
        when(intel.getCards()).thenReturn(trucoCardList);
        when(intel.getVira()).thenReturn(vira);
        when(intel.getOpponentCard()).thenReturn(Optional.ofNullable(TrucoCard.of(CardRank.FOUR, CardSuit.SPADES)));
        when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.DREW));
        CardToPlay response = leonardaBot.chooseCard(intel);
        assertThat(response.content()).isEqualTo(TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS));
    }

    @Test
    @DisplayName("Should not accept raise proposal if lost first round and opponent has score equal or higher than 9.")
    void testShouldNotAcceptRaiseProposal() {
        when(intel.getOpponentScore()).thenReturn(9);
        when(intel.getRoundResults()).thenReturn(Arrays.asList(GameIntel.RoundResult.LOST, GameIntel.RoundResult.WON));
        int response = leonardaBot.getRaiseResponse(intel);
        assertThat(response).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should raise winning first round and having a strong hand.")
    void testShouldRaiseWinningFirstRoundWithStrongHand() {
        final TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);
        final List<TrucoCard> trucoCardList = Arrays.asList(TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS), TrucoCard.of(CardRank.THREE, CardSuit.HEARTS), TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS));
        when(intel.getCards()).thenReturn(trucoCardList);
        when(intel.getVira()).thenReturn(vira);
        when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON));
        boolean response = leonardaBot.decideIfRaises(intel);
        assertThat(response).isTrue();
    }

    @Test
    @DisplayName("Should not fall for opponent's bluff in Mão de Onze scenario.")
    void notFallForOpponentBluffInMaoDeOnzeScenario() {
        final List<TrucoCard> trucoCardList = Arrays.asList(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));
        when(intel.getCards()).thenReturn(trucoCardList);
        CardToPlay chosenCard = leonardaBot.chooseCard(intel);
        TrucoCard playedCard = chosenCard.content();
        assertThat(playedCard.getRank()).isNotEqualTo(CardRank.SIX);
    }

    @Test
    @DisplayName("Should not accept the hand in a Mão de Onze scenario.")
    void testShouldNotRaiseInMaoDeOnzeWithScore11() {
        final TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);
        final List<TrucoCard> trucoCardList = Arrays.asList(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));
        when(intel.getVira()).thenReturn(vira);
        when(intel.getCards()).thenReturn(trucoCardList);
        when(intel.getScore()).thenReturn(11);
        int botResponse = leonardaBot.getRaiseResponse(intel);
        assertThat(botResponse).isEqualTo(0);
    }

    @Test
    @DisplayName("Should accept raise with a Manilha and card value > 4 when vira is not 3.")
    void testShouldAcceptRaiseWithManilhaAndCardValueGreaterThan4() {
        final TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS);
        final List<TrucoCard> trucoCardList = Arrays.asList(TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS), TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS), TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS));
        when(intel.getVira()).thenReturn(vira);
        when(intel.getCards()).thenReturn(trucoCardList);
        int botResponse = leonardaBot.getRaiseResponse(intel);
        assertThat(botResponse).isEqualTo(0);
    }

    @Test
    @DisplayName("Should raise if has a Lower Casal in hand and vira is not 4 or 7.")
    void testShouldRaiseWithLowerCasalAndNon4Non7Vira() {
        final TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);
        final List<TrucoCard> trucoCardList = Arrays.asList(TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS), TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS), TrucoCard.of(CardRank.KING, CardSuit.CLUBS));
        when(intel.getVira()).thenReturn(vira);
        when(intel.getCards()).thenReturn(trucoCardList);
        int botResponse = leonardaBot.getRaiseResponse(intel);
        assertThat(botResponse).isEqualTo(1);
    }

    @Test
    @DisplayName("Should bluff by playing a lower-ranked card when having a strong hand.")
    void testShouldBluffWithLowerRankedCard() {
        final TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);
        final List<TrucoCard> trucoCardList = Arrays.asList(TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS), TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS), TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));
        lenient().when(intel.getVira()).thenReturn(vira);
        lenient().when(intel.getCards()).thenReturn(trucoCardList);
        CardToPlay chosenCard = leonardaBot.chooseCard(intel);
        TrucoCard expectedLowerRankedCard = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);
        assertThat(chosenCard).isNotNull();
        assertThat(chosenCard.value()).isEqualTo(expectedLowerRankedCard);
    }

    @Test
    @DisplayName("Should rank the Manilha card correctly.")
    void rankManilhaCard() {
        LeonardaBot leonardaBot = new LeonardaBot();
        TrucoCard manilhaCard = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);
        leonardaBot.setManilhaCard(manilhaCard);
        List<TrucoCard> leonardaCards = Arrays.asList(TrucoCard.of(CardRank.TWO, CardSuit.SPADES), TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS), TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), manilhaCard);
        leonardaBot.setPlayerCards(leonardaCards);
        List<Integer> cardRanks = leonardaBot.calculateCardRanks();
        int manilhaRank = cardRanks.get(leonardaCards.indexOf(manilhaCard));
        assertEquals(1, manilhaRank);
    }
}