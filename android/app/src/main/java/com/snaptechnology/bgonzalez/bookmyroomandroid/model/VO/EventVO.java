package com.snaptechnology.bgonzalez.bookmyroomandroid.model.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.snaptechnology.bgonzalez.bookmyroomandroid.model.Location;
import com.snaptechnology.bgonzalez.bookmyroomandroid.services.EventService;

/**
 * Class to get fields to make request get all events
 * Those fields are enough
 * @author Brayan González
 * @since 20/09/2016.
 */
public class EventVO {
    @JsonProperty("Location")
    private Location location;

    @JsonProperty("Start")
    private String start;

    @JsonProperty("End")
    private String end;

    public EventVO(){

    }
    public EventVO( Location location, String start, String end){
        this.location = location;
        this.start = start;
        this.end = end;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
