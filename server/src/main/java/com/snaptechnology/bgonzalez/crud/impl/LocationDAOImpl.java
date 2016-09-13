package com.snaptechnology.bgonzalez.crud.impl;

import com.snaptechnology.bgonzalez.crud.daos.LocationDAO;
import com.snaptechnology.bgonzalez.database.DataBasePostgreSQL;
import com.snaptechnology.bgonzalez.model.Location;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bgonzalez on 06/09/2016.
 */
public class LocationDAOImpl implements LocationDAO {


    private DataBasePostgreSQL connection;

    public LocationDAOImpl(){
        this.connection = new DataBasePostgreSQL();
    }

    public String create(Location location) {
        connection.connect();
        String query = "INSERT INTO locations (display_name) VALUES('"+ location.getDisplayName()+"')";
        connection.executeUpdate(query);
        connection.disconnect();
        return location.getDisplayName();
    }

    public Location read(String id) {
        connection.connect();
        String query = "SELECT display_name FROM locations WHERE display_name ='" + id+"');" ;
        ResultSet result = connection.executeQuery(query);
        String displayName= null;
        try {
            displayName = result.getString("displayName");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.disconnect();
        return new Location(displayName);
    }

    public void update(Location location) {
    }

    public void delete(Location location) {
        connection.connect();
        String query = "DELETE FROM locations WHERE display_name='"+location.getDisplayName()+"'";
        connection.executeUpdate(query);
        connection.disconnect();
    }

    public List<Location> findAll() {
        List<Location> locations = new ArrayList<Location>();
        connection.connect();
        String query = "SELECT display_name FROM locations ;" ;
        ResultSet result = connection.executeQuery(query);
        String displayName= null;
        try {
            while (result.next()) {
                displayName = result.getString("display_name");
                locations.add(new Location(displayName));
            }
            connection.disconnect(); }
        catch (SQLException e) {
            e.printStackTrace();
        }
        connection.disconnect();
        return locations;
    }

    public void bindLocationToDevice(String idDevice, String idLocation){
        connection.connect();
        String query = String.format("INSERT INTO locations_devices (address, display_name) VALUES ('%s','%s');", idDevice, idLocation);
        connection.executeUpdate(query);
        connection.disconnect();
    }
    public void unBindLocationToDevice(String idDevice, String idLocation){
        connection.connect();
        String query = String.format("DELETE FROM locations_devices WHERE display_name='%s' AND address = '%s';", idLocation,idDevice);
        connection.executeUpdate(query);
        connection.disconnect();
    }

}
