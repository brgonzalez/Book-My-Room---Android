package com.snaptechnology.bgonzalez.services;

import com.snaptechnology.bgonzalez.model.Event;
import com.snaptechnology.bgonzalez.model.Location;
import org.apache.log4j.Logger;

/**
 * URLService is a service to make urls to send to API Office 365
 * @author Brayan González Chaves
 * @since 04/08/2016.
 */

public class URLService {

    /** Main Path to API Office 365**/
    private String path ="https://outlook.office365.com/api/v1.0/";

    /** Room Account, must go inside the urls*/
    private Location location;
    private String Delta;

    final static Logger logger = Logger.getLogger(URLService.class);

    /**
     * Method to make the url to API Office 365 to create a event
     * @return url creation to API Office 365
     */
    public String getURLCreateEvent(){
        String url = getPath() +  "users('" + location.getDisplayName() + "')/events";
        logger.info("Getting URL create event : "+ url );
        return url;
    }

    /**
     * Method to make the url to API Office 365 to get events
     * @return url getting events to API Office 365
     */
    public String getURLEvents(String startDate, String endDate){
        String url = String.format( getPath() + "users('%s')/calendarview?startDateTime=%s&endDateTime=%s", location.getDisplayName(), startDate, endDate);
        logger.info("Getting URL get events : "+ url);
        return url;
    }

    /**
     * Method to make the url to API Office 365 to update a event
     * @return url of update a event to API Office 365
     */
    public String getURLUpdateEvent(Event event){
        String url = String.format(getPath() + "users('%s')/events/%s", location.getDisplayName(),  event.getId());
        logger.info("Getting URL update event : "+ url );
        return url;
    }

    /**
     * Method to make the url to API Office 365 to delete a event
     * @return url of delete a event to API Office 365
     */
    public String getURLDeleteEvent(Event event){
        String url = String.format(getPath() + "users('%s')/events/%s", location.getDisplayName(),  event.getId());
        logger.info("Getting URL delete event : "+ url );
        return url;
    }



    public String getURLSynchronizeEvents(String startDate, String endDate,String delta){

 //       logger.info("Getting URL synchronize events");

        return String.format( getPath() + "users('%s')/calendarview?startDateTime=%s&endDateTime=%s&$deltatoken=%s", location.getDisplayName(), startDate, endDate,delta);
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getDelta() {
        return Delta;
    }

    public void setDelta(String delta) {
        Delta = delta;
    }

}