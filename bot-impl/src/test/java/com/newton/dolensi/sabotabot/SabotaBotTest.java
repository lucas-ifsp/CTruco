package com.newton.dolensi.sabotabot;


import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
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
            var cards = IntelMock.cardList3Cards();
            when(intel.getCards()).thenReturn(cards);
            when(intel.getVira()).thenReturn(IntelMock.vira5C());

            assertNotNull(sut.chooseCard(intel).content());
        }

        @Nested
        @DisplayName("First Round Plays")
        class FirstRoundTests{

            @Test
            @DisplayName("Should play a strong card if is first to play")
            void shouldPlayAStrongCardIfIsFirstToPlay(){
                var cards = IntelMock.cardList3Cards();
                when(intel.getCards()).thenReturn(cards);
                when(intel.getVira()).thenReturn(IntelMock.vira5C());

                when(intel.getOpponentCard()).thenReturn(Optional.empty());
                assertEquals(cards.get(2), sut.chooseCard(intel).content());
            }

            @Test
            @DisplayName("Should play the weakest card if other player plays a 3")
            void shouldPlayTheWeakestCardIfOtherPlayerPlaysA3(){
                var cards = IntelMock.cardList3Cards();
                when(intel.getCards()).thenReturn(cards);
                when(intel.getVira()).thenReturn(IntelMock.vira5C());

                when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)));
                assertEquals(cards.get(1), sut.chooseCard(intel).content());
            }
        }

        @Nested
        @DisplayName("Second Round Plays")
        class SecondRoundTests{
            @Test
            @DisplayName("Should play a card in second round")
            void shouldPlayACardInSecondRound(){
                var cards = IntelMock.cardList2Cards();
                when(intel.getCards()).thenReturn(cards);
                when(intel.getVira()).thenReturn(IntelMock.vira5C());

                assertNotNull(sut.chooseCard(intel).content());
            }
        }

        @Nested
        @DisplayName("Third Round Plays")
        class ThirdRoundTests{

        }

    }
}
