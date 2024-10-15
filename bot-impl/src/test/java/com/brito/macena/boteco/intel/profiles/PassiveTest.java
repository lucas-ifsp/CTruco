package com.brito.macena.boteco.intel.profiles;

import com.brito.macena.boteco.interfaces.ProfileBot;
import com.brito.macena.boteco.utils.Status;
import com.bueno.spi.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class PassiveTest {

    @Nested
    @DisplayName("First Round")
    class FirstRoundTests {

        @Test
        @DisplayName("should kill the opponent's card if you have a medium hand")
        void shouldPlayStrongestCardIfItWins() {
            List<TrucoCard> botHand = List.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.JACK, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
            );
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS);
            TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(botHand, 0)
                    .opponentScore(0)
                    .opponentCard(opponentCard)
                    .build();

            ProfileBot passive = new Passive(intel, Status.MEDIUM);

            CardToPlay selectedCard = passive.firstRoundChoose();
            assertThat(selectedCard).isEqualTo(CardToPlay.of(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)));
        }

        @Test
        @DisplayName("should must play the weakest card possible to tie or kill in the first round if you have a medium hand")
        void shouldPlayWeakestCardIfItLoses() {
            List<TrucoCard> botHand = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS)
            );
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS);
            TrucoCard opponentCard = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(botHand, 0)
                    .opponentScore(0)
                    .opponentCard(opponentCard)
                    .build();

            ProfileBot passive = new Passive(intel, Status.MEDIUM);

            CardToPlay selectedCard = passive.firstRoundChoose();
            assertThat(selectedCard).isEqualTo(CardToPlay.of(TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS)));
        }
    }

    @Nested
    @DisplayName("Second Round")
    class SecondRoundTests {

        @Test
        @DisplayName("should guarantee the second round if he loses the first round")
        void shouldPlayTrumpCardIfAvailable() {
            List<TrucoCard> botHand = List.of(
                    TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
            );
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), List.of(), vira, 1)
                    .botInfo(botHand, 0)
                    .opponentScore(0)
                    .opponentCard(opponentCard)
                    .build();

            ProfileBot passive = new Passive(intel, Status.GOOD);

            CardToPlay selectedCard = passive.secondRoundChoose();
            assertThat(selectedCard).isEqualTo(CardToPlay.of(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)));
        }

        @Test
        @DisplayName("should try to secure the second if in passive mode")
        void shouldPlayWeakestCardIfNoTrumpCard() {
            List<TrucoCard> botHand = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS)
            );
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS);

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), List.of(), vira, 1)
                    .botInfo(botHand, 0)
                    .opponentScore(0)
                    .build();

            ProfileBot passive = new Passive(intel, Status.MEDIUM);

            CardToPlay selectedCard = passive.secondRoundChoose();
            assertThat(selectedCard).isEqualTo(CardToPlay.of(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)));
        }
    }

    @Nested
    @DisplayName("Third Round")
    class ThirdRoundTests {

    }
}
