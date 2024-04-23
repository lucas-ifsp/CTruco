package com.fabio.bruno.minepowerbot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class MinePowerBotTest {

    private final MinePowerBot sut = new MinePowerBot();
    private GameIntel intel;
    private TrucoCard vira;
    private List<TrucoCard> cards;
    private Optional<TrucoCard> opponentCard;

    @Test
    @DisplayName("Should play the lowest card that is stronger than the opponent card")
    void shouldPlayTheLowestCardThatIsStrongerThanOpponentCard() {
        vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
        cards = List.of(TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS),
                TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS));
        opponentCard = Optional.of(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS));

        when(intel.getVira()).thenReturn(vira);
        when(intel.getCards()).thenReturn(cards);
        when(intel.getOpponentCard()).thenReturn(opponentCard);

        assertThat(sut.chooseCard(intel).content()).isEqualTo(TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS));
    }
}