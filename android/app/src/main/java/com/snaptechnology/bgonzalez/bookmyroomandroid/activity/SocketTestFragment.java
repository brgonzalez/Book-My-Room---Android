package com.snaptechnology.bgonzalez.bookmyroomandroid.activity;

/**
 * Created by bgonzalez on 29/08/2016.
 */

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.snaptechnology.bgonzalez.bookmyroomandroid.R;
import com.snaptechnology.bgonzalez.bookmyroomandroid.circleprogress.AnimationState;
import com.snaptechnology.bgonzalez.bookmyroomandroid.circleprogress.AnimationStateChangedListener;
import com.snaptechnology.bgonzalez.bookmyroomandroid.circleprogress.CircleProgressView;
import com.snaptechnology.bgonzalez.bookmyroomandroid.circleprogress.TextMode;
import com.snaptechnology.bgonzalez.bookmyroomandroid.circleprogress.UnitPosition;
import com.snaptechnology.bgonzalez.bookmyroomandroid.model.Location;
import com.snaptechnology.bgonzalez.bookmyroomandroid.model.VO.EventVO;
import com.snaptechnology.bgonzalez.bookmyroomandroid.services.LocationService;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class SocketTestFragment extends Fragment {



    TextView text;
    public LocationService locationService = new LocationService();
    public SocketTestFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_socket_test, container, false);

        text = (TextView) rootView.findViewById(R.id.socket);

        new MyTask().execute();

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

    private class MyTask extends AsyncTask<Void, Void, Void> {

        String textResult;

        @Override
        protected Void doInBackground(Void... params) {
            textResult = locationService.getLocations().toString();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            text.setText(textResult);
            super.onPostExecute(result);
        }
    }


}