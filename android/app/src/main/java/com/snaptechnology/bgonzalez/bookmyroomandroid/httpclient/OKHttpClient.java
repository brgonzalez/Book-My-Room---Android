package com.snaptechnology.bgonzalez.bookmyroomandroid.httpclient;

/**
 * OkHttpClient is a client to send request to the server
 *
 * @autor Brayan González
 * @since 04/08/2016.
 */

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public final class OKHttpClient {

    private static final String TAG = OKHttpClient.class.getSimpleName();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient okHttpClient;
    private static final int TIMEOUT = 6;

    public OKHttpClient(){
        this.okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    /**
     * Method to make a get request
     * @param resource is the URL's request
     * @return Event list
     */
    public String get(String resource){
        Request request = new Request.Builder()
                .url(resource)
                .build();

        String result= null;
        try {
             Response response = okHttpClient.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Method to do posts to the server
     * @param url url to connect the server
     * @param json request body
     * @return response body
     */
    public String post(String url, String json) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        String result= null;
        try {
            Response response = okHttpClient.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            Log.e(TAG,"Timeout sending request");
        }
        return result;
    }
}