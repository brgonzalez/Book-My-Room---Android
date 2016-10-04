package com.snaptechnology.bgonzalez.bookmyroomandroid.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


/**
 *
 * The object location is the abstraction of a room, Its name is by the API Calendar Office 365.
 * It modeling a table from the database
 * @author Brayan González
 * @since 18/08/2016.
 *
 */


public class Location {


    @JsonProperty("DisplayName")
    private String displayName;

    public Location(JSONObject json) {
        try {
            this.displayName = json.get("DisplayName").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Location(String displayName ){
        this.displayName = displayName;
    }

    public Location(){
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    @Override
    public String toString() {
        return "displayName:'" + displayName + '\'' ;
    }
}
