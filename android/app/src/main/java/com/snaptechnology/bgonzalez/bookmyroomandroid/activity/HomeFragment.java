package com.snaptechnology.bgonzalez.bookmyroomandroid.activity;

/**
 * Created by bgonzalez on 24/08/2016.
 */

import android.animation.ObjectAnimator;
import android.app.*;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.*;
import cn.pedant.SweetAlert.SweetAlertDialog;
import com.snaptechnology.bgonzalez.bookmyroomandroid.R;
import com.snaptechnology.bgonzalez.bookmyroomandroid.model.Attendee;
import com.snaptechnology.bgonzalez.bookmyroomandroid.model.Event;
import com.snaptechnology.bgonzalez.bookmyroomandroid.model.Location;
import com.snaptechnology.bgonzalez.bookmyroomandroid.services.EventService;
import com.snaptechnology.bgonzalez.bookmyroomandroid.services.TimeService;
import com.snaptechnology.bgonzalez.bookmyroomandroid.utils.UtilProperties;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.extra.Scale;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class HomeFragment extends Fragment {

    private final static String TAG = "MainActivity";
    private long mLastClickTime = 0;


    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();



    private EventService eventService = EventService.getInstance(getActivity());
    private TimeService timeService = new TimeService();

    private Event currentEvent;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        StrictMode.setThreadPolicy(policy);

        Runnable myRunnable = new Runnable(){
            public void run(){
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                refreshFragment();
            }
        };
        Thread thread = new Thread(myRunnable);
        thread.start();

        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        final ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        ImageView image_unavailable = (ImageView) rootView.findViewById(R.id.image_unavailable);
        ImageView image_available = (ImageView) rootView.findViewById(R.id.image_available);

        final ViewGroup transitionsContainer = (ViewGroup) rootView.findViewById(R.id.transitions_container);

        Map<String, String > mainData = getDataHome();

        if( mainData.get("state").equalsIgnoreCase("B")){
            long timeMeeting =timeService.calculateDifferenceDates(currentEvent.getStart(), currentEvent.getEnd());

            long diff =  timeService.calculateDifferenceDates(currentEvent.getStart(),timeService.getActualTimeInString());

            int total = (int) ((int)timeMeeting *.1);
            int progress = (int) ((int)(diff) *.1);

            //unavailable
            progressBar.bringToFront();
            progressBar.setVisibility(View.VISIBLE);
            ObjectAnimator animation = ObjectAnimator.ofInt (progressBar, "progress", progress*500/total, 500); // see this max value coming back here, we animale towards that value
            animation.setDuration (5000000); //in milliseconds
            animation.setInterpolator (new DecelerateInterpolator());
            image_unavailable.setVisibility(View.VISIBLE);
            image_unavailable.bringToFront();
            animation.start ();

            final TextView finished = (TextView) transitionsContainer.findViewById(R.id.circle_finished);

            transitionsContainer.findViewById(R.id.image_unavailable).setOnClickListener(new VisibleToggleClickListener() {

                @Override
                protected void changeVisibility(boolean visible) {
                    TransitionManager.beginDelayedTransition(transitionsContainer, new Scale());
                    finished.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
                }


            });

            finished.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // mis-clicking prevention, using threshold of 1000 ms
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    deleteEvent(currentEvent);
                }
            });

        }else{

            /**when is Available*/

            /**Store ids of view to each minute available*/
            final List<Integer> rIds = new ArrayList<>();
            rIds.add(R.id.minutes1);
            rIds.add(R.id.minutes2);
            rIds.add(R.id.minutes3);
            rIds.add(R.id.minutes4);

            /** bring circle to front*/
            image_available.setVisibility(View.VISIBLE);
            image_available.bringToFront();

            String timeInString = timeService.getActualTimeInString();
            final List<String> availableMinutes = getAvailableMinutes(timeInString);

            /** To book the quarter hour before*/
            final String start = timeService.roundDateToLessInString(timeInString);
            /** Is booked from the next quarter hour according to the users*/
            final String tmpEnd = timeService.roundDateToHigherInString(timeInString);

            for(int i = 0; i < availableMinutes.size(); i++){
                /** Declare a text view according to available minutes */
                TextView minute = (TextView) rootView.findViewById(rIds.get(i));

                minute.setText(availableMinutes.get(i));

                final String end = timeService.addMinutes(tmpEnd,i+1);
                minute.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // mis-clicking prevention, using threshold of 1000 ms
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                    /** Instantiate an AlertDialog.Builder with its constructor */
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    /** Get the layout inflater */
                    LayoutInflater inflater = getActivity().getLayoutInflater();

                    /** Indexing dialog*/
                    final View dialogView =  inflater.inflate(R.layout.dialog_book_room_from_home,null);
                    builder.setView(dialogView);

                    final AlertDialog dialog = builder.create();

                    Button buttonBookRoom = (Button) dialogView.findViewById(R.id.btn_book_room_from_home);
                    buttonBookRoom.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // mis-clicking prevention, using threshold of 1000 ms
                            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                                return;
                            }
                            mLastClickTime = SystemClock.elapsedRealtime();

                            String id = "id";
                            String subject = (((TextInputLayout) dialogView.findViewById(R.id.subject_book_room_from_home)).getEditText().getText()).toString();
                            Event event = new Event(id, subject, new Location(UtilProperties.getLocationProperty(getActivity())),new ArrayList<Attendee>(), false,  start,  end);
                            createEvent(event);

                        dialog.cancel();
                        }
                    });
                    dialog.show();
                    }

                });

            }

            transitionsContainer.findViewById(R.id.image_available).setOnClickListener(new VisibleToggleClickListener() {

                @Override
                protected void changeVisibility(boolean visible) {

                TransitionManager.beginDelayedTransition(transitionsContainer, new Scale());
                for(int i = 0 ; i < availableMinutes.size(); i++){
                    (rootView.findViewById(rIds.get(i))).setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
                }
                }


            });

        }

        TextView availableIn =(TextView) rootView.findViewById(R.id.available_in);
        availableIn.setText(mainData.get("availableIn"));

        TextView nextMeeting =(TextView) rootView.findViewById(R.id.next_meeting);
        nextMeeting.setText(mainData.get("nextMeeting"));

        final TableLayout timeLine = (TableLayout) rootView.findViewById(R.id.time_line);

        final TableRow row  = (TableRow) timeLine.getChildAt(1);
        String start =  timeService.getInitialTimeDay();
        for(int i = 0 ; i < row.getChildCount(); i++){
            final TextView v = (TextView) row.getChildAt(i);

            if(eventService.getEventMapper().containsKey(start)){
                v.setBackgroundColor(Color.RED);
            }
            start = timeService.addMinutes(start);
        }



        // Inflate the layout for this fragment
        return rootView;
    }

    public List<String> getAvailableMinutes(String date){

        List<String> availableMinutes = new ArrayList<>();
        int minutes = timeService.getMinMin();



        String limitDate = timeService.resetHoursStringDate(date);
        limitDate = timeService.addADay(limitDate);

        Date actual = timeService.convertStringToDate(date);
        timeService.roundDateToHigherInString(date);
        Date nextDay = timeService.convertStringToDate(limitDate);

        while(!eventService.getEventMapper().containsKey(date) && actual.compareTo(nextDay) < 0 && availableMinutes.size() < 4) {

            availableMinutes.add(Integer.toString(minutes) + " min");
            minutes += timeService.getMinMin();
            date = timeService.addMinutes(date);
            actual = timeService.convertStringToDate(date);
        }


        return availableMinutes;
    }

    private Map<String,String> getDataHome(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Map<String,String> dataHome = new HashMap<>();

        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.add(Calendar.HOUR_OF_DAY, 4); //

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
            /**Start is less than current and end is higher than current */
            else if( !startIsHigherToCurrent && endIsHigherToCurrent  ){
                Log.e("Midle","en el medio");
                currentEvent = tmpEvent;
                dataHome.put("state","B");

                /**if exist other event in the week*/
                if ( i + 1  < sizeEvents){
                    Log.e("Midle","if");

                    dataHome.put("nextMeeting", timeService.calculateDifferenceInString(currentDateInString, eventService.getEvent().get(i+1).getStart()));
                    while( i + 1 < sizeEvents){ // suma uno ya que si era el ultimo se evaluo en el caso anterior
                        /** Compare actual with next*/
                        if ( ! eventService.getEvent().get(i).getEnd().equalsIgnoreCase(eventService.getEvent().get( i + 1 ).getStart())){
                            dataHome.put("availableIn",timeService.calculateDifferenceInString(currentDateInString,eventService.getEvent().get(i).getEnd()));
                            return dataHome;
                        }
                        i++;
                    }
                    dataHome.put("availableIn",timeService.calculateDifferenceInString(currentDateInString,eventService.getEvent().get(sizeEvents - 1).getEnd()));
                    return dataHome;


                }else{
                    dataHome.put("nextMeeting", "Next Week");
                    dataHome.put("availableIn",timeService.calculateDifferenceInString(currentDateInString,tmpEvent.getEnd()));
                    dataHome.put("state","B");
                    return dataHome;
                }
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

    private void createEvent(final Event event){
        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE).setTitleText("Creating...");
        pDialog.show();
        pDialog.setCancelable(false);

        new CountDownTimer(800 * 7, 800) {
            boolean isExecuted = false;
            boolean isCreated = false;
            public void onTick(long millisUntilFinished) {
                try{
                    pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.blue_btn_bg_color));
                }catch (IllegalStateException e){
                    Log.e("Error","Error showing progress bar because user tried to create two event at same time");
                }
                while (isExecuted == false){
                    isExecuted = true;
                    isCreated = eventService.createEvent(event);
                }
            }

            public void onFinish() {
                if(isCreated) {
                    pDialog.setTitleText("Success!")
                            .setConfirmText("OK")
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                }else{
                    pDialog.setTitleText("Error!")
                            .setConfirmText("OK")
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                }
                refreshFragment();

            }
        }.start();

    }

    private void deleteEvent(final Event event){
        final SweetAlertDialog sweetAlertDialog =  new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
                sweetAlertDialog.setTitleText("Are you sure?")
                .setContentText("Won't be able to recover this meeting!")
                .setCancelText("Cancel")
                .setConfirmText("Delete")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE).setTitleText("Deleting");
                        pDialog.show();
                        pDialog.setCancelable(false);

                        new CountDownTimer(800 * 7, 800) {
                            boolean isExecuted = false;
                            boolean isDeleted = false;
                            public void onTick(long millisUntilFinished) {
                                try{
                                    pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.blue_btn_bg_color));
                                }catch (IllegalStateException e){
                                    Log.e("Error","Error showing progress bar because user tried to create two event at same time");
                                }
                                while (isExecuted == false){
                                    isExecuted = true;
                                    isDeleted = eventService.deleteEvent(event);
                                }
                            }

                            public void onFinish() {
                                if(isDeleted) {
                                    pDialog.setTitleText("Deleted it!")
                                            .setConfirmText("OK")
                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    sweetAlertDialog.cancel();


                                }else{
                                    pDialog.setTitleText("Error!")
                                            .setConfirmText("OK")
                                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                    sweetAlertDialog.cancel();
                                }

                                refreshFragment();

                            }
                        }.start();

                    }
                })
                .show();
    }

    private void refreshFragment(){
        try{
            Fragment fragment = new HomeFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }catch ( NullPointerException e){
            Log.i("Warning","Was tried reload the home fragment while user used home screen");
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


    private class CreaterEvents extends AsyncTask<Void, Void, Void> {

        Event event;
        int i = -1;

        public CreaterEvents(Event event){
            super();
            this.event = event;
        }
        @Override
        protected Void doInBackground(Void... params) {
            //SweetAlertDialog sd = new SweetAlertDialog(getActivity());
            getActivity().runOnUiThread(new Runnable() {
                public void run() {

                    final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE)
                            .setTitleText("Loading");
                    pDialog.show();
                    pDialog.setCancelable(false);
                    new CountDownTimer(800 * 7, 800) {
                        public void onTick(long millisUntilFinished) {
                            pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.blue_btn_bg_color));
                            if(eventService.createEvent(event)){
                                return;
                            }
                        }

                        public void onFinish() {
                            i = -1;
                            pDialog.setTitleText("Success!")
                                    .setConfirmText("OK")
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            Fragment fragment = new HomeFragment();
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.container_body, fragment);
                            fragmentTransaction.commit();
                        }
                    }.start();

                }
            });

            ;

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    private class DeleterEvents extends AsyncTask<Void, Void, Void> {

        Event event;

        public DeleterEvents(Event event){
            super();
            this.event = event;
        }
        @Override
        protected Void doInBackground(Void... params) {
            eventService.deleteEvent(event);
            Fragment fragment = new HomeFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
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
