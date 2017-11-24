/*
 * Copyright (c) 2017 Intland Software (support@intland.com)
 *
 */

package com.intland.codebeamer.api.client.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intland.codebeamer.api.client.CodebeamerApiConfiguration;
import com.intland.codebeamer.api.client.Version;
import com.intland.codebeamer.api.client.dto.TestResultContextDto;
import com.intland.codebeamer.api.client.dto.TrackerDto;
import com.intland.codebeamer.api.client.dto.TrackerItemDto;
import com.intland.codebeamer.api.client.dto.TrackerTypeDto;
import jcifs.util.Base64;
import org.apache.commons.codec.Charsets;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

public class RestAdapterImpl implements RestAdapter {

    private static final String REST_PATH = "/rest";
    private static final int ATTEMPT_THRESHLD = 3;

    private static Logger logger = Logger.getLogger(RestAdapter.class);
    private ObjectMapper objectMapper = new ObjectMapper();

    private HttpClient client;
    private RequestConfig requestConfig;

    public RestAdapterImpl(HttpClient httpClient) {
        this.client = httpClient != null ? httpClient : buildHttpClient();
        this.requestConfig = buildRequestConfig();
    }

    private RequestConfig buildRequestConfig() {
        return RequestConfig
                .custom()
                .build();
    }

    private HttpClient buildHttpClient() {
        return HttpClientBuilder
                .create()
                .setDefaultHeaders(getDefaultHeaders(CodebeamerApiConfiguration.getInstance().getUsername(), CodebeamerApiConfiguration.getInstance().getPassword()))
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
    public Version getVersion() throws RequestFailed {
        String uri = String.format("%s/version", REST_PATH);
        String response = executeGet(uri);
        return Version.getVersionFromString(response);
    }

    @Override
    public TrackerItemDto getTrackerItem(Integer id) throws RequestFailed {
        String uri = String.format("%s/item/%s", REST_PATH, id);
        String response = executeGet(uri);
        try {
            return objectMapper.readValue(response, TrackerItemDto.class);
        } catch (IOException ex) {
            logger.error(ex);
            return null;
        }
    }

    @Override
    public TrackerDto getTracker(Integer id) throws RequestFailed {
        String uri = String.format("%s/tracker/%s", REST_PATH, id);
        String response = executeGet(uri);
        try {
            return objectMapper.readValue(response, TrackerDto.class);
        } catch (IOException e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    public TrackerTypeDto getTrackerType(Integer id) throws RequestFailed {
        String uri = String.format("%s/tracker/type/%s", REST_PATH, id);
        String response = executeGet(uri);
        try {
            return objectMapper.readValue(response, TrackerTypeDto.class);
        } catch (IOException e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    public boolean testConnection() {
        try {
            executeGet("");
            return true;
        } catch (IOException ex) {
            logger.debug(ex);
            return false;
        }
    }

    @Override
    public boolean testCredentials() {
        try {
            Version version = getVersion();
            logger.info("Connection successful. CodeBeamer Version is " + version.toString());
            return true;
        } catch (RequestFailed ex) {
            logger.error(ex);
            return false;
        }
    }

    @Override
    public void uploadXUnitResults(File[] files) throws RequestFailed {
        String uri = String.format("%s/xunitresults", REST_PATH);
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.STRICT);

        TestResultContextDto configurationDto = CodebeamerApiConfiguration.getInstance().getTestResultConfigurationDto();
        try {
            multipartEntityBuilder.addTextBody("configuration", objectMapper.writeValueAsString(configurationDto), ContentType.APPLICATION_JSON);
        } catch (JsonProcessingException ex) {
            logger.error(ex);
            throw new RequestFailed(ex);
        }

        for (File file : files) {
            logger.info(String.format("preparing to upload %s with a size of %d bytes...", file.getName(), file.length()));
            multipartEntityBuilder.addBinaryBody(file.getName(), file);
        }

        RequestConfig requestConfigForUpload = RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(5000).setSocketTimeout(1000 * 60 * 20).build();
        
        HttpEntity entity = multipartEntityBuilder.build();
        try {
            executePost(uri, entity, requestConfigForUpload);
        } catch (RequestFailed ex) {
            logger.error(ex);
            throw ex;
        }
    }

    private String executeGet(String uri) throws RequestFailed {
        HttpGet get = new HttpGet(CodebeamerApiConfiguration.getInstance().getUri() + uri);
        get.setConfig(requestConfig);

        return executeRest(get);
    }

    private String executePost(String uri, HttpEntity entity, RequestConfig requestConfig) throws RequestFailed {
        HttpPost post = new HttpPost(CodebeamerApiConfiguration.getInstance().getUri() + uri);
        post.setConfig(requestConfig == null ? this.requestConfig : requestConfig);
        post.setEntity(entity);

        return executeRest(post);
    }

    private String executeRest(HttpRequestBase request) throws RequestFailed {
        assert client != null;
        logger.debug(String.format("%s-request to %s", request.getMethod(), request.getURI()));
        HttpResponse response = null;
        int attempt = 1;
        while (true) {
	        try {
	            response = client.execute(request);
	            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
	                throw new InvalidCredentialsException("incorrect credentials");
	            }
	            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
	                throw new ItemNotFoundException("cannot find item");
	            }
	            return new BasicResponseHandler().handleResponse(response);
	        } catch (InvalidCredentialsException icex) {
	        	throw icex;
	        } catch (ItemNotFoundException infex) {
	        	throw infex;
	        } catch (IOException ex) {
	        	if (attempt == ATTEMPT_THRESHLD) {
	        		throw new ConnectionFailedException(String.format("%s-request to %s timed out", request.getConfig(), request.getURI()), ex);
	        	}
	        	logger.warn(String.format("%s-request to %s timed out, this was the %s. attempt of %s", request.getConfig(), request.getURI(), (attempt), ATTEMPT_THRESHLD));
	        	++attempt;
	        } finally {
	            request.releaseConnection();
	        }
        }
    }
}
