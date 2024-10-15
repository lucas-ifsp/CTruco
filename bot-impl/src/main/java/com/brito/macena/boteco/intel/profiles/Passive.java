package com.brito.macena.boteco.intel.profiles;

import com.brito.macena.boteco.interfaces.ProfileBot;
import com.brito.macena.boteco.utils.MyHand;
import com.brito.macena.boteco.utils.Status;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;


public class Passive extends ProfileBot {
    private final GameIntel intel;
    private final Status status;
    private final TrucoCard vira;
    private final TrucoCard bestCard;
    private final TrucoCard secondBestCard;
    private final TrucoCard worstCard;

    public Passive(GameIntel intel, Status status) {
        this.intel = intel;
        this.status = status;
        vira = intel.getVira();

        MyHand myHand = new MyHand(intel.getCards(), intel.getVira());

        bestCard = myHand.getBestCard();
        secondBestCard = myHand.getSecondBestCard();
        worstCard = myHand.getWorstCard();
    }

    @Override
    public CardToPlay firstRoundChoose() {
        return null;
    }

    @Override
    public CardToPlay secondRoundChoose() {
        return null;
    }

    @Override
    public CardToPlay thirdRoundChoose() {
        return null;
    }

    private boolean haveAtLeastTwoManilhas() {
        return false;
    }

    private boolean haveAtLeastOneManilha() {
        return false;
    }

    private long getManilhaAmount() {
        return 0L;
    }

    private long powerOfTheTwoBestCards() {
        return 0L;
    }
}