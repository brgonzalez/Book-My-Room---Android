package com.snaptechnology.bgonzalez.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snaptechnology.bgonzalez.crud.impl.DeviceDAOImpl;
import com.snaptechnology.bgonzalez.crud.impl.LocationDAOImpl;
import com.snaptechnology.bgonzalez.httpclient.RequestController;
import com.snaptechnology.bgonzalez.model.Device;
import com.snaptechnology.bgonzalez.model.Event;
import com.snaptechnology.bgonzalez.model.Location;
import com.snaptechnology.bgonzalez.socket.SenderSocket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bgonzalez on 08/09/2016.
 */
public class UpdateDataService {

    private LocationDAOImpl locationDAO;
    private RequestController requestController;
    private DeviceDAOImpl deviceDAO;
    private SenderSocket senderSocket;

    Map<String, String> deltaMap = new HashMap<String, String>();


    public UpdateDataService(){
        this.deviceDAO = new DeviceDAOImpl();
        this.locationDAO = new LocationDAOImpl();
        this.requestController = new RequestController();
        this.senderSocket = new SenderSocket();
    }

    public void update() throws InterruptedException {
        List<Location> locations;
        ObjectMapper mapper = new ObjectMapper();

        while(true){
            Thread.sleep(60000);

            locations = locationDAO.findAll();

            for(Location location : locations){

                if(!deltaMap.containsKey(location.getDisplayName())){
                    deltaMap.put(location.getDisplayName(),"tempDelta");
                }

                List<Event> recientEvent;
                recientEvent = requestController.synchronizedEvents(location,"2016-09-13T00:00:00.0003579Z","2016-09-13T23:59:00.0003579Z",deltaMap.get(location.getDisplayName()));

                if(!recientEvent.isEmpty()){
                    List<Device> devices = deviceDAO.findDevicesByLocation(location.getDisplayName());
                    String events ="null";
                    try {
                         events= mapper.writeValueAsString(recientEvent);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    senderSocket.sendData(devices,events);
                }
            }

        }



    }

    public static void main(String[] args){
        Map<String, String> deltaMap = new HashMap<String, String>();
        Location location = new Location("bgonzalez@snaptechnology.net");
        UpdateDataService updateDataService = new UpdateDataService();

        try {
            updateDataService.update();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
