package com.intland.codebeamer.api.client;

import org.apache.log4j.Logger;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.mockito.Mockito.mock;

public class XUnitFileCollectorTest {

    private static Logger logger = Logger.getLogger(XUnitFileCollectorTest.class);
    File emptyDir;
    File dirWithOneFile;
    File dirWithSixFiles;
    private XUnitFileCollector collector;

    @BeforeSuite
    public void getXUnitResultReader() throws IOException {
        collector = new XUnitFileCollector();
        emptyDir = getEmptyDir();
        dirWithOneFile = getTestResultDirWithOneFile();
        dirWithSixFiles = getTestResultDirWithSixFiles();
    }

    @Test
    public void testGetListOfFilesForMultipleDirectories_withOneFileTotal() throws Exception {
        File[] directories = new File[]{dirWithOneFile, emptyDir};
        File[] actualFiles = collector.getListOfFilesForMultipleDirectories(directories);

        Assert.assertEquals(actualFiles.length, 1);
    }

    @Test
    public void testGetListOfFilesForMultipleDirectories_withSevenFilesTotal() throws Exception {
        File[] directories = new File[]{dirWithOneFile, dirWithSixFiles};
        File[] actualFiles = collector.getListOfFilesForMultipleDirectories(directories);

        Assert.assertEquals(actualFiles.length, 7);
    }

    @Test
    public void testGetListOfFilesForMultipleDirectories_withZeroFilesTotal() throws Exception {
        File[] directories = new File[]{emptyDir, emptyDir};
        File[] actualFiles = collector.getListOfFilesForMultipleDirectories(directories);

        Assert.assertEquals(actualFiles.length, 0);
    }

    @Test
    public void testGetListOfFilesForDirectory_forEmptyDirectory() throws Exception {
        File[] actualFiles = collector.getListOfFilesForDirectory(emptyDir);

        Assert.assertEquals(actualFiles.length, 0);
    }

    @Test
    public void testGetListOfFilesForDirectory_forDirectoryWithOneFile() throws Exception {
        File[] actualFiles = collector.getListOfFilesForDirectory(dirWithOneFile);

        Assert.assertEquals(actualFiles.length, 1);
    }

    @Test
    public void testGetListOfFilesForDirectory_forDirectoryWithSixFiles() throws Exception {
        File[] actualFiles = collector.getListOfFilesForDirectory(dirWithSixFiles);

        Assert.assertEquals(actualFiles.length, 6);
    }

    @Test
    public void testListFiles() throws Exception {
        ArgumentCaptor<String> logCaptor = ArgumentCaptor.forClass(String.class);
        Logger logger = mock(Logger.class);

        XUnitFileCollector collectorWithCustomLogger = new XUnitFileCollector(logger);
        collectorWithCustomLogger.listFiles(collector.getListOfFilesForDirectory(dirWithOneFile));

        Mockito.verify(logger).info(logCaptor.capture());
        List<String> log = logCaptor.getAllValues();
        String first = log.remove(0);
        Assert.assertEquals(first, "File AclRemotingTests.xml");
    }

    private File getTestResultDirWithSixFiles() throws IOException {
        Path tempDir = Files.createTempDirectory("dir_with_six_files_");
        logger.debug(tempDir);
        Files.copy(Paths.get(ClassLoader.getSystemResource("test_results/ArtifactRemotingTests.xml").getPath()), Paths.get(tempDir + "/ArtifactRemotingTests.xml"), REPLACE_EXISTING);
        Files.copy(Paths.get(ClassLoader.getSystemResource("test_results/GeneralRemotingTests.xml").getPath()), Paths.get(tempDir + "/GeneralRemotingTests.xml"), REPLACE_EXISTING);
        Files.copy(Paths.get(ClassLoader.getSystemResource("test_results/TestManagementRemotingTests.xml").getPath()), Paths.get(tempDir + "/TestManagementRemotingTests.xml"), REPLACE_EXISTING);
        Files.copy(Paths.get(ClassLoader.getSystemResource("test_results/TrackerItemAttachmentRemotingTests.xml").getPath()), Paths.get(tempDir + "/TrackerItemAttachmentRemotingTests.xml"), REPLACE_EXISTING);
        Files.copy(Paths.get(ClassLoader.getSystemResource("test_results/TrackerItemRemotingTests.xml").getPath()), Paths.get(tempDir + "/TrackerItemRemotingTests.xml"), REPLACE_EXISTING);
        Files.copy(Paths.get(ClassLoader.getSystemResource("test_results/WikiPageRemotingTests.xml").getPath()), Paths.get(tempDir + "/WikiPageRemotingTests.xml"), REPLACE_EXISTING);
        return tempDir.toFile();
    }

    private File getTestResultDirWithOneFile() throws IOException {
        Path tempDir = Files.createTempDirectory("dir_with_one_file_");
        logger.debug(tempDir);
        Files.copy(Paths.get(ClassLoader.getSystemResource("test_results/AclRemotingTests.xml").getPath()), Paths.get(tempDir + "/AclRemotingTests.xml"), REPLACE_EXISTING);
        return tempDir.toFile();
    }

    private File getEmptyDir() throws IOException {
        Path tempDir = Files.createTempDirectory("empty_dir_");
        logger.debug(tempDir);
        return tempDir.toFile();
    }
}
