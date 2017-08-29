package com.codebeamer.api.client;

@FunctionalInterface
public interface RestAdapter {
    Version getVersion() throws Exception;
}
