package com.snaptechnology.bgonzalez.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONObject;

/**
 *
 * @autor Brayan González
 * @since 10/11/2016.
 */
public class Organizer {
    @JsonProperty("EmailAddress")
    private EmailAddress emailAddress;

    public Organizer(JSONObject json){
        this.emailAddress = new EmailAddress(json.getJSONObject("EmailAddress"));
    }
    public Organizer(EmailAddress emailAddress){
        this.emailAddress = emailAddress;
    }

    public Organizer(){

    }

    public EmailAddress getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(EmailAddress emailAddress) {
        this.emailAddress = emailAddress;
    }
}
