package com.bueno.truco.domain.entities.player;

public class InvalidTrucoScoreIncrementException extends RuntimeException {
    public InvalidTrucoScoreIncrementException(String message) {
        super(message);
    }
}
