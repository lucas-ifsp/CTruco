package com.lucas.felipe.newbot;

import com.bueno.spi.model.TrucoCard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DefaultFunctions {
    private List<TrucoCard> roundCards;
    private TrucoCard vira;

    public DefaultFunctions(List<TrucoCard> roundCards, TrucoCard vira) {
        this.roundCards = roundCards;
        this.vira = vira;
    }

    Comparator<TrucoCard> byValue = (card1, card2) ->
            Integer.compare(card1.relativeValue(vira), card2.relativeValue(vira));

    protected List<TrucoCard> sortCards(List<TrucoCard> cards){
        List<TrucoCard> sortedCards = new ArrayList<>(cards);
        sortedCards.sort(byValue);

        return sortedCards;
    }

    protected boolean isPowerfull(List<TrucoCard> ordenedCards){
        if (ordenedCards.size() == 3) {
            return ordenedCards.get(2).relativeValue(vira) >= 9 && ordenedCards.get(1).relativeValue(vira) >= 9;
        } else if (ordenedCards.size() == 2) {
            return ordenedCards.get(1).relativeValue(vira) >= 9 && ordenedCards.get(0).relativeValue(vira) >= 9;
        } else {
            return ordenedCards.get(0).relativeValue(vira) >= 9;
        }
    }

    protected boolean isMedium(List<TrucoCard> ordenedCards){
        if (ordenedCards.size() == 3) {
            return ordenedCards.get(2).relativeValue(vira) >= 7 && ordenedCards.get(1).relativeValue(vira) >= 7;
        } else if (ordenedCards.size() == 2) {
            return ordenedCards.get(1).relativeValue(vira) >= 7 && ordenedCards.get(0).relativeValue(vira) >= 7;
        } else {
            return ordenedCards.get(0).relativeValue(vira) >= 7;
        }
    }
}
