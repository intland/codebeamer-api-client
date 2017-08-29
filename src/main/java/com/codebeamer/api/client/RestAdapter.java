package com.codebeamer.api.client;

import org.apache.http.auth.InvalidCredentialsException;
import org.apache.http.conn.ConnectTimeoutException;

@FunctionalInterface
public interface RestAdapter {
    String getVersion() throws ConnectTimeoutException, InvalidCredentialsException;
}
