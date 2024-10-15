package com.brito.macena.boteco.intel.profiles;

import com.brito.macena.boteco.interfaces.ProfileBot;
import com.brito.macena.boteco.utils.Status;
import com.bueno.spi.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AgressiveTest {

    @Test
    @DisplayName("Should play the weakest card if the hand is good")
    void shouldPlayWeakestCardIfHandIsGood() {
        List<TrucoCard> botHand = List.of(
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS),
                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
        );
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);
        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS);

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), List.of(), vira, 1)
                .botInfo(botHand, 0)
                .opponentScore(0)
                .opponentCard(opponentCard)
                .build();

        ProfileBot agressive = new Agressive(intel, Status.GOOD);

        CardToPlay selectedCard = agressive.firstRoundChoose();
        assertThat(selectedCard).isEqualTo(CardToPlay.of(TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS)));
    }
}
