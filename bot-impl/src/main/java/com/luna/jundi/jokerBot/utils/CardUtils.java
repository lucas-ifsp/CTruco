package com.luna.jundi.jokerBot.utils;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.luna.jundi.jokerBot.HandClassification;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.luna.jundi.jokerBot.HandClassification.*;
import static com.luna.jundi.jokerBot.utils.HandUtils.*;

public class CardUtils {
    //Transform these methods in to functions

    public static HandClassification getHandClassification(GameIntel intel){
        double average = averageRelativeValueOfCards(intel);
        if ( average >= 10 ) return EXCELLENT;
        if ( average >= 8  ) return GREAT;
        if ( average >= 6 || getNumberOfManilhas(intel.getCards(), intel.getVira()) > 0  ) return GOOD;
        if ( average >= 4  ) return NOT_BAD;
        return BAD;
    }
    public static double averageRelativeValueOfCards(GameIntel intel) {
        return getMyCardsSorted(intel).stream()
                .mapToDouble(card -> card.relativeValue(intel.getVira()))
                .average().orElse(0.00);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    //////                          GET CARDS!                                                 //////
    /////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the list of cards sorted from largest to smallest
     * relative value of the card.
     *        //removi a condição que apenas retorna quando tem apenas uma carta,
     *         //sem precisar ordenar
     *         //Refatorar : retornar um new Arraylist adicionando as cartas? <= pensar
     */
    public static List<TrucoCard> getMyCardsSorted(GameIntel intel){
        return intel.getCards().stream()
                .sorted(Comparator.comparingInt(card -> ((TrucoCard)card).relativeValue(intel.getVira())).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Returns the best card in the hand
     */
    public static CardToPlay getBestCard(GameIntel intel){
        return CardToPlay.of(getMyCardsSorted(intel).get(0));
    }

    /**
     * Returns the medium card in the hand
     * only valid when has 3 cards in the hand
     * => pensar em retornar a carta maior
     */
    public static CardToPlay getMediumCard(GameIntel intel){
        List<TrucoCard> myCardsSorted = getMyCardsSorted(intel);
        int numberOfCards = myCardsSorted.size();
        return CardToPlay.of(numberOfCards == 3? myCardsSorted.get(numberOfCards - 2) : myCardsSorted.get(numberOfCards -1));
    }

    /**
 * Antes de chamá-lo verificar se possui a carta igual
 * através do método boolean hasTheSameCardInHand(GameIntel intel)
 * Método que retorna uma carta igual se tiver
 */
public static CardToPlay getTheSameCardInHand(GameIntel intel) {
    TrucoCard vira = intel.getVira();
    List<TrucoCard> myCardsSorted = getMyCardsSorted(intel);
    int size = myCardsSorted.size() -1;
    Optional<TrucoCard> OptionalOpponentCard = intel.getOpponentCard();
    if (OptionalOpponentCard.isPresent()){
        TrucoCard opponentCard = OptionalOpponentCard.get();
        return CardToPlay.of(getMyCardsSorted(intel).stream()
               .filter(card -> card.relativeValue(vira) == opponentCard.relativeValue(vira))
               .findFirst()
               .orElseGet(()-> myCardsSorted.get(size))); //mudar para getWorstCard depois
    }
    return getWorstCard(intel);
}

    //Jogar uma exception se não tiver cartas
    public static CardToPlay getWorstCard(GameIntel intel){
        List<TrucoCard> myCardsSorted = getMyCardsSorted(intel);
        return CardToPlay.of(myCardsSorted.get(myCardsSorted.size() - 1));
    }

    //descartar uma carta? talvez não implementar para não tomar exception
    public static CardToPlay drewCard(GameIntel intel){
        //implement
        return null;
    }

}
