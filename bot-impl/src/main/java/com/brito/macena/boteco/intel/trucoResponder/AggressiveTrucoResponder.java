package com.brito.macena.boteco.intel.trucoResponder;

import com.brito.macena.boteco.factories.InstanceFactory;
import com.brito.macena.boteco.interfaces.Analyzer;
import com.brito.macena.boteco.utils.Game;
import com.brito.macena.boteco.utils.Status;
import com.brito.macena.boteco.interfaces.TrucoResponder;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.List;

public class AggressiveTrucoResponder implements TrucoResponder {

    @Override
    public int getRaiseResponse(GameIntel intel, Status status) {
        int myScore = intel.getScore();
        int oppScore = intel.getOpponentScore();
        int scoreDifference = myScore - oppScore;
        List<TrucoCard> myCards = intel.getCards();

        if (!myCards.isEmpty()) {
            Analyzer analyzer = InstanceFactory.createAnaliseInstance(intel);
            status = analyzer.myHand(intel);
        }

        if (status == Status.EXCELLENT) {
            return 1;
        }

        if (myCards.size() == 3) {
            if (status == Status.GOOD) {
                return 1;
            } else if (status == Status.MEDIUM) {
                return 0;
            } else {
                return -1;
            }
        }

        if (myCards.size() == 2) {
            if (Game.wonFirstRound(intel)) {
                return 1;
            }

            if (Game.lostFirstRound(intel)) {
                if (status == Status.GOOD || status == Status.MEDIUM) {
                    return 1;
                }
                return -1;
            }

            if (status == Status.GOOD && scoreDifference <= -6) {
                return 1;
            }

            return 0;
        }

        if (myCards.size() == 1) {
            if (status == Status.GOOD || status == Status.EXCELLENT) {
                return 1;
            }

            return 0;
        }

        if (status == Status.MEDIUM && scoreDifference <= -4) {
            return 1;
        }

        return 0;
    }
}