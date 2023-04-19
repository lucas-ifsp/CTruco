package com.hermespiassi.casados.marrecobot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;


class MarrecoBotTest {
    private GameIntel.StepBuilder stepBuilder;
    private List<GameIntel.RoundResult> results;
    private List<TrucoCard> openCards;
    private List<TrucoCard> botCards;
    private TrucoCard vira;

    @Test
    @DisplayName("Should return pica-fumo in first raise if bot has a pica-fumo")
    void shouldReturnPicaFumoInFirstRaiseIfBotHasAPicaFumo() {
        results = List.of();
        botCards = List.of(TrucoCard.of(TWO, HEARTS), TrucoCard.of(FIVE, CLUBS), TrucoCard.of(TWO, DIAMONDS));
        vira = TrucoCard.of(ACE, HEARTS);
        openCards = List.of(vira);
        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(results, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0);

        CardToPlay cardToPlay = new MarrecoBot().chooseCard(stepBuilder.build());
        assertThat(cardToPlay.value().getSuit()).isEqualTo(DIAMONDS);
    }
}
