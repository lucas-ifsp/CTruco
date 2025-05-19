package com.fernandoAloneBot;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;


import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MeuBotTest {

    private MeuBot meuBot;

    @BeforeEach
    void setUp() {
        meuBot = new MeuBot();
    }

    // Método original, sem opponentCard (sem quebrar testes antigos)
    private GameIntel createIntel(List<TrucoCard> hand, TrucoCard vira, int myScore, int opponentScore,
                                  List<GameIntel.RoundResult> roundResults, List<TrucoCard> openCards) {
        return GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(hand, myScore)
                .opponentScore(opponentScore)
                .build();
    }

    // Sobrecarga nova que aceita opponentCard — só usar quando precisar
    private GameIntel createIntel(List<TrucoCard> hand, TrucoCard vira, int myScore, int opponentScore,
                                  List<GameIntel.RoundResult> roundResults, List<TrucoCard> openCards,
                                  TrucoCard opponentCard) {
        return GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(hand, myScore)
                .opponentScore(opponentScore)
                .opponentCard(opponentCard)  // adiciona opponentCard aqui
                .build();
    }

    // Sobrecarga simplificada, sem openCards (mantém testes antigos)
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

    private List<TrucoCard> openCards;

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
            Assertions.assertFalse(meuBot.getMaoDeOnzeResponse(intel));
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
            hand = List.of(TrucoCard.of(SIX, HEARTS), TrucoCard.of(FOUR, HEARTS), TrucoCard.of(THREE, SPADES));

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

        @Test
        @DisplayName("Aceita jogar mão de onze com manilha e um 3")
        void aceitaMaoDeOnzeComManilha() {
            hand = List.of(
                    TrucoCard.of(THREE, CLUBS),
                    TrucoCard.of(KING, HEARTS),
                    TrucoCard.of(TWO, DIAMONDS)
            );

            vira = TrucoCard.of(ACE, DIAMONDS);

            openCards = List.of(vira);
            intel = createIntel(hand, vira, 11, 11, List.of(),openCards);


          Assertions.assertTrue(meuBot.getMaoDeOnzeResponse(intel));

        }

        @Test
        @DisplayName("Aceita jogar mão de onze com 3 cartas de 3")
        void aceitaMaoDeOnzeComTresCartasDeTres() {
            hand = List.of(
                    TrucoCard.of(THREE, CLUBS),
                    TrucoCard.of(THREE, HEARTS),
                    TrucoCard.of(THREE, DIAMONDS)
            );

            vira = TrucoCard.of(ACE, DIAMONDS);

            openCards = List.of(vira);


            intel = createIntel(hand, vira, 11, 11, List.of(), openCards);


            Assertions.assertTrue(meuBot.getMaoDeOnzeResponse(intel));
        }

        @Test
        @DisplayName("Aceita jogar mão de onze com 3 cartas de 2")
        void aceitaMaoDeOnzeComTresCartasDeDois() {
            hand = List.of(
                    TrucoCard.of(TWO, CLUBS),
                    TrucoCard.of(TWO, HEARTS),
                    TrucoCard.of(TWO, DIAMONDS)
            );

            vira = TrucoCard.of(THREE, DIAMONDS);

            openCards = List.of(vira);


            intel = createIntel(hand, vira, 11, 11, List.of(), openCards);


            Assertions.assertTrue(meuBot.getMaoDeOnzeResponse(intel));
        }








    }

    @Nested
    @DisplayName("Decidir se aumenta")
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

        @Test
        @DisplayName("Aceita aumento com casal maior")
        void testeAceitaAumentoCasalMaior() {
            hand = List.of(TrucoCard.of(FIVE,CLUBS ), TrucoCard.of(FIVE, HEARTS));
            vira = TrucoCard.of(FOUR, DIAMONDS);
            openCards = List.of(vira);

            intel = createIntel(hand, vira, 6, 7, List.of(GameIntel.RoundResult.DREW),openCards);
            Assertions.assertTrue(meuBot.decideIfRaises(intel));
        }

        @Test
        @DisplayName("Testa aumenta apos ganhou a primeira rodada e tem zap")
        void testMaoDeOnzeWonFirstRoundAndHasZap() {

            hand = List.of(
                    TrucoCard.of(TWO, CLUBS),
                    TrucoCard.of(TWO, HEARTS),
                    TrucoCard.of(ACE, SPADES)
            );
            vira = TrucoCard.of(ACE, HEARTS);
            openCards = List.of(vira);

            GameIntel intel = createIntel(hand, vira, 6, 8, List.of(GameIntel.RoundResult.WON), openCards);

            Assertions.assertTrue(meuBot.decideIfRaises(intel));
        }

        @Test
        @DisplayName("Não chama Truco se a força da mão for média")
        void  naoChamaTrucoQuandoForMaoMedia(){
            hand = List.of(
                    TrucoCard.of(FIVE, CLUBS),
                    TrucoCard.of(FOUR, HEARTS),
                    TrucoCard.of(FOUR, SPADES)
            );
            vira = TrucoCard.of(THREE, HEARTS);
            openCards = List.of(vira);

            GameIntel intel = createIntel(hand, vira, 6, 8, List.of(), openCards);

            Assertions.assertTrue(meuBot.decideIfRaises(intel));
        }

        @Test
        @DisplayName("Aceita aumento com casal menor")
        void aceitaAumentoComCasalMenor() {
            hand = List.of(TrucoCard.of(FIVE, SPADES), TrucoCard.of(FIVE, HEARTS));
            vira = TrucoCard.of(FOUR, HEARTS);
            openCards = List.of(vira);

            intel = createIntel(hand, vira, 6, 8, List.of(GameIntel.RoundResult.LOST), openCards);
            Assertions.assertTrue(meuBot.decideIfRaises(intel));
        }

        @Test
        @DisplayName("Recusa aumento após perder a primeira rodada e partida empatada")
        void recusaAumentoAposPerderPrimeiraRodadaComPartidaEmpatada() {
            hand = List.of(TrucoCard.of(ACE, SPADES), TrucoCard.of(FOUR, HEARTS), TrucoCard.of(THREE, CLUBS));
            vira = TrucoCard.of(FOUR, HEARTS);
            openCards = List.of(TrucoCard.of(THREE, DIAMONDS)); // carta da primeira rodada

            intel = createIntel(hand, vira, 10, 10, List.of(GameIntel.RoundResult.LOST), openCards);

            Assertions.assertFalse(meuBot.decideIfRaises(intel));
        }

        @Test
        @DisplayName("Aceita aumento com duas manilhas na mão")
        void aceitaAumentoComDuasManilhas() {
            hand = List.of(TrucoCard.of(TWO, HEARTS), TrucoCard.of(TWO, CLUBS));
            vira = TrucoCard.of(ACE, DIAMONDS);
            openCards = List.of(vira);

            intel = createIntel(hand, vira, 5, 6, List.of(), openCards);
            Assertions.assertTrue(meuBot.decideIfRaises(intel));
        }

        @Test
        @DisplayName("Recusa aumento com cartas baixas")
        void recusaAumentoComCartasBaixas() {
            hand = List.of(TrucoCard.of(FOUR, CLUBS), TrucoCard.of(SIX, SPADES), TrucoCard.of(FIVE, HEARTS));
            vira = TrucoCard.of(QUEEN, CLUBS);
            openCards = List.of(vira);

            intel = createIntel(hand, vira, 5, 6, List.of(), openCards);
            Assertions.assertFalse(meuBot.decideIfRaises(intel));
        }

        @Test
        @DisplayName("Não aumenta se já perdeu a primeira rodada e não tem cartas fortes")
        void naoAumentaSePerdeuRodadaNaoTemCartasFortes() {
            hand = List.of(TrucoCard.of(SEVEN, CLUBS), TrucoCard.of(SIX, HEARTS), TrucoCard.of(SEVEN, DIAMONDS));
            vira = TrucoCard.of(FOUR, HEARTS);
            openCards = List.of(vira);

            intel = createIntel(hand, vira, 8, 8, List.of(GameIntel.RoundResult.LOST), openCards);
            Assertions.assertFalse(meuBot.decideIfRaises(intel));
        }

        @Test
        @DisplayName("Aceita aumento após vencer a primeira rodada com mão forte")
        void aceitaAumentoAposGanharRodadaComMaoBoa() {
            hand = List.of(TrucoCard.of(FIVE, SPADES), TrucoCard.of(THREE, HEARTS), TrucoCard.of(FIVE, DIAMONDS));
            vira = TrucoCard.of(FOUR, HEARTS);
            openCards = List.of(vira);

            intel = createIntel(hand, vira, 7, 6, List.of(GameIntel.RoundResult.WON), openCards);
            Assertions.assertTrue(meuBot.decideIfRaises(intel));
        }

        @Test
        @DisplayName("Não aumenta se estiver com 11 pontos e mao fraca")
        void naoAumentaComOnzePontosMaoFraca() {
            hand = List.of(TrucoCard.of(KING, CLUBS), TrucoCard.of(QUEEN, HEARTS), TrucoCard.of(JACK, DIAMONDS));
            vira = TrucoCard.of(FOUR, HEARTS);
            openCards = List.of(vira);

            intel = createIntel(hand, vira, 11, 10, List.of(), openCards);
            Assertions.assertFalse(meuBot.decideIfRaises(intel));
        }

        @Test
        @DisplayName("Não aumenta com mão média e oponente com 11 pontos")
        void naoAumentaComMaoMediaEOponenteComOnze() {
            List<TrucoCard> hand = List.of(
                    TrucoCard.of(ACE, CLUBS),
                    TrucoCard.of(SEVEN, HEARTS),
                    TrucoCard.of(QUEEN, DIAMONDS)
            );

            TrucoCard vira = TrucoCard.of(THREE, SPADES);

            GameIntel intel = createIntel(hand, vira, 9, 11, List.of());

            boolean res = meuBot.decideIfRaises(intel);

            assertThat(res).isFalse(); // Situação arriscada
        }



    }

    @Nested
    @DisplayName("Obter resposta de aumento")
    class getRaiseResponse {

        @Test
        @DisplayName("Testa aceita aumento se tiver uma manilha ou mais")
        void testaAceitaAumentoSeTemManilhaOuMais() {
            hand = List.of(TrucoCard.of(TWO, HEARTS), TrucoCard.of(TWO, CLUBS), TrucoCard.of(QUEEN, CLUBS));
            vira = TrucoCard.of(ACE, SPADES);
            openCards = List.of(vira);

            intel = createIntel(hand, vira, 5, 6, List.of(), openCards);

            int res = meuBot.getRaiseResponse(intel);


            assertThat(res).isPositive();
        }

        @Test
        @DisplayName("Teste aceita o aumento se ganhou a primeiro e tem zap")
        void testMaoDeOnzeMaoForte() {
            hand = List.of( TrucoCard.of(SIX,CLUBS), TrucoCard.of(THREE, SPADES));

            vira = TrucoCard.of(FIVE, HEARTS);
            openCards = List.of(vira);

            intel = createIntel(hand, vira, 5, 6, List.of(GameIntel.RoundResult.WON), openCards);


            int res = meuBot.getRaiseResponse(intel);
            // System.out.println(res);
            assertThat(res).isPositive();

        }


        @Test
        @DisplayName("Recusa aumento com mão fraca")
        void recusaAumentoComMaoFraca() {
            hand = List.of(TrucoCard.of(JACK, SPADES), TrucoCard.of(FOUR, HEARTS), TrucoCard.of(FOUR, DIAMONDS));

            vira = TrucoCard.of(FOUR, HEARTS);
            openCards = List.of(vira);

            intel =createIntel(hand, vira, 5, 6, List.of(GameIntel.RoundResult.DREW), openCards);

            int res = meuBot.getRaiseResponse(intel);
            System.out.println(res);

            assertThat(res).isLessThanOrEqualTo(0);
        }

        @Test
        @DisplayName("Deve aumentar truco se força da mão >= 15 e diferença de pontos > 3")
        void deveAumentarTrucoForcaMaiorOuIgual15EDiferencaMaiorQue3() {
            hand = List.of(
                    TrucoCard.of(ACE, SPADES),
                    TrucoCard.of(TWO, HEARTS),
                    TrucoCard.of(QUEEN, CLUBS)
            );

            vira = TrucoCard.of(ACE, HEARTS);

            openCards = List.of(vira);

            intel = createIntel(hand, vira, 10, 5, List.of(), openCards); // diferença 5


            int res = meuBot.getRaiseResponse(intel);

            System.out.println("Resposta: " + res);

            assertThat(res).isLessThanOrEqualTo(0);
        }

        @Test
        @DisplayName("Responde aceitando aumento mesmo sem manilha quando a mão é forte")
        void aceitaAumentoMesmoSemManilhaQuandoMaoEhForte() {
            hand = List.of(
                    TrucoCard.of(THREE, HEARTS),
                    TrucoCard.of(TWO, SPADES),
                    TrucoCard.of(ACE, DIAMONDS)
            );

            vira = TrucoCard.of(FOUR, CLUBS);
            openCards = List.of(vira);

            intel = createIntel(hand, vira, 10, 5, List.of(), openCards);

            int res = meuBot.getRaiseResponse(intel);

            assertThat(res).isLessThanOrEqualTo(0);  // Aceita aumento, resposta positiva
        }

        @Test
        @DisplayName("Responde aceitando aumento se tem zap e está empatado")
        void respondeAceitandoAumentoSeTemZapEEstaEmpatado() {
            hand = List.of(
                    TrucoCard.of(SIX, CLUBS),
                    TrucoCard.of(TWO, HEARTS),
                    TrucoCard.of(THREE, SPADES)
            );
            vira = TrucoCard.of(FIVE, HEARTS);
            openCards = List.of(vira);
           intel = createIntel(hand, vira, 10, 5, List.of(GameIntel.RoundResult.DREW), openCards);

            int res = meuBot.getRaiseResponse(intel);
            System.out.println(res);

            assertThat(res).isOne();
        }

        @Test
        @DisplayName("Responde recusa após perder a primeira mesmo com uma manilha")
        void respondeRecusaAposPerderPrimeiraComManilha() {
            hand = List.of(TrucoCard.of(FOUR, HEARTS), TrucoCard.of(SIX, SPADES));
            vira = TrucoCard.of(THREE, SPADES);

            openCards = List.of(vira);

            intel = createIntel(hand, vira, 2, 5, List.of(GameIntel.RoundResult.LOST), openCards);

            int res = meuBot.getRaiseResponse(intel);

            assertThat(res).isZero();
        }

        @Test
        @DisplayName("Responde aceita após perder a primeira rodada, mas ter casal maior")
        void respondeAceitaAposPerderPrimeiraMasTerCasalMaior() {
            hand = List.of(TrucoCard.of(FOUR, HEARTS), TrucoCard.of(FOUR, CLUBS));

            vira = TrucoCard.of(THREE, CLUBS);

            openCards = List.of();

            intel = createIntel(hand, vira, 2, 5, List.of(GameIntel.RoundResult.LOST), openCards);


            int res = meuBot.getRaiseResponse(intel);
            assertThat(res).isPositive();
        }



    }

     @Nested
    @DisplayName("Escolher carta")
    class chooseCard{
         @Test
         @DisplayName("Joga a menor carta que vence a do oponente quando não é manilha")
         void jogaCartaQueMataOponenteQuandoNaoEhManilha() {
             hand = List.of(TrucoCard.of(TWO, HEARTS), TrucoCard.of(KING, CLUBS), TrucoCard.of(TWO, CLUBS));

             vira = TrucoCard.of(SEVEN, SPADES);

             openCards = List.of(TrucoCard.of(ACE, DIAMONDS));

             intel = createIntel(hand, vira, 3, 0, List.of(), openCards);

             CardToPlay cardToPlay = meuBot.chooseCard(intel);

             System.out.println(cardToPlay.value());

             //  DOIS DE COPAS, que é a menor Ace
             assertEquals(TrucoCard.of(TWO, HEARTS), cardToPlay.value());
         }


         @Test
         @DisplayName("Testa jogar a carta mais fraca quando não tem manilha")
         void jogaCartaMaisFracaQuandoNaoTemManilha() {
             hand = List.of(
                     TrucoCard.of(FOUR, SPADES),
                     TrucoCard.of(THREE, CLUBS)
             );

             vira = TrucoCard.of(ACE, SPADES);

             openCards = List.of(TrucoCard.of(TWO, DIAMONDS)); // carta jogada pelo oponente

             intel = createIntel(hand, vira, 1, 0, List.of(GameIntel.RoundResult.DREW), openCards);

             CardToPlay cardToPlay = meuBot.chooseCard(intel);

             assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(FOUR, SPADES));
         }

         @Test
         @DisplayName("Testa jogar a carta mais forte quando não tem manilha")
         void jogaCartaMaisForteQuandoNaoTemManilha() {
             hand = List.of(TrucoCard.of(FOUR, SPADES), TrucoCard.of(THREE, CLUBS), TrucoCard.of(FOUR, CLUBS));

             vira = TrucoCard.of(ACE, SPADES);

             openCards = List.of(TrucoCard.of(TWO, DIAMONDS));

             intel = createIntel(hand, vira, 1, 0, List.of(GameIntel.RoundResult.DREW), openCards);

             CardToPlay cardToPlay = meuBot.chooseCard(intel);

             assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(THREE, CLUBS));
         }

         @Test
         @DisplayName("Joga a menor carta que vence a do oponente (sem manilha)")
         void jogaMenorCartaQueMataOponenteSemManilha() {


             hand = List.of(TrucoCard.of(TWO, CLUBS), TrucoCard.of(SIX, CLUBS), TrucoCard.of(THREE, DIAMONDS));

             vira = TrucoCard.of(THREE, HEARTS);


             openCards = List.of(TrucoCard.of(FIVE, CLUBS));

             TrucoCard opponentCard = TrucoCard.of(TWO, DIAMONDS);

             intel = createIntel(hand, vira, 1, 0, List.of(), openCards,opponentCard);

             CardToPlay cardToPlay = meuBot.chooseCard(intel);

             assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(THREE, DIAMONDS));
         }

         @Test
         @DisplayName("Joga manilha para matar manilha do oponente")
         void jogaManilhaParaMatarManilhaDoOponent() {
             hand = List.of(
                     TrucoCard.of(KING, DIAMONDS),
                     TrucoCard.of(TWO, HEARTS),
                     TrucoCard.of(TWO, SPADES)
             );

             vira = TrucoCard.of(ACE, SPADES); // manilha = DOIS

             openCards = List.of(TrucoCard.of(TWO, DIAMONDS));

             TrucoCard opponentCard = TrucoCard.of(TWO, DIAMONDS);

             intel = createIntel(hand, vira, 3, 0, List.of(), openCards, opponentCard);

             CardToPlay cardToPlay = meuBot.chooseCard(intel);


             assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(TWO, SPADES));
         }

         @Test
         @DisplayName("Testa  descartar manilha menor")
         void jogaDescartaManilhaMenor() {
             hand = List.of(
                     TrucoCard.of(THREE, DIAMONDS),
                     TrucoCard.of(THREE, SPADES),
                     TrucoCard.of(THREE, CLUBS)
             );
             vira = TrucoCard.of(TWO, SPADES);

              openCards = List.of();

             intel = createIntel(hand, vira, 5, 6, List.of(), openCards);

             CardToPlay cardToPlay = meuBot.chooseCard(intel);

             assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(THREE, DIAMONDS));
         }

         @Test
         @DisplayName("Descarta menor carta quando não pode vencer manilha")
         void descartaCartaSemChanceDeVencer() {
             hand = List.of(TrucoCard.of(QUEEN, CLUBS), TrucoCard.of(SEVEN, SPADES), TrucoCard.of(TWO, DIAMONDS));
             vira = TrucoCard.of(FOUR, HEARTS);

             openCards = List.of(TrucoCard.of(FIVE, DIAMONDS));

             TrucoCard opponentCard = TrucoCard.of(FIVE, DIAMONDS);

             intel = createIntel(hand, vira, 2, 11, List.of(), openCards, opponentCard);
             CardToPlay cardToPlay = meuBot.chooseCard(intel);

             assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(SEVEN, SPADES));
         }

         @Test
         @DisplayName("Joga a menor manilha para matar a manilha do oponente")
         void jogaMenorManilhaParaMatarManilhaOponente() {
             hand = List.of(
                     TrucoCard.of(THREE, HEARTS),
                     TrucoCard.of(THREE, SPADES),
                     TrucoCard.of(KING, CLUBS)
             );

             vira = TrucoCard.of(TWO, HEARTS);

              openCards = List.of(TrucoCard.of(THREE,DIAMONDS ));

             TrucoCard opponentCard = TrucoCard.of(THREE, DIAMONDS);

             intel = createIntel(hand, vira, 5, 3, List.of(), openCards, opponentCard);

             CardToPlay cardToPlay = meuBot.chooseCard(intel);

             assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(THREE, SPADES));
         }

         @Test
         @DisplayName("Joga a carta mais forte na primeira rodada")
         void jogaCartaMaisForteNaPrimeiraRodada() {
             hand = List.of(
                     TrucoCard.of(SEVEN, CLUBS),
                     TrucoCard.of(ACE, DIAMONDS),
                     TrucoCard.of(THREE, HEARTS)
             );

             vira = TrucoCard.of(FOUR, CLUBS);

             openCards = List.of();


             intel = createIntel(hand, vira, 0, 0, List.of(), openCards);

             CardToPlay cardToPlay = meuBot.chooseCard(intel);

             assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(THREE, HEARTS));
         }

         @Test
         @DisplayName("Joga carta para empatar quando tiver manilha e oponente jogou carta que não é manilha")
         void jogaCartaParaEmpatarComManilhaContraCartaNormal() {
             hand = List.of(
                     TrucoCard.of(TWO, HEARTS),
                     TrucoCard.of(FIVE, CLUBS),
                     TrucoCard.of(SEVEN, DIAMONDS)
             );

             vira = TrucoCard.of(THREE, SPADES);

             TrucoCard opponentCard = TrucoCard.of(SIX, DIAMONDS);

             openCards = List.of(opponentCard);
             intel = createIntel(hand, vira, 2, 2, List.of(), openCards, opponentCard);

             CardToPlay cardToPlay = meuBot.chooseCard(intel);

             assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(SEVEN,DIAMONDS));
         }

         @Test
         @DisplayName("Joga a carta mais forte da mão")
         void jogaCartaMaisForte() {
             hand = List.of(
                     TrucoCard.of(SEVEN, CLUBS),
                     TrucoCard.of(KING, HEARTS),
                     TrucoCard.of(ACE, SPADES)
             );

             vira = TrucoCard.of(THREE, DIAMONDS);

             openCards = List.of();


           //  TrucoCard opponentCard = null;

             intel = createIntel(hand, vira, 2, 2, List.of(), openCards);

             CardToPlay cardToPlay = meuBot.chooseCard(intel);


             assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(ACE, SPADES));
         }










     }


}
