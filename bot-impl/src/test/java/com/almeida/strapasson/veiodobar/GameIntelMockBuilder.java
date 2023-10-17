package com.almeida.strapasson.veiodobar;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class GameIntelMockBuilder {
    private final GameIntel intel;

    private GameIntelMockBuilder() {
        intel = mock(GameIntel.class);
    }

    static GameIntelMockBuilder make() { return new GameIntelMockBuilder(); }

    GameIntelMockBuilder cardsToBe(TrucoCard... cards) {
        when(intel.getCards()).thenReturn(Arrays.stream(cards).toList());
        return this;
    }

    GameIntelMockBuilder cardsToBeAceTwoAndThreeOfSuit(CardSuit suit) {
        return cardsToBe(
                TrucoCard.of(CardRank.ACE, suit),
                TrucoCard.of(CardRank.TWO, suit),
                TrucoCard.of(CardRank.THREE, suit)
        );
    }

    GameIntelMockBuilder cardsToBeThreeOf(CardRank rank) {
        return cardsToBe(
                TrucoCard.of(rank, CardSuit.SPADES),
                TrucoCard.of(rank, CardSuit.HEARTS),
                TrucoCard.of(rank, CardSuit.CLUBS)
        );
    }

    GameIntelMockBuilder viraToBeDiamondsOfRank(CardRank viraRank) {
        when(intel.getVira()).thenReturn(TrucoCard.of(viraRank, CardSuit.DIAMONDS));
        return this;
    }

    GameIntelMockBuilder opponentCardToBe(TrucoCard card) {
        when(intel.getOpponentCard()).thenReturn(Optional.of(card));
        return this;
    }

    GameIntelMockBuilder botToBeFirstToPlay() {
        when(intel.getOpponentCard()).thenReturn(Optional.empty());
        return this;
    }

    GameIntelMockBuilder botToWinTheFirstRound() {
        when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON));
        return this;
    }

    GameIntelMockBuilder botToLoseTheFirstRound() {
        when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.LOST));
        return this;
    }

    GameIntelMockBuilder roundResultToBe(GameIntel.RoundResult... results) {
        when(intel.getRoundResults()).thenReturn(Arrays.stream(results).toList());
        return this;
    }

    GameIntel finish() { return intel; }
}
