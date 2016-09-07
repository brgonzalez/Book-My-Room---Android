package com.snaptechnology.bgonzalez.bookmyroomandroid.model;

/**
 * Created by bgonzalez on 04/08/2016.
 */

import org.json.JSONObject;

public class Person {
    private String name;
    private String email;


    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public static void main(){

    }

}
