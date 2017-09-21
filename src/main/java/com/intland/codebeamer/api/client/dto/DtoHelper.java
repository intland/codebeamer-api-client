/*
 * Copyright (c) 2017 Intland Software (support@intland.com)
 *
 */
package com.intland.codebeamer.api.client.dto;

public class DtoHelper {
    private DtoHelper() {

    }

    public static Integer parseIdFromUri(String uri) {
        if (uri.isEmpty()) {
            return null;
        }
        int index = uri.lastIndexOf('/');
        return Integer.parseInt(uri.substring(index + 1));
    }
}
