package com.snaptechnology.bgonzalez.bookmyroomandroid.activity;

/**
 *
 *
 * @autor Brayan González
 * @since 24/08/2016.
 */

import android.app.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.snaptechnology.bgonzalez.bookmyroomandroid.R;
import com.snaptechnology.bgonzalez.bookmyroomandroid.utils.FileUtils;



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


        String data = FileUtils.readLocation(getActivity());

        location.setText(data);
        Button button = (Button) rootView.findViewById(R.id.btn_update_device_setting);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    public void onDetach() {
        super.onDetach();
    }
}