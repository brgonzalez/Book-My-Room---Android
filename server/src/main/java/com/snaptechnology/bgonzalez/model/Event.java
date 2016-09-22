package com.snaptechnology.bgonzalez.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * The object event is the abstraction of a event,
 * a event in the context of this application is related with a meeting
 *
 * @author Brayan González
 * @since 04/08/2016.
 */
@JsonIgnoreProperties( { "allDay" })

public class Event {

    @JsonProperty("Id")
    private String id ;
    @JsonProperty("Subject")
    private String subject;
    @JsonProperty("Location")
    private Location location;
    /**
    @JsonProperty("Organizer")
    private Organizer organizer;
    */
    @JsonProperty("Attendees")
    private List<Attendee> attendees;


    @JsonProperty("IsAllDay")
    private boolean isAllDay;
    @JsonProperty("Start")
    private String start;
    @JsonProperty("End")
    private String end;

    public Event(JSONObject json) {
        this.id = json.get("Id").toString();
        this.subject = (json.get("Subject").toString());
        this.location = new Location((json.getJSONObject("Location").getString("DisplayName")));
        /**this.organizer = new Organizer(json.getJSONObject("Organizer"));*/

        JSONArray array = json.getJSONArray("Attendees");
        List<Attendee> attendees = new ArrayList<Attendee>();
        for (int i = 0 ; i < array.length(); i++ ){
            attendees.add(new Attendee(array.getJSONObject(i)));
        }
        this.attendees = attendees;

        this.isAllDay = json.getBoolean("IsAllDay");
        this.start = json.getString("Start");
        this.end = json.getString("End");
    }

    public Event(String id, String subject, Location location, /**Organizer organizer, */ List<Attendee> attendees, boolean isAllDay, String start, String end) {
        this.id = id;
        this.subject = subject;
        this.location = location;
        /**this.organizer = organizer;*/
        this.attendees = attendees;
        this.isAllDay = isAllDay;
        this.start = start;
        this.end = end;
    }

    public Event(){}

    public String getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public Location getLocation() {
        return location;
    }

    /**
    public Organizer getOrganizer(){
        return organizer;
    }*/

    public List<Attendee> getAttendees(){
        return attendees;
    }

    public boolean isAllDay() {
        return isAllDay;
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

    /**
    public void setOrganizer(Organizer organizer) {
        this.organizer = organizer;
    }*/

    public void setAttendees(List<Attendee> attendees) {
        this.attendees = attendees;
    }

    public void setIsAllDay(boolean isAllDay) {
        this.isAllDay = isAllDay ;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end) {
        this.end = end;
    }

}

