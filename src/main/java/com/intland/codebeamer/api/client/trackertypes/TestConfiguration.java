/*
 * Copyright (c) 2017 Intland Software (support@intland.com)
 *
 */
package com.intland.codebeamer.api.client.trackertypes;

public final class TestConfiguration implements TrackerType {
    @Override
    public Integer getId() {
        return 109;
    }

    @Override
    public String getName() {
        return "Test Configuration";
    }
}
