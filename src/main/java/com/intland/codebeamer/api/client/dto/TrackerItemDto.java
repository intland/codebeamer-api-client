/*
 * Copyright (c) 2017 Intland Software (support@intland.com)
 *
 */
package com.intland.codebeamer.api.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import static com.intland.codebeamer.api.client.dto.DtoHelper.parseIdFromUri;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackerItemDto {
    Integer id;
    String uri;
    TrackerDto tracker;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
        this.id = parseIdFromUri(uri);
    }

    public Integer getId() {
        return id;
    }

    public TrackerDto getTracker() {
        return tracker;
    }

    public void setTracker(TrackerDto tracker) {
        this.tracker = tracker;
    }
}
