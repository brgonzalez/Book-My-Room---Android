package com.snaptechnology.bgonzalez.httpclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snaptechnology.bgonzalez.model.*;
import com.snaptechnology.bgonzalez.services.URLService;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bgonzalez on 04/08/2016.
 */
public class RequestController {

    private ApacheHttpClient client;
    private URLService urlService;


    private String delta="";

    final static Logger logger = Logger.getLogger(RequestController.class);



    public RequestController() {
        setUrlService(new URLService());
        setClient(new ApacheHttpClient());
    }

    public List<Event> getAllEvents(){
        List<Event> events = new ArrayList<Event>();

        client.getHttpRequest(urlService.getURLCreateEvent());

        String dataDelta = new JSONObject(client.getOutput()).getString("@odata.deltaLink");
        setDelta(dataDelta);

        JSONArray json = new JSONObject(client.getOutput()).getJSONArray("value");
        for(int i = 0; i < json.length(); i++){
            //events.add(new Event(json.getJSONObject(i)));
        }
        return events;
    }

    public List<Event> getEvents(Location location,String startDate, String endDate){

        logger.info("Getting events from API Office 365");

        List<Event> events = new ArrayList<Event>();

        client.setDisplayNameLocation(location.getDisplayName());
        urlService.setDisplayNameLocation(location.getDisplayName());

        client.getHttpRequest(urlService.getURLEvents(startDate,endDate));
        System.out.println(urlService.getURLEvents(startDate,endDate));


        String dataDelta = new JSONObject(client.getOutput()).getString("@odata.deltaLink");
        System.out.println(dataDelta);

        setDelta(dataDelta);

        JSONArray json = new JSONObject(client.getOutput()).getJSONArray("value");
        for(int i = 0; i < json.length(); i++){
            events.add(new Event(json.getJSONObject(i)));
        }
        return events;
    }

    public void createEvent(String json){

        JSONObject jsonObj = new JSONObject(json);



        client.setDisplayNameLocation(jsonObj.getString("displayNameLocation"));
        urlService.setDisplayNameLocation(jsonObj.getString("displayNameLocation"));


        System.out.println(urlService.getURLCreateEvent());
        System.out.println(json);

        String data = jsonObj.getJSONObject("data").toString();
        System.out.println(data);
        client.postHttpRequest(urlService.getURLCreateEvent(), data);
        System.out.println(client.getOutput());
    }


    public void updateEvent (Event event){
        //client.patchHttpRequest(urlService.getURLUpdateEvent(event.getId()),);
    }

    public List<Event> synchronizedEvents(Location location, String startDate, String endDate,String delta){
        List<Event> events = new ArrayList<Event>();

        client.setDisplayNameLocation(location.getDisplayName());
        urlService.setDisplayNameLocation(location.getDisplayName());

        client.getHttpRequest(urlService.getURLSynchronizeEvents(startDate, endDate, delta));
        String dataDelta = new JSONObject(client.getOutput()).getString("@odata.deltaLink");

        setDelta(dataDelta);
        System.out.println(urlService.getURLSynchronizeEvents(startDate, endDate, delta));

        JSONArray json = new JSONObject(client.getOutput()).getJSONArray("value");
        System.out.println(client.getOutput());

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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    private Account account;


    public static void main(String[] args) throws JsonProcessingException {
        RequestController rc = new RequestController();
        ObjectMapper mapper = new ObjectMapper();

        //rc.createEvent("Brayan","UTC","2016-08-18T11:30:00.0003579Z","2016-08-18T12:30:00.0003579Z",attendees);

        Account account = new Account("bgonzalez@snaptechnology.net","Brayan", "BrgcBrgc5snap");
        rc.setAccount(account);



        //Get



        // Create

        EmailAddress emailAddress = new EmailAddress("Brayan González","bgonzalez@snaptechnology.net");
        Organizer organizer = new Organizer(emailAddress);
        Attendee attendee = new Attendee(emailAddress,"Required");
        Location location = new Location("Bella");
        List<Attendee> attendees = new ArrayList<Attendee>();
        attendees.add(attendee);

        Event event = new Event("id","Test from Server",location,false,"2016-09-13T16:30:00.0003579Z","2016-09-13T20:30:00.0003579Z");
        String JSON = mapper.writeValueAsString(event);

        String test = "{\"displayNameLocation\":\"bgonzalez@snaptechnology.net\",\"data\":"+JSON+"}";
        System.out.println(test);
        rc.createEvent(test);



        //System.out.println(mapper.writeValueAsString(rc.synchronizedEvents("2016-09-13T10:30:00.0003579Z","2016-09-13T11:30:00.0003579Z")));


































        //Event e = new Event("1","subject",null,null,null,false,"","");
        //System.out.println(mapper.writeValueAsString(e));

        /*String jsonInString = null;
        while (true) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                jsonInString = mapper.writeValueAsString(rc.synchronizedEvents("2016-08-16T11:29:00.0003579Z", "2016-08-20T21:30:00.0003579Z"));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            System.out.println(jsonInString);
        }


        */
    }

}

