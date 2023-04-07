package com.bonelli.noli.paulistabot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.GameIntel.StepBuilder;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

class PaulistaBotTest {

    private StepBuilder stepBuilder;

    private List<GameIntel.RoundResult> roundResults;
    private List<TrucoCard> openCards;
    private List<TrucoCard> botCards;

    private TrucoCard vira;

    @Test
    @DisplayName("Creating a GameIntel object")
    void creatingAGameIntelObject () {
        roundResults = List.of(GameIntel.RoundResult.DREW);
        openCards = List.of(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS));
        botCards = List.of(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                TrucoCard.of(CardRank.THREE, CardSuit.SPADES));
        vira = TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS);
        stepBuilder = StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(3);
        GameIntel intel = stepBuilder.opponentCard(TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS)).build();
        System.out.println("Cartas do meu bot");
        for (int i = 0; i < intel.getCards().size(); i++) {
            System.out.printf("Carta %d com valor %d \n", (i + 1), intel.getCards().get(i).getRank().value());
            System.out.printf("Carta %d com rank %s \n", (i + 1), intel.getCards().get(i));
        }
        System.out.println("Carta do oponente -> " + intel.getOpponentCard());
        System.out.println("Cartas abertas -> " + intel.getOpenCards());
        System.out.println("Vira -> " + intel.getVira());
    }
}