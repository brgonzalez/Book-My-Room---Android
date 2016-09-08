package com.snaptechnology.bgonzalez.httpclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snaptechnology.bgonzalez.model.Event;
import com.snaptechnology.bgonzalez.model.Person;
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

        client.getHttpRequest(urlService.getURLAllEvents());

        String dataDelta = new JSONObject(client.getOutput()).getString("@odata.deltaLink");
        setDelta(dataDelta);

        JSONArray json = new JSONObject(client.getOutput()).getJSONArray("value");
        for(int i = 0; i < json.length(); i++){
            events.add(new Event(json.getJSONObject(i)));
        }
        return events;
    }

    public List<Event> getEvents(String startDate, String endDate){

        logger.info("Getting all events from API Office 365");

        List<Event> events = new ArrayList<Event>();

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

    public void createEvent(String subject,String timeZone,String startDate, String endDate, List<Person> attendees/*, Room room*/){
        String jsonAttendees = "\"Attendees\": [";
        for(int i = 0; i < attendees.size(); i++){
            jsonAttendees += "{ \"EmailAddress\": { \"Address\": \""+ attendees.get(i).getEmail()+ "\", \"Name\": \"" + attendees.get(i).getName() + "\"},\"Type\": \"Required\" }";
            if(i < attendees.size() - 1){
                jsonAttendees += ",";
            }
        }
        jsonAttendees += "]";

        String json = "{\"Subject\": \"" + subject +"\",\"StartTimeZone\": \"" + timeZone + "\",\"EndTimeZone\": \"" + timeZone + "\", \"Start\": \"" + startDate +"\" , \"End\": \"" + endDate + "\", "+ jsonAttendees + " }";
        client.postHttpRequest(urlService.getURLAllEvents(), json);
    }




    /*
        Update a event
        Parameters:
            id: id of the event, is get it from Office 365 calendar
     */
    public void updateEvent (Event event){
        //client.patchHttpRequest(urlService.getURLUpdateEvent(event.getId()),);
    }

    public List<Event> synchronizedEvents(String startDate, String endDate){
        List<Event> events = new ArrayList<Event>();

        client.getHttpRequest(urlService.getURLSynchronizeEvents(startDate, endDate, delta));
        String dataDelta = new JSONObject(client.getOutput()).getString("@odata.deltaLink");

        setDelta(dataDelta);
        System.out.println(urlService.getURLSynchronizeEvents(startDate, endDate, delta));

        JSONArray json = new JSONObject(client.getOutput()).getJSONArray("value");
        for(int i = 0; i < json.length(); i++){
            events.add(new Event(json.getJSONObject(i)));
        }

        return events;
    }

    public String getDelta(){
        return delta;
    }

    public void setDelta(String dataDelta){
        /* This is because when is done a getEvents(), the paramenter is deltatoken
        and when is done a synchronizedEvents() the parameter is deltaToken
        with T in capitalized
         */
        String[] arrayDataDelta = dataDelta.split("oken=");
        String delta = arrayDataDelta[1];
        this.delta = delta;
    }

    private void setClient(ApacheHttpClient client) {
        this.client = client;
    }
    private void setUrlService(URLService urlService){
        this.urlService  = urlService;
    }

    public static void main(String[] args) throws JsonProcessingException {
        RequestController rc = new RequestController();
        ObjectMapper mapper = new ObjectMapper();

        //rc.createEvent("Brayan","UTC","2016-08-18T11:30:00.0003579Z","2016-08-18T12:30:00.0003579Z",attendees);


        System.out.println(mapper.writeValueAsString(rc.getEvents("2016-08-17T10:30:00.0003579Z","2016-08-19T14:30:00.0003579Z")));

        /*
        String jsonInString = null;
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

