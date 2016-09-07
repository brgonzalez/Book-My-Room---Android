package com.snaptechnology.bgonzalez.crud.impl;

import com.snaptechnology.bgonzalez.crud.DeviceDAO;
import com.snaptechnology.bgonzalez.database.DataBasePostgreSQL;
import com.snaptechnology.bgonzalez.model.Device;
import com.snaptechnology.bgonzalez.model.Location;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bgonzalez on 07/09/2016.
 */
public class DeviceDAOImpl implements DeviceDAO{
    private DataBasePostgreSQL connection;


    public String create(Device device) {
        connection.connect();
        String query = String.format("INSERT INTO devices (address, ip) VALUES('%s', '%s');",device.getAddress(), device.getIp());
        connection.executeUpdate(query);
        connection.disconnect();
        return device.getAddress();
    }

    public Device read(String id) {
        connection.connect();
        String query = String.format("SELECT display_name FROM locations WHERE display_name ='%s');", id);
        ResultSet result = connection.executeQuery(query);
        String address= null;
        String ip = null;
        try {
            address = result.getString("address");
            ip = result.getString("ip");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.disconnect();
        return new Device(address,ip);
    }

    public void update(Device device) {
        connection.connect();
        String query ="";
    }

    public void delete(Device device) {

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


}
