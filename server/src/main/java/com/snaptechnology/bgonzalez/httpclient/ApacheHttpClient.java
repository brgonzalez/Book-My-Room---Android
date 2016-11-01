package com.snaptechnology.bgonzalez.httpclient;

/**
 * Created by bgonzalez on 04/08/2016.
 */


import com.snaptechnology.bgonzalez.model.Location;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public final class ApacheHttpClient {

    static Logger log = Logger.getLogger(ApacheHttpClient.class.getName());


    /** Properties library org.apache.http.client.methods to make requests */
    private HttpClient client;
    private HttpGet getRequest;
    private HttpPost postRequest;
    private HttpDelete deleteRequest;
    private HttpResponse response;
    private HttpPatch patchRequest;

    /** Reader*/
    private BufferedReader br;

    /** Variable to catch output requests */
    private StringBuffer output;

    private Location location;
    private String password ="BrgcBrgc5snap";
    private Encoder encoder;
    private String codeBasicAuth;

    public ApacheHttpClient() {
        this.encoder = new Encoder();
    }


    /**
     * Method to make a get request
     * @param resource is the URL's request
     * @return value status code
     */
    public StatusLine getHttpRequest(String resource) {
        setClient();
        setGetRequest(resource);
        setResponse(getRequest);
        setBr();
        setOutput();
        log.info("Status code : " + response.getStatusLine().getStatusCode());
        return response.getStatusLine();
    }
    /**
     * Method to make a delete request
     * @param resource is the URL's request
     * @return value status code
     */
    public StatusLine deleteHttpRequest(String resource) {
        setClient();
        setDeleteRequest(resource);
        setResponse(deleteRequest);
        /**
         * setBr();  // this set cause a error
         * setOutput(); // this set cause a error
         */
        log.info("Status code : " + response.getStatusLine().getStatusCode());
        return response.getStatusLine();
    }

    /**
     * Method to make a post request
     * @param resource is the URL's request
     * @return value status code
     */
    public StatusLine postHttpRequest(String resource, String json) {
        setClient();
        setPostRequest(resource, json);
        setResponse(postRequest);
        setBr();
        setOutput();

        log.info("Status code : " + response.getStatusLine().getStatusCode());
        return response.getStatusLine();
    }

    /**
     * Method to make a patch request
     * @param resource is the URL's request
     * @return value status code
     */
    public StatusLine patchHttpRequest(String resource, String json){
        setClient();
        setPatchRequest(resource,json);
        setResponse(patchRequest);
        setBr();
        setOutput();

        log.info("Status code : " + response.getStatusLine().getStatusCode());
        return response.getStatusLine();
    }

    /**
     * Method to set get request
     * @param resource is the URL's request
     */
    private void setGetRequest(String resource) {
        getRequest = new HttpGet(resource);
        setRequestHeaders(getRequest);
    }

    /**
     * Method to set post request
     * @param resource is the URL's request
     */
    private void setPostRequest(String resource, String json) {
        postRequest = new HttpPost(resource);
        StringEntity input = null;
        input = new StringEntity(json,"UTF-8");
        input.setContentType("application/json");
        postRequest.setEntity(input);
        setRequestHeaders(postRequest);
    }

    /**
     * Method to set delete request
     * @param resource is the URL's request
     */
    private void setDeleteRequest(String resource) {
        deleteRequest = new HttpDelete(resource);
        setRequestHeaders(deleteRequest);
    }

    /**
     * Method to set patch request
     * @param resource is the URL's request
     */
    private void setPatchRequest(String resource, String json){
        patchRequest = new HttpPatch(resource);
        StringEntity input = null;
        input = new StringEntity(json,"UTF-8");
        input.setContentType("application/json");
        patchRequest.setEntity(input);
        setRequestHeaders(patchRequest);
    }

    /**
     * Method the set headers of a request
     * @param request could be a get request, a post request, a delete request or patch request
     */
    private void setRequestHeaders(HttpRequestBase request){
        codeBasicAuth = encoder.encode(location.getDisplayName(),password);
        request.addHeader("Authorization", "Basic " + codeBasicAuth);
        request.addHeader("Accept", "application/json");
        request.addHeader("Content-Type" , "Application/Json;charset=UTF-8" );
        request.addHeader("Accept-Language", "es-419,es;q=0.8");
        request.addHeader("Prefer", "odata.track-changes");
    }

    /**
     * Method to execute request and get a response
     * @param request
     */
    private void setResponse(HttpRequestBase request){
        try {
            response = client.execute(request);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void setBr() {
        try {
            br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getOutput() {
        return output.toString();
    }

    private void setOutput() {
        output = new StringBuffer();
        String line = "";
        try {
            while ((line = br.readLine()) != null) {
                output.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setClient() {
        client = HttpClientBuilder.create().build();
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
