package com.otavio.lopes.teitasbot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.TrucoCard;
import com.otavio.lopes.teitasbot.TeitasBotFunctions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TeitasBotFunctionsTest {

    @Nested
    @DisplayName("HasManilhaTest")
    class HasManilhaTest {
        @Test
        @DisplayName("Should return true if hand at least one manilha")
        void shouldReturnTrueIfHandHasManilha() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
            );
            assertThat(TeitasBotFunctions.hasManilha(cards, vira)).isTrue();
        }

        @Test
        @DisplayName("Should return false if hand has no manilha")
        void shouldReturnFalseIfHandHasNoManilha() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
            );
            assertThat(TeitasBotFunctions.hasManilha(cards, vira)).isFalse();
        }
    }
    

}