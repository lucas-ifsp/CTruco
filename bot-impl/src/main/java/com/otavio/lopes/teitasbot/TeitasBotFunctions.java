package com.otavio.lopes.teitasbot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.TrucoCard;


import java.util.List;

public class TeitasBotFunctions {
    static Boolean hasManilha(List<TrucoCard> cards, TrucoCard vira) {
        return cards.stream().anyMatch(card -> card.isManilha(vira));
    }
    static Boolean hasStrongHand(List<TrucoCard> cards, TrucoCard vira){
        return cards.stream().anyMatch(card -> card.getRank() == CardRank.THREE);
    }


}
