package com.garcia.orlandi.slayerbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SlayerBotTest {

    @Test
    @DisplayName("If first to play, should not play zap on first round")
    void shouldNotPlayZapOnFirstRound(){

        List<GameIntel.RoundResult> roundResults = List.of();
        TrucoCard vira = TrucoCard.of(FOUR, HEARTS);
        List<TrucoCard> cards = List.of(
                TrucoCard.of(FIVE, CLUBS),
                TrucoCard.of(SEVEN, CLUBS),
                TrucoCard.of(FIVE, HEARTS));
        List<TrucoCard> tableCards = List.of(vira);

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder
                .with()
                .gameInfo(roundResults, tableCards, vira, 1)
                .botInfo(cards, 0).opponentScore(0);

        GameIntel game = stepBuilder.build();

        CardToPlay card = new SlayerBot().chooseCard(game);
        TrucoCard chosenCard = card.value();
        assertFalse(chosenCard.isZap(game.getVira()));
    }
}
