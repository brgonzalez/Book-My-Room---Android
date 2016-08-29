package com.snaptechnology.bgonzalez.bookmyroomandroid.activity;

/**
 * Created by bgonzalez on 25/08/2016.
 */
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.snaptechnology.bgonzalez.bookmyroomandroid.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;


public class UpdateDeviceSettingFragment extends Fragment {

    MaterialBetterSpinner materialBetterSpinner ;

    String[] SPINNER_DATA = {"Room name 1","Room name 2","Room name 3"};


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

        materialBetterSpinner = (MaterialBetterSpinner)rootView.findViewById(R.id.material_spinner1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, SPINNER_DATA);

        materialBetterSpinner.setAdapter(adapter);
        // Inflate the layout for this fragment
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