package com.antonelli.rufino.bardoalexbot;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static com.bueno.spi.model.CardSuit.*;
import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

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

    @Test
    public void testChooseCard() {
        BarDoAlexBot bot = new BarDoAlexBot();
        GameIntel intel = new GameIntel();
        intel.setVira(new TrucoCard(1, ESPADA)); // Define a carta vira
        intel.setCards(Arrays.asList(
                new TrucoCard(7, COPAS),
                new TrucoCard(7, ESPADA),
                new TrucoCard(3, ESPADA),
                new TrucoCard(4, ESPADA)
        ));
        assertThat(bot.chooseCard(intel)).isEqualTo(new TrucoCard(4, ESPADA));
    }

    @Test
    public void testGetRaiseResponse() {
        BarDoAlexBot bot = new BarDoAlexBot();
        GameIntel intel = new GameIntel();
        intel.setBotScore(11); // Bot atinge 11 pontos
        assertEquals(-1, bot.getRaiseResponse(intel));

        intel.setBotScore(0);
        intel.setOpponentScore(11);
        intel.setCards(Arrays.asList(
                new TrucoCard(7, COPAS),
                new TrucoCard(7, ESPADA),
                new TrucoCard(3, ESPADA),
                new TrucoCard(4, ESPADA)
        ));
        assertEquals(1, bot.getRaiseResponse(intel));

        intel.setCards(Arrays.asList(
                new TrucoCard(4, ESPADA),
                new TrucoCard(5, ESPADA),
                new TrucoCard(6, ESPADA),
                new TrucoCard(7, ESPADA)
        ));
        assertEquals(1, bot.getRaiseResponse(intel));
    }
}
