package com.snaptechnology.bgonzalez.httpclient;

/**
 * Created by bgonzalez on 04/08/2016.
 */

// libraries:

import com.snaptechnology.bgonzalez.model.Location;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public final class ApacheHttpClient {

    // properties:
    private HttpClient client;
    private HttpGet getRequest;
    private HttpPost postRequest;
    private HttpDelete deleteRequest;
    private HttpResponse response;
    private HttpPatch patchRequest;
    private BufferedReader br;
    private StringBuffer output;



    private Location location;
    private String password ="BrgcBrgc5snap";
    private Encoder encoder;
    private String codeBasicAuth;

    public ApacheHttpClient() {
        this.encoder = new Encoder();
    }
    private void setClient() {
        client = HttpClientBuilder.create().build();
    }


    public StatusLine getHttpRequest(String resource) {
        setClient();
        setGetRequest(resource);
        setResponse(getRequest);
        setBr();
        setOutput();
        return response.getStatusLine();
    }

    protected StatusLine deleteHttpRequest(String resource) {
        setClient();
        setDeleteRequest(resource);
        setResponse(deleteRequest);
        setBr();
        setOutput();
        return response.getStatusLine();
    }

    public StatusLine postHttpRequest(String resource, String json) {
        setClient();
        setPostRequest(resource, json);
        setResponse(postRequest);
        setBr();
        setOutput();
        return response.getStatusLine();
    }

    protected StatusLine patchHttpRequest(String resource, String json){
        setClient();
        setPatchRequest(resource,json);
        setResponse(patchRequest);
        setBr();
        setOutput();
        return response.getStatusLine();
    }



    private void setGetRequest(String resource) {
        getRequest = new HttpGet(resource);
        setRequestHeaders(getRequest);
    }

    private void setPostRequest(String resource, String json) {
        postRequest = new HttpPost(resource);
        StringEntity input = null;
        try {
            input = new StringEntity(json);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.out.println("Cannot read json");
        }
        input.setContentType("application/json");
        postRequest.setEntity(input);

        setRequestHeaders(postRequest);
    }

    private void setDeleteRequest(String resource) {
        deleteRequest = new HttpDelete(resource);
        setRequestHeaders(deleteRequest);
    }

    private void setPatchRequest(String resource, String json){
        patchRequest = new HttpPatch(resource);
        StringEntity input = null;
        try {
            input = new StringEntity(json);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        input.setContentType("application/json");
        setRequestHeaders(patchRequest);
    }

    private void setRequestHeaders(HttpRequestBase request){
        codeBasicAuth = encoder.encode(location.getDisplayName(),password);
        request.addHeader("Authorization", "Basic " + codeBasicAuth);
        request.addHeader("Accept", "application/json");
        request.addHeader("Content-Type" , "Application/Json" );
        request.addHeader("Accept-Language", "es-419,es;q=0.8");
        request.addHeader("Prefer", "odata.track-changes");
    }

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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public static void main(String[] args) {
        ApacheHttpClient apache = new ApacheHttpClient();
        System.out.println(apache.getHttpRequest(""));
        System.out.println(apache.getOutput());

    }
}
