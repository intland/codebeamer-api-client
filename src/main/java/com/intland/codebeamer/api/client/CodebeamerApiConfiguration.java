/*
 * Copyright (c) 2017 Intland Software (support@intland.com)
 *
 */
package com.intland.codebeamer.api.client;

public class CodebeamerApiConfiguration {
    private static CodebeamerApiConfiguration instance = null;
    private String uri;
    private String username;
    private String password;


    private CodebeamerApiConfiguration() {

    }

    public static CodebeamerApiConfiguration getInstance() {
        if (instance == null) {
            instance = new CodebeamerApiConfiguration();
        }
        return instance;
    }

    public String getUri() {
        return uri;
    }

    public CodebeamerApiConfiguration withUri(String uri) {
        this.uri = uri;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public CodebeamerApiConfiguration withUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public CodebeamerApiConfiguration withPassword(String password) {
        this.password = password;
        return this;
    }
}
