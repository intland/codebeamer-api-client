/*
 * Copyright (c) 2017 Intland Software (support@intland.com)
 *
 */
package com.intland.codebeamer.api.client.rest;

public class InvalidCredentialsException extends CodebeamerNotAccessibleException {
    public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public InvalidCredentialsException(Throwable throwable) {
        super(throwable);
    }
}
