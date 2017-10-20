/*
 * Copyright (c) 2017 Intland Software (support@intland.com)
 *
 */
package com.intland.codebeamer.api.client.rest;

public class ConnectionFailedException extends RequestFailed {
    
	private static final long serialVersionUID = 7918920227424286471L;

	public ConnectionFailedException(String message) {
        super(message);
    }

    public ConnectionFailedException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ConnectionFailedException(Throwable throwable) {
        super(throwable);
    }
}
