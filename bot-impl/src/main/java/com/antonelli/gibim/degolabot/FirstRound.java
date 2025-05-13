package com.antonelli.gibim.degolabot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.antonelli.gibim.degolabot.BotUtils;

import java.util.List;

public class FirstRound implements Strategy {

    @Override
    public int getRaiseResponse(GameIntel intel) {
        boolean opponentHasCard = BotUtils.isPlayingSecond(intel);
        List<TrucoCard> currentCards = intel.getCards();

        if (opponentHasCard && intel.getOpponentCard().isPresent() &&
                currentCards.stream()
                        .filter(c -> c.compareValueTo(intel.getOpponentCard().get(), intel.getVira()) >= 0)
                        .count() >= 2) {
            return 0;
        }

        if (intel.getOpponentScore() < 6 && BotUtils.countStrongCards(intel) > 0) {
            return 0;
        }

        boolean hasZap = BotUtils.cards(intel).anyMatch(c -> c.isZap(intel.getVira()));
        boolean hasManilha = BotUtils.cards(intel).anyMatch(c -> c.isManilha(intel.getVira()) && !c.isZap(intel.getVira()));
        if (hasZap && hasManilha) return 1;

        if (BotUtils.countManilha(intel) > 0) return 0;

        if (BotUtils.countStrongCards(intel, 8) >= 2) return 0;

        return -1;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if (BotUtils.countManilha(intel) >= 2 || BotUtils.countStrongCards(intel) > 2) return true;

        if (intel.getOpponentCard().isPresent()) {
            TrucoCard opponentCard = intel.getOpponentCard().get();
            boolean canBeat = intel.getCards().stream()
                    .anyMatch(c -> c.compareValueTo(opponentCard, intel.getVira()) > 0);

            if (canBeat || BotUtils.countStrongCards(intel, 7) >= 2) return true;

            return intel.getCards().stream()
                    .filter(c -> c.compareValueTo(opponentCard, intel.getVira()) >= 0)
                    .count() >= 2 && BotUtils.countStrongCards(intel, 8) > 0;
        }

        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        boolean isSecond = BotUtils.isPlayingSecond(intel);
        List<TrucoCard> cartas = intel.getCards();

        long boas = BotUtils.countStrongCards(intel, 8);
        long manilhas = BotUtils.countManilha(intel);
        long ruins = cartas.stream().filter(c -> c.relativeValue(intel.getVira()) <= 4).count();
        long medias = cartas.stream().filter(c -> c.relativeValue(intel.getVira()) > 4 && c.relativeValue(intel.getVira()) < 8).count();

        if (!isSecond) {
            if (ruins == 3) return CardToPlay.of(BotUtils.selectWeakestCard(intel));
            if (medias >= 1 && boas == 0 && manilhas == 0) return CardToPlay.of(BotUtils.selectStrongestCard(intel));
            if (boas >= 1 && manilhas == 0) return CardToPlay.of(BotUtils.selectStrongestCard(intel));
            if (manilhas == 1 && boas >= 1) {
                return CardToPlay.of(
                        cartas.stream()
                                .filter(c -> c.relativeValue(intel.getVira()) > 2)
                                .findFirst()
                                .orElse(BotUtils.selectStrongestCard(intel))
                );
            }
            if (manilhas == 1 && ruins == 2) return CardToPlay.of(BotUtils.cards(intel)
                    .filter(c -> c.isManilha(intel.getVira()))
                    .findFirst().orElse(BotUtils.selectStrongestCard(intel)));
        } else {
            TrucoCard adversario = intel.getOpponentCard().get();

            if (ruins == 3) {
                TrucoCard matadora = cartas.stream()
                        .filter(c -> c.compareValueTo(adversario, intel.getVira()) > 0)
                        .min((a, b) -> Integer.compare(a.relativeValue(intel.getVira()), b.relativeValue(intel.getVira())))
                        .orElse(null);

                if (matadora != null) return CardToPlay.of(matadora);
                return CardToPlay.of(BotUtils.selectWeakestCard(intel));
            }

            if (medias >= 1) {
                TrucoCard matadora = cartas.stream()
                        .filter(c -> c.compareValueTo(adversario, intel.getVira()) > 0)
                        .min((a, b) -> Integer.compare(a.relativeValue(intel.getVira()), b.relativeValue(intel.getVira())))
                        .orElse(null);

                if (matadora != null) return CardToPlay.of(matadora);
                return CardToPlay.of(BotUtils.selectWeakestCard(intel));
            }

            if (boas >= 3) {
                TrucoCard matadora = cartas.stream()
                        .filter(c -> c.compareValueTo(adversario, intel.getVira()) > 0)
                        .min((a, b) -> Integer.compare(a.relativeValue(intel.getVira()), b.relativeValue(intel.getVira())))
                        .orElse(null);

                if (matadora != null) return CardToPlay.of(matadora);
                return CardToPlay.of(BotUtils.selectWeakestCard(intel));
            }

            if (manilhas >= 1) {
                if (boas >= 1) {
                    TrucoCard matadora = cartas.stream()
                            .filter(c -> c.compareValueTo(adversario, intel.getVira()) > 0)
                            .min((a, b) -> Integer.compare(a.relativeValue(intel.getVira()), b.relativeValue(intel.getVira())))
                            .orElse(null);
                    if (matadora != null) return CardToPlay.of(matadora);

                    TrucoCard amarradora = cartas.stream()
                            .filter(c -> c.compareValueTo(adversario, intel.getVira()) == 0)
                            .findFirst()
                            .orElse(null);
                    if (amarradora != null) return CardToPlay.of(amarradora);

                    return CardToPlay.of(cartas.stream()
                            .filter(c -> c.isManilha(intel.getVira()))
                            .findFirst().orElse(BotUtils.selectStrongestCard(intel)));
                }

                if (ruins >= 1) {
                    TrucoCard amarradora = cartas.stream()
                            .filter(c -> c.compareValueTo(adversario, intel.getVira()) == 0)
                            .findFirst()
                            .orElse(null);
                    if (amarradora != null) return CardToPlay.of(amarradora);

                    return CardToPlay.of(cartas.stream()
                            .filter(c -> c.isManilha(intel.getVira()))
                            .findFirst().orElse(BotUtils.selectStrongestCard(intel)));
                }
            }
        }

        return CardToPlay.of(BotUtils.selectStrongestCard(intel));
    }
}
