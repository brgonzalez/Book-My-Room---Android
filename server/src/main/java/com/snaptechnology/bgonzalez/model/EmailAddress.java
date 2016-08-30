package com.snaptechnology.bgonzalez.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONObject;

/**
 * Created by bgonzalez on 18/08/2016.
 */
public class EmailAddress {
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Address")
    private String address;


    public EmailAddress(JSONObject json) {
        this.name = json.get("Name").toString();
        this.address = json.get("Address").toString();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }
}