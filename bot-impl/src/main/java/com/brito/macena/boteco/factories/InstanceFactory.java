package com.brito.macena.boteco.factories;

import com.brito.macena.boteco.intel.profiles.Agressive;
import com.brito.macena.boteco.intel.profiles.Passive;
import com.brito.macena.boteco.interfaces.ProfileBot;
import com.brito.macena.boteco.utils.Status;
import com.bueno.spi.model.GameIntel;

public class InstanceFactory {
    public static ProfileBot createProfileBot(GameIntel intel, Status status) {
        int myScore = intel.getScore();
        int oppScore = intel.getOpponentScore();
        int scoreDifference = myScore - oppScore;

        if (oppScore > myScore && scoreDifference < -6) {
            return new Passive(intel, status);
        } else {
            return new Agressive(intel, status);
        }
    }
}
