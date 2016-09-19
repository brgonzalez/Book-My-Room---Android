package com.snaptechnology.bgonzalez.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


/**
 *
 * The object location is the abstraction of a room, Its name is by the API Calendar Office 365.
 * It modeling a table from the database
 * @author Brayan González
 * @since 18/08/2016.
 *
 */

@Entity
@Table(name= "locations")
public class Location  implements Serializable{

    @Id
    @Column(name = "display_name")
    @JsonProperty("DisplayName")
    private String displayName;

    public Location(JSONObject json) {
        this.displayName = json.get("DisplayName").toString();
    }

    public Location(String displayName ){
        this.displayName = displayName;
    }

    public Location(){
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    @Override
    public String toString() {
        return "displayName:'" + displayName + '\'' ;
    }
}
