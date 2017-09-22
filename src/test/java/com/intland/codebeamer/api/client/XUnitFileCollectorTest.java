package com.intland.codebeamer.api.client;

import org.apache.log4j.Logger;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
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
        Path tempDir = Files.createTempDirectory("dir_with_subdirs_");
        Path sub1 = Files.createDirectory(Paths.get(tempDir + "/sub1"));
        Path sub2 = Files.createDirectory(Paths.get(tempDir + "/sub2"));
        logger.debug(tempDir);

        Files.copy(Paths.get(ClassLoader.getSystemResource("test_results/ArtifactRemotingTests.xml").getPath()), Paths.get(tempDir + "/ArtifactRemotingTests.xml"), REPLACE_EXISTING);
        Files.copy(Paths.get(ClassLoader.getSystemResource("test_results/GeneralRemotingTests.xml").getPath()), Paths.get(sub1 + "/GeneralRemotingTests.xml"), REPLACE_EXISTING);
        Files.copy(Paths.get(ClassLoader.getSystemResource("test_results/TestManagementRemotingTests.xml").getPath()), Paths.get(sub2 + "/TestManagementRemotingTests.xml"), REPLACE_EXISTING);

        return tempDir.toFile();
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

    private File getDirWithNonXmlFile() throws IOException {
        Path tempDir = Files.createTempDirectory("dir_with_non_xml_file_");
        logger.debug(tempDir);
        Files.copy(Paths.get(ClassLoader.getSystemResource("test_results/not_a_result.txt").getPath()), Paths.get(tempDir + "/not_a_result.txt"), REPLACE_EXISTING);
        return tempDir.toFile();
    }
}
