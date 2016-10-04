package com.snaptechnology.bgonzalez.bookmyroomandroid.activity;




import android.app.Activity;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;


import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.snaptechnology.bgonzalez.bookmyroomandroid.R;
import com.snaptechnology.bgonzalez.bookmyroomandroid.model.Location;
import com.snaptechnology.bgonzalez.bookmyroomandroid.services.LocationService;
import com.snaptechnology.bgonzalez.bookmyroomandroid.utils.UtilProperties;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class UpdateDeviceSettingFragment extends Fragment {

    MaterialBetterSpinner locationSpinner ;

    String[] SPINNER_DATA = {"Room name 1","Room name 2","Room name 3"};

    List<Location> locations ;

    LocationService locationService = new LocationService();

    List<String> locationsSpinner = new ArrayList<>();

    ArrayAdapter<String> adapter;

    private Toolbar mToolbar;




    public UpdateDeviceSettingFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_update_device_setting, container, false);

        locationSpinner = (MaterialBetterSpinner)rootView.findViewById(R.id.location_spinner);

        new MyTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        try {
            locationSpinner.setHint(new UtilProperties().getLocationProperty(getActivity()));
        } catch (IOException e) {
            e.printStackTrace();e.printStackTrace();
        }

        Button btnSave = (Button) rootView.findViewById(R.id.btn_save_device_setting);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new UtilProperties().setProperty(getActivity(),locationSpinner.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }

    public void updateLocation(View v){

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        List<Location> locationsRequest;

        @Override
        protected Void doInBackground(Void... params) {
            locationsRequest = locationService.getLocations();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            List<String> stringLocations = new ArrayList<>();
            for (Location location: locationsRequest){
                stringLocations.add(location.getDisplayName());
            }

            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, stringLocations);
            locationSpinner.setAdapter(adapter);
            super.onPostExecute(result);
        }
    }

}