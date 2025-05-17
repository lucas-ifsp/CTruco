package com.paola.pedro.test;


import com.paola.pedro.RoundClosingCapucina;
import com.paola.pedro.RoundClosingCapucina.VencedorRodada;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CapucinaRoudClosingTest {

    private RoundClosingCapucina round;

    @BeforeEach
    void setUp() {
        round = new RoundClosingCapucina();
    }

    @Test
    void deveRegistrarVencedorERetornarBotSeGanhaDois() {
        round.registrarResultadoRodada(1, VencedorRodada.BOT);
        round.registrarResultadoRodada(2, VencedorRodada.BOT);
        assertEquals(VencedorRodada.BOT, round.determinarVencedorRodada());
    }

    @Test
    void deveRegistrarVencedorERetornarAdversarioSeGanhaDois() {
        round.registrarResultadoRodada(1, VencedorRodada.ADVERSARIO);
        round.registrarResultadoRodada(2, VencedorRodada.ADVERSARIO);
        assertEquals(VencedorRodada.ADVERSARIO, round.determinarVencedorRodada());
    }

    @Test
    void deveRetornarEmpateSeCadaUmVenceUmaEUmaEmpata() {
        round.registrarResultadoRodada(1, VencedorRodada.BOT);
        round.registrarResultadoRodada(2, VencedorRodada.ADVERSARIO);
        round.registrarResultadoRodada(3, VencedorRodada.EMPATE);
        assertEquals(VencedorRodada.EMPATE, round.determinarVencedorRodada());
    }

    @Test
    void deveRetornarBotSeVencerDuasMesmoComEmpate() {
        round.registrarResultadoRodada(1, VencedorRodada.BOT);
        round.registrarResultadoRodada(2, VencedorRodada.EMPATE);
        round.registrarResultadoRodada(3, VencedorRodada.BOT);
        assertEquals(VencedorRodada.BOT, round.determinarVencedorRodada());
    }

    @Test
    void deveRetornarAdversarioSeVencerMaisRodadas() {
        round.registrarResultadoRodada(1, VencedorRodada.ADVERSARIO);
        round.registrarResultadoRodada(2, VencedorRodada.ADVERSARIO);
        round.registrarResultadoRodada(3, VencedorRodada.BOT);
        assertEquals(VencedorRodada.ADVERSARIO, round.determinarVencedorRodada());
    }

    @Test
    void deveRetornarEmpateSeSemVencedorDefinido() {
        assertEquals(VencedorRodada.EMPATE, round.determinarVencedorRodada());
    }

    @Test
    void deveLimparResultadosAoResetarRodada() {
        round.registrarResultadoRodada(1, VencedorRodada.BOT);
        round.resetarRodada();
        assertEquals(VencedorRodada.EMPATE, round.determinarVencedorRodada());
    }

    @Test
    void deveLancarExcecaoParaRodadaInvalida() {
        assertThrows(IllegalArgumentException.class, () -> round.registrarResultadoRodada(0, VencedorRodada.BOT));
        assertThrows(IllegalArgumentException.class, () -> round.registrarResultadoRodada(4, VencedorRodada.BOT));
    }
}
