package com.intland.codebeamer.api.client.rest;

import com.intland.codebeamer.api.client.Version;

public interface RestAdapter {
    Version getVersion() throws CodebeamerNotAccessibleException;

    Boolean testConnection() throws CodebeamerNotAccessibleException;
}
