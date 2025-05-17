package com.aguiar.vitor.trucorinthians;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

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

    @DisplayName("Should play smallest non-losing card when responding in first round")
    @Test
    void shouldPlaySmallestCardThatDoesNotLoseWhenFirstRoundAsSecondPlayer() {
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

        TrucoCard opponentCard = TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS);

        TrucoCard perde = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);
        TrucoCard empata = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
        TrucoCard vence = TrucoCard.of(CardRank.SIX, CardSuit.SPADES);

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), List.of(), vira, 1)
                .botInfo(List.of(perde, empata, vence), 0)
                .opponentScore(0)
                .opponentCard(opponentCard)
                .build();

        CardToPlay result = sut.chooseCard(intel);

        assertThat(result.content()).isEqualTo(empata);
    }

    @DisplayName("Should play weakest card if cannot win or draw when responding in first round")
    @Test
    void shouldPlayWeakestCardIfCannotWinOrDrawWhenFirstRoundAsSecondPlayer() {
        TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS);
        TrucoCard opponentCard = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);

        TrucoCard fraca = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);
        TrucoCard media = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
        TrucoCard alta = TrucoCard.of(CardRank.SIX, CardSuit.SPADES);

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), List.of(), vira, 1)
                .botInfo(List.of(media, fraca, alta), 0)
                .opponentScore(0)
                .opponentCard(opponentCard)
                .build();

        CardToPlay result = sut.chooseCard(intel);

        assertThat(result.content()).isEqualTo(fraca);
    }

    @DisplayName("Should play strongest card in second round after losing first")
    @Test
    void shouldPlayStrongestCardWhenSecondRoundAfterLosingFirst() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.HEARTS);

        List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.LOST);

        TrucoCard fraca = TrucoCard.of(CardRank.TWO, CardSuit.SPADES);
        TrucoCard media = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);
        TrucoCard manilha = TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS);

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, List.of(), vira, 1)
                .botInfo(List.of(fraca, manilha, media), 0)
                .opponentScore(0)
                .build();

        CardToPlay result = sut.chooseCard(intel);

        assertThat(result.content()).isEqualTo(manilha);
    }

    @DisplayName("Should play weakest card when second round after winning first if starting round")
    @Test
    void shouldPlayWeakestCardWhenSecondRoundAfterWinningFirstAsFirstToPlay() {
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

        List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.WON);

        TrucoCard fraca = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);
        TrucoCard media = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);
        TrucoCard manilha = TrucoCard.of(CardRank.SIX, CardSuit.CLUBS);

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, List.of(), vira, 1)
                .botInfo(List.of(media, manilha, fraca), 0)
                .opponentScore(0)
                .build();

        CardToPlay result = sut.chooseCard(intel);

        assertThat(result.content()).isEqualTo(fraca);
    }
}
