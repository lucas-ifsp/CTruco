package com.brito.macena.boteco.utils;

import com.bueno.spi.model.TrucoCard;

import java.util.List;

public class MyCards {

    List<TrucoCard> myHand;
    TrucoCard vira;


    public MyCards(List<TrucoCard> myHand, TrucoCard vira) {
        this.myHand = myHand;
        this.vira = vira;
    }


    public TrucoCard getBestCard() {
        return null;
    }

    public TrucoCard getSecondBestCard() {
        return null;
    }

    public TrucoCard getWorstCard() {
        return null;
    }
}