package com.brito.macena.boteco.factories;

import com.brito.macena.boteco.intel.profiles.Agressive;
import com.brito.macena.boteco.intel.profiles.Passive;
import com.brito.macena.boteco.interfaces.ProfileBot;
import com.brito.macena.boteco.utils.Status;
import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class InstanceFactoryTest {

    @Nested
    @DisplayName("ProfileBot Creation Tests")
    class CreateProfileBotTests {
        @Test
        @DisplayName("should create Passive bot when opponent score is high and score difference is less than -6")
        void shouldCreatePassiveBotWhenOpponentScoreIsHigh() {
            List<TrucoCard> botEcoHand = List.of(
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                    TrucoCard.of(CardRank.KING, CardSuit.CLUBS)
            );
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS);

            GameIntel step = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(botEcoHand, 0)
                    .opponentScore(9)
                    .build();

            ProfileBot bot = InstanceFactory.createProfileBot(step, Status.MEDIUM);

            assertThat(bot).isInstanceOf(Passive.class);
        }

        @Test
        @DisplayName("should create Aggressive bot when score difference is not less than -6")
        void shouldCreateAggressiveBotWhenScoreDifferenceIsNotLessThanNegativeSix() {
            List<TrucoCard> botEcoHand = List.of(
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                    TrucoCard.of(CardRank.KING, CardSuit.CLUBS)
            );
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS);

            GameIntel step = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(botEcoHand, 0)
                    .opponentScore(5)
                    .build();

            ProfileBot bot = InstanceFactory.createProfileBot(step, Status.MEDIUM);

            assertThat(bot).isInstanceOf(Agressive.class);
        }
    }
}
