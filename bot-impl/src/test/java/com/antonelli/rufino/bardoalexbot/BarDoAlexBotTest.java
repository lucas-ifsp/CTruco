package com.antonelli.rufino.bardoalexbot;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static com.bueno.spi.model.CardSuit.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BarDoAlexBotTest {

    @Test
    public void testDecideIfRaises() {
        BarDoAlexBot bot = new BarDoAlexBot();
        GameIntel intel = new GameIntel();
        intel.setVira(new TrucoCard(1, ESPADA)); // Define a carta vira
        intel.setCards(Arrays.asList(
                new TrucoCard(7, COPAS),
                new TrucoCard(7, ESPADA),
                new TrucoCard(3, ESPADA),
                new TrucoCard(4, ESPADA)
        ));
        assertTrue(bot.decideIfRaises(intel));
    }
}
