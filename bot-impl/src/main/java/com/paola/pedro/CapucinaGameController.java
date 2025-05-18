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
