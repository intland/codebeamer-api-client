/*
 * Copyright (c) 2017 Intland Software (support@intland.com)
 *
 */

package com.codebeamer.api.client;

import jcifs.util.Base64;
import org.apache.commons.codec.Charsets;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.InvalidCredentialsException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashSet;

public class RestAdapterImpl implements RestAdapter {

    private static Logger logger = Logger.getLogger(RestAdapter.class);

    private HttpClient client;
    private RequestConfig requestConfig;
    private Configuration configuration;

    private static final int timeout = 500;
    private static final String REST_PATH = "/rest";

    public RestAdapterImpl(Configuration config) {
        this.configuration = config;
        this.client = buildHttpClient();
        this.requestConfig = buildRequestConfig();
    }

    private HttpClient buildHttpClient() {
        return HttpClientBuilder
                .create()
                .setDefaultHeaders(getDefaultHeaders(configuration.getUsername(), configuration.getPassword()))
                .build();
    }

    private RequestConfig buildRequestConfig() {
        return RequestConfig
                .custom()
                .setConnectTimeout(timeout)
                .build();
    }

    private HashSet<Header> getDefaultHeaders(String username, String password) {
        HashSet<Header> defaultHeaders = new HashSet<Header>();

        defaultHeaders.add(getAuthenticationHeader(username, password));

        return defaultHeaders;
    }

    private BasicHeader getAuthenticationHeader(String username, String password) {
        final String authHeader = "Basic " + new Base64().encode((username + ":" + password).getBytes(Charsets.UTF_8));
        return new BasicHeader(HttpHeaders.AUTHORIZATION, authHeader);
    }

    @Override
    public String getVersion() throws ConnectTimeoutException, InvalidCredentialsException {
        return executeGet(configuration.getUri() + REST_PATH + "/version");
    }

    private String executeGet(String uri) throws ConnectTimeoutException, InvalidCredentialsException {
        logger.info(uri);
        HttpGet get = new HttpGet(uri);
        get.setConfig(requestConfig);

        try {
            HttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
                throw new InvalidCredentialsException("incorrect credentials");
            }
            return new BasicResponseHandler().handleResponse(response);
        } catch (IOException ex) {
            logger.debug(ex);
            throw new ConnectTimeoutException("connection to " + uri + " timed out");
        } finally {
            get.releaseConnection();
        }
    }
}
