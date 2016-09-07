package com.snaptechnology.bgonzalez.services;

import com.snaptechnology.bgonzalez.httpclient.Account;
import com.snaptechnology.bgonzalez.model.Event;
import org.apache.log4j.Logger;

/**
 * Created by bgonzalez on 04/08/2016.
 */

public class URLService {

    private String path ="https://outlook.office365.com/api/v1.0/";
    private Account account;
    private String Delta;

    /*
    public URLService(Account account){
        this.account = account;
    }*/

    final static Logger logger = Logger.getLogger(URLService.class);


    public String getURLAllEvents(){

        logger.info("Getting URL et all events");

        return getPath() +  "users('" +getAccount().getAddress() + "')/events";
    }


    public String getURLEvents(String startDate, String endDate){

        logger.info("Getting URL get events");

        return String.format( getPath() + "users('%s')/calendarview?startDateTime=%s&endDateTime=%s", getAccount().getAddress(), startDate, endDate);
    }


    public String getURLSynchronizeEvents(String startDate, String endDate,String delta){

        logger.info("Getting URL synchronize events");

        return String.format( getPath() + "users('%s')/calendarview?startDateTime=%s&endDateTime=%s&$deltatoken=%s", getAccount().getAddress(), startDate, endDate,delta);
    }


    public String getURLDeleteEvent(Event event){

        logger.info("Getting URL delete event with id "+ event.getId() );

        return String.format(getPath() + "users('%s')/events/%s", getAccount().getAddress(),  event.getId());
    }


    public String getURLUpdateEvent(Event event){

        logger.info("Getting URL update event with id "+ event.getId() );

        return String.format(getPath() + "users('%s')/events/%s", getAccount().getAddress(),  event.getId());
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
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
        service.setAccount(a);
        System.out.println(service.getURLEvents("2016-08-18T22:31:57.9051079Z","2016-08-19T22:31:57.9051079Z"));
    }
}