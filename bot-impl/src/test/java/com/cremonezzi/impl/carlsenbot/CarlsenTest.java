package com.cremonezzi.impl.carlsenbot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CarlsenTest {
    private Carlsen carlsenBot;

    @BeforeEach
    public void config() {
        carlsenBot = new Carlsen();
    }

    @Test
    @DisplayName("Should raise")
    public void ShouldRaise() {
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS); // Vira is AD | Manilha is 2

        // Game info
        List<GameIntel.RoundResult> roundResults = Collections.emptyList(); // Game just started
        List<TrucoCard> openCards = List.of(vira);

        // Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .build();

        assertThat(carlsenBot.decideIfRaises(intel)).isTrue();
    }

    @Test
    @DisplayName("Should choose lowest card in hand")
    public void ShouldChooseLowestInHand() {
        TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES); // Vira is QD | Manilha is J

        // Game info
        List<GameIntel.RoundResult> roundResults = Collections.emptyList(); // Game just started
        List<TrucoCard> openCards = List.of(vira);

        // Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.JACK, CardSuit.CLUBS),
                TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.THREE, CardSuit.SPADES)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .build();

        assertThat(carlsenBot.chooseCard(intel).value().getRank()).isEqualTo(CardRank.SEVEN);
    }

    @Test
    @DisplayName("Should choose lowest card in hand when opponent zap")
    public void ShouldChooseLowestWhenOpponentZap() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.HEARTS); // Vira is 6H | Manilha is 7

        // Game info
        List<GameIntel.RoundResult> roundResults = Collections.emptyList(); // Game just started
        List<TrucoCard> openCards = List.of(vira);

        // Bot info
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS)
        );

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 5)
                .opponentScore(2)
                .opponentCard(TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS))
                .build();

        assertThat(carlsenBot.chooseCard(intel).value().getRank()).isEqualTo(CardRank.FOUR);
    }
}