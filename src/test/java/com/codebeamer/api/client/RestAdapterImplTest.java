package com.codebeamer.api.client;

import org.apache.http.auth.InvalidCredentialsException;
import org.apache.http.conn.ConnectTimeoutException;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class RestAdapterImplTest {

    private String baseUrl;

    @BeforeSuite
    public void getBaseUrl() {
        baseUrl = System.getProperty("baseUrl", "http://10.200.10.1:8080/cb");
    }

    @Test
    public void testGetVersion_withCorrectEndpoint_withCorrectCredentials() throws Exception {
        RestAdapter rest = getRestAdapterWithCorrectEndpointAndCredentials();
        Assert.assertTrue(rest.getVersion() instanceof Version);
    }

    @Test(expectedExceptions = ConnectTimeoutException.class)
    public void testGetVersion_withIncorrectEndpoint_shouldThrowException() throws Exception {
        RestAdapter rest = getRestAdapterWithIncorrectEndpoint();
        rest.getVersion();
    }

    @Test(expectedExceptions = InvalidCredentialsException.class)
    public void testGetVersion_withCorrectEndpoint_WithIncorrectCredentials() throws Exception {
        RestAdapter rest = getRestAdapterWithCorrectEndpointAndIncorrectCredentials();
        rest.getVersion();
    }

    private RestAdapter getRestAdapterWithCorrectEndpointAndCredentials() {
        Configuration config = new Configuration(baseUrl, "bond", "007");
        return new RestAdapterImpl(config);
    }

    private RestAdapter getRestAdapterWithCorrectEndpointAndIncorrectCredentials() {
        Configuration config = new Configuration(baseUrl, "bond", "invalid");
        return new RestAdapterImpl(config);
    }

    private RestAdapter getRestAdapterWithIncorrectEndpoint() {
        Configuration config = new Configuration("http://127.0.0.1:10000", "bond", "007");
        return new RestAdapterImpl(config);
    }
}
