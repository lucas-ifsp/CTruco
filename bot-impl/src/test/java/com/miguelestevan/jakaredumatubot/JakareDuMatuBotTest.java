package com.miguelestevan.jakaredumatubot;

import com.cremonezzi.impl.carlsenbot.Carlsen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JakareDuMatuBotTest {

    private JakareDuMatuBot jakareDuMatuBot;

    @BeforeEach
    public void config() {
        jakareDuMatuBot = new JakareDuMatuBot();
    }

    @Test
    void getMaoDeOnzeResponse() {
    }

    @Test
    void decideIfRaises() {
    }

    @Test
    void chooseCard() {
    }

    @Test
    void getRaiseResponse() {
    }

    @Test
    @DisplayName("Should name of Bot jakareDuMatuBot")
    void getName() {
        assertEquals(jakareDuMatuBot.getName(), "JakaréDuMatuBóty");
    }
}