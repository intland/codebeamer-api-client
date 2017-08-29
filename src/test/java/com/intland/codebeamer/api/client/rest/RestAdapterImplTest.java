package com.intland.codebeamer.api.client.rest;

import com.intland.codebeamer.api.client.Configuration;
import com.intland.codebeamer.api.client.Version;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RestAdapterImplTest {

    private static final Configuration config = new Configuration("http:/localhost:8080/cb", "bond", "007");

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

        RestAdapter rest = new RestAdapterImpl(config, client);

        Assert.assertTrue(rest.getVersion() instanceof Version);
    }

    @Test(expectedExceptions = CodebeamerNotAccessibleException.class)
    public void testGetVersion_withIncorrectEndpoint_shouldThrowException() throws Exception {
        HttpClient client = mock(HttpClient.class);
        when(client.execute(Mockito.any(HttpGet.class))).thenThrow(new IOException("simulated timeout"));

        RestAdapter rest = new RestAdapterImpl(config, client);
        rest.getVersion();
    }

    @Test(expectedExceptions = CodebeamerNotAccessibleException.class)
    public void testGetVersion_withCorrectEndpoint_WithIncorrectCredentials() throws Exception {
        HttpClient client = mock(HttpClient.class);
        HttpResponse response = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);

        when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_UNAUTHORIZED);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(client.execute(Mockito.any(HttpGet.class))).thenReturn(response);

        RestAdapter rest = new RestAdapterImpl(config, client);
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

        RestAdapter rest = new RestAdapterImpl(config, client);
        Assert.assertTrue(rest.testConnection(), "connection test");
    }

    @Test
    public void testTestConnection_CodebeamerReturns401() throws Exception {
        HttpClient client = mock(HttpClient.class);
        HttpResponse response = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);

        when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_UNAUTHORIZED);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(client.execute(Mockito.any(HttpGet.class))).thenReturn(response);

        RestAdapter rest = new RestAdapterImpl(config, client);
        Assert.assertFalse(rest.testConnection(), "connection test");
    }

    @Test
    public void testTestConnection_CodebeamerTimesOut() throws Exception {
        HttpClient client = mock(HttpClient.class);

        when(client.execute(Mockito.any(HttpGet.class))).thenThrow(new IOException("simulated timeout"));

        RestAdapter rest = new RestAdapterImpl(config, client);
        Assert.assertFalse(rest.testConnection(), "connection test");
    }
}
