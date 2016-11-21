package com.snaptechnology.bgonzalez.bookmyroomandroid.model;

import android.util.Log;
import android.widget.TextView;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @autor Brayan González
 * @since 10/11/2016.
 */
public class Organizer {

    private static final String TAG = Organizer.class.getSimpleName();

    @JsonProperty("EmailAddress")
    private EmailAddress emailAddress;

    public Organizer(JSONObject json){
        try {
            this.emailAddress = new EmailAddress(json.getJSONObject("EmailAddress"));
        } catch (JSONException e) {
            Log.e(TAG,"JSONException parsing Organizer");
        }
    }
    public Organizer(){

    }
    public Organizer(EmailAddress emailAddress){
        this.emailAddress = emailAddress;
    }

    public EmailAddress getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(EmailAddress emailAddress) {
        this.emailAddress = emailAddress;
    }
}
