package com.fabio.bruno.minepowerbot;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static com.fabio.bruno.minepowerbot.MinePowerBotIntelMockBuilder.create;

class MinePowerBotTest {

    private MinePowerBot sut;
    private GameIntel intel;
    private TrucoCard vira;
    private List<TrucoCard> cards;
    private Optional<TrucoCard> opponentCard;

    @BeforeEach
    void setUp(){
        sut = new MinePowerBot();
    }

    @Test
    @DisplayName("Given the game intel, hasHigher method should return false if don't have higher card than opponent")
    void shouldReturnFalseIfCardsInHandAreLowerThanOpponents(){
        TrucoCard playingCard = TrucoCard.of(CardRank.TWO, CardSuit.SPADES);
        intel = create().cardsToBeAceTwoAndThreeOfSuit(CardSuit.SPADES)
                .opponentCardToBe(TrucoCard.of(CardRank.KING, CardSuit.SPADES))
                .viraToBeDiamondsOfRank(CardRank.KING)
                .finish();

        assertThat(sut.chooseCard(intel)).isEqualTo(CardToPlay.of(playingCard));
    }

    @Test @DisplayName("Check if has manilha")
    void checkIfHasManilha(){
        intel = create()
                .viraToBe(CardRank.FOUR, CardSuit.SPADES)
                .cards(
                TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS),
                TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)).finish();
        TrucoCard vira = intel.getVira();

        assertThat(sut.chooseCard(intel).content().isManilha(vira));
    }

    @Test
    @DisplayName("Should play the lowest rank manilha if it has at least two of them in hand")
    void shouldPlayLowestRankManilhaIfItHasAtLeastTwoOfThemInHandInTheFirstRoundAndItIsTheFirstToPlay() {
        intel = create()
                .viraToBe(CardRank.KING, CardSuit.DIAMONDS)
                .cards(
                TrucoCard.of(CardRank.ACE, CardSuit.HEARTS), // Manilha
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), // Manilha
                TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS)).finish();

        assertThat(sut.chooseCard(intel).content()).isEqualTo(TrucoCard.of(CardRank.ACE, CardSuit.HEARTS));
    }

    @Test
    @DisplayName("Should raise bet if has manilha in hand")
    void raiseIfHasManilha() {
        intel = create()
                .viraToBe(CardRank.KING, CardSuit.DIAMONDS)
                .cards(
                        TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS)).finish();

        assertThat(sut.decideIfRaises(intel)).isTrue();
    }

    @Test
    @DisplayName("Should play the lowest card that is stronger than the opponent card")
    void shouldPlayTheLowestCardThatIsStrongerThanOpponentCard() {
        intel = create().viraToBe(CardRank.ACE, CardSuit.SPADES).cards(
                TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS),
                TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS)
        ).opponentCardToBe(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS)).finish();

        assertThat(sut.chooseCard(intel).content()).isEqualTo(TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS));
    }
}