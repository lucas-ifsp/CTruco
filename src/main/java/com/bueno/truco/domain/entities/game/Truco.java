package com.bueno.truco.domain.entities.game;

import com.bueno.truco.domain.entities.player.Player;

public class Truco {

    private Player requester;
    private Player responder;

    public Truco(Player requester, Player responder) {
        this.requester = requester;
        this.responder = responder;
    }

    public TrucoResult handle(int handPoints) {
        if(Player.isValidScoreIncrement(handPoints) || requester == null || responder == null) {
            throw new IllegalArgumentException("Players must no be null. Hand points must comply with truco rules.");
        }

        if(isForbidenToAskForTruco() || isNotAskingForTruco()) {
            return new TrucoResult(handPoints);
        }

        int pointsLimit = Player.MAX_SCORE - Math.min(requester.getScore(), responder.getScore());

        while(handPoints < pointsLimit){
            int requestAnswer = responder.getTrucoResponse(getNextValidIncrement(handPoints));

            if(requestAnswer < 0) {
                return new TrucoResult(handPoints, requester);
            }

            handPoints = getNextValidIncrement(handPoints);
            if(requestAnswer == 0){
                break;
            }

            changeRoles();
        }
        return new TrucoResult(handPoints, null, requester);
    }

    private boolean isForbidenToAskForTruco() {
        return requester.getScore() == 11 || responder.getScore() == 11;
    }

    private boolean isNotAskingForTruco() {
        return !requester.requestTruco();
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
