package com.codebeamer.api.client;

import org.testng.Assert;
import org.testng.annotations.Test;

public class VersionTest {
    @Test
    public void testGetVersionFromString_withValidString() throws Exception {
        Version version = Version.getVersionFromString("8.2.0");
        Assert.assertTrue(version instanceof Version);
    }

    @Test
    public void testGetVersionFromString_withInvalidString() throws Exception {
        Version version = Version.getVersionFromString("8.a.0");
        Assert.assertNull(version);
    }

    @Test
    public void testIsValidVersionString() throws Exception {
        Assert.assertTrue(Version.isValidVersionString("8.2.0"));
        Assert.assertTrue(Version.isValidVersionString("8.2.0.1"));
        Assert.assertFalse(Version.isValidVersionString("8.2"));
        Assert.assertFalse(Version.isValidVersionString("8.2.0."));
        Assert.assertFalse(Version.isValidVersionString("8.2.0-1"));
        Assert.assertFalse(Version.isValidVersionString("a.2.0"));
        Assert.assertFalse(Version.isValidVersionString("8.b.0"));
        Assert.assertFalse(Version.isValidVersionString("8.2.c"));
    }

    @Test
    public void testGetMajorVersion() throws Exception {
        Version actualVersion = Version.getVersionFromString("8.2.0");
        Assert.assertEquals(actualVersion.getMajorVersion(), Integer.valueOf(8));
    }

    @Test
    public void testGetMinorVersion() throws Exception {
        Version actualVersion = Version.getVersionFromString("8.2.0");
        Assert.assertEquals(actualVersion.getMinorVersion(), Integer.valueOf(2));
    }

    @Test
    public void testGetBuild() throws Exception {
        Version actualVersion = Version.getVersionFromString("8.2.0");
        Assert.assertEquals(actualVersion.getBuild(), Integer.valueOf(0));
    }

    @Test
    public void testGetRevision() throws Exception {
        Version actualVersion = Version.getVersionFromString("8.2.0.1");
        Assert.assertEquals(actualVersion.getRevision(), Integer.valueOf(1));
    }

    @Test
    public void testGetRevision_noRevisionGiven() throws Exception {
        Version actualVersion = Version.getVersionFromString("8.2.0");
        Assert.assertNull(actualVersion.getRevision());
    }

    @Test
    public void testGetVersionString() throws Exception {
        Version actualVersion = Version.getVersionFromString("8.2.0");
        String actualVersionString = actualVersion.getVersionString();
        Assert.assertEquals(actualVersionString, "8.2.0");
    }

    @Test
    public void testCompareTo_baseVersionHasRevision() throws Exception {
        Version baseVersion = new Version(8, 2, 0, 1);

        // equals
        Assert.assertEquals(baseVersion.compareTo(Version.getVersionFromString("8.2.0.1")), 0);
        Assert.assertEquals(baseVersion.compareTo(baseVersion), 0);

        // other version is higher
        Assert.assertEquals(baseVersion.compareTo(Version.getVersionFromString("8.3.0")), 1);
        Assert.assertEquals(baseVersion.compareTo(Version.getVersionFromString("8.2.0.2")), 1);
        Assert.assertEquals(baseVersion.compareTo(Version.getVersionFromString("9.0.0")), 1);

        // other version is lower
        Assert.assertEquals(baseVersion.compareTo(Version.getVersionFromString("8.2.0")), -1);
        Assert.assertEquals(baseVersion.compareTo(Version.getVersionFromString("8.2.0.0")), -1);
        Assert.assertEquals(baseVersion.compareTo(Version.getVersionFromString("8.1.0")), -1);
        Assert.assertEquals(baseVersion.compareTo(Version.getVersionFromString("8.1.0.5")), -1);
        Assert.assertEquals(baseVersion.compareTo(Version.getVersionFromString("7.3.0")), -1);
    }

    @Test
    public void testCompareTo_baseVersionHasNoRevision() throws Exception {
        Version baseVersion = new Version(8, 2, 0, null);

        // equals
        Assert.assertEquals(baseVersion.compareTo(Version.getVersionFromString("8.2.0")), 0);
        Assert.assertEquals(baseVersion.compareTo(Version.getVersionFromString("8.2.0.0")), 0);
        Assert.assertEquals(baseVersion.compareTo(baseVersion), 0);

        // other version is higher
        Assert.assertEquals(baseVersion.compareTo(Version.getVersionFromString("8.2.0.1")), 1);
        Assert.assertEquals(baseVersion.compareTo(Version.getVersionFromString("8.3.0")), 1);
        Assert.assertEquals(baseVersion.compareTo(Version.getVersionFromString("8.3.0.0")), 1);
        Assert.assertEquals(baseVersion.compareTo(Version.getVersionFromString("9.0.0")), 1);

        // other version is lower
        Assert.assertEquals(baseVersion.compareTo(Version.getVersionFromString("8.1.0")), -1);
        Assert.assertEquals(baseVersion.compareTo(Version.getVersionFromString("7.3.0")), -1);
        Assert.assertEquals(baseVersion.compareTo(Version.getVersionFromString("7.3.0.5")), -1);
    }
}
