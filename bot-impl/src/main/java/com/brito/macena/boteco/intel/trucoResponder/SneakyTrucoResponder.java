package com.brito.macena.boteco.intel.trucoResponder;

import com.brito.macena.boteco.factories.InstanceFactory;
import com.brito.macena.boteco.interfaces.Analyzer;
import com.brito.macena.boteco.interfaces.TrucoResponder;
import com.brito.macena.boteco.utils.Status;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.List;
import java.util.Random;

public class SneakyTrucoResponder implements TrucoResponder {

    private final Random random = new Random();

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

        if (status == Status.GOOD) {
            return random.nextInt(100) < 80 ? 1 : 0;
        }

        if (status == Status.MEDIUM) {
            return random.nextInt(100) < 50 ? 1 : 0;
        }

        if (status == Status.BAD) {
            if (random.nextInt(100) < 25) {
                return 1;
            } else {
                return -1;
            }
        }

        if (scoreDifference <= -6 && random.nextInt(100) < 50) {
            return 1;
        }

        return 0;
    }
}