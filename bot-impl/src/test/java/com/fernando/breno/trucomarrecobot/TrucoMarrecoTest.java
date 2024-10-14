package com.fernando.breno.trucomarrecobot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;

import static org.junit.jupiter.api.Assertions.*;

class TrucoMarrecoTest {
    private List<TrucoCard> hand;

    private GameIntel intel;

    private GameIntel.StepBuilder stepBuilder;

    TrucoMarreco  trucoMarreco;
    @BeforeEach
    void setUp() {trucoMarreco = new TrucoMarreco();}
    @Nested
    @DisplayName("getMaoDeOnzeResponse")
     class GetMaoDeOnzeResponse {
          @Test
          @DisplayName("Testa se aceita mão de onze  com duas manilhas ou mais")
          void TestMaoDeOnzeWithTwoOrMoreManilhas() {
              hand = List.of(TrucoCard.of(SIX, SPADES), TrucoCard.of(SIX, HEARTS), TrucoCard.of(THREE, SPADES));
              TrucoCard vira = TrucoCard.of(FIVE, HEARTS);
              List<TrucoCard> openCards = List.of(vira);

              stepBuilder = GameIntel.StepBuilder.with().gameInfo(List.of(GameIntel.RoundResult.LOST),openCards,vira,1).
                      botInfo(hand,11).opponentScore(5);
              Boolean acceptMaoDeOnze = trucoMarreco.getMaoDeOnzeResponse(stepBuilder.build());
              assertTrue(acceptMaoDeOnze);

          }

     }
    @Test
    void decideIfRaises() {
    }

    @Test
    void chooseCard() {
    }

    @Test
    void getRaiseResponse() {
    }
}