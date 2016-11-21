package com.snaptechnology.bgonzalez.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snaptechnology.bgonzalez.httpclient.ApacheHttpClient;
import com.snaptechnology.bgonzalez.model.Event;
import com.snaptechnology.bgonzalez.model.Location;
import com.snaptechnology.bgonzalez.model.VO.EventVO;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Office365Service is a service that is used by Event Controller,
 * this service communicate with ApacheHttpClient to make request to the API Office 365
 *
 * @author Brayan González
 * @since 04/08/2016.
 */
@Service
public class Office365Service {

    private ApacheHttpClient client;
    private URLService urlService;
    private String delta="";

    final static Logger logger = Logger.getLogger(Office365Service.class);

    public Office365Service() {
        setUrlService(new URLService());
        setClient(new ApacheHttpClient());
    }

    /**
     * Method to get events to send to EventController
     * @param eventVO with fields location, startDate and endDate
     * @return events
     */
    public List<Event> getEvents(EventVO eventVO) {

        logger.info("Getting status code to get events from Office365 Service");

        List<Event> events = new ArrayList<Event>();

        client.setLocation(eventVO.getLocation());
        urlService.setLocation(eventVO.getLocation());

        client.getHttpRequest(urlService.getURLEvents(eventVO.getStart(),eventVO.getEnd()));

        //logger.info(client.getOutput());
        JSONArray json = new JSONObject(client.getOutput()).getJSONArray("value");

        for(int i = 0; i < json.length(); i++){
            events.add(new Event(json.getJSONObject(i)));
        }
        return events;
    }

    /**
     * Method to get status code of event creation to send to EventController
     * @param event
     * @return Operation status code
     */
    public int createEvent(Event event){

        logger.info("Getting status code to create event from Office365 Service");

        ObjectMapper mapper = new ObjectMapper();
        client.setLocation(event.getLocation());
        urlService.setLocation(event.getLocation());

        int statusCode = 0;
        try {
            String requestRody =  mapper.writeValueAsString(event);

            logger.info("RequestBody is :" +requestRody);
            statusCode = client.postHttpRequest(urlService.getURLCreateEvent(),requestRody).getStatusCode();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.error("Error trying to get status code to create a event in Office365 Service, request body did not mapped");
        }
        //logger.info("Output from API Office 365 : " + client.getOutput());

        return (statusCode == 201) ? 200 : statusCode ;
    }

    /**
     * Method to get status code of update a event to send to EventController
     * @param event
     * @return Operation status code
     */
    public int updateEvent (Event event){

        logger.info("Getting status code to update event from Office365 Service");

        ObjectMapper mapper = new ObjectMapper();

        client.setLocation(event.getLocation());
        urlService.setLocation(event.getLocation());

        int statusCode = 0;
        try {
            statusCode = client.patchHttpRequest(urlService.getURLUpdateEvent(event), mapper.writeValueAsString(event)).getStatusCode();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.error("Error trying to get status code to update a event in Office365 Service, request body did not mapped");
        }

        //logger.info("Output from API Office 365 : " + client.getOutput());

        return statusCode;
    }

    /**
     * Method to get status code of delete a event to send to EventController
     * @param event
     * @return Operation status code
     */
    public int deleteEvent (Event event){
        logger.info("Getting status code to eliminate event from Office365 Service");

        client.setLocation(event.getLocation());
        urlService.setLocation(event.getLocation());

        int statusCode = client.deleteHttpRequest(urlService.getURLDeleteEvent(event)).getStatusCode();

        /*logger.info("Output from API Office 365 : " + client.getOutput()); //It has not output*/

        return (statusCode == 204) ? 200 : statusCode ;
    }

    public List<Event> synchronizedEvents(Location location, String startDate, String endDate, String delta){
        List<Event> events = new ArrayList<Event>();

        client.getHttpRequest(urlService.getURLSynchronizeEvents(startDate, endDate, delta));
        String dataDelta = new JSONObject(client.getOutput()).getString("@odata.deltaLink");

        setDelta(dataDelta);

        JSONArray json = new JSONObject(client.getOutput()).getJSONArray("value");
        //System.out.println(client.getOutput());

        for(int i = 0; i < json.length(); i++){
            events.add(new Event(json.getJSONObject(i)));
        }

        return events;
    }

    public String getDelta(){
        return delta;
    }

    public void setDelta(String dataDelta){
        /* This is because when is done a getEvents(), the parameter is deltatoken
        and when is done a synchronizedEvents() the parameter is deltaToken
        with T in capitalized
         */
        String[] arrayDataDelta = dataDelta.split("oken=");
        String delta = arrayDataDelta[1];
        this.delta = delta;
    }

    public URLService getUrlService() {
        return urlService;
    }

    private void setClient(ApacheHttpClient client) {
        this.client = client;
    }
    private void setUrlService(URLService urlService){
        this.urlService  = urlService;
    }

}

