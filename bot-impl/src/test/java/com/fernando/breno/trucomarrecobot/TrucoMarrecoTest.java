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
    private List<TrucoCard> openCards;

    private TrucoCard vira;

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
              vira = TrucoCard.of(FIVE, HEARTS);
              openCards = List.of(vira);

              stepBuilder = GameIntel.StepBuilder.with().gameInfo(List.of(GameIntel.RoundResult.LOST),openCards,vira,1).
                      botInfo(hand,11).opponentScore(5);
              Boolean acceptMaoDeOnze = trucoMarreco.getMaoDeOnzeResponse(stepBuilder.build());
              assertTrue(acceptMaoDeOnze);

          }

        @Test
        @DisplayName("Testa se recusa mão de onze sem manilha")
        void  testHandOfElevenNoManilhasAndNoBiggestCouple() {
            hand = List.of(TrucoCard.of(SIX, SPADES), TrucoCard.of(SIX, HEARTS), TrucoCard.of(THREE, SPADES));
            vira = TrucoCard.of(FOUR, HEARTS);
            openCards = List.of(vira);


            stepBuilder = GameIntel.StepBuilder.with().gameInfo(List.of(GameIntel.RoundResult.DREW),openCards,vira,1).
                    botInfo(hand,11).opponentScore(7);
            Boolean acceptMaoDeOnze = trucoMarreco.getMaoDeOnzeResponse(stepBuilder.build());
            assertFalse(acceptMaoDeOnze);

        }

        @Test
        @DisplayName("Testa se recusa mão de onze com uma  manilha")
        void testMaoDeOnzeOneManilha() {
            hand = List.of(TrucoCard.of(SIX, SPADES), TrucoCard.of(FOUR, HEARTS), TrucoCard.of(FIVE, SPADES));
            vira = TrucoCard.of(FIVE, HEARTS);
            openCards = List.of(vira);


            stepBuilder = GameIntel.StepBuilder.with().gameInfo(List.of(GameIntel.RoundResult.LOST),openCards,vira,1).
                    botInfo(hand,11).opponentScore(8);
            Boolean acceptMaoDeOnze = trucoMarreco.getMaoDeOnzeResponse(stepBuilder.build());
            assertFalse(acceptMaoDeOnze);

        }
        @Test
        @DisplayName("Testa se aceita mão de onze com  uma manilha e um 3")
        void testMaoDeOnzehandStrong() {
            hand = List.of(TrucoCard.of(SIX, SPADES), TrucoCard.of(FOUR, HEARTS), TrucoCard.of(THREE, SPADES));
            vira = TrucoCard.of(FIVE, HEARTS);
            openCards = List.of(vira);


            stepBuilder = GameIntel.StepBuilder.with().gameInfo(List.of(GameIntel.RoundResult.DREW),openCards,vira,1).
                    botInfo(hand,11).opponentScore(5);
            Boolean acceptMaoDeOnze = trucoMarreco.getMaoDeOnzeResponse(stepBuilder.build());
            assertTrue(acceptMaoDeOnze);

        }


        @Test
        @DisplayName("Testa se aceita mão de onze  se ganhou a primeira rodada e tem zap")
        void testMaoDeOnzeWonFirstRoundAndHasZap() {
            hand = List.of(TrucoCard.of(SIX, CLUBS), TrucoCard.of(FOUR, HEARTS), TrucoCard.of(THREE, SPADES));
            vira = TrucoCard.of(FIVE, HEARTS);
            openCards = List.of(vira);


            stepBuilder = GameIntel.StepBuilder.with().gameInfo(List.of(GameIntel.RoundResult.WON),openCards,vira,1).
                    botInfo(hand,11).opponentScore(5);
            Boolean acceptMaoDeOnze = trucoMarreco.getMaoDeOnzeResponse(stepBuilder.build());
            assertTrue(acceptMaoDeOnze);

        }

     }
     @Nested
     @DisplayName("decideIfRaises")
     class DecideIfRaises{


     }


    @Test
    void chooseCard() {
    }

    @Test
    void getRaiseResponse() {
    }
}