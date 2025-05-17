package com.paola.pedro;

import com.bueno.spi.model.CardRank;

import java.util.HashSet;
import java.util.Set;

/**
 * Classe responsável por representar o comportamento do bot durante a rodada.
 */
public class PlaysRoundCapucina {

    private String posicaoNaRodada;
    private final Set<CardRank> cartasAdversario = new HashSet<>();
    private String expectativa = "normal";

    private int placarBot = 0;
    private int placarAdversario = 0;
    private static final String EXPECTATIVA_NORMAL = "normal";
    private static final String EXPECTATIVA_ALTA = "alta";

    public void setPosicaoNaRodada(String posicao) {
        this.posicaoNaRodada = posicao;
    }

    public void registrarCartaAdversario(CardRank carta) {
        cartasAdversario.add(carta);

        if (carta == CardRank.ACE || "ESPADAO".equals(carta.name())) {
            expectativa = EXPECTATIVA_ALTA;
        }
    }


    public Set<CardRank> getCartasAdversario() {
        return new HashSet<>(cartasAdversario);
    }


    public String getExpectativaRodada() {
        return expectativa;
    }


    public void setPlacar(int placarBot, int placarAdversario) {
        this.placarBot = placarBot;
        this.placarAdversario = placarAdversario;
    }

    public String getEstiloDeJogo() {
        if (placarBot == 11 && placarAdversario == 11) return "defensivo";
        if (placarBot == 10) return "cauteloso";
        if (placarAdversario == 11) return "agressivo";
        if (placarBot == 0) return "ousado";
        return "normal";
    }

    public String getPosicaoNaRodada() {
        return posicaoNaRodada;
    }
}
