/*
 * Copyright (c) 2017 Intland Software (support@intland.com)
 *
 */
package com.intland.codebeamer.api.client.trackertypes;

public class UserStory implements TrackerType {
    @Override
    public Integer getId() {
        return 10;
    }

    @Override
    public String getName() {
        return "User Story";
    }
}
