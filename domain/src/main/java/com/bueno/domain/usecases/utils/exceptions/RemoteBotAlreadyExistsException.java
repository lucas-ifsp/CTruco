package com.bueno.domain.usecases.utils.exceptions;

public class RemoteBotAlreadyExistsException extends Exception{ // TODO retirar essas exceptions e fazer o tratamento com as que já existem
    public RemoteBotAlreadyExistsException(String message) {
        super(message);
    }
}
