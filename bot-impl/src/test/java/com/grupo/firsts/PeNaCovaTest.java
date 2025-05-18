package com.grupo.firsts;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.model.CardToPlay;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
      "THREE, HEARTS, FOUR, SPADES, TWO, DIAMONDS, false",
      "KING, HEARTS, FIVE, SPADES, SIX, DIAMONDS, false",
      "JACK, HEARTS, QUEEN, SPADES, KING, DIAMONDS, true",
      "JACK, HEARTS, FIVE, SPADES, SIX, DIAMONDS, false",
      "JACK, HEARTS, ACE, SPADES, TWO, DIAMONDS, true",
      "QUEEN, HEARTS, QUEEN, SPADES, FOUR, DIAMONDS, true",
      "SIX, HEARTS, SEVEN, SPADES, ACE, DIAMONDS, false"


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
            5
        )
        .opponentScore(3)
        .build();//Opponent score


    boolean result = bot.getMaoDeOnzeResponse(intel);

    assertThat(result).isEqualTo(expected);
  }

  static Stream<Arguments> provideScenarios(){
    return Stream.of(
        Arguments.of(
            List.of(
              parseCard("FOUR","HEARTS"),
              parseCard("KING","SPADES"),
              parseCard("JACK","DIAMONDS")
            ),
            parseCard("FIVE", "CLUBS"),
            List.of(parseCard("KING", "SPADES")),
            parseCard("FOUR", "HEARTS")
        ),
        Arguments.of(
            List.of(
                parseCard("TWO","HEARTS"),
                parseCard("THREE","SPADES"),
                parseCard("FOUR","DIAMONDS")
            ),
            parseCard("ACE", "CLUBS"),
            List.of(parseCard("THREE","SPADES")),
            parseCard("TWO", "HEARTS")
        ),
        Arguments.of(
            List.of(
                parseCard("FIVE","HEARTS"),
                parseCard("SEVEN","SPADES"),
                parseCard("SIX","DIAMONDS")
            ),
            parseCard("FOUR", "SPADES"),
            List.of(parseCard("SEVEN","HEARTS")),
            parseCard("FIVE", "HEARTS")
        ),
        Arguments.of(
            List.of(
                parseCard("ACE","SPADES"),
                parseCard("KING","DIAMONDS"),
                parseCard("THREE","HEARTS")
            ),
            parseCard("QUEEN", "HEARTS"),
            List.of(),
            parseCard("THREE", "HEARTS")
        ),
        Arguments.of(
            List.of(
                parseCard("FOUR","SPADES"),
                parseCard("FIVE","CLUBS"),
                parseCard("SIX","DIAMONDS")
            ),
            parseCard("SEVEN", "HEARTS"),
            List.of(parseCard("FOUR","CLUBS")),
            parseCard("FIVE", "CLUBS")
        ),
        Arguments.of(
            List.of(
                parseCard("SIX","CLUBS"),
                parseCard("SIX","HEARTS"),
                parseCard("SEVEN","DIAMONDS")
            ),
            parseCard("FIVE", "DIAMONDS"),
            List.of(parseCard("ACE","SPADES")),
            parseCard("SIX", "HEARTS")
        ),
        Arguments.of(
            List.of(
                parseCard("SIX","CLUBS"),
                parseCard("SIX","HEARTS"),
                parseCard("SIX","SPADES")
            ),
            parseCard("FIVE", "DIAMONDS"),
            List.of(parseCard("SIX","HEARTS")),
            parseCard("SIX", "CLUBS")
        ),
        Arguments.of(
            List.of(
                parseCard("KING","CLUBS"),
                parseCard("KING","DIAMONDS"),
                parseCard("KING","HEARTS")
            ),
            parseCard("QUEEN", "SPADES"),
            List.of(parseCard("KING","HEARTS")),
            parseCard("KING", "CLUBS")
        ),
        Arguments.of(
            List.of(
                parseCard("THREE","HEARTS"),
                parseCard("QUEEN","DIAMONDS"),
                parseCard("KING","SPADES")
            ),
            parseCard("TWO", "CLUBS"),
            List.of(parseCard("THREE","CLUBS")),
            parseCard("QUEEN", "DIAMONDS")
        ),
        Arguments.of(
            List.of(
                parseCard("KING","CLUBS"),
                parseCard("KING","DIAMONDS"),
                parseCard("KING","HEARTS")
            ),
            parseCard("QUEEN", "SPADES"),
            List.of(parseCard("KING","HEARTS")),
            parseCard("KING", "CLUBS")
        ),
        Arguments.of(
            List.of(
                parseCard("THREE","HEARTS"),
                parseCard("QUEEN","DIAMONDS"),
                parseCard("KING","SPADES")
            ),
            parseCard("TWO", "CLUBS"),
            List.of(parseCard("THREE","CLUBS")),
            parseCard("QUEEN", "DIAMONDS")
        ),
        Arguments.of(
            List.of(
                parseCard("FIVE","SPADES"),
                parseCard("THREE","CLUBS"),
                parseCard("KING","DIAMONDS")
            ),
            parseCard("FOUR", "HEARTS"),
            List.of(),
            parseCard("FIVE", "SPADES")
        ),
        Arguments.of(
            List.of(
                parseCard("FOUR","DIAMONDS"),
                parseCard("FIVE","HEARTS"),
                parseCard("SIX","SPADES")
            ),
            parseCard("SEVEN", "CLUBS"),
            List.of(parseCard("JACK", "HEARTS")),
            parseCard("FOUR", "DIAMONDS")
        )
        );

  }

  @ParameterizedTest
  @MethodSource("provideScenarios")
  void chooseCard_scenarios(
      List<TrucoCard> hand,
      TrucoCard vira,
      List<TrucoCard> mesaRestante,
      TrucoCard expectedCard
  ) {
    List<TrucoCard> openCards = new ArrayList<>();
    openCards.add(vira);
    openCards.addAll(mesaRestante);

    List<GameIntel.RoundResult> roundResults = Collections.emptyList();

    GameIntel intel = GameIntel.StepBuilder.with()
        .gameInfo(roundResults, openCards, vira,1)
        .botInfo(hand,0)
        .opponentScore(0)
        .build();

    CardToPlay result = bot.chooseCard(intel);

    assertEquals(expectedCard, result.content());
  }

  static TrucoCard parseCard(String rank, String suit){
    return TrucoCard.of(CardRank.valueOf(rank), CardSuit.valueOf(suit));
  }

  public static Stream<Arguments> providRaiseScenarios() {
    return Stream.of(
        Arguments.of(
            List.of(
                TrucoCard.of(CardRank.SIX, CardSuit.CLUBS),
                TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)
            ),
            TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
            true
        ),
        Arguments.of(
            List.of(
                TrucoCard.of(CardRank.SIX, CardSuit.SPADES),
                TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS)
            ),
            TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
            true
        )
    );
  }

  @ParameterizedTest
  @MethodSource("providRaiseScenarios")
  void testDecideIfRaises(List<TrucoCard> hand, TrucoCard vira, boolean expected){
    GameIntel intel = GameIntel.StepBuilder.with()
        .gameInfo(List.of(), List.of(), vira,1)
        .botInfo(hand,0)
        .opponentScore(0)
        .build();

    assertEquals(expected, bot.decideIfRaises(intel));

  }


}