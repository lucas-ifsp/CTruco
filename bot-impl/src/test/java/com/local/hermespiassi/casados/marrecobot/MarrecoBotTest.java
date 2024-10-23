/*
 *  Copyright (C) 2023 Nathan Hermes Gonçalves da Silva, Pedro Augusto Correia Piassi - IFSP/SCL
 *  Contact: n <dot> hermes <at> aluno <dot> ifsp <dot> edu <dot> br, piassi <dot> pedro <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.local.hermespiassi.casados.marrecobot;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;
import static com.bueno.spi.model.CardSuit.HIDDEN;
import static com.bueno.spi.model.GameIntel.RoundResult.LOST;
import static com.bueno.spi.model.GameIntel.RoundResult.WON;
import static org.assertj.core.api.Assertions.assertThat;

class MarrecoBotTest {
  private GameIntel.StepBuilder stepBuilder;
  private List<GameIntel.RoundResult> results;
  private List<TrucoCard> openCards;
  private List<TrucoCard> botCards;
  private TrucoCard vira;

  @Test
  @DisplayName("Should not return card hidden in first and not manilha")
  void ShouldNotReturnCardHiddenInFirstAndNotManilha() {
    results = List.of();
    botCards = List.of(TrucoCard.of(SIX, HEARTS), TrucoCard.of(FIVE, CLUBS), TrucoCard.of(ACE, DIAMONDS));
    vira = TrucoCard.of(ACE, HEARTS);
    openCards = List.of(vira);
    stepBuilder = GameIntel.StepBuilder.with()
            .gameInfo(results, openCards, vira, 1)
            .botInfo(botCards, 0)
            .opponentScore(0);

    CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());
    assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(ACE, DIAMONDS));
  }

  @Test
  @DisplayName("Should return pica-fumo in first raise if bot has a pica-fumo")
  void shouldReturnPicaFumoInFirstRaiseIfBotHasAPicaFumo() {
    results = List.of();
    botCards = List.of(TrucoCard.of(TWO, HEARTS), TrucoCard.of(FIVE, CLUBS), TrucoCard.of(TWO, DIAMONDS));
    vira = TrucoCard.of(ACE, HEARTS);
    openCards = List.of(vira);
    stepBuilder = GameIntel.StepBuilder.with()
        .gameInfo(results, openCards, vira, 1)
        .botInfo(botCards, 0)
        .opponentScore(0);

    CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());
    assertThat(cardToPlay.value().getSuit()).isEqualTo(DIAMONDS);
  }

  @Test
  @DisplayName("Should not return pica-fumo if opponent card is manilha")
  void shouldNotReturnPicaFumoIfOpponentCardIsManilha() {
    results = List.of();
    botCards = List.of(
        TrucoCard.of(TWO, HEARTS),
        TrucoCard.of(FIVE, CLUBS),
        TrucoCard.of(TWO, DIAMONDS)
    );
    vira = TrucoCard.of(ACE, HEARTS);
    openCards = List.of(vira, TrucoCard.of(TWO, SPADES));
    stepBuilder = GameIntel.StepBuilder.with()
        .gameInfo(results, openCards, vira, 1)
        .botInfo(botCards, 0)
        .opponentScore(0)
        .opponentCard(TrucoCard.of(TWO, SPADES));

    CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());

    assertThat(cardToPlay.value().getSuit()).isNotEqualTo(DIAMONDS);
  }

  @Test
  @DisplayName("Should return the biggest card if has is no manilha, in the first round")
  void ShouldReturnTheBiggestCardIfHasIsNoManilhaInYheFirstRound() {
    results = List.of();
    botCards = List.of(
        TrucoCard.of(FIVE, HEARTS),
        TrucoCard.of(FOUR, DIAMONDS),
        TrucoCard.of(FIVE, CLUBS)
    );
    vira = TrucoCard.of(ACE, HEARTS);
    openCards = List.of(vira);
    stepBuilder = GameIntel.StepBuilder.with()
        .gameInfo(results, openCards, vira, 1)
        .botInfo(botCards, 0)
        .opponentScore(0);

    CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());
    assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(FIVE, CLUBS));
  }

  @Test
  @DisplayName("Should return the less card")
  void ShouldReturnTheLessCard() {
    results = List.of();
    botCards = List.of(
        TrucoCard.of(TWO, CLUBS),
        TrucoCard.of(FIVE, DIAMONDS),
        TrucoCard.of(SEVEN, HEARTS)
    );
    vira = TrucoCard.of(TWO, HEARTS);
    openCards = List.of(vira, TrucoCard.of(FOUR, SPADES));
    stepBuilder = GameIntel.StepBuilder.with()
        .gameInfo(results, openCards, vira, 1)
        .botInfo(botCards, 0)
        .opponentScore(0)
        .opponentCard(TrucoCard.of(FOUR, SPADES));

    CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());
    assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(FIVE, DIAMONDS));
  }

  @Test
  @DisplayName("Should choose lowest card when opponent card has hidden")
  void ShouldChooseLowestCardWhenOpponentCardHasHidden() {
    results = List.of();
    botCards = List.of(
            TrucoCard.of(TWO, CLUBS),
            TrucoCard.of(FIVE, DIAMONDS),
            TrucoCard.of(FOUR, HEARTS)
    );
    vira = TrucoCard.of(TWO, HEARTS);
    openCards = List.of(vira, TrucoCard.of(CardRank.HIDDEN, HIDDEN));
    stepBuilder = GameIntel.StepBuilder.with()
            .gameInfo(results, openCards, vira, 1)
            .botInfo(botCards, 0)
            .opponentScore(0)
            .opponentCard(TrucoCard.of(CardRank.HIDDEN, HIDDEN));

    CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());
    assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(FOUR, HEARTS));
  }

  @Test
  @DisplayName("Should return three if has two three")
  void ShouldReturnThreeIfHasTwoThree() {
    results = List.of();
    botCards = List.of(
        TrucoCard.of(THREE, DIAMONDS),
        TrucoCard.of(THREE, CLUBS),
        TrucoCard.of(FOUR, HEARTS)
    );
    vira = TrucoCard.of(ACE, HEARTS);
    openCards = List.of(vira, TrucoCard.of(ACE, SPADES));
    stepBuilder = GameIntel.StepBuilder.with()
        .gameInfo(results, openCards, vira, 1)
        .botInfo(botCards, 0)
        .opponentScore(0)
        .opponentCard(TrucoCard.of(FOUR, SPADES));

    CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());
    assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(THREE, DIAMONDS));
  }

  @Test
  @DisplayName("Should amarrar if not has card for win and has two three")
  void ShouldAmarrarIfNotHasCardForWin() {
    results = List.of();
    botCards = List.of(
        TrucoCard.of(THREE, CLUBS),
        TrucoCard.of(THREE, DIAMONDS),
        TrucoCard.of(FOUR, HEARTS)
    );
    vira = TrucoCard.of(ACE, HEARTS);
    openCards = List.of(vira, TrucoCard.of(THREE, SPADES));
    stepBuilder = GameIntel.StepBuilder.with()
        .gameInfo(results, openCards, vira, 1)
        .botInfo(botCards, 0)
        .opponentScore(0)
        .opponentCard(TrucoCard.of(FOUR, SPADES));

    CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());
    assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(THREE, CLUBS));
  }

  @Test
  @DisplayName("Should amarrar if bot has zap and first round")
  void ShouldAmarrarIfBotHasZapAndFirstRound() {
    results = List.of();
    botCards = List.of(
            TrucoCard.of(THREE, CLUBS),
            TrucoCard.of(SEVEN, DIAMONDS),
            TrucoCard.of(TWO, CLUBS)
    );
    vira = TrucoCard.of(ACE, HEARTS);
    openCards = List.of(vira, TrucoCard.of(SEVEN, SPADES));
    stepBuilder = GameIntel.StepBuilder.with()
            .gameInfo(results, openCards, vira, 1)
            .botInfo(botCards, 0)
            .opponentScore(0)
            .opponentCard(TrucoCard.of(SEVEN, SPADES));

    CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());
    assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(SEVEN, DIAMONDS));
  }

  @Nested
  @DisplayName("Tests bot logic when bot have manilha and opponent cards are manilha")
  class OpponentCardIsManilha {
    @BeforeEach
    void beforeEach() {
      results = List.of();
      vira = TrucoCard.of(ACE, DIAMONDS);
    }

    @Test
    @DisplayName("Should return single manilha that bot has when opponent card is of diamond")
    void shouldReturnSingleManilhaThatBotHasWhenOpponentCardIsOfDiamond() {
      botCards = List.of(TrucoCard.of(FOUR, DIAMONDS), TrucoCard.of(TWO, SPADES), TrucoCard.of(THREE, CLUBS));
      openCards = List.of(vira, TrucoCard.of(TWO, DIAMONDS));
      stepBuilder = GameIntel.StepBuilder.with()
          .gameInfo(results, openCards, vira, 1)
          .botInfo(botCards, 0)
          .opponentScore(0)
          .opponentCard(TrucoCard.of(TWO, DIAMONDS));

      CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());
      assertThat(cardToPlay.value().getSuit()).isEqualTo(SPADES);
    }

    @Test
    @DisplayName("Should return less manilha if opponent card is pica-fumo and bot have two manilhas")
    void shouldReturnLessManilhaIfOpponentCardIsPicaFumoAndBotHaveTwoManilhas() {
      results = List.of();
      botCards = List.of(
          TrucoCard.of(TWO, HEARTS),
          TrucoCard.of(FIVE, CLUBS),
          TrucoCard.of(TWO, SPADES)
      );
      vira = TrucoCard.of(ACE, HEARTS);
      openCards = List.of(vira, TrucoCard.of(TWO, DIAMONDS));
      stepBuilder = GameIntel.StepBuilder.with()
          .gameInfo(results, openCards, vira, 1)
          .botInfo(botCards, 0)
          .opponentScore(0)
          .opponentCard(TrucoCard.of(TWO, DIAMONDS));

      CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());

      assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(TWO, SPADES));
    }

    @Test
    @DisplayName("Should return greater manilha if opponent card is pica-fumo and bot have three manilhas")
    void shouldReturnGreaterManilhaIfOpponentCardIsPicaFumoAndBotHaveThreeManilhas() {
      results = List.of();
      botCards = List.of(
          TrucoCard.of(TWO, HEARTS),
          TrucoCard.of(TWO, CLUBS),
          TrucoCard.of(TWO, SPADES)
      );
      vira = TrucoCard.of(ACE, HEARTS);
      openCards = List.of(vira, TrucoCard.of(TWO, DIAMONDS));
      stepBuilder = GameIntel.StepBuilder.with()
          .gameInfo(results, openCards, vira, 1)
          .botInfo(botCards, 0)
          .opponentScore(0)
          .opponentCard(TrucoCard.of(TWO, DIAMONDS));

      CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());

      assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(TWO, CLUBS));
    }

    @Test
    @DisplayName("Should return manilha if opponent card is spades and bot have a greater manilha")
    void shouldReturnManilhaIfOpponentCardIsSpadesAndBotHaveAGreaterManilha() {
      results = List.of();
      botCards = List.of(
          TrucoCard.of(THREE, DIAMONDS),
          TrucoCard.of(TWO, HEARTS),
          TrucoCard.of(FOUR, SPADES)
      );
      vira = TrucoCard.of(ACE, HEARTS);
      openCards = List.of(vira, TrucoCard.of(TWO, SPADES));
      stepBuilder = GameIntel.StepBuilder.with()
          .gameInfo(results, openCards, vira, 1)
          .botInfo(botCards, 0)
          .opponentScore(0)
          .opponentCard(TrucoCard.of(TWO, SPADES));

      CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());

      assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(TWO, HEARTS));
    }

    @Test
    @DisplayName("Should return greater manilha if opponent card is spades and bot have one greater manilha")
    void shouldReturnGreaterManilhaIfOpponentCardIsSpadesAndBotHaveOneGreaterManilha() {
      results = List.of();
      botCards = List.of(
          TrucoCard.of(FOUR, DIAMONDS),
          TrucoCard.of(TWO, DIAMONDS),
          TrucoCard.of(TWO, CLUBS)
      );
      vira = TrucoCard.of(ACE, HEARTS);
      openCards = List.of(vira, TrucoCard.of(TWO, SPADES));
      stepBuilder = GameIntel.StepBuilder.with()
          .gameInfo(results, openCards, vira, 1)
          .botInfo(botCards, 0)
          .opponentScore(0)
          .opponentCard(TrucoCard.of(TWO, SPADES));

      CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());

      assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(TWO, CLUBS));
    }

    @Test
    @DisplayName("Should return less manilha if opponent card is spades and bot have two greater manilha")
    void shouldReturnLessManilhaIfOpponentCardIsSpadesAndBotHaveTwoGreaterManilha() {
      results = List.of();
      botCards = List.of(
          TrucoCard.of(TWO, CLUBS),
          TrucoCard.of(FIVE, DIAMONDS),
          TrucoCard.of(TWO, HEARTS)
      );
      vira = TrucoCard.of(ACE, HEARTS);
      openCards = List.of(vira, TrucoCard.of(TWO, SPADES));
      stepBuilder = GameIntel.StepBuilder.with()
          .gameInfo(results, openCards, vira, 1)
          .botInfo(botCards, 0)
          .opponentScore(0)
          .opponentCard(TrucoCard.of(TWO, SPADES));

      CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());

      assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(TWO, HEARTS));
    }

    @Test
    @DisplayName("Should return card of clubs that bot has when opponent card is of hearts")
    void ShouldReturnCardOfClubsThatBotHasWhenOpponentCardIsOfHearts() {
      results = List.of();
      botCards = List.of(
          TrucoCard.of(THREE, HEARTS),
          TrucoCard.of(FIVE, DIAMONDS),
          TrucoCard.of(TWO, CLUBS)
      );
      vira = TrucoCard.of(ACE, HEARTS);
      openCards = List.of(vira, TrucoCard.of(TWO, HEARTS));
      stepBuilder = GameIntel.StepBuilder.with()
          .gameInfo(results, openCards, vira, 1)
          .botInfo(botCards, 0)
          .opponentScore(0)
          .opponentCard(TrucoCard.of(TWO, HEARTS));

      CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());

      assertThat(cardToPlay.value().getSuit()).isEqualTo(CLUBS);
    }

    @Nested
    @DisplayName("Opponent card is manilha of clubs")
    class ManilhaOfClubs {
      @BeforeEach
      void beforeEach() {
        openCards = List.of(vira, TrucoCard.of(TWO, CLUBS));
      }

      @Test
      @DisplayName("Should return weak card when bot has one manilha")
      void shouldReturnWeakCardWhenBotHasOneManilha() {
        botCards = List.of(TrucoCard.of(KING, CLUBS), TrucoCard.of(TWO, SPADES), TrucoCard.of(SEVEN, DIAMONDS));
        stepBuilder = GameIntel.StepBuilder.with()
            .gameInfo(results, openCards, vira, 1)
            .botInfo(botCards, 0)
            .opponentScore(0)
            .opponentCard(TrucoCard.of(TWO, CLUBS));

        CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());
        assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(SEVEN, DIAMONDS));
      }

      @Test
      @DisplayName("Should return single weak card when bot has two manilhas")
      void shouldReturnSingleWeakCardWhenBotHasTwo() {
        botCards = List.of(TrucoCard.of(TWO, DIAMONDS), TrucoCard.of(TWO, SPADES), TrucoCard.of(SEVEN, DIAMONDS));
        stepBuilder = GameIntel.StepBuilder.with()
            .gameInfo(results, openCards, vira, 1)
            .botInfo(botCards, 0)
            .opponentScore(0)
            .opponentCard(TrucoCard.of(TWO, CLUBS));

        CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());
        assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(SEVEN, DIAMONDS));
      }

      @Test
      @DisplayName("Should return weak manilha when boy has three manilhas")
      void shouldReturnWeakManilhaWhenBoyHasThreeManilhas() {
        botCards = List.of(TrucoCard.of(TWO, SPADES), TrucoCard.of(TWO, DIAMONDS), TrucoCard.of(TWO, HEARTS));
        stepBuilder = GameIntel.StepBuilder.with()
            .gameInfo(results, openCards, vira, 1)
            .botInfo(botCards, 0)
            .opponentScore(0)
            .opponentCard(TrucoCard.of(TWO, CLUBS));

        CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());
        assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(TWO, DIAMONDS));
      }

      @Test
      @DisplayName("Shoul return of more weak card when bot has zap")
      void ShoulReturnOfMoreWeakCardWhenBotHasZap() {
        botCards = List.of(TrucoCard.of(TWO, SPADES), TrucoCard.of(THREE, HEARTS), TrucoCard.of(FOUR, DIAMONDS));
        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(results, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(TrucoCard.of(TWO, CLUBS));

        CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());
        assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(FOUR, DIAMONDS));
      }
    }
  }

  @Test
  @DisplayName("Should return highest card when tied")
  void ShouldReturnHighestCardWhenTied() {
    results = List.of(WON, LOST);
    botCards = List.of(TrucoCard.of(THREE, SPADES));
    vira = TrucoCard.of(ACE, HEARTS);
    openCards = List.of(vira,
            TrucoCard.of(TWO, DIAMONDS), TrucoCard.of(TWO, SPADES),
            TrucoCard.of(FOUR, CLUBS), TrucoCard.of(FIVE, CLUBS)
    );
    stepBuilder = GameIntel.StepBuilder.with()
            .gameInfo(results, openCards, vira, 1)
            .botInfo(botCards, 0)
            .opponentScore(0);

    CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());
    assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(THREE, SPADES));
  }

  @Nested
  @DisplayName("Test bot logic when bot have manilha and opponent cards are not manilha")
  class OpponentCardIsNotManilha {
    @BeforeEach
    void beforeEach() {
      results = List.of();
      vira = TrucoCard.of(ACE, DIAMONDS);
    }

    @Nested
    @DisplayName("Bot has only manilha of diamond")
    class BotManilhaOfDiamond {
      @Test
      @DisplayName("Should return manilha of diamond when opponent card is not manilha but is greater other bot cards")
      void shouldReturnManilhaOfDiamondWhenOpponentCardIsNotManilhaButIsGreaterOtherBotCards() {
        botCards = List.of(TrucoCard.of(FOUR, CLUBS), TrucoCard.of(TWO, DIAMONDS), TrucoCard.of(KING, DIAMONDS));
        openCards = List.of(vira, TrucoCard.of(THREE, CLUBS));
        stepBuilder = GameIntel.StepBuilder.with()
            .gameInfo(results, openCards, vira, 1)
            .botInfo(botCards, 0)
            .opponentScore(0)
            .opponentCard(TrucoCard.of(THREE, CLUBS));

        CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());
        assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(TWO, DIAMONDS));
      }

      @Test
      @DisplayName("Should return greater card that bot has when opponent card is greater but not is manilha")
      void shouldReturnGreaterCardThatBotHasWhenOpponentCardIsGreaterButNotIsManilha() {
        botCards = List.of(TrucoCard.of(FOUR, CLUBS), TrucoCard.of(TWO, DIAMONDS), TrucoCard.of(SIX, DIAMONDS));
        openCards = List.of(vira, TrucoCard.of(FIVE, DIAMONDS));
        stepBuilder = GameIntel.StepBuilder.with()
            .gameInfo(results, openCards, vira, 1)
            .botInfo(botCards, 0)
            .opponentScore(0)
            .opponentCard(TrucoCard.of(FIVE, DIAMONDS));

        CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());
        assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(SIX, DIAMONDS));
      }

      @Test
      @DisplayName("Should return weak card of greater cards, but not manilha ")
      void shouldReturnWeakCardOfGreaterCardsButNotManilha() {
        botCards = List.of(TrucoCard.of(QUEEN, CLUBS), TrucoCard.of(TWO, DIAMONDS), TrucoCard.of(SIX, SPADES));
        openCards = List.of(vira, TrucoCard.of(FIVE, DIAMONDS));
        stepBuilder = GameIntel.StepBuilder.with()
            .gameInfo(results, openCards, vira, 1)
            .botInfo(botCards, 0)
            .opponentScore(0)
            .opponentCard(TrucoCard.of(FIVE, DIAMONDS));

        CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());
        assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(SIX, SPADES));
      }
    }
  }

  @Nested
  @DisplayName("Test logic to bot return true when it has manilhas or card of rank three")
  class TrueToRaise {

    @Test
    @DisplayName("Should return true when bot win first round and has only one manilha that is of clubs")
    void shouldReturnTrueWhenBotWinFirstRoundAndHasOnlyOneManilhaThatIsOfClubs() {
      results = List.of(WON);
      botCards = List.of(
          TrucoCard.of(THREE, HEARTS),
          TrucoCard.of(TWO, CLUBS)
      );
      vira = TrucoCard.of(ACE, HEARTS);
      openCards = List.of(vira, TrucoCard.of(QUEEN, SPADES), TrucoCard.of(KING, HEARTS));
      stepBuilder = GameIntel.StepBuilder.with()
          .gameInfo(results, openCards, vira, 1)
          .botInfo(botCards, 0)
          .opponentScore(0);

      Boolean responseRaise = new MarrecoBot().decideIfRaises(stepBuilder.build());
      assertThat(responseRaise).as("Return true when bot win first round and has manilha of clubs").isEqualTo(true);
    }

    @Test
    @DisplayName("Should return true when bot win first round and has only one manilha that is of hearts")
    void shouldReturnTrueWhenBotWinFirstRoundAndHasOnlyOneManilhaThatIsOfHearts() {
      results = List.of(WON);
      botCards = List.of(
          TrucoCard.of(FIVE, HEARTS),
          TrucoCard.of(TWO, HEARTS)
      );
      vira = TrucoCard.of(ACE, HEARTS);
      openCards = List.of(vira, TrucoCard.of(QUEEN, SPADES), TrucoCard.of(KING, HEARTS));
      stepBuilder = GameIntel.StepBuilder.with()
          .gameInfo(results, openCards, vira, 1)
          .botInfo(botCards, 0)
          .opponentScore(0);

      Boolean responseRaise = new MarrecoBot().decideIfRaises(stepBuilder.build());
      assertThat(responseRaise)
          .as("Return true when bot win first round and has manilha of hearts")
          .isEqualTo(true);
    }

    @Test
    @DisplayName("Should return true when bot win first round and has only one manilha that is of spades")
    void shouldReturnTrueWhenBotWinFirstRoundAndHasOnlyOneManilhaThatIsOfSpades() {
      results = List.of(WON);
      botCards = List.of(
          TrucoCard.of(FIVE, HEARTS),
          TrucoCard.of(TWO, SPADES)
      );
      vira = TrucoCard.of(ACE, HEARTS);
      openCards = List.of(vira, TrucoCard.of(QUEEN, SPADES), TrucoCard.of(KING, HEARTS));
      stepBuilder = GameIntel.StepBuilder.with()
          .gameInfo(results, openCards, vira, 1)
          .botInfo(botCards, 0)
          .opponentScore(0);

      Boolean responseRaise = new MarrecoBot().decideIfRaises(stepBuilder.build());
      assertThat(responseRaise)
          .as("Return true when bot win first round and has manilha of spades")
          .isEqualTo(true);
    }

    @Test
    @DisplayName("Should return true when bot win first round and has only one manilha that is of diamond")
    void shouldReturnTrueWhenBotWinFirstRoundAndHasOnlyOneManilhaThatIsOfDiamond() {
      results = List.of(WON);
      botCards = List.of(
          TrucoCard.of(FIVE, HEARTS),
          TrucoCard.of(TWO, DIAMONDS)
      );
      vira = TrucoCard.of(ACE, HEARTS);
      openCards = List.of(vira, TrucoCard.of(QUEEN, SPADES), TrucoCard.of(KING, HEARTS));
      stepBuilder = GameIntel.StepBuilder.with()
          .gameInfo(results, openCards, vira, 1)
          .botInfo(botCards, 0)
          .opponentScore(0);

      Boolean responseRaise = new MarrecoBot().decideIfRaises(stepBuilder.build());
      assertThat(responseRaise)
          .as("Return true when bot win first round and has manilha of diamond")
          .isEqualTo(true);
    }

    @Test
    @DisplayName("Should return true when bot win first round and has two manilhas")
    void shouldReturnTrueWhenBotWinFirstRoundAndHasTwoManilhas() {
      results = List.of(WON);
      botCards = List.of(
          TrucoCard.of(TWO, CLUBS),
          TrucoCard.of(TWO, DIAMONDS)
      );
      vira = TrucoCard.of(ACE, HEARTS);
      openCards = List.of(vira, TrucoCard.of(QUEEN, SPADES), TrucoCard.of(KING, HEARTS));
      stepBuilder = GameIntel.StepBuilder.with()
          .gameInfo(results, openCards, vira, 1)
          .botInfo(botCards, 0)
          .opponentScore(0);

      Boolean responseRaise = new MarrecoBot().decideIfRaises(stepBuilder.build());
      assertThat(responseRaise)
          .as("Return true when bot win first round and has two manilhas")
          .isEqualTo(true);
    }

    @Test
    @DisplayName("Should return true when bot lost first round and has two manilhas")
    void shouldReturnTrueWhenBotLostFirstRoundAndHasTwoManilhas() {
      results = List.of(LOST);
      botCards = List.of(
          TrucoCard.of(TWO, CLUBS),
          TrucoCard.of(TWO, DIAMONDS)
      );
      vira = TrucoCard.of(ACE, HEARTS);
      openCards = List.of(vira, TrucoCard.of(KING, SPADES), TrucoCard.of(QUEEN, HEARTS));
      stepBuilder = GameIntel.StepBuilder.with()
          .gameInfo(results, openCards, vira, 1)
          .botInfo(botCards, 0)
          .opponentScore(0);

      Boolean responseRaise = new MarrecoBot().decideIfRaises(stepBuilder.build());
      assertThat(responseRaise)
          .as("Return true when bot lost first round and has two manilhas")
          .isEqualTo(true);
    }

    @Test
    @DisplayName("Should return true when bot win first round and has card of rank three")
    void shouldReturnTrueWhenBotWinFirstRoundAndHasCardOfRankThree() {
      results = List.of(WON);
      botCards = List.of(
          TrucoCard.of(FIVE, HEARTS),
          TrucoCard.of(THREE, DIAMONDS)
      );
      vira = TrucoCard.of(ACE, HEARTS);
      openCards = List.of(vira, TrucoCard.of(QUEEN, SPADES), TrucoCard.of(KING, HEARTS));
      stepBuilder = GameIntel.StepBuilder.with()
          .gameInfo(results, openCards, vira, 1)
          .botInfo(botCards, 0)
          .opponentScore(0);

      Boolean responseRaise = new MarrecoBot().decideIfRaises(stepBuilder.build());
      assertThat(responseRaise)
          .as("Return true when bot win first round and has card of rank three")
          .isEqualTo(true);
    }
  }

  @Nested
  @DisplayName("Test bot logic to accept truco")
  class AcceptTruco {
    @Test
    @DisplayName("Should accept truco when bot has 2 manilhas")
    void shouldAcceptTrucoWhenBotHas2Manilhas() {
      results = List.of();
      botCards = List.of(TrucoCard.of(SEVEN, SPADES), TrucoCard.of(TWO, SPADES), TrucoCard.of(TWO, HEARTS));
      vira = TrucoCard.of(ACE, HEARTS);
      openCards = List.of(vira);
      stepBuilder = GameIntel.StepBuilder.with()
          .gameInfo(results, openCards, vira, 1)
          .botInfo(botCards, 0)
          .opponentScore(0);

      int responseRaise = new MarrecoBot().getRaiseResponse(stepBuilder.build());
      assertThat(responseRaise)
          .as("Return 0 when bot has 2 manilhas.")
          .isZero();
    }

    @Test
    @DisplayName("Should accept truco when bot has 3 manilhas")
    void shouldAcceptTrucoWhenBotHas3Manilhas() {
      results = List.of();
      botCards = List.of(TrucoCard.of(TWO, DIAMONDS), TrucoCard.of(TWO, SPADES), TrucoCard.of(TWO, HEARTS));
      vira = TrucoCard.of(ACE, HEARTS);
      openCards = List.of(vira);
      stepBuilder = GameIntel.StepBuilder.with()
              .gameInfo(results, openCards, vira, 1)
              .botInfo(botCards, 0)
              .opponentScore(0);

      int responseRaise = new MarrecoBot().getRaiseResponse(stepBuilder.build());
      assertThat(responseRaise)
              .as("Return 0 when bot has 3 manilhas.")
              .isZero();
    }

    @Test
    @DisplayName("Should accept truco when bot has 1 manilha and three")
    void ShouldAcceptTrucoWhenBotHas1ManilhaAndThree() {
      results = List.of();
      botCards = List.of(TrucoCard.of(TWO, DIAMONDS), TrucoCard.of(ACE, SPADES), TrucoCard.of(THREE, HEARTS));
      vira = TrucoCard.of(ACE, HEARTS);
      openCards = List.of(vira);
      stepBuilder = GameIntel.StepBuilder.with()
              .gameInfo(results, openCards, vira, 1)
              .botInfo(botCards, 0)
              .opponentScore(0);

      int responseRaise = new MarrecoBot().getRaiseResponse(stepBuilder.build());
      assertThat(responseRaise)
              .as("Return 0 when bot has 1 manilha and three.")
              .isZero();
    }
    @Test
    @DisplayName("Should accept truco when bot has first won round and has three")
    void ShouldAcceptTrucoWhenBotHasFirstWonRoundAndHasTwo() {
      results = List.of(WON, LOST);
      botCards = List.of(TrucoCard.of(THREE, HEARTS));
      vira = TrucoCard.of(ACE, HEARTS);
      openCards = List.of(vira,
              TrucoCard.of(SIX, SPADES), TrucoCard.of(TWO, DIAMONDS),
              TrucoCard.of(SEVEN, SPADES), TrucoCard.of(KING, HEARTS)
      );
      stepBuilder = GameIntel.StepBuilder.with()
              .gameInfo(results, openCards, vira, 1)
              .botInfo(botCards, 1)
              .opponentScore(1);

      int responseRaise = new MarrecoBot().getRaiseResponse(stepBuilder.build());
      assertThat(responseRaise)
              .as("Return 0 when bot has won first round and has three.")
              .isZero();
    }

    @Test
    @DisplayName("Should accept truco when bot has first lost round and two manilhas")
    void ShouldAcceptTrucoWhenBotHasFirstLostRoundAndTwoManilhas() {
      results = List.of(LOST);
      botCards = List.of(TrucoCard.of(TWO, HEARTS), TrucoCard.of(TWO, CLUBS));
      vira = TrucoCard.of(ACE, HEARTS);
      openCards = List.of(vira,
              TrucoCard.of(TWO, SPADES), TrucoCard.of(SIX, DIAMONDS)
      );
      stepBuilder = GameIntel.StepBuilder.with()
              .gameInfo(results, openCards, vira, 1)
              .botInfo(botCards, 0)
              .opponentScore(1);

      int responseRaise = new MarrecoBot().getRaiseResponse(stepBuilder.build());
      assertThat(responseRaise)
              .as("Return 0 when bot has lost first round and has two manilhas.")
              .isZero();
    }

    @Test
    @DisplayName("Should accept truco when bot has first lost round has copas and three")
    void ShouldAcceptTrucoWhenBotHasFirstLostRoundHasCopasAndThree() {
      results = List.of(LOST);
      botCards = List.of(TrucoCard.of(TWO, HEARTS), TrucoCard.of(THREE, CLUBS));
      vira = TrucoCard.of(ACE, HEARTS);
      openCards = List.of(vira,
              TrucoCard.of(TWO, SPADES), TrucoCard.of(SIX, DIAMONDS)
      );
      stepBuilder = GameIntel.StepBuilder.with()
              .gameInfo(results, openCards, vira, 1)
              .botInfo(botCards, 0)
              .opponentScore(1);

      int responseRaise = new MarrecoBot().getRaiseResponse(stepBuilder.build());
      assertThat(responseRaise)
              .as("Return 0 when bot has lost first round and  has copas and three.")
              .isZero();
    }

    @Test
    @DisplayName("Should accept truco when bot has 3 three")
    void shouldAcceptTrucoWhenBotHas3Three() {
      results = List.of();
      botCards = List.of(TrucoCard.of(THREE, SPADES), TrucoCard.of(THREE, CLUBS), TrucoCard.of(THREE, HEARTS));
      vira = TrucoCard.of(ACE, HEARTS);
      openCards = List.of(vira);
      stepBuilder = GameIntel.StepBuilder.with()
              .gameInfo(results, openCards, vira, 1)
              .botInfo(botCards, 0)
              .opponentScore(0);

      int responseRaise = new MarrecoBot().getRaiseResponse(stepBuilder.build());
      assertThat(responseRaise)
              .as("Return 0 when bot has 3 three.")
              .isZero();
    }

    @Test
    @DisplayName("Should accept truco when bot has 1 manilha and 1 two if score opponent has is smaller 8")
    void ShouldAcceptTrucoWhenBotHas1ManilhaEnd1TwoIfScoreOpponentHasIsSmaller8() {
      results = List.of();
      botCards = List.of(TrucoCard.of(FIVE, SPADES), TrucoCard.of(TWO, CLUBS), TrucoCard.of(ACE, HEARTS));
      vira = TrucoCard.of(FOUR, HEARTS);
      openCards = List.of(vira);
      stepBuilder = GameIntel.StepBuilder.with()
              .gameInfo(results, openCards, vira, 1)
              .botInfo(botCards, 0)
              .opponentScore(7);

      int responseRaise = new MarrecoBot().getRaiseResponse(stepBuilder.build());
      assertThat(responseRaise)
              .as("Return 0 when bot has 1 manilha, 1 two and if score opponent has is smaller 4.")
              .isZero();
    }
  }

  @Test
  @DisplayName("Should return 1 to raise when bot win first round and has zap")
    void ShouldReturn1ToRaiseWhenBotWinFirstRoundAndHasZap() {
      results = List.of(WON);
      botCards = List.of(TrucoCard.of(FIVE, CLUBS), TrucoCard.of(ACE, HEARTS));
      vira = TrucoCard.of(FOUR, HEARTS);
      openCards = List.of(vira, TrucoCard.of(TWO, CLUBS), TrucoCard.of(THREE, SPADES));
      stepBuilder = GameIntel.StepBuilder.with()
              .gameInfo(results, openCards, vira, 3)
              .botInfo(botCards, 0)
              .opponentScore(0);

      Boolean responseRaise = new MarrecoBot().decideIfRaises(stepBuilder.build());
      assertThat(responseRaise)
              .as("Return TRUE to raise when bot win first round and has zap.")
              .isTrue();
    }

  @Test
  @DisplayName("Should return 1 to raise when bot win first round and has copas")
  void ShouldReturn1ToRaiseWhenBotWinFirstRoundAndHasCopas() {
    results = List.of(WON);
    botCards = List.of(TrucoCard.of(FIVE, HEARTS), TrucoCard.of(ACE, HEARTS));
    vira = TrucoCard.of(FOUR, HEARTS);
    openCards = List.of(vira, TrucoCard.of(TWO, CLUBS), TrucoCard.of(THREE, SPADES));
    stepBuilder = GameIntel.StepBuilder.with()
            .gameInfo(results, openCards, vira, 3)
            .botInfo(botCards, 0)
            .opponentScore(0);

    Boolean responseRaise = new MarrecoBot().decideIfRaises(stepBuilder.build());
    assertThat(responseRaise)
            .as("Return TRUE to raise when bot win first round and has copas.")
            .isTrue();
  }

  @Test
  @DisplayName("Should return TRUE to raise when bot has 1 manilha and opponent score are less than 5")
  void ShouldReturnTRUEToRaiseWhenBotHas1ManilhaAndOpponentScoreAreLessThan5() {
    results = List.of();
    botCards = List.of(TrucoCard.of(FIVE, HEARTS), TrucoCard.of(THREE, SPADES), TrucoCard.of(ACE, HEARTS));
    vira = TrucoCard.of(FOUR, HEARTS);
    openCards = List.of(vira);
    stepBuilder = GameIntel.StepBuilder.with()
            .gameInfo(results, openCards, vira, 3)
            .botInfo(botCards, 0)
            .opponentScore(4);

    Boolean responseRaise = new MarrecoBot().decideIfRaises(stepBuilder.build());
    assertThat(responseRaise)
            .as("Return TRUE to raise when bot has 1 manilha and opponent score are less than 5.")
            .isTrue();
  }

  @Test
  @DisplayName("Should return TRUE to raise when bot has 2 manilhas and opponent score are less than 6")
  void ShouldReturnTRUEToRaiseWhenBotHas2ManilhasAndOpponentScoreAreLessThan6() {
    results = List.of();
    botCards = List.of(TrucoCard.of(FIVE, HEARTS), TrucoCard.of(THREE, SPADES), TrucoCard.of(FIVE, CLUBS));
    vira = TrucoCard.of(FOUR, HEARTS);
    openCards = List.of(vira);
    stepBuilder = GameIntel.StepBuilder.with()
            .gameInfo(results, openCards, vira, 3)
            .botInfo(botCards, 0)
            .opponentScore(5);

    Boolean responseRaise = new MarrecoBot().decideIfRaises(stepBuilder.build());
    assertThat(responseRaise)
            .as("Return TRUE to raise when bot has 2 manilha and opponent score are less than 6.")
            .isTrue();
  }

  @Test
  @DisplayName("Should return TRUE to raise when bot win first round and has two manilhas")
  void ShouldReturnTRUEToRaiseWhenBotWinFirstRoundAndHasTwoManilhas() {
    results = List.of(WON);
    botCards = List.of(TrucoCard.of(FIVE, CLUBS), TrucoCard.of(FIVE, DIAMONDS));
    vira = TrucoCard.of(FOUR, HEARTS);
    openCards = List.of(vira, TrucoCard.of(TWO, CLUBS), TrucoCard.of(THREE, SPADES));
    stepBuilder = GameIntel.StepBuilder.with()
            .gameInfo(results, openCards, vira, 3)
            .botInfo(botCards, 0)
            .opponentScore(0);

    Boolean responseRaise = new MarrecoBot().decideIfRaises(stepBuilder.build());
    assertThat(responseRaise)
            .as("Return TRUE to raise when bot win first round and has two manilhas.")
            .isTrue();
  }

  @Test
  @DisplayName("Should return TRUE to raise when bot lost first round and has HEARTS and 1 three")
  void ShouldReturnTRUEToRaiseWhenBotLostFirstRoundAndHasHEARTSAnd1Three() {
    results = List.of(LOST);
    botCards = List.of(TrucoCard.of(THREE, SPADES), TrucoCard.of(FIVE, HEARTS));
    vira = TrucoCard.of(FOUR, HEARTS);
    openCards = List.of(vira, TrucoCard.of(FIVE, CLUBS), TrucoCard.of(KING, CLUBS));
    stepBuilder = GameIntel.StepBuilder.with()
            .gameInfo(results, openCards, vira, 3)
            .botInfo(botCards, 0)
            .opponentScore(0);

    Boolean responseRaise = new MarrecoBot().decideIfRaises(stepBuilder.build());
    assertThat(responseRaise)
            .as("Return TRUE to raise when bot lost first round, has HEARTS and 1 three.")
            .isTrue();
  }

  @Test
  @DisplayName("Should return TRUE to raise when bot lost first round and has espadilha and 1 three")
  void ShouldReturnTRUEToRaiseWhenBotLostFirstRoundAndHasEspadilhaAnd1Three() {
    results = List.of(LOST);
    botCards = List.of(TrucoCard.of(THREE, SPADES), TrucoCard.of(FIVE, SPADES));
    vira = TrucoCard.of(FOUR, HEARTS);
    openCards = List.of(vira, TrucoCard.of(FIVE, CLUBS), TrucoCard.of(KING, CLUBS));
    stepBuilder = GameIntel.StepBuilder.with()
            .gameInfo(results, openCards, vira, 3)
            .botInfo(botCards, 0)
            .opponentScore(0);

    Boolean responseRaise = new MarrecoBot().decideIfRaises(stepBuilder.build());
    assertThat(responseRaise)
            .as("Return TRUE to raise when bot lost first round, has SPADEs and 1 three.")
            .isTrue();
  }

  @Nested
  @DisplayName("Test bot logic to accept mao de onze")
  class AcceptMaoDeOnze {
    @Test
    @DisplayName("Should accept mao de onze when bot has 3 manilhas")
    void shouldAcceptMaoDeOnzeWhenBotHas3Manilhas() {
      results = List.of();
      botCards = List.of(TrucoCard.of(TWO, DIAMONDS), TrucoCard.of(TWO, SPADES), TrucoCard.of(TWO, HEARTS));
      vira = TrucoCard.of(ACE, HEARTS);
      openCards = List.of(vira);
      stepBuilder = GameIntel.StepBuilder.with()
          .gameInfo(results, openCards, vira, 1)
          .botInfo(botCards, 11)
          .opponentScore(0);

      Boolean acceptMaoDeOnze = new MarrecoBot().getMaoDeOnzeResponse(stepBuilder.build());
      assertThat(acceptMaoDeOnze)
          .as("Return TRUE when bot has 3 manilhas.")
          .isTrue();
    }

    @Test
    @DisplayName("Should accept mao de onze when bot has 2 manilhas")
    void shouldAcceptMaoDeOnzeWhenBotHas2Manilhas() {
      results = List.of();
      botCards = List.of(TrucoCard.of(TWO, DIAMONDS), TrucoCard.of(FIVE, SPADES), TrucoCard.of(TWO, HEARTS));
      vira = TrucoCard.of(ACE, HEARTS);
      openCards = List.of(vira);
      stepBuilder = GameIntel.StepBuilder.with()
          .gameInfo(results, openCards, vira, 1)
          .botInfo(botCards, 11)
          .opponentScore(0);

      Boolean acceptMaoDeOnze = new MarrecoBot().getMaoDeOnzeResponse(stepBuilder.build());
      assertThat(acceptMaoDeOnze)
          .as("Return TRUE when bot has 2 manilhas.")
          .isTrue();
    }

    @Test
    @DisplayName("Should accept mao de onze when bot has 1 manilha and 1 card 3")
    void shouldAcceptMaoDeOnzeWhenBotHas1ManilhaAnd1RankCard3() {
      results = List.of();
      botCards = List.of(TrucoCard.of(THREE, DIAMONDS), TrucoCard.of(FIVE, SPADES), TrucoCard.of(TWO, SPADES));
      vira = TrucoCard.of(ACE, HEARTS);
      openCards = List.of(vira);
      stepBuilder = GameIntel.StepBuilder.with()
          .gameInfo(results, openCards, vira, 1)
          .botInfo(botCards, 11)
          .opponentScore(0);

      Boolean acceptMaoDeOnze = new MarrecoBot().getMaoDeOnzeResponse(stepBuilder.build());
      assertThat(acceptMaoDeOnze)
          .as("Return TRUE when bot has 1 manilha and 1 card 3")
          .isTrue();
    }

    @Test
    @DisplayName("Should accept mao de onze when bot has 1 manilha, 1 two and if score opponent has is smaller 4")
    void ShouldAcceptMaoDeOnzeWhenBotHas1Manilha1TwoAndIfScoreOpponentHasIsSmaller4() {
      results = List.of();
      botCards = List.of(TrucoCard.of(FOUR, CLUBS), TrucoCard.of(FIVE, SPADES), TrucoCard.of(TWO, SPADES));
      vira = TrucoCard.of(THREE, HEARTS);
      openCards = List.of(vira);
      stepBuilder = GameIntel.StepBuilder.with()
              .gameInfo(results, openCards, vira, 1)
              .botInfo(botCards, 11)
              .opponentScore(3);

      Boolean acceptMaoDeOnze = new MarrecoBot().getMaoDeOnzeResponse(stepBuilder.build());
      assertThat(acceptMaoDeOnze)
              .as("Return TRUE when bot has 1 manilha , 1 two and if score opponent has is smaller 4.")
              .isTrue();
    }
  }
}
