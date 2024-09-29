package com.adivic.octopus;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class OctopusTest {
    private Octopus octopus;

    private GameIntel.StepBuilder stepBuilder;

    @BeforeEach
    public void setUp(){
        octopus = new Octopus();
    }
    @Nested
    @DisplayName("Testing the functions to identify manilhas")
    class ManilhaCardsTest{
        @Test
        @DisplayName("Return if the hand contains manilhas")
        void returnIfTheHandContainsManilha(){
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS);
            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.KING, CardSuit.SPADES)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(vira), vira, 1)
                    .botInfo(ourCards, 0)
                    .opponentScore(0);

            assertThat(octopus.hasManilha(stepBuilder.build())).isFalse();
        }

        @Test
        @DisplayName("Return the number of manilhas in the hand")
        void returnTheNumberOfManilhasInTheHand(){
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SIX, CardSuit.SPADES));

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(vira), vira, 1)
                    .botInfo(ourCards, 4)
                    .opponentScore(7);

            assertThat(octopus.numberOfManilhas(stepBuilder.build())).isEqualTo(2);
        }

        @Test
        @DisplayName("Return which manilhas have in hand")
        void returnWhichManilhasHaveInHand(){
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SIX, CardSuit.SPADES));

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(vira), vira, 1)
                    .botInfo(ourCards, 4)
                    .opponentScore(4);


            List <TrucoCard> expectedManilhas = List.of(
                    TrucoCard.of(vira.getRank().next(), CardSuit.DIAMONDS),
                    TrucoCard.of(vira.getRank().next(), CardSuit.SPADES),
                    TrucoCard.of(vira.getRank().next(), CardSuit.HEARTS),
                    TrucoCard.of(vira.getRank().next(), CardSuit.CLUBS));

            assertThat(octopus.listOfManilhas(stepBuilder.build())).containsAnyElementsOf(expectedManilhas);
        }
    }
}
