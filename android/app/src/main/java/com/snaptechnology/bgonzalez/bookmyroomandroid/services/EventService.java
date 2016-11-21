package com.snaptechnology.bgonzalez.bookmyroomandroid.services;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.snaptechnology.bgonzalez.bookmyroomandroid.httpclient.OKHttpClient;
import com.snaptechnology.bgonzalez.bookmyroomandroid.model.Event;
import com.snaptechnology.bgonzalez.bookmyroomandroid.model.Location;
import com.snaptechnology.bgonzalez.bookmyroomandroid.model.VO.EventVO;
import com.snaptechnology.bgonzalez.bookmyroomandroid.utils.FileUtil;

import org.json.JSONException;
import org.json.JSONObject;

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

    private static  final String TAG = EventService.class.getSimpleName();
    private OKHttpClient client;
    private URLService urlService;
    private TimeService timeService;
    private Context context;
    private Map<String, Event> eventMapper;
    private static EventService instance = null;
    private static List<Event> events;

    private EventService(Context context){
        this.client = new OKHttpClient();
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

    /**
     * Method to send posts: create, delete, update
     * @param event
     * @param url
     * @return true if the post was completed correctly and false in other case
     */
    public boolean doPost(Event event, String url){
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String status;
        try {
            status = client.post(url, json);
            if (new JSONObject(status).getString("statusCode").equalsIgnoreCase("200")) {
                Log.i(TAG,"Operation successfully");
                updateEvents();
                return true;
            }else{
                Log.i(TAG,"Operation was not successfully, response:" +status);
            }
        }catch (JSONException e) {
            Log.e(TAG,"JsonProcessingException error parsing the result body in doPost()");
        }catch (NullPointerException e){
            Log.e(TAG,"NullPointerException");
        }
        return false;
    }

    /**
     * Method to update the events
     */
    public boolean updateEvents(){
        ObjectMapper mapper = new ObjectMapper();
        List<Event> events;
        Log.i(TAG, "Updating Events");
        try {
            Map<String, String> startEndDate = timeService.getRangeDays();
            String displayName = FileUtil.readLocation(context);
            EventVO eventVO = new EventVO(new Location(displayName), startEndDate.get("start"), startEndDate.get("end"));

            String json = mapper.writeValueAsString(eventVO);
            String url = urlService.getURLEvents();
            Log.i(TAG, "Post to "+ urlService.getURLEvents() +", request body "+ json );
            String result = client.post(url,json);
            Log.i(TAG, "Output: "+result);

            events = mapper.readValue(result, TypeFactory.defaultInstance().constructCollectionType(List.class, Event.class));

            this.getEventMapper().clear();
            setEvents(events);
            String startDate ;
            for(Event event : getEvents()){
                startDate = event.getStart();
                while( !timeService.isHigherOrEqual(startDate, event.getEnd())) {
                    getEventMapper().put(startDate, event);
                    startDate = timeService.addMinutes(startDate);
                }
            }
            return true;
        }catch (JsonProcessingException e) {
            Log.e(TAG,"JsonProcessingException error parsing the result body in UpdateEvents()");
        }catch (IOException e) {
            Log.e(TAG,"IOException error parsing the result body in UpdateEvents()");
        }catch (IllegalStateException e){
            Log.e(TAG,"Error IllegalStateException to execute operation, was not executed at time");
        }catch (NullPointerException e){
            Log.e(TAG,"Error NullPointerException to execute operation, probably the device is not connected with the server" );
        }
        this.getEventMapper().clear();
        setEvents(new ArrayList<Event>());
        return false;
    }

    public List<Event> getEvents(){
        return events;
    }

    private static  synchronized void setEvents(List<Event> newEvents) {
        events = newEvents;
    }

    public TimeService getTimeService() {
        return timeService;
    }

    public Map<String, Event> getEventMapper() {
        return eventMapper;
    }




}
