/*
 * Copyright (c) 2017 Intland Software (support@intland.com)
 *
 */
package com.intland.codebeamer.api.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import static com.intland.codebeamer.api.client.dto.DtoHelper.parseIdFromUri;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackerTypeDto {
    private String uri;
    private String name;

    public Integer getTypeId() {
        return parseIdFromUri(uri);
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
