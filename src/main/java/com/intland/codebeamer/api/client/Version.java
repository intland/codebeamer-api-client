/*
 * Copyright (c) 2017 Intland Software (support@intland.com)
 *
 */
package com.intland.codebeamer.api.client;

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

    @Override
    public String toString() {
        String versionString = String.format("%d.%d.%d", major, minor, build);
        if (revision != null) {
            versionString += String.format(".%d", revision);
        }
        return versionString;
    }

    /**
     * Returns Compare.EQUAL when the two versions are equal,
     * Compare.OTHER_IS_OLDER if the other version is older and
     * Compare.OTHER_IS_NEWER if the other version is newer
     *
     * @param other another version to compare to
     * @return enum Version.Compare
     */
    public Compare compareTo(Version other) {
        return compareMajorVersionTo(other);
    }

    private Compare compareMajorVersionTo(Version other) {
        if (this.major != other.getMajorVersion()) {
            return this.major > other.getMajorVersion() ? Compare.OTHER_IS_OLDER : Compare.OTHER_IS_NEWER;
        }
        return compareMinorVersionTo(other);
    }

    private Compare compareMinorVersionTo(Version other) {
        if (this.minor != other.getMinorVersion()) {
            return this.minor > other.getMinorVersion() ? Compare.OTHER_IS_OLDER : Compare.OTHER_IS_NEWER;
        }
        return compareBuildVersionTo(other);
    }

    private Compare compareBuildVersionTo(Version other) {
        if (this.build != other.getBuild()) {
            return this.build > other.getBuild() ? Compare.OTHER_IS_OLDER : Compare.OTHER_IS_NEWER;
        }
        return compareRevisionVersionTo(other);
    }

    private Compare compareRevisionVersionTo(Version other) {
        if (this.getRevision() == 0 && other.getRevision() > 0) {
            return Compare.OTHER_IS_NEWER;
        } else if (this.getRevision() > 0 && other.getRevision() == 0) {
            return Compare.OTHER_IS_OLDER;
        } else if (this.getRevision() != other.getRevision()) {
            return this.getRevision() > other.getRevision() ? Compare.OTHER_IS_OLDER : Compare.OTHER_IS_NEWER;
        }
        return Compare.EQUAL;
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
        return revision == null ? 0 : revision;
    }

    public enum Compare {
        EQUAL,
        OTHER_IS_NEWER,
        OTHER_IS_OLDER
    }
}
