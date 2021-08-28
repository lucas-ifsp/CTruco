package com.bueno.truco.domain.entities.truco;

import com.bueno.truco.domain.entities.hand.HandScore;
import com.bueno.truco.domain.entities.player.Player;

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

        int pointsLimit = Player.MAX_SCORE - Math.min(requester.getScore(), responder.getScore());

        while(handScore.get() < pointsLimit){
            final HandScore nextValidScore = handScore.increase();
            LOGGER.info(requester.getNickname()  + " is asking to increase to " + nextValidScore + " points.");

            int requestAnswer = responder.getTrucoResponse(nextValidScore);

            if(requestAnswer < 0) {
                LOGGER.info(responder.getNickname()  + " run.");
                return new TrucoResult(handScore, requester);
            }

            handScore = nextValidScore;
            if(requestAnswer == 0){
                LOGGER.info(responder.getNickname()  + " accepted.");
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
        if(!isRequesting) LOGGER.info(requester.getNickname() + " did not ask to increase hand score.");
        return !isRequesting;
    }

    private void changeRoles() {
        Player swap = requester;
        requester = responder;
        responder = swap;
    }
}
