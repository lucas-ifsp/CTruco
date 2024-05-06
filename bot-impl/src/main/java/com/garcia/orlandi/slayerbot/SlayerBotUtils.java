package com.garcia.orlandi.slayerbot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.TrucoCard;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SlayerBotUtils {

    public List<TrucoCard> getManilhas(List<TrucoCard> cards, TrucoCard vira){
        return cards.stream().filter(card -> card.isManilha(vira)).toList();
    }

    public TrucoCard playWeakestManilha(List<TrucoCard> manilhas) {
        return manilhas.stream()
                .min(Comparator.comparingInt(card -> card.relativeValue(null)))
                .orElse(null);
    }
}
