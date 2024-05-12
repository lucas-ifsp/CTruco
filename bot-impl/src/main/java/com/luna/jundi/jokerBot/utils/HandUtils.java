package com.luna.jundi.jokerBot.utils;

import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.List;
import java.util.Optional;

import static com.bueno.spi.model.GameIntel.RoundResult.DREW;
import static com.bueno.spi.model.GameIntel.RoundResult.WON;
import static com.luna.jundi.jokerBot.utils.CardUtils.*;
import static com.luna.jundi.jokerBot.utils.RoundUtils.*;
import static com.luna.jundi.jokerBot.utils.RoundUtils.isStartOfRound;

public class HandUtils {
    //Transform these methods in to functions
    public static boolean isFirstToPlay(GameIntel intel){
        return getNumberOfPlayedCards(intel) == 0;
    }

    public static boolean isWinningHand(GameIntel intel){
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();
        int roundNumber = getRoundNumber(intel);
        //considerando: round 0, round 1, round 2

        //se está empatado não está ganhando
        if (isTiedHand(intel)) return false;
        //na primeira rodada ninguém está ganhando
        if (isStartOfRound(intel)) return false;
        //na segunda rodada, e ganhou na primeira
        //comentei pq talvez não precisa verificar:
        //if (roundNumber == 1 && WON.equals(roundResults.get(0))) return true;
        return true;
    }

    public static boolean isTiedHand(GameIntel intel){
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();
        int roundNumber = getRoundNumber(intel);
        //considerando: round 0, round 1, round 2

        //esta no começo do primeiro round
        if (isStartOfRound(intel)) return false;

        //se está no segundo round e empatou no primeiro round
        if (roundNumber == 1 && roundResults.get(0).equals(DREW)) return true;

        //se está no terceiro round e empatou no primeiro e no segundo round
        if (roundNumber == 2 && roundResults.get(1).equals(DREW)) return true;

        //com três empates não verifico, pois a mão acabou
        //no inicio da mão não verifico, pois está no começo da rodada
        return false;
    }

    public static boolean isLoosingHand(GameIntel intel){
        //se não está ganhando e não está empatado, está perdendo a mão
        return (!(isWinningHand(intel) || isTiedHand(intel) ||  isStartOfRound(intel)));
    }

    public static boolean hasTheSameCardInHand(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        List<Integer> listRelativeValuesOfMyCards = getMyCardsSorted(intel).stream()
                .map(card -> card.relativeValue(vira))
                .toList();

        int opponentCardRelativeValue = intel.getOpponentCard()
                .map(card -> card.relativeValue(vira))
                .orElse(0);

        if (opponentCardRelativeValue == 0) return false;

        return listRelativeValuesOfMyCards.contains(opponentCardRelativeValue);
    }

    public static boolean hasABiggerCardInHand(GameIntel intel){
        //tentar refatorar:
        TrucoCard vira = intel.getVira();
        Optional<Integer> opponentCard = intel.getOpponentCard().map(card -> card.relativeValue(vira));
        int opponentCardValue = opponentCard.orElse(0);

        if (opponentCardValue == 0) return true;
        return getMyCardsSorted(intel)
                .stream()
                .filter(card -> card.relativeValue(vira) > opponentCardValue)
                .anyMatch(card -> true);
    }

    public static long getNumberOfManilhas(List<TrucoCard> trucoCards, TrucoCard vira){
        return trucoCards.stream()
                .filter(card -> card.isManilha(vira))
                .count();
    }

    public static int getNumberOfPlayedCards(GameIntel intel){
        return intel.getOpenCards().size() - 1;
    }

    public static boolean canRaiseHand(GameIntel intel){
        if (isFirstToPlay(intel) && !isStartOfRound(intel) && isWinningHand(intel)) return true;
        if (!isFirstToPlay(intel) && hasABiggerCardInHand(intel) && isTiedHand(intel)) return true;
        return  false;
    }
}
