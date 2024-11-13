package com.rennan.podecorrerpatinho;

import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.List;

public class PCPUtils {

    public static boolean hasCasalMaior(TrucoCard vira, List<TrucoCard> myHand) {
        return myHand.stream().anyMatch(card -> card.isZap(vira))
                && myHand.stream().anyMatch(card -> card.isCopas(vira));
    }

    public static boolean hasZap(TrucoCard vira, List<TrucoCard> myHand) {
        return myHand.stream().anyMatch(card -> card.isZap(vira));
    }

    public static boolean hasCopas(TrucoCard vira, List<TrucoCard> myHand) {
        return myHand.stream().anyMatch(card -> card.isCopas(vira));
    }

    public static boolean hasManilha(TrucoCard vira, List<TrucoCard> myHand) {
        return myHand.stream().anyMatch(card -> card.isManilha(vira));
    }

    public static TrucoCard getStrongest(TrucoCard vira, List<TrucoCard> myHand) {
        if (myHand == null || myHand.isEmpty()) {
            return null;
        }

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
        for (TrucoCard card : myHand) {
            if (card.compareValueTo(weakest, vira) < 0) {
                weakest = card;
            }
        }
        return weakest;
    }

    public static int countManilhas(TrucoCard vira, List<TrucoCard> myHand) {
        return (int) myHand.stream().filter(card -> card.isManilha(vira)).count();
    }

    public static int handStrength(TrucoCard vira, List<TrucoCard> myHand) {
        int strength = 0;
        int numManilhas = countManilhas(vira, myHand);
        boolean hasCasalMaior = hasCasalMaior(vira, myHand);

        if (hasCasalMaior) {
            strength += 30;
        }

        strength += numManilhas * 15;

        strength += myHand.stream()
                .mapToInt(card -> card.relativeValue(vira))
                .sum();

        return strength;
    }

    public static boolean hasManilhaHigherThanValue(TrucoCard vira, List<TrucoCard> myHand, int minValue) {
        return myHand.stream().anyMatch(card -> card.isManilha(vira) && card.relativeValue(vira) >= minValue);
    }

    public static boolean verifyIfHasManilhaHigherThanSpadesAndOtherCardHigherThanValue(TrucoCard vira,
            List<TrucoCard> myHand, int minValue) {
        int manilhaValue = 0;
        int cardValue = 0;
        for (TrucoCard card : myHand) {
            if (card.isManilha(vira)) {
                manilhaValue = card.relativeValue(vira);
            } else {
                if (cardValue < card.relativeValue(vira)) {
                    cardValue = card.relativeValue(vira);
                }
            }
        }
        return manilhaValue >= 11 && cardValue >= minValue;
    }

    public static boolean verifyIfHasManilhaAndOtherCardEqualOrHigherThanValue(TrucoCard vira, List<TrucoCard> myHand,
            int minValue) {
        int cardValue = 0;
        boolean hasManilha = hasManilha(vira, myHand);
        for (TrucoCard card : myHand) {
            if (!card.isManilha(vira)) {
                cardValue = card.relativeValue(vira);
            }
        }
        return hasManilha && cardValue >= minValue;
    }

    public static int numManilhasHigh(TrucoCard vira, List<TrucoCard> myHand, int minValue) {
        return (int) myHand.stream()
                .filter(card -> card.isManilha(vira) && card.relativeValue(vira) >= minValue)
                .count();
    }

    public static TrucoCard whenRespondingToOpponent(TrucoCard vira, List<TrucoCard> myHand, TrucoCard opponentCard,
            List<GameIntel.RoundResult> roundResults) {
        TrucoCard strongestCard = getStrongest(vira, myHand);
        TrucoCard weakestCard = getWeakest(vira, myHand);

        double opponentTendency = calculateOpponentTendency(roundResults);

        if (opponentTendency > 0.7) {
            return strongestCard;
        } else {
            return weakestCard;
        }
    }

    private static double calculateOpponentTendency(List<GameIntel.RoundResult> roundResults) {
        long strongPlays = roundResults.stream().filter(result -> result == GameIntel.RoundResult.WON).count();
        return strongPlays / (double) roundResults.size();
    }

}