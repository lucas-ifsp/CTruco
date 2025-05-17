package com.aguiar.vitor.trucorinthians;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TrucorinthiansTest {

    private Trucorinthians sut;
    private GameIntel intel;

    @BeforeEach
    void setUp() {
        sut = new Trucorinthians();
    }

    @DisplayName("Shouldn't return null when choosing card")
    @Test
    public void shouldNotReturnNullWhenChoosingCard() {
        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), List.of(), TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS), 1)
                .botInfo(List.of(TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS)), 0)
                .opponentScore(0)
                .build();

        CardToPlay result = sut.chooseCard(intel);

        assertThat(result).isNotNull();
        assertThat(result.content()).isNotNull();
    }
}
