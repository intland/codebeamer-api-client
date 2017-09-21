/*
 * Copyright (c) 2017 Intland Software (support@intland.com)
 *
 */
package com.intland.codebeamer.api.client.trackertypes;

public class TestCase implements TrackerType {
    @Override
    public Integer getId() {
        return 102;
    }

    @Override
    public String getName() {
        return "Test Case";
    }
}
