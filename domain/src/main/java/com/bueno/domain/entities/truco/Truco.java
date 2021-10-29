/*
 *  Copyright (C) 2021 Lucas B. R. de Oliveira - IFSP/SCL
 *  Contact: lucas <dot> oliveira <at> ifsp <dot> edu <dot> br
 *
 *  This file is part of CTruco (Truco game for didactic purpose).
 *
 *  CTruco is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CTruco is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CTruco.  If not, see <https://www.gnu.org/licenses/>
 */

package com.bueno.domain.entities.truco;

import com.bueno.domain.entities.hand.HandScore;
import com.bueno.domain.entities.player.util.Player;

import java.util.logging.Logger;

public class Truco {

    private Player requester;
    private Player responder;

    private final static Logger LOGGER = Logger.getLogger(Truco.class.getName());

    public Truco(Player requester, Player responder) {
        this.requester = requester;
        this.responder = responder;
    }

    public TrucoResult handle(HandScore handScore) {
        if(requester == null || responder == null) {
            throw new IllegalArgumentException("Players must no be null!");
        }

        if(isMaoDeOnze() || isNotAskingForTruco()) {
            return new TrucoResult(handScore);
        }

        int maxScoreIncrement = Player.MAX_SCORE - Math.min(requester.getScore(), responder.getScore());

        while(handScore.get() < maxScoreIncrement){
            final HandScore nextValidScore = handScore.increase();
            LOGGER.info(requester.getUsername()  + " is asking to increase to " + nextValidScore + " points.");

            int requestAnswer = responder.getTrucoResponse(nextValidScore);

            if(requestAnswer < 0) {
                LOGGER.info(responder.getUsername()  + " run.");
                return new TrucoResult(handScore, requester);
            }

            handScore = nextValidScore;
            if(requestAnswer == 0){
                LOGGER.info(responder.getUsername()  + " accepted.");
                break;
            }

            changeRoles();
        }
        return new TrucoResult(handScore, null, requester);
    }

    private boolean isMaoDeOnze() {
        final boolean maoDeOnze = requester.getScore() == 11 ^ responder.getScore() == 11;
        if(maoDeOnze) LOGGER.info("Asking for truco is not allowed due to mão de onze");
        return maoDeOnze;
    }

    private boolean isNotAskingForTruco() {
        final boolean isRequesting = requester.requestTruco();
        if(!isRequesting) LOGGER.info(requester.getUsername() + " did not ask to increase hand score.");
        return !isRequesting;
    }

    private void changeRoles() {
        Player swap = requester;
        requester = responder;
        responder = swap;
    }
}
