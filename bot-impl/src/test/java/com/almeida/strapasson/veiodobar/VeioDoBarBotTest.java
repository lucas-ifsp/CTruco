package com.almeida.strapasson.veiodobar;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.almeida.strapasson.veiodobar.GameIntelMockBuilder.make;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.fail;

class VeioDoBarBotTest {
    private VeioDoBarBot sut;

    @BeforeEach
    void setUp() {
        sut = new VeioDoBarBot();
    }

    @Test
    @DisplayName("Should play the smallest card necessary to win the round")
    void shouldPlayTheSmallestCardNecessaryToWinTheRound() {
        TrucoCard playingCard = TrucoCard.of(CardRank.TWO, CardSuit.SPADES);
        var intel = make().cardsToBeAceTwoAndThreeOfSuit(CardSuit.SPADES)
                        .opponentCardToBe(TrucoCard.of(CardRank.KING, CardSuit.SPADES))
                        .viraToBeDiamondsOfRank(CardRank.KING)
                        .finish();

        assertThat(sut.chooseCard(intel)).isEqualTo(CardToPlay.of(playingCard));
    }

    @Test
    @DisplayName("Should play the smallest card if not able to win the first round")
    void shouldPlayTheSmallestCardIfNotAbleToWinTheFirstRound() {
        var playingCard = TrucoCard.of(CardRank.TWO, CardSuit.SPADES);
        var intel = make().cardsToBeAceTwoAndThreeOfSuit(CardSuit.SPADES)
                        .opponentCardToBe(TrucoCard.of(CardRank.ACE, CardSuit.HEARTS))
                        .viraToBeDiamondsOfRank(CardRank.KING)
                        .finish();

        assertThat(sut.chooseCard(intel)).isEqualTo(CardToPlay.of(playingCard));
    }
    
    @Test
    @DisplayName("Should play the second strongest card if bot is the very first to play and no casal maior")
    void shouldPlayTheSecondStrongestCardIfBotIsTheVeryFirstToPlayAndNoCasalMaior() {
        var playingCard = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
        var intel = make().cardsToBeAceTwoAndThreeOfSuit(CardSuit.SPADES)
                        .viraToBeDiamondsOfRank(CardRank.KING)
                        .botToBeFirstToPlay()
                        .finish();

        assertThat(sut.chooseCard(intel)).isEqualTo(CardToPlay.of(playingCard));
    }

    @Test
    @DisplayName("Should play the smallest card at the first round when it has casal maior")
    void shouldPlayTheSmallestCardAtTheFirstRoundWhenItHasCasalMaior() {
        var playingCard = TrucoCard.of(CardRank.TWO, CardSuit.SPADES);
        var intel = make().cardsToBe(playingCard, TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.ACE, CardSuit.CLUBS))
                        .viraToBeDiamondsOfRank(CardRank.KING)
                        .botToBeFirstToPlay()
                        .finish();

        assertThat(sut.chooseCard(intel)).isEqualTo(CardToPlay.of(playingCard));
    }

    @Test
    @DisplayName("Should play the smallest card at second round if won the first one")
    void shouldPlayTheSmallestCardAtSecondRoundIfWonTheFirstOne() {
        var playingCard = TrucoCard.of(CardRank.TWO, CardSuit.SPADES);
        var intel = make().cardsToBe(playingCard, TrucoCard.of(CardRank.ACE, CardSuit.SPADES))
                        .viraToBeDiamondsOfRank(CardRank.KING)
                        .botToBeFirstToPlay()
                        .botToWinTheFirstRound()
                        .finish();

        assertThat(sut.chooseCard(intel)).isEqualTo(CardToPlay.of(playingCard));
    }

    @Test
    @DisplayName("Should play the smallest card necessary to win at second round if lost the first one")
    void shouldPlayTheSmallestCardNecessaryToWinAtSecondRoundIfLostTheFirstOne() {
        var playingCard = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
        var intel = make().cardsToBe(playingCard, TrucoCard.of(CardRank.TWO, CardSuit.SPADES))
                        .viraToBeDiamondsOfRank(CardRank.KING)
                        .botToLoseTheFirstRound()
                        .opponentCardToBe(TrucoCard.of(CardRank.THREE, CardSuit.SPADES))
                        .finish();

        assertThat(sut.chooseCard(intel)).isEqualTo(CardToPlay.of(playingCard));
    }

    @Test
    @DisplayName("Should discard the smallest card at second round if lost the first one and unable to win")
    void shouldDiscardTheSmallestCardAtSecondRoundIfLostTheFirstOneAndUnableToWin() {
        var playingCard = TrucoCard.of(CardRank.TWO, CardSuit.SPADES);
        var intel = make().cardsToBe(playingCard, TrucoCard.of(CardRank.ACE, CardSuit.SPADES))
                        .viraToBeDiamondsOfRank(CardRank.KING)
                        .botToLoseTheFirstRound()
                        .opponentCardToBe(TrucoCard.of(CardRank.ACE, CardSuit.HEARTS))
                        .finish();

        assertThat(sut.chooseCard(intel)).isEqualTo(CardToPlay.discard(playingCard));
    }

    @Test
    @DisplayName("Should play the last card at the third round")
    void shouldPlayTheLastCardAtTheThirdRound() {
        var playingCard = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
        var intel = make().cardsToBe(playingCard)
                        .viraToBeDiamondsOfRank(CardRank.KING)
                        .roundResultToBe(GameIntel.RoundResult.LOST, GameIntel.RoundResult.WON)
                        .opponentCardToBe(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS))
                        .finish();

        assertThat(sut.chooseCard(intel)).isEqualTo(CardToPlay.of(playingCard));
    }

    @Test
    @DisplayName("Should throw a descriptive NullPointerException when intel is null")
    void shouldThrowADescriptiveNullPointerExceptionWhenIntelIsNull() {
        assertThatThrownBy(() -> sut.chooseCard(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Game intel must be given for the bot choose how to act!");
    }

    @Test
    @DisplayName("Should play mao de onze if has at least two cards greater or equal to two")
    void shouldPlayMaoDeOnzeIfHasAtLeastTwoCardsGreaterOrEqualToTwo() {
        var intel = make().cardsToBeThreeOf(CardRank.TWO)
                        .viraToBeDiamondsOfRank(CardRank.TWO)
                        .finish();

        assertThat(sut.getMaoDeOnzeResponse(intel)).isTrue();
    }

    @Test
    @DisplayName("Should accept raise points if has one manilha and one card equal or greater than jack")
    void shouldAcceptRaisePontsIfHasOneManilhaAndOneCardEqualOrGreaterThanJack() {
        var intel = make().cardsToBe(TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                            TrucoCard.of(CardRank.JACK, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS))
                        .viraToBeDiamondsOfRank(CardRank.FOUR)
                        .finish();

        assertThat(sut.getRaiseResponse(intel)).isEqualTo(0);
    }

    @Test
    @DisplayName("Should accept raise points if has two cards equalts to or greater than two")
    void shouldAcceptRaisePointsIfHasTwoCardsEqualtsToOrGreaterThanTwo() {
        var intel = make().cardsToBe(
                            TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS))
                        .viraToBeDiamondsOfRank(CardRank.TWO)
                        .finish();

        assertThat(sut.getRaiseResponse(intel)).isEqualTo(0);
    }

    @Test
    @DisplayName("Should refuse points raising if all cards are lower than jacks and no manilhas")
    void shouldRefusePointsRaisingIfAllCardsAreLowerThanJacksAndNoManilhas(){
        var intel = make().cardsToBeThreeOf(CardRank.FOUR)
                        .viraToBeDiamondsOfRank(CardRank.FOUR)
                        .finish();

        assertThat(sut.getRaiseResponse(intel)).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should refuse if has two cards lower than Jack")
    void shouldRefuseIfHasTwoCardsLowerThanJack() {
        var intel = make().cardsToBe(TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                            TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                            TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS))
                        .viraToBeDiamondsOfRank(CardRank.FOUR)
                        .finish();

        assertThat(sut.getRaiseResponse(intel)).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should refuse if has two cards lower than two")
    void shouldRefuseIfHasTwoCardsLessThanTwo() {
        var intel = make().cardsToBeThreeOf(CardRank.ACE)
                        .viraToBeDiamondsOfRank(CardRank.FOUR)
                        .finish();

        assertThat(sut.getRaiseResponse(intel)).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should raise points if has the greater couple")
    void shouldRaisePointsIfHasTheGreaterCouple() {
        var intel = make().cardsToBe(
                            TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.ACE, CardSuit.SPADES))
                        .viraToBeDiamondsOfRank(CardRank.ACE)
                        .finish();

        assertThat(sut.decideIfRaises(intel)).isTrue();
    }

    @Test
    @DisplayName("Should raise points if has three cards three")
    void shouldRaisePointsIfHasThreeCardsThree() {
        var intel = make().cardsToBeThreeOf(CardRank.THREE)
                        .viraToBeDiamondsOfRank(CardRank.ACE)
                        .finish();

        assertThat(sut.decideIfRaises(intel)).isTrue();

    }

    @Test
    @DisplayName("Should raise points if has one manilha and one card equal or greater than two")
    void shouldRaisePointsIfHasOneManilhaAndOneCardEqualOrGreaterThanTwo() {
        var intel = make().cardsToBe(
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS))
                .viraToBeDiamondsOfRank(CardRank.FOUR)
                .finish();

        assertThat(sut.decideIfRaises(intel)).isTrue();
    }

    @Test
    @DisplayName("Should raise point if score greater 6 points than other bot")
    void shouldRaisePointIfScoreGreater6PointsThanOtherBot() {
        var intel = make().scoreToBe(6).scoreOponentToBe(0)
                .cardsToBe(
                        TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS))
                .viraToBeDiamondsOfRank(CardRank.FOUR)
                .finish();

        assertThat(sut.decideIfRaises(intel)).isTrue();
    }

}