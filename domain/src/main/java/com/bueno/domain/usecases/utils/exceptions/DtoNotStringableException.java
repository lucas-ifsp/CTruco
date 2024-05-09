package com.bueno.domain.usecases.utils.exceptions;

public class DtoNotStringableException extends Exception{
    public DtoNotStringableException(String message) {
        super(message);
    }
}
