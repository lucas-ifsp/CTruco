package com.fernandoAloneBot;

import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;


import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MeuBotTest {

    private MeuBot meuBot;

    @BeforeEach
    void setUp() {
        meuBot = new MeuBot();
    }

    private GameIntel createIntel(List<TrucoCard> hand, TrucoCard vira, int myScore, int opponentScore,
                                  List<GameIntel.RoundResult> roundResults, List<TrucoCard> openCards) {
        return GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(hand, myScore)
                .opponentScore(opponentScore)
                .build();
    }

    // Versão simplificada, sem openCards
    private GameIntel createIntel(List<TrucoCard> hand, TrucoCard vira, int myScore, int opponentScore,
                                  List<GameIntel.RoundResult> roundResults) {
        return createIntel(hand, vira, myScore, opponentScore, roundResults, List.of());
    }



    private TrucoCard getDefaultVira() {
        return TrucoCard.of(FIVE, DIAMONDS);
    }

    private List<TrucoCard> getDefaultHand() {
        return List.of(
                TrucoCard.of(FOUR, CLUBS),
                TrucoCard.of(FIVE, CLUBS),
                TrucoCard.of(SIX, CLUBS)
        );
    }

    private List<TrucoCard> hand;
    private List<GameIntel.RoundResult> result;

    private TrucoCard vira;

    private GameIntel intel;

    private GameIntel.StepBuilder stepBuilder;




    @Nested
    @DisplayName("Mão de onze")
    class GetMaoDeOnzeResponse {

        @Test
        @DisplayName("Testa se recusa mão de onze sem manilhas")
        void testaSeRecusaMaoDeOnzeSemManilhas() {
            GameIntel intel = createIntel(getDefaultHand(), getDefaultVira(), 10, 10, List.of());
            assertFalse(meuBot.getMaoDeOnzeResponse(intel));
        }

        @Test
        @DisplayName("Aceita mão de onze se ambos têm 11 pontos")
        void testMaoDeOnzeBothPlayersHave11() {
          hand = List.of(TrucoCard.of(FOUR, SPADES), TrucoCard.of(FIVE, HEARTS), TrucoCard.of(THREE, CLUBS)
            );

            GameIntel intel = createIntel(hand, getDefaultVira(), 11, 11, List.of());
            Assertions.assertTrue(meuBot.getMaoDeOnzeResponse(intel));
        }

        @Test
        @DisplayName("Recusa mão de onze com cartas fracas e oponente com 11 pontos")
        void testMaoDeOnzeWithWeakHand() {
             hand = List.of(TrucoCard.of(FOUR, SPADES), TrucoCard.of(FIVE, HEARTS), TrucoCard.of(THREE, CLUBS)
            );

            GameIntel intel = createIntel(hand, getDefaultVira(), 10, 11, List.of());
            Assertions.assertFalse(meuBot.getMaoDeOnzeResponse(intel));
        }

        @Test
        @DisplayName("Não aceita mão de onze com dois ases")
        void testMaoDeOnzeWithTwoAces() {
             hand = List.of(TrucoCard.of(ACE, CLUBS), TrucoCard.of(ACE, HEARTS), TrucoCard.of(FOUR, SPADES));

            GameIntel intel = createIntel(hand, getDefaultVira(), 11, 10, List.of());
            Assertions.assertFalse(meuBot.getMaoDeOnzeResponse(intel));
        }

        @Test
        @DisplayName("Aceita mão de onze com 2 cartas de 3")
        void testMaoDeOnzeWithAverageHand() {
             hand = List.of(TrucoCard.of(SEVEN, CLUBS), TrucoCard.of(THREE, HEARTS), TrucoCard.of(THREE, SPADES));

            GameIntel intel = createIntel(hand, getDefaultVira(), 11, 9, List.of());
            Assertions.assertFalse(meuBot.getMaoDeOnzeResponse(intel));
        }

        @Test
        @DisplayName("Deve retornar false se não houver manilha")
        void testHasMaoEquilibradaFalsePorManilha() {
            hand = List.of(TrucoCard.of(SIX, SPADES), TrucoCard.of(FOUR, HEARTS), TrucoCard.of(FIVE, SPADES));
            vira = TrucoCard.of(FIVE, HEARTS);
            //openCards = List.of(vira);
             vira = TrucoCard.of(SEVEN, DIAMONDS); // vira que não ativa manilha

            GameIntel intel = createIntel(hand, vira, 10, 10, List.of());

            Assertions.assertFalse(meuBot.getMaoDeOnzeResponse(intel));

        }

        @Test
        @DisplayName("Testa se recusa mão de onze com uma manilha")
        void testMaoDeOnzeOneManilha() {
            hand = List.of(TrucoCard.of(SIX, SPADES),TrucoCard.of(FOUR, HEARTS),TrucoCard.of(FIVE, SPADES));

            vira = TrucoCard.of(FIVE, HEARTS);
            GameIntel intel = createIntel(hand, vira, 11, 6, List.of());

            Assertions.assertFalse(meuBot.getMaoDeOnzeResponse(intel));
        }

        @Test
        @DisplayName("Recusa mão de onze com 0 manilhas e 3 cartas fracas")
        void testMaoDeOnzeZeroManilhasCartasFracas() {
            hand = List.of(TrucoCard.of(FOUR, CLUBS), TrucoCard.of(FIVE, DIAMONDS), TrucoCard.of(FIVE, CLUBS));
            vira = TrucoCard.of(FIVE, HEARTS);

            GameIntel intel = createIntel(hand, vira, 11, 7, List.of());

            Assertions.assertFalse(meuBot.getMaoDeOnzeResponse(intel));
        }

        @Test
        @DisplayName("Aceita mão de onze com 1 manilha e uma carta forte")
        void testMaoDeOnzeOneManilhaOneStrongCard() {

            hand = List.of(
                    TrucoCard.of(SIX, SPADES),
                    TrucoCard.of(FOUR, HEARTS),
                    TrucoCard.of(THREE, SPADES)
            );
            vira = TrucoCard.of(FIVE, HEARTS);

            intel = createIntel(hand, vira, 11, 10, List.of(GameIntel.RoundResult.DREW));

            Assertions.assertTrue(meuBot.getMaoDeOnzeResponse(intel));
        }

        @Test
        @DisplayName("Testa se aceita mão de onze se ganhou a primeira rodada e tem zap")
        void testMaoDeOnzeWonFirstRoundAndHasZap() {
            hand = List.of(TrucoCard.of(SIX, CLUBS), TrucoCard.of(FOUR, HEARTS), TrucoCard.of(THREE, SPADES)
            );

            vira = TrucoCard.of(FIVE, HEARTS);

            GameIntel intel = createIntel(hand, vira, 11, 8, List.of(GameIntel.RoundResult.WON));

            Assertions.assertTrue(meuBot.getMaoDeOnzeResponse(intel));
        }


        @Test
        @DisplayName("Teste mão com 2 cartas de 2")
        void testHandOfThreeestou() {
            hand = List.of(TrucoCard.of(TWO, HEARTS), TrucoCard.of(TWO, SPADES), TrucoCard.of(FIVE, CLUBS));
            vira = TrucoCard.of(FOUR, DIAMONDS);

            intel = createIntel(hand, vira, 11, 9, List.of());

            Assertions.assertFalse(meuBot.getMaoDeOnzeResponse(intel));
        }

        @Test
        @DisplayName("Recusa mão de onze com cartas medianas e oponente com 10 pontos")
        void testMaoDeOnzeMediumHandOpponentTen() {
            hand = List.of(
                    TrucoCard.of(FIVE, SPADES),
                    TrucoCard.of(FOUR, HEARTS),
                    TrucoCard.of(SIX, CLUBS)
            );
            vira = TrucoCard.of(THREE, DIAMONDS);

            intel = createIntel(hand, vira, 11, 10, List.of());

            Assertions.assertFalse(meuBot.getMaoDeOnzeResponse(intel));
        }



    }

    @Nested
    @DisplayName("decideIfRaises")
    class DecideIfRaises {

        @Test
        @DisplayName("Recusa aumento ")
        void testeDecidaSeAumentaComMaoFraca(){
            hand = List.of(TrucoCard.of(FOUR, SPADES), TrucoCard.of(THREE, HEARTS), TrucoCard.of(FOUR, DIAMONDS));
            vira = TrucoCard.of(FOUR, HEARTS);

            intel = createIntel(hand, vira, 6, 7, List.of(GameIntel.RoundResult.DREW));

            Assertions.assertFalse(meuBot.getMaoDeOnzeResponse(intel));
        }
        @Test@DisplayName("Recusa aumento após perder rodada")
        void testeSeDecideAumentarAposPerderRodada() {
            hand = List.of(TrucoCard.of(ACE, SPADES), TrucoCard.of(FOUR, HEARTS), TrucoCard.of(FOUR, DIAMONDS));
            vira = TrucoCard.of(FOUR, HEARTS);

            intel = createIntel(hand, vira, 6, 7, List.of(GameIntel.RoundResult.LOST));

            Assertions.assertFalse(meuBot.getMaoDeOnzeResponse(intel));


        }




    }

}
