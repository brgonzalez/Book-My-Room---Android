package com.snaptechnology.bgonzalez.crud.impl;

import com.snaptechnology.bgonzalez.crud.daos.DeviceDAO;
import com.snaptechnology.bgonzalez.database.DataBasePostgreSQL;
import com.snaptechnology.bgonzalez.model.Device;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bgonzalez on 07/09/2016.
 */
public class DeviceDAOImpl implements DeviceDAO {
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
        String query = String.format("UPDATE devices SET ip = '%s' where address = '%s';", device.getIp(), device.getAddress());
        connection.executeUpdate(query);
        connection.disconnect();
    }

    public void delete(Device device) {
        connection.connect();
        String query = String.format("DELETE FROM devices WHERE address = '%s'", device.getAddress());
        connection.executeUpdate(query);
        connection.disconnect();
    }

    public List<Device> findDevicesByLocation(String idLocation){
        List<Device> devices = new ArrayList<Device>();
        connection.connect();
        String query = String.format("SELECT d.ip, d.address FROM devices d, locations l, locations_devices ld \n" +
                "WHERE d.address = ld.address AND l.display_name = '%s';", idLocation);
        ResultSet result = connection.executeQuery(query);
        String address;
        String ip;
        try {
            while(result.next()){
                address = result.getString("address");
                ip = result.getString("ip");
                devices.add(new Device (address, ip));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return devices;
    }

}
