/*
 * Copyright (c) 2017 Intland Software (support@intland.com)
 *
 */

package com.intland.codebeamer.api.client;

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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashSet;

public class RestAdapterImpl implements RestAdapter {

    private static final int TIMEOUT = 500;
    private static final String REST_PATH = "/rest";

    private static Logger logger = Logger.getLogger(RestAdapter.class);

    private HttpClient client;
    private RequestConfig requestConfig;
    private Configuration configuration;

    public RestAdapterImpl(Configuration config, HttpClient httpClient) {
        this.configuration = config;
        this.client = httpClient != null ? httpClient : buildHttpClient();
        this.requestConfig = buildRequestConfig();
    }

    private RequestConfig buildRequestConfig() {
        return RequestConfig
                .custom()
                .setConnectTimeout(TIMEOUT)
                .build();
    }

    private HttpClient buildHttpClient() {
        return HttpClientBuilder
                .create()
                .setDefaultHeaders(getDefaultHeaders(configuration.getUsername(), configuration.getPassword()))
                .build();
    }

    private HashSet<Header> getDefaultHeaders(String username, String password) {
        HashSet<Header> defaultHeaders = new HashSet<>();

        defaultHeaders.add(getAuthenticationHeader(username, password));

        return defaultHeaders;
    }

    private BasicHeader getAuthenticationHeader(String username, String password) {
        final String authHeader = "Basic " + Base64.encode((username + ":" + password).getBytes(Charsets.UTF_8));
        return new BasicHeader(HttpHeaders.AUTHORIZATION, authHeader);
    }

    @Override
    public Version getVersion() throws Exception {
        String response = executeGet(configuration.getUri() + REST_PATH + "/version");
        return Version.getVersionFromString(response);
    }

    @Override
    public Boolean testConnection() {
        try {
            Version version = getVersion();
            logger.info("Connection successful. CodeBeamer Version is " + version.toString());
            return true;
        } catch (Exception ex) {
            logger.info("Connection not successful.");
            logger.error(ex);
            return false;
        }
    }

    private String executeGet(String uri) throws Exception {
        HttpGet get = new HttpGet(uri);
        get.setConfig(requestConfig);

        return executeRest(get);
    }

    private String executePost(String uri, String content) throws Exception {
        StringEntity entity = new StringEntity(content, Charsets.UTF_8);
        entity.setContentType("application/json");

        HttpPost post = new HttpPost(uri);
        post.setConfig(requestConfig);
        post.setEntity(entity);

        return executeRest(post);
    }

    private String executeRest(HttpRequestBase request) throws Exception {
        logger.debug(String.format("%s-request to %s", request.getConfig(), request.getURI()));
        try {
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
                throw new InvalidCredentialsException("incorrect credentials");
            }
            return new BasicResponseHandler().handleResponse(response);
        } catch (IOException ex) {
            logger.warn(ex);
            throw new ConnectTimeoutException(String.format("%s-request to %s timed out", request.getConfig(), request.getURI()));
        } finally {
            request.releaseConnection();
        }
    }
}
