package com.newton.dolensi.sabotabot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.TrucoCard;

import java.util.List;

public class IntelMock {

    public static List<TrucoCard> cardList3Cards() {
        return List.of(
                TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS));
    }

    public static List<TrucoCard> cardList2Cards() {
        return List.of(
                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS));
    }

    public static TrucoCard vira5C() {
        return TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS);
    }
}
