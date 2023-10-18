package com.correacarini.trucomachinebot;

import com.bueno.spi.model.*;
import com.correacarini.impl.trucomachinebot.TrucoMachineBot;
import com.hermespiassi.casados.marrecobot.MarrecoBot;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardRank.HIDDEN;
import static com.bueno.spi.model.CardSuit.*;
import static com.bueno.spi.model.GameIntel.RoundResult.LOST;
import static com.bueno.spi.model.GameIntel.RoundResult.WON;
import static org.assertj.core.api.Assertions.assertThat;
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
    @Test
    @DisplayName("Should raise if has zap on third round")
    void ShouldRaiseIfHasZapOnThirdRound() {
        GameIntel.StepBuilder stepBuilder = (GameIntel.StepBuilder) GameIntel.StepBuilder.with()
                .gameInfo(List.of(WON, LOST), List.of(), TrucoCard.of(TWO, SPADES), 1)
                .botInfo(List.of(TrucoCard.of(THREE, CLUBS)), 0);

        boolean raise = new TrucoMachineBot().decideIfRaises(stepBuilder.build());
        assertTrue(raise);
    }

    @Test
    @DisplayName("Should accept mao de onze when opponent score is 11")
    void shouldAcceptMaoDeOnzeWhenOpponentScoreIs11(){
        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), List.of(), TrucoCard.of(ACE, SPADES), 1)
                .botInfo(List.of(TrucoCard.of(ACE, CLUBS)), 0)
                .opponentScore(11);

        boolean playMaoDeOnze = new TrucoMachineBot().getMaoDeOnzeResponse(stepBuilder.build());
        assertTrue(playMaoDeOnze);
    }

    @Test
    @DisplayName("Should not accept mao de onze when opponent score is not 11")
    void shouldNotAcceptMaoDeOnzeWhenOpponentScoreIsNot11(){
        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), List.of(), TrucoCard.of(ACE, SPADES), 1)
                .botInfo(List.of(TrucoCard.of(ACE, CLUBS)), 0)
                .opponentScore(10);

        boolean playMaoDeOnze = new TrucoMachineBot().getMaoDeOnzeResponse(stepBuilder.build());
        assertFalse(playMaoDeOnze);
    }
    
    @Test
    @DisplayName("Should accept mao de onze when having 3 strong cards including manilha ")
    void shouldAcceptMaoDeOnzeWhenHaving3StrongCardsIncludingManilha(){
        TrucoCard manilha = TrucoCard.of(TWO,CLUBS);
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(THREE,SPADES),
                TrucoCard.of(THREE, DIAMONDS),
                manilha
        );
        GameIntel.StepBuilder stepBuilder = (GameIntel.StepBuilder) GameIntel.StepBuilder.with()
                .gameInfo(List.of(), List.of(), TrucoCard.of(ACE, SPADES), 1)
                .botInfo(botCards, 0)
                .opponentScore(7);


        boolean playMaoDeOnze = new TrucoMachineBot().getMaoDeOnzeResponse(stepBuilder.build());
        assertTrue(playMaoDeOnze);
    }

    @Test
    @DisplayName("Should not accept mao de onze when having 3 strong cards not including manilha ")
    void shouldNotAcceptMaoDeOnzeWhenHaving3StrongCardsNotIncludingManilha(){
        TrucoCard manilha = TrucoCard.of(THREE,CLUBS);
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(THREE,SPADES),
                TrucoCard.of(THREE, DIAMONDS),
                manilha
        );
        GameIntel.StepBuilder stepBuilder = (GameIntel.StepBuilder) GameIntel.StepBuilder.with()
                .gameInfo(List.of(), List.of(), TrucoCard.of(ACE, SPADES), 1)
                .botInfo(botCards, 0)
                .opponentScore(7);

        boolean playMaoDeOnze = new TrucoMachineBot().getMaoDeOnzeResponse(stepBuilder.build());
        assertFalse(playMaoDeOnze);

    }
    
    @Test
    @DisplayName("Should not accept mao de onze when not having 3 strong cards")
    void shouldNotAcceptMaoDeOnzeWhenNotHaving3StrongCards(){
        TrucoCard manilha = TrucoCard.of(THREE,CLUBS);
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(THREE,SPADES),
                TrucoCard.of(QUEEN, DIAMONDS),
                manilha
        );
        GameIntel.StepBuilder stepBuilder = (GameIntel.StepBuilder) GameIntel.StepBuilder.with()
                .gameInfo(List.of(), List.of(), TrucoCard.of(ACE, SPADES), 1)
                .botInfo(botCards, 0)
                .opponentScore(7);

        boolean playMaoDeOnze = new TrucoMachineBot().getMaoDeOnzeResponse(stepBuilder.build());
        assertFalse(playMaoDeOnze);
    }
    
    @Test
    @DisplayName("Should accept Mao de onze when having 3 strong cards and opponent scores is 8 or more")
    void shouldAcceptMaoDeOnzeWhenOnlyHaving3StrongCardsAndOpponentScoresIs8OrMore(){
        TrucoCard manilha = TrucoCard.of(THREE,CLUBS);
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(THREE,SPADES),
                TrucoCard.of(THREE, DIAMONDS),
                manilha
        );
        GameIntel.StepBuilder stepBuilder = (GameIntel.StepBuilder) GameIntel.StepBuilder.with()
                .gameInfo(List.of(), List.of(), TrucoCard.of(ACE, SPADES), 1)
                .botInfo(botCards, 0)
                .opponentScore(8);

        boolean playMaoDeOnze = new TrucoMachineBot().getMaoDeOnzeResponse(stepBuilder.build());
        assertTrue(playMaoDeOnze);

    }
    @Test
    @DisplayName("Should raise if has 3 three cards and score difference is greater than 3")
    void ShouldRaiseIfHas3ThreeCardsAndScoreDifferenceIsGreaterThan3() {
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(THREE,SPADES),
                TrucoCard.of(THREE, DIAMONDS),
                TrucoCard.of(THREE, CLUBS)
        );
        GameIntel.StepBuilder stepBuilder = (GameIntel.StepBuilder) GameIntel.StepBuilder.with()
                .gameInfo(List.of(), List.of(), TrucoCard.of(ACE, SPADES), 1)
                .botInfo(botCards, 5)
                .opponentScore(0);

        boolean raise = new TrucoMachineBot().decideIfRaises(stepBuilder.build());
        assertTrue(raise);
    }
    @Test
    @DisplayName("Should raise if lost first round and has 2 manilhas")
    void ShouldRaiseIfLostFirstRoundAndHas2Manilhas() {
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(SIX, DIAMONDS),
                TrucoCard.of(SIX, SPADES)
        );
        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(LOST), List.of(), TrucoCard.of(FIVE, SPADES), 1)
                .botInfo(botCards, 5)
                .opponentScore(0);

        boolean raise = new TrucoMachineBot().decideIfRaises(stepBuilder.build());
        assertTrue(raise);
    }
    @Test
    @DisplayName("Should raise if won first round and has zap")
    void ShouldRaiseIfWonFirstRoundAndHasZap() {
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(KING, DIAMONDS),
                TrucoCard.of(SIX, CLUBS)
        );
        GameIntel.StepBuilder stepBuilder = (GameIntel.StepBuilder) GameIntel.StepBuilder.with()
                .gameInfo(List.of(WON), List.of(), TrucoCard.of(FIVE, SPADES), 1)
                .botInfo(botCards, 0);

        boolean raise = new TrucoMachineBot().decideIfRaises(stepBuilder.build());
        assertTrue(raise);
    }
    @Test
    @DisplayName("Should raise if won first round and has hearts")
    void ShouldRaiseIfWonFirstRoundAndHasHearts() {
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(SIX, HEARTS),
                TrucoCard.of(KING, CLUBS)
        );
        GameIntel.StepBuilder stepBuilder = (GameIntel.StepBuilder) GameIntel.StepBuilder.with()
                .gameInfo(List.of(WON), List.of(), TrucoCard.of(FIVE, SPADES), 1)
                .botInfo(botCards, 0);

        boolean raise = new TrucoMachineBot().decideIfRaises(stepBuilder.build());
        assertTrue(raise);
    }

    @Test
    @DisplayName("Should raise if score difference is grater than 3 and won first round and has diamond and king")
    void ShouldRaiseIfScoredifferenceIsGreaterThan3AndWonFirstRoundAndHasDiamondAndKing() {
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(SIX, DIAMONDS),
                TrucoCard.of(KING, CLUBS)
        );
        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(WON), List.of(), TrucoCard.of(FIVE, SPADES), 1)
                .botInfo(botCards, 4)
                .opponentScore(0);

        boolean raise = new TrucoMachineBot().decideIfRaises(stepBuilder.build());
        assertTrue(raise);
    }

    @Test
    @DisplayName("Should accept truco when having zap copas")
    void shouldAcceptTrucoWhenHavingZapCopas(){
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(TWO,CLUBS),
                TrucoCard.of(TWO, HEARTS),
                TrucoCard.of(THREE, CLUBS)
        );
        GameIntel.StepBuilder stepBuilder = (GameIntel.StepBuilder) GameIntel.StepBuilder.with()
                .gameInfo(List.of(), List.of(), TrucoCard.of(ACE, SPADES), 1)
                .botInfo(botCards, 0)
                .opponentScore(0);

        int raiseResponse = new TrucoMachineBot().getRaiseResponse(stepBuilder.build());
        assertThat(raiseResponse).isOne();

    }

    @Test
    @DisplayName("Should accept truco when having zap and Manilha in round1")
    void shouldAcceptTrucoWhenHavingZapAndManilhaInRound1(){
        List<GameIntel.RoundResult> roundResults = List.of(

        );

        List<TrucoCard> botCards = List.of(
                TrucoCard.of(THREE,CLUBS),
                TrucoCard.of(THREE, DIAMONDS),
                TrucoCard.of(KING, CLUBS)
        );
        GameIntel.StepBuilder stepBuilder = (GameIntel.StepBuilder) GameIntel.StepBuilder.with()
                .gameInfo(roundResults, List.of(), TrucoCard.of(TWO, SPADES), 1)
                .botInfo(botCards, 0)
                .opponentScore(0);

        int raiseResponse = new TrucoMachineBot().getRaiseResponse(stepBuilder.build());
        assertThat(raiseResponse).isZero();

    }

    @Test
    @DisplayName("Should reraise truco when having zap and Manilha in round2")
    void shouldReraiseTrucoWhenHavingZapAndManilhaInRound2(){
        List<GameIntel.RoundResult> roundResults = List.of(
                GameIntel.RoundResult.LOST
        );

        List<TrucoCard> botCards = List.of(
                TrucoCard.of(THREE,CLUBS),
                TrucoCard.of(THREE, DIAMONDS),
                TrucoCard.of(KING, CLUBS)
        );
        GameIntel.StepBuilder stepBuilder = (GameIntel.StepBuilder) GameIntel.StepBuilder.with()
                .gameInfo(roundResults, List.of(), TrucoCard.of(TWO, SPADES), 1)
                .botInfo(botCards, 0)
                .opponentScore(0);

        int raiseResponse = new TrucoMachineBot().getRaiseResponse(stepBuilder.build());
        assertThat(raiseResponse).isOne();

    }

    @Test
    @DisplayName("should accept truco if round is 0 having zero manilhas but three ternos")
    void shouldAcceptTrucoIfRoundIs0HavingZeroManilhasButThreeTernos(){
        List<GameIntel.RoundResult> roundResults = List.of(
        );

        List<TrucoCard> botCards = List.of(
                TrucoCard.of(THREE,CLUBS),
                TrucoCard.of(THREE, DIAMONDS),
                TrucoCard.of(THREE, SPADES)
        );
        GameIntel.StepBuilder stepBuilder = (GameIntel.StepBuilder) GameIntel.StepBuilder.with()
                .gameInfo(roundResults, List.of(), TrucoCard.of(FIVE, SPADES), 1)
                .botInfo(botCards, 0)
                .opponentScore(0);

        int raiseResponse = new TrucoMachineBot().getRaiseResponse(stepBuilder.build());
        assertThat(raiseResponse).isZero();

    }

    @Test
    @DisplayName("should accept truco if round is 0 having zero manilhas but two ternos")
    void shouldAcceptTrucoIfRoundIs0HavingZeroManilhasButTwoTernos(){

        List<GameIntel.RoundResult> roundResults = List.of(
        );

        List<TrucoCard> botCards = List.of(
                TrucoCard.of(THREE,CLUBS),
                TrucoCard.of(THREE, DIAMONDS),
                TrucoCard.of(TWO, SPADES)
        );
        GameIntel.StepBuilder stepBuilder = (GameIntel.StepBuilder) GameIntel.StepBuilder.with()
                .gameInfo(roundResults, List.of(), TrucoCard.of(FIVE, SPADES), 1)
                .botInfo(botCards, 0)
                .opponentScore(0);

        int raiseResponse = new TrucoMachineBot().getRaiseResponse(stepBuilder.build());
        assertThat(raiseResponse).isZero();
    }

    @Test
    @DisplayName("should accept truco if round is 0 having zero manilhas but one terno and two duques")
    void shouldAcceptTrucoIfRoundIs0HavingZeroManilhasButOneTernoAndTwoDuques(){
        List<GameIntel.RoundResult> roundResults = List.of(
        );

        List<TrucoCard> botCards = List.of(
                TrucoCard.of(THREE,CLUBS),
                TrucoCard.of(TWO, DIAMONDS),
                TrucoCard.of(TWO, SPADES)
        );
        GameIntel.StepBuilder stepBuilder = (GameIntel.StepBuilder) GameIntel.StepBuilder.with()
                .gameInfo(roundResults, List.of(), TrucoCard.of(FIVE, SPADES), 1)
                .botInfo(botCards, 0)
                .opponentScore(0);

        int raiseResponse = new TrucoMachineBot().getRaiseResponse(stepBuilder.build());
        assertThat(raiseResponse).isZero();

    }

    @Test
    @DisplayName("Should not accept truco if round is 0 and dont have manilhas and atleat one terno")
    void shouldNotAcceptTrucoIfRoundIs0AndDontHaveManilhasAndAtleatOneTerno(){
        List<GameIntel.RoundResult> roundResults = List.of(
        );

        List<TrucoCard> botCards = List.of(
                TrucoCard.of(TWO,CLUBS),
                TrucoCard.of(TWO, DIAMONDS),
                TrucoCard.of(TWO, SPADES)
        );
        GameIntel.StepBuilder stepBuilder = (GameIntel.StepBuilder) GameIntel.StepBuilder.with()
                .gameInfo(roundResults, List.of(), TrucoCard.of(FIVE, SPADES), 1)
                .botInfo(botCards, 0)
                .opponentScore(0);

        int raiseResponse = new TrucoMachineBot().getRaiseResponse(stepBuilder.build());
        assertThat(raiseResponse).isNegative();

    }
    
    @Test
    @DisplayName("should accept truco if round is 0 having one Manilha and atleast one terno")
    void shouldAcceptTrucoIfRoundIs0HavingOneManilhaAndAtleastOneTerno(){
        List<GameIntel.RoundResult> roundResults = List.of(
        );

        List<TrucoCard> botCards = List.of(
                TrucoCard.of(SIX,SPADES),
                TrucoCard.of(FOUR, DIAMONDS),
                TrucoCard.of(THREE, SPADES)
        );
        GameIntel.StepBuilder stepBuilder = (GameIntel.StepBuilder) GameIntel.StepBuilder.with()
                .gameInfo(roundResults, List.of(), TrucoCard.of(FIVE, SPADES), 1)
                .botInfo(botCards, 0)
                .opponentScore(0);

        int raiseResponse = new TrucoMachineBot().getRaiseResponse(stepBuilder.build());
        assertThat(raiseResponse).isZero();

    }

    @Test
    @DisplayName("should accept truco if round is 0 having one Zap or one Copas and atleast one duque")
    void shouldAcceptTrucoIfRoundIs0HavingOneZapOrOneCopasAndAtleastOneDuque(){
        List<GameIntel.RoundResult> roundResults = List.of(
        );

        List<TrucoCard> botCards = List.of(
                TrucoCard.of(SIX,CLUBS),
                TrucoCard.of(FOUR, DIAMONDS),
                TrucoCard.of(TWO, SPADES)
        );
        GameIntel.StepBuilder stepBuilder = (GameIntel.StepBuilder) GameIntel.StepBuilder.with()
                .gameInfo(roundResults, List.of(), TrucoCard.of(FIVE, SPADES), 1)
                .botInfo(botCards, 0)
                .opponentScore(0);

        int raiseResponse = new TrucoMachineBot().getRaiseResponse(stepBuilder.build());
        assertThat(raiseResponse).isZero();

    }

}
