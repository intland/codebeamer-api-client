/*
 * Copyright (c) 2017 Intland Software (support@intland.com)
 *
 */
package com.codebeamer.api.client;

public class Version {
    private Integer major;
    private Integer minor;
    private Integer build;
    private Integer revision;

    // major.minor.build[.revision] e.g 8.2.0[.1]
    private static final String VERSION_REGEXP = "\\d\\.\\d\\.\\d(\\.\\d)?";

    private Version() {
    }

    private Version(Integer major, Integer minor, Integer build, Integer revision) {
        this.major = major;
        this.minor = minor;
        this.build = build;
        this.revision = revision;
    }

    public static Version getVersionFromString(String versionString) {
        if (isValidVersionString(versionString)) {
            String[] parts = versionString.split("\\.");
            Integer major = Integer.valueOf(parts[0]);
            Integer minor = Integer.valueOf(parts[1]);
            Integer build = Integer.valueOf(parts[2]);
            Integer revision = parts.length == 4 ? Integer.valueOf(parts[3]) : null;
            return new Version(major, minor, build, revision);
        }
         return null;
    }

    public static boolean isValidVersionString(String versionString) {
        return versionString.matches(VERSION_REGEXP);
    }

    public String getVersionString() {
        String versionString = String.format("%d.%d.%d", major, minor, build);
        if (revision != null) {
            versionString += String.format(".%d", revision);
        }
        return versionString;
    }

    public Integer getMajorVersion() {
        return major;
    }

    public Integer getMinorVersion() {
        return minor;
    }

    public Integer getBuild() {
        return build;
    }

    public Integer getRevision() {
        return revision;
    }
}
