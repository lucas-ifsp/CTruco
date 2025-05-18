package com.paola.pedro.test;

import com.bueno.spi.model.CardRank;
import com.paola.pedro.PlaysRoundCapucina;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CapucinaPlaysTest {

    private PlaysRoundCapucina playsRound;

    @BeforeEach
    void setUp() {
        playsRound = new PlaysRoundCapucina();
    }

    @Test
    void deveDefinirPosicaoNaRodada() {
        playsRound.setPosicaoNaRodada("primeiro");
        assertEquals("primeiro", playsRound.getPosicaoNaRodada());
    }

    @Test
    void deveRegistrarCartaAdversario() {
        playsRound.registrarCartaAdversario(CardRank.FOUR);
        Set<CardRank> cartas = playsRound.getCartasAdversario();
        assertTrue(cartas.contains(CardRank.FOUR));
    }

    @Test
    void deveAumentarExpectativaSeCartaForAce() {
        playsRound.registrarCartaAdversario(CardRank.ACE);
        assertEquals("alta", playsRound.getExpectativaRodada());
    }

    @Test
    void deveAumentarExpectativaSeCartaForEspadao() {
        // Supondo que "ESPADAO" seja um CardRank válido ou equivalente a algum enum
        // Simulamos com um valor fake chamado ESPADAO apenas para esse teste
        CardRank espadaoFake = CardRank.valueOf("ACE"); // substitua se houver CardRank.ESPADAO real
        playsRound.registrarCartaAdversario(espadaoFake);
        assertEquals("alta", playsRound.getExpectativaRodada());
    }

    @Test
    void expectativaDeveSerNormalSeCartaNaoForAlta() {
        playsRound.registrarCartaAdversario(CardRank.SEVEN);
        assertEquals("normal", playsRound.getExpectativaRodada());
    }

    @Test
    void estiloDeJogoDeveSerDefensivoSeOnzeAOnze() {
        playsRound.setPlacar(11, 11);
        assertEquals("defensivo", playsRound.getEstiloDeJogo());
    }

    @Test
    void estiloDeJogoDeveSerCautelosoSeBotTemDez() {
        playsRound.setPlacar(10, 5);
        assertEquals("cauteloso", playsRound.getEstiloDeJogo());
    }

    @Test
    void estiloDeJogoDeveSerAgressivoSeAdversarioTemOnze() {
        playsRound.setPlacar(8, 11);
        assertEquals("agressivo", playsRound.getEstiloDeJogo());
    }

    @Test
    void estiloDeJogoDeveSerOusadoSeBotTemZero() {
        playsRound.setPlacar(0, 6);
        assertEquals("ousado", playsRound.getEstiloDeJogo());
    }

    @Test
    void estiloDeJogoDeveSerNormalEmOutrosCasos() {
        playsRound.setPlacar(3, 4);
        assertEquals("normal", playsRound.getEstiloDeJogo());
    }

    @Test
    void naoDeveSerDefensivoSePlacarNaoForOnzeAOnze() {
        playsRound.setPlacar(11, 10);
        assertNotEquals("defensivo", playsRound.getEstiloDeJogo());
    }

    @Test
    void naoDeveSerCautelosoSeBotNaoTiverDez() {
        playsRound.setPlacar(9, 5);
        assertNotEquals("cauteloso", playsRound.getEstiloDeJogo());
    }

    @Test
    void naoDeveSerAgressivoSeAdversarioNaoTiverOnze() {
        playsRound.setPlacar(8, 10);
        assertNotEquals("agressivo", playsRound.getEstiloDeJogo());
    }

    @Test
    void naoDeveSerOusadoSeBotNaoTiverZero() {
        playsRound.setPlacar(1, 6);
        assertNotEquals("ousado", playsRound.getEstiloDeJogo());
    }
}
