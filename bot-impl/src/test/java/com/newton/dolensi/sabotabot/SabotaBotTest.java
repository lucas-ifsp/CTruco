package com.newton.dolensi.sabotabot;


import com.bueno.spi.model.GameIntel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SabotaBotTest {

    @Nested
    @DisplayName("Choose Card")
    class ChooseCardTests{
        @Mock
        GameIntel intel;
        @InjectMocks
        SabotaBot sut;

        @Test
        @DisplayName("Should play any card from hand")
        void shouldPlayAnyCardFromHand(){
            var cards = CardsMock.cardList();
            when(intel.getCards()).thenReturn(cards);
            assertEquals(sut.chooseCard(intel).content(), cards.get(0));
        }
    }
}
