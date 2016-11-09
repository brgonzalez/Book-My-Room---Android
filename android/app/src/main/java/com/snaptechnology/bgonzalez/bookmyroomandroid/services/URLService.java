package com.snaptechnology.bgonzalez.bookmyroomandroid.services;

/**
 * Created by bgonzalez on 23/09/2016.
 */
public class URLService {

    //private String server = "http://192.168.100.4:8080";
    private String server = "http://10.110.10.153:8080";

    private static final String EVENT = "/event";
    private static final String LOCATION = "/location";


    public String getURLAllLocations(){
        String url = server + LOCATION + "/findall";
        return url;
    }



    public String getURLEvents(){
        return server + EVENT + "/events";
    }

    public String getURLCreateEvent(){
        return server + EVENT + "/create";
    }

    public String getURLUpdateEvent(){
        return server + EVENT + "/update";
    }

    public String getURLDeleteEvent(){
        return server + EVENT + "/delete";
    }



}
