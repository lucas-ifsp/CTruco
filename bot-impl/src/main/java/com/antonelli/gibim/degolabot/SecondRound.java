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

        long fracas = cards.stream().filter(c -> c.relativeValue(vira) <= 4).count();
        long medias = cards.stream().filter(c -> c.relativeValue(vira) > 4 && c.relativeValue(vira) < 8).count();
        long boas = cards.stream().filter(c -> c.relativeValue(vira) >= 8 && !c.isManilha(vira)).count();
        long manilhas = cards.stream().filter(c -> c.isManilha(vira)).count();

        if (venceuPrimeira) {
            if (!ehSegundo) {
                if (manilhas >= 1) {
                    return CardToPlay.of(cards.stream()
                            .filter(c -> !c.isManilha(vira))
                            .max((a, b) -> Integer.compare(a.relativeValue(vira), b.relativeValue(vira)))
                            .orElse(cards.stream().findFirst().get()));
                }
                if (boas > 0) {
                    return CardToPlay.of(cards.stream()
                            .filter(c -> !c.equals(cards.stream()
                                    .max((a, b) -> Integer.compare(a.relativeValue(vira), b.relativeValue(vira))).get()))
                            .findFirst()
                            .orElse(cards.stream().findFirst().get()));
                }
                return CardToPlay.of(cards.stream()
                        .min((a, b) -> Integer.compare(a.relativeValue(vira), b.relativeValue(vira)))
                        .get());
            }
        } else {
            if (ehSegundo && intel.getOpponentCard().isPresent()) {
                TrucoCard opp = intel.getOpponentCard().get();

                TrucoCard matadora = cards.stream()
                        .filter(c -> c.compareValueTo(opp, vira) > 0)
                        .min((a, b) -> Integer.compare(a.relativeValue(vira), b.relativeValue(vira)))
                        .orElse(null);

                if (matadora != null) return CardToPlay.of(matadora);

                if (manilhas > 0) {
                    return CardToPlay.of(cards.stream()
                            .filter(c -> c.isManilha(vira))
                            .findFirst()
                            .orElse(cards.get(0)));
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
