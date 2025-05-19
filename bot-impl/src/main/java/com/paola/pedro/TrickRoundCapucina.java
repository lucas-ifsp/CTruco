package com.paola.pedro;

import com.bueno.spi.model.TrucoCard;

import java.util.Comparator;
import java.util.List;

public class TrickRoundCapucina {

    // Decide se vale a pena continuar na 3ª rodada ou desistir
    public boolean deveContinuarJogandoTerceiraRodada(
            List<TrucoCard> maoRestante,
            TrucoCard cartaAdversario,
            TrucoCard vira,
            String estiloRodada,
            StrategiesRoundCapucina estrategia
    ) {
        int valorAdversario = estrategia.avaliarCarta(cartaAdversario, vira);
        TrucoCard melhorCartaBot = obterMelhorCarta(maoRestante, vira, estrategia);
        int valorMelhorBot = estrategia.avaliarCarta(melhorCartaBot, vira);

        // Estratégias de decisão baseadas no estilo
        switch (estiloRodada.toLowerCase()) {
            case "agressivo":
                return true; // continua mesmo com risco
            case "seguro":
                return valorMelhorBot > valorAdversario;
            case "pressionar":
                return valorMelhorBot >= valorAdversario - 1;
            case "defensivo":
                return valorMelhorBot >= valorAdversario;
            default: // estilo "normal"
                return valorMelhorBot > valorAdversario || estrategia.getExpectativa().equals("alta");
        }
    }

    private TrucoCard obterMelhorCarta(List<TrucoCard> mao, TrucoCard vira, StrategiesRoundCapucina estrategia) {
        return mao.stream()
                .max(Comparator.comparingInt(c -> estrategia.avaliarCarta(c, vira)))
                .orElse(mao.get(0));
    }
}
