package com.paola.pedro;

import com.bueno.spi.model.TrucoCard;

import java.util.*;

public class StrategiesRoundCapucina {

    private static final int VALOR_FORTE = 12;
    private static final int VALOR_FRACO = 5;

    private boolean blefeAnteriorAceito = false;
    private boolean adversarioFugiuBlefe = false;

    private final List<TrucoCard> cartasAdversario = new ArrayList<>();
    private final Map<TrucoCard, Integer> frequenciaCartasAdversario = new HashMap<>();
    private final Set<TrucoCard> cartasPerigosas = new HashSet<>();

    private String expectativa = "normal";

    // Define o estilo da rodada com base nas vitórias anteriores
    public String definirEstiloRodada(int rodadaAtual, Boolean venceuRodada1, Boolean venceuRodada2) {
        if (rodadaAtual == 2) {
            return Boolean.TRUE.equals(venceuRodada1) ? "seguro" : "agressivo";
        }

        if (rodadaAtual == 3 && venceuRodada1 != null && venceuRodada2 != null && !venceuRodada1.equals(venceuRodada2)) {
            return "pressionar";
        }

        return "normal";
    }

    public String definirEstiloPontuacao(int pontosBot, int pontosAdversario) {
        if (pontosBot == 11 && pontosAdversario == 11) return "defensivo";
        if (pontosBot == 10) return "cauteloso";
        if (pontosAdversario == 11) return "agressivo";
        if (pontosBot == 0) return "ousado";
        if (pontosAdversario == 1) return "blefe";
        return "normal";
    }

    public boolean deveBlefar(List<TrucoCard> mao, int pontosBot, int pontosAdversario, TrucoCard vira) {
        boolean cartasFracas = mao.stream().allMatch(c -> avaliarCarta(c, vira) <= VALOR_FRACO);
        boolean botPerdendo = pontosBot < pontosAdversario;
        boolean adversarioComUmPonto = pontosAdversario == 1;

        return cartasFracas || blefeAnteriorAceito || adversarioFugiuBlefe || botPerdendo || adversarioComUmPonto;
    }

    public void marcarBlefeAceito() {
        blefeAnteriorAceito = true;
    }

    public void marcarAdversarioFugiuDoBlefe() {
        adversarioFugiuBlefe = true;
    }


    public void registrarCartaAdversario(TrucoCard carta, TrucoCard vira) {
        cartasAdversario.add(carta);
        frequenciaCartasAdversario.merge(carta, 1, Integer::sum);

        if (avaliarCarta(carta, vira) >= VALOR_FORTE) {
            expectativa = "alta";
            cartasPerigosas.add(carta);
        }
    }

    public boolean deveEvitarCarta(TrucoCard carta) {
        return cartasPerigosas.contains(carta) || frequenciaCartasAdversario.containsKey(carta);
    }

    public boolean adversarioRepetiuCarta() {
        return frequenciaCartasAdversario.values().stream().anyMatch(freq -> freq > 1);
    }

    public String getExpectativa() {
        return expectativa;
    }

    public TrucoCard decidirJogadaContraCarta(TrucoCard cartaAdversario, List<TrucoCard> mao, TrucoCard vira) {
        int valorAdversario = avaliarCarta(cartaAdversario, vira);

        if (valorAdversario <= VALOR_FRACO) {
            return buscarCartaSuperior(mao, valorAdversario, vira);
        }

        if (valorAdversario >= VALOR_FORTE) {
            return buscarCartaMaisFraca(mao, vira);
        }

        return mao.get(0); // fallback
    }

    public int avaliarCarta(TrucoCard carta, TrucoCard vira) {
        return carta.relativeValue(vira);
    }

    private TrucoCard buscarCartaSuperior(List<TrucoCard> mao, int valorAdversario, TrucoCard vira) {
        return mao.stream()
                .filter(c -> avaliarCarta(c, vira) > valorAdversario)
                .findFirst()
                .orElse(buscarCartaMaisFraca(mao, vira));
    }

    private TrucoCard buscarCartaMaisFraca(List<TrucoCard> mao, TrucoCard vira) {
        return mao.stream()
                .min(Comparator.comparingInt(c -> avaliarCarta(c, vira)))
                .orElseThrow(() -> new IllegalStateException("Não foi possível determinar a menor carta."));
    }

    public void limparMemoriaRodada() {
        cartasAdversario.clear();
        frequenciaCartasAdversario.clear();
        cartasPerigosas.clear();
        expectativa = "normal";
    }

    public void limparEstado() {
        blefeAnteriorAceito = false;
        adversarioFugiuBlefe = false;
        limparMemoriaRodada();
    }
}
