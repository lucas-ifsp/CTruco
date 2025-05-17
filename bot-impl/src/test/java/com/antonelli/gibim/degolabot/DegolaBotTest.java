package com.antonelli.gibim.degolabot;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class DegolaBotTest {

    private DegolaBot sut;

    GameIntel.StepBuilder intel;

    @BeforeEach
    void setUp(){sut = new DegolaBot(); }
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

    @Test
    @DisplayName("If only have bad cards then discard the one with lower value")
    void ifOnlyHaveBadCardsThenDiscardTheOneWithLowerValue() {
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(
                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS)
        );

        List<TrucoCard> openCards = Collections.singletonList(vira);

        intel = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0);

        assertThat(sut.chooseCard(intel.build())).isEqualTo(CardToPlay.of(botCards.get(0)));
    }

    @Test
    public void testChooseCardDoesNotThrowException() {
        TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.SIX, CardSuit.SPADES),
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS)
        );
        List<TrucoCard> openCards = List.of(vira);

        intel = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(3);

        assertDoesNotThrow(() -> sut.chooseCard(intel.build()));
    }

}

