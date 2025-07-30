package com.local.contiero.lemes.atrasabot.services.utils;

import com.bueno.spi.model.TrucoCard;

import java.util.List;

public class MyCards {

    List<TrucoCard> myHand;
    List<Integer> myHandValuesSorted;
    TrucoCard vira;


    public MyCards(List<TrucoCard> myHand, TrucoCard vira) {
        this.myHand = myHand;
        this.vira = vira;
        myHandValuesSorted = this.myHand.stream()
                .map(trucoCard -> trucoCard.relativeValue(vira))
                .sorted()
                .toList();
    }


    public TrucoCard getBestCard() {

        Integer valueOfBestCard;
        if (myHand.size() == 3) valueOfBestCard = myHandValuesSorted.get(2);
        else if (myHand.size() == 2) valueOfBestCard = myHandValuesSorted.get(1);
        else valueOfBestCard = myHandValuesSorted.get(0);

        return myHand.stream()
                .filter(trucoCard -> trucoCard.relativeValue(vira) == valueOfBestCard)
                .findAny()
                .orElse(myHand.get(0));
    }

    public TrucoCard getSecondBestCard() {

        Integer valueOfSecondBestCard;
        if (myHand.size() == 3) valueOfSecondBestCard = myHandValuesSorted.get(1);
        else valueOfSecondBestCard = myHandValuesSorted.get(0);

        return myHand.stream()
                .filter(trucoCard -> trucoCard.relativeValue(vira) == valueOfSecondBestCard)
                .findAny()
                .orElse(myHand.get(0));
    }

    public TrucoCard getWorstCard() {

        Integer worstCardValue = myHandValuesSorted.get(0);

        return myHand.stream()
                .filter(trucoCard -> trucoCard.relativeValue(vira) == worstCardValue)
                .findAny()
                .orElse(myHand.get(0));
    }
}
