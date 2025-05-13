package com.antonelli.gibim.degolabot;

import com.antonelli.gibim.degolabot.FirstRound;
import com.bueno.spi.model.GameIntel;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class DegolaBotTest {

    @Test
    public void testGetRaiseResponse() {
        GameIntel intel = mock(GameIntel.class);


        when(intel.getOpponentScore()).thenReturn(5);
        when(intel.getCards()).thenReturn(mock(List.class));

        FirstRound strategy = new FirstRound();
        int response = strategy.getRaiseResponse(intel);

        assertEquals(-1, response);
    }

    @Test
    public void testDecideIfRaises() {
        GameIntel intel = mock(GameIntel.class);

        when(intel.getOpponentCard()).thenReturn(Optional.empty());

        FirstRound strategy = new FirstRound();
        boolean result = strategy.decideIfRaises(intel);


        assertFalse(result);
    }
}

