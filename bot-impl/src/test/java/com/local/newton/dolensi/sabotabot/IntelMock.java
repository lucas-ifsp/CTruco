package com.local.newton.dolensi.sabotabot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel.*;
import com.bueno.spi.model.TrucoCard;

import java.util.List;

public class IntelMock {

    public static List<TrucoCard> cardListAC4C3H() {
        return List.of(
                TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS));
    }

    public static List<TrucoCard> cardListAD2CAH() {
        return List.of(
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                TrucoCard.of(CardRank.ACE, CardSuit.HEARTS));
    }

    public static List<TrucoCard> cardListAD7CKH() {
        return List.of(
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                TrucoCard.of(CardRank.KING, CardSuit.HEARTS));
    }

    public static List<TrucoCard> cardList3D7C2H() {
        return List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                TrucoCard.of(CardRank.TWO, CardSuit.HEARTS));
    }

    public static List<TrucoCard> cardList3DACAH() {
        return List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.ACE, CardSuit.HEARTS));
    }

    public static List<TrucoCard> cardList3D5CQH() {
        return List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS));
    }

    public static List<TrucoCard> cardList4C3D() {
        return List.of(
                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS));
    }

    public static List<TrucoCard> cardList2H() {
        return List.of(TrucoCard.of(CardRank.TWO, CardSuit.HEARTS));
    }

    public static TrucoCard vira5C() { return TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS); }
    public static TrucoCard vira6C() { return TrucoCard.of(CardRank.SIX, CardSuit.CLUBS); }
    public static TrucoCard viraKC() { return TrucoCard.of(CardRank.KING, CardSuit.CLUBS); }
    public static TrucoCard vira2H() { return TrucoCard.of(CardRank.TWO, CardSuit.HEARTS); }
    public static TrucoCard vira3C() { return TrucoCard.of(CardRank.THREE, CardSuit.CLUBS); }

    public static List<RoundResult> roundResultDrewIn1st() {
        return List.of(RoundResult.DREW);
    }
    public static List<RoundResult> roundResultWonIn1st() {
        return List.of(RoundResult.WON);
    }
}
