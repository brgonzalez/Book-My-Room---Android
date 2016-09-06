package com.snaptechnology.bgonzalez.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONObject;

/**
 * Created by bgonzalez on 18/08/2016.
 */

public class Location  {

    @JsonProperty("DisplayName")
    private String displayName;

    public Location(JSONObject json) {
        this.displayName = json.get("DisplayName").toString();
    }

    public Location(String displayName ){
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
