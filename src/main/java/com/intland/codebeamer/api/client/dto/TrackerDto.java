/*
 * Copyright (c) 2017 Intland Software (support@intland.com)
 *
 */
package com.intland.codebeamer.api.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import static com.intland.codebeamer.api.client.dto.DtoHelper.parseIdFromUri;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackerDto {
    private TrackerTypeDto type;
    private ProjectDto project;
    private Integer id;
    private String uri;
    private String name;

    public ProjectDto getProject() {
        return project;
    }

    public void setProject(ProjectDto project) {
        this.project = project;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TrackerTypeDto getType() {
        return type;
    }

    public void setType(TrackerTypeDto type) {
        this.type = type;
    }
}
