package com.snaptechnology.bgonzalez.bookmyroomandroid.activity;




import android.app.Activity;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import com.snaptechnology.bgonzalez.bookmyroomandroid.R;
import com.snaptechnology.bgonzalez.bookmyroomandroid.model.Location;
import com.snaptechnology.bgonzalez.bookmyroomandroid.services.EventService;
import com.snaptechnology.bgonzalez.bookmyroomandroid.services.LocationService;
import com.snaptechnology.bgonzalez.bookmyroomandroid.utils.FileUtil;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class UpdateDeviceSettingFragment extends Fragment {

    private static String TAG = UpdateDeviceSettingFragment.class.getSimpleName();

    private EventService eventService = EventService.getInstance(getActivity());

    MaterialBetterSpinner locationSpinner ;
    List<Location> locationsRequest;

    LocationService locationService = new LocationService();

    ArrayAdapter<String> adapter;

    private static int WAITING_TIME = 4000;

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


        locationSpinner.setHint("Room name");
        String current = new FileUtil().readLocation(getActivity());
        if(current != null){
            locationSpinner.setText(current);
        }

        final Thread marcoThread = new Thread(new Runnable() {
            @Override
            public void run() {
                locationsRequest = locationService.getLocations();

            }
        });

        marcoThread.start();

        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE).setTitleText("Loading rooms...");
        pDialog.show();
        pDialog.setCancelable(false);

        new CountDownTimer(WAITING_TIME, 800) {
            public void onTick(long millisUntilFinished) {
                try{
                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));
                }catch (NullPointerException e){
                    Log.e(TAG,"Error while was changed the color of waiting bar");
                }
            }
            public void onFinish() {
                try {
                    marcoThread.join();
                } catch (InterruptedException e) {
                    Log.e(TAG, "InterruptedException joining createThread");
                }
                if(locationsRequest.size()==0) {
                    pDialog.setTitleText("Error!")
                            .setContentText("Probably the device do not have access to the server")
                            .setConfirmText("OK")
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            refreshFragment();
                            pDialog.cancel();
                        }
                    });
                }else{
                    List<String> locations = new ArrayList<>();
                    for (Location location: locationsRequest){
                        locations.add(location.getDisplayName());
                    }
                    adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, locations);
                    locationSpinner.setAdapter(adapter);
                    pDialog.cancel();
                }
            }
        }.start();


        Button btnSave = (Button) rootView.findViewById(R.id.btn_save_device_setting);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FileUtil().writeLocation(getActivity(),locationSpinner.getText().toString());
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
                sweetAlertDialog.setTitleText("Success");
                sweetAlertDialog.setContentText("Room Account was changed");
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        refreshFragment();
                        eventService.updateEvents();
                    }
                });
                sweetAlertDialog.show();

            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }

    private void refreshFragment(){
        try{
            Fragment fragment = new DeviceSettingFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.container_body, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }catch ( NullPointerException e){
            Log.i("Warning refresh","Update fragment not completed");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class ReaderProperties extends AsyncTask<Void, Void, Void> {

        List<Location> locationsRequest;

        @Override
        protected Void doInBackground(Void... params) {
            //locationsRequest = locationService.getLocations();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try{
                List<String> stringLocations = new ArrayList<>();
                //for (Location location: locationsRequest){
                //    stringLocations.add(location.getDisplayName());
                //}
                stringLocations.add("s");

                adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, stringLocations);
                locationSpinner.setAdapter(adapter);
            } catch(NullPointerException e){
                Log.e("TAG", "Null");
            }

            super.onPostExecute(result);
        }
    }

}