package com.snaptechnology.bgonzalez.bookmyroomandroid.activity;

/**
 * Created by bgonzalez on 24/08/2016.
 */

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.snaptechnology.bgonzalez.bookmyroomandroid.R;

import com.snaptechnology.bgonzalez.bookmyroomandroid.circleprogress.CircleProgressView;

import com.snaptechnology.bgonzalez.bookmyroomandroid.model.Event;
import com.snaptechnology.bgonzalez.bookmyroomandroid.services.EventService;
import com.snaptechnology.bgonzalez.bookmyroomandroid.services.TimeService;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.extra.Scale;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class HomeFragment extends Fragment {

    public static int size;

    private final static String TAG = "MainActivity";

    private EventService eventService = EventService.getInstance(getActivity());
    private TimeService timeService = new TimeService();

    private Event currentEvent;


    CircleProgressView mCircleView;
    Switch mSwitchSpin;
    Switch mSwitchShowUnit;
    SeekBar mSeekBar;
    SeekBar mSeekBarSpinnerLength;
    Boolean mShowUnit = true;
    Spinner mSpinner;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);



        final ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        ImageView image_unavalailable = (ImageView) rootView.findViewById(R.id.image_unavailable);
        ImageView image_available = (ImageView) rootView.findViewById(R.id.image_available);


        final ViewGroup transitionsContainer = (ViewGroup) rootView.findViewById(R.id.transitions_container);

        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Map<String, String > mainData = getDataHome();

        if( mainData.get("state").equalsIgnoreCase("B")){
            //unavailable
            progressBar.bringToFront();
            progressBar.setVisibility(View.VISIBLE);
            ObjectAnimator animation = ObjectAnimator.ofInt (progressBar, "progress", 50, 500); // see this max value coming back here, we animale towards that value
            animation.setDuration (10000); //in milliseconds
            animation.setInterpolator (new DecelerateInterpolator());
            image_unavalailable.setVisibility(View.VISIBLE);
            image_unavalailable.bringToFront();
            animation.start ();

            final TextView finished = (TextView) transitionsContainer.findViewById(R.id.circle_finished);

            transitionsContainer.findViewById(R.id.image_unavailable).setOnClickListener(new VisibleToggleClickListener() {

                @Override
                protected void changeVisibility(boolean visible) {
                    TransitionManager.beginDelayedTransition(transitionsContainer, new Scale());
                    finished.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);

                }


            });
        }else{

            //available
            image_available.setVisibility(View.VISIBLE);
            image_available.bringToFront();




            final TextView minutes1 = (TextView) transitionsContainer.findViewById(R.id.minutes1);
            final TextView minutes2 = (TextView) transitionsContainer.findViewById(R.id.minutes2);
            final TextView minutes3 = (TextView) transitionsContainer.findViewById(R.id.minutes3);
            final TextView minutes4 = (TextView) transitionsContainer.findViewById(R.id.minutes4);


            transitionsContainer.findViewById(R.id.image_available).setOnClickListener(new VisibleToggleClickListener() {

                @Override
                protected void changeVisibility(boolean visible) {
                    TransitionManager.beginDelayedTransition(transitionsContainer, new Scale());
                    minutes1.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
                    minutes2.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
                    minutes3.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
                    minutes4.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
                }


            });



        }

        Log.i("State", mainData.get("state"));

        TextView availableIn =(TextView) rootView.findViewById(R.id.available_in);
        availableIn.setText(mainData.get("availableIn"));

        TextView nextMeeting =(TextView) rootView.findViewById(R.id.next_meeting);
        nextMeeting.setText(mainData.get("nextMeeting"));





        // Inflate the layout for this fragment
        return rootView;
    }

    public String getNextMeeting(){
        return "";
    }

    private Map<String,String> getDataHome(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Map<String,String> dataHome = new HashMap<>();

        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.add(Calendar.HOUR_OF_DAY, 4); // adds one hour

        Date currentDate = new Date();
        String currentDateInString = df.format(cal.getTime());
        boolean startIsHigherToCurrent;
        boolean endIsHigherToCurrent;

        int sizeEvents =eventService.getEvent().size();
        Event tmpEvent ;
        for(int i = 0 ; i < sizeEvents; i++){
            tmpEvent = eventService.getEvent().get(i);
            startIsHigherToCurrent = timeService.isGreaterDate(tmpEvent.getStart(), currentDateInString);
            endIsHigherToCurrent = timeService.isGreaterDate(tmpEvent.getEnd(), currentDateInString);


            /**Start and end are higher than current*/
            if( startIsHigherToCurrent && endIsHigherToCurrent ){
                dataHome.put("state","F");
                dataHome.put("availableIn","Now");
                dataHome.put("nextMeeting",timeService.calculateDifferenceInString(currentDateInString, eventService.getEvent().get(i).getStart()));
                currentEvent = null;
                return dataHome;
            }
            else if( !startIsHigherToCurrent && endIsHigherToCurrent  ){
                currentEvent = tmpEvent;
                /**if exist other event in the week*/
                if ( i +1  < sizeEvents){
                    dataHome.put("nextMeeting", timeService.calculateDifferenceInString(currentDateInString, eventService.getEvent().get(i+1).getStart()));
                }else{
                    dataHome.put("nextMeeting", "Next Week");
                }
                dataHome.put("state","B");
                int tempI = i + 1;
                String tempDateInString;
                Event nextEvent;
                while(tempI < sizeEvents){
                    tempDateInString = tmpEvent.getEnd();
                    nextEvent = eventService.getEvent().get(tempI);
                    if (tempI + 1 == sizeEvents){
                        dataHome.put("availableIn", timeService.calculateDifferenceInString(currentDateInString, tempDateInString));
                    }


                    if( tempDateInString.equalsIgnoreCase(nextEvent.getStart()) ){

                    }else{
                        dataHome.put("availableIn", timeService.calculateDifferenceInString(currentDateInString, tempDateInString));
                        return dataHome;
                    }
                    tempI++;
                }
                dataHome.put("availableIn",timeService.calculateDifferenceInString( currentDateInString, eventService.getEvent().get(sizeEvents-1).getEnd()));


                return dataHome;


            }



            /**Start and end are less than current*/
            else if( !startIsHigherToCurrent && !endIsHigherToCurrent  ){
                /**Pass to next event*/
            }

        }
        currentEvent = null;
        dataHome.put("state","F");
        dataHome.put("availableIn","Now");
        dataHome.put("nextMeeting","Next Week");

        return dataHome;
    }

    private String calculateNextMeeting(Event event){
        return "";
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public static void main(String[] args){
        TimeService timeService = new TimeService();
        if (timeService.isGreaterDate("2015-09-09T01:00:00Z", "2015-09-09T00:01:00Z")){
            System.out.println("1");
        }else{
            System.out.println("2");
        }

    }

}
