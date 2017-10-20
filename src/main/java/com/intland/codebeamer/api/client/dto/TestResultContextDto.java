/*
 * Copyright (c) 2017 Intland Software (support@intland.com)
 *
 */
package com.intland.codebeamer.api.client.dto;

public class TestResultContextDto {
    private Integer testConfigurationId;
    private Integer testCaseTrackerId;
    private Integer testCaseId;
    private Integer releaseId;
    private Integer testRunTrackerId;
    private String buildIdentifier;
    private String defaultPackagePrefix;

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
	
	public Integer getTestCaseId() {
        return testCaseId;
    }

    public void setTestCaseId(Integer testCaseId) {
        this.testCaseId = testCaseId;
    }

    public Integer getReleaseId() {
        return releaseId;
    }

    public void setReleaseId(Integer releaseId) {
        this.releaseId = releaseId;
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

	public String getDefaultPackagePrefix() {
		return defaultPackagePrefix;
	}

	public void setDefaultPackagePrefix(String defaultPackagePrefix) {
		this.defaultPackagePrefix = defaultPackagePrefix;
	}
}
