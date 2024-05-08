package com.garcia.orlandi.slayerbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

public class SlayerBotTest {

    List<GameIntel.RoundResult> roundResults;
    TrucoCard vira;
    List<TrucoCard> openCards;
    List<TrucoCard> cards;
    GameIntel.StepBuilder stepBuilder;
    SlayerBotUtils utils;

    @Test
    @DisplayName("If first to play, should not play zap on first round")
    void shouldNotPlayZapOnFirstRound(){

        roundResults = List.of();
        vira = TrucoCard.of(FOUR, HEARTS);
        cards = List.of(
                TrucoCard.of(FIVE, CLUBS),
                TrucoCard.of(SEVEN, CLUBS),
                TrucoCard.of(FIVE, HEARTS));
        openCards = List.of(vira);

        stepBuilder = GameIntel.StepBuilder
                .with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(cards, 0).opponentScore(0);

        CardToPlay card = new SlayerBot().chooseCard(stepBuilder.build());
        TrucoCard chosenCard = card.value();
        assertFalse(chosenCard.isZap(vira));
    }

    @Test
    @DisplayName("If first to play and with two manilhas, play the weakest one first and the stronger afterwards")
    void shouldPlayWeakerManilhaFirst(){
        roundResults = List.of();
        vira = TrucoCard.of(FOUR, HEARTS);
        cards = List.of(
                TrucoCard.of(FIVE, CLUBS),
                TrucoCard.of(SEVEN, CLUBS),
                TrucoCard.of(FIVE, HEARTS));
        openCards = List.of(vira);

        stepBuilder = GameIntel.StepBuilder
                .with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(cards, 0).opponentScore(0);

        CardToPlay card = new SlayerBot().chooseCard(stepBuilder.build());
        TrucoCard chosenCard = card.value();
        assertFalse(chosenCard.isZap(vira));
        assertTrue(chosenCard.isCopas(vira));
    }

    @Test
    @DisplayName("If first to play and only have Zap, play second strongest card first to try to win the round")
    void shouldPlaySecondStrongestCardIfOnlyHasZapInTheFirstRound(){
        roundResults = List.of();
        vira = TrucoCard.of(FOUR, HEARTS);
        cards = List.of(
                TrucoCard.of(FIVE, CLUBS),
                TrucoCard.of(SEVEN, CLUBS),
                TrucoCard.of(THREE, SPADES));
        openCards = List.of(vira);

        stepBuilder = GameIntel.StepBuilder
                .with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(cards, 0).opponentScore(0);

        CardToPlay card = new SlayerBot().chooseCard(stepBuilder.build());
        TrucoCard chosenCard = card.value();
        assertThat(chosenCard).isEqualTo(TrucoCard.of(THREE, SPADES));
    }

    @Test
    @DisplayName("If first to play and with two manilhas, but no zap, play weakest manilha first")
    void shouldPlayWeakestManilhaInFirstRoundIfHaveTwoButNoZap(){
        roundResults = List.of();
        vira = TrucoCard.of(THREE, HEARTS);
        cards = List.of(
                TrucoCard.of(THREE, SPADES),
                TrucoCard.of(FOUR, HEARTS),
                TrucoCard.of(FOUR, DIAMONDS));
        openCards = List.of(vira);

        stepBuilder = GameIntel.StepBuilder
                .with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(cards, 0).opponentScore(0);

        CardToPlay card = new SlayerBot().chooseCard(stepBuilder.build());
        TrucoCard chosenCard = card.value();
        assertThat(chosenCard).isEqualTo(TrucoCard.of(FOUR, DIAMONDS));
    }

    @Test
    @DisplayName("If first to play and has only one manilha, play best card except manilha")
    void shouldPlayStrongerCardExceptManilhaIfHasOnlyOneManilhaInFirstRound(){
        roundResults = List.of();
        vira = TrucoCard.of(SIX, HEARTS);
        cards = List.of(
                TrucoCard.of(SEVEN, HEARTS),
                TrucoCard.of(FIVE, HEARTS),
                TrucoCard.of(THREE, DIAMONDS));
        openCards = List.of(vira);

        stepBuilder = GameIntel.StepBuilder
                .with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(cards, 0).opponentScore(0);

        CardToPlay card = new SlayerBot().chooseCard(stepBuilder.build());
        TrucoCard chosenCard = card.value();
        assertThat(chosenCard).isEqualTo(TrucoCard.of(THREE, DIAMONDS));
    }

    @Test
    @DisplayName("If first to play and with three manilhas at hand, play the second strongest and save best card for last")
    void shouldPlaySecondStrongestIfFirstToPlayAndWithThreeManilhasExceptZap(){
        roundResults = List.of();
        vira = TrucoCard.of(SIX, HEARTS);
        cards = List.of(
                TrucoCard.of(SEVEN, DIAMONDS),
                TrucoCard.of(SEVEN, HEARTS),
                TrucoCard.of(SEVEN, SPADES));
        openCards = List.of(vira);

        stepBuilder = GameIntel.StepBuilder
                .with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(cards, 0).opponentScore(0);

        CardToPlay card = new SlayerBot().chooseCard(stepBuilder.build());
        TrucoCard chosenCard = card.value();
        assertThat(chosenCard).isEqualTo(TrucoCard.of(SEVEN, SPADES));
    }

    @Test
    @DisplayName("If first to play and with no manilha but has two 3s at hand, play one of them")
    void shouldPlayThreeIfFirstToPlayWithNoManilhaButHasTwoThrees(){
        roundResults = List.of();
        vira = TrucoCard.of(FIVE, HEARTS);
        cards = List.of(
                TrucoCard.of(THREE, DIAMONDS),
                TrucoCard.of(THREE, HEARTS),
                TrucoCard.of(SEVEN, SPADES));
        openCards = List.of(vira);

        stepBuilder = GameIntel.StepBuilder
                .with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(cards, 0).opponentScore(0);

        CardToPlay card = new SlayerBot().chooseCard(stepBuilder.build());
        TrucoCard chosenCard = card.value();
        assertThat(chosenCard).isNotEqualTo(TrucoCard.of(SEVEN, SPADES));
    }

    @Test
    @DisplayName("If first to play with no manilha and only one three, play strongest card first except the three")
    void shouldPlaySecondStrongestCardIfHasOnlyOneThreeAndNoManilha(){
        roundResults = List.of();
        vira = TrucoCard.of(FIVE, HEARTS);
        cards = List.of(
                TrucoCard.of(THREE, DIAMONDS),
                TrucoCard.of(TWO, HEARTS),
                TrucoCard.of(SEVEN, SPADES));
        openCards = List.of(vira);

        stepBuilder = GameIntel.StepBuilder
                .with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(cards, 0).opponentScore(0);

        CardToPlay card = new SlayerBot().chooseCard(stepBuilder.build());
        TrucoCard chosenCard = card.value();
        assertThat(chosenCard).isEqualTo(TrucoCard.of(TWO, HEARTS));
    }

    @Test
    @DisplayName("If first to play and with no manilhas and no threes, play second strongest card")
    void shouldPlaySecondStrongestCardIfHasNoManilhasAndNoThreesAtHand(){
        roundResults = List.of();
        vira = TrucoCard.of(FIVE, HEARTS);
        cards = List.of(
                TrucoCard.of(KING, DIAMONDS),
                TrucoCard.of(TWO, HEARTS),
                TrucoCard.of(SEVEN, SPADES));
        openCards = List.of(vira);

        stepBuilder = GameIntel.StepBuilder
                .with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(cards, 0).opponentScore(0);

        CardToPlay card = new SlayerBot().chooseCard(stepBuilder.build());
        TrucoCard chosenCard = card.value();
        assertThat(chosenCard).isEqualTo(TrucoCard.of(KING, DIAMONDS));
    }

    @Test
    @DisplayName("If won the first round, should play strongest card to win the game")
    void shouldPlayStrongestCardIfWonFirstRound(){
        roundResults = List.of(GameIntel.RoundResult.WON);
        vira = TrucoCard.of(FIVE, HEARTS);
        cards = List.of(
                TrucoCard.of(KING, DIAMONDS),
                TrucoCard.of(TWO, HEARTS));
        openCards = List.of(vira);

        stepBuilder = GameIntel.StepBuilder
                .with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(cards, 1).opponentScore(0);

        CardToPlay card = new SlayerBot().chooseCard(stepBuilder.build());
        TrucoCard chosenCard = card.value();
        assertThat(chosenCard).isEqualTo(TrucoCard.of(TWO, HEARTS));
    }

    @Test
    @DisplayName("Should not play maoDeOnze if has no manilhas at hand")
    void shouldNotPlayMaoDeOnzeIfHasNoManilhas(){
        roundResults = List.of();
        vira = TrucoCard.of(FIVE, HEARTS);
        cards = List.of(
                TrucoCard.of(KING, DIAMONDS),
                TrucoCard.of(TWO, HEARTS),
                TrucoCard.of(SEVEN, SPADES));
        openCards = List.of(vira);

        stepBuilder = GameIntel.StepBuilder
                .with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(cards, 0).opponentScore(0);

        Boolean maoDeOnzeResponse = new SlayerBot().getMaoDeOnzeResponse(stepBuilder.build());
        assertThat(maoDeOnzeResponse).isFalse();
    }

    @Test
    @DisplayName("Should accept maoDeOnze if has two manilhas at hand")
    void shouldAcceptMaoDeOnzeWithTwoManilhas(){
        roundResults = List.of();
        vira = TrucoCard.of(FIVE, HEARTS);
        cards = List.of(
                TrucoCard.of(SIX, DIAMONDS),
                TrucoCard.of(SIX, HEARTS),
                TrucoCard.of(SEVEN, SPADES));
        openCards = List.of(vira);

        stepBuilder = GameIntel.StepBuilder
                .with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(cards, 0).opponentScore(0);

        Boolean maoDeOnzeResponse = new SlayerBot().getMaoDeOnzeResponse(stepBuilder.build());
        assertThat(maoDeOnzeResponse).isTrue();
    }

    @Test
    @DisplayName("Should accept maoDeOnze if has manilha and three at hand")
    void shouldPlayMaoDeOnzeIfHasManilhaAndThree(){
        roundResults = List.of();
        vira = TrucoCard.of(FIVE, HEARTS);
        cards = List.of(
                TrucoCard.of(SIX, DIAMONDS),
                TrucoCard.of(THREE, HEARTS),
                TrucoCard.of(SEVEN, SPADES));
        openCards = List.of(vira);

        stepBuilder = GameIntel.StepBuilder
                .with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(cards, 0).opponentScore(0);

        Boolean maoDeOnzeResponse = new SlayerBot().getMaoDeOnzeResponse(stepBuilder.build());
        assertThat(maoDeOnzeResponse).isTrue();
    }

}
