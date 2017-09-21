package com.intland.codebeamer.api.client.rest;

import com.intland.codebeamer.api.client.Version;
import com.intland.codebeamer.api.client.dto.TrackerDto;
import com.intland.codebeamer.api.client.dto.TrackerItemDto;
import com.intland.codebeamer.api.client.dto.TrackerTypeDto;

import java.io.File;

public interface RestAdapter {
    Version getVersion() throws CodebeamerNotAccessibleException;

    Boolean testConnection() throws CodebeamerNotAccessibleException;

    TrackerItemDto getTrackerItem(Integer id) throws CodebeamerNotAccessibleException;

    TrackerDto getTracker(Integer id) throws CodebeamerNotAccessibleException;

    TrackerTypeDto getTrackerType(Integer id) throws CodebeamerNotAccessibleException;

    Boolean uploadXUnitResults(File[] files) throws CodebeamerNotAccessibleException;
}
