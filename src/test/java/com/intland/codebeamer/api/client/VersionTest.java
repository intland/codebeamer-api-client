package com.intland.codebeamer.api.client;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
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
        Assert.assertFalse(version instanceof Version);
        Assert.assertNull(version);
    }

    @Test(dataProvider = "isValidVersionStringProvider")
    public void testIsValidVersionString(String versionString, boolean expected) throws Exception {
        Assert.assertEquals(Version.isValidVersionString(versionString), expected, String.format("is %s a valid version string", versionString));
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
    public void testGetVersionString_withoutRevision() throws Exception {
        Version actualVersion = new Version(8, 2, 0, null);
        String actualVersionString = actualVersion.toString();
        Assert.assertEquals(actualVersionString, "8.2.0");
    }

    @Test
    public void testGetVersionString_withRevision() throws Exception {
        Version actualVersion = new Version(8, 2, 0, 1);
        String actualVersionString = actualVersion.toString();
        Assert.assertEquals(actualVersionString, "8.2.0.1");
    }

    @Test(dataProvider = "versionCompareProvider")
    public void testCompareTo(Version version, Version other, Version.Compare expectedResult) {
        Assert.assertEquals(version.compareTo(other), expectedResult, String.format("comparing %s to %s", version, other));
    }

    @DataProvider(name = "isValidVersionStringProvider")
    private Object[][] isValidVersionStringProvicer() {
        return new Object[][]{
                // valid Strings
                {"8.2.0", true},
                {"8.2.0.1", true},
                // invalid Strings
                {"8.2", false},
                {"8.2.0.", false},
                {"8.2.0-1", false},
                {"a.2.0", false},
                {"8.b.0", false},
                {"8.2.c", false},
        };
    }

    @DataProvider(name = "versionCompareProvider")
    private Object[][] versionCompareProvider() {
        return new Object[][]{
                // equals
                {new Version(8, 2, 0, null), new Version(8, 2, 0, null), Version.Compare.EQUAL},
                {new Version(8, 2, 0, null), new Version(8, 2, 0, 0), Version.Compare.EQUAL},
                {new Version(8, 2, 0, 0), new Version(8, 2, 0, null), Version.Compare.EQUAL},
                {new Version(8, 2, 0, 0), new Version(8, 2, 0, 0), Version.Compare.EQUAL},
                // other version is newer
                {new Version(8, 2, 0, 0), new Version(8, 2, 0, 1), Version.Compare.OTHER_IS_NEWER},
                {new Version(8, 2, 0, 0), new Version(8, 2, 1, null), Version.Compare.OTHER_IS_NEWER},
                {new Version(8, 2, 0, 0), new Version(8, 3, 0, null), Version.Compare.OTHER_IS_NEWER},
                {new Version(8, 2, 0, 0), new Version(9, 2, 0, null), Version.Compare.OTHER_IS_NEWER},
                {new Version(8, 2, 0, null), new Version(8, 2, 0, 1), Version.Compare.OTHER_IS_NEWER},
                {new Version(8, 2, 0, null), new Version(8, 2, 1, 0), Version.Compare.OTHER_IS_NEWER},
                {new Version(8, 2, 0, null), new Version(8, 2, 1, null), Version.Compare.OTHER_IS_NEWER},
                {new Version(8, 2, 0, null), new Version(8, 3, 0, 0), Version.Compare.OTHER_IS_NEWER},
                {new Version(8, 2, 0, null), new Version(8, 3, 0, null), Version.Compare.OTHER_IS_NEWER},
                {new Version(8, 2, 0, null), new Version(9, 1, 0, 0), Version.Compare.OTHER_IS_NEWER},
                {new Version(8, 2, 0, null), new Version(9, 1, 0, null), Version.Compare.OTHER_IS_NEWER},
                {new Version(8, 2, 0, null), new Version(9, 2, 0, 0), Version.Compare.OTHER_IS_NEWER},
                {new Version(8, 2, 0, null), new Version(9, 2, 0, null), Version.Compare.OTHER_IS_NEWER},
                {new Version(8, 2, 1, null), new Version(8, 3, 0, 0), Version.Compare.OTHER_IS_NEWER},
                {new Version(8, 2, 1, null), new Version(8, 3, 0, null), Version.Compare.OTHER_IS_NEWER},
                // other version is older
                {new Version(8, 2, 1, 1), new Version(7, 2, 1, null), Version.Compare.OTHER_IS_OLDER},
                {new Version(8, 2, 1, 1), new Version(7, 3, 2, 5), Version.Compare.OTHER_IS_OLDER},
                {new Version(8, 2, 1, 1), new Version(8, 1, 1, null), Version.Compare.OTHER_IS_OLDER},
                {new Version(8, 2, 1, 1), new Version(8, 1, 2, 5), Version.Compare.OTHER_IS_OLDER},
                {new Version(8, 2, 1, 1), new Version(8, 1, 2, null), Version.Compare.OTHER_IS_OLDER},
                {new Version(8, 2, 1, 1), new Version(8, 2, 0, 5), Version.Compare.OTHER_IS_OLDER},
                {new Version(8, 2, 1, 1), new Version(8, 2, 0, null), Version.Compare.OTHER_IS_OLDER},
                {new Version(8, 2, 1, 1), new Version(8, 2, 1, 0), Version.Compare.OTHER_IS_OLDER},
                {new Version(8, 2, 1, null), new Version(7, 2, 1, 5), Version.Compare.OTHER_IS_OLDER},
                {new Version(8, 2, 1, null), new Version(7, 2, 1, null), Version.Compare.OTHER_IS_OLDER},
                {new Version(8, 2, 1, null), new Version(7, 3, 2, 5), Version.Compare.OTHER_IS_OLDER},
                {new Version(8, 2, 1, null), new Version(8, 1, 1, null), Version.Compare.OTHER_IS_OLDER},
                {new Version(8, 2, 1, null), new Version(8, 1, 2, 5), Version.Compare.OTHER_IS_OLDER},
                {new Version(8, 2, 1, null), new Version(8, 1, 2, null), Version.Compare.OTHER_IS_OLDER},
                {new Version(8, 2, 1, null), new Version(8, 2, 0, 5), Version.Compare.OTHER_IS_OLDER},
                {new Version(8, 2, 1, null), new Version(8, 2, 0, null), Version.Compare.OTHER_IS_OLDER},
        };
    }
}
