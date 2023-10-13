package com.newton.dolensi.sabotabot;


import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SabotaBotTest {
    @Mock
    GameIntel intel;
    @InjectMocks
    SabotaBot sut;

    @Nested
    @DisplayName("Choose Card")
    class ChooseCardTests{

        @Test
        @DisplayName("Should play any card from hand")
        void shouldPlayAnyCardFromHand(){
            var cards = CardsMock.cardList();
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getCards()).thenReturn(cards);
            assertNotNull(sut.chooseCard(intel).content());
        }

        @Test
        @DisplayName("Should play a strong card if is first to play")
        void shouldPlayAStrongCardIfIsFirstToPlay(){
            var cards = CardsMock.cardList();
            when(intel.getOpponentCard()).thenReturn(Optional.empty());
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getCards()).thenReturn(cards);
            assertEquals(sut.chooseCard(intel).content(), cards.get(2));
        }

        @Test
        @DisplayName("Should play the weakest card if other player plays a 3")
        void shouldPlayTheWeakestCardIfOtherPlayerPlaysA3(){
            var cards = CardsMock.cardList();
            when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)));
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getCards()).thenReturn(cards);
            assertEquals(sut.chooseCard(intel).content(), cards.get(1));
        }

    }
}
