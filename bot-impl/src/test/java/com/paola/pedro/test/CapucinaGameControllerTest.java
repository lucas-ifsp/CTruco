package com.paola.pedro.test;


import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.TrucoCard;
import com.paola.pedro.CapucinaGameController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CapucinaGameControllerTest {

    private CapucinaGameController controller;
    private TrucoCard vira;

    @BeforeEach
    void setUp() {
        controller = new CapucinaGameController();
        vira = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS); // carta virada padrão
    }

    @Test
    void deveIniciarPartidaComPlacarCorreto() {
        controller.iniciarPartida("Jogador", 5, 7, vira);
        assertEquals("normal", controller.getEstiloDeJogo());
    }

    @Test
    void deveIniciarRodadaERegistrarMao() {
        List<TrucoCard> mao = List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)
        );
        controller.iniciarRodada(mao);
        boolean resultado = controller.deveBlefar();
        assertNotNull(resultado); // só testando que processou corretamente
    }

    @Test
    void deveRegistrarCartaDoAdversarioECriarExpectativaAlta() {
        controller.iniciarPartida("Jogador", 5, 7, vira);
        controller.registrarCartaAdversario(CardRank.SEVEN, TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES));
        String expectativa = controller.getExpectativaRodada();
        assertEquals("alta", expectativa);
    }

    @Test
    void deveRetornarEstiloDeJogoDefensivoEmOnzeOnze() {
        controller.iniciarPartida("Jogador", 11, 11, vira);
        assertEquals("defensivo", controller.getEstiloDeJogo());
    }

    @Test
    void deveRetornarEstiloAgressivoComAdversario11Pontos() {
        controller.iniciarPartida("Jogador", 5, 11, vira);
        assertEquals("agressivo", controller.getEstiloDeJogo());
    }

    @Test
    void deveIndicarQueBotDeveBlefarComAdversarioFraco() {
        List<TrucoCard> mao = List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)
        );
        controller.iniciarPartida("Jogador", 10, 1, vira);
        controller.iniciarRodada(mao);
        assertTrue(controller.deveBlefar());
    }
}

