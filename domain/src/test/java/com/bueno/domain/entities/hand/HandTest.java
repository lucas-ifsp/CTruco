/*
 *  Copyright (C) 2022 Lucas B. R. de Oliveira - IFSP/SCL
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
import com.bueno.domain.entities.deck.Deck;
import com.bueno.domain.entities.deck.Rank;
import com.bueno.domain.entities.deck.Suit;
import com.bueno.domain.entities.game.GameRuleViolationException;
import com.bueno.domain.entities.intel.PossibleAction;
import com.bueno.domain.entities.player.Player;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.EnumSet;
import java.util.List;

import static com.bueno.domain.entities.hand.HandPoints.*;
import static com.bueno.domain.entities.intel.PossibleAction.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HandTest {

    private Hand sut;
    @Mock private Player player1;
    @Mock private Player player2;

    @BeforeEach
    void setUp() {
        Deck deck = new Deck();
        when(player1.getCards()).thenReturn(deck.take(40));
        sut = new Hand(player1, player2, Card.of(Rank.SEVEN, Suit.CLUBS));
    }

    @AfterEach
    void tearDown() {
        sut = null;
    }

    @Nested
    @DisplayName("When playing ")
    class PlayCardTest {

        @Test
        @DisplayName("Should store open cards")
        void shouldStoreOpenCards() {
            sut.playFirstCard(player1, Card.of(Rank.THREE, Suit.SPADES));
            sut.playSecondCard(player2, Card.of(Rank.FOUR, Suit.SPADES));
            sut.playFirstCard(player1, Card.of(Rank.THREE, Suit.HEARTS));
            sut.playSecondCard(player2, Card.of(Rank.FOUR, Suit.HEARTS));
            assertThat(sut.getOpenCards().size()).isEqualTo(5);
        }

        @Test
        @DisplayName("Should accept first card if has none")
        void shouldAcceptFirstCardIfHasNone() {
            sut.playFirstCard(player1, Card.of(Rank.KING, Suit.SPADES));
            assertThat(sut.getCardToPlayAgainst().orElse(null)).isEqualTo(Card.of(Rank.KING, Suit.SPADES));
        }

        @Test
        @DisplayName("Should not accept last to play playing first card")
        void shouldNotAcceptLastToPlayPlayingFirstCard() {
            assertThatIllegalArgumentException().isThrownBy(() -> sut.playFirstCard(player2, Card.closed()));
        }

        @Test
        @DisplayName("Should not accept first card if has one")
        void shouldNotAcceptFirstCardIfHasOne() {
            sut.playFirstCard(player1, Card.of(Rank.KING, Suit.SPADES));
            assertThatIllegalStateException().isThrownBy(() -> sut.playFirstCard(player2, Card.closed()));
        }

        @Test
        @DisplayName("Should not accept first card after three rounds")
        void shouldNotAcceptFirstCardAfterThreeRounds() {
            sut.playFirstCard(player1, Card.of(Rank.KING, Suit.SPADES));
            sut.playSecondCard(player2, Card.of(Rank.KING, Suit.DIAMONDS));
            sut.playFirstCard(player1, Card.of(Rank.FOUR, Suit.SPADES));
            sut.playSecondCard(player2, Card.of(Rank.FOUR, Suit.DIAMONDS));
            sut.playFirstCard(player1, Card.of(Rank.FIVE, Suit.SPADES));
            sut.playSecondCard(player2, Card.of(Rank.FIVE, Suit.DIAMONDS));
            assertThatIllegalArgumentException().isThrownBy(() -> sut.playFirstCard(player1, Card.closed()));
        }

        @Test
        @DisplayName("Should not accept first card while waiting bet response")
        void shouldNotAcceptFirstCardWhileWaitingBetResponse() {
            sut.raise(player1);
            SoftAssertions softly = new SoftAssertions();
            softly.assertThatThrownBy(() -> sut.playFirstCard(player1, Card.closed()))
                    .as("Player1 is playing card before responding request")
                    .isInstanceOf(IllegalArgumentException.class);
            softly.assertThatThrownBy(() -> sut.playFirstCard(player2, Card.closed()))
                    .as("Player2 is playing out of turn")
                    .isInstanceOf(IllegalStateException.class);
            softly.assertAll();
        }

        @Test
        @DisplayName("Should have played a round after playing both cards")
        void shouldHavePlayedARoundAfterPlayingBothCards() {
            sut.playFirstCard(player1, Card.of(Rank.KING, Suit.SPADES));
            sut.playSecondCard(player2, Card.of(Rank.JACK, Suit.SPADES));
            assertThat(sut.numberOfRoundsPlayed()).isOne();
        }

        @Test
        @DisplayName("Should not accept second card while waiting bet response")
        void shouldNotAcceptSecondCardWhileWaitingBetResponse() {
            sut.playFirstCard(player1, Card.of(Rank.KING, Suit.SPADES));
            sut.raise(player2);
            assertThatIllegalStateException().isThrownBy(() -> sut.playSecondCard(player1, Card.closed()));
        }

        @Test
        @DisplayName("Should not accept a second card if has two")
        void shouldNotAcceptASecondCardIfHasTwo() {
            sut.playFirstCard(player1, Card.of(Rank.KING, Suit.SPADES));
            sut.playSecondCard(player2, Card.of(Rank.JACK, Suit.SPADES));
            assertThatIllegalStateException().isThrownBy(() -> sut.playSecondCard(player1, Card.closed()));
        }

        @Test
        @DisplayName("Should not accept repeated cards")
        void shouldNotAcceptRepeatedCards() {
            sut.playFirstCard(player1, Card.of(Rank.KING, Suit.SPADES));
            sut.playSecondCard(player2, Card.of(Rank.FOUR, Suit.CLUBS));
            assertThatExceptionOfType(GameRuleViolationException.class)
                    .isThrownBy(() -> sut.playFirstCard(player1, Card.of(Rank.KING, Suit.SPADES)));

        }

        @Test
        @DisplayName("Should not accept first player throw closed card in first round")
        void shouldNotAcceptFirstPlayerThrowClosedCardInFirstRound() {
            assertThatExceptionOfType(GameRuleViolationException.class)
                    .isThrownBy(() -> sut.playFirstCard(player1, Card.closed()));
        }

        @Test
        @DisplayName("Should not accept second player throw closed card in first round")
        void shouldNotAcceptSecondPlayerThrowClosedCardInFirstRound() {
            sut.playFirstCard(player1, Card.of(Rank.KING, Suit.SPADES));
            assertThatExceptionOfType(GameRuleViolationException.class)
                    .isThrownBy(() -> sut.playSecondCard(player2, Card.closed()));
        }

        @Test
        @DisplayName("Should not accept card that has not been dealt")
        void shouldNotAcceptCardThatHasNotBeenDealt() {
            when(player1.getCards()).thenReturn(List.of(Card.of(Rank.SEVEN, Suit.SPADES)));
            sut = new Hand(player1, player2, Card.of(Rank.SEVEN, Suit.CLUBS));
            assertThatExceptionOfType(GameRuleViolationException.class)
                    .isThrownBy(() -> sut.playFirstCard(player1, Card.of(Rank.KING, Suit.HEARTS)));
        }

        @Test
        @DisplayName("Should win hand winning first two rounds")
        void shouldWinHandWinningFirstTwoRounds() {
            sut.playFirstCard(player1, Card.of(Rank.THREE, Suit.SPADES));
            sut.playSecondCard(player2, Card.of(Rank.FOUR, Suit.SPADES));
            sut.playFirstCard(player1, Card.of(Rank.THREE, Suit.HEARTS));
            sut.playSecondCard(player2, Card.of(Rank.FOUR, Suit.HEARTS));
            assertThat(getPossibleWinner()).isEqualTo(player1);
        }

        @Test
        @DisplayName("Should win hand tying first and winning second")
        void shouldWinHandTyingFirstAndWinningSecond() {
            sut.playFirstCard(player1, Card.of(Rank.THREE, Suit.SPADES));
            sut.playSecondCard(player2, Card.of(Rank.THREE, Suit.CLUBS));
            sut.playFirstCard(player1, Card.of(Rank.THREE, Suit.HEARTS));
            sut.playSecondCard(player2, Card.of(Rank.FOUR, Suit.HEARTS));
            assertThat(getPossibleWinner()).isEqualTo(player1);
        }

        @Test
        @DisplayName("Should win hand winning first and tying second")
        void shouldWinHandWinningFirstAndTyingSecond() {
            sut.playFirstCard(player1, Card.of(Rank.THREE, Suit.SPADES));
            sut.playSecondCard(player2, Card.of(Rank.FOUR, Suit.HEARTS));
            sut.playFirstCard(player1, Card.of(Rank.THREE, Suit.HEARTS));
            sut.playSecondCard(player2, Card.of(Rank.THREE, Suit.CLUBS));
            assertThat(getPossibleWinner()).isEqualTo(player1);
        }

        @Test
        @DisplayName("Should draw hand with three tied rounds")
        void shouldDrawHandWithThreeTiedRounds() {
            sut.playFirstCard(player1, Card.of(Rank.THREE, Suit.SPADES));
            sut.playSecondCard(player2, Card.of(Rank.THREE, Suit.HEARTS));
            sut.playFirstCard(player1, Card.of(Rank.TWO, Suit.HEARTS));
            sut.playSecondCard(player2, Card.of(Rank.TWO, Suit.CLUBS));
            sut.playFirstCard(player1, Card.of(Rank.ACE, Suit.HEARTS));
            sut.playSecondCard(player2, Card.of(Rank.ACE, Suit.CLUBS));
            assertThat(getPossibleWinner()).isNull();
        }

        @Test
        @DisplayName("Should win hand by best of three")
        void shouldWinHandByBestOfThree() {
            sut.playFirstCard(player1, Card.of(Rank.THREE, Suit.SPADES));
            sut.playSecondCard(player2, Card.of(Rank.TWO, Suit.CLUBS));
            sut.playFirstCard(player1, Card.of(Rank.TWO, Suit.SPADES));
            sut.playSecondCard(player2, Card.of(Rank.THREE, Suit.CLUBS));
            sut.playFirstCard(player2, Card.of(Rank.KING, Suit.SPADES));
            sut.playSecondCard(player1, Card.of(Rank.ACE, Suit.CLUBS));
            assertThat(getPossibleWinner()).isEqualTo(player1);
        }

        @Test
        @DisplayName("Should win hand winning first and tying third")
        void shouldWinHandWinningFirstAndTyingThird() {
            sut.playFirstCard(player1, Card.of(Rank.THREE, Suit.SPADES));
            sut.playSecondCard(player2, Card.of(Rank.TWO, Suit.CLUBS));
            sut.playFirstCard(player1, Card.of(Rank.TWO, Suit.SPADES));
            sut.playSecondCard(player2, Card.of(Rank.THREE, Suit.CLUBS));
            sut.playFirstCard(player2, Card.of(Rank.ACE, Suit.SPADES));
            sut.playSecondCard(player1, Card.of(Rank.ACE, Suit.CLUBS));
            assertThat(getPossibleWinner()).isEqualTo(player1);
        }

        @Test
        @DisplayName("Should first round winner be the second round first to play")
        void shouldFirstRoundWinnerBeTheSecondRoundFirstToPlay() {
            sut.playFirstCard(player1, Card.of(Rank.TWO, Suit.SPADES));
            sut.playSecondCard(player2, Card.of(Rank.THREE, Suit.CLUBS));
            assertThat(sut.getCurrentPlayer()).isEqualTo(player2);
        }

        @Test
        @DisplayName("Should first to play in second round the same one if first round is tied.")
        void shouldFirstToPlayInSecondRoundTheSameOneIfFirstRoundIsTied() {
            sut.playFirstCard(player1, Card.of(Rank.THREE, Suit.SPADES));
            sut.playSecondCard(player2, Card.of(Rank.THREE, Suit.CLUBS));
            assertThat(sut.getCurrentPlayer()).isEqualTo(player1);
        }

        @Test
        @DisplayName("Should second round winner be the third round first to play")
        void shouldSecondRoundWinnerBeTheThirdRoundFirstToPlay() {
            sut.playFirstCard(player1, Card.of(Rank.THREE, Suit.SPADES));
            sut.playSecondCard(player2, Card.of(Rank.TWO, Suit.CLUBS));
            sut.playFirstCard(player1, Card.of(Rank.KING, Suit.SPADES));
            sut.playSecondCard(player2, Card.of(Rank.ACE, Suit.CLUBS));
            assertThat(sut.getCurrentPlayer()).isEqualTo(player2);
        }

        @Test
        @DisplayName("Should not allow opponent to play second card in player turn")
        void shouldNotAllowOpponentToPlaySecondCardInPlayerTurn() {
            sut.playFirstCard(player1, Card.of(Rank.THREE, Suit.SPADES));
            assertThatIllegalArgumentException().isThrownBy(() -> sut.playSecondCard(player1, Card.closed()));
        }

        @Test
        @DisplayName("Should current player be able to play card or raise bet when the hand begins")
        void shouldCurrentPlayerBeAbleToPlayCardOrRaiseBetWhenTheHandBegins() {
            EnumSet<PossibleAction> possibleActions = EnumSet.of(PLAY, RAISE);
            assertThat(sut.getPossibleActions()).isEqualTo(possibleActions);
        }

        @Test
        @DisplayName("Should current player be able to play card or raise bet after first round card is played")
        void shouldCurrentPlayerBeAbleToPlayCardOrRaiseBetAfterFirstRoundCardIsPlayed() {
            EnumSet<PossibleAction> possibleActions = EnumSet.of(RAISE, PLAY);
            sut.playFirstCard(player1, Card.of(Rank.THREE, Suit.CLUBS));
            assertThat(sut.getPossibleActions()).isEqualTo(possibleActions);
        }
    }

    @Nested
    @DisplayName("When raising bet ")
    class RaisingBetTest {

        @Test
        @DisplayName("Should opponent be the current player after the player raises the bet")
        void shouldOpponentBeTheCurrentPlayerAfterThePlayerRaisesTheBet() {
            sut.raise(player1);
            assertThat(sut.getCurrentPlayer()).isEqualTo(player2);
        }

        @Test
        @DisplayName("Should not allow opponent raise the bet during player turn")
        void shouldNotAllowOpponentRaiseTheBetDuringPlayerTurn() {
            assertThatIllegalArgumentException().isThrownBy(() -> sut.raise(player2));
        }

        @Test
        @DisplayName("Should not allow a player to accept its own raise bet")
        void shouldNotAllowAPlayerToAcceptItsOwnRaiseBet() {
            sut.raise(player1);
            assertThatIllegalArgumentException().isThrownBy(() -> sut.accept(player1));
        }

        @Test
        @DisplayName("Should raise hand points if bet is accepted")
        void shouldRaiseHandPointsIfBetIsAccepted() {
            sut.raise(player1);
            sut.accept(player2);
            assertThat(sut.getPoints()).isEqualTo(THREE);
        }

        @Test
        @DisplayName("Should be able to re-raise as answer for a raise")
        void shouldBeAbleToReRaiseAsAnswerForARaise() {
            sut.raise(player1);
            sut.raise(player2);
            sut.accept(player1);
            assertThat(sut.getPoints()).isEqualTo(HandPoints.SIX);
        }

        @Test
        @DisplayName("Should be able to play first card after raising bet")
        void shouldBeAbleToPlayFirstCardAfterRaisingBet() {
            sut.raise(player1);
            sut.accept(player2);
            sut.playFirstCard(player1, Card.of(Rank.THREE, Suit.CLUBS));
            assertThat(sut.getCardToPlayAgainst().orElse(null)).isEqualTo(Card.of(Rank.THREE, Suit.CLUBS));
        }

        @Test
        @DisplayName("Should not allow the same player to raise the bet consecutively")
        void shouldNotAllowTheSamePlayerToRaiseTheBetConsecutively() {
            sut.raise(player1);
            sut.accept(player2);
            assertThatIllegalStateException().isThrownBy(() -> sut.raise(player1));
        }

        @Test
        @DisplayName("Should be able to play second card after raising bet")
        void shouldBeAbleToPlaySecondCardAfterRaisingBet() {
            sut.playFirstCard(player1, Card.of(Rank.THREE, Suit.CLUBS));
            sut.raise(player2);
            sut.accept(player1);
            sut.playSecondCard(player2, Card.of(Rank.TWO, Suit.CLUBS));
            assertThat(sut.numberOfRoundsPlayed()).isOne();
        }

        @Test
        @DisplayName("Should not allow accepting a bet if no bet raise has been made")
        void shouldNotAllowAcceptingABetIfNoBetRaiseHasBeenMade() {
            SoftAssertions softly = new SoftAssertions();

            softly.assertThatThrownBy(() -> sut.accept(player1))
                    .as("There is no request to accept before playing first card")
                    .isInstanceOf(IllegalStateException.class);

            softly.assertThatThrownBy(() -> {
                        sut.playFirstCard(player1, Card.of(Rank.THREE, Suit.CLUBS));
                        sut.accept(player2);
                    })
                    .as("There is no request to accept after playing first card")
                    .isInstanceOf(IllegalStateException.class);

            softly.assertAll();
        }

        @Test
        @DisplayName("Should not allow quiting a bet if no bet raise has been made")
        void shouldNotAllowQuitingABetIfNoBetRaiseHasBeenMade() {
            SoftAssertions softly = new SoftAssertions();
            softly.assertThatThrownBy(() -> sut.quit(player1))
                    .as("There is no request to quit before playing first card")
                    .isInstanceOf(IllegalStateException.class);

            softly.assertThatThrownBy(() -> {
                        sut.playFirstCard(player1, Card.of(Rank.THREE, Suit.CLUBS));
                        sut.quit(player2);
                    })
                    .as("There is no request to quit before playing first card")
                    .isInstanceOf(IllegalStateException.class);

            softly.assertAll();
        }

        @Test
        @DisplayName("Should not allow quiting others bet raise request")
        void shouldNotAllowQuitingOthersBetRaiseRequest() {
            sut.raise(player1);
            assertThatIllegalArgumentException().isThrownBy(() -> sut.quit(player1));
        }

        @Test
        @DisplayName("Should return no winner and match worth 12 points")
        void shouldReturnNoWinnerAndMatchWorth12Points() {
            sut.raise(player1);
            sut.raise(player2);
            sut.raise(player1);
            sut.raise(player2);
            sut.accept(player1);
            SoftAssertions softly = new SoftAssertions();
            softly.assertThat(sut.getPoints()).as("Hand points").isEqualTo(HandPoints.TWELVE);
            softly.assertThat(getPossibleWinner()).as("Hand winner").isNull();
            softly.assertAll();
        }

        @Test
        @DisplayName("Should return winner after call and then run")
        void shouldReturnWinnerAfterCallAndThenRun() {
            sut.raise(player1);
            sut.raise(player2);
            sut.quit(player1);

            SoftAssertions softly = new SoftAssertions();
            softly.assertThat(sut.getPoints()).as("Hand points").isEqualTo(THREE);
            softly.assertThat(getPossibleWinner()).as("Hand winner").isEqualTo(player2);
            softly.assertAll();
        }

        @Test
        @DisplayName("Should not allow to raise bet above twelve points")
        void shouldNotAllowToRaiseBetAboveTwelvePoints() {
            sut.raise(player1);
            sut.raise(player2);
            sut.raise(player1);
            sut.raise(player2);
            final EnumSet<PossibleAction> actions = EnumSet.of(QUIT, ACCEPT);

            SoftAssertions softly = new SoftAssertions();
            softly.assertThat(sut.getPossibleActions()).as("Possible actions").isEqualTo(actions);
            softly.assertThat(sut.getCurrentPlayer()).as("Current player").isEqualTo(player1);
            softly.assertThatThrownBy(() -> sut.raise(player1)).isInstanceOf(IllegalStateException.class);
            softly.assertAll();
        }

        @Test
        @DisplayName("Should allow to raise the bet just enough to enable both players to win")
        void shouldAllowToRaiseTheBetJustEnoughToEnableBothPlayersToWin() {
            sut.raise(player1);
            sut.raise(player2);
            sut.raise(player1);
            sut.accept(player2);
            assertThat(sut.getPoints()).isEqualTo(NINE);
        }

        @Test
        @DisplayName("Should allow to re-raise bet after accepting previous bet")
        void shouldAllowToReRaiseBetAfterAcceptingPreviousBet() {
            sut.raise(player1);
            sut.raise(player2);
            sut.accept(player1);
            sut.raise(player1);
            sut.raise(player2);
            assertThatNoException().isThrownBy(() -> sut.accept(player1));
        }

        @Test
        @DisplayName("Should lose the hand if quits a bet")
        void shouldLoseTheHandIfQuitsABet() {
            sut.raise(player1);
            sut.quit(player2);
            assertThat(getPossibleWinner()).isEqualTo(player1);
        }
/*
        @Test
        @DisplayName("Should have not points proposal after quitting")
        void shouldHaveNotPointsProposalAfterQuitting() {
            sut.raise(player1);
            sut.quit(player2);
            assertThat(sut.getPointsProposal()).isNull();
        }*/

        @Test
        @DisplayName("Should current player be able to quit or accept or raise after a player requested to raise a bet")
        void shouldCurrentPlayerBeAbleToQuitOrAcceptOrRaiseAfterAPlayerRequestedToRaiseABet() {
            EnumSet<PossibleAction> possibleActions = EnumSet.of(QUIT, ACCEPT, RAISE);
            sut.raise(player1);
            assertThat(sut.getPossibleActions()).isEqualTo(possibleActions);
        }

        @Test
        @DisplayName("Should current player only be able to play after opponent accepted the bet raise")
        void shouldCurrentPlayerOnlyBeAbleToPlayAfterOpponentAcceptedTheBetRaise() {
            EnumSet<PossibleAction> possibleActions = EnumSet.of(PLAY);
            sut.raise(player1);
            sut.accept(player2);
            assertThat(sut.getPossibleActions()).isEqualTo(possibleActions);
        }

        @Test
        @DisplayName("Should current player only be able to play after opponent accepted the bet raise with one card already played")
        void shouldCurrentPlayerOnlyBeAbleToPlayAfterOpponentAcceptedTheBetRaiseWithOneCardAlreadyPlayed() {
            EnumSet<PossibleAction> possibleActions = EnumSet.of(PLAY);
            sut.playFirstCard(player1, Card.of(Rank.THREE, Suit.CLUBS));
            sut.raise(player2);
            sut.accept(player1);
            assertThat(sut.getPossibleActions()).isEqualTo(possibleActions);
        }

        @Test
        @DisplayName("Should not be able to raise when hand reaches the max of points")
        void shouldNotBeAbleToRaiseWhenHandReachesMaxPoints() {
            when(player1.getScore()).thenReturn(10);
            when(player2.getScore()).thenReturn(10);
            sut.raise(player1);
            sut.accept(player2);
            sut.playFirstCard(player1, Card.of(Rank.THREE, Suit.CLUBS));
            EnumSet<PossibleAction> possibleActions = EnumSet.of(PLAY);
            assertThat(sut.getPossibleActions()).isEqualTo(possibleActions);
        }

        @Test
        @DisplayName("Should not be able to re-raise the bet when already reached the max of hand points")
        void shouldNotBeAbleToReRaiseTheBetWhenAlreadyReachedTheMaxHandPoints() {
            when(player1.getScore()).thenReturn(5);
            when(player2.getScore()).thenReturn(8);
            sut.raise(player1);
            sut.raise(player2);
            sut.raise(player1);
            EnumSet<PossibleAction> possibleActions = EnumSet.of(ACCEPT, QUIT);
            assertThat(sut.getPossibleActions()).isEqualTo(possibleActions);
        }

        @Test
        @DisplayName("Should current player only be able to play if any user has 11 points and a round card was already played")
        void shouldCurrentPlayerOnlyBeAbleToPlayIfAnyUserHas11PointsAndARoundCardWasAlreadyPlayed() {
            when(player2.getScore()).thenReturn(11);
            sut.playFirstCard(player1, Card.of(Rank.THREE, Suit.CLUBS));
            EnumSet<PossibleAction> possibleActions = EnumSet.of(PLAY);
            assertThat(sut.getPossibleActions()).isEqualTo(possibleActions);
        }
    }

    @Nested
    @DisplayName("When handling mao de onze ")
    class HandleMaoDeOnzeTest {

        @Test
        @DisplayName("Should be mao de onze if at least one player has 11 score points")
        void shouldBeMaoDeOnzeIfAtLeastOnePlayerHas11ScorePoints() {
            when(player2.getScore()).thenReturn(11);
            assertThat(sut.isMaoDeOnze()).isTrue();
        }

        @Test
        @DisplayName("Should be the player with 11 points who decides if plays hand in mao de onze")
        void shouldBeThePlayerWith11PointsWhoDecidesIfPlaysHandInMaoDeOnze() {
            when(player2.getScore()).thenReturn(11);
            sut = new Hand(player1, player2, Card.of(Rank.SEVEN, Suit.CLUBS));
            assertThatIllegalArgumentException().isThrownBy(() -> sut.accept(player1));
        }

        @Test
        @DisplayName("Should mao de onze responder not interfere in playing order")
        void shouldMaoDeOnzeResponderNotInterfereInPlayingOrder() {
            when(player2.getScore()).thenReturn(11);
            sut = new Hand(player1, player2, Card.of(Rank.SEVEN, Suit.CLUBS));
            sut.accept(player2);
            assertThatNoException().isThrownBy(() -> sut.playFirstCard(player1, Card.of(Rank.THREE, Suit.CLUBS)));
        }

        @Test
        @DisplayName("Should not allow to play first card before deciding if plays mao de onze")
        void shouldNotAllowToPlayCardFirstBeforeDecidingIfPlaysMaoDeOnze() {
            when(player1.getScore()).thenReturn(11);
            sut = new Hand(player1, player2, Card.of(Rank.SEVEN, Suit.CLUBS));
            assertThatIllegalStateException().isThrownBy(() -> sut.playFirstCard(player1, Card.closed()));
        }

        @Test
        @DisplayName("Should not allow to play second card before deciding if plays mao de onze")
        void shouldNotAllowToPlaySecondCardBeforeDecidingIfPlaysMaoDeOnze() {
            when(player1.getScore()).thenReturn(11);
            sut = new Hand(player1, player2, Card.of(Rank.SEVEN, Suit.CLUBS));
            assertThatIllegalStateException().isThrownBy(() -> sut.playSecondCard(player1, Card.closed()));
        }

        @Test
        @DisplayName("Should not allow to raise bet while deciding if plays mao de onze")
        void shouldNotAllowToRaiseBetWhileDecidingIfPlaysMaoDeOnze() {
            when(player1.getScore()).thenReturn(11);
            sut = new Hand(player1, player2, Card.of(Rank.SEVEN, Suit.CLUBS));
            assertThatIllegalStateException().isThrownBy(() -> sut.raise(player1));
        }

        @Test
        @DisplayName("Should be able to play card after deciding if plays mao de onze")
        void shouldBeAbleToPlayCardAfterDecidingIfPlaysMaoDeOnze() {
            when(player1.getScore()).thenReturn(11);
            sut = new Hand(player1, player2, Card.of(Rank.SEVEN, Suit.CLUBS));
            sut.accept(player1);
            assertThatNoException().isThrownBy(() -> sut.playFirstCard(player1, Card.of(Rank.THREE, Suit.CLUBS)));
        }

        @Test
        @DisplayName("Should hand worth 3 points if player accepts mao de onze")
        void shouldHandWorth3PointsIfPlayerAcceptsMaoDeOnze() {
            when(player1.getScore()).thenReturn(11);
            sut = new Hand(player1, player2, Card.of(Rank.SEVEN, Suit.CLUBS));
            sut.accept(player1);
            assertThat(sut.getPoints()).isEqualTo(THREE);
        }

        @Test
        @DisplayName("Should opponent win 1 point if player quits mao de onze")
        void shouldOpponentWin1PointIfPlayerQuitsMaoDeOnze() {
            when(player1.getScore()).thenReturn(11);
            sut = new Hand(player1, player2, Card.of(Rank.SEVEN, Suit.CLUBS));
            sut.quit(player1);
            assertThat(sut.getResult().orElse(null)).isEqualTo(HandResult.of(player2, ONE));
        }

        @Test
        @DisplayName("Should hand be done after player quits mao de onze")
        void shouldHandBeDoneAfterPlayerQuitsMaoDeOnze() {
            when(player1.getScore()).thenReturn(11);
            sut = new Hand(player1, player2, Card.of(Rank.SEVEN, Suit.CLUBS));
            sut.quit(player1);
            assertThat(sut.isDone()).isTrue();
        }

        @Test
        @DisplayName("Should not be able to raise the bet when any user has 11 points")
        void shouldNotBeAbleToRaiseBetWhenAnyUserHas11Points() {
            when(player1.getScore()).thenReturn(11);
            when(player2.getScore()).thenReturn(11);
            sut = new Hand(player1, player2, Card.of(Rank.SEVEN, Suit.CLUBS));
            EnumSet<PossibleAction> possibleActions = EnumSet.of(PLAY);
            assertThat(sut.getPossibleActions()).isEqualTo(possibleActions);
        }

        @Test
        @DisplayName("Should not be able to raise the bet when any user has 11 points after a round card was played")
        void shouldNotBeAbleToRaiseBetWhenAnyUserHas11PointsAfterARoundCardWasPlayed() {
            when(player1.getScore()).thenReturn(11);
            when(player2.getScore()).thenReturn(11);
            sut.playFirstCard(player1, Card.of(Rank.THREE, Suit.CLUBS));
            EnumSet<PossibleAction> possibleActions = EnumSet.of(PLAY);
            assertThat(sut.getPossibleActions()).isEqualTo(possibleActions);
        }

        @Test
        @DisplayName("Should current player only be able to play if any user has 11 points")
        void shouldCurrentPlayerOnlyBeAbleToPlayIfAnyUserHas11Points() {
            when(player2.getScore()).thenReturn(11);
            sut.playFirstCard(player1, Card.of(Rank.THREE, Suit.CLUBS));
            sut.playSecondCard(player2, Card.of(Rank.TWO, Suit.CLUBS));
            EnumSet<PossibleAction> possibleActions = EnumSet.of(PLAY);
            assertThat(sut.getPossibleActions()).isEqualTo(possibleActions);
        }

        @Test
        @DisplayName("Should current player be able only to accept or quit when hand begins in mao de onze")
        void shouldCurrentPlayerBeAbleOnlyToAcceptOrQuitWhenHandBeginsInMaoDeOnze() {
            when(player1.getScore()).thenReturn(11);
            sut = new Hand(player1, player2, Card.of(Rank.SEVEN, Suit.CLUBS));
            EnumSet<PossibleAction> possibleActions = EnumSet.of(ACCEPT, QUIT);
            assertThat(sut.getPossibleActions()).isEqualTo(possibleActions);
        }

        @Test
        @DisplayName("Should current player only be able to play after responder accepts mao de onze")
        void shouldCurrentPlayerOnlyBeAbleToPlayAfterResponderAcceptsMaoDeOnze() {
            when(player2.getScore()).thenReturn(11);
            sut = new Hand(player1, player2, Card.of(Rank.SEVEN, Suit.CLUBS));
            sut.accept(player2);
            EnumSet<PossibleAction> possibleActions = EnumSet.of(PLAY);
            assertThat(sut.getPossibleActions()).isEqualTo(possibleActions);
        }
    }

    @Nested
    @DisplayName("When handling requests")
    class HandleRequestTest {

        @Test
        @DisplayName("Should not accept null parameters in any external request")
        void shouldNotAcceptNullParametersInAnyExternalRequest() {
            final Class<NullPointerException> exception = NullPointerException.class;
            SoftAssertions softly = new SoftAssertions();
            softly.assertThatThrownBy(() -> new Hand(null, null, null)).isInstanceOf(exception);
            softly.assertThatThrownBy(() -> sut.playFirstCard(null, Card.closed())).isInstanceOf(exception);
            softly.assertThatThrownBy(() -> sut.playFirstCard(player1, null)).isInstanceOf(exception);
            softly.assertThatThrownBy(() -> sut.playSecondCard(null, Card.closed())).isInstanceOf(exception);
            softly.assertThatThrownBy(() -> sut.playSecondCard(player2, null)).isInstanceOf(exception);
            softly.assertThatThrownBy(() -> sut.raise(null)).isInstanceOf(exception);
            softly.assertThatThrownBy(() -> sut.quit(null)).isInstanceOf(exception);
            softly.assertAll();
        }

        @Test
        @DisplayName("Should current player be able of nothing is the hand is done")
        void shouldCurrentPlayerBeAbleOfNothingIsTheHandIsDone() {
            EnumSet<PossibleAction> possibleActions = EnumSet.noneOf(PossibleAction.class);
            sut.raise(player1);
            sut.quit(player2);
            assertThat(sut.getPossibleActions()).isEqualTo(possibleActions);
        }

        @Test
        @DisplayName("Should have no current player if hand is done")
        void shouldHaveNoCurrentPlayerIfHandIsDone() {
            sut.raise(player1);
            sut.quit(player2);
            assertThat(sut.getCurrentPlayer()).isNull();
        }

        @Test
        @DisplayName("Should throw if any action is requested when the hand is done")
        void shouldThrowIfAnyActionIsRequestedWhenTheHandIsDone() {
            sut.raise(player1);
            sut.quit(player2);
            final Class<IllegalArgumentException> exception = IllegalArgumentException.class;
            final SoftAssertions softly = new SoftAssertions();
            softly.assertThatThrownBy(() -> sut.playFirstCard(player1, Card.closed())).isInstanceOf(exception);
            softly.assertThatThrownBy(() -> sut.playSecondCard(player1, Card.closed())).isInstanceOf(exception);
            softly.assertThatThrownBy(() -> sut.accept(player2)).isInstanceOf(exception);
            softly.assertThatThrownBy(() -> sut.quit(player2)).isInstanceOf(exception);
            softly.assertThatThrownBy(() -> sut.raise(player2)).isInstanceOf(exception);
            softly.assertAll();
        }
    }

    private Player getPossibleWinner() {
        return sut.getResult().flatMap(HandResult::getWinner).orElse(null);
    }
}