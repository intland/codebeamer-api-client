/*
 * Copyright (c) 2017 Intland Software (support@intland.com)
 *
 */

package com.intland.codebeamer.api.client.rest;

import com.intland.codebeamer.api.client.Configuration;
import com.intland.codebeamer.api.client.Version;
import jcifs.util.Base64;
import org.apache.commons.codec.Charsets;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
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
    public Version getVersion() throws CodebeamerNotAccessibleException {
        String response = executeGet(configuration.getUri() + REST_PATH + "/version");
        return Version.getVersionFromString(response);
    }

    @Override
    public Boolean testConnection() throws CodebeamerNotAccessibleException {
        try {
            Version version = getVersion();
            logger.info("Connection successful. CodeBeamer Version is " + version.toString());
            return true;
        } catch (ConnectionFailedException ex) {
            logger.error("Connection not successful. Please check CodeBeamer address: " + configuration.getUri());
            throw new CodebeamerNotAccessibleException(ex);
        } catch (InvalidCredentialsException ex) {
            logger.error("Connection not successful. Please check the credentials");
            throw new CodebeamerNotAccessibleException(ex);
        }
    }

    private String executeGet(String uri) throws CodebeamerNotAccessibleException {
        HttpGet get = new HttpGet(uri);
        get.setConfig(requestConfig);

        return executeRest(get);
    }

    private String executePost(String uri, String content) throws CodebeamerNotAccessibleException {
        StringEntity entity = new StringEntity(content, Charsets.UTF_8);
        entity.setContentType("application/json");

        HttpPost post = new HttpPost(uri);
        post.setConfig(requestConfig);
        post.setEntity(entity);

        return executeRest(post);
    }

    private String executeRest(HttpRequestBase request) throws CodebeamerNotAccessibleException {
        logger.debug(String.format("%s-request to %s", request.getConfig(), request.getURI()));
        try {
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
                throw new InvalidCredentialsException("incorrect credentials");
            }
            return new BasicResponseHandler().handleResponse(response);
        } catch (IOException ex) {
            throw new ConnectionFailedException(String.format("%s-request to %s timed out", request.getConfig(), request.getURI()), ex);
        } finally {
            request.releaseConnection();
        }
    }
}
