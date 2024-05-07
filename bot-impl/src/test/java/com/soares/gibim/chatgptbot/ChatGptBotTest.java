package com.soares.gibim.chatgptbot;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChatGptBotTest {

    private ChatGptBot sut;

    GameIntel.StepBuilder intel;

    @BeforeEach
    void setUp(){sut = new ChatGptBot(); }

    @Nested
    @DisplayName("Testing chooseCard")
    class chooseCardTest {
        @Nested
        @DisplayName("When is the first Round")
        class FirstRound {

            @Nested
            @DisplayName("When bot is the first to play")
            class FirstToPlay {

                @Test
                @DisplayName("If only have bad cards then discard the one with lower value")
                void IfOnlyHaveBadCardsThenDiscardTheOneWithLowerValue() {
                    TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

                    List<TrucoCard> botCards = Arrays.asList(
                            TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
                    );

                    List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(
                            CardRank.ACE, CardSuit.DIAMONDS)
                    );
                    intel = GameIntel.StepBuilder.with()
                            .gameInfo(List.of(), openCards, vira, 1)
                            .botInfo(botCards, 0)
                            .opponentScore(0);

                    assertThat(sut.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(2)));
                }

                @Test
                @DisplayName("If only have middle cards then use the one with highest value")
                void IfOnlyHaveMiddleCardsThenUseTheOneWithHighestValue() {
                    TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

                    List<TrucoCard> botCards = Arrays.asList(
                            TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.JACK, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)
                    );

                    List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(
                            CardRank.ACE, CardSuit.DIAMONDS)
                    );
                    intel = GameIntel.StepBuilder.with()
                            .gameInfo(List.of(), openCards, vira, 1)
                            .botInfo(botCards, 0)
                            .opponentScore(0);

                    assertThat(sut.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(2)));
                }
            }
        }
    }

    @Test
    @DisplayName("If its the last hand and have zap then ask truco")
    void IfItsTheLastRoundAndHaveZapThenAskTruco() {
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Collections.singletonList(
                TrucoCard.of(CardRank.SIX, CardSuit.CLUBS)
        );

        List<TrucoCard> openCards = Collections.singletonList(
                TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS)
        );

        intel = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON, GameIntel.RoundResult.LOST), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0);

        assertTrue(sut.decideIfRaises(intel.build()));
    }

    @Test
    @DisplayName("If we do the first round we order truco in the second")
    void testIfWeDoTheFirstRoundAskForTricksInTheSecond() {
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);

        List<TrucoCard> openCards = Collections.singletonList(
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
        );

        List<TrucoCard> botCards = Collections.singletonList(
                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
        );

        intel = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0);

        assertTrue(sut.decideIfRaises(intel.build()));

    }

}

