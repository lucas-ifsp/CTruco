package com.brito.macena.boteco.intel.profiles;

import com.brito.macena.boteco.interfaces.ProfileBot;
import com.brito.macena.boteco.utils.Status;
import com.bueno.spi.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AgressiveTest {

    @Nested
    @DisplayName("First Round")
    class FirstRoundTests {

        @Test
        @DisplayName("should kill the opponent's card if you have a medium hand")
        void shouldPlayStrongestCardIfItWins() {
            List<TrucoCard> botHand = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
            );
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);
            TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS);

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(botHand, 0)
                    .opponentScore(0)
                    .opponentCard(opponentCard)
                    .build();

            ProfileBot agressive = new Agressive(intel, Status.GOOD);

            CardToPlay selectedCard = agressive.firstRoundChoose();
            assertThat(selectedCard).isEqualTo(CardToPlay.of(TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS)));
        }

        @Test
        @DisplayName("Should must play the weakest card if you have two Manilhas")
        void shouldPlayWeakestCardIfHaveTwoManilhas() {
            List<TrucoCard> botHand = List.of(
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS)
            );
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS);
            TrucoCard opponentCard = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(botHand, 0)
                    .opponentScore(0)
                    .opponentCard(opponentCard)
                    .build();

            ProfileBot agressive = new Agressive(intel, Status.EXCELLENT);

            CardToPlay selectedCard = agressive.firstRoundChoose();
            assertThat(selectedCard).isEqualTo(CardToPlay.of(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)));
        }
    }

    @Nested
    @DisplayName("Second Round")
    class SecondRoundTests {

        @Test
        @DisplayName("Should play the weakest if you've won the first round and your hand is good")
        void shouldPlayWeakestCardIfWonFirstRoundAndHandIsGood() {
            List<TrucoCard> botHand = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
            );
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), List.of(), vira, 1)
                    .botInfo(botHand, 0)
                    .opponentScore(0)
                    .opponentCard(opponentCard)
                    .build();

            ProfileBot agressive = new Agressive(intel, Status.GOOD);

            CardToPlay selectedCard = agressive.secondRoundChoose();
            assertThat(selectedCard).isEqualTo(CardToPlay.of(TrucoCard.of(CardRank.KING, CardSuit.HEARTS)));
        }
    }
}
