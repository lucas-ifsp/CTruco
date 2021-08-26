package com.bueno.truco.domain.entities.game;

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

    public TrucoResult handle(int handPoints) {
        if(Player.isValidScoreIncrement(handPoints) || requester == null || responder == null) {
            throw new IllegalArgumentException("Players must no be null. Hand points must comply with truco rules.");
        }

        if(isMaoDeOnze() || isNotAskingForTruco()) {
            return new TrucoResult(handPoints);
        }

        int pointsLimit = Player.MAX_SCORE - Math.min(requester.getScore(), responder.getScore());

        while(handPoints < pointsLimit){
            final int nextValidScore = getNextValidIncrement(handPoints);
            LOGGER.info(requester.getNickname()  + " is asking to increase to " + nextValidScore + " points.");

            int requestAnswer = responder.getTrucoResponse(nextValidScore);

            if(requestAnswer < 0) {
                LOGGER.info(responder.getNickname()  + " run.");
                return new TrucoResult(handPoints, requester);
            }

            handPoints = nextValidScore;
            if(requestAnswer == 0){
                LOGGER.info(responder.getNickname()  + " accepted.");
                break;
            }

            changeRoles();
        }
        return new TrucoResult(handPoints, null, requester);
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

    private int getNextValidIncrement(int currentValue){
        if(currentValue == 1) return 3;
        if(currentValue == 12) return 12;
        return currentValue + 3;
    }

    private void changeRoles() {
        Player swap = requester;
        requester = responder;
        responder = swap;
    }
}
