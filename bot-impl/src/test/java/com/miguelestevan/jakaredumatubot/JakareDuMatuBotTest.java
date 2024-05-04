package com.miguelestevan.jakaredumatubot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.TrucoCard;
import com.cremonezzi.impl.carlsenbot.Carlsen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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
        public void shouldAcceptMaoDeOnzeIfHaveCasalMaior(){
            TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);



        }




    }

    @Nested
    class decideIfRaises {
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