package com.grupo.firsts;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PeNaCovaTest {

  private PeNaCova bot;

  @BeforeEach
  public void setUp(){
    bot = new PeNaCova();
  }

  @ParameterizedTest
  @CsvSource({
      "JACK, HEARTS, QUEEN, SPADES, FIVE, DIAMONDS, true",
      "TWO, HEARTS, FIVE, SPADES, SIX, DIAMONDS, false",
      "KING, HEARTS, ACE, SPADES, THREE, DIAMONDS,true",
      "SEVEN, HEARTS, SIX, SPADES, JACK, DIAMONDS, false",
      "QUEEN, HEARTS, KING, SPADES, JACK, DIAMONDS, true",
      "THREE, HEARTS, FOUR, SPADES, TWO, DIAMONDS, false"

  })
  @DisplayName("Should determine if the bot accepts Mão de Onze")
  void shouldAcceptMaoDeOnze(
      CardRank rank1, CardSuit suit1,
      CardRank rank2, CardSuit suit2,
      CardRank rank3, CardSuit suit3,
      boolean expected
  ){

    GameIntel intel = GameIntel.StepBuilder.with()
        .gameInfo(
            List.of(),
            List.of(),
            TrucoCard.of(CardRank.THREE,CardSuit.HEARTS),
            0
        )
        .botInfo(
            List.of(
                TrucoCard.of(rank1,suit1),
                TrucoCard.of(rank2,suit2),
                TrucoCard.of(rank3,suit3)
            ),
            10
        )
        .opponentScore(3)
        .build();//Opponent score


    boolean result = bot.getMaoDeOnzeResponse(intel);

    assertThat(result).isEqualTo(expected);
  }


}