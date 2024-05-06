package com.garcia.orlandi.slayerbot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.TrucoCard;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SlayerBotUtils {

    public List<TrucoCard> getManilhas(List<TrucoCard> cards, TrucoCard vira) {
        CardRank nextRank = vira.getRank().next();
        return cards.stream()
                .filter(card -> card.getRank() == nextRank)
                .collect(Collectors.toList());
    }

    public CardToPlay playWeakestManilha(List<TrucoCard> manilhas) {
        if (manilhas.isEmpty()) {
            throw new IllegalStateException("No manilhas found when expected.");
        }
        return manilhas.stream()
                .min(Comparator.comparing(TrucoCard::getRank))
                .map(CardToPlay::of)
                .orElseThrow(() -> new IllegalStateException("Failed to find the weakest manilha"));
    }
}
