package com.bueno.truco.domain.usecases.truco;

import com.bueno.truco.domain.entities.player.Player;

public class RequestTrucoUseCase {

    private Player currentRequester;
    private Player currentResponder;

    public TrucoResult handle(Player requester, Player responder, int handPoints) {
        if(Player.isValidScoreIncrement(handPoints) || requester == null || responder == null) {
            throw new IllegalArgumentException("Players must no be null. Hand points must comply with truco rules.");
        }
        if(!requester.requestTruco()) {
            return new TrucoResult(handPoints);
        }
        if(requester.getScore() == 11) {
            return new TrucoResult(12, responder);
        }

        int pointsLimit = Player.MAX_SCORE - Math.min(requester.getScore(), responder.getScore());

        currentRequester = requester;
        currentResponder = responder;

        while(handPoints < pointsLimit){
            int requestAnswer = currentResponder.getTrucoResponse(getNextValidIncrement(handPoints));

            if(requestAnswer < 0) {
                return new TrucoResult(handPoints, currentRequester);
            }

            handPoints = getNextValidIncrement(handPoints);
            if(requestAnswer == 0){
                break;
            }

            changeRoles();
        }
        return new TrucoResult(handPoints, null, currentRequester);
    }

    private int getNextValidIncrement(int currentValue){
        if(currentValue == 1) return 3;
        if(currentValue == 12) return 12;
        return currentValue + 3;
    }

    private void changeRoles() {
        Player swap = currentRequester;
        currentRequester = currentResponder;
        currentResponder = swap;
    }

}
