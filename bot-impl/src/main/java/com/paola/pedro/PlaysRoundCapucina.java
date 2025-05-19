/*
 * Este arquivo é parte do projeto CTruco.
 *
 * Copyright (C) 2024 PaolaTmpr
 *
 * Este programa é software livre; você pode redistribuí-lo e/ou modificá-lo
 * sob os termos da Licença Pública Geral GNU conforme publicada pela Free Software Foundation,
 * na versão 3 da Licença, ou (a seu critério) qualquer versão posterior.
 *
 * Este programa é distribuído na esperança de que seja útil, mas SEM NENHUMA GARANTIA;
 * sem mesmo a garantia implícita de COMERCIALIZAÇÃO ou ADEQUAÇÃO A UM DETERMINADO FIM.
 * Veja a Licença Pública Geral GNU para mais detalhes.
 *
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa.
 * Se não, veja <https://www.gnu.org/licenses/>.
 */

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
        if (isOnzeAOnze()) return "defensivo";
        if (isBotComDezPontos()) return "cauteloso";
        if (isAdversarioComOnze()) return "agressivo";
        if (isBotComZero()) return "ousado";
        return "normal";
    }

    private boolean isOnzeAOnze() {
        return placarBot == 11 && placarAdversario == 11;
    }

    private boolean isBotComDezPontos() {
        return placarBot == 10;
    }

    private boolean isAdversarioComOnze() {
        return placarAdversario == 11;
    }

    private boolean isBotComZero() {
        return placarBot == 0;
    }

     public String getPosicaoNaRodada() {
        return posicaoNaRodada;
    }

}
