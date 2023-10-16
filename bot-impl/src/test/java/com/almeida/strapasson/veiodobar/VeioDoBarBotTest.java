package com.almeida.strapasson.veiodobar;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VeioDoBarBotTest {
    private GameIntel intel;
    private VeioDoBarBot sut;

    @BeforeEach
    void setUp() {
        sut = new VeioDoBarBot();
        intel = mock(GameIntel.class);
    }

    @Test
    @DisplayName("Should play the smallest card necessary to win the round")
    void shouldPlayTheSmallestCardNecessaryToWinTheRound() {
        TrucoCard playingCard = TrucoCard.of(CardRank.TWO, CardSuit.SPADES);

        when(intel.getCards()).thenReturn(List.of(
                playingCard,
                TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                TrucoCard.of(CardRank.THREE, CardSuit.SPADES)
        ));
        when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.KING, CardSuit.SPADES)));
        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS));

        assertThat(sut.chooseCard(intel)).isEqualTo(CardToPlay.of(playingCard));
    }

    @Test
    @DisplayName("Should discard the smallest card if not able to win the round")
    void shouldDiscardTheSmallestCardIfNotAbleToWinTheRound() {
        var playingCard = TrucoCard.of(CardRank.TWO, CardSuit.SPADES);

        when(intel.getCards()).thenReturn(List.of(
          playingCard,
          TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
          TrucoCard.of(CardRank.THREE, CardSuit.SPADES)
        ));
        when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.ACE, CardSuit.HEARTS)));
        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS));

        assertThat(sut.chooseCard(intel)).isEqualTo(CardToPlay.discard(playingCard));
    }
    
    @Test
    @DisplayName("Should play the second strongest card if bot is the very first to play and no casal maior")
    void shouldPlayTheSecondStrongestCardIfBotIsTheVeryFirstToPlayAndNoCasalMaior() {
        var playingCard = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);

        when(intel.getCards()).thenReturn(List.of(
                playingCard,
                TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                TrucoCard.of(CardRank.TWO, CardSuit.SPADES)
        ));
        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.KING, CardSuit.SPADES));
        when(intel.getOpponentCard()).thenReturn(Optional.empty());

        assertThat(sut.chooseCard(intel)).isEqualTo(CardToPlay.of(playingCard));
    }

    @Test
    @DisplayName("Should play the smallest card at the first round when it has casal maior")
    void shouldPlayTheSmallestCardAtTheFirstRoundWhenItHasCasalMaior() {
        var playingCard = TrucoCard.of(CardRank.TWO, CardSuit.SPADES);

        when(intel.getCards()).thenReturn(List.of(
           playingCard,
           TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
           TrucoCard.of(CardRank.ACE, CardSuit.CLUBS)
        ));
        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.KING, CardSuit.SPADES));
        when(intel.getOpponentCard()).thenReturn(Optional.empty());

        assertThat(sut.chooseCard(intel)).isEqualTo(CardToPlay.of(playingCard));
    }
    
    @Test
    @DisplayName("Should play the smallest card at second round if won the first one")
    void shouldPlayTheSmallestCardAtSecondRoundIfWonTheFirstOne() {
        var playingCard = TrucoCard.of(CardRank.TWO, CardSuit.SPADES);

        when(intel.getCards()).thenReturn(List.of(
            playingCard,
            TrucoCard.of(CardRank.ACE, CardSuit.SPADES)
        ));
        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.KING, CardSuit.SPADES));
        when(intel.getOpponentCard()).thenReturn(Optional.empty());
        when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON));

        assertThat(sut.chooseCard(intel)).isEqualTo(CardToPlay.of(playingCard));
    }

    @Test
    @DisplayName("Should play the smallest card necessary to win at second round if lost the first one")
    void shouldPlayTheSmallestCardNecessaryToWinAtSecondRoundIfLostTheFirstOne() {
        var playingCard = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);

        when(intel.getCards()).thenReturn(List.of(
                playingCard,
                TrucoCard.of(CardRank.TWO, CardSuit.SPADES)
        ));
        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.KING, CardSuit.SPADES));
        when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.LOST));
        when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.THREE, CardSuit.SPADES)));

        assertThat(sut.chooseCard(intel)).isEqualTo(CardToPlay.of(playingCard));
    }

    @Test
    @DisplayName("Should play the last card at the third round")
    void shouldPlayTheLastCardAtTheThirdRound() {
        var playingCard = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);

        when(intel.getCards()).thenReturn(List.of(playingCard));
        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.KING, CardSuit.SPADES));
        when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.LOST, GameIntel.RoundResult.WON));
        when(intel.getOpponentCard()).thenReturn(Optional.empty());

        assertThat(sut.chooseCard(intel)).isEqualTo(CardToPlay.of(playingCard));
    }

    @Test
    @DisplayName("Should refuse points raising if all cards are lower than jacks and no manilhas")
    void shouldRefusePointsRaisingIfAllCardsAreLowerThanJacksAndNoManilhas(){
        when(intel.getCards()).thenReturn(List.of(
                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS)
        ));
        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));

        assertThat(sut.getRaiseResponse(intel)).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should accept points raising if all cards are upper than jacks and no manilhas")
    void shouldAcceptPointsRaisingIfAllCardsAreUpperThanJacksAndNoManilhas() {
        when(intel.getCards()).thenReturn(List.of(
                TrucoCard.of(CardRank.KING, CardSuit.SPADES),
                TrucoCard.of(CardRank.KING, CardSuit.HEARTS),
                TrucoCard.of(CardRank.KING, CardSuit.CLUBS)
        ));
        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.TWO, CardSuit.SPADES));

        assertThat(sut.getRaiseResponse(intel)).isEqualTo(0);
    }

    @Test
    @DisplayName("should accept raise points if has two manilhas")
    void shouldRaisePointsIfHasTwoManilhas() {
        when(intel.getCards()).thenReturn(List.of(
                TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
        ));
        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS));

        assertThat(sut.getRaiseResponse(intel)).isEqualTo(0);
    }


    @Test
    @DisplayName("Should refuse raise points if has just one manilha")
    void shouldRefuseRaisePointsIfHasJustOneManilha() {
        when(intel.getCards()).thenReturn(List.of(
                TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
        ));
        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));

        assertThat(sut.getRaiseResponse(intel)).isEqualTo(-1);
    }
}