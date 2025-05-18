package com.paola.pedro;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.*;

public class StartRoundCapucina implements BotServiceProvider {


    private static final int VALOR_CARTA_FORTE = 9;

    private boolean partidaIniciada = false;
    private final List<CardRank> mao = new ArrayList<>();
    private final Set<CardRank> cartasJogadas = new HashSet<>();
    private final List<CardRank> cartasAdversario = new ArrayList<>();
    private final Map<Integer, Boolean> resultadoRodadas = new HashMap<>();

    private boolean trucoRecusado = false;
    private boolean blefeAceito = false;
    private boolean adversarioFugiu = false;

    int placarBot = 0;
    int placarAdversario = 0;

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        long cartasFortes = intel.getCards().stream()
                .filter(card -> card.relativeValue(intel.getVira()) >= 8)
                .count();

        return cartasFortes >= 2 && intel.getMyScore() < 11;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> cartas = intel.getCards();
        if (cartas.isEmpty()) return null;
        TrucoCard menor = cartas.stream()
                .min(Comparator.comparingInt(c -> c.relativeValue(intel.getVira())))
                .orElse(cartas.get(0));
        return CardToPlay.of(menor);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }


    public void iniciarPartida(String jogadorInicial, int placarBot, int placarAdv) {
        this.partidaIniciada = true;
        this.placarBot = placarBot;
        this.placarAdversario = placarAdv;
    }

    public void iniciarRodada(List<CardRank> cartas) {
        if (cartas == null || cartas.size() != 3) {
            throw new IllegalArgumentException("Deve receber exatamente 3 cartas");
        }
        mao.clear();
        mao.addAll(cartas);
        cartasJogadas.clear();
        cartasAdversario.clear();
    }

    public List<CardRank> getMao() {
        return new ArrayList<>(mao);
    }

    public void registrarCartaAdversario(CardRank carta) {
        cartasAdversario.add(carta);
    }

    public void marcarJogada(CardRank carta) {
        cartasJogadas.add(carta);
        mao.remove(carta);
    }

    public boolean foiCartaJogada(CardRank carta) {
        return cartasJogadas.contains(carta);
    }

    public void processarJogadaInvalida(String jogada) {
        // Nenhuma ação por enquanto — apenas garante que não lança exceção
    }

    public void recusarTruco() {
        trucoRecusado = true;
    }

    public boolean isTrucoRecusado() {
        return trucoRecusado;
    }

    public void novaPartida() {
        partidaIniciada = false;
        mao.clear();
        cartasJogadas.clear();
        cartasAdversario.clear();
        resultadoRodadas.clear();
        trucoRecusado = false;
        blefeAceito = false;
        adversarioFugiu = false;
    }

    public boolean deveBlefar() {
        boolean soCartasFracas = mao.stream().allMatch(c ->
                EnumSet.of(CardRank.TWO, CardRank.THREE, CardRank.FOUR, CardRank.FIVE, CardRank.SIX).contains(c));
        return soCartasFracas || blefeAceito || (placarBot < placarAdversario);
    }

    public void marcarBlefeAceito() {
        blefeAceito = true;
    }

    public void marcarAdversarioFugiu() {
        adversarioFugiu = true;
    }

    public String getEstrategia() {
        return adversarioFugiu ? "agressiva" : "normal";
    }

    public void setPlacar(int bot, int adv) {
        this.placarBot = bot;
        this.placarAdversario = adv;
    }

    public int valorCarta(CardRank carta) {
        return carta.value();
    }

    public void registrarResultadoRodada(int rodada, boolean venceu) {
        resultadoRodadas.put(rodada, venceu);
    }

    public String getEstiloDeRodada(int rodada) {
        Boolean venceuPrimeira = resultadoRodadas.get(1);
        Boolean venceuSegunda = resultadoRodadas.get(2);

        if (rodada == 2 && venceuPrimeira != null) {
            return venceuPrimeira ? "seguro" : "agressivo";
        }

        if (rodada == 3 && Boolean.FALSE.equals(venceuPrimeira) && Boolean.TRUE.equals(venceuSegunda)) {
            return "pressionar";
        }

        return "normal";
    }


    public CardRank jogar() {
        if (!partidaIniciada || mao.isEmpty()) {
            throw new IllegalStateException("Estado do bot não iniciado");
        }

        CardRank cartaParaJogar;
        if (!"agressiva".equals(getEstrategia())) {
            cartaParaJogar = mao.stream()
                    .filter(c -> c.value() < VALOR_CARTA_FORTE)
                    .min(Comparator.comparingInt(CardRank::value))
                    .orElse(mao.get(0));
        } else {
            cartaParaJogar = mao.stream()
                    .max(Comparator.comparingInt(CardRank::value))
                    .orElse(mao.get(0));
        }

        marcarJogada(cartaParaJogar);
        return cartaParaJogar;
    }



}
