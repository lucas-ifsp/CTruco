package com.bueno.domain.usecases.utils.exceptions;

public class BotAlreadyExistsException extends Exception{
    public BotAlreadyExistsException(String message) {
        super(message);
    }
}
