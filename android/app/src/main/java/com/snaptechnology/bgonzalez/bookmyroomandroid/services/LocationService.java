package com.snaptechnology.bgonzalez.bookmyroomandroid.services;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.snaptechnology.bgonzalez.bookmyroomandroid.httpclient.OKHttpClient;
import com.snaptechnology.bgonzalez.bookmyroomandroid.model.Location;

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

    private static final String TAG = LocationService.class.getSimpleName();
    private OKHttpClient client;
    private URLService urlService;

    public LocationService(){
        this.client = new OKHttpClient();
        this.urlService = new URLService();
    }

    public List<Location> getLocations(){

        ObjectMapper mapper = new ObjectMapper();

        List<Location> locations = new ArrayList<>();
        try {
            String url = urlService.getURLAllLocations();
            Log.i(TAG,"Getting locations, Url:  "+ url);
            String output =client.get(url);
            Log.i(TAG,"Output:"+output);
            locations = mapper.readValue(output,TypeFactory.defaultInstance().constructCollectionType(List.class, Location.class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            Log.e(TAG,"JsonProcessingException error to process JSON Object in LocationService");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"IOException error IOException in Location Service");
        }catch (NullPointerException e){
            Log.e(TAG,"NullPointerException reading locations");

        }
        return locations;

    }
}
