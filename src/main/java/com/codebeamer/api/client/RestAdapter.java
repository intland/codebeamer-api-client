package com.codebeamer.api.client;

import org.apache.http.auth.InvalidCredentialsException;
import org.apache.http.conn.ConnectTimeoutException;

@FunctionalInterface
public interface RestAdapter {
    Version getVersion() throws ConnectTimeoutException, InvalidCredentialsException;
}
