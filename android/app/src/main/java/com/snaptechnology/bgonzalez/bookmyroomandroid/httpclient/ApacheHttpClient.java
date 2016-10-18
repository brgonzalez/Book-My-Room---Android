package com.snaptechnology.bgonzalez.bookmyroomandroid.httpclient;

/**
 * Created by bgonzalez on 04/08/2016.
 */


import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

public final class ApacheHttpClient {

    static Logger log = Logger.getLogger(ApacheHttpClient.class.getName());


    /**UtilProperties library org.apache.http.client.methods to make requests*/
    private HttpClient client;
    private HttpGet getRequest;
    private HttpPost postRequest;
    private HttpDelete deleteRequest;
    private HttpResponse response;
    private HttpPatch patchRequest;

    /** Reader*/
    private BufferedReader br;

    /**Variable to catch output requests*/
    private StringBuffer output;


    public ApacheHttpClient(){
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
        try {
            input = new StringEntity(json);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        input.setContentType("application/json");
        postRequest.setEntity(input);

        setRequestHeaders(postRequest);
    }



    /**
     * Method the set headers of a request
     * @param request could be a get request, a post request, a delete request or patch request
     */
    private void setRequestHeaders(HttpRequestBase request){
        request.addHeader("Accept", "application/json");
        request.addHeader("Content-Type" , "Application/Json" );
        request.addHeader("Accept-Language", "es-419,es;q=0.8");
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
        try{
            client = new DefaultHttpClient();
        }
        catch (IllegalArgumentException e){

        }
    }

}
