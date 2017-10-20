/*
 * Copyright (c) 2017 Intland Software (support@intland.com)
 *
 */
package com.intland.codebeamer.api.client.rest;

public class ItemNotFoundException extends RequestFailed {
    
	private static final long serialVersionUID = 2336965684196924424L;

	public ItemNotFoundException(String message) {
        super(message);
    }

    public ItemNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ItemNotFoundException(Throwable throwable) {
        super(throwable);
    }
}
