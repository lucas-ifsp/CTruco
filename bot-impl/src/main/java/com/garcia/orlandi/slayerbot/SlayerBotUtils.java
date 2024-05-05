package com.garcia.orlandi.slayerbot;

import com.bueno.spi.model.TrucoCard;

import java.util.List;

public class SlayerBotUtils {

    public List<TrucoCard> getManilhas(List<TrucoCard> cards, TrucoCard vira){
        return cards.stream().filter(card -> card.isManilha(vira)).toList();
    }
}
