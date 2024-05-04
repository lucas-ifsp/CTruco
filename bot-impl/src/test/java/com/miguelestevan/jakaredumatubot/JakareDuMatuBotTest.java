package com.miguelestevan.jakaredumatubot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.cremonezzi.impl.carlsenbot.Carlsen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JakareDuMatuBotTest {

    private JakareDuMatuBot jakareDuMatuBot;

    @BeforeEach
    public void config() {
        jakareDuMatuBot = new JakareDuMatuBot();
    }

    @Nested
    class getMaoDeOnzeResponse {
        @Test
        @DisplayName("Should accept mao de onze if have casal maior")
        public void ShouldAcceptMaoDeOnzeIfHaveCasalMaior() {
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);

            // Game info
            List<GameIntel.RoundResult> roundResults = List.of();
            List<TrucoCard> openCards = List.of(vira);

            // Bot info
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 11)
                    .opponentScore(2)
                    .build();

            assertThat(jakareDuMatuBot.getMaoDeOnzeResponse(intel)).isEqualTo(true);
        }
    }

    @Nested
    @DisplayName("Decide If Raises Request Point tests")
    class decideIfRaises {
        // First Hand
        @Test
        @DisplayName("Decide that you will ask for tricks in the first round with an older couple")
        public void DecideThatYouWillAskForTricksInTheFirstRoundWithAnOlderCouple(){
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);

            // Game info
            List<GameIntel.RoundResult> roundResults = List.of();
            List<TrucoCard> openCards = List.of(vira);

            // Bot info
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 5)
                    .opponentScore(2)
                    .build();

            assertThat(jakareDuMatuBot.decideIfRaises(intel)).isEqualTo(true);
        }

        @Test
        @DisplayName("Should raise request truco when you have two shackles") // Pedir truco quando tiver duas manilhas na mão na primeira rodada
        public void ShouldRaiseRequestTrucoWhenYouHaveTwoShackles(){
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);

            // Game info
            List<GameIntel.RoundResult> roundResults = List.of();
            List<TrucoCard> openCards = List.of(vira);

            // Bot info
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 5)
                    .opponentScore(2)
                    .build();

            assertThat(jakareDuMatuBot.decideIfRaises(intel)).isEqualTo(true);
        }

        // Second Hand
    }

    @Nested
    class chooseCard {

    }

    @Nested
    class getRaiseResponse {
    }

    @Nested
    @DisplayName("getName Tests")
    class getName {
        @Test
        @DisplayName("Should name of Bot jakareDuMatuBot")
        public void showNameOfJakareDuMatuBot(){
            assertEquals(jakareDuMatuBot.getName(), "JakaréDuMatuBóty");
        }

    }

}