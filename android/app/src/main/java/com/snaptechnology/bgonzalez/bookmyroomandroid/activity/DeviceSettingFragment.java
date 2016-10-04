package com.snaptechnology.bgonzalez.bookmyroomandroid.activity;

/**
 * Created by bgonzalez on 24/08/2016.
 */

import android.app.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;//
//import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.snaptechnology.bgonzalez.bookmyroomandroid.R;
import com.snaptechnology.bgonzalez.bookmyroomandroid.model.Location;
import com.snaptechnology.bgonzalez.bookmyroomandroid.utils.UtilProperties;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.IOException;
import java.util.List;


public class DeviceSettingFragment extends Fragment {


    public DeviceSettingFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_device_setting, container, false);
        TextView location = (TextView) rootView.findViewById(R.id.location);
        try {

            String data =new UtilProperties().getLocationProperty(getActivity());
            location.setText(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Button button = (Button) rootView.findViewById(R.id.btn_update_device_setting);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Fragment fragment = new UpdateDeviceSettingFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.container_body, fragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}