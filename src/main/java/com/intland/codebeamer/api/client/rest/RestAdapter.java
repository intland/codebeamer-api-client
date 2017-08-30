package com.intland.codebeamer.api.client.rest;

import com.intland.codebeamer.api.client.Version;

import java.io.File;

public interface RestAdapter {
    Version getVersion() throws CodebeamerNotAccessibleException;

    Boolean testConnection() throws CodebeamerNotAccessibleException;

    Boolean uploadXUnitResults(File[] files) throws CodebeamerNotAccessibleException;
}
