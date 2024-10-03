package com.manhani.stefane.reimubot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ReimuBotTest {

    private ReimuBot reimuBot;
    
    @BeforeEach
    void setUp(){ reimuBot = new ReimuBot(); }
    
    @Nested
    @DisplayName("chooseCard tests")
    class ChooseCardTests {
        @Test
        @DisplayName("Should select weakest card if cannot defeat opponent")
        void selectWeakestIfLose() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
            List<TrucoCard> reimuCards = List.of(
                    TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS)
            );
            var step = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1).botInfo(reimuCards, 0)
                    .opponentScore(0).opponentCard(TrucoCard.of(CardRank.TWO, CardSuit.CLUBS))
                    .build();
            var selectedCard = reimuBot.chooseCard(step).content();
            assertThat(selectedCard).isEqualTo(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS));
        }

    }

    @Nested
    @DisplayName("decideIfRaises tests")
    class DecideIfRaisesTests {
        @Test
        @DisplayName("Should raise if on round 2 and has two manilhas")
        void raiseIfTwoManilhas() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
            List<TrucoCard> reimuCards = List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS)
            );
            var step = GameIntel.StepBuilder.with()
                    .gameInfo(
                            List.of(GameIntel.RoundResult.WON),
                            List.of(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)),
                            vira, 1).botInfo(reimuCards, 0)
                    .opponentScore(1).opponentCard(TrucoCard.of(CardRank.KING, CardSuit.CLUBS))
                    .build();
            assertThat(reimuBot.decideIfRaises(step)).isTrue();
        }
    }
    
    @Nested
    @DisplayName("getMaoDeOnzeResponse tests")
    class GetMaoDeOnzeResponseTests {
        @Test
        @DisplayName("Should refuse mão de onze if enemy has 9 points and cards are weak")
        void refuseIfCardsSuck() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
            List<TrucoCard> reimuCards = List.of(
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SIX, CardSuit.HEARTS)
            );
            var step = GameIntel.StepBuilder.with()
                    .gameInfo(
                            List.of(),
                            List.of(),
                            vira, 1).botInfo(reimuCards, 11)
                    .opponentScore(9)
                    .build();
            assertThat(reimuBot.getMaoDeOnzeResponse(step)).isFalse();
        }
    }
    


}