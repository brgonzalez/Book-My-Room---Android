package com.snaptechnology.bgonzalez.services;

import com.snaptechnology.bgonzalez.httpclient.Account;
import com.snaptechnology.bgonzalez.model.Event;
import org.apache.log4j.Logger;

/**
 * Created by bgonzalez on 04/08/2016.
 */

public class URLService {

    private String path ="https://outlook.office365.com/api/v1.0/";


    private String displayNameLocation;
    private String Delta;



    final static Logger logger = Logger.getLogger(URLService.class);


    public String getURLCreateEvent(){

        logger.info("Getting URL create event ");

        return getPath() +  "users('" + displayNameLocation + "')/events";
    }


    public String getURLEvents(String startDate, String endDate){

        logger.info("Getting URL get events");

        return String.format( getPath() + "users('%s')/calendarview?startDateTime=%s&endDateTime=%s", displayNameLocation, startDate, endDate);
    }


    public String getURLSynchronizeEvents(String startDate, String endDate,String delta){

 //       logger.info("Getting URL synchronize events");

        return String.format( getPath() + "users('%s')/calendarview?startDateTime=%s&endDateTime=%s&$deltatoken=%s", displayNameLocation, startDate, endDate,delta);
    }


    public String getURLDeleteEvent(Event event){

        logger.info("Getting URL delete event with id "+ event.getId() );

        return String.format(getPath() + "users('%s')/events/%s", displayNameLocation,  event.getId());
    }


    public String getURLUpdateEvent(Event event){

        logger.info("Getting URL update event with id "+ event.getId() );

        return String.format(getPath() + "users('%s')/events/%s", displayNameLocation,  event.getId());
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }



    public String getDisplayNameLocation() {
        return displayNameLocation;
    }

    public void setDisplayNameLocation(String displayNameLocation) {
        this.displayNameLocation = displayNameLocation;
    }

    public String getDelta() {
        return Delta;
    }

    public void setDelta(String delta) {
        Delta = delta;
    }

    public static void main(String[] agrs){
        Account a = new Account("bgonzalez@snaptechnology.net","Brayan", "BrgcBrgc5snap");
        URLService service = new URLService();

    }
}