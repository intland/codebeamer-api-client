package com.intland.codebeamer.api.client.rest;

import com.intland.codebeamer.api.client.Version;
import com.intland.codebeamer.api.client.dto.TrackerDto;
import com.intland.codebeamer.api.client.dto.TrackerItemDto;
import com.intland.codebeamer.api.client.dto.TrackerTypeDto;

import java.io.File;

public interface RestAdapter {
    Version getVersion() throws RequestFailed;

    boolean testConnection();

    boolean testCredentials();

    TrackerItemDto getTrackerItem(Integer id) throws RequestFailed;

    TrackerDto getTracker(Integer id) throws RequestFailed;

    TrackerTypeDto getTrackerType(Integer id) throws RequestFailed;

    void uploadXUnitResults(File[] files) throws RequestFailed;
}
