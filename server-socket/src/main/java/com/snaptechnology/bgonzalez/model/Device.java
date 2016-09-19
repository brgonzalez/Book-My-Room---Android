package com.snaptechnology.bgonzalez.model;

/**
 * Created by bgonzalez on 05/09/2016.
 */
public class Device {
    private String ip;
    private String address;

    public Device(String address, String ip){
        this.address = address;
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
