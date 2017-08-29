package com.intland.codebeamer.api.client.rest;

import com.intland.codebeamer.api.client.Version;

public interface RestAdapter {
    Version getVersion() throws Exception;

    Boolean testConnection();
}
