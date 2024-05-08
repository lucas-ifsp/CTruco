package com.garcia.orlandi.slayerbot;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.List;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;



public class SlayerBotTest {

    List<GameIntel.RoundResult> roundResults;
    TrucoCard vira;
    List<TrucoCard> openCards;
    List<TrucoCard> cards;
    GameIntel.StepBuilder stepBuilder;
    SlayerBotUtils utils;

    @Test
    @DisplayName("Should request point raise when holding zap and a winning card")
    void shouldRequestPointRaiseWhenHoldingZapAndWinningCard() {
        TrucoCard vira = TrucoCard.of(FOUR, CardSuit.SPADES);
        TrucoCard opponentCard = TrucoCard.of(JACK, CardSuit.HEARTS);
        TrucoCard zap = TrucoCard.of(CardRank.FIVE, CLUBS);
        TrucoCard winningCard = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);
        List<TrucoCard> openCards = List.of(vira, opponentCard);
        List<TrucoCard> cards = Arrays.asList(zap, winningCard);

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), openCards, vira, 1)
                .botInfo(cards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        GameIntel game = stepBuilder.build();
        SlayerBot bot = new SlayerBot();

        boolean shouldRaise = bot.decideIfRaises(game);

        assertTrue(shouldRaise, "SlayerBot should request a point raise when holding zap and a winning card");
    }

    @Test
    @DisplayName("Should not play a hidden card if second to play in the first round")
    void shouldNotPlayHiddenCardIfSecondToPlayInFirstRound() {
        List<GameIntel.RoundResult> roundResults = Collections.emptyList();

        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

        List<TrucoCard> cards = Arrays.asList(
                TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                TrucoCard.of(CardRank.ACE, CardSuit.HEARTS)
        );

        TrucoCard opponentCard = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, Collections.singletonList(vira), vira, 1)
                .botInfo(cards, 1)
                .opponentScore(0)
                .opponentCard(opponentCard);

        GameIntel game = stepBuilder.build();

        SlayerBot bot = new SlayerBot();

        CardToPlay cardToPlay = bot.chooseCard(game);
        TrucoCard chosenCard = cardToPlay.value();

        assertNotEquals(TrucoCard.closed(), chosenCard, "Chosen card should not be a hidden card if second to play in the first round");

    }
    @Test
    @DisplayName("Should play the weakest card when second to play and dont win against opponent card")
    void shouldPlayWeakestCardWhenSecondToPlayAndDontWinOpponentCard() {
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

        List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES),
                TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)
        );

        TrucoCard opponentManilha = TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS);
        List<TrucoCard> openCards = List.of(vira, opponentManilha);

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(Collections.emptyList(), openCards, vira, 1)
                .botInfo(cards, 0)
                .opponentScore(0)
                .opponentCard(opponentManilha);

        SlayerBot bot = new SlayerBot();
        CardToPlay card = bot.chooseCard(stepBuilder.build());
        TrucoCard expectedCard = TrucoCard.of(CardRank.SEVEN, SPADES);
        assertThat(card.value()).isEqualTo(expectedCard);
    }

    @Test
    @DisplayName("Should play the matching card to tie, then play the strongest card next round")
    void shouldPlayMatchingThenStrongestCard() {
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

        List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.SIX, CardSuit.SPADES)
        );

        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);

        List<TrucoCard> openCards = List.of(vira, opponentCard);
        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), openCards, vira, 1)
                .botInfo(cards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        SlayerBot bot = new SlayerBot();

        CardToPlay firstPlay = bot.chooseCard(stepBuilder.build());
        TrucoCard expectedTieCard = TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS);
        assertThat(firstPlay.value()).isEqualTo(expectedTieCard);

        List<TrucoCard> remainingCards = List.of(
                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                TrucoCard.of(CardRank.SIX, CardSuit.SPADES)
        );
        List<TrucoCard> updatedOpenCards = List.of(vira, opponentCard, expectedTieCard);
        GameIntel updatedGame = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.DREW), updatedOpenCards, vira, 1)
                .botInfo(remainingCards, 0)
                .opponentScore(0)
                .build();

        CardToPlay secondPlay = bot.chooseCard(updatedGame);
        TrucoCard expectedManilha = TrucoCard.of(CardRank.SIX, CardSuit.SPADES);
        assertThat(secondPlay.value()).isEqualTo(expectedManilha);
    }

    @Test
    @DisplayName("Should request truco if tied in first round and holding a manilha")
    void shouldRequestTrucoAfterTieIfHoldingManilha() {
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

        List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES),
                TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS)
        );

        TrucoCard opponentCard = TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS);

        List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.DREW);

        List<TrucoCard> openCards = List.of(vira, opponentCard);
        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(cards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        SlayerBot bot = new SlayerBot();

        boolean shouldRequestTruco = bot.decideIfRaises(stepBuilder.build());
        assertThat(shouldRequestTruco).isTrue();
    }

    @Test
    @DisplayName("Should refuse truco if only holding weak cards as the second player in the first round")
    void shouldRefuseTrucoIfOnlyHoldingWeakCards() {
        TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);

        List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS)
        );

        TrucoCard opponentCard = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
        List<TrucoCard> openCards = List.of(vira, opponentCard);
        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), openCards, vira, 1)
                .botInfo(cards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        SlayerBot bot = new SlayerBot();

        int raiseResponse = bot.getRaiseResponse(stepBuilder.build());

        assertThat(raiseResponse).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should not request truco if either bot or opponent has 11 points")
    void shouldNotRequestTrucoIfInMaoDeOnze() {
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

        List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS)
        );

        TrucoCard opponentCard = TrucoCard.of(CardRank.SIX, CardSuit.HEARTS);
        List<TrucoCard> openCards = List.of(vira, opponentCard);

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), openCards, vira, 1)
                .botInfo(cards, 11)
                .opponentScore(11)
                .opponentCard(opponentCard);

        SlayerBot bot = new SlayerBot();

        boolean shouldRequestTruco = bot.decideIfRaises(stepBuilder.build());
        assertThat(shouldRequestTruco).isFalse();
    }

    @Test
    @DisplayName("Should play the zap to win against the opponent's manilha of hearts")
    void shouldPlayZapToWinAgainstOpponentManilha() {
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

        List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES)
        );

        TrucoCard opponentManilha = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
        List<TrucoCard> openCards = List.of(vira, opponentManilha);

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), openCards, vira, 1)
                .botInfo(cards, 0)
                .opponentScore(0)
                .opponentCard(opponentManilha);

        SlayerBot bot = new SlayerBot();

        CardToPlay cardToPlay = bot.chooseCard(stepBuilder.build());
        TrucoCard expectedZap = TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS);
        assertThat(cardToPlay.value()).isEqualTo(expectedZap);
    }

    @Test
    @DisplayName("Should request truco if second to play and holding 3 manilhas")
    void shouldRequestTrucoIfSecondToPlayWithThreeManilhas() {
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

        List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS)
        );

        TrucoCard opponentCard = TrucoCard.of(CardRank.SIX, CardSuit.CLUBS);
        List<TrucoCard> openCards = List.of(vira, opponentCard);

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), openCards, vira, 1)
                .botInfo(cards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        SlayerBot bot = new SlayerBot();

        boolean shouldRequestTruco = bot.decideIfRaises(stepBuilder.build());
        assertThat(shouldRequestTruco).isTrue();
    }

    @Test
    @DisplayName("Should request truco in the third round if second to play and last card wins against opponent")
    void shouldRequestTrucoInThirdRoundIfSecondToPlayWithWinningCard() {
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

        TrucoCard winningCard = TrucoCard.of(CardRank.THREE, CardSuit.CLUBS);

        List<GameIntel.RoundResult> roundResults = List.of(
                GameIntel.RoundResult.WON,
                GameIntel.RoundResult.LOST
        );

        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);
        List<TrucoCard> openCards = List.of(vira, opponentCard);

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(List.of(winningCard), 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        SlayerBot bot = new SlayerBot();

        boolean shouldRequestTruco = bot.decideIfRaises(stepBuilder.build());
        assertThat(shouldRequestTruco).isTrue();
    }

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
    @DisplayName("If won one round and lost the other, should play the strongest card")
    void shouldPlayStrongestIfWonOneAndLostOne(){
        roundResults = List.of(GameIntel.RoundResult.WON, GameIntel.RoundResult.LOST);
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
    @DisplayName("If a drew happens, the bot should play the strongest card")
    void shouldPlayStrongestCardInCaseOfDrew(){
        roundResults = List.of(GameIntel.RoundResult.DREW);
        vira = TrucoCard.of(FIVE, HEARTS);
        cards = List.of(
                TrucoCard.of(KING, DIAMONDS),
                TrucoCard.of(SIX, HEARTS));
        openCards = List.of(vira);

        stepBuilder = GameIntel.StepBuilder
                .with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(cards, 1).opponentScore(0);

        CardToPlay card = new SlayerBot().chooseCard(stepBuilder.build());
        TrucoCard chosenCard = card.value();
        assertThat(chosenCard).isEqualTo(TrucoCard.of(SIX, HEARTS));
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

    @Test
    @DisplayName("Should request truco in the second round if second to play and holding a Three or any manilha")
    void shouldRequestTrucoInSecondRoundWithThreeOrManilha() {
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

        List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS)
        );

        List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.WON);

        TrucoCard opponentCard = TrucoCard.of(CardRank.SIX, CardSuit.CLUBS);
        List<TrucoCard> openCards = List.of(vira, opponentCard);

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(cards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        SlayerBot bot = new SlayerBot();

        boolean shouldRequestTruco = bot.decideIfRaises(stepBuilder.build());
        assertThat(shouldRequestTruco).isTrue();
    }

    @Test
    @DisplayName("Should request truco if second to play and holding 3 Three cards")
    void shouldRequestTrucoIfSecondToPlayWithThreeThrees() {
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

        List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS)
        );

        TrucoCard opponentCard = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);
        List<TrucoCard> openCards = List.of(vira, opponentCard);

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(Collections.emptyList(), openCards, vira, 1)
                .botInfo(cards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        SlayerBot bot = new SlayerBot();

        boolean shouldRequestTruco = bot.decideIfRaises(stepBuilder.build());
        assertThat(shouldRequestTruco).isTrue();
    }

    @Test
    @DisplayName("Should accept truco if the opponent requests truco after bot played a strong card and won the previous round")
    void shouldAcceptTrucoAfterPlayingStrongCardAndWinningPreviousRound() {
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

        List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)
        );

        List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.WON);

        TrucoCard opponentCard = TrucoCard.of(CardRank.SIX, CardSuit.CLUBS);
        List<TrucoCard> openCards = List.of(vira, opponentCard);

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(cards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        SlayerBot bot = new SlayerBot();

        int raiseResponse = bot.getRaiseResponse(stepBuilder.build());
        assertThat(raiseResponse).isEqualTo(0);
    }

    @Test
    @DisplayName("Should request truco if second to play in the third round and holding any manilha")
    void shouldRequestTrucoInThirdRoundWithAnyManilha() {
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

        List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS)
        );

        List<GameIntel.RoundResult> roundResults = List.of(
                GameIntel.RoundResult.WON,
                GameIntel.RoundResult.LOST
        );

        TrucoCard opponentCard = TrucoCard.of(CardRank.SIX, CardSuit.CLUBS);
        List<TrucoCard> openCards = List.of(vira, opponentCard);

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(cards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        SlayerBot bot = new SlayerBot();

        boolean shouldRequestTruco = bot.decideIfRaises(stepBuilder.build());
        assertThat(shouldRequestTruco).isTrue();
    }
}
