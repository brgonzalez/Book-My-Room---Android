package com.snaptechnology.bgonzalez.bookmyroomandroid.model;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Attendee is the abstraction of meeting's assistants
 * @author Brayan González
 * @since 18/08/2016.
 */

public class Attendee {

    private final static String TAG = Attendee.class.getSimpleName();

    @JsonProperty("EmailAddress")
    private EmailAddress emailAddress;
    @JsonProperty("Type")
    private String type;

    public Attendee(JSONObject json){
        try {
            this.emailAddress = new EmailAddress(json.getJSONObject("EmailAddress"));
            this.type = json.getString("Type");

        } catch (JSONException e) {
            Log.e(TAG,"JSONException parsing attendee");
        }
    }

    public Attendee(EmailAddress emailAddress, String type){
        this.emailAddress = emailAddress;
        this.type = type;
    }

    public Attendee(){

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
