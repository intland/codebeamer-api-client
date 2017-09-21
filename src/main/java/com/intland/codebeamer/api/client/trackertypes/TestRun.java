/*
 * Copyright (c) 2017 Intland Software (support@intland.com)
 *
 */
package com.intland.codebeamer.api.client.trackertypes;

public class TestRun implements TrackerType {
    @Override
    public Integer getId() {
        return 9;
    }

    @Override
    public String getName() {
        return "Test Run";
    }
}
