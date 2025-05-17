package com.antonelli.gibim.degolabot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.List;

public class SecondRound implements Strategy {

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return -1;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();
        boolean venceuPrimeira = BotUtils.didWinFirstRound(intel);
        boolean ehSegundo = BotUtils.isPlayingSecond(intel);

        List<TrucoCard> fracas = cards.stream().filter(c -> c.relativeValue(vira) <= 4).toList();
        List<TrucoCard> medias = cards.stream().filter(c -> c.relativeValue(vira) > 4 && c.relativeValue(vira) < 8).toList();
        List<TrucoCard> boas = cards.stream().filter(c -> c.relativeValue(vira) >= 8 && !c.isManilha(vira)).toList();
        List<TrucoCard> manilhas = cards.stream().filter(c -> c.isManilha(vira)).toList();

        if (venceuPrimeira) {
            if (!ehSegundo) {
                if (!manilhas.isEmpty()) {
                    return CardToPlay.of(cards.stream()
                            .filter(c -> !c.isManilha(vira))
                            .max((a, b) -> Integer.compare(a.relativeValue(vira), b.relativeValue(vira)))
                            .orElse(cards.get(0)));
                }
                if (!medias.isEmpty()) {
                    return CardToPlay.of(medias.get(0));
                }
                if (!fracas.isEmpty()) {
                    return CardToPlay.of(fracas.get(0));
                }
                if (!boas.isEmpty()) {
                    return CardToPlay.of(boas.get(0));
                }
                return CardToPlay.of(cards.get(0));
            }
        } else {
            if (ehSegundo && intel.getOpponentCard().isPresent()) {
                TrucoCard opp = intel.getOpponentCard().get();

                TrucoCard matadora = cards.stream()
                        .filter(c -> c.compareValueTo(opp, vira) > 0)
                        .min((a, b) -> Integer.compare(a.relativeValue(vira), b.relativeValue(vira)))
                        .orElse(null);

                if (matadora != null) return CardToPlay.of(matadora);

                if (!manilhas.isEmpty()) {
                    return CardToPlay.of(manilhas.get(0));
                }

                return CardToPlay.of(cards.stream()
                        .min((a, b) -> Integer.compare(a.relativeValue(vira), b.relativeValue(vira)))
                        .get());
            }
        }

        return CardToPlay.of(cards.stream()
                .min((a, b) -> Integer.compare(a.relativeValue(vira), b.relativeValue(vira)))
                .get());
    }
}
