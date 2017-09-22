package com.intland.codebeamer.api.client.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intland.codebeamer.api.client.CodebeamerApiConfiguration;
import com.intland.codebeamer.api.client.Version;
import com.intland.codebeamer.api.client.dto.TrackerDto;
import com.intland.codebeamer.api.client.dto.TrackerItemDto;
import com.intland.codebeamer.api.client.dto.TrackerTypeDto;
import org.apache.commons.codec.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.log4j.Logger;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.mockito.Mockito.*;

public class RestAdapterImplTest {
    private static Logger logger = Logger.getLogger(RestAdapterImplTest.class);
    private HttpClient clientMock;
    private HttpResponse responseMock;
    private StatusLine statusLineMock;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeMethod
    public void setupMocks() {
        clientMock = mock(HttpClient.class);
        responseMock = mock(HttpResponse.class);
        statusLineMock = mock(StatusLine.class);

        CodebeamerApiConfiguration.getInstance()
                .withUri("http://localhost:8080/cb")
                .withUsername("bond")
                .withPassword("007")
                .withTestConfigurationId(10)
                .withTestCaseTrackerId(20)
                .withTestSetTrackerId(30)
                .withTestRunTrackerId(40);
    }

    @Test(dataProvider = "trackerItemProvider")
    public void testGetTrackerItem(int statusCode, String uri, Integer expectedItemId, Class<RequestFailed> exceptionClass) throws Exception {
        TrackerItemDto expectedItemDto = new TrackerItemDto();
        expectedItemDto.setUri(uri);
        HttpEntity entity = new StringEntity(objectMapper.writeValueAsString(expectedItemDto));

        when(statusLineMock.getStatusCode()).thenReturn(statusCode);
        when(responseMock.getStatusLine()).thenReturn(statusLineMock);
        when(responseMock.getEntity()).thenReturn(entity);
        when(clientMock.execute(Mockito.any(HttpGet.class))).thenReturn(responseMock);
        RestAdapter rest = new RestAdapterImpl(clientMock);

        try {
            TrackerItemDto actual = rest.getTrackerItem(1);
            Assert.assertEquals(actual.getId(), expectedItemId, "id must match");
        } catch (RequestFailed ex) {
            checkException(ex, exceptionClass, statusCode);
        }
    }

    @DataProvider(name = "trackerItemProvider")
    public Object[][] trackerItemProvider() {
        return new Object[][]{
                {HttpStatus.SC_OK, "/item/1", 1, null},
                {HttpStatus.SC_UNAUTHORIZED, "/item/1", 1, InvalidCredentialsException.class},
                {HttpStatus.SC_NOT_FOUND, "/item/1", 1, ItemNotFoundException.class},
        };
    }

    @Test(dataProvider = "trackerProvider")
    public void testGetTracker(int statusCode, String content, Integer trackerTypeId, String trackerTypeName, Class<RequestFailed> exceptionClass) throws Exception {
        HttpEntity entity = new StringEntity(content);

        when(statusLineMock.getStatusCode()).thenReturn(statusCode);
        when(responseMock.getStatusLine()).thenReturn(statusLineMock);
        when(responseMock.getEntity()).thenReturn(entity);
        when(clientMock.execute(Mockito.any(HttpGet.class))).thenReturn(responseMock);
        RestAdapter rest = new RestAdapterImpl(clientMock);

        try {
            TrackerDto actual = rest.getTracker(1);
            Assert.assertEquals(actual.getType().getTypeId(), trackerTypeId, "tracker type id");
            Assert.assertEquals(actual.getType().getName(), trackerTypeName, "tracker type name");
        } catch (RequestFailed ex) {
            checkException(ex, exceptionClass, statusCode);
        }
    }

    @DataProvider(name = "trackerProvider")
    public Object[][] trackerProvider() {
        return new Object[][]{
                {HttpStatus.SC_OK, "{\"type\":{\"uri\":\"/category/type/1\", \"name\":\"Dummy Name\"}}", 1, "Dummy Name", null},
                {HttpStatus.SC_UNAUTHORIZED, "{\"type\":{\"uri\":\"/category/type/1\", \"name\":\"Dummy Name\"}}", 1, "Dummy Name", InvalidCredentialsException.class},
                {HttpStatus.SC_NOT_FOUND, "{\"type\":{\"uri\":\"/category/type/1\", \"name\":\"Dummy Name\"}}", 1, "Dummy Name", ItemNotFoundException.class},
        };
    }

    @Test(dataProvider = "trackerTypeProvider")
    public void testGetTrackerType(int statusCode, String uri, String name, Integer expectedId, Class<RequestFailed> exceptionClass) throws Exception {
        TrackerTypeDto expectedTypeDto = new TrackerTypeDto();
        expectedTypeDto.setUri(uri);
        expectedTypeDto.setName(name);
        HttpEntity entity = new StringEntity(objectMapper.writeValueAsString(expectedTypeDto));

        TrackerDto tracker = new TrackerDto();
        tracker.setType(expectedTypeDto);

        when(statusLineMock.getStatusCode()).thenReturn(statusCode);
        when(responseMock.getStatusLine()).thenReturn(statusLineMock);
        when(responseMock.getEntity()).thenReturn(entity);
        when(clientMock.execute(Mockito.any(HttpGet.class))).thenReturn(responseMock);
        RestAdapter rest = new RestAdapterImpl(clientMock);

        try {
            TrackerTypeDto actual = rest.getTrackerType(expectedId);
            Assert.assertEquals(actual.getTypeId(), expectedId, "tracker type id");
            Assert.assertEquals(actual.getName(), name, "tracker type name");
        } catch (RequestFailed ex) {
            checkException(ex, exceptionClass, statusCode);
        }
    }

    @DataProvider(name = "trackerTypeProvider")
    public Object[][] trackerTypeProvider() {
        return new Object[][]{
                {HttpStatus.SC_OK, "/tracker/type/1", "Dummy Name", 1, null},
                {HttpStatus.SC_UNAUTHORIZED, "/tracker/type/1", "Dummy Name", 1, InvalidCredentialsException.class},
                {HttpStatus.SC_NOT_FOUND, "/tracker/type/1", "Dummy Name", 1, ItemNotFoundException.class},
        };
    }

    private void checkException(Exception actual, Class<RequestFailed> expected, int statusCode) throws Exception {
        Assert.assertEquals(actual.getClass().getSimpleName(), expected.getSimpleName(), "Statuscode " + statusCode);
    }

    @Test
    public void testGetVersion_withCorrectEndpoint_withCorrectCredentials() throws Exception {
        HttpClient client = mock(HttpClient.class);
        HttpResponse response = mock(HttpResponse.class);
        HttpEntity entity = new StringEntity("\"8.3.0\"");
        StatusLine statusLine = mock(StatusLine.class);

        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(client.execute(Mockito.any(HttpGet.class))).thenReturn(response);

        RestAdapter rest = new RestAdapterImpl(client);

        Assert.assertTrue(rest.getVersion() instanceof Version);
    }

    @Test(expectedExceptions = {RequestFailed.class, ConnectionFailedException.class})
    public void testGetVersion_withIncorrectEndpoint_shouldThrowException() throws Exception {
        HttpClient client = mock(HttpClient.class);
        when(client.execute(Mockito.any(HttpGet.class))).thenThrow(new IOException("simulated timeout"));

        RestAdapter rest = new RestAdapterImpl(client);
        rest.getVersion();
    }

    @Test(expectedExceptions = {RequestFailed.class, InvalidCredentialsException.class})
    public void testGetVersion_withCorrectEndpoint_WithIncorrectCredentials() throws Exception {
        HttpClient client = mock(HttpClient.class);
        HttpResponse response = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);

        when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_UNAUTHORIZED);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(client.execute(Mockito.any(HttpGet.class))).thenReturn(response);

        RestAdapter rest = new RestAdapterImpl(client);
        rest.getVersion();
    }

    @Test(dataProvider = "testConnectionProvider")
    public void testTestConnection(boolean expected) throws Exception {
        if (expected) {
            when(statusLineMock.getStatusCode()).thenReturn(HttpStatus.SC_OK);
            when(responseMock.getStatusLine()).thenReturn(statusLineMock);
            when(responseMock.getEntity()).thenReturn(new StringEntity(""));
            when(clientMock.execute(Mockito.any(HttpGet.class))).thenReturn(responseMock);
        } else {
            when(clientMock.execute(Mockito.any(HttpGet.class))).thenThrow(new IOException("simulated timeout"));
        }

        RestAdapter rest = new RestAdapterImpl(clientMock);
        Assert.assertEquals(rest.testConnection(), expected, "test connection");
    }

    @DataProvider(name = "testConnectionProvider")
    private Object[][] testConnectionProvider() {
        return new Object[][]{
                {true},
                {false},
        };
    }

    @Test(dataProvider = "testCredentialsProvider")
    public void testTestCredentials(int statusCode, boolean expected) throws Exception {
        when(statusLineMock.getStatusCode()).thenReturn(statusCode);
        when(responseMock.getStatusLine()).thenReturn(statusLineMock);
        when(responseMock.getEntity()).thenReturn(new StringEntity("\"8.3.0\""));
        when(clientMock.execute(Mockito.any(HttpGet.class))).thenReturn(responseMock);

        RestAdapter rest = new RestAdapterImpl(clientMock);

        Assert.assertEquals(rest.testCredentials(), expected, "test credentials");
    }

    @DataProvider(name = "testCredentialsProvider")
    private Object[][] testCredentialsProvider() {
        return new Object[][]{
                {HttpStatus.SC_OK, true},
                {HttpStatus.SC_UNAUTHORIZED, false},
                {HttpStatus.SC_NOT_FOUND, false},
        };
    }

    @Test(dataProvider = "fileListProvider")
    public void testUploadXUnitResults(File[] files, int statusCode, int numberOfRequest) throws Exception {
        ArgumentCaptor<HttpPost> requestCaptor = ArgumentCaptor.forClass(HttpPost.class);

        when(statusLineMock.getStatusCode()).thenReturn(statusCode);
        when(responseMock.getStatusLine()).thenReturn(statusLineMock);
        when(clientMock.execute(Mockito.any(HttpPost.class))).thenReturn(responseMock);

        RestAdapter rest = new RestAdapterImpl(clientMock);

        // check whether successful
        if (statusCode >= 200 && statusCode < 300) {
            rest.uploadXUnitResults(files);
            Assert.assertTrue(true, "upload was successful");
        } else {
            try {
                rest.uploadXUnitResults(files);
                Assert.assertFalse(true, "upload was successful");
            } catch (RequestFailed ex) {
                // swallow
            }
        }

        // check number of requests
        Mockito.verify(clientMock, times(numberOfRequest)).execute(Mockito.any(HttpPost.class));

        // check content of request
        Mockito.verify(clientMock, times(numberOfRequest)).execute(requestCaptor.capture());
        List<HttpPost> posts = requestCaptor.getAllValues();
        for (HttpPost post : posts) {
            checkUploadedEntity(post.getEntity(), files);
            checkConfigurationIsIncluded(post.getEntity());
        }
    }

    @DataProvider(name = "fileListProvider")
    private Object[][] fileListProvider() {
        File one = new File(ClassLoader.getSystemResource("test_results/AclRemotingTests.xml").getFile());
        File two = new File(ClassLoader.getSystemResource("test_results/ArtifactRemotingTests.xml").getFile());
        File three = new File(ClassLoader.getSystemResource("test_results/GeneralRemotingTests.xml").getFile());
        return new Object[][]{
                {new File[]{one}, HttpStatus.SC_CREATED, 1},
                {new File[]{one}, HttpStatus.SC_UNAUTHORIZED, 1},
                {new File[]{one, two, three}, HttpStatus.SC_CREATED, 1},
                {new File[]{one, two, three}, HttpStatus.SC_UNAUTHORIZED, 1},
        };
    }

    private void checkUploadedEntity(HttpEntity entity, File[] files) throws Exception {
        String body = IOUtils.toString(entity.getContent(), Charsets.UTF_8);
        long totalFileSize = 0;
        for (File file : files) {
            String fileContent = new String(Files.readAllBytes(file.toPath()));
            totalFileSize += file.length();
            Assert.assertTrue(body.contains(fileContent), String.format("body contains content of %s", file.getName()));
        }
        Assert.assertTrue(totalFileSize < entity.getContentLength(), "entity length is greater than combined file size");
    }

    private void checkConfigurationIsIncluded(HttpEntity entity) throws Exception {
        String body = IOUtils.toString(entity.getContent(), Charsets.UTF_8);
        CodebeamerApiConfiguration config = CodebeamerApiConfiguration.getInstance();
        Assert.assertTrue(body.contains(String.format("\"testConfigurationId\":%s", config.getTestConfigurationId())), "testConfigurationId is included");
        Assert.assertTrue(body.contains(String.format("\"testSetTrackerId\":%s", config.getTestSetTrackerId())), "testSetTrackerId is included");
        Assert.assertTrue(body.contains(String.format("\"testCaseTrackerId\":%s", config.getTestCaseTrackerId())), "testCaseTrackerId is included");
        Assert.assertTrue(body.contains(String.format("\"testRunTrackerId\":%s", config.getTestRunTrackerId())), "testRunTrackerId is included");
    }
}
