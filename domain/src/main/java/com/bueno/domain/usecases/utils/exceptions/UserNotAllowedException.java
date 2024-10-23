package com.bueno.domain.usecases.utils.exceptions;

public class UserNotAllowedException extends RuntimeException {
    public UserNotAllowedException(String message) {
        super(message);
    }
}
