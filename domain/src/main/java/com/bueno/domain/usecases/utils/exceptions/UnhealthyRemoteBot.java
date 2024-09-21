package com.bueno.domain.usecases.utils.exceptions;

public class UnhealthyRemoteBot extends RuntimeException {
    public UnhealthyRemoteBot(String message) {
        super(message);
    }
}
