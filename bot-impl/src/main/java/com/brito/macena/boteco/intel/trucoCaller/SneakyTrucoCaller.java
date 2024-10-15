package com.brito.macena.boteco.intel.trucoCaller;

import com.brito.macena.boteco.interfaces.TrucoCaller;
import com.brito.macena.boteco.utils.MyHand;
import com.brito.macena.boteco.utils.Status;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import java.util.List;

public class SneakyTrucoCaller implements TrucoCaller {

    @Override
    public boolean shouldCallTruco(GameIntel intel, Status status) {
        MyHand myHand = new MyHand(intel.getCards(), intel.getVira());
        int scoreDistance = intel.getScore() - intel.getOpponentScore();
        List<TrucoCard> myCards = intel.getCards();

        if ((status == Status.MEDIUM || status == Status.GOOD) && myCards.size() == 3) {
            return true;
        }

        if (status == Status.EXCELLENT && myCards.size() <= 2) {
            return true;
        }

        if (status == Status.GOOD) {
            if (myCards.size() == 2) {
                return myHand.powerOfCard(intel, 1) >= 8;
            }
            if (myCards.size() == 1) {
                return true;
            }
        }
        return false;
    }
}
