/*
 * Copyright (c) 2017 Intland Software (support@intland.com)
 *
 */
package com.intland.codebeamer.api.client.rest;

public class RequestFailed extends Exception {
    public RequestFailed(String message) {
        super(message);
    }

    public RequestFailed(String message, Throwable throwable) {
        super(message, throwable);
    }

    public RequestFailed(Throwable throwable) {
        super(throwable);
    }
}
