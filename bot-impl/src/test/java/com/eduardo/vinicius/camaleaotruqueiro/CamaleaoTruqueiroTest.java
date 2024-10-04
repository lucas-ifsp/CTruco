package com.eduardo.vinicius.camaleaotruqueiro;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.fail;

public class CamaleaoTruqueiroTest {

    private CamaleaoTruqueiro camaleao;

    @BeforeEach
    public void config() {
        camaleao = new CamaleaoTruqueiro();
    }

    //maior carta
    @Test
    @DisplayName("Should return the greater rank card")
    void shouldReturnTheGreaterRankCard() {
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);

        List<TrucoCard> cards = Arrays.asList(
                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
        );

        TrucoCard greaterCard = camaleao.getGreaterCard(cards, vira);
        assertEquals(greaterCard, TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));
    }


    //menor carta
    @Test
    @DisplayName("Should return the lowest card")
    void shouldReturnTheLowestCard() {
        TrucoCard vira = TrucoCard.of(CardRank.THREE,CardSuit.CLUBS);

        List<TrucoCard> cards = Arrays.asList(
                TrucoCard.of(CardRank.FOUR,CardSuit.CLUBS),
                TrucoCard.of(CardRank.QUEEN,CardSuit.CLUBS),
                TrucoCard.of(CardRank.THREE,CardSuit.CLUBS)
        );

        TrucoCard lowest = camaleao.getLowestCard(cards, vira);
        assertThat(lowest).isEqualTo(TrucoCard.of(CardRank.QUEEN,CardSuit.CLUBS));
    }


    @Test
    @DisplayName("Shoult return number of manilhas")
    void shoultReturnoNumberOfManilhas() {
        SoftAssertions softly = new SoftAssertions();
        //Given
        TrucoCard vira = TrucoCard.of(CardRank.THREE, CardSuit.HEARTS);

        List<TrucoCard> zeroManilhasCards = Arrays.asList(
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)
        );
        List<TrucoCard> oneManilhasCards = Arrays.asList(
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)
        );
        List<TrucoCard> twoManilhasCards = Arrays.asList(
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)
        );
        List<TrucoCard> threeManilhasCards = Arrays.asList(
                TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)
        );
        //Then
        softly.assertThat(camaleao.numberOfManilhas(zeroManilhasCards, vira)).as("Zero manilhas").isEqualTo(0);
        softly.assertThat(camaleao.numberOfManilhas(oneManilhasCards, vira)).as("One manilha").isEqualTo(1);
        softly.assertThat(camaleao.numberOfManilhas(twoManilhasCards, vira)).as("Two manilhas").isEqualTo(2);
        softly.assertThat(camaleao.numberOfManilhas(threeManilhasCards, vira)).as("Three manilhas").isEqualTo(3);
        softly.assertAll();
    }
    
    @Test
    @DisplayName("shouldReturnOneHighCards")
    void shouldReturnOneHighCard(){
        TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS);

        TrucoCard card1 = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
        TrucoCard card2 = TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS);
        TrucoCard card3 = TrucoCard.of(CardRank.THREE, CardSuit.CLUBS);
        List<TrucoCard> handCards = Arrays.asList(card1, card2, card3);

        int numberOfHighCard = camaleao.getNumberOfHighCards(handCards,vira);
        assertEquals(1, numberOfHighCard);
    }

    @Test
    @DisplayName("shouldReturnTwoHighCards")
    void shouldReturnTwoHighCards(){
        TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS);

        TrucoCard card1 = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);
        TrucoCard card2 = TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS);
        TrucoCard card3 = TrucoCard.of(CardRank.THREE, CardSuit.CLUBS);
        List<TrucoCard> handCards = Arrays.asList(card1, card2, card3);

        int numberOfHighCard = camaleao.getNumberOfHighCards(handCards,vira);
        assertEquals(2, numberOfHighCard);
    }

    @Test
    @DisplayName("shouldReturnThreeHighCards")
    void shouldReturnThreeHighCards(){
        TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS);

        TrucoCard card1 = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);
        TrucoCard card2 = TrucoCard.of(CardRank.JACK, CardSuit.HEARTS);
        TrucoCard card3 = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
        List<TrucoCard> handCards = Arrays.asList(card1, card2, card3);

        int numberOfHighCard = camaleao.getNumberOfHighCards(handCards,vira);
        assertEquals(3, numberOfHighCard);
    }

    @Test
    @DisplayName("shouldReturnIfTheBotIsWinning")
    void shouldReturnIfTheBotIsWinning(){
        boolean isWinning = camaleao.isWinning(9,9);
        assertTrue(isWinning);
    }

    @Test
    @DisplayName("Should return the chances of player has a absolut victory when he or she has best cards")
    void shouldReturnTheChancesOfPlayerHasAAbsolutVictoryWhenHeOrSheHasGreatestCards() {
        TrucoCard vira = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
        List<TrucoCard> playersHand = Arrays.asList(
                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
        );

        Float result = camaleao.getProbabilityOfAbsolutVictoryHand(playersHand, vira);
        assertThat(result).isEqualTo(1);
    }

    @Test
    @DisplayName("Should return the chances of player has a absolut victory when he or she has worst cards")
    void shouldReturnTheChancesOfPlayerHasAAbsolutVictoryWhenHeOrSheHasWorstestCards() {
        TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);
        List<TrucoCard> playersHand = Arrays.asList(
                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
        );

        Float result = camaleao.getProbabilityOfAbsolutVictoryHand(playersHand, vira);
        assertThat(result).isEqualTo(0);
    }

    @Test
    @DisplayName("Should return the chances of player has a absolut victory when he or she has median cards")
    void shouldReturnTheChancesOfPlayerHasAAbsolutVictoryHeOrSheHasMedianCards() {
        TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);
        List<TrucoCard> playersHand = Arrays.asList(
                TrucoCard.of(CardRank.JACK, CardSuit.CLUBS),
                TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS)
        );

        Float result = camaleao.getProbabilityOfAbsolutVictoryHand(playersHand, vira);
        assertThat(result).isEqualTo(0);
    }



    //estamos ganhando

    //estamos perdendo

}
