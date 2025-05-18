package com.fernandoAloneBot;

import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;


import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MeuBotTest {

    private MeuBot meuBot;

    @BeforeEach
    void setUp() {
        meuBot = new MeuBot();
    }
    private GameIntel createIntel(List<TrucoCard> hand, TrucoCard vira, int myScore, int opponentScore, List<GameIntel.RoundResult> roundResults) {
        return GameIntel.StepBuilder.with()
                .gameInfo(roundResults, List.of(vira), vira, 1)
                .botInfo(hand, myScore)
                .opponentScore(opponentScore)
                .build();
    }

    private TrucoCard getDefaultVira() {
        return TrucoCard.of(FIVE, DIAMONDS);
    }

    private List<TrucoCard> getDefaultHand() {
        return List.of(
                TrucoCard.of(FOUR, CLUBS),
                TrucoCard.of(FIVE, CLUBS),
                TrucoCard.of(SIX, CLUBS)
        );
    }


    @Nested
    @DisplayName("Mão de onze")
    class GetMaoDeOnzeResponse {

        @Test
        @DisplayName("Testa se recusa mão de onze sem manilhas")
        void testaSeRecusaMaoDeOnzeSemManilhas() {
            GameIntel intel = createIntel(getDefaultHand(), getDefaultVira(), 10, 10, List.of());
            assertFalse(meuBot.getMaoDeOnzeResponse(intel));
        }















    }
}
