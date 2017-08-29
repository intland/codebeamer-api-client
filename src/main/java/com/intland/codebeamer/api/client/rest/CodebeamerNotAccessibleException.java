/*
 * Copyright (c) 2017 Intland Software (support@intland.com)
 *
 */
package com.intland.codebeamer.api.client.rest;

public class CodebeamerNotAccessibleException extends Exception {
    public CodebeamerNotAccessibleException(String message) {
        super(message);
    }

    public CodebeamerNotAccessibleException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public CodebeamerNotAccessibleException(Throwable throwable) {
        super(throwable);
    }
}
