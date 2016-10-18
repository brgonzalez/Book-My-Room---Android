package com.snaptechnology.bgonzalez.bookmyroomandroid.services;

import android.content.Context;
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
import java.util.List;
import java.util.Map;

/**
 * Created by bgonzalez on 29/08/2016.
 */
public class UpdateService {

    private String data;
    private EventService eventService;
    private TimeService timeService;
    private ApacheHttpClient client;
    private URLService urlService;
    private Context context;

    public UpdateService(EventService eventService, Context context){
        this.eventService = eventService;
        this.timeService = new TimeService();
        this.client = new ApacheHttpClient();
        this.urlService = new URLService();
        this.context = context;
    }

    public void  updateData(){

        ObjectMapper mapper = new ObjectMapper();

        List<Event> events;
        while (true) {

            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.i("Update Events", "Updating Events");
            try {

                Map<String, String> startEndDate = timeService.getRangeToRequest();
                String displayName = new UtilProperties().getLocationProperty(context);

                EventVO eventVO = new EventVO(new Location(displayName), startEndDate.get("start"), startEndDate.get("end"));
                String json = mapper.writeValueAsString(eventVO);
                String url = urlService.getURLEvents();
                Log.i("POST method", "Sending post method to "+ urlService.getURLEvents() +" with request body "+ json );
                client.postHttpRequest(url,json );

                String output = client.getOutput();
                Log.i("Output POST method", "The output is " +output);
                events = mapper.readValue(output, TypeFactory.defaultInstance().constructCollectionType(List.class, Event.class));

                eventService.setEvents(events);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
