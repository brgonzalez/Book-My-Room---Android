package com.snaptechnology.bgonzalez.services;

/**
 * Created by bgonzalez on 04/08/2016.
 */

public class URLService {

    private String path ="https://outlook.office365.com/api/v1.0/";
    private String user = "me";
    /*
    EVENTS("calendarview?startDateTime=2016-08-09T11:29:00.0003579Z&endDateTime=2016-08-09T21:30:00.0003579Z

    */

    public String getURLAllEvents(){
        return path + user + "/events";
    }

    public String getURLEvents(String startDate, String endDate){
        return getPath() + getUser() + "/calendarview?startDateTime="+ startDate + "&" + "endDateTime=" + endDate;
    }

    public String getURLSynchronizeEvents(String startDate, String endDate,String delta){
        return getPath() + getUser() + "/calendarview?startDateTime="+ startDate + "&" + "endDateTime=" + endDate + "&$deltatoken=" + delta;
    }
    public String getPath(){
        return path;
    }

    public String getURLUpdateEvent(String id){
        return "";
    }

    public String getUser(){
        return user;
    }

    public void setPath(String path){
        this.path = path;
    }
    public void setUser(String user){
        this.user = user;
    }
}