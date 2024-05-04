package com.fabio.bruno.minepowerbot;

import com.bueno.spi.model.GameIntel;

import static org.mockito.Mockito.mock;

public class MinePowerBotIntelMockBuilder {
    private final GameIntel intel;

    private MinePowerBotIntelMockBuilder(){
        intel = mock(GameIntel.class);
    }

    static MinePowerBotIntelMockBuilder create() { return new MinePowerBotIntelMockBuilder(); }

    
}
