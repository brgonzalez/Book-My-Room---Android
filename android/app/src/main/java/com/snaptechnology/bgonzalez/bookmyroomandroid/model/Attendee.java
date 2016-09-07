package com.snaptechnology.bgonzalez.bookmyroomandroid.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bgonzalez on 18/08/2016.
 */
public class Attendee {
    @JsonProperty("EmailAddress")
    private EmailAddress emailAddress;
    @JsonProperty("Type")
    private String type;

    public Attendee(EmailAddress emailAddress, String type){
        this.emailAddress = emailAddress;
        this.type = type;
    }

    public EmailAddress getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(EmailAddress emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
