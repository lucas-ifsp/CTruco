package com.paola.pedro.test;


import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.TrucoCard;
import com.paola.pedro.StrategiesRoundCapucina;
import com.paola.pedro.TrickRoundCapucina;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CapucinaTrickRoundTest {

    private TrickRoundCapucina trickRound;
    private StrategiesRoundCapucina estrategia;
    private TrucoCard vira;

    @BeforeEach
    void setUp() {
        trickRound = new TrickRoundCapucina();
        estrategia = Mockito.mock(StrategiesRoundCapucina.class);
        vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);
    }

    private TrucoCard createCard(CardRank rank) {
        return TrucoCard.of(rank, CardSuit.CLUBS);
    }

    @Test
    void deveContinuarSeEstiloForAgressivo() {
        List<TrucoCard> mao = List.of(createCard(CardRank.SEVEN));
        TrucoCard adversario = createCard(CardRank.FIVE);

        boolean resultado = trickRound.deveContinuarJogandoTerceiraRodada(
                mao, adversario, vira, "agressivo", estrategia
        );

        assertTrue(resultado);
    }

    @Test
    void deveContinuarSeEstiloForSeguroComCartaMelhor() {
        TrucoCard minhaCarta = createCard(CardRank.KING);
        TrucoCard adversario = createCard(CardRank.JACK);

        Mockito.when(estrategia.avaliarCarta(minhaCarta, vira)).thenReturn(9);
        Mockito.when(estrategia.avaliarCarta(adversario, vira)).thenReturn(5);

        boolean resultado = trickRound.deveContinuarJogandoTerceiraRodada(
                List.of(minhaCarta), adversario, vira, "seguro", estrategia
        );

        assertTrue(resultado);
    }

    @Test
    void naoDeveContinuarSeEstiloForSeguroComCartaPior() {
        TrucoCard minhaCarta = createCard(CardRank.FIVE);
        TrucoCard adversario = createCard(CardRank.KING);

        Mockito.when(estrategia.avaliarCarta(minhaCarta, vira)).thenReturn(4);
        Mockito.when(estrategia.avaliarCarta(adversario, vira)).thenReturn(10);

        boolean resultado = trickRound.deveContinuarJogandoTerceiraRodada(
                List.of(minhaCarta), adversario, vira, "seguro", estrategia
        );

        assertFalse(resultado);
    }

    @Test
    void deveContinuarSeEstiloForPressionarComValorProximo() {
        TrucoCard minhaCarta = createCard(CardRank.SIX);
        TrucoCard adversario = createCard(CardRank.SEVEN);

        Mockito.when(estrategia.avaliarCarta(minhaCarta, vira)).thenReturn(6);
        Mockito.when(estrategia.avaliarCarta(adversario, vira)).thenReturn(7);

        boolean resultado = trickRound.deveContinuarJogandoTerceiraRodada(
                List.of(minhaCarta), adversario, vira, "pressionar", estrategia
        );

        assertTrue(resultado);
    }

    @Test
    void naoDeveContinuarSeEstiloForPressionarComValorDistante() {
        TrucoCard minhaCarta = createCard(CardRank.THREE);
        TrucoCard adversario = createCard(CardRank.KING);

        Mockito.when(estrategia.avaliarCarta(minhaCarta, vira)).thenReturn(3);
        Mockito.when(estrategia.avaliarCarta(adversario, vira)).thenReturn(10);

        boolean resultado = trickRound.deveContinuarJogandoTerceiraRodada(
                List.of(minhaCarta), adversario, vira, "pressionar", estrategia
        );

        assertFalse(resultado);
    }

    @Test
    void deveContinuarSeEstiloForNormalEExpectativaAlta() {
        TrucoCard minhaCarta = createCard(CardRank.TWO);
        TrucoCard adversario = createCard(CardRank.THREE);

        Mockito.when(estrategia.avaliarCarta(minhaCarta, vira)).thenReturn(2);
        Mockito.when(estrategia.avaliarCarta(adversario, vira)).thenReturn(5);
        Mockito.when(estrategia.getExpectativa()).thenReturn("alta");

        boolean resultado = trickRound.deveContinuarJogandoTerceiraRodada(
                List.of(minhaCarta), adversario, vira, "normal", estrategia
        );

        assertTrue(resultado);
    }

    @Test
    void naoDeveContinuarSeEstiloForNormalComExpectativaBaixaECartaPior() {
        TrucoCard minhaCarta = createCard(CardRank.TWO);
        TrucoCard adversario = createCard(CardRank.KING);

        Mockito.when(estrategia.avaliarCarta(minhaCarta, vira)).thenReturn(3);
        Mockito.when(estrategia.avaliarCarta(adversario, vira)).thenReturn(10);
        Mockito.when(estrategia.getExpectativa()).thenReturn("normal");

        boolean resultado = trickRound.deveContinuarJogandoTerceiraRodada(
                List.of(minhaCarta), adversario, vira, "normal", estrategia
        );

        assertFalse(resultado);
    }
}
