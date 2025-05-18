package com.paola.pedro.test;

import com.paola.pedro.StartRoundCapucina;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.bueno.spi.model.CardRank;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CapucinaStartTest {

    private StartRoundCapucina bot;

    @BeforeEach
    void setUp() {
        bot = new StartRoundCapucina();
    }

    @Test
    void deveArmazenarInformacoesIniciaisQuandoPartidaInicia() {
        bot.iniciarPartida("bot", 0, 0);
        //assertTrue(bot.iniciarPartida(jogadorInicial, placarBot, placarAdversario));
    }

    @Test
    @DisplayName("Dado que o bot iniciou uma nova rodada, então deve receber 3 cartas.")
    void deveReceber3CartasNaNovaRodada() {
        bot.iniciarRodada(List.of(CardRank.FOUR, CardRank.QUEEN, CardRank.SEVEN));
        assertEquals(3, bot.getMao().size());
    }

    @Test
    @DisplayName("Dado que o bot recebeu menos de 3 cartas, então deve lançar um erro.")
    void deveLancarErroSeReceberMenosDe3Cartas() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                bot.iniciarRodada(List.of(CardRank.FOUR, CardRank.QUEEN))
        );
        assertEquals("Deve receber exatamente 3 cartas", exception.getMessage());
    }

    @Test
    @DisplayName("Dado que a rodada reiniciou, então deve limpar o estado da rodada anterior.")
    void deveLimparEstadoDaRodadaAnterior() {
        bot.marcarJogada(CardRank.QUEEN);
        bot.iniciarRodada(List.of(CardRank.FOUR, CardRank.QUEEN, CardRank.SEVEN));
        assertFalse(bot.foiCartaJogada(CardRank.QUEEN));
    }

    @Test
    @DisplayName("Dado que é a primeira rodada da partida, então o bot deve jogar de forma segura.")
    void deveJogarDeFormaSeguraNaPrimeiraRodada() {
        bot.iniciarPartida("bot", 0, 0);
        bot.iniciarRodada(List.of(CardRank.FOUR, CardRank.QUEEN, CardRank.SEVEN));
        CardRank jogada = bot.jogar();
        assertFalse(bot.getMao().contains(jogada));
    }

    @Test
    void deveLancarExcecaoSemEstadoCarregado() {
        Exception exception = assertThrows(IllegalStateException.class, bot::jogar);
        assertEquals("Estado do bot não iniciado", exception.getMessage());
    }

    @Test
    void deveTratarJogadaInvalidaComSeguranca() {
        assertDoesNotThrow(() -> bot.processarJogadaInvalida(CardRank.JACK.name()));
    }

    @Test
    void deveRemoverCartaAposJogar() {
        bot.iniciarPartida("bot", 0, 0);
        bot.iniciarRodada(List.of(CardRank.FOUR, CardRank.QUEEN, CardRank.SEVEN));
        CardRank jogada = bot.jogar();
        assertFalse(bot.getMao().contains(jogada));
    }

    @Test
    void deveMarcarRecusaTruco() {
        bot.recusarTruco();
        assertTrue(bot.isTrucoRecusado());
    }

    @Test
    void deveZerarEstadoEmNovaPartida() {
        bot.iniciarRodada(List.of(CardRank.FOUR, CardRank.QUEEN, CardRank.SEVEN));
        bot.novaPartida();
        assertTrue(bot.getMao().isEmpty());
    }

    @Test
    void deveBlefarComCartasFracas() {
        bot.iniciarRodada(List.of(CardRank.FOUR, CardRank.FIVE, CardRank.SIX));
        assertTrue(bot.deveBlefar());
    }

    @Test
    void deveRepetirBlefeSeFoiAceitoAntes() {
        bot.marcarBlefeAceito();
        assertTrue(bot.deveBlefar());
    }

    @Test
    void deveEsconderCartaForte() {
        bot.iniciarPartida("bot", 0, 0); // Adicione esta linha
        bot.iniciarRodada(List.of(CardRank.THREE, CardRank.FOUR, CardRank.ACE));
        CardRank jogada = bot.jogar();
        assertNotEquals(CardRank.ACE, jogada);
    }

    @Test
    void deveBlefarMaisQuandoEstiverPerdendo() {
        bot.setPlacar(5, 10); // bot perdendo
        assertTrue(bot.deveBlefar());
    }

    @Test
    void devePressionarSeAdversarioFugiuDoBlefe() {
        bot.marcarAdversarioFugiu();
        assertEquals("agressiva", bot.getEstrategia());
    }

    @Test
    void deveTratarJogadaInvalidaSemExcecao() {
        StartRoundCapucina bot = new StartRoundCapucina();
        assertDoesNotThrow(() -> bot.processarJogadaInvalida("JOGADA_INVALIDA"));
    }
}