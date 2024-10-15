package com.brito.macena.boteco.interfaces;

import com.brito.macena.boteco.intel.profiles.Passive;
import com.brito.macena.boteco.utils.Status;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.List;

public abstract class ProfileBot {
    public CardToPlay choose(GameIntel intel, Status status) {
        Passive profileBot = new Passive(intel, status);
        List<TrucoCard> myCards = intel.getCards();
        if (myCards.size() == 3) return profileBot.firstRoundChoose();
        if (myCards.size() == 2) return profileBot.secondRoundChoose();
        else return profileBot.thirdRoundChoose();
    }

    public abstract CardToPlay firstRoundChoose();
    public abstract CardToPlay secondRoundChoose();
    public abstract CardToPlay thirdRoundChoose();
}