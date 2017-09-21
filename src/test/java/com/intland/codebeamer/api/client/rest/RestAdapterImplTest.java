package com.intland.codebeamer.api.client.rest;

import com.intland.codebeamer.api.client.CodebeamerApiConfiguration;
import com.intland.codebeamer.api.client.Version;
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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.mockito.Mockito.*;

public class RestAdapterImplTest {
    private static Logger logger = Logger.getLogger(RestAdapterImplTest.class);

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

        RestAdapter rest = new RestAdapterImpl(getDummyConfig(), client);

        Assert.assertTrue(rest.getVersion() instanceof Version);
    }

    @Test(expectedExceptions = {CodebeamerNotAccessibleException.class, ConnectionFailedException.class})
    public void testGetVersion_withIncorrectEndpoint_shouldThrowException() throws Exception {
        HttpClient client = mock(HttpClient.class);
        when(client.execute(Mockito.any(HttpGet.class))).thenThrow(new IOException("simulated timeout"));

        RestAdapter rest = new RestAdapterImpl(getDummyConfig(), client);
        rest.getVersion();
    }

    @Test(expectedExceptions = {CodebeamerNotAccessibleException.class, InvalidCredentialsException.class})
    public void testGetVersion_withCorrectEndpoint_WithIncorrectCredentials() throws Exception {
        HttpClient client = mock(HttpClient.class);
        HttpResponse response = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);

        when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_UNAUTHORIZED);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(client.execute(Mockito.any(HttpGet.class))).thenReturn(response);

        RestAdapter rest = new RestAdapterImpl(getDummyConfig(), client);
        rest.getVersion();
    }

    @Test
    public void testTestConnection_CodebeamerReturns200() throws Exception {
        HttpClient client = mock(HttpClient.class);
        HttpResponse response = mock(HttpResponse.class);
        HttpEntity entity = new StringEntity("\"8.3.0\"");
        StatusLine statusLine = mock(StatusLine.class);

        when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_OK);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(client.execute(Mockito.any(HttpGet.class))).thenReturn(response);

        RestAdapter rest = new RestAdapterImpl(getDummyConfig(), client);
        Assert.assertTrue(rest.testConnection(), "connection test");
    }

    @Test(expectedExceptions = {CodebeamerNotAccessibleException.class, InvalidCredentialsException.class})
    public void testTestConnection_CodebeamerReturns401() throws Exception {
        HttpClient client = mock(HttpClient.class);
        HttpResponse response = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);

        when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_UNAUTHORIZED);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(client.execute(Mockito.any(HttpGet.class))).thenReturn(response);

        RestAdapter rest = new RestAdapterImpl(getDummyConfig(), client);
        Assert.assertFalse(rest.testConnection(), "connection test");
    }

    @Test(expectedExceptions = {CodebeamerNotAccessibleException.class, ConnectionFailedException.class})
    public void testTestConnection_CodebeamerTimesOut() throws Exception {
        HttpClient client = mock(HttpClient.class);

        when(client.execute(Mockito.any(HttpGet.class))).thenThrow(new IOException("simulated timeout"));

        RestAdapter rest = new RestAdapterImpl(getDummyConfig(), client);
        Assert.assertFalse(rest.testConnection(), "connection test");
    }

    @Test(dataProvider = "fileListProvider")
    public void testUploadXUnitResults_checkResult_CodebeamerReturns201(File[] files) throws Exception {
        HttpClient client = mock(HttpClient.class);
        HttpResponse response = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);

        when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_CREATED);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(client.execute(Mockito.any(HttpPost.class))).thenReturn(response);

        RestAdapter rest = new RestAdapterImpl(getDummyConfig(), client);
        Assert.assertTrue(rest.uploadXUnitResults(files), "was upload successful");
    }

    @Test(dataProvider = "fileListProvider")
    public void testUploadXUnitResults_checkResult_CodebeamerReturn401(File[] files) throws Exception {
        HttpClient client = mock(HttpClient.class);
        HttpResponse response = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);

        when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_UNAUTHORIZED);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(client.execute(Mockito.any(HttpPost.class))).thenReturn(response);

        RestAdapter rest = new RestAdapterImpl(getDummyConfig(), client);
        Assert.assertFalse(rest.uploadXUnitResults(files), "was upload successful");
    }

    @Test(dataProvider = "fileListProvider")
    public void testUploadXUnitResults_checkNumberOfRequests(File[] files) throws Exception {
        logger.info(String.format("testing uploadXUnitResults with %d files", files.length));

        HttpClient client = mock(HttpClient.class);
        HttpResponse response = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);

        when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_CREATED);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(client.execute(Mockito.any(HttpPost.class))).thenReturn(response);

        RestAdapter rest = new RestAdapterImpl(getDummyConfig(), client);
        rest.uploadXUnitResults(files);

        Mockito.verify(client, times(1)).execute(Mockito.any(HttpPost.class));
    }

    @Test(dataProvider = "fileListProvider")
    public void testUploadXUnitResults_checkContent(File[] files) throws Exception {
        ArgumentCaptor<HttpPost> requestCaptor = ArgumentCaptor.forClass(HttpPost.class);
        HttpClient client = mock(HttpClient.class);
        HttpResponse response = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);

        when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_CREATED);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(client.execute(Mockito.any(HttpPost.class))).thenReturn(response);

        RestAdapter rest = new RestAdapterImpl(getDummyConfig(), client);
        rest.uploadXUnitResults(files);

        Mockito.verify(client, times(1)).execute(requestCaptor.capture());
        List<HttpPost> posts = requestCaptor.getAllValues();

        for (HttpPost post : posts) {
            checkUploadedEntity(post.getEntity(), files);
        }
    }

    @DataProvider(name = "fileListProvider")
    private Object[][] getFileListWithOneFile() {
        File one = new File(ClassLoader.getSystemResource("test_results/AclRemotingTests.xml").getFile());
        File two = new File(ClassLoader.getSystemResource("test_results/ArtifactRemotingTests.xml").getFile());
        File three = new File(ClassLoader.getSystemResource("test_results/GeneralRemotingTests.xml").getFile());
        return new Object[][]{
                {new File[]{one}},
                {new File[]{one, two, three}},
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

    private CodebeamerApiConfiguration getDummyConfig() {
        return CodebeamerApiConfiguration.getInstance()
                .withUri("http://localhost:8080/cb")
                .withUsername("bond")
                .withPassword("007");
    }
}
