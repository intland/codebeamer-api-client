package com.codebeamer.api.client;

public interface RestAdapter {
    Version getVersion() throws Exception;

    Boolean testConnection();
}
