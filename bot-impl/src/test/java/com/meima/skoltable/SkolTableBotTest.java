package com.meima.skoltable;

import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SkolTableBotTest {

    private SkolTable skolTable;

    @BeforeEach
    public void setUp(){
        skolTable = new SkolTable();
    }


    @Test
    @DisplayName("Should play strongest card in first round if it's stronger than opponent's")
    void shouldPlayStrongestCardInFirstRoundIfStrongerThanOpponents() {

        List<TrucoCard> botCards = List.of(TrucoCard.of(ACE, SPADES), TrucoCard.of(THREE, CLUBS), TrucoCard.of(FOUR, HEARTS));
        TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        TrucoCard opponentCard = TrucoCard.of(ACE, CLUBS);

        List<TrucoCard> openCards = List.of(vira);
        List<GameIntel.RoundResult> results = List.of();

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(results, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);


        assertEquals(TrucoCard.of(FOUR, HEARTS), skolTable.chooseCard(stepBuilder.build()).content());
    }

    @Test
    @DisplayName("Should play strongest card in first round without opponent's card")
    void shouldPlayStrongestCardInFirstRoundWithoutOpponentsCard() {
        List<TrucoCard> botCards = List.of(TrucoCard.of(ACE, SPADES), TrucoCard.of(THREE, CLUBS), TrucoCard.of(FOUR, HEARTS));
        TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        List<TrucoCard> openCards = List.of(vira);
        List<GameIntel.RoundResult> results = List.of();

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(results, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0);

        assertEquals(TrucoCard.of(FOUR, HEARTS), skolTable.chooseCard(stepBuilder.build()).content());
    }
    @Test
    @DisplayName("Should play weakest card in first round when opponent's card is stronger")
    void shouldPlayWeakestCardInFirstRoundWhenOpponentsCardIsStronger() {
        List<TrucoCard> botCards = List.of(TrucoCard.of(THREE, SPADES), TrucoCard.of(TWO, CLUBS), TrucoCard.of(FOUR, HEARTS));
        TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        TrucoCard opponentCard = TrucoCard.of(FOUR, CLUBS);

        List<TrucoCard> openCards = List.of(vira, opponentCard);
        List<GameIntel.RoundResult> results = List.of();

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(results, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        assertEquals(TrucoCard.of(TWO, CLUBS), skolTable.chooseCard(stepBuilder.build()).content());
    }

    @Test
    @DisplayName("Should not rise if lost first round")
    void ShouldNotRisIfLostFirstRound(){
        List<GameIntel.RoundResult> rounds = List.of(GameIntel.RoundResult.LOST);
        List<TrucoCard> openCards = List.of();

        TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        List<TrucoCard> botCards = List.of(TrucoCard.of(ACE, SPADES), TrucoCard.of(THREE, CLUBS));

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, openCards, vira, 3)
                .botInfo(botCards, 0)
                .opponentScore(0);
        assertThat(skolTable.getRaiseResponse(stepBuilder.build())).isNegative();
    }

    @Test
    @DisplayName("Should not rise if don't have good cards")
    void shouldNotRiseIfDontHaveGoodCards() {
        List<GameIntel.RoundResult> rounds = List.of();
        TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        List<TrucoCard> openCards = List.of(vira);
        TrucoCard opponentCard = TrucoCard.of(FOUR, CLUBS);

        List<TrucoCard> botCards = List.of(TrucoCard.of(TWO, SPADES), TrucoCard.of(FIVE, CLUBS), TrucoCard.of(SIX, SPADES));

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0);

        assertEquals(-1, skolTable.getRaiseResponse(stepBuilder.build()));
    }

    @Test
    @DisplayName("Should accept rise if won the first round and have strong card in hand")
    void ShouldRiseIfWonFirstRoundAndHaveStrongCard(){
        List<GameIntel.RoundResult> rounds = List.of(GameIntel.RoundResult.WON);
        List<TrucoCard> openCards = List.of();

        TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        List<TrucoCard> botCards = List.of(TrucoCard.of(FOUR, HEARTS), TrucoCard.of(ACE, CLUBS));

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, openCards, vira, 3)
                .botInfo(botCards, 0)
                .opponentScore(0);
        assertThat(skolTable.getRaiseResponse(stepBuilder.build())).isZero();
    }

    @Test
    @DisplayName("Should rise again if won the first round and have strong card in hand")
    void ShouldRiseAgainIfWonFirstRoundAndHaveStrongCard(){
        List<GameIntel.RoundResult> rounds = List.of(GameIntel.RoundResult.WON);
        List<TrucoCard> openCards = List.of();

        TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        List<TrucoCard> botCards = List.of(TrucoCard.of(FOUR, HEARTS), TrucoCard.of(FOUR, CLUBS));

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, openCards, vira, 3)
                .botInfo(botCards, 0)
                .opponentScore(0);
        assertThat(skolTable.getRaiseResponse(stepBuilder.build())).isPositive();
    }

    @Test
    @DisplayName("Should rise again if have very strong cards in hand")
    void ShouldRiseAgainIfHaveVeryStrongCards(){
        List<TrucoCard> openCards = List.of();

        TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        List<TrucoCard> botCards = List.of(TrucoCard.of(FOUR, HEARTS), TrucoCard.of(KING, CLUBS), TrucoCard.of(TWO, HEARTS));

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), openCards, vira, 3)
                .botInfo(botCards, 0)
                .opponentScore(0);
        assertThat(skolTable.getRaiseResponse(stepBuilder.build())).isPositive();
    }

    @Test
    @DisplayName("Should accept if have strong card in hand")
    void ShouldAcceptIfHaveStrongCardInHand() {
        List<GameIntel.RoundResult> rounds = List.of();
        TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        List<TrucoCard> opponentCard = List.of(TrucoCard.of(FOUR, DIAMONDS));

        List<TrucoCard> strongHand = List.of(TrucoCard.of(ACE, SPADES), TrucoCard.of(THREE, SPADES), TrucoCard.of(QUEEN, SPADES));

        GameIntel.StepBuilder strongHandBuilder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, opponentCard, vira, 1)
                .botInfo(strongHand, 0)
                .opponentScore(0);
        assertThat(skolTable.getRaiseResponse(strongHandBuilder.build())).isEqualTo(0);
    }

    @Test
    @DisplayName("Should not accept rise again if have weak cards in hand")
    void ShouldNotAcceptRiseIfHaveWeakCards(){
        List<TrucoCard> openCards = List.of();

        TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        List<TrucoCard> botCards = List.of(TrucoCard.of(QUEEN, HEARTS), TrucoCard.of(FIVE, CLUBS), TrucoCard.of(SIX, HEARTS));

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), openCards, vira, 3)
                .botInfo(botCards, 0)
                .opponentScore(0);
        assertThat(skolTable.getRaiseResponse(stepBuilder.build())).isNegative();
    }

    @Test
    @DisplayName("Should ask for raise with a very strong hand")
    void shouldAskForTrucoWithVeryStrongHand() {
        List<GameIntel.RoundResult> rounds = List.of();
        TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        List<TrucoCard> openCards = List.of();

        List<TrucoCard> strongHand = List.of(TrucoCard.of(FOUR, DIAMONDS), TrucoCard.of(FOUR,CLUBS), TrucoCard.of(ACE, DIAMONDS));

        GameIntel.StepBuilder strongHandBuilder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, openCards, vira, 1)
                .botInfo(strongHand, 0)
                .opponentScore(0);

        assertTrue(skolTable.decideIfRaises(strongHandBuilder.build()));
    }

    @Test
    @DisplayName("Should raise for a hand with powerRank 3 in the first round")
    void shouldRequestTrucoForPowerRank3HandInFirstRound() {
        List<GameIntel.RoundResult> rounds = List.of();
        TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        List<TrucoCard> openCards = List.of();

        List<TrucoCard> powerRank3Hand = List.of(
                TrucoCard.of(KING, CLUBS),
                TrucoCard.of(ACE, SPADES),
                TrucoCard.of(ACE, DIAMONDS)
        );

        GameIntel.StepBuilder powerRank3HandBuilder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, openCards, vira, 1)
                .botInfo(powerRank3Hand, 0)
                .opponentScore(0);

        assertTrue(skolTable.decideIfRaises(powerRank3HandBuilder.build()));
    }

    @Test
    @DisplayName("Should raise in Eleven Score Hand if opponent have 10 points and have a good hand")
    public void shouldRaiseInElevenScoreHandIfOpponentHave10PointsAndHavePower() {
        List<GameIntel.RoundResult> rounds = List.of();
        List<TrucoCard> openCards = List.of();
        TrucoCard vira = TrucoCard.of(THREE, HEARTS);

        List<TrucoCard> botCards = List.of(TrucoCard.of(FOUR, HEARTS), TrucoCard.of(KING, CLUBS), TrucoCard.of(TWO, HEARTS));

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, openCards, vira, 1)
                .botInfo(botCards, 11)
                .opponentScore(10);

        assertTrue(skolTable.getMaoDeOnzeResponse(stepBuilder.build()));
    }

    @Test
    @DisplayName("Should not raise in Eleven Score Hand if opponent have 10 points and haven't a good hand")
    public void shouldNotRaiseInElevenScoreHandIfOpponentHave8PointsAndHavePower() {
        List<GameIntel.RoundResult> rounds = List.of();
        List<TrucoCard> openCards = List.of();
        TrucoCard vira = TrucoCard.of(THREE, HEARTS);

        List<TrucoCard> botCards = List.of(TrucoCard.of(SEVEN, DIAMONDS), TrucoCard.of(SIX, SPADES), TrucoCard.of(FIVE, CLUBS));

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, openCards, vira, 1)
                .botInfo(botCards, 11)
                .opponentScore(10);

        assertFalse(skolTable.getMaoDeOnzeResponse(stepBuilder.build()));
    }

    @Test
    @DisplayName("Should raise if have a pair")
    public void shouldRaiseIfHaveAGoodPair(){
        List<GameIntel.RoundResult> rounds = List.of();
        List<TrucoCard> openCards = List.of();
        TrucoCard vira = TrucoCard.of(THREE, HEARTS);

        List<TrucoCard> botCards = List.of(TrucoCard.of(FOUR, DIAMONDS), TrucoCard.of(FOUR, SPADES), TrucoCard.of(ACE, CLUBS));

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0);

        assertThat(skolTable.getRaiseResponse(stepBuilder.build())).isEqualTo(0);
    }

}