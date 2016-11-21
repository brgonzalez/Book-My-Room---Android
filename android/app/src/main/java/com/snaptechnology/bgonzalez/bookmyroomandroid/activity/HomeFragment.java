package com.snaptechnology.bgonzalez.bookmyroomandroid.activity;

/**
 * Home Fragment, you can book and delete events from this fragment
 */

import android.animation.ObjectAnimator;
import android.app.AlertDialog;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.design.widget.TextInputLayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import cn.pedant.SweetAlert.SweetAlertDialog;
import com.snaptechnology.bgonzalez.bookmyroomandroid.R;
import com.snaptechnology.bgonzalez.bookmyroomandroid.model.Attendee;
import com.snaptechnology.bgonzalez.bookmyroomandroid.model.Event;
import com.snaptechnology.bgonzalez.bookmyroomandroid.model.Location;
import com.snaptechnology.bgonzalez.bookmyroomandroid.services.EventService;
import com.snaptechnology.bgonzalez.bookmyroomandroid.services.TimeService;
import com.snaptechnology.bgonzalez.bookmyroomandroid.services.URLService;
import com.snaptechnology.bgonzalez.bookmyroomandroid.utils.FileUtil;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.extra.Scale;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeFragment extends Fragment {
    private static String TAG = HomeFragment.class.getSimpleName();

    /**Variable used to check the last click by the user*/
    private static long mLastClickTime;

    private static final int WAITING_TIME = 5000;


    /**Services to get the functions to do the task by the user*/
    private EventService eventService = EventService.getInstance(getActivity());
    private TimeService timeService = new TimeService();

    /** Current event to show to user, if is null is because there is not event at the moment*/
    private Event currentEvent;

    /** Skip Policies to make request to the server easier */
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

    private static boolean wasSuccessfulOperation = false;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        StrictMode.setThreadPolicy(policy); // set policies to this fragment

        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);


        // Declarations to main circle to book and delete events
        final ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        final ViewGroup transitionsContainer = (ViewGroup) rootView.findViewById(R.id.transitions_container);

        // Get the status actual to the home fragment
        Map<String, String> mainData = getDataHome();

        if (mainData.get("state").equalsIgnoreCase("B")) { // in case of being busy
            //calculates to progress circle bar
            int meetingTime = (int) (timeService.getDifference(currentEvent.getStart(), currentEvent.getEnd()) * .1);
            int lapsedMeetingTime = (int) (timeService.getDifference(currentEvent.getStart(), timeService.getTimeInString()) * .1);

            progressBar.bringToFront();
            progressBar.setVisibility(View.VISIBLE);

            int progress = lapsedMeetingTime * 500 / meetingTime;
            ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", progress, 500); // see this max value coming back here, we animale towards that value
            animation.setDuration(5000000);
            animation.setInterpolator(new DecelerateInterpolator());
            animation.start();

            ImageView unavailableImage = (ImageView) rootView.findViewById(R.id.image_unavailable);
            unavailableImage.setVisibility(View.VISIBLE);
            unavailableImage.bringToFront();

            final TextView cancelButton = (TextView) transitionsContainer.findViewById(R.id.circle_finished);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    deleteEvent(currentEvent);
                }
            });

            transitionsContainer.findViewById(R.id.image_unavailable).setOnClickListener(new VisibleToggleClickListener() {
                @Override
                protected void changeVisibility(boolean visible) {
                    TransitionManager.beginDelayedTransition(transitionsContainer, new Scale());
                    cancelButton.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
                }
            });


        } else {// in the case of being free

            ImageView availableImage = (ImageView) rootView.findViewById(R.id.image_available);
            availableImage.setVisibility(View.VISIBLE);
            availableImage.bringToFront();

            //Store ids of view to each minute available
            final List<Integer> rIds = new ArrayList<>();
            rIds.add(R.id.fifteen_minutes);
            rIds.add(R.id.thirty_minutes);
            rIds.add(R.id.fortyFive_minutes);
            rIds.add(R.id.sixty_minutes);


            final String dateInString = timeService.getTimeInString();
            final List<String> availableMinutes = getAvailableMinutesToBook(dateInString);
            final String tmpEndDate = timeService.roundUp(dateInString);


            //Calculate available minutes to book the location
            for (int i = 0; i < availableMinutes.size(); i++) {
                TextView minutesToBook = (TextView) rootView.findViewById(rIds.get(i));

                final String endDate = timeService.addMinutes(tmpEndDate, i + 1);
                minutesToBook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        final View dialogView = inflater.inflate(R.layout.dialog_book_room_from_home, null);
                        builder.setView(dialogView);
                        final AlertDialog dialog = builder.create();

                        //Button to book
                        Button buttonBookRoom = (Button) dialogView.findViewById(R.id.btn_book_room_from_home);
                        buttonBookRoom.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                                    return;
                                }
                                mLastClickTime = SystemClock.elapsedRealtime();

                                String startDate = timeService.roundDown(dateInString);
                                String subject = (((TextInputLayout) dialogView.findViewById(R.id.subject_book_room_from_home)).getEditText().getText()).toString();
                                Event event = new Event("id", subject, new Location(FileUtil.readLocation(getActivity())),null, new ArrayList<Attendee>(), false, startDate, endDate);
                                dialog.dismiss();
                                createEvent(event);
                            }
                        });
                        dialog.show();

                    }
                });
            }
            // Change the visibility of minutes to book the location
            transitionsContainer.findViewById(R.id.image_available).setOnClickListener(new VisibleToggleClickListener() {
                @Override
                protected void changeVisibility(boolean visible) {
                    TransitionManager.beginDelayedTransition(transitionsContainer, new Scale());
                    for (int i = 0; i < availableMinutes.size(); i++) {
                        (rootView.findViewById(rIds.get(i))).setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
                    }
                }
            });
        }
        // Modify the texts next to the main circle
        TextView availableIn = (TextView) rootView.findViewById(R.id.available_in);
        availableIn.setText(mainData.get("availableIn"));

        TextView nextMeeting = (TextView) rootView.findViewById(R.id.next_meeting);
        nextMeeting.setText(mainData.get("nextMeeting"));


        refreshTimeline(rootView);

        //Button to show attendees
        final TextView attendees = (TextView) rootView.findViewById(R.id.people_image);
        attendees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                showAttendees();

            }
        });
        return rootView;
    }

    /**
     * Method to display dialog showing the attendees
     */
    private void showAttendees(){
        /** TODO: Add the organizer to the attendees list*/
        List<String> attendeesName = new ArrayList<>();

        if(currentEvent != null){
            if(currentEvent.getOrganizer()!= null){
                attendeesName.add(currentEvent.getOrganizer().getEmailAddress().getName() + " ( Organizer )");
            }
            for(Attendee attendee : currentEvent.getAttendees()){
                if(!attendee.getEmailAddress().getName().equalsIgnoreCase(currentEvent.getOrganizer().getEmailAddress().getName())){
                    attendeesName.add(attendee.getEmailAddress().getName());
                }
            }

        }else{
            attendeesName.add("No booking");
        }
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View convertView = inflater.inflate(R.layout.dialog_atendees, null);
        alertDialog.setView(convertView);
        ListView lv = (ListView) convertView.findViewById(R.id.listView1);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,attendeesName);
        lv.setAdapter(adapter);
        alertDialog.show();
    }

    /**
     * Method to get the available minutes to book a meeting
     * @param date current date
     * @return String list that contains available minutes
     */
    public List<String> getAvailableMinutesToBook(String date){

        List<String> availableMinutes = new ArrayList<>();
        int minutes = timeService.getMinMin();

        String limitDateOfDay = timeService.addADay(timeService.cleanDate(date));

        Date actual = timeService.convertStringToDate(date);
        date = timeService.roundUp(date);
        Date nextDay = timeService.convertStringToDate(limitDateOfDay);

        while(!eventService.getEventMapper().containsKey(date) && actual.compareTo(nextDay) < 0 && availableMinutes.size() < 4) {

            availableMinutes.add(Integer.toString(minutes) + " min");
            minutes += timeService.getMinMin();
            date = timeService.addMinutes(date);
            actual = timeService.convertStringToDate(date);
        }
        return availableMinutes;
    }

    /**
     * Method to get the data to fill the home fragment
     *
     * @return Map with current state and information of next meeting and when is available
     */
    private Map<String,String> getDataHome(){
        Map<String,String> dataHome = new HashMap<>();

        String currentDateInString = timeService.getTimeInString();
        boolean startIsHigherToCurrent;
        boolean endIsHigherToCurrent;

        int sizeEvents = eventService.getEvents().size();
        Event tmpEvent ;
        for(int i = 0 ; i < sizeEvents; i++){
            tmpEvent = eventService.getEvents().get(i);
            startIsHigherToCurrent = timeService.isHigherOrEqual(tmpEvent.getStart(), currentDateInString);
            endIsHigherToCurrent = timeService.isHigherOrEqual(tmpEvent.getEnd(), currentDateInString);

            //**Start and end are higher than current*/
            if( startIsHigherToCurrent && endIsHigherToCurrent ){
                dataHome.put("state","F");
                dataHome.put("availableIn","Now");
                dataHome.put("nextMeeting",timeService.getDifferenceInText(currentDateInString, eventService.getEvents().get(i).getStart()));
                currentEvent = null;
                return dataHome;
            }
            //**Start is less than current and end is higher than current */
            else if( !startIsHigherToCurrent && endIsHigherToCurrent  ){
                currentEvent = tmpEvent;
                dataHome.put("state","B");

                //**if exist other event in the week*/
                if ( i + 1  < sizeEvents){

                    dataHome.put("nextMeeting", timeService.getDifferenceInText(currentDateInString, eventService.getEvents().get(i+1).getStart()));
                    while( i + 1 < sizeEvents){
                        //** Compare actual with next event*/
                        if ( ! eventService.getEvents().get(i).getEnd().equalsIgnoreCase(eventService.getEvents().get( i + 1 ).getStart())){
                            dataHome.put("availableIn",timeService.getDifferenceInText(currentDateInString,eventService.getEvents().get(i).getEnd()));
                            return dataHome;
                        }
                        i++;
                    }
                    dataHome.put("availableIn",timeService.getDifferenceInText(currentDateInString,eventService.getEvents().get(sizeEvents - 1).getEnd()));
                    return dataHome;


                }else{
                    dataHome.put("nextMeeting", "Next Week");
                    dataHome.put("availableIn",timeService.getDifferenceInText(currentDateInString,tmpEvent.getEnd()));
                    dataHome.put("state","B");
                    return dataHome;
                }
            }
            // In other case do not make anything/

        }
        currentEvent = null;
        dataHome.put("state","F");
        dataHome.put("availableIn","Now");
        dataHome.put("nextMeeting","Next Week");

        return dataHome;
    }

    /**
     * Method to create a event from the GUI
     * @param event event to create
     */
    private void createEvent(final Event event){
        final Thread createThread =new Thread(new Runnable() {// Thread to refresh the home fragment
            @Override
            public void run() {
                eventService.updateEvents();
                wasSuccessfulOperation = eventService.doPost(event,new URLService().getURLCreateEvent());
            }
        });
        createThread.start();

        final SweetAlertDialog createDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE).setTitleText("Creating...");
        createDialog.show();
        createDialog.setCancelable(false);

        new CountDownTimer(WAITING_TIME, 800) {
            public void onTick(long millisUntilFinished) {
                try{
                    createDialog.getProgressHelper().setBarColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));
                }catch (NullPointerException e){
                    Log.e(TAG,"Error while was changed the color of waiting bar");
                }
            }
            public void onFinish() {
                try {
                    createThread.join();
                } catch (InterruptedException e) {
                    Log.e(TAG, "InterruptedException joining createThread");
                }
                if(wasSuccessfulOperation) {
                    createDialog.setTitleText("Success!")
                            .setConfirmText("OK")
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    wasSuccessfulOperation = false;
                }else{
                    createDialog.setTitleText("Error! Try again")
                            .setContentText("Probably the device do not have access to the server")
                            .setConfirmText("OK")
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                }
                refreshFragment();
            }
        }.start();
    }

    /**
     * Method to delete a event from the GUI
     * @param event to delete
     */
    private void deleteEvent(final Event event) throws NullPointerException{

        final SweetAlertDialog sweetAlertDialog =  new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
                sweetAlertDialog.setTitleText("Are you sure?")
                .setContentText("Won't be able to recover this meeting!")
                .setCancelText("Cancel")
                .setConfirmText("Delete")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        final Thread deleteThread = new Thread(new Runnable() {// Thread to refresh the home fragment
                            @Override
                            public void run() {
                                wasSuccessfulOperation = eventService.doPost(event, new URLService().getURLDeleteEvent());
                            }
                        });
                        deleteThread.start();

                        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE).setTitleText("Deleting");
                        pDialog.show();
                        pDialog.setCancelable(false);

                        new CountDownTimer(WAITING_TIME, 1000) {
                            //display waiting bar
                            public void onTick(long millisUntilFinished) {
                                try{
                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));
                                }catch (NullPointerException e){
                                    Log.e(TAG,"Error while was changed the color of waiting bar");
                                }
                            }
                            public void onFinish() {
                                try {
                                    deleteThread.join();
                                } catch (InterruptedException e) {
                                    Log.e(TAG, "InterruptedException joining deleteThread");
                                }
                                if(wasSuccessfulOperation) {
                                    pDialog.setTitleText("Deleted it!")
                                            .setConfirmText("OK")
                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    sweetAlertDialog.cancel();
                                    wasSuccessfulOperation = false;
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

    /**
     * Method to refresh the time line day of home screen
     * @param rootView main view
     */
    private void refreshTimeline(View rootView){
        final TableLayout timeLine = (TableLayout) rootView.findViewById(R.id.time_line);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final TableRow rowTimeLine = (TableRow) timeLine.getChildAt(1);
                String start = timeService.getInitialTimeDay();
                for (int i = 0; i < rowTimeLine.getChildCount(); i++) {
                    final TextView v = (TextView) rowTimeLine.getChildAt(i);
                    if (eventService.getEventMapper().containsKey(start)) {
                        v.setBackgroundColor(Color.RED);
                    }
                    start = timeService.addMinutes(start);
                }
            }
        }).start();
    }

    /**
     * Method to refresh the fragment
     */
    private void refreshFragment()  {
        try {
            Fragment fragment = new HomeFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.container_body, fragment);
            transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();

        } catch (NullPointerException e) {
            Log.i(TAG, "Refresh fragment not completed");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

}
