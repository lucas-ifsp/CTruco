package com.rennan.podecorrerpatinho;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.TrucoCard;

import java.util.List;

public class PCPUtils {
    public static boolean hasCasalMaior(TrucoCard vira, List<TrucoCard> myHand){
        return myHand.stream().anyMatch(card -> card.isZap(vira)) && myHand.stream().anyMatch(card -> card.isCopas(vira));
    }

    public static boolean hasCasalMenor(TrucoCard vira, List<TrucoCard> myHand){
        return myHand.stream().anyMatch(card -> card.isOuros(vira)) && myHand.stream().anyMatch(card -> card.isEspadilha(vira));
    }

    public static boolean hasCasalPreto(TrucoCard vira, List<TrucoCard> myHand){
        return myHand.stream().anyMatch(card -> card.isZap(vira)) && myHand.stream().anyMatch((card -> card.isEspadilha(vira)));
    }

    public static boolean hasCasalVermelho(TrucoCard vira, List<TrucoCard> myHand){
        return myHand.stream().anyMatch(card -> card.isCopas(vira)) && myHand.stream().anyMatch((card -> card.isOuros(vira)));
    }

    public static boolean hasZapOuros(TrucoCard vira, List<TrucoCard> myHand){
        return myHand.stream().anyMatch(card -> card.isZap(vira)) && myHand.stream().anyMatch((card -> card.isOuros(vira)));
    }

    public static boolean hasCopasEspadilha(TrucoCard vira, List<TrucoCard> myHand){
        return myHand.stream().anyMatch(card -> card.isCopas(vira)) && myHand.stream().anyMatch((card -> card.isEspadilha(vira)));
    }

    public static boolean hasZap(TrucoCard vira, List<TrucoCard> myHand){
        return myHand.stream().anyMatch(card -> card.isZap(vira));
    }

    public static boolean hasCopas(TrucoCard vira, List<TrucoCard> myHand){
        return myHand.stream().anyMatch(card -> card.isCopas(vira));
    }

    public static boolean hasEspada(TrucoCard vira, List<TrucoCard> myHand){
        return myHand.stream().anyMatch(card -> card.isEspadilha(vira));
    }

    public static boolean hasOuros(TrucoCard vira, List<TrucoCard> myHand){
        return myHand.stream().anyMatch(card -> card.isOuros(vira));
    }

    public static boolean hasManilha(TrucoCard vira, List<TrucoCard> myHand){
        return myHand.stream().anyMatch(card -> card.isManilha(vira));
    }

    public static boolean hasThree(TrucoCard vira, List<TrucoCard> myHand){
        return myHand.stream().anyMatch(card -> card.getRank() == CardRank.THREE);
    }

    public static boolean zapAlreadyPlayed(TrucoCard vira, List<TrucoCard> gameHand){
        return gameHand.stream().anyMatch(card -> card.isZap(vira));
    }

    public static boolean zapAndCopasAlreadyPlayed(TrucoCard vira, List<TrucoCard> gameHand){
        return gameHand.stream().anyMatch(card -> card.isCopas(vira))
                && gameHand.stream().anyMatch(card -> card.isZap(vira));
    }

    public static boolean zapCopasAndEspadaAlreadyPlayed(TrucoCard vira, List<TrucoCard> gameHand){
        return gameHand.stream().anyMatch(card -> card.isCopas(vira))
                && gameHand.stream().anyMatch(card -> card.isEspadilha(vira))
                && gameHand.stream().anyMatch(card -> card.isZap(vira));
    }

    public static TrucoCard getStrongest(TrucoCard vira, List<TrucoCard> myHand) {

        TrucoCard strongest = myHand.get(0);
    
        for (TrucoCard card : myHand) {
            if (card.compareValueTo(strongest, vira) > 0) {
                strongest = card;
            }
        }
    
        return strongest;
    }

    public static TrucoCard getWeakest(TrucoCard vira, List<TrucoCard> myHand) {
        TrucoCard weakest = myHand.get(0);
        for (TrucoCard card : myHand)
        {
            if (card.compareValueTo(weakest, vira) < 0)
            {
                weakest = card;
            }
        }
        return weakest;

    }
}