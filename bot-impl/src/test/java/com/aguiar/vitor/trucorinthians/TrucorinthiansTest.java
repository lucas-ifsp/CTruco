package com.aguiar.vitor.trucorinthians;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TrucorinthiansTest {

    private Trucorinthians sut;
    private GameIntel intel;

    @BeforeEach
    void setUp() {
        sut = new Trucorinthians();
    }

    @DisplayName("Shouldn't return null when choosing card")
    @Test
    void shouldNotReturnNullWhenChoosingCard() {
        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), List.of(), TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS), 1)
                .botInfo(List.of(TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS)), 0)
                .opponentScore(0)
                .build();

        CardToPlay result = sut.chooseCard(intel);

        assertThat(result).isNotNull();
        assertThat(result.content()).isNotNull();
    }

    @DisplayName("Should play weakest card when starting first round")
    @Test
    void shouldPlayWeakestCardWhenFirstRoundAsFirstPlayer() {
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);

        TrucoCard fraca = TrucoCard.of(CardRank.SIX, CardSuit.CLUBS);
        TrucoCard media = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);
        TrucoCard forte = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), List.of(), vira, 1)
                .botInfo(List.of(forte, fraca, media), 0)
                .opponentScore(0)
                .build();

        CardToPlay result = sut.chooseCard(intel);

        assertThat(result.content()).isEqualTo(fraca);
    }
}
