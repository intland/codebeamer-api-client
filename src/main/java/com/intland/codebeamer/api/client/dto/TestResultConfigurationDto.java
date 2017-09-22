/*
 * Copyright (c) 2017 Intland Software (support@intland.com)
 *
 */
package com.intland.codebeamer.api.client.dto;

public class TestResultConfigurationDto {
    private Integer testConfigurationId;
    private Integer testCaseTrackerId;
    private Integer testSetTrackerId;
    private Integer testRunTrackerId;

    public Integer getTestConfigurationId() {
        return testConfigurationId;
    }

    public void setTestConfigurationId(Integer testConfigurationId) {
        this.testConfigurationId = testConfigurationId;
    }

    public Integer getTestCaseTrackerId() {
        return testCaseTrackerId;
    }

    public void setTestCaseTrackerId(Integer testCaseTrackerId) {
        this.testCaseTrackerId = testCaseTrackerId;
    }

    public Integer getTestSetTrackerId() {
        return testSetTrackerId;
    }

    public void setTestSetTrackerId(Integer testSetTrackerId) {
        this.testSetTrackerId = testSetTrackerId;
    }

    public Integer getTestRunTrackerId() {
        return testRunTrackerId;
    }

    public void setTestRunTrackerId(Integer testRunTrackerId) {
        this.testRunTrackerId = testRunTrackerId;
    }
}
