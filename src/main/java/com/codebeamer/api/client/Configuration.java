/*
 * Copyright (c) 2017 Intland Software (support@intland.com)
 *
 */
package com.codebeamer.api.client;

public class Configuration {
    private String uri;

    private String username;
    private String password;

    private Configuration() {

    }

    public Configuration(String uri, String username, String password) {
        this.uri = uri;
        this.username = username;
        this.password = password;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
