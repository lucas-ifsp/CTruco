package com.silvabrufato.impl.silvabrufatobot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

public class Utilities {

    public static int countManilhas(GameIntel gameIntel) {
        int count = 0;
        for(TrucoCard card : gameIntel.getCards()) 
            if(card.isManilha(gameIntel.getVira())) count++;
        return count;
    }

    public static int countCardsEqualOrHigherThanAce(GameIntel gameIntel) {
        int count = 0;
        for(TrucoCard card : gameIntel.getCards()) 
            if(card.getRank().value() >= CardRank.ACE.value()) count++;
        return count;
    }

    
}
