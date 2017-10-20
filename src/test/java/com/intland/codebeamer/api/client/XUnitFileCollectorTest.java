package com.intland.codebeamer.api.client;

import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class XUnitFileCollectorTest {

    File dirWithNonXmlFile;
    File emptyDir;
    File dirWithOneFile;
    File dirWithSixFiles;
    File dirWithSubDirs;
    private XUnitFileCollector collector;

    @BeforeSuite
    public void getXUnitResultReader() throws IOException {
        dirWithNonXmlFile = getDirWithNonXmlFile();
        emptyDir = getEmptyDir();
        dirWithOneFile = getTestResultDirWithOneFile();
        dirWithSixFiles = getTestResultDirWithSixFiles();
        dirWithSubDirs = getTestResultDirWithSubDirs();
    }

    @BeforeMethod
    public void beforeMethod() {
        this.collector = new XUnitFileCollector();
    }

    @Test
    public void testListFiles() throws Exception {
        ArgumentCaptor<String> logCaptor = ArgumentCaptor.forClass(String.class);
        Logger logger = mock(Logger.class);

        XUnitFileCollector collectorWithCustomLogger = new XUnitFileCollector(logger);
        collectorWithCustomLogger.listFiles(collector.getFiles(dirWithOneFile));

        Mockito.verify(logger).info(logCaptor.capture());
        List<String> log = logCaptor.getAllValues();
        String first = log.remove(0);
        Assert.assertEquals(first, "File AclRemotingTests.xml");
    }

    @Test(dataProvider = "testResultDirProvider")
    public void testGetListOfFiles(File[] directories, int expectedLength) throws Exception {
        for (File file : directories) {
            collector.addDirectory(file);
        }
        File[] actualFiles = collector.getFiles();

        Assert.assertEquals(actualFiles.length, expectedLength, "number of files");
    }

    @DataProvider(name = "testResultDirProvider")
    private Object[][] testResultDirProvider() throws IOException {
        return new Object[][]{
                {new File[]{dirWithNonXmlFile}, 0},
                {new File[]{emptyDir}, 0},
                {new File[]{dirWithOneFile}, 1},
                {new File[]{dirWithSixFiles}, 6},

                {new File[]{dirWithOneFile, dirWithSixFiles}, 7},
                {new File[]{dirWithNonXmlFile, dirWithOneFile}, 1},
                {new File[]{dirWithNonXmlFile, dirWithSixFiles}, 6},
                {new File[]{dirWithNonXmlFile, dirWithOneFile, dirWithSixFiles}, 7},

                {new File[]{dirWithOneFile, dirWithOneFile}, 1},
                {new File[]{dirWithOneFile, dirWithOneFile, dirWithSixFiles}, 7},

                {new File[]{dirWithSubDirs}, 3},
                {new File[]{dirWithNonXmlFile, dirWithSubDirs}, 3},
                {new File[]{dirWithOneFile, dirWithSubDirs}, 4},
                {new File[]{dirWithSixFiles, dirWithSubDirs}, 9},
                {new File[]{dirWithSubDirs, dirWithSubDirs}, 3},
        };
    }

    private File getTestResultDirWithSubDirs() throws IOException {
    	File dir = createTempDir("dir_with_subdirs_", new File(System.getProperty("java.io.tmpdir")));
    	copyFiles(dir, new HashSet<String>(Arrays.asList(new String[] {"ArtifactRemotingTests.xml"})));
    	File subDir1 = createTempDir("sub1", dir);
        copyFiles(subDir1, new HashSet<String>(Arrays.asList(new String[] {"GeneralRemotingTests.xml"})));
    	File subDir2 = createTempDir("sub2", dir);
    	subDir2.mkdir();
        copyFiles(subDir2, new HashSet<String>(Arrays.asList(new String[] {"TestManagementRemotingTests.xml"})));
        return dir;
    }

    private File getTestResultDirWithSixFiles() throws IOException {
    	File dir = createTempDir("dir_with_six_files_", new File(System.getProperty("java.io.tmpdir")));
        copyFiles(dir, new HashSet<String>(Arrays.asList(new String[] {"ArtifactRemotingTests.xml", "GeneralRemotingTests.xml", "TestManagementRemotingTests.xml", "TrackerItemAttachmentRemotingTests.xml", "TrackerItemRemotingTests.xml", "WikiPageRemotingTests.xml"})));
        return dir;
    }

    private File getTestResultDirWithOneFile() throws IOException {
    	File dir = createTempDir("dir_with_one_file_", new File(System.getProperty("java.io.tmpdir")));
        copyFiles(dir, new HashSet<String>(Arrays.asList(new String[] {"AclRemotingTests.xml"})));
        return dir;
    }

    private File getEmptyDir() throws IOException {
        return createTempDir("empty_dir_", new File(System.getProperty("java.io.tmpdir")));
    }

    private File getDirWithNonXmlFile() throws IOException {
    	File dir = createTempDir("dir_with_non_xml_file_", new File(System.getProperty("java.io.tmpdir")));
    	copyFiles(dir, new HashSet<String>(Arrays.asList("not_a_result.txt")));
    	return dir;
    }
    
    private void copyFiles(File outputDirectory, Set<String> fileNames) throws IOException {
    	
        for (String fileName : fileNames) {
        	File outputFile = new File(outputDirectory, fileName);
            if (outputFile.exists()) {
            	outputFile.delete();
            }
            IOUtils.copy(new FileInputStream(new File(ClassLoader.getSystemResource(String.format("test_results/%s", fileName)).getPath())), new FileOutputStream(outputFile));
        }
    }
    
    private File createTempDir(String name, File parent) {
    	File dir = new File(parent, name);
        if (dir.exists()) {
        	dir.delete();
        }
        dir.mkdir();
        return dir;
    }
}
