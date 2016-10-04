package com.snaptechnology.bgonzalez.bookmyroomandroid.services;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.snaptechnology.bgonzalez.bookmyroomandroid.httpclient.ApacheHttpClient;
import com.snaptechnology.bgonzalez.bookmyroomandroid.model.Event;
import com.snaptechnology.bgonzalez.bookmyroomandroid.model.Location;
import com.snaptechnology.bgonzalez.bookmyroomandroid.model.VO.EventVO;
import com.snaptechnology.bgonzalez.bookmyroomandroid.utils.UtilProperties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EventService is a service to make request to the main server,
 * this server is connected with API Office 365
 * @author Brayan González
 * @since 14/09/2016.
 */
public final class EventService  {

    private ApacheHttpClient client;
    private URLService urlService;

    public TimeService getTimeService() {
        return timeService;
    }

    public void setTimeService(TimeService timeService) {
        this.timeService = timeService;
    }

    private TimeService timeService;
    private Context context;

    public Map<String, Event> getEventMapper() {
        return eventMapper;
    }

    public void setEventMapper(Map<String, Event> eventMapper) {
        this.eventMapper = eventMapper;
    }

    private Map<String, Event> eventMapper;


    private static EventService instance = null;

    private static List<Event> events;

    public EventService(Context context){
        this.client = new ApacheHttpClient();
        this.urlService = new URLService();
        this.timeService = new TimeService();
        this.context = context;
        eventMapper = new HashMap<>();
        events = new ArrayList<>();
    }

    public static synchronized EventService getInstance(Context context){
        if(instance == null){
            instance = new EventService(context);
        }
        return instance;
    }

    public boolean createEvent(Event event){
        ObjectMapper mapper = new ObjectMapper();
        String url = urlService.getURLCreateEvent();
        String json = null;
        try {
            json = mapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        client.postHttpRequest(url,json);
        String output = client.getOutput();

        System.out.println(output);
        updateEvents();

        return false;

    }

    public  void updateEvents(){

        ObjectMapper mapper = new ObjectMapper();

        List<Event> events;

        Log.i("Update Events", "Updating Events");
        try {

            Map<String, String> startEndDate = timeService.getStartEndCurrentWeek();
            String displayName = new UtilProperties().getLocationProperty(context);

            EventVO eventVO = new EventVO(new Location(displayName), startEndDate.get("start"), startEndDate.get("end"));
            String json = mapper.writeValueAsString(eventVO);
            String url = urlService.getURLEvents();
            Log.i("POST method", "Sending post method to "+ urlService.getURLEvents() +" with request body "+ json );
            client.postHttpRequest(url,json );

            String output = client.getOutput();
            Log.i("Output POST method", "The output is " +output);
            events = mapper.readValue(output, TypeFactory.defaultInstance().constructCollectionType(List.class, Event.class));
            this.getEventMapper().clear();
            this.setEvents(events);
            for(Event event : getEvent()){
                getEventMapper().put(event.getStart(),event);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<Event> getEvent (){
        return events;
    }

    public static synchronized List<Event> getEvents() {
        return events;
    }

    public static  synchronized void setEvents(List<Event> newEvents) {
        events = newEvents;
    }



}
