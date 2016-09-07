package com.snaptechnology.bgonzalez.bookmyroomandroid.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bgonzalez on 04/08/2016.
 */
public class Event {

    @JsonProperty("Id")
    private String id ;
    @JsonProperty("Subject")
    private String subject;
    @JsonProperty("Location")
    private Location location;
    @JsonProperty("Organizer")
    private Organizer organizer;
    @JsonProperty("Attendees")
    private List<Attendee> attendees;
    @JsonProperty("AllDay")
    private boolean allDay;
    @JsonProperty("Start")
    private String start;
    @JsonProperty("End")
    private String end;

    public Event(JSONObject json) throws JSONException {
        /*this.id = json.get("Id").toString();
        this.subject = (json.get("Subject").toString());
        this.location = new Location((json.getJSONObject("Location").getString("DisplayName")));
        this.organizer = new Organizer(json.getJSONObject("Organizer"));
        JSONArray array = json.getJSONArray("Attendees");
        List<Attendee> attendees = new ArrayList<Attendee>();
        for (int i = 0 ; i < array.length(); i++ ){
            attendees.add(new Attendee(array.getJSONObject(i)));
        }
        this.attendees = attendees;
        this.allDay = json.getBoolean("IsAllDay");
        this.start = json.getString("Start");
        this.end = json.getString("End");*/
    }

    public Event(String id, String subject, Location location, Organizer organizer, List<Attendee> attendees, boolean allDay, String start, String end) {
        this.id = id;
        this.subject = subject;
        this.location = location;
        this.organizer = organizer;
        this.attendees = attendees;
        this.allDay = allDay;
        this.start = start;
        this.end = end;
    }

    public String getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public Location getLocation() {
        return location;
    }

    public Organizer getOrganizer(){
        return organizer;
    }

    public List<Attendee> getAttendees(){
        return attendees;
    }

    public boolean isAllDay() {
        return allDay;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setOrganizer(Organizer organizer) {
        this.organizer = organizer;
    }

    public void setAttendees(List<Attendee> attendees) {
        this.attendees = attendees;
    }

    public void setIsAllDay(boolean allDay) {
        allDay = allDay;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end) {
        this.end = end;
    }

}

