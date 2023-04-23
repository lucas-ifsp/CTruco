package com.cremonezzi.impl.carlsenbot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CarlsenTest {
    private Carlsen carlsenBot;

    @BeforeEach
    public void config() {
        carlsenBot = new Carlsen();
    }

    @Test
    @DisplayName("Should raise if have zap and and winning")
    public void ShouldRaiseIfZapAndWinning() {
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

        // Game info
        List<GameIntel.RoundResult> roundResults = List.of(
                GameIntel.RoundResult.WON
        );
        List<TrucoCard> openCards = List.of(vira);

        // Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .build();

        assertThat(carlsenBot.decideIfRaises(intel)).isTrue();
    }

    @Test
    @DisplayName("Should choose lowest card in hand if is first playing")
    public void ShouldChooseLowestInHandIfFirstPlaying() {
        TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES);

        // Game info
        List<GameIntel.RoundResult> roundResults = Collections.emptyList();
        List<TrucoCard> openCards = List.of(vira);

        // Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.JACK, CardSuit.CLUBS),
                TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.THREE, CardSuit.SPADES)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .build();

        assertThat(carlsenBot.chooseCard(intel).value().toString()).isEqualTo("[7D]");
    }

    @Test
    @DisplayName("Should choose higher card in hand if is losing")
    public void ShouldChooseHigherInHandIfLosing() {
        TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);

        // Game info
        List<GameIntel.RoundResult> roundResults = List.of(
                GameIntel.RoundResult.LOST
        );
        List<TrucoCard> openCards = List.of(vira);

        // Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                TrucoCard.of(CardRank.ACE, CardSuit.SPADES)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .build();

        assertThat(carlsenBot.chooseCard(intel).value().toString()).isEqualTo("[AS]");
    }

    @Test
    @DisplayName("Should discard when opponent zap")
    public void ShouldDiscardWhenOpponentZap() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.HEARTS);

        // Game info
        List<GameIntel.RoundResult> roundResults = List.of(
                GameIntel.RoundResult.WON
        );
        List<TrucoCard> openCards = List.of(vira);

        // Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 5)
                .opponentScore(2)
                .opponentCard(TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS))
                .build();

        assertThat(carlsenBot.chooseCard(intel).value().toString()).isEqualTo("[XX]");
    }

    @Test
    @DisplayName("Should not accept raise if dont have manilha")
    public void ShoulNotAcceptIfNoManilha() {
        TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS);

        // Game info
        List<GameIntel.RoundResult> roundResults = List.of();
        List<TrucoCard> openCards = List.of(vira);

        // Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES),
                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 5)
                .opponentScore(2)
                .opponentCard(TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS))
                .build();

        assertThat(carlsenBot.getRaiseResponse(intel)).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should accept raise have manilha")
    public void ShoulAcceptIfManilha() {
        TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS);

        // Game info
        List<GameIntel.RoundResult> roundResults = List.of();
        List<TrucoCard> openCards = List.of(vira);

        // Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 5)
                .opponentScore(2)
                .opponentCard(TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS))
                .build();

        assertThat(carlsenBot.getRaiseResponse(intel)).isEqualTo(0);
    }

    @Test
    @DisplayName("Should raise if have more than one manilha")
    public void ShoulRaiseIfManyManilha() {
        TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES);

        // Game info
        List<GameIntel.RoundResult> roundResults = List.of();
        List<TrucoCard> openCards = List.of(vira);

        // Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.JACK, CardSuit.CLUBS)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 5)
                .opponentScore(2)
                .opponentCard(TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS))
                .build();

        assertThat(carlsenBot.getRaiseResponse(intel)).isEqualTo(1);
    }

    @Test
    @DisplayName("Should quit if dont have manilha")
    public void ShoulQuitIfNoManilha() {
        TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS);

        // Game info
        List<GameIntel.RoundResult> roundResults = List.of();
        List<TrucoCard> openCards = List.of(vira);

        // Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 5)
                .opponentScore(2)
                .build();

        assertThat(carlsenBot.getRaiseResponse(intel)).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should choose lowest card in hand if opponent discarded")
    public void ShoulChooseLowestIfDiscarded() {
        TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS);

        // Game info
        List<GameIntel.RoundResult> roundResults = List.of(
                GameIntel.RoundResult.LOST
        );
        List<TrucoCard> openCards = List.of(vira);

        // Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                TrucoCard.of(CardRank.TWO, CardSuit.SPADES)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 5)
                .opponentScore(2)
                .opponentCard(TrucoCard.closed())
                .build();

        assertThat(carlsenBot.chooseCard(intel).value().toString()).isEqualTo("[2S]");
    }

    @Test
    @DisplayName("Should decline mao de onze if hand too low")
    public void ShoulDeclineIfLowHand() {
        TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.SPADES);

        // Game info
        List<GameIntel.RoundResult> roundResults = Collections.emptyList();
        List<TrucoCard> openCards = List.of(vira);

        // Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 11)
                .opponentScore(8)
                .build();

        assertThat(carlsenBot.getMaoDeOnzeResponse(intel)).isFalse();
    }

    @Test
    @DisplayName("Should accept mao de onze if high hand")
    public void ShoulAcceptIfHighHand() {
        TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES);

        // Game info
        List<GameIntel.RoundResult> roundResults = Collections.emptyList();
        List<TrucoCard> openCards = List.of(vira);

        // Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.JACK, CardSuit.CLUBS),
                TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                TrucoCard.of(CardRank.TWO, CardSuit.HEARTS)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 11)
                .opponentScore(8)
                .build();

        assertThat(carlsenBot.getMaoDeOnzeResponse(intel)).isTrue();
    }

    @Test
    @DisplayName("Should raise if there is a zap and other high value in hand")
    public void ShouldRaiseZapAndHigh() {
        TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.CLUBS);

        //Game info
        List<TrucoCard> openCards = List.of(vira);
        List<GameIntel.RoundResult> roundResults = List.of(
                GameIntel.RoundResult.LOST
        );

        //Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                TrucoCard.of(CardRank.ACE, CardSuit.HEARTS)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 10)
                .opponentScore(2)
                .build();

        assertThat(carlsenBot.getRaiseResponse(intel)).isEqualTo(1);
    }

    @Test
    @DisplayName("Should start raise if there is a zap and high card")
    public void ShouldStartRaiseZapAndHigh() {
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);

        //Game info
        List<TrucoCard> openCards = List.of(vira);
        List<GameIntel.RoundResult> roundResults = List.of(
                GameIntel.RoundResult.WON
        );

        //Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 10)
                .opponentScore(2)
                .build();

        assertThat(carlsenBot.decideIfRaises(intel)).isTrue();
    }

    @Test
    @DisplayName("Should not use manilha if can win in other ways")
    public void ShouldNotUseManilhaIfCanWinInOtherWays() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.SPADES);

        //Game info
        List<TrucoCard> openCards = List.of(vira);
        List<GameIntel.RoundResult> roundResults = List.of(
                GameIntel.RoundResult.LOST
        );

        //Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 5)
                .opponentScore(7)
                .opponentCard(TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS))
                .build();

        assertThat(carlsenBot.chooseCard(intel).value().getRank()).isNotEqualTo(CardRank.SEVEN);
    }

    @Test
    @DisplayName("Should calculate hand result as 0")
    public void ShouldCalculateHandResultAsZero() {
        List<GameIntel.RoundResult> roundResults = List.of(
                GameIntel.RoundResult.LOST,
                GameIntel.RoundResult.WON
        );

        assertThat(carlsenBot.calcHandScore(roundResults)).isEqualTo(0);
    }

    @Test
    @DisplayName("Should calculate hand result as 1")
    public void ShouldCalculateHandResultAsOne() {
        List<GameIntel.RoundResult> roundResults = List.of(
                GameIntel.RoundResult.WON
        );

        assertThat(carlsenBot.calcHandScore(roundResults)).isEqualTo(1);
    }

    @Test
    @DisplayName("Should calculate hand result as -1")
    public void ShouldCalculateHandResultAsMinusOne() {
        List<GameIntel.RoundResult> roundResults = List.of(
                GameIntel.RoundResult.LOST
        );

        assertThat(carlsenBot.calcHandScore(roundResults)).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should discard if can't win round and is winning")
    public void ShouldDiscardIfCantWin() {
        TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS);

        //Game info
        List<TrucoCard> openCards = List.of(vira);
        List<GameIntel.RoundResult> roundResults = List.of(
                GameIntel.RoundResult.WON
        );

        //Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 10)
                .opponentScore(2)
                .opponentCard(TrucoCard.of(CardRank.ACE, CardSuit.HEARTS))
                .build();

        assertThat(carlsenBot.chooseCard(intel).value().toString()).isEqualTo("[XX]");
    }

    @Test
    @DisplayName("Should not discard if is losing")
    public void ShouldNotDiscardIfLosing() {
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

        //Game info
        List<TrucoCard> openCards = List.of(vira);
        List<GameIntel.RoundResult> roundResults = List.of(
                GameIntel.RoundResult.LOST
        );

        //Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.SIX, CardSuit.CLUBS),
                TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS))
                .build();

        assertThat(carlsenBot.chooseCard(intel).value().toString()).isNotEqualTo("[XX]");
    }

    @Test
    @DisplayName("Should not discard if is tied")
    public void ShouldNotDiscardIfTied() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.CLUBS);

        //Game info
        List<TrucoCard> openCards = List.of(vira);
        List<GameIntel.RoundResult> roundResults = List.of(
                GameIntel.RoundResult.DREW
        );

        //Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS))
                .build();

        assertThat(carlsenBot.chooseCard(intel).value().toString()).isNotEqualTo("[XX]");
    }

    @Test
    @DisplayName("Should not discard if in first round")
    public void ShouldDiscardIfInFirstRound() {
        TrucoCard vira = TrucoCard.of(CardRank.THREE, CardSuit.CLUBS);

        //Game info
        List<TrucoCard> openCards = List.of(vira);
        List<GameIntel.RoundResult> roundResults = Collections.emptyList();

        //Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.SIX, CardSuit.SPADES),
                TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS),
                TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .build();

        assertThat(carlsenBot.chooseCard(intel).value().toString()).isNotEqualTo("[XX]");
    }

    @Test
    @DisplayName("Should raise if opponent already played and we can win the hand")
    public void ShouldRaiseIfOpponentPlayedAndCanWinHand() {
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);

        //Game info
        List<TrucoCard> openCards = List.of(vira);
        List<GameIntel.RoundResult> roundResults = List.of(
                GameIntel.RoundResult.WON
        );

        //Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(TrucoCard.of(CardRank.SIX, CardSuit.HEARTS))
                .build();

        assertThat(carlsenBot.decideIfRaises(intel)).isTrue();
    }

    @Test
    @DisplayName("Should choose lowest card in hand if that card can win")
    public void ShouldLowestCardIfThatCardCanWin() {
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);

        //Game info
        List<TrucoCard> openCards = List.of(vira);
        List<GameIntel.RoundResult> roundResults = Collections.emptyList();

        //Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FOUR, CardSuit.SPADES)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS))
                .build();

        assertThat(carlsenBot.chooseCard(intel).value().toString()).isEqualTo("[KD]");
    }

    @Test
    @DisplayName("Should not raise if opponent already played and we can can't win the hand")
    public void ShouldNotRaiseIfOpponentPlayedAndCantWinHand() {
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);

        //Game info
        List<TrucoCard> openCards = List.of(vira);
        List<GameIntel.RoundResult> roundResults = List.of(
                GameIntel.RoundResult.LOST
        );

        //Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(TrucoCard.of(CardRank.TWO, CardSuit.HEARTS))
                .build();

        assertThat(carlsenBot.decideIfRaises(intel)).isFalse();
    }

    @Test
    @DisplayName("Should quit if lost and have bad hand")
    public void ShouldQuitBadHand(){
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);

        //Game info
        List<TrucoCard> openCards = List.of(vira);
        List<GameIntel.RoundResult> roundResults = List.of(
                GameIntel.RoundResult.LOST
        );

        //Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                TrucoCard.of(CardRank.TWO, CardSuit.SPADES)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 1)
                .opponentScore(1)
                .opponentCard(TrucoCard.of(CardRank.JACK, CardSuit.CLUBS))
                .build();

        assertThat(carlsenBot.getRaiseResponse(intel)).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should accept raise on first round if have a medium hand")
    public void ShouldAcceptMediumHandFirstRound(){
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);

        //Game info
        List<TrucoCard> openCards = List.of(vira);
        List<GameIntel.RoundResult> roundResults = List.of();

        //Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 0)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .build();

        assertThat(carlsenBot.getRaiseResponse(intel)).isEqualTo(0);
    }

    @Test
    @DisplayName("Should raise on first round if have a good hand without manilha")
    public void ShouldRaiseGoodHandFirstRound(){
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);

        //Game info
        List<TrucoCard> openCards = List.of(vira);
        List<GameIntel.RoundResult> roundResults = List.of();

        //Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS),
                TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 0)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .build();

        assertThat(carlsenBot.getRaiseResponse(intel)).isEqualTo(1);
    }

    @Test
    @DisplayName("Should accept raise if lost first round if have a medium hand without manilha")
    public void ShouldAcceptRaiseMediumHandLost(){
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);

        //Game info
        List<TrucoCard> openCards = List.of(vira);
        List<GameIntel.RoundResult> roundResults = List.of(
                GameIntel.RoundResult.LOST
        );

        //Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS),
                TrucoCard.of(CardRank.THREE, CardSuit.SPADES)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 0)
                .botInfo(botCards, 0)
                .opponentScore(1)
                .build();

        assertThat(carlsenBot.getRaiseResponse(intel)).isEqualTo(0);
    }

    @Test
    @DisplayName("Should raise if lost first round if have a good hand without manilha")
    public void ShouldRaiseGoodHandLost(){
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);

        //Game info
        List<TrucoCard> openCards = List.of(vira);
        List<GameIntel.RoundResult> roundResults = List.of(
                GameIntel.RoundResult.LOST
        );

        //Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.THREE, CardSuit.SPADES)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 0)
                .botInfo(botCards, 0)
                .opponentScore(1)
                .build();

        assertThat(carlsenBot.getRaiseResponse(intel)).isEqualTo(1);
    }

    @Test
    @DisplayName("Should raise if only have fours in hand")
    public void ShouldRaiseOnlyFours(){
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);

        //Game info
        List<TrucoCard> openCards = List.of(vira);
        List<GameIntel.RoundResult> roundResults = List.of();

        //Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 0)
                .botInfo(botCards, 0)
                .opponentScore(1)
                .build();

        assertThat(carlsenBot.getRaiseResponse(intel)).isEqualTo(1);
    }

    @Test
    @DisplayName("Should start raise if only have fours in hand")
    public void ShouldStartRaiseOnlyFours(){
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);

        //Game info
        List<TrucoCard> openCards = List.of(vira);
        List<GameIntel.RoundResult> roundResults = List.of();

        //Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 0)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .build();

        assertThat(carlsenBot.decideIfRaises(intel)).isTrue();
    }

    @Test
    @DisplayName("Should raise on first round if opponent already played and has manilha")
    public void ShouldRaiseFirstRoundIfOpponentPlayedHasManilha() {
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);

        //Game info
        List<TrucoCard> openCards = List.of(vira);
        List<GameIntel.RoundResult> roundResults = List.of();

        //Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.TWO, CardSuit.SPADES)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(TrucoCard.of(CardRank.SIX, CardSuit.HEARTS))
                .build();

        assertThat(carlsenBot.decideIfRaises(intel)).isTrue();
    }

    @Test
    @DisplayName("Should play a card to draw the current round if have a high value card")
    public void ShouldDrawIfHasHighCard(){
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);

        //Game info
        List<TrucoCard> openCards = List.of(vira);
        List<GameIntel.RoundResult> roundResults = List.of();

        //Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.THREE, CardSuit.SPADES)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(TrucoCard.of(CardRank.SIX, CardSuit.HEARTS))
                .build();

        assertThat(carlsenBot.chooseCard(intel).value()).isEqualTo(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));
    }

    @Test
    @DisplayName("Should raise if have a manilha and drew the previous round")
    public void ShouldRaiseHadDrewAndHasManilha(){
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);

        //Game info
        List<TrucoCard> openCards = List.of(vira);
        List<GameIntel.RoundResult> roundResults = List.of(
                GameIntel.RoundResult.DREW
        );

        //Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.TWO, CardSuit.SPADES)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .build();

        assertThat(carlsenBot.decideIfRaises(intel)).isTrue();
    }

    @Test
    @DisplayName("Should quit if drew and don't have manilha")
    public void ShouldQuitDrewAndNoManilha(){
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);

        //Game info
        List<TrucoCard> openCards = List.of(vira);
        List<GameIntel.RoundResult> roundResults = List.of(
                GameIntel.RoundResult.DREW
        );

        //Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .build();

        assertThat(carlsenBot.decideIfRaises(intel)).isFalse();
    }

    @Test
    @DisplayName("Should accept raise if opponent called it before playing a card and have medium hand without manilha")
    public void ShouldAcceptRaiseBeforeOpponentPlayWithMediumHand(){
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);

        //Game info
        List<TrucoCard> openCards = List.of(vira);
        List<GameIntel.RoundResult> roundResults = List.of();

        //Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .build();

        assertThat(carlsenBot.getRaiseResponse(intel)).isEqualTo(0);
    }

    @Test
    @DisplayName("Should raise again if opponent called it before playing a card and have good hand without manilha")
    public void ShouldRaiseBeforeOpponentPlayWithMediumHand(){
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);

        //Game info
        List<TrucoCard> openCards = List.of(vira);
        List<GameIntel.RoundResult> roundResults = List.of();

        //Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                TrucoCard.of(CardRank.ACE, CardSuit.HEARTS)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .build();

        assertThat(carlsenBot.getRaiseResponse(intel)).isEqualTo(1);
    }

    @Test
    @DisplayName("Should accept raise if opponent called it before playing a card and have medium hand with manilha")
    public void ShouldAcceptRaiseBeforeOpponentPlayWithMediumHandWithManilha(){
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);

        //Game info
        List<TrucoCard> openCards = List.of(vira);
        List<GameIntel.RoundResult> roundResults = List.of();

        //Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                TrucoCard.of(CardRank.TWO, CardSuit.HEARTS)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .build();

        assertThat(carlsenBot.getRaiseResponse(intel)).isEqualTo(0);
    }

    @Test
    @DisplayName("Should raise again if opponent called it before playing a card and have good hand with manilha")
    public void ShouldRaiseBeforeOpponentPlayWithGoodHandWithManilha(){
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);

        //Game info
        List<TrucoCard> openCards = List.of(vira);
        List<GameIntel.RoundResult> roundResults = List.of();

        //Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                TrucoCard.of(CardRank.TWO, CardSuit.HEARTS)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .build();

        assertThat(carlsenBot.getRaiseResponse(intel)).isEqualTo(1);
    }
}