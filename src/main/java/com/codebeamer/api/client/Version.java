/*
 * Copyright (c) 2017 Intland Software (support@intland.com)
 *
 */
package com.codebeamer.api.client;

public class Version {
    // major.minor.build[.revision] e.g 8.2.0[.1]
    private static final String VERSION_REGEXP = "\\d\\.\\d\\.\\d(\\.\\d)?";

    private Integer major;
    private Integer minor;
    private Integer build;
    private Integer revision;

    private Version() {
    }

    public Version(Integer major, Integer minor, Integer build, Integer revision) {
        this.major = major;
        this.minor = minor;
        this.build = build;
        this.revision = revision;
    }

    public static Version getVersionFromString(String versionString) {
        String trimmedVersionString = versionString.replaceAll("^\"", "").replaceAll("\"$", "");
        if (isValidVersionString(trimmedVersionString)) {
            String[] parts = trimmedVersionString.split("\\.");
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

    /**
     * Returns 0 when the two versions are equal, -1 if the other version is older and 1 if the other version is newer
     *
     * @param other another version to compare to
     * @return the signum function of the difference
     */
    public int compareTo(Version other) {
        if (this.major != other.getMajorVersion()) {
            return Integer.signum(other.getMajorVersion() - this.major);
        }
        if (this.minor != other.getMinorVersion()) {
            return Integer.signum(other.getMinorVersion() - this.minor);
        }
        if (this.build != other.getBuild()) {
            return Integer.signum(other.getBuild() - this.build);
        }
        if ((this.revision == null || this.revision == 0) && (other.getRevision() != null && other.getRevision() > 0)) {
            return 1;
        } else if ((this.revision != null && this.revision > 0) && (other.getRevision() == null || other.getRevision() == 0)) {
            return -1;
        } else if (this.revision != null && other.getRevision() != null) {
            return Integer.signum(other.getRevision() - this.revision);
        }
        return 0;
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
