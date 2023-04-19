package com.indi.addthenewsoul.AddTheNewSoul;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.indi.impl.addthenewsoul.AddTheNewSoul;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AddTheNewSoulTest {
    private AddTheNewSoul addTheNewSoul;
    List<TrucoCard> botCards = Arrays.asList(
            TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
            TrucoCard.of(CardRank.SIX, CardSuit.SPADES),
            TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES));
    TrucoCard opponentCard = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
    private GameIntel.StepBuilder stepBuilder;
    @BeforeEach
    public void setUp(){
        addTheNewSoul = new AddTheNewSoul();
    }

    @Test
    @DisplayName("Should play the lowest card to win")
    public void shouldPlayTheLowestCardToWinTest(){
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);
        TrucoCard opponentCard = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);
        List<TrucoCard> openCards = Arrays.asList(
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS));

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 1)
                .botInfo(botCards, 9)
                .opponentScore(3)
                .opponentCard(opponentCard);
        assertEquals(CardRank.FIVE, addTheNewSoul.chooseCard(stepBuilder.build()).content().getRank());
    }
    @Test
    @DisplayName("It is not possible to win, discard the lowest card.")
    public void itIsNotPossibleToWinDiscardTheLowestCardTest(){
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);
        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS);

        List<TrucoCard> openCards = Arrays.asList(
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS));

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 1)
                .botInfo(botCards, 9)
                .opponentScore(3)
                .opponentCard(opponentCard);

        assertEquals(CardRank.FIVE, addTheNewSoul.chooseCard(stepBuilder.build()).content().getRank());
    }

    @Test
    @DisplayName("Should play the lowest card according to the suit")
    public void shouldPlayTheLowestCardAccordingToTheSuitTest(){
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);
        List<TrucoCard> openCards = Arrays.asList(
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                TrucoCard.closed());

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                .botInfo(botCards, 9)
                .opponentScore(4)
                .opponentCard(TrucoCard.closed());
        assertEquals(CardSuit.SPADES, addTheNewSoul.chooseCard(stepBuilder.build()).content().getSuit());
    }

    @Test
    @DisplayName("Should play a manilha stronger than the player's")
    public void shouldPlayManilhaStrongerThanThePlayersTest(){
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
        TrucoCard opponentCard = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> opensCards = Arrays.asList(
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), opensCards, vira, 1)
                .botInfo(botCards, 9)
                .opponentScore(4)
                .opponentCard(opponentCard);
        assertEquals(CardRank.SIX, addTheNewSoul.chooseCard(stepBuilder.build()).content().getRank());
    }
    @Test
    @DisplayName("Should play the smallest manilha that beats the opponent")
    public void shouldPlayTheSmallestManilhaThatBeatsTheOpponentTest(){

        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
        List<TrucoCard> opensCards = Arrays.asList(
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        List<TrucoCard> botCards = Arrays.asList(
                TrucoCard.of(CardRank.SIX, CardSuit.CLUBS),
                TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), opensCards, vira, 1)
                .botInfo(botCards, 9)
                .opponentScore(4)
                .opponentCard(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));
        assertEquals(CardSuit.HEARTS, addTheNewSoul.chooseCard(stepBuilder.build()).content().getSuit());
    }

    @Test
    @DisplayName("Should play the smallest card even if has manilha that is not diamond when no cards have been played")
    void shouldPlayTheSmallestCardEvenIfHasManilhaThatIsNotDiamondWhenNoCardsHaveBeenPlayedTest(){
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), List.of(vira), vira, 1)
                .botInfo(botCards, 9)
                .opponentScore(4);
        assertEquals(CardRank.FIVE, addTheNewSoul.chooseCard(stepBuilder.build()).content().getRank());
    }

    @Test
    @DisplayName("Should always play the Diamonds manilha when in hand")
    void shouldAlwaysPlayTheDiamondsManilhaWhenInHandTest(){
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
        List<TrucoCard> botCards = Arrays.asList(
                TrucoCard.of(CardRank.SIX, CardSuit.CLUBS),
                TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), List.of(vira), vira, 1)
                .botInfo(botCards, 9)
                .opponentScore(4)
                .opponentCard(TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS));
        assertEquals(CardSuit.DIAMONDS, addTheNewSoul.chooseCard(stepBuilder.build()).content().getSuit());
    }
    
    @Test
    @DisplayName("Should discard when the opponent plays a card that Adenilso cant beat ")
    void shouldDiscardWhenTheOpponentPlaysACardThatAdenilsoCantBeatTest(){
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
        List<TrucoCard> openCards = Arrays.asList(
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        List<TrucoCard> botCards = Arrays.asList(
                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS));

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                .botInfo(botCards, 9)
                .opponentScore(4)
                .opponentCard(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));
        assertTrue(addTheNewSoul.chooseCard(stepBuilder.build()).isDiscard());
    }

    @Test
    @DisplayName("Should play the lowest card on hand opened in the first round if cannot beat the opponent ")
    void shouldPlayTheLowestCardOnHandOpenedInTheFirstRoundIfCannotBeatTheOpponentTest(){
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
        List<TrucoCard> openCards = Arrays.asList(
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        List<TrucoCard> botCards = Arrays.asList(
                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                .botInfo(botCards, 9)
                .opponentScore(4)
                .opponentCard(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));
        assertEquals(CardRank.FOUR, addTheNewSoul.chooseCard(stepBuilder.build()).content().getRank());
    }
    
    @Test
    @DisplayName("Should play attack card if has two at least two attack cards in hand")
    void shouldPlayAttackCardIfHasTwoAtLeastTwoAttackCardsInHandTest(){
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
        List<TrucoCard> botCards = Arrays.asList(
                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), List.of(vira), vira, 1)
                .botInfo(botCards, 9)
                .opponentScore(4)
                .opponentCard(TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS));
        assertEquals(CardSuit.DIAMONDS, addTheNewSoul.chooseCard(stepBuilder.build()).content().getSuit());
    }

    @Test
    @DisplayName("Should play the highest card when game is amarrado")
    void shouldPlayTheHighestCardWhenGameIsAmarradoTest(){
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
        List<TrucoCard> openCards = Arrays.asList(
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS),
                TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS));

        List<TrucoCard> botCards = Arrays.asList(
                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 1)
                .botInfo(botCards, 9)
                .opponentScore(4)
                .opponentCard(TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS));
        assertEquals(CardRank.THREE, addTheNewSoul.chooseCard(stepBuilder.build()).content().getRank());
    }

    @Test
    @DisplayName("Should always amarrar when its possible")
    void shouldAlwaysAmarrarWhenItsPossibleTest(){
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
        List<TrucoCard> openCards = Arrays.asList(
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS));

        List<TrucoCard> botCards = Arrays.asList(
                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), openCards, vira, 1)
                .botInfo(botCards, 9)
                .opponentScore(4)
                .opponentCard(TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS));
        assertEquals(CardRank.SEVEN, addTheNewSoul.chooseCard(stepBuilder.build()).content().getRank());

    }


    // FIM DOS CASOS DE TESTE DO METODO CHOOSECARD









    // INICIO DOS CASOS DE TESTE DO METODO DECIDEIFRAISES
    @Test
    @DisplayName("Should not rise if the opponent has more than 8 points")
    public void shouldNotRiseIfTheOpponentHasMoreThan8pointsTest(){
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);
        List<TrucoCard> openCards = Arrays.asList(
                vira,
                opponentCard);
        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                .botInfo(botCards, 9)
                .opponentScore(9)
                .opponentCard(opponentCard);
        assertFalse(addTheNewSoul.decideIfRaises(stepBuilder.build()));
    }
    @Test
    @DisplayName("Should rise if has good cards and opponent less than 8 points")
    public void shouldRiseIfHasGoodCardsAndOpponentLessThab8pointsTest(){
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);
        List<TrucoCard> openCards = Arrays.asList(
                vira,
                opponentCard);
        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                .botInfo(botCards, 9)
                .opponentScore(8)
                .opponentCard(opponentCard);
        assertTrue(addTheNewSoul.decideIfRaises(stepBuilder.build()));
    }

    @Test
    @DisplayName("Should not rise if dont have good cards")
    public void shouldNotRiseIfDontHaveGoodCardsTest(){
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

        List<TrucoCard> openCards = Arrays.asList(
                TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                opponentCard);

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                .botInfo(botCards, 3)
                .opponentScore(3)
                .opponentCard(opponentCard);
        assertFalse(addTheNewSoul.decideIfRaises(stepBuilder.build()));

    }

    @Test
    @DisplayName("Should raise if it has manilha and card with relative value greater than 4")
    public void shouldRaiseIfItHasManilhaAndCardWithRelativeValueGraterThan4Test(){
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);
        List<TrucoCard> openCards = Arrays.asList(
                vira,
                opponentCard);
        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                .botInfo(botCards, 9)
                .opponentScore(4)
                .opponentCard(opponentCard);
        assertTrue(addTheNewSoul.decideIfRaises(stepBuilder.build()));
    }

    @Test
    @DisplayName("Should not raise if only have manilha")
    public void shouldNotRaiseIfOnlyHaveManilhaTest(){
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

        List<TrucoCard> openCards = Arrays.asList(
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS));
        List<TrucoCard> cards = Arrays.asList(
                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                TrucoCard.of(CardRank.SIX, CardSuit.CLUBS));
        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                .botInfo(cards, 7)
                .opponentScore(8)
                .opponentCard(opponentCard);
        assertFalse(addTheNewSoul.decideIfRaises(stepBuilder.build()));
    }
    @Test
    @DisplayName("Should not raise if have only card good")
    public void shouldNotRaiseIfHaveOnlyCardGoodTest(){
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

        List<TrucoCard> openCards = Arrays.asList(
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS));
        List<TrucoCard> cards = Arrays.asList(
                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                TrucoCard.of(CardRank.SIX, CardSuit.CLUBS));
        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                .botInfo(cards, 4)
                .opponentScore(6)
                .opponentCard(opponentCard);
        assertFalse(addTheNewSoul.decideIfRaises(stepBuilder.build()));
    }
    @Test
    @DisplayName("Should increase if sum of card values is above average")
    public void shouldIncreaseIfSumOfCardValuesIsAboveAverageTest(){
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> openCards = Arrays.asList(
                TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS));
        List<TrucoCard> cards = Arrays.asList(
                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS));

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                .botInfo(cards, 3)
                .opponentScore(6)
                .opponentCard(opponentCard);
        assertTrue(addTheNewSoul.decideIfRaises(stepBuilder.build()));
    }

    @Test
    @DisplayName("Should increase have just manilhas")
    public void shouldIncreaseHaveJustManilhasTest(){
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

        List<TrucoCard> openCards = Arrays.asList(
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                opponentCard);
        List<TrucoCard> botCards = Arrays.asList(
                TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                TrucoCard.of(CardRank.TWO, CardSuit.SPADES));

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                .botInfo(botCards, 3)
                .opponentScore(2)
                .opponentCard(opponentCard);
        assertTrue(addTheNewSoul.decideIfRaises(stepBuilder.build()));
    }
    @Test
    @DisplayName("Should increase if winning by more than 3 points independent of cards")
    public void shouldIncreaseIfWinningByMoreThan3PointsIndependentOfCardsTest(){
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

        List<TrucoCard> openCards = Collections.singletonList(
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));


        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                .botInfo(botCards, 6)
                .opponentScore(2);
        assertTrue(addTheNewSoul.decideIfRaises(stepBuilder.build()));
    }
    @Test
    @DisplayName("Should not raise if have 11 points and opponent has 10")
    public void shouldNotRaiseIfHave11PointsAndOpponentHas10Test() {
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

        List<TrucoCard> openCards = Arrays.asList(
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                opponentCard);
        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                .botInfo(botCards, 11)
                .opponentScore(10)
                .opponentCard(opponentCard);
        assertFalse(addTheNewSoul.decideIfRaises(stepBuilder.build()));
    }
    @Test
    @DisplayName("Should not raise if all cards are below seven")
    public void shouldNotRaiseIfAllCardsAreBelowSevenTest(){
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

        List<TrucoCard> openCards = Arrays.asList(
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                opponentCard);


        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                .botInfo(botCards, 3)
                .opponentScore(2)
                .opponentCard(opponentCard);
        assertFalse(addTheNewSoul.decideIfRaises(stepBuilder.build()));
    }
    @Test
    @DisplayName("Should not raise if opponent has a better card than the best card")
    public void shouldNotRaiseIfOpponentHasABetterCardThanTheBestCardTest(){
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

        List<TrucoCard> openCards = Arrays.asList(
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS));


        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                .botInfo(botCards, 3)
                .opponentScore(2)
                .opponentCard(TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS));
        assertFalse(addTheNewSoul.decideIfRaises(stepBuilder.build()));
    }

    // FIM DOS CASOS DE TESTE DO METODO DECIDEIFRAISES


}
