package com.snaptechnology.bgonzalez.model;

/**
 * Class Status is used to response from requests by status code
 * @author Brayan González
 * @since 21/09/2016.
 */
public class Status {

    private int statusCode;

    public Status(){

    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
