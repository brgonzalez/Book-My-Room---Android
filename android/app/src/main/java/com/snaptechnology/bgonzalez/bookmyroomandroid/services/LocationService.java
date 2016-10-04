package com.snaptechnology.bgonzalez.bookmyroomandroid.services;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.snaptechnology.bgonzalez.bookmyroomandroid.httpclient.ApacheHttpClient;
import com.snaptechnology.bgonzalez.bookmyroomandroid.model.Location;
import com.snaptechnology.bgonzalez.bookmyroomandroid.model.VO.EventVO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * LocationService is a service to make request to the main server,
 * this server is connected with API Office 365
 * @author Brayan González
 * @since 14/09/2016.
 */
public class LocationService {

    private ApacheHttpClient client;
    private URLService urlService;

    public LocationService(){
        this.client = new ApacheHttpClient();
        this.urlService = new URLService();
    }

    public List<Location> getLocations(){

        ObjectMapper mapper = new ObjectMapper();

        List<Location> locations = new ArrayList<>();
        try {
            String url = urlService.getURLAllLocations();
            Log.i("GET method","Sending GET method to "+ url);
            client.getHttpRequest(url);
            String output = client.getOutput();
            Log.i("Output GET method","The output is "+output);
            locations = mapper.readValue(output,TypeFactory.defaultInstance().constructCollectionType(List.class, Location.class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            Log.e("JsonProcessingException","Error to process JSON Object in LocationService");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("IOException","Error IOException in Location Service");

        }
        return locations;

    }
}
