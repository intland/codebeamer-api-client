/*
 * Copyright (c) 2017 Intland Software (support@intland.com)
 *
 */
package com.intland.codebeamer.api.client.trackertypes;

public class Bug implements TrackerType {
    @Override
    public Integer getId() {
        return 2;
    }

    @Override
    public String getName() {
        return "Bug";
    }
}
