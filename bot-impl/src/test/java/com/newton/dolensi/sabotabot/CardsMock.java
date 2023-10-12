package com.newton.dolensi.sabotabot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.TrucoCard;

import java.util.List;

public class CardsMock {

    public static List<TrucoCard> cardList() {
        return List.of(
                TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS));
    }

}
