package com.aah.refactor.me;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;
import static com.bueno.spi.model.CardSuit.HIDDEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import static com.bueno.spi.model.CardRank.ACE;
import static com.bueno.spi.model.CardSuit.DIAMONDS;

public class RefactorMePleaseBotTest{

    private RefactorMePleaseBot refactorMePleaseBot;

    @BeforeEach
    public void setUp() {
        refactorMePleaseBot = new RefactorMePleaseBot();
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

        assertEquals(TrucoCard.of(FOUR, HEARTS), refactorMePleaseBot.chooseCard(stepBuilder.build()).content());
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

        assertEquals(TrucoCard.of(TWO, CLUBS), refactorMePleaseBot.chooseCard(stepBuilder.build()).content());
    }

    @Test
    @DisplayName("Should not rise if lost first round")
    void ShouldNotRisIfLostFirstRound() {
        List<GameIntel.RoundResult> rounds = List.of(GameIntel.RoundResult.LOST);
        List<TrucoCard> openCards = List.of();

        TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        List<TrucoCard> botCards = List.of(TrucoCard.of(ACE, SPADES), TrucoCard.of(THREE, CLUBS));

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, openCards, vira, 3)
                .botInfo(botCards, 0)
                .opponentScore(0);
        assertThat(refactorMePleaseBot.getRaiseResponse(stepBuilder.build())).isNegative();
    }

    @Test
    @DisplayName("Should not rise if don't have good cards")
    void shouldNotRiseIfDontHaveGoodCards() {
        List<GameIntel.RoundResult> rounds = List.of();
        TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        List<TrucoCard> openCards = List.of(vira);

        List<TrucoCard> botCards = List.of(TrucoCard.of(TWO, SPADES), TrucoCard.of(FIVE, CLUBS), TrucoCard.of(SIX, SPADES));

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0);

        assertEquals(-1, refactorMePleaseBot.getRaiseResponse(stepBuilder.build()));
    }

    @Test
    @DisplayName("Should accept rise if won the first round and have strong card in hand")
    void ShouldRiseIfWonFirstRoundAndHaveStrongCard() {
        List<GameIntel.RoundResult> rounds = List.of(GameIntel.RoundResult.WON);
        List<TrucoCard> openCards = List.of();

        TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        List<TrucoCard> botCards = List.of(TrucoCard.of(FOUR, HEARTS), TrucoCard.of(ACE, CLUBS));

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, openCards, vira, 3)
                .botInfo(botCards, 0)
                .opponentScore(0);
        assertThat(refactorMePleaseBot.getRaiseResponse(stepBuilder.build())).isZero();
    }

    @Test
    @DisplayName("Should rise again if won the first round and have strong card in hand")
    void ShouldRiseAgainIfWonFirstRoundAndHaveStrongCard() {
        List<GameIntel.RoundResult> rounds = List.of(GameIntel.RoundResult.WON);
        List<TrucoCard> openCards = List.of();

        TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        List<TrucoCard> botCards = List.of(TrucoCard.of(FOUR, HEARTS), TrucoCard.of(THREE, CLUBS));

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, openCards, vira, 3)
                .botInfo(botCards, 0)
                .opponentScore(0);
        assertThat(refactorMePleaseBot.getRaiseResponse(stepBuilder.build())).isPositive();
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
        assertThat(refactorMePleaseBot.getRaiseResponse(strongHandBuilder.build())).isZero();
    }

    @Test
    @DisplayName("Should not accept rise again if have weak cards in hand")
    void ShouldNotAcceptRiseIfHaveWeakCards() {
        List<TrucoCard> openCards = List.of();

        TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        List<TrucoCard> botCards = List.of(TrucoCard.of(QUEEN, HEARTS), TrucoCard.of(FIVE, CLUBS), TrucoCard.of(SIX, HEARTS));

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), openCards, vira, 3)
                .botInfo(botCards, 0)
                .opponentScore(0);
        assertThat(refactorMePleaseBot.getRaiseResponse(stepBuilder.build())).isNegative();
    }

    @Test
    @DisplayName("Should ask for raise with a very strong hand")
    void shouldAskForRaiseWithVeryStrongHand() {
        List<GameIntel.RoundResult> rounds = List.of();
        TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        List<TrucoCard> openCards = List.of();

        List<TrucoCard> strongHand = List.of(TrucoCard.of(FOUR, DIAMONDS), TrucoCard.of(FOUR, CLUBS), TrucoCard.of(ACE, DIAMONDS));

        GameIntel.StepBuilder strongHandBuilder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, openCards, vira, 1)
                .botInfo(strongHand, 0)
                .opponentScore(0);

        assertTrue(refactorMePleaseBot.decideIfRaises(strongHandBuilder.build()));
    }

    @Test
    @DisplayName("Should raise for a hand with powerRank 3 in the first round")
    void shouldRaiseForPowerRank3HandInFirstRound() {
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

        assertTrue(refactorMePleaseBot.decideIfRaises(powerRank3HandBuilder.build()));
    }

    @Test
    @DisplayName("Should raise in Eleven Score Hand if opponent have 10 points and have a good hand")
    void shouldRaiseInElevenScoreHandIfOpponentHave10PointsAndHavePower() {
        List<GameIntel.RoundResult> rounds = List.of();
        List<TrucoCard> openCards = List.of();
        TrucoCard vira = TrucoCard.of(THREE, HEARTS);

        List<TrucoCard> botCards = List.of(TrucoCard.of(FOUR, HEARTS), TrucoCard.of(KING, CLUBS), TrucoCard.of(TWO, HEARTS));

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, openCards, vira, 1)
                .botInfo(botCards, 11)
                .opponentScore(10);

        assertTrue(refactorMePleaseBot.getMaoDeOnzeResponse(stepBuilder.build()));
    }

    @Test
    @DisplayName("Should not raise in Eleven Score Hand if opponent have 10 points and haven't a good hand")
    void shouldNotRaiseInElevenScoreHandIfOpponentHave8PointsAndHavePower() {
        List<GameIntel.RoundResult> rounds = List.of();
        List<TrucoCard> openCards = List.of();
        TrucoCard vira = TrucoCard.of(THREE, HEARTS);

        List<TrucoCard> botCards = List.of(TrucoCard.of(SEVEN, DIAMONDS), TrucoCard.of(SIX, SPADES), TrucoCard.of(FIVE, CLUBS));

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, openCards, vira, 1)
                .botInfo(botCards, 11)
                .opponentScore(10);

        assertFalse(refactorMePleaseBot.getMaoDeOnzeResponse(stepBuilder.build()));
    }

    @Test
    @DisplayName("Should raise if have a pair")
    void shouldRaiseIfHaveAGoodPair() {
        List<GameIntel.RoundResult> rounds = List.of();
        List<TrucoCard> openCards = List.of();
        TrucoCard vira = TrucoCard.of(THREE, HEARTS);

        List<TrucoCard> botCards = List.of(TrucoCard.of(FOUR, DIAMONDS), TrucoCard.of(FOUR, SPADES), TrucoCard.of(ACE, CLUBS));

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0);

        assertThat(refactorMePleaseBot.getRaiseResponse(stepBuilder.build())).isZero();
    }

    @Test
    @DisplayName("Should ask for raise if won first round and have a strong hand")
    void shouldAskForRaiseIfWonFirstRoundAndHaveStrongHand() {
        List<GameIntel.RoundResult> rounds = List.of(GameIntel.RoundResult.WON);
        TrucoCard vira = TrucoCard.of(THREE, HEARTS);

        List<TrucoCard> strongHand = List.of(TrucoCard.of(FOUR, CLUBS), TrucoCard.of(FIVE, DIAMONDS));

        GameIntel.StepBuilder strongHandBuilder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, List.of(), vira, 1)
                .botInfo(strongHand, 0)
                .opponentScore(0);

        assertTrue(refactorMePleaseBot.decideIfRaises(strongHandBuilder.build()));
    }

    @Test
    @DisplayName("Should play the strongest card if first round is drew")
    void shouldPlayTheStrongestCardIfFirstRoundIsDrew() {
        List<GameIntel.RoundResult> rounds = List.of(GameIntel.RoundResult.DREW);
        TrucoCard vira = TrucoCard.of(THREE, HEARTS);

        List<TrucoCard> strongHand = List.of(TrucoCard.of(FOUR, CLUBS), TrucoCard.of(ACE, DIAMONDS));

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, List.of(), vira, 1)
                .botInfo(strongHand, 0)
                .opponentScore(0);

        assertEquals(TrucoCard.of(FOUR, CLUBS), refactorMePleaseBot.chooseCard(stepBuilder.build()).content());
    }

    @Test
    @DisplayName("Should play the weakest card if opponents card is hidden")
    void shouldPlayTheWeakestCardIfOpponentsCardIsHidden() {
        List<GameIntel.RoundResult> rounds = List.of(GameIntel.RoundResult.WON);
        TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        TrucoCard opponentCard = TrucoCard.of(CardRank.HIDDEN, HIDDEN);

        List<TrucoCard> strongHand = List.of(TrucoCard.of(FOUR, CLUBS), TrucoCard.of(FIVE, DIAMONDS));

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, List.of(), vira, 1)
                .botInfo(strongHand, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        assertEquals(TrucoCard.of(FIVE, DIAMONDS), refactorMePleaseBot.chooseCard(stepBuilder.build()).content());
    }

    @Test
    @DisplayName("Should play the weakest card if opponents card is Zap")
    void shouldPlayTheWeakestCardIfOpponentsCardIsZap() {
        List<GameIntel.RoundResult> rounds = List.of(GameIntel.RoundResult.WON);
        TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        TrucoCard opponentCard = TrucoCard.of(FOUR, CLUBS);

        List<TrucoCard> strongHand = List.of(TrucoCard.of(FOUR, HEARTS), TrucoCard.of(FIVE, DIAMONDS));

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, List.of(), vira, 1)
                .botInfo(strongHand, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        assertEquals(TrucoCard.of(FIVE, DIAMONDS), refactorMePleaseBot.chooseCard(stepBuilder.build()).content());
    }

    @Test
    @DisplayName("Should decide not to raise in the second round with low hand Power Rank")
    void shouldNotRaiseInSecondRoundWithLowHandPowerRank() {
        List<GameIntel.RoundResult> rounds = List.of(GameIntel.RoundResult.WON);
        TrucoCard vira = TrucoCard.of(THREE, HEARTS);

        List<TrucoCard> strongHand = List.of(TrucoCard.of(FOUR, CLUBS), TrucoCard.of(THREE, DIAMONDS));

        GameIntel.StepBuilder strongHandBuilder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, List.of(), vira, 1)
                .botInfo(strongHand, 0)
                .opponentScore(0);

        assertFalse(refactorMePleaseBot.decideIfRaises(strongHandBuilder.build()));
    }

    @Test
    @DisplayName("Should decide not to raise in the second round with low hand Power Rank after a drew")
    void shouldNotRaiseInSecondRoundDRWithLowHandPowerRankAfterDrew() {
        List<GameIntel.RoundResult> rounds = List.of(GameIntel.RoundResult.DREW);
        TrucoCard vira = TrucoCard.of(THREE, HEARTS);

        List<TrucoCard> strongHand = List.of(TrucoCard.of(FOUR, CLUBS), TrucoCard.of(THREE, DIAMONDS));

        GameIntel.StepBuilder strongHandBuilder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, List.of(), vira, 1)
                .botInfo(strongHand, 0)
                .opponentScore(0);

        assertTrue(refactorMePleaseBot.decideIfRaises(strongHandBuilder.build()));
    }

    @Test
    @DisplayName("Should decide not to raise in the second round with low hand Power Rank after lost")
    void shouldNotRaiseInSecondRoundWithLowHandPowerRankAfterLost() {
        List<GameIntel.RoundResult> rounds = List.of(GameIntel.RoundResult.LOST);
        TrucoCard vira = TrucoCard.of(THREE, HEARTS);

        List<TrucoCard> strongHand = List.of(TrucoCard.of(FOUR,CLUBS), TrucoCard.of(THREE, DIAMONDS));

        GameIntel.StepBuilder strongHandBuilder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, List.of(), vira, 1)
                .botInfo(strongHand, 0)
                .opponentScore(0);

        assertTrue(refactorMePleaseBot.decideIfRaises(strongHandBuilder.build()));
    }

    @Test
    @DisplayName("Should decide to raise in the second round with strong hand Power Rank")
    void shouldRaiseInSecondRoundWithStrongHandPowerRank() {
        List<GameIntel.RoundResult> rounds = List.of(GameIntel.RoundResult.WON);
        TrucoCard vira = TrucoCard.of(THREE, HEARTS);

        List<TrucoCard> strongHand = List.of(TrucoCard.of(FOUR, CLUBS), TrucoCard.of(FIVE, DIAMONDS));

        GameIntel.StepBuilder strongHandBuilder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, List.of(), vira, 1)
                .botInfo(strongHand, 0)
                .opponentScore(0);

        assertTrue(refactorMePleaseBot.decideIfRaises(strongHandBuilder.build()));
    }

    @Test
    @DisplayName("Should play the strongest card if lost first round")
    void shouldPlayTheStrongestCardIfLostFirstRound() {
        List<GameIntel.RoundResult> rounds = List.of(GameIntel.RoundResult.LOST);
        TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        TrucoCard opponentCard = TrucoCard.of(ACE, CLUBS);

        List<TrucoCard> strongHand = List.of(TrucoCard.of(FOUR, HEARTS), TrucoCard.of(FIVE, DIAMONDS));

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, List.of(), vira, 1)
                .botInfo(strongHand, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        assertEquals(TrucoCard.of(FOUR, HEARTS), refactorMePleaseBot.chooseCard(stepBuilder.build()).content());
    }

    @Test
    @DisplayName("Should play the weakest card on second round if won first round and have zap")
    void shouldPlayTheWeakestCardIfWonFirstRoundAndHaveZap() {
        List<GameIntel.RoundResult> rounds = List.of(GameIntel.RoundResult.WON);
        TrucoCard vira = TrucoCard.of(THREE, HEARTS);

        List<TrucoCard> strongHand = List.of(TrucoCard.of(FOUR, CLUBS), TrucoCard.of(ACE, DIAMONDS));

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, List.of(), vira, 1)
                .botInfo(strongHand, 0)
                .opponentScore(0);

        assertEquals(TrucoCard.of(ACE, DIAMONDS), refactorMePleaseBot.chooseCard(stepBuilder.build()).content());
    }

    @Test
    @DisplayName("Should not play the strong card on second round if won first round and have zap")
    void shouldNotPlayTheStrongCardIfWonFirstRoundAndHaveZap() {
        List<GameIntel.RoundResult> rounds = List.of(GameIntel.RoundResult.WON);
        TrucoCard vira = TrucoCard.of(THREE, HEARTS);

        List<TrucoCard> strongHand = List.of(TrucoCard.of(FOUR, CLUBS), TrucoCard.of(ACE, DIAMONDS));

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, List.of(), vira, 1)
                .botInfo(strongHand, 0)
                .opponentScore(0);

        assertNotEquals(TrucoCard.of(FOUR, CLUBS), refactorMePleaseBot.chooseCard(stepBuilder.build()).content());
    }

    @Test
    @DisplayName("Should play the weakest card on first round if it has copas and zap")
    void shouldPlayTheWeakestCardOnFirstRoundIfHasCopasAndZap() {
        List<GameIntel.RoundResult> rounds = List.of();
        TrucoCard vira = TrucoCard.of(THREE, HEARTS);

        List<TrucoCard> strongHand = List.of(TrucoCard.of(FOUR, CLUBS), TrucoCard.of(FOUR, HEARTS), TrucoCard.of(SIX, SPADES));

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, List.of(), vira, 1)
                .botInfo(strongHand, 0)
                .opponentScore(0);

        assertEquals(TrucoCard.of(SIX, SPADES), refactorMePleaseBot.chooseCard(stepBuilder.build()).content());
    }


    @Test
    @DisplayName("Should decide to raise in the second round if lost first round and bot has copas and zap")
    void shouldRaiseInSecondRoundIfLostFirstRoundAndHasCopasAndZap() {
        List<GameIntel.RoundResult> rounds = List.of(GameIntel.RoundResult.LOST);
        TrucoCard vira = TrucoCard.of(THREE, HEARTS);

        List<TrucoCard> strongHand = List.of(TrucoCard.of(FOUR, CLUBS), TrucoCard.of(FOUR, HEARTS));

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, List.of(), vira, 1)
                .botInfo(strongHand, 0)
                .opponentScore(0);

        assertTrue(refactorMePleaseBot.decideIfRaises(stepBuilder.build()));
    }

    @Test
    @DisplayName("Should play the weakest card capable of win on first round if opponents card is weak")
    void shouldPlayTheWeakestCardOnCapableOfWinOnFirstRoundIfOpponentsCardIsWeak() {
        List<GameIntel.RoundResult> rounds = List.of();
        TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        TrucoCard opponentCard = TrucoCard.of(SEVEN, SPADES);

        List<TrucoCard> strongHand = List.of(TrucoCard.of(THREE, CLUBS), TrucoCard.of(FOUR, HEARTS), TrucoCard.of(SIX, SPADES));

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, List.of(), vira, 1)
                .botInfo(strongHand, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        assertEquals(TrucoCard.of(THREE, CLUBS), refactorMePleaseBot.chooseCard(stepBuilder.build()).content());
    }

    @Test
    @DisplayName("Should not raise if is bot eleven hand round")
    void ShouldNotRiseIsBotElevenHandRound () {
        List<GameIntel.RoundResult> rounds = List.of();
        TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        List<TrucoCard> openCards = List.of();
        List<TrucoCard> hand = List.of();
        GameIntel.StepBuilder powerRank3HandBuilder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, openCards, vira, 1)
                .botInfo(hand, 11)
                .opponentScore(0);

        assertFalse(refactorMePleaseBot.decideIfRaises(powerRank3HandBuilder.build()));
    }


    @Test
    @DisplayName("Should not raise if is opponents eleven hand round")
    void ShouldNotRiseIsOpponentsElevenHandRound () {
        List<GameIntel.RoundResult> rounds = List.of();
        TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        List<TrucoCard> openCards = List.of();
        List<TrucoCard> hand = List.of();
        GameIntel.StepBuilder powerRank3HandBuilder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, openCards, vira, 1)
                .botInfo(hand, 0)
                .opponentScore(11);

        assertFalse(refactorMePleaseBot.decideIfRaises(powerRank3HandBuilder.build()));
    }

    @Test
    @DisplayName("Can raise if it's not bot eleven hand round")
    void CanRiseIfItIsNotBotElevenHandRound () {
        List<GameIntel.RoundResult> rounds = List.of();
        TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        List<TrucoCard> openCards = List.of();
        List<TrucoCard> hand = List.of(TrucoCard.of(THREE, CLUBS), TrucoCard.of(FOUR, HEARTS), TrucoCard.of(SIX, SPADES));
        GameIntel.StepBuilder builder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, openCards, vira, 1)
                .botInfo(hand, 10)
                .opponentScore(0);

        assertTrue(refactorMePleaseBot.decideIfRaises(builder.build()));
    }

    @Test
    @DisplayName("Can raise if it's not opponents eleven hand round")
    void CanRiseIfItIsNotOpponentsElevenHandRound () {
        List<GameIntel.RoundResult> rounds = List.of();
        TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        List<TrucoCard> openCards = List.of();
        List<TrucoCard> hand = List.of(TrucoCard.of(THREE, CLUBS), TrucoCard.of(FOUR, HEARTS), TrucoCard.of(SIX, SPADES));
        GameIntel.StepBuilder builder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, openCards, vira, 1)
                .botInfo(hand, 10)
                .opponentScore(10);

        assertTrue(refactorMePleaseBot.decideIfRaises(builder.build()));
    }

    @Test
    @DisplayName("Should not raise if hand points is 12")
    void ShouldNotRiseIfHandPointsIs12 () {
        List<GameIntel.RoundResult> rounds = List.of();
        TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        List<TrucoCard> openCards = List.of();
        List<TrucoCard> hand = List.of(TrucoCard.of(THREE, CLUBS), TrucoCard.of(FOUR, HEARTS), TrucoCard.of(SIX, SPADES));
        GameIntel.StepBuilder powerRank3HandBuilder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, openCards, vira, 12)
                .botInfo(hand, 0)
                .opponentScore(0);

        assertFalse(refactorMePleaseBot.decideIfRaises(powerRank3HandBuilder.build()));
    }

    @Test
    @DisplayName("Should respond to raises correctly with different scores")
    void shouldRespondFalseToRaisesCorrectlyWithDifferentScores() {
        TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        List<TrucoCard> openCards = List.of();

        List<TrucoCard> botCardsWithWeakHand = List.of(TrucoCard.of(SEVEN, DIAMONDS), TrucoCard.of(SIX, SPADES), TrucoCard.of(FIVE, CLUBS));
        GameIntel.StepBuilder weakHandBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), openCards, vira, 1)
                .botInfo(botCardsWithWeakHand, 11)
                .opponentScore(8);

        assertFalse(refactorMePleaseBot.decideIfRaises(weakHandBuilder.build()));
    }

    @Test
    @DisplayName("Should play the strongest card in different combinations (1)")
    void shouldPlayStrongestCardWithDifferentCombinations1() {
        TrucoCard vira = TrucoCard.of(THREE, HEARTS);

        List<TrucoCard> botCards1 = List.of(TrucoCard.of(ACE, SPADES), TrucoCard.of(THREE, CLUBS), TrucoCard.of(FOUR, HEARTS));
        List<TrucoCard> openCards1 = List.of(vira);
        GameIntel.StepBuilder stepBuilder1 = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), openCards1, vira, 1)
                .botInfo(botCards1, 0)
                .opponentScore(0);
        assertEquals(TrucoCard.of(FOUR, HEARTS), refactorMePleaseBot.chooseCard(stepBuilder1.build()).content());
    }

    @Test
    @DisplayName("Should play the strongest card in different combinations (2)")
    void shouldPlayStrongestCardWithDifferentCombinations2() {
        TrucoCard vira = TrucoCard.of(TWO, HEARTS);
        List<TrucoCard> botCards2 = List.of(TrucoCard.of(THREE, SPADES), TrucoCard.of(SEVEN, CLUBS), TrucoCard.of(ACE, DIAMONDS));
        List<TrucoCard> openCards2 = List.of(vira);
        GameIntel.StepBuilder stepBuilder2 = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), openCards2, vira, 1)
                .botInfo(botCards2, 0)
                .opponentScore(0);
        assertEquals(TrucoCard.of(THREE, SPADES), refactorMePleaseBot.chooseCard(stepBuilder2.build()).content());
    }

    @Test
    @DisplayName("Should play the strongest card in different combinations (3)")
    void shouldPlayStrongestCardWithDifferentCombinations3() {
        TrucoCard vira = TrucoCard.of(ACE, HEARTS);
        List<TrucoCard> botCards3 = List.of(TrucoCard.of(TWO, SPADES), TrucoCard.of(FIVE, CLUBS), TrucoCard.of(SIX, HEARTS));
        List<TrucoCard> openCards3 = List.of(vira);
        GameIntel.StepBuilder stepBuilder3 = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), openCards3, vira, 1)
                .botInfo(botCards3, 0)
                .opponentScore(0);
        assertEquals(TrucoCard.of(TWO, SPADES), refactorMePleaseBot.chooseCard(stepBuilder3.build()).content());
    }

    @Test
    @DisplayName("Should play the weakest card on second round if won first round and have zap with different combination")
    void shouldPlayTheWeakestCardIfWonFirstRoundAndHaveZap2() {
        List<GameIntel.RoundResult> rounds = List.of(GameIntel.RoundResult.WON);
        TrucoCard vira = TrucoCard.of(SIX, HEARTS);

        List<TrucoCard> strongHand = List.of(TrucoCard.of(SEVEN, CLUBS), TrucoCard.of(ACE, DIAMONDS));

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, List.of(), vira, 6)
                .botInfo(strongHand, 9)
                .opponentScore(0);

        assertEquals(TrucoCard.of(ACE, DIAMONDS), refactorMePleaseBot.chooseCard(stepBuilder.build()).content());
    }



}