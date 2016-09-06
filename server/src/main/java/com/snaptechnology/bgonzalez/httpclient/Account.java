package com.snaptechnology.bgonzalez.httpclient;

/**
 * Created by bgonzalez on 06/09/2016.
 */
public class Account {
    private String address;
    private String name;
    private String password;

    public Account(String address, String name, String password){
        this.address = address;
        this.name = name ;
        this.password = password;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
