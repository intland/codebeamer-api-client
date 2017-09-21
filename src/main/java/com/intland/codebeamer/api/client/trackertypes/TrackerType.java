/*
 * Copyright (c) 2017 Intland Software (support@intland.com)
 *
 */
package com.intland.codebeamer.api.client.trackertypes;

public interface TrackerType {

    Integer getId();

    String getName();

    default String getUri() {
        return String.format("/category/type/%s", getId());
    }
}
