package com.joao.alexandre.jormungandrbot;

import com.bueno.spi.model.GameIntel;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class JormungandrBotTest {
    private JormungandrBot jormungandrBot;
    private GameIntel.StepBuilder stepBuilder;

    @BeforeEach
    public void config() {
        jormungandrBot = new JormungandrBot();

    }

}