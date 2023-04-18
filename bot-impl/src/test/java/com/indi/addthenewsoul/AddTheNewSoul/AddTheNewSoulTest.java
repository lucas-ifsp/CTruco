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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddTheNewSoulTest {
    private AddTheNewSoul addTheNewSoul;
    List<TrucoCard> botCards = Arrays.asList(
            TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
            TrucoCard.of(CardRank.SIX, CardSuit.SPADES),
            TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES));
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
    public void shouldPlayTheLowestCardAccordingToTheSuit(){
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
    public void shouldPlayManilhaStrongerThanThePlayers(){
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
    public void shouldPlayTheSmallestManilhaThatBeatsTheOpponent(){

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
    @DisplayName("Should play the smallest card even if has manilha when no cards have been played")
    void shouldPlayTheSmallestCardEvenIfHasManilhaWhenNoCardsHaveBeenPlayed(){
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), List.of(vira), vira, 1)
                .botInfo(botCards, 9)
                .opponentScore(4);
        assertEquals(CardRank.FIVE, addTheNewSoul.chooseCard(stepBuilder.build()).content().getRank());
    }
}
