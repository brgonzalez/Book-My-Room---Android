package com.snaptechnology.bgonzalez.model.VO;

import com.snaptechnology.bgonzalez.model.Location;

/**
 * Created by bgonzalez on 20/09/2016.
 */
public class EventVO {

    private Location location;

    private String start;

    private String end;

    public EventVO(){

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
