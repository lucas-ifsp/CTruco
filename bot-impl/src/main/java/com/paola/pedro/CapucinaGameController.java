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
import com.bueno.spi.model.TrucoCard;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador principal do jogo Capucina.
 * Este controlador orquestra as interações entre as diferentes partes do jogo,
 * incluindo o início da partida, o registro de cartas jogadas e a decisão de estratégias.
 */

public class CapucinaGameController {

    private final StartRoundCapucina startRound = new StartRoundCapucina();
    private final PlaysRoundCapucina playsRound = new PlaysRoundCapucina();
    private final StrategiesRoundCapucina strategyRound = new StrategiesRoundCapucina();
    TrickRoundCapucina trick = new TrickRoundCapucina();

    private TrucoCard vira;
    private List<TrucoCard> maoAtual;

    /**
     * Inicia uma nova partida com o jogador inicial e os placares iniciais.
     *
     * @param jogadorInicial  O nome do jogador inicial.
     * @param placarBot      O placar inicial do bot.
     * @param placarAdversario O placar inicial do adversário.
     * @param vira           A carta que será virada na mesa.
     */

    public void iniciarPartida(String jogadorInicial, int placarBot, int placarAdversario, TrucoCard vira) {
        this.vira = vira;
        startRound.iniciarPartida(jogadorInicial, placarBot, placarAdversario);
        playsRound.setPlacar(placarBot, placarAdversario);
    }


    public void iniciarRodada(List<TrucoCard> mao) {
        this.maoAtual = mao;
        List<CardRank> ranks = mao.stream()
                .map(TrucoCard::getRank)
                .collect(Collectors.toList());

        startRound.iniciarRodada(ranks);
        // Se necessário, playsRound e strategyRound também podem receber a mão completa
    }

    public void registrarCartaAdversario(CardRank cartaRank, TrucoCard cartaCompleta) {
        startRound.registrarCartaAdversario(cartaRank);
        playsRound.registrarCartaAdversario(cartaRank);
        strategyRound.registrarCartaAdversario(cartaCompleta, vira);

    }

    public boolean deveBlefar() {
        return strategyRound.deveBlefar(maoAtual, startRound.placarBot, startRound.placarAdversario, vira);
    }

    public String getEstiloDeJogo() {
        return playsRound.getEstiloDeJogo();
    }

    public String getExpectativaRodada() {
        return strategyRound.getExpectativa();
    }

}
