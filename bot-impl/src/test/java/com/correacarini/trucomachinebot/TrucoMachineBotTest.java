package com.correacarini.trucomachinebot;

import com.bueno.spi.model.*;
import com.correacarini.impl.trucomachinebot.TrucoMachineBot;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardRank.HIDDEN;
import static com.bueno.spi.model.CardSuit.*;
import static com.bueno.spi.model.GameIntel.RoundResult.WON;
import static org.junit.jupiter.api.Assertions.*;

public class TrucoMachineBotTest {
    @Test
    @DisplayName("Should choose a card")
    void ShouldChooseACard() {
        GameIntel.StepBuilder stepBuilder = (GameIntel.StepBuilder) GameIntel.StepBuilder.with()
                .gameInfo(List.of(), List.of(), TrucoCard.of(ACE, SPADES), 1)
                .botInfo(List.of(TrucoCard.of(ACE, CLUBS)), 0);

        CardToPlay cardToPlay = new TrucoMachineBot().chooseCard(stepBuilder.build());
        assertNotNull(cardToPlay);
    }
    @Test
    @DisplayName("Should return greatest card in round 1 if is first to play")
    void ShouldReturnGreatestCardInRound1IfIsFirstToPlay() {
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(SIX, CLUBS),
                TrucoCard.of(FIVE, DIAMONDS),
                TrucoCard.of(THREE, CLUBS)
        );
        GameIntel.StepBuilder stepBuilder = (GameIntel.StepBuilder) GameIntel.StepBuilder.with()
                .gameInfo(List.of(), List.of(), TrucoCard.of(ACE, SPADES), 1)
                .botInfo(botCards, 0);

        CardToPlay cardToPlay = new TrucoMachineBot().chooseCard(stepBuilder.build());
        assertEquals(CardToPlay.of(TrucoCard.of(THREE, CLUBS)), cardToPlay);
    }
    @Test
    @DisplayName("Should return minimal greater card If is second to play")
    void ShouldReturnMinimalGreaterCardIfIsSecondToPlay() {
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(SEVEN, CLUBS),
                TrucoCard.of(FIVE, DIAMONDS),
                TrucoCard.of(THREE, CLUBS)
        );
        GameIntel.StepBuilder stepBuilder = (GameIntel.StepBuilder) GameIntel.StepBuilder.with()
                .gameInfo(List.of(), List.of(), TrucoCard.of(ACE, SPADES), 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(TrucoCard.of(SIX, SPADES));

        CardToPlay cardToPlay = new TrucoMachineBot().chooseCard(stepBuilder.build());
        assertEquals(CardToPlay.of(TrucoCard.of(SEVEN, CLUBS)), cardToPlay);
    }
    @Test
    @DisplayName("Should return lowest card if none beats opponent")
    void ShouldReturnLowestCardIfNoneBeatsOpponent() {
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(SEVEN, CLUBS),
                TrucoCard.of(FIVE, DIAMONDS),
                TrucoCard.of(THREE, CLUBS)
        );
        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), List.of(), TrucoCard.of(ACE, SPADES), 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(TrucoCard.of(TWO, SPADES));

        CardToPlay cardToPlay = new TrucoMachineBot().chooseCard(stepBuilder.build());
        assertEquals(CardToPlay.of(TrucoCard.of(FIVE, DIAMONDS)), cardToPlay);
    }
    @Test
    @DisplayName("Should amarrar if greatest card is equal to opponent card")
    void ShouldAmarrarIfGreatestCardIsEqualToOpponentCard() {
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(SEVEN, CLUBS),
                TrucoCard.of(FIVE, DIAMONDS),
                TrucoCard.of(THREE, CLUBS)
        );
        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), List.of(), TrucoCard.of(ACE, SPADES), 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(TrucoCard.of(THREE, SPADES));

        CardToPlay cardToPlay = new TrucoMachineBot().chooseCard(stepBuilder.build());
        assertEquals(CardToPlay.of(TrucoCard.of(THREE, CLUBS)), cardToPlay);
    }
    @Test
    @DisplayName("Should play last round")
    void ShouldPlayLastRound() {
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(THREE, CLUBS)
        );
        GameIntel.StepBuilder stepBuilder = (GameIntel.StepBuilder) GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST, WON), List.of(), TrucoCard.of(ACE, SPADES), 1)
                .botInfo(botCards, 0);

        CardToPlay cardToPlay = new TrucoMachineBot().chooseCard(stepBuilder.build());
        assertEquals(CardToPlay.of(TrucoCard.of(THREE, CLUBS)), cardToPlay);
    }
    @Test
    @DisplayName("Should not raise when bot score is equal to 11")
    void ShouldNotRaiseWhenBotScoreIsEqualTo11() {
        GameIntel.StepBuilder stepBuilder = (GameIntel.StepBuilder) GameIntel.StepBuilder.with()
                .gameInfo(List.of(), List.of(), TrucoCard.of(ACE, SPADES), 1)
                .botInfo(List.of(), 11);

        boolean raises = new TrucoMachineBot().decideIfRaises(stepBuilder.build());
        assertFalse(raises);
    }
    @Test
    @DisplayName("Should not raise when opponent score is equal to 11")
    void ShouldNotRaiseWhenOpponentScoreIsEqualTo11() {
        GameIntel.StepBuilder stepBuilder = (GameIntel.StepBuilder) GameIntel.StepBuilder.with()
                .gameInfo(List.of(), List.of(), TrucoCard.of(ACE, SPADES), 1)
                .botInfo(List.of(), 5)
                .opponentScore(11);

        boolean raises = new TrucoMachineBot().decideIfRaises(stepBuilder.build());
        assertFalse(raises);
    }
    @Test
    @DisplayName("Should raise if has zap and manilha")
    void ShouldRaiseIfHasZapAndManilha() {
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(SEVEN, CLUBS),
                TrucoCard.of(FIVE, DIAMONDS),
                TrucoCard.of(FIVE, CLUBS)
        );

        GameIntel.StepBuilder stepBuilder = (GameIntel.StepBuilder) GameIntel.StepBuilder.with()
                .gameInfo(List.of(), List.of(), TrucoCard.of(FOUR, SPADES), 1)
                .botInfo(botCards, 0);

        boolean raises = new TrucoMachineBot().decideIfRaises(stepBuilder.build());
        assertTrue(raises);
    }
    @Test
    @DisplayName("Should raise when has manilha and 3 and score difference is greater than 3")
    void ShouldRaiseWhenHasManilhaAnd3AndScoreDifferenceIsGreaterThan3() {
        TrucoCard vira = TrucoCard.of(FIVE, SPADES);
        List<TrucoCard> card = List.of(
                TrucoCard.of(SIX, DIAMONDS),
                TrucoCard.of(THREE, CLUBS)
        );

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), List.of(), vira, 1)
                .botInfo(card, 4)
                .opponentScore(0);

        boolean decideIfRaises = new TrucoMachineBot().decideIfRaises(stepBuilder.build());
        assertTrue(decideIfRaises);
    }
    @Test
    @DisplayName("Should raise when has manilha and 2 if score difference is greater than 3 and won first round")
    void ShouldRaiseWhenHasManilhaAnd2IfScoreDifferenceIsGreaterThan3AndWonFirstRound() {
        TrucoCard vira = TrucoCard.of(FIVE, SPADES);
        List<TrucoCard> card = List.of(
                TrucoCard.of(SIX, DIAMONDS),
                TrucoCard.of(TWO, CLUBS)
        );

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(WON), List.of(), vira, 1)
                .botInfo(card, 4)
                .opponentScore(0);

        boolean raises = new TrucoMachineBot().decideIfRaises(stepBuilder.build());
        assertTrue(raises);
    }
    @Test
    @DisplayName("Should raise if last round card is greater than opponent card")
    void ShouldRaiseIfLastRoundCardIsGreaterThanOpponentCard() {
        TrucoCard vira = TrucoCard.of(ACE, SPADES);
        List<TrucoCard> card = List.of(
                TrucoCard.of(TWO, CLUBS)
        );
        List<GameIntel.RoundResult> roundResults = List.of(
                GameIntel.RoundResult.WON,
                GameIntel.RoundResult.LOST
        );

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, List.of(TrucoCard.of(FOUR, CLUBS)), vira, 1)
                .botInfo(card, 0)
                .opponentScore(0)
                .opponentCard(TrucoCard.of(FOUR, DIAMONDS));

        boolean raises = new TrucoMachineBot().decideIfRaises(stepBuilder.build());
        assertTrue(raises);
    }
    @Test
    @DisplayName("Should discard second round when has zap and won first round")
    void ShouldDiscardSecondRoundWhenHasZapAndWonFirstRound() {
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(THREE, CLUBS),
                TrucoCard.of(SEVEN, CLUBS)
        );
        GameIntel.StepBuilder stepBuilder = (GameIntel.StepBuilder) GameIntel.StepBuilder.with()
                .gameInfo(List.of(WON), List.of(), TrucoCard.of(SIX, SPADES), 1)
                .botInfo(botCards, 0);

        CardToPlay cardToPlay = new TrucoMachineBot().chooseCard(stepBuilder.build());
        assertEquals(CardToPlay.discard(TrucoCard.of(THREE, CLUBS)), cardToPlay);
    }
    @Test
    @DisplayName("Should choose lowest card when opponent discard")
    void ShouldChooseLowestCardWhenOpponentDiscard() {
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(THREE, DIAMONDS),
                TrucoCard.of(QUEEN, CLUBS)
        );
        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(WON), List.of(), TrucoCard.of(SIX, SPADES), 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(TrucoCard.of(CardRank.HIDDEN, CardSuit.HIDDEN));

        CardToPlay cardToPlay = new TrucoMachineBot().chooseCard(stepBuilder.build());
        assertEquals(CardToPlay.of(TrucoCard.of(QUEEN, CLUBS)), cardToPlay);
    }
}
