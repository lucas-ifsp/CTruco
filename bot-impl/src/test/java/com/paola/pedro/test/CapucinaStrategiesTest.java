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
    void deveResponderComCartaSuperiorSeAdversarioJogouFraca() {
        bot.iniciarPartida("bot", 0, 0); // Corrige o estado do bot
        bot.iniciarRodada(List.of(CardRank.SIX, CardRank.JACK, CardRank.THREE));
        bot.registrarCartaAdversario(CardRank.FOUR);
        CardRank jogada = bot.jogar();
        assertTrue(jogada.ordinal() > CardRank.FOUR.ordinal());
        assertTrue(List.of(CardRank.SIX, CardRank.JACK, CardRank.THREE).contains(jogada));
    }

    @Test
    void deveFugirComMaisFracaSeAdversarioJogouForte() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);
        TrucoCard zap = TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS);

        bot.iniciarPartida("bot", 0, 0); // Corrige o estado do bot
        bot.iniciarRodada(List.of(
                CardRank.FIVE,
                CardRank.SEVEN,
                zap.getRank()
        ));
        bot.registrarCartaAdversario(zap.getRank());
        assertEquals(CardRank.FIVE, bot.jogar()); // Compara apenas o rank
    }

    @Test
    void valorCartaDeveRetornarValorCorreto() {
        // Suponha que a vira seja o SEIS de OUROS
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);
        TrucoCard zap = TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS);
    }
}