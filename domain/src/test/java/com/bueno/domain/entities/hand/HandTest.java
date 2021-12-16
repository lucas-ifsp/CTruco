/*
 *  Copyright (C) 2021 Lucas B. R. de Oliveira - IFSP/SCL
 *  Contact: lucas <dot> oliveira <at> ifsp <dot> edu <dot> br
 *
 *  This file is part of CTruco (Truco game for didactic purpose).
 *
 *  CTruco is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CTruco is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CTruco.  If not, see <https://www.gnu.org/licenses/>
 */

package com.bueno.domain.entities.hand;

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.deck.Rank;
import com.bueno.domain.entities.deck.Suit;
import com.bueno.domain.entities.game.GameRuleViolationException;
import com.bueno.domain.entities.player.util.Player;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.EnumSet;
import java.util.logging.LogManager;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

//TODO Criar casos de teste para garantir que a carta chegando pertence ao jogador

@ExtendWith(MockitoExtension.class)
class HandTest {

    private Hand sut;
    @Mock private Player p1;
    @Mock private Player p2;

    @BeforeAll
    static void init(){
        LogManager.getLogManager().reset();
    }

    @BeforeEach
    void setUp() {
        sut  = new Hand(p1, p2, Card.of(Rank.SEVEN, Suit.CLUBS));
    }

    @AfterEach
    void tearDown() {
        sut = null;
    }

    @Test
    @DisplayName("Should not accept null parameters in any external request")
    void shouldNotAcceptNullParametersInAnyExternalRequest() {
        final Class<NullPointerException> expectedException = NullPointerException.class;
        assertAll(
                () -> assertThrows(expectedException, () -> new Hand(null, null, null)),
                () -> assertThrows(expectedException, () -> sut.playFirstCard(null, Card.closed())),
                () -> assertThrows(expectedException, () -> sut.playFirstCard(p1, null)),
                () -> assertThrows(expectedException, () -> sut.playSecondCard(null, Card.closed())),
                () -> assertThrows(expectedException, () -> sut.playSecondCard(p2, null)),
                () -> assertThrows(expectedException, () -> sut.raiseBet(null)),
                () -> assertThrows(expectedException, () -> sut.accept(null)),
                () -> assertThrows(expectedException, () -> sut.quit(null))
        );
    }

    @Test
    @DisplayName("Should store open cards")
    void shouldStoreOpenCards(){
        sut.playFirstCard(p1, Card.of(Rank.THREE,Suit.SPADES));
        sut.playSecondCard(p2, Card.of(Rank.FOUR,Suit.SPADES));
        sut.playFirstCard(p1, Card.of(Rank.THREE,Suit.HEARTS));
        sut.playSecondCard(p2, Card.of(Rank.FOUR,Suit.HEARTS));
        assertEquals(5, sut.getOpenCards().size());
    }

    @Test
    @DisplayName("Should accept first card if has none")
    void shouldAcceptFirstCardIfHasNone() {
        sut.playFirstCard(p1, Card.of(Rank.KING, Suit.SPADES));
        assertEquals(Card.of(Rank.KING, Suit.SPADES), sut.getCardToPlayAgainst().orElse(null));
    }

    @Test
    @DisplayName("Should not accept last to play playing first card")
    void shouldNotAcceptLastToPlayPlayingFirstCard() {
        assertThrows(IllegalArgumentException.class, () -> sut.playFirstCard(p2, Card.closed()));
    }

    @Test
    @DisplayName("Should not accept first card if has one")
    void shouldNotAcceptFirstCardIfHasOne() {
        sut.playFirstCard(p1, Card.of(Rank.KING, Suit.SPADES));
        assertThrows(IllegalStateException.class, () -> sut.playFirstCard(p2, Card.closed()));
    }

    @Test
    @DisplayName("Should not accept first card after three rounds")
    void shouldNotAcceptFirstCardAfterThreeRounds() {
        sut.playFirstCard(p1, Card.of(Rank.KING, Suit.SPADES));
        sut.playSecondCard(p2, Card.of(Rank.KING, Suit.DIAMONDS));
        sut.playFirstCard(p1, Card.of(Rank.FOUR, Suit.SPADES));
        sut.playSecondCard(p2, Card.of(Rank.FOUR, Suit.DIAMONDS));
        sut.playFirstCard(p1, Card.of(Rank.FIVE, Suit.SPADES));
        sut.playSecondCard(p2, Card.of(Rank.FIVE, Suit.DIAMONDS));
        assertThrows(IllegalArgumentException.class, () -> sut.playFirstCard(p1, Card.closed()));
    }

    @Test
    @DisplayName("Should not accept first card while waiting bet response")
    void shouldNotAcceptFirstCardWhileWaitingBetResponse() {
        sut.raiseBet(p1);
        assertAll(
                () ->  assertThrows(IllegalArgumentException.class, () -> sut.playFirstCard(p1, Card.closed())),
                () ->  assertThrows(IllegalStateException.class, () -> sut.playFirstCard(p2, Card.closed()))
        );
    }

    @Test
    @DisplayName("Should have played a round after playing both cards")
    void shouldHavePlayedARoundAfterPlayingBothCards() {
        sut.playFirstCard(p1, Card.of(Rank.KING, Suit.SPADES));
        sut.playSecondCard(p2, Card.of(Rank.JACK, Suit.SPADES));
        assertEquals(1, sut.numberOfRoundsPlayed());
    }

    @Test
    @DisplayName("Should not accept second card while waiting bet response")
    void shouldNotAcceptSecondCardWhileWaitingBetResponse() {
        sut.playFirstCard(p1, Card.of(Rank.KING, Suit.SPADES));
        sut.raiseBet(p2);
        assertThrows(IllegalStateException.class, () -> sut.playSecondCard(p1, Card.closed()));
    }

    @Test
    @DisplayName("Should not accept a second card if has two")
    void shouldNotAcceptASecondCardIfHasTwo() {
        sut.playFirstCard(p1, Card.of(Rank.KING, Suit.SPADES));
        sut.playSecondCard(p2, Card.of(Rank.JACK, Suit.SPADES));
        assertThrows(IllegalStateException.class, () -> sut.playSecondCard(p1, Card.closed()));
    }

    @Test
    @DisplayName("Should get correct last round winner")
    void shouldGetCorrectLastRoundWinner(){
        sut.playFirstCard(p1, Card.of(Rank.THREE,Suit.SPADES));
        sut.playSecondCard(p2, Card.of(Rank.FOUR,Suit.SPADES));
        assertEquals(p1, sut.getLastRoundWinner().orElse(null));
    }

    @Test
    @DisplayName("Should win hand winning first two rounds")
    void shouldWinHandWinningFirstTwoRounds(){
        sut.playFirstCard(p1, Card.of(Rank.THREE,Suit.SPADES));
        sut.playSecondCard(p2, Card.of(Rank.FOUR,Suit.SPADES));
        sut.playFirstCard(p1, Card.of(Rank.THREE,Suit.HEARTS));
        sut.playSecondCard(p2, Card.of(Rank.FOUR,Suit.HEARTS));
        assertEquals(p1, getPossibleWinner());
    }

    @Test
    @DisplayName("Should win hand tying first and winning second")
    void shouldWinHandTyingFirstAndWinningSecond(){
        sut.playFirstCard(p1, Card.of(Rank.THREE,Suit.SPADES));
        sut.playSecondCard(p2, Card.of(Rank.THREE,Suit.CLUBS));
        sut.playFirstCard(p1, Card.of(Rank.THREE,Suit.HEARTS));
        sut.playSecondCard(p2, Card.of(Rank.FOUR,Suit.HEARTS));
        assertEquals(p1, getPossibleWinner());
    }

    @Test
    @DisplayName("Should win hand winning first and tying second")
    void shouldWinHandWinningFirstAndTyingSecond(){
        sut.playFirstCard(p1, Card.of(Rank.THREE,Suit.SPADES));
        sut.playSecondCard(p2, Card.of(Rank.FOUR,Suit.HEARTS));
        sut.playFirstCard(p1, Card.of(Rank.THREE,Suit.HEARTS));
        sut.playSecondCard(p2, Card.of(Rank.THREE,Suit.CLUBS));
        assertEquals(p1, getPossibleWinner());
    }

    @Test
    @DisplayName("Should draw hand with three tied rounds")
    void shouldDrawHandWithThreeTiedRounds(){
        sut.playFirstCard(p1, Card.of(Rank.THREE,Suit.SPADES));
        sut.playSecondCard(p2, Card.of(Rank.THREE,Suit.HEARTS));
        sut.playFirstCard(p1, Card.of(Rank.TWO,Suit.HEARTS));
        sut.playSecondCard(p2, Card.of(Rank.TWO,Suit.CLUBS));
        sut.playFirstCard(p1, Card.of(Rank.ACE,Suit.HEARTS));
        sut.playSecondCard(p2, Card.of(Rank.ACE,Suit.CLUBS));
        assertNull(getPossibleWinner());
    }

    @Test
    @DisplayName("Should win hand by best of three")
    void shouldWinHandByBestOfThree(){
        sut.playFirstCard(p1, Card.of(Rank.THREE,Suit.SPADES));
        sut.playSecondCard(p2, Card.of(Rank.TWO,Suit.CLUBS));
        sut.playFirstCard(p1, Card.of(Rank.TWO,Suit.SPADES));
        sut.playSecondCard(p2, Card.of(Rank.THREE,Suit.CLUBS));
        sut.playFirstCard(p2, Card.of(Rank.KING, Suit.SPADES));
        sut.playSecondCard(p1, Card.of(Rank.ACE, Suit.CLUBS));
        assertEquals(p1, getPossibleWinner());
    }

    @Test
    @DisplayName("Should win hand winning first and tying third")
    void shouldWinHandWinningFirstAndTyingThird(){
        sut.playFirstCard(p1, Card.of(Rank.THREE,Suit.SPADES));
        sut.playSecondCard(p2, Card.of(Rank.TWO,Suit.CLUBS));
        sut.playFirstCard(p1, Card.of(Rank.TWO,Suit.SPADES));
        sut.playSecondCard(p2, Card.of(Rank.THREE,Suit.CLUBS));
        sut.playFirstCard(p2, Card.of(Rank.ACE, Suit.SPADES));
        sut.playSecondCard(p1, Card.of(Rank.ACE, Suit.CLUBS));
        assertEquals(p1, getPossibleWinner());
    }

    @Test
    @DisplayName("Should first round winner be the second round first to play")
    void shouldFirstRoundWinnerBeTheSecondRoundFirstToPlay() {
        sut.playFirstCard(p1, Card.of(Rank.TWO,Suit.SPADES));
        sut.playSecondCard(p2, Card.of(Rank.THREE,Suit.CLUBS));
        assertEquals(p2, sut.getCurrentPlayer());
    }

    @Test
    @DisplayName("Should first to play in second round the same one if first round is tied.")
    void shouldFirstToPlayInSecondRoundTheSameOneIfFirstRoundIsTied() {
        sut.playFirstCard(p1, Card.of(Rank.THREE,Suit.SPADES));
        sut.playSecondCard(p2, Card.of(Rank.THREE,Suit.CLUBS));
        assertEquals(p1, sut.getCurrentPlayer());
    }

    @Test
    @DisplayName("Should second round winner be the third round first to play")
    void shouldSecondRoundWinnerBeTheThirdRoundFirstToPlay() {
        sut.playFirstCard(p1, Card.of(Rank.THREE,Suit.SPADES));
        sut.playSecondCard(p2, Card.of(Rank.TWO,Suit.CLUBS));
        sut.playFirstCard(p1, Card.of(Rank.KING,Suit.SPADES));
        sut.playSecondCard(p2, Card.of(Rank.ACE,Suit.CLUBS));
        assertEquals(p2, sut.getCurrentPlayer());
    }

    @Test
    @DisplayName("Should not allow opponent to play second card in player turn")
    void shouldNotAllowOpponentToPlaySecondCardInPlayerTurn() {
        sut.playFirstCard(p1, Card.of(Rank.THREE, Suit.SPADES));
        assertThrows(IllegalArgumentException.class, () -> sut.playSecondCard(p1, Card.closed()));
    }

    @Test
    @DisplayName("Should opponent be the current player after the player raises the bet")
    void shouldOpponentBeTheCurrentPlayerAfterThePlayerRaisesTheBet() {
        sut.raiseBet(p1);
        assertEquals(p2, sut.getCurrentPlayer());
    }

    @Test
    @DisplayName("Should not allow opponent raise the bet during player turn")
    void shouldNotAllowOpponentRaiseTheBetDuringPlayerTurn() {
        assertThrows(IllegalArgumentException.class, () -> sut.raiseBet(p2));
    }

    @Test
    @DisplayName("Should not allow a player to accept its own raise bet")
    void shouldNotAllowAPlayerToAcceptItsOwnRaiseBet() {
        sut.raiseBet(p1);
        assertThrows(IllegalArgumentException.class, () -> sut.accept(p1));
    }

    @Test
    @DisplayName("Should raise hand score if bet is accepted")
    void shouldRaiseHandScoreIfBetIsAccepted() {
        sut.raiseBet(p1);
        sut.accept(p2);
        assertEquals(HandScore.THREE, sut.getScore());
    }

    @Test
    @DisplayName("Should be able to re-raise as answer for a raise")
    void shouldBeAbleToReRaiseAsAnswerForARaise() {
        sut.raiseBet(p1);
        sut.raiseBet(p2);
        sut.accept(p1);
        assertEquals(HandScore.SIX, sut.getScore());
    }

    @Test
    @DisplayName("Should be able to play first card after raising bet")
    void shouldBeAbleToPlayFirstCardAfterRaisingBet() {
        sut.raiseBet(p1);
        sut.accept(p2);
        sut.playFirstCard(p1, Card.closed());
        assertEquals(Card.closed(), sut.getCardToPlayAgainst().orElse(null));
    }

    @Test
    @DisplayName("Should not allow the same player to raise the bet consecutively")
    void shouldNotAllowTheSamePlayerToRaiseTheBetConsecutively() {
        sut.raiseBet(p1);
        sut.accept(p2);
        assertThrows(IllegalStateException.class, () -> sut.raiseBet(p1));
    }

    @Test
    @DisplayName("Should be able to play second card after raising bet")
    void shouldBeAbleToPlaySecondCardAfterRaisingBet() {
        sut.playFirstCard(p1, Card.closed());
        sut.raiseBet(p2);
        sut.accept(p1);
        sut.playSecondCard(p2, Card.closed());
        assertEquals(1, sut.numberOfRoundsPlayed());
    }

    @Test
    @DisplayName("Should not allow accepting a bet if no bet raise has been made")
    void shouldNotAllowAcceptingABetIfNoBetRaiseHasBeenMade() {
        assertAll(
                () -> assertThrows(IllegalStateException.class, () -> sut.accept(p1)),
                () -> {
                    sut.playFirstCard(p1, Card.closed());
                    assertThrows(IllegalStateException.class, () -> sut.accept(p2));
                }
        );
    }

    @Test
    @DisplayName("Should not allow quiting a bet if no bet raise has been made")
    void shouldNotAllowQuitingABetIfNoBetRaiseHasBeenMade() {
        assertAll(
                () -> assertThrows(IllegalStateException.class, () -> sut.quit(p1)),
                () -> {
                    sut.playFirstCard(p1, Card.closed());
                    assertThrows(IllegalStateException.class, () -> sut.quit(p2));
                }
        );
    }

    @Test
    @DisplayName("Should not allow quiting others bet raise request")
    void shouldNotAllowQuitingOthersBetRaiseRequest() {
        sut.raiseBet(p1);
        assertThrows(IllegalArgumentException.class, () -> sut.quit(p1));
    }

    @Test
    @DisplayName("Should return not winner and match worth 12 points")
    void shouldReturnNoWinnerAndMatchWorth12Points(){
        sut.raiseBet(p1);
        sut.raiseBet(p2);
        sut.raiseBet(p1);
        sut.raiseBet(p2);
        sut.accept(p1);
        assertAll(
                () -> assertEquals(HandScore.TWELVE, sut.getScore()),
                () -> assertNull(getPossibleWinner())
        );
    }

    @Test
    @DisplayName("Should return winner after call and then run")
    void shouldReturnWinnerAfterCallAndThenRun(){
        sut.raiseBet(p1);
        sut.raiseBet(p2);
        sut.quit(p1);
        assertAll(
                () -> assertEquals(HandScore.THREE, sut.getScore()),
                () -> assertEquals(p2, getPossibleWinner())
        );
    }

    @Test
    @DisplayName("Should not allow to raise bet above twelve points")
    void shouldNotAllowToRaiseBetAboveTwelvePoints() {
        sut.raiseBet(p1);
        sut.raiseBet(p2);
        sut.raiseBet(p1);
        sut.raiseBet(p2);
        assertAll(
                () -> assertThrows(GameRuleViolationException.class, () -> sut.raiseBet(p1)),
                () -> assertEquals(p1, sut.getCurrentPlayer())
        );
    }

    @Test
    @DisplayName("Should allow to raise the bet just enough to enable both players to win")
    void shouldAllowToRaiseTheBetJustEnoughToEnableBothPlayersToWin() {
        sut.raiseBet(p1);
        sut.raiseBet(p2);
        sut.raiseBet(p1);
        sut.accept(p2);
        assertEquals(HandScore.NINE, sut.getScore());
    }

    @Test
    @DisplayName("Should not allow to raise more than enough to both players win")
    void shouldNotAllowToRaiseMoreThanEnoughToBothPlayersWin() {
        when(p1.getScore()).thenReturn(5);
        when(p2.getScore()).thenReturn(8);
        sut.raiseBet(p1);
        sut.raiseBet(p2);
        sut.raiseBet(p1);
        assertAll(
                () -> assertThrows(GameRuleViolationException.class, () -> sut.raiseBet(p2)),
                () -> assertEquals(p2, sut.getCurrentPlayer())
        );
    }

    @Test
    @DisplayName("Should lose the hand if request to raise the bet when any user has 11 points")
    void shouldLoseTheHandIfRequestToRaiseTheBetWhenAnyUserHas11Points() {
        when(p1.getScore()).thenReturn(11);
        sut.raiseBet(p1);
        assertEquals(p2, getPossibleWinner());
    }

    @Test
    @DisplayName("Should lose the hand if request to raise the bet when any user has 11 points after a round card was played")
    void shouldLoseTheHandIfRequestToRaiseTheBetWhenAnyUserHas11PointsAfterARoundCardWasPlayed() {
        when(p1.getScore()).thenReturn(11);
        sut.playFirstCard(p1, Card.closed());
        sut.raiseBet(p2);
        assertEquals(p1, getPossibleWinner());
    }

    @Test
    @DisplayName("Should lose the hand if quits a bet")
    void shouldLoseTheHandIfQuitsABet() {
        sut.raiseBet(p1);
        sut.quit(p2);
        assertEquals(p1, getPossibleWinner());
    }

    @Test
    @DisplayName("Should be mao de onze if at least one player has 11 score points")
    void shouldBeMaoDeOnzeIfAtLeastOnePlayerHas11ScorePoints() {
        when(p2.getScore()).thenReturn(11);
        assertTrue(sut.isMaoDeOnze());
    }

    @Test
    @DisplayName("Should not allow to play first card before deciding if plays mao de onze")
    void shouldNotAllowToPlayCardFirstBeforeDecidingIfPlaysMaoDeOnze() {
        when(p1.getScore()).thenReturn(11);
        sut = new Hand(p1, p2, Card.of(Rank.SEVEN, Suit.CLUBS));
        assertThrows(IllegalStateException.class, () -> sut.playFirstCard(p1, Card.closed()));
    }

    @Test
    @DisplayName("Should not allow to play second card before deciding if plays mao de onze")
    void shouldNotAllowToPlaySecondCardBeforeDecidingIfPlaysMaoDeOnze() {
        when(p1.getScore()).thenReturn(11);
        sut = new Hand(p1, p2, Card.of(Rank.SEVEN, Suit.CLUBS));
        assertThrows(IllegalStateException.class, () -> sut.playSecondCard(p1, Card.closed()));
    }

    @Test
    @DisplayName("Should not allow to raise bet while deciding if plays mao de onze")
    void shouldNotAllowToRaiseBetWhileDecidingIfPlaysMaoDeOnze() {
        when(p1.getScore()).thenReturn(11);
        sut = new Hand(p1, p2, Card.of(Rank.SEVEN, Suit.CLUBS));
        assertThrows(IllegalStateException.class, () -> sut.raiseBet(p1));
    }


    @Test
    @DisplayName("Should be able to play card after deciding if plays mao de onze")
    void shouldBeAbleToPlayCardAfterDecidingIfPlaysMaoDeOnze() {
        when(p1.getScore()).thenReturn(11);
        sut = new Hand(p1, p2, Card.of(Rank.SEVEN, Suit.CLUBS));
        sut.accept(p1);
        assertDoesNotThrow(() -> sut.playFirstCard(p1, Card.closed()));
    }

    @Test
    @DisplayName("Should hand worth 3 points if player accepts mao de onze")
    void shouldHandWorth3PointsIfPlayerAcceptsMaoDeOnze() {
        when(p1.getScore()).thenReturn(11);
        sut = new Hand(p1, p2, Card.of(Rank.SEVEN, Suit.CLUBS));
        sut.accept(p1);
        assertEquals(HandScore.THREE, sut.getScore());
    }

    @Test
    @DisplayName("Should opponent win 1 point if player quits mao de onze")
    void shouldOpponentWin1PointIfPlayerQuitsMaoDeOnze() {
        when(p1.getScore()).thenReturn(11);
        sut = new Hand(p1, p2, Card.of(Rank.SEVEN, Suit.CLUBS));
        sut.quit(p1);
        assertEquals(new HandResult(p2, HandScore.ONE), sut.getResult().orElse(null));
    }

    @Test
    @DisplayName("Should hand be done after player quits mao de onze")
    void shouldHandBeDoneAfterPlayerQuitsMaoDeOnze() {
        when(p1.getScore()).thenReturn(11);
        sut = new Hand(p1, p2, Card.of(Rank.SEVEN, Suit.CLUBS));
        sut.quit(p1);
        assertTrue(sut.isDone());
    }

    @Test
    @DisplayName("Should current player be able to play card or raise bet when the hand begins")
    void shouldCurrentPlayerBeAbleToPlayCardOrRaiseBetWhenTheHandBegins() {
        EnumSet<PossibleActions> possibleActions = EnumSet.of(PossibleActions.PLAY, PossibleActions.RAISE);
        assertEquals(possibleActions, sut.getPossibleActions());
    }

    @Test
    @DisplayName("Should current player be able only to accept or quit when hand begins in mao de onze")
    void shouldCurrentPlayerBeAbleOnlyToAcceptOrQuitWhenHandBeginsInMaoDeOnze() {
        when(p1.getScore()).thenReturn(11);
        sut = new Hand(p1, p2, Card.of(Rank.SEVEN, Suit.CLUBS));
        EnumSet<PossibleActions> possibleActions = EnumSet.of(PossibleActions.ACCEPT_HAND, PossibleActions.QUIT_HAND);
        assertEquals(possibleActions, sut.getPossibleActions());
    }

    @Test
    @DisplayName("Should current player be able to play card or raise bet after first round card is played")
    void shouldCurrentPlayerBeAbleToPlayCardOrRaiseBetAfterFirstRoundCardIsPlayed() {
        EnumSet<PossibleActions> possibleActions = EnumSet.of(PossibleActions.PLAY, PossibleActions.RAISE);
        sut.playFirstCard(p1, Card.closed());
        assertEquals(possibleActions, sut.getPossibleActions());
    }

    @Test
    @DisplayName("Should current player be able to quit or accept or raise after a player requested to raise a bet")
    void shouldCurrentPlayerBeAbleToQuitOrAcceptOrRaiseAfterAPlayerRequestedToRaiseABet() {
        EnumSet<PossibleActions> possibleActions = EnumSet.of(PossibleActions.QUIT, PossibleActions.ACCEPT, PossibleActions.RAISE);
        sut.raiseBet(p1);
        assertEquals(possibleActions, sut.getPossibleActions());
    }

    @Test
    @DisplayName("Should current player only be able to play after opponent accepted the bet raise")
    void shouldCurrentPlayerOnlyBeAbleToPlayAfterOpponentAcceptedTheBetRaise() {
        EnumSet<PossibleActions> possibleActions = EnumSet.of(PossibleActions.PLAY);
        sut.raiseBet(p1);
        sut.accept(p2);
        assertEquals(possibleActions, sut.getPossibleActions());
    }

    @Test
    @DisplayName("Should current player only be able to play after opponent accepted the bet raise with one card already played")
    void shouldCurrentPlayerOnlyBeAbleToPlayAfterOpponentAcceptedTheBetRaiseWithOneCardAlreadyPlayed() {
        EnumSet<PossibleActions> possibleActions = EnumSet.of(PossibleActions.PLAY);
        sut.playFirstCard(p1, Card.closed());
        sut.raiseBet(p2);
        sut.accept(p1);
        assertEquals(possibleActions, sut.getPossibleActions());
    }

    @Test
    @DisplayName("Should current player only be able to play if any user has 11 points")
    void shouldCurrentPlayerOnlyBeAbleToPlayIfAnyUserHas11Points() {
        when(p1.getScore()).thenReturn(11);
        sut.playFirstCard(p1, Card.closed());
        sut.playSecondCard(p2, Card.closed());
        EnumSet<PossibleActions> possibleActions = EnumSet.of(PossibleActions.PLAY);
        assertEquals(possibleActions, sut.getPossibleActions());
    }

    @Test
    @DisplayName("Should current player only be able to play if any user has 11 points and a round card was already player")
    void shouldCurrentPlayerOnlyBeAbleToPlayIfAnyUserHas11PointsAndARoundCardWasAlreadyPlayed() {
        when(p1.getScore()).thenReturn(11);
        sut.playFirstCard(p1, Card.closed());
        EnumSet<PossibleActions> possibleActions = EnumSet.of(PossibleActions.PLAY);
        assertEquals(possibleActions, sut.getPossibleActions());
    }

    @Test
    @DisplayName("Should current player be able of nothing is the hand is done")
    void shouldCurrentPlayerBeAbleOfNothingIsTheHandIsDone() {
        EnumSet<PossibleActions> possibleActions = EnumSet.noneOf(PossibleActions.class);
        sut.raiseBet(p1);
        sut.quit(p2);
        assertEquals(possibleActions, sut.getPossibleActions());
    }

    @Test
    @DisplayName("Should throw if any action is requested when the hand is done")
    void shouldThrowIfAnyActionIsRequestedWhenTheHandIsDone() {
        sut.raiseBet(p1);
        sut.quit(p2);
        final Class<IllegalArgumentException> expectedException = IllegalArgumentException.class;
        assertAll(
                () -> assertThrows(expectedException, () -> sut.playFirstCard(p1, Card.closed())),
                () -> assertThrows(expectedException, () -> sut.playSecondCard(p1, Card.closed())),
                () -> assertThrows(expectedException, () -> sut.accept(p2)),
                () -> assertThrows(expectedException, () -> sut.quit(p2)),
                () -> assertThrows(expectedException, () -> sut.raiseBet(p2))
        );
    }

    @Test
    @DisplayName("Should have no current player if hand is done")
    void shouldHaveNoCurrentPlayerIfHandIsDone() {
        sut.raiseBet(p1);
        sut.quit(p2);
        assertNull(sut.getCurrentPlayer());
    }

    private Player getPossibleWinner() {
        return sut.getResult().flatMap(HandResult::getWinner).orElse(null);
    }
}