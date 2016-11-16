package com.snaptechnology.bgonzalez.bookmyroomandroid.activity;

import android.app.AlertDialog;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.snaptechnology.bgonzalez.bookmyroomandroid.R;
import com.snaptechnology.bgonzalez.bookmyroomandroid.model.Attendee;
import com.snaptechnology.bgonzalez.bookmyroomandroid.model.Event;
import com.snaptechnology.bgonzalez.bookmyroomandroid.model.Location;
import com.snaptechnology.bgonzalez.bookmyroomandroid.services.EventService;
import com.snaptechnology.bgonzalez.bookmyroomandroid.services.TimeService;
import com.snaptechnology.bgonzalez.bookmyroomandroid.services.URLService;
import com.snaptechnology.bgonzalez.bookmyroomandroid.utils.FileUtil;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class WeeklyCalendarFragment extends Fragment {

    private static final String TAG = WeeklyCalendarFragment.class.getSimpleName();

    //The WAITING_TIME must be higher or equals to the timeout to make a request
    private static final int WAITING_TIME = 5000;
    private long mLastClickTime = 0;

    private EventService eventService = EventService.getInstance(getActivity());
    private TimeService timeService = new TimeService();

    private static Boolean wasSuccessfulOperation = false;

    public WeeklyCalendarFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        //TextView headerCalendar = (TextView) rootView.findViewById(R.id.week);
        //headerCalendar.setText(getStringHeaderCalendar());

        eventService.getTimeService().updateIdsWeeklyCalendar();

        //**Store dates to assign to each cell(TextView) of the table */
        String[][] ids = eventService.getTimeService().getDates();

        final TableLayout table = (TableLayout) rootView.findViewById(R.id.table_calendar);

        for(int i = 0 ; i < table.getChildCount(); i++){

            final TableRow row = (TableRow) table.getChildAt(i);

            for( int j = 1 ; j < row.getChildCount(); j++){

                final TextView column = (TextView) row.getChildAt(j);

                /** j-1 because you need to exclude hours in the calendar*/
                String tag = ids[i][j-1];
                column.setTag(tag);
                column.setTextColor(Color.WHITE);
                //column.setText(tag);

                final boolean isThereEvent = eventService.getEventMapper().containsKey(tag);

                if(isThereEvent){
                    Event event = eventService.getEventMapper().get(tag);
                    column.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorAccent));
                    column.setText(event.getSubject());
                }

                column.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View cell){
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        if(isThereEvent){
                            showDialogUpdateDelete(cell);
                        }else{
                            showDialogToCreateEvent(cell);
                        }
                    }
                });
            }
        }
        return rootView;
    }

    /**
     * Show dialog to create a event
     * @param cell or field to book the meeting
     */
    private void showDialogToCreateEvent(final View cell){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View dialogToBook =  inflater.inflate(R.layout.dialog_book_room,null);
        builder.setView(dialogToBook);
        final AlertDialog dialog = builder.create();

        TextView headerDialogToBook = (TextView) dialogToBook.findViewById(R.id.title_book_room);
        headerDialogToBook.setText(String.format("Book Room - %s", timeService.convertComplexToSimpleHour(cell.getTag().toString())));

        final MaterialBetterSpinner availableMinutesSpinner = (MaterialBetterSpinner)dialogToBook.findViewById(R.id.spinner_time_book_room);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(dialogToBook.getContext(), android.R.layout.simple_dropdown_item_1line, getAvailableMinutes(cell.getTag().toString()));
        availableMinutesSpinner.setAdapter(adapter);

        /**Check if the location is available to all day*/
        if( ! isToAllDay(cell.getTag().toString())){
            dialogToBook.findViewById(R.id.all_day_book_room).setEnabled(false);
        }

        /**Define the checkbox and if this is pressed, the visibility of the spinner change*/
        final CheckBox checkbox =(CheckBox) dialogToBook.findViewById(R.id.all_day_book_room);

        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox c = (CheckBox) v;
                if (c.isChecked()) {
                    availableMinutesSpinner.setVisibility(View.INVISIBLE);
                } else {
                    availableMinutesSpinner.setVisibility(View.VISIBLE);
                }
            }
        });

        Button buttonBookRoom = (Button) dialogToBook.findViewById(R.id.btn_book_room);
        buttonBookRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                Event event = new Event();
                event.setId("id");
                event.setSubject((((TextInputLayout) dialogToBook.findViewById(R.id.subject_book_room)).getEditText().getText()).toString());
                event.setLocation(new Location(FileUtil.readLocation(getActivity())));
                event.setAttendees(new ArrayList<Attendee>());
                event.setIsAllDay(false);
                event.setOrganizer(null);

                if( checkbox.isChecked()){
                    String start = timeService.cleanDate(cell.getTag().toString());
                    event.setStart(timeService.convertSimpleToComplexHour("06:00",timeService.getIntCurrentDay(cell.getTag().toString())));
                    event.setEnd(timeService.convertSimpleToComplexHour("18:00",timeService.getIntCurrentDay(cell.getTag().toString())));
                }else{
                    event.setStart(cell.getTag().toString());
                    String duration = availableMinutesSpinner.getEditableText().toString();
                    if(  duration.equalsIgnoreCase("")){
                        event.setEnd(getEndTimeFromSpinner(event.getStart() , "15 min"));
                    }else{
                        event.setEnd(getEndTimeFromSpinner(event.getStart() , availableMinutesSpinner.getEditableText().toString()));
                    }
                }
                createEvent(event);
                dialog.cancel();
            }
        });

        dialog.show();
    }
    /**
     * Method to create a event from the GUI
     * @param event event to create
     */
    private void createEvent(final Event event){
        final Thread createThread =new Thread(new Runnable() {// Thread to refresh the home fragment
            @Override
            public void run() {
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

    private void showDialogUpdateDelete(final View cell){

        final Event event = eventService.getEventMapper().get(cell.getTag().toString());
        /** Instantiate an AlertDialog.Builder with its constructor */
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        /** Get the layout inflater */
        LayoutInflater inflater = getActivity().getLayoutInflater();

        /** Indexing dialog*/
        final View dialogView =  inflater.inflate(R.layout.dialog_update_delete_room,null);
        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();

        TextView headerDialog = (TextView) dialogView.findViewById(R.id.title_book_room);
        headerDialog.setText(String.format(getString(R.string.nameMeeting)+ event.getSubject()));
        ((TextInputLayout) dialogView.findViewById(R.id.subject_update_delete)).getEditText().setText(event.getSubject());


        /** Setting Spinner */
        final MaterialBetterSpinner startDatesSpinner = (MaterialBetterSpinner)dialogView.findViewById(R.id.start_time_meeting);
        final ArrayAdapter<String> startDates = new ArrayAdapter<>(dialogView.getContext(), android.R.layout.simple_dropdown_item_1line,getAvailableTimes(event) );
        startDatesSpinner.setAdapter(startDates);
        startDatesSpinner.setText(timeService.convertComplexToSimpleHour(event.getStart()));

        /** Setting Spinner */
        final MaterialBetterSpinner spinnerEndTimes = (MaterialBetterSpinner)dialogView.findViewById(R.id.end_time_meeting);
        final ArrayAdapter<String> endTimes = new ArrayAdapter<>(dialogView.getContext(), android.R.layout.simple_dropdown_item_1line, getAvailableTimes(event));
        spinnerEndTimes.setAdapter(endTimes);
        spinnerEndTimes.setText(timeService.convertComplexToSimpleHour(event.getEnd()));


        Button updateButton = (Button) dialogView.findViewById(R.id.btn_update_meeting);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                String startDate = timeService.convertSimpleToComplexHour(startDatesSpinner.getEditableText().toString(), timeService.getIntCurrentDay(event.getStart()));
                String endDate = timeService.convertSimpleToComplexHour(spinnerEndTimes.getEditableText().toString(), timeService.getIntCurrentDay(event.getStart()));
                if(!timeService.isHigherOrEqual( startDate ,endDate)){
                    event.setSubject((((TextInputLayout) dialogView.findViewById(R.id.subject_update_delete)).getEditText().getText()).toString());
                    event.setStart(startDate);
                    event.setEnd(endDate);

                    updateEvent(event);
                    dialog.cancel();
                }else{
                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error")
                            .setContentText("End Time must be higher than Start Time")
                            .show();
                }


            }
        });

        Button buttonDeleteEvent = (Button) dialogView.findViewById(R.id.btn_delete_meeting);
        buttonDeleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();                deleteEvent(event);
                dialog.cancel();
            }
        });

        dialog.show();

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
                                            .setContentText("The meeting was not deleted")
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
     * Method to update a event
     * @param event to update
     */
    private void updateEvent(final Event event)throws NullPointerException{
        final Thread updateThread = new Thread(new Runnable() {// Thread to refresh the home fragment
            @Override
            public void run() {
                wasSuccessfulOperation = eventService.doPost(event, new URLService().getURLUpdateEvent());
            }
        });
        updateThread.start();
        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE).setTitleText("Updating...");
        pDialog.show();
        pDialog.setCancelable(false);

        new CountDownTimer(WAITING_TIME, 1000) {
            public void onTick(long millisUntilFinished) {
                try{
                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));
                }catch (NullPointerException e){
                    Log.e(TAG,"Error while was changed the color of waiting bar");
                }
            }
            public void onFinish() {
                try {
                    updateThread.join();
                } catch (InterruptedException e) {
                    Log.e(TAG, "InterruptedException joining updateThread");
                }
                if(wasSuccessfulOperation) {
                    pDialog.setTitleText("Success!")
                            .setConfirmText("OK")
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    wasSuccessfulOperation = false;

                }else{
                    pDialog.setTitleText("Error!")
                            .setContentText("The meeting was not updated")
                            .setConfirmText("OK")
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                }
                refreshFragment();
            }
        }.start();
    }

    public List<String> getAvailableTimes(Event event){
        //TODO join this function
        List<String> availableTimes = new ArrayList<>();

        for(String time : getAvailableStartTimes(event)){
            availableTimes.add(time);
        }
        String tmpTime = event.getStart();
        while(timeService.isHigherOrEqual(event.getEnd(),tmpTime)){
            availableTimes.add(timeService.convertComplexToSimpleHour(tmpTime));
            tmpTime = timeService.addMinutes(tmpTime);
        }
        for(String time : getAvailableEndTimes(event)){
            availableTimes.add(time);
        }
        return availableTimes;
    }

    public List<String> getAvailableStartTimes(Event event){
        List<String> availableStartTimes = new ArrayList<>();

        String tmp = timeService.lessMinutes(event.getStart());
        String simpleHour;
        int cnt = 0;
        while (! eventService.getEventMapper().containsKey(tmp) && cnt < 16 && !timeService.convertComplexToSimpleHour(event.getStart()).equalsIgnoreCase(timeService.getMinSimpleHour())){
            simpleHour = timeService.convertComplexToSimpleHour(tmp);
            availableStartTimes.add(0,simpleHour);
            if(simpleHour.equalsIgnoreCase(timeService.getMinSimpleHour())){
                break;
            }
            tmp = timeService.lessMinutes(tmp);
            cnt++;
        }
        availableStartTimes.add(timeService.convertComplexToSimpleHour(event.getStart()));
        return availableStartTimes;
    }

    public List<String> getAvailableEndTimes(Event event){
        List<String> availableEndTimes = new ArrayList<>();
        String simpleHour;
        int cnt = 0;
        String tmp = event.getEnd();
        while (! eventService.getEventMapper().containsKey(tmp) && cnt < 16){
            simpleHour = timeService.convertComplexToSimpleHour(tmp);
            availableEndTimes.add(timeService.convertComplexToSimpleHour(tmp));
            if(simpleHour.equalsIgnoreCase(timeService.getMaxSimpleHour())){
                break;
            }
            tmp = timeService.addMinutes(tmp);
            cnt++;
        }
        return availableEndTimes;
    }

    /**
     * Method to get time available to book a location
     *
     * @param date current date
     * @return available minutes list
     */
    public List<String> getAvailableMinutes(String date){

        List<String> availableMinutes = new ArrayList<>();
        int minutes = timeService.getMinMin();

        String limitDate = timeService.cleanDate(date);
        limitDate = timeService.addADay(limitDate);

        Date actual = timeService.convertStringToDate(date);
        Date nextDay = timeService.convertStringToDate(limitDate);

        while(!eventService.getEventMapper().containsKey(date) && actual.compareTo(nextDay) < 0) {

            availableMinutes.add(Integer.toString(minutes) + " min");
            minutes += timeService.getMinMin();
            date = timeService.addMinutes(date);
            actual = timeService.convertStringToDate(date);
        }

        return availableMinutes;
    }

    /*
    public String getStringHeaderCalendar(){
        Map<String,String> startEnd = timeService.getRangeDays();
        String start = startEnd.get("start").split("T")[0];
        String end =  startEnd.get("end").split("T")[0];
        return start +"  -  " +end;
    }
    */

    public String getEndTimeFromSpinner(String start,String minutes){
        if (minutes == null){
            return timeService.addMinutes(start);
        }
        else{
            int times = Integer.parseInt(minutes.split(" ")[0]) / timeService.getMinMin();
            return timeService.addMinutes(start,times);
        }
    }

    /**
     * Method to check is the location is available to all day
     *
     * @param date a specific time of a day
     * @return true if is available and false in other case
     */
    public boolean isToAllDay(String date){
        date = timeService.cleanDate(date);
        String nextDayInString = timeService.addADay(date);
        Date actualDayDate= timeService.convertStringToDate(date);
        Date nextDayInDate = timeService.convertStringToDate(nextDayInString);

        while( actualDayDate.compareTo(nextDayInDate) < 0 ){
            if(eventService.getEventMapper().containsKey(date)){
                return false;
            }
            date = timeService.addMinutes(date);
            actualDayDate = timeService.convertStringToDate(date);
        }
        return true;
    }

    /**
     * Method to refresh the fragment
     */
    private void refreshFragment(){

        try{
            Fragment fragment = new WeeklyCalendarFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.container_body, fragment);
            transaction.addToBackStack(null);
            transaction.commit();

        }catch ( NullPointerException e){
            Log.i(TAG,"Refresh fragment not completed");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}

