package com.miguelestevan.jakaredumatubot;

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