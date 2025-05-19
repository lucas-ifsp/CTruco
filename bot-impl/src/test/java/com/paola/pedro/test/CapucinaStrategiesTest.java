package com.paola.pedro.test;

import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.TrucoCard;
import com.paola.pedro.StartRoundCapucina;
import com.bueno.spi.model.CardRank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CapucinaStrategiesTest {

    private StartRoundCapucina bot;

    @BeforeEach
    void setUp() {
        bot = new StartRoundCapucina();
    }

    @Test
    void deveJogarComMaisSegurancaSeVenceuPrimeiraRodada() {
        bot.registrarResultadoRodada(1, true);
        String estilo = bot.getEstiloDeRodada(2);
        assertEquals("seguro", estilo);
    }

    @Test
    void deveJogarComMaisAgressividadeSePerdeuPrimeiraRodada() {
        bot.registrarResultadoRodada(1, false);
        String estilo = bot.getEstiloDeRodada(2);
        assertEquals("agressivo", estilo);
    }

    @Test
    void deveTentarForcarErroQuandoRodadaEmpatada() {
        bot.registrarResultadoRodada(1, false);
        bot.registrarResultadoRodada(2, true);
        String estilo = bot.getEstiloDeRodada(3);
        assertEquals("pressionar", estilo);
    }

    @Test
    void deveFugirComMaisFracaSeAdversarioJogouForte() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);
        TrucoCard zap = TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS);

        bot.iniciarPartida("Jogador", 0, 0); // Remova o 'vira' se não for aceito
        bot.iniciarRodada(List.of(
                CardRank.FIVE,
                CardRank.SEVEN,
                zap.getRank()
        ));
        bot.registrarCartaAdversario(zap.getRank());
        assertEquals(CardRank.FIVE, bot.jogar());
    }

    @Test
    void valorCartaDeveRetornarValorCorreto() {
        // Suponha que a vira seja o SEIS de OUROS
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);
        TrucoCard zap = TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS);
    }
}