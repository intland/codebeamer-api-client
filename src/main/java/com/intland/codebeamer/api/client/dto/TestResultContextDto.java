/*
 * Copyright (c) 2017 Intland Software (support@intland.com)
 *
 */
package com.intland.codebeamer.api.client.dto;

public class TestResultContextDto {
    private Integer testConfigurationId;
    private Integer testCaseTrackerId;
    private Integer testSetTrackerId;
    private Integer testRunTrackerId;
    private String buildIdentifier;

    private int numberOfTestResults;

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

    public String getBuildIdentifier() {
        return buildIdentifier;
    }

    public void setBuildIdentifier(String buildIdentifier) {
        this.buildIdentifier = buildIdentifier;
    }

    public int getNumberOfTestResults() {
        return numberOfTestResults;
    }

    public void setNumberOfTestResults(int numberOfTestResults) {
        this.numberOfTestResults = numberOfTestResults;
    }
}
