package com.snaptechnology.bgonzalez.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONObject;

/**
 * Created by bgonzalez on 18/08/2016.
 */
public class Organizer {
    @JsonProperty("EmailAddress")
    private EmailAddress emailAddress;

    public Organizer (JSONObject json){
        this.emailAddress = new EmailAddress(json.getJSONObject("EmailAddress"));
    }

    public EmailAddress getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(EmailAddress emailAddress) {
        this.emailAddress = emailAddress;
    }



}
