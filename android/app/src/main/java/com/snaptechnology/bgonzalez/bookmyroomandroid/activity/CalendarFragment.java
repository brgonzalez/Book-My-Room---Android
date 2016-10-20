package com.snaptechnology.bgonzalez.bookmyroomandroid.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.snaptechnology.bgonzalez.bookmyroomandroid.R;
import com.snaptechnology.bgonzalez.bookmyroomandroid.model.Attendee;
import com.snaptechnology.bgonzalez.bookmyroomandroid.model.Event;
import com.snaptechnology.bgonzalez.bookmyroomandroid.model.Location;
import com.snaptechnology.bgonzalez.bookmyroomandroid.services.EventService;
import com.snaptechnology.bgonzalez.bookmyroomandroid.services.TimeService;
import com.snaptechnology.bgonzalez.bookmyroomandroid.utils.EventHandlerUtilities;
import com.snaptechnology.bgonzalez.bookmyroomandroid.utils.UtilProperties;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class CalendarFragment extends Fragment {


    private int countToBack = 0;

    private long mLastClickTime = 0;


    private EventService eventService = EventService.getInstance(getActivity());
    private TimeService timeService = new TimeService();

    public CalendarFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        final View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "test", Toast.LENGTH_SHORT).show();

            }
        });
        TextView headerCalendar = (TextView) rootView.findViewById(R.id.week);
        headerCalendar.setText(getStringHeaderCalendar());

        eventService.getTimeService().updateDatesToCalendar();

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

                final boolean isThereEvent = eventService.getEventMapper().containsKey(tag);

                if(isThereEvent){
                    Event event = eventService.getEventMapper().get(tag);
                    column.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    column.setText(event.getSubject());
                }

                column.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View cell){
// mis-clicking prevention, using threshold of 1000 ms
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();                        if(isThereEvent){
                            showDialogUpdateDelete(cell);
                        }else{
                            showDialogToCreateEvent(cell);
                        }
                    }
                });
            }
        }

        ScrollView s = (ScrollView) rootView.findViewById(R.id.scrollView);
        s.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                EventHandlerUtilities.preventDoubleClick();
                countToBack = 0;
            }
        });

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
        headerDialogToBook.setText("Book Room - "+ timeService.convertComplexHourToSimpleHour(cell.getTag().toString()));

        final MaterialBetterSpinner availableMinutesSpinner = (MaterialBetterSpinner)dialogToBook.findViewById(R.id.spinner_time_book_room);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(dialogToBook.getContext(), android.R.layout.simple_dropdown_item_1line, getAvailableMinutes(cell.getTag().toString()));
        availableMinutesSpinner.setAdapter(adapter);

        /**Check if the location is available to all day*/
        if( ! isToAllDay(cell.getTag().toString())){
            ((CheckBox) dialogToBook.findViewById(R.id.all_day_book_room)).setEnabled(false);
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
                event.setLocation(new Location(UtilProperties.getLocationProperty(getActivity())));
                event.setAttendees(new ArrayList<Attendee>());
                event.setIsAllDay(checkbox.isChecked());

                if( checkbox.isChecked()){
                    event.setStart(timeService.resetHoursStringDate(cell.getTag().toString()));
                    event.setEnd(timeService.addADay(event.getStart()));
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
     * @param event
     */
    private void createEvent(final Event event){
        Log.e("Name", event.getSubject());
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
                    pDialog.setTitleText("Error! The meeting was not created")
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
        headerDialog.setText("Meeting:   "+ event.getSubject());
        ((TextInputLayout) dialogView.findViewById(R.id.subject_update_delete)).getEditText().setText(event.getSubject());


        /** Setting Spinner */
        final MaterialBetterSpinner startDatesSpinner = (MaterialBetterSpinner)dialogView.findViewById(R.id.start_time_meeting);
        final ArrayAdapter<String> startDates = new ArrayAdapter<String>(dialogView.getContext(), android.R.layout.simple_dropdown_item_1line, getAvailableStartTimes(event));
        startDatesSpinner.setAdapter(startDates);
        startDatesSpinner.setText(timeService.convertComplexHourToSimpleHour(event.getStart()));

        /** Setting Spinner */
        final MaterialBetterSpinner spinnerEndTimes = (MaterialBetterSpinner)dialogView.findViewById(R.id.end_time_meeting);
        final ArrayAdapter<String> endTimes = new ArrayAdapter<String>(dialogView.getContext(), android.R.layout.simple_dropdown_item_1line, getAvailableEndTimes(event));
        spinnerEndTimes.setAdapter(endTimes);
        spinnerEndTimes.setText(timeService.convertComplexHourToSimpleHour(event.getEnd()));


        Button updateButton = (Button) dialogView.findViewById(R.id.btn_update_meeting);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                EventHandlerUtilities.preventDoubleClick();
                event.setSubject((((TextInputLayout) dialogView.findViewById(R.id.subject_update_delete)).getEditText().getText()).toString());
                event.setStart(timeService.convertSimpleHourToComplexHour(startDatesSpinner.getEditableText().toString(), timeService.getIntOfActualDay(event.getStart())));
                event.setEnd(timeService.convertSimpleHourToComplexHour(spinnerEndTimes.getEditableText().toString(), timeService.getIntOfActualDay(event.getStart())));


                updateEvent(event);
                dialog.cancel();
            }
        });

        Button buttonDeleteEvent = (Button) dialogView.findViewById(R.id.btn_delete_meeting);
        buttonDeleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                EventHandlerUtilities.preventDoubleClick();
                deleteEvent(event);
                dialog.cancel();
            }
        });

        dialog.show();

    }

    /**
     * Method to delete a event from the GUI
     * @param event
     */
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

    private void updateEvent(final Event event){
        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE).setTitleText("Updating...");
        pDialog.show();
        pDialog.setCancelable(false);

        new CountDownTimer(800 * 7, 800) {
            boolean isExecuted = false;
            boolean wasUpdated = false;
            public void onTick(long millisUntilFinished) {
                try{
                    pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.blue_btn_bg_color));
                }catch (IllegalStateException e){
                    Log.e("Error","Error showing progress bar because user tried to create two event at same time");
                }
                while (isExecuted == false){
                    isExecuted = true;
                    wasUpdated = eventService.updateEvent(event);
                }
            }

            public void onFinish() {
                if(wasUpdated) {
                    pDialog.setTitleText("Success!")
                            .setConfirmText("OK")
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                }else{
                    pDialog.setTitleText("Error! The meeting was not updated")
                            .setConfirmText("OK")
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                }
                refreshFragment();

            }
        }.start();

    }

    public List<String> getAvailableStartTimes(Event event){
        List<String> availableStartTimes = new ArrayList<>();
        //availableStartTimes.add(timeService.convertComplexHourToSimpleHour(event.getStart()));

        String tmp = timeService.lessMinutes(event.getStart());
        String simpleHour;
        int cnt = 0;
        while (! eventService.getEventMapper().containsKey(tmp) && cnt < 8 && !timeService.convertComplexHourToSimpleHour(event.getStart()).equalsIgnoreCase(timeService.getMinSimpleHour())){
            simpleHour = timeService.convertComplexHourToSimpleHour(tmp);
            availableStartTimes.add(0,simpleHour);
            if(simpleHour.equalsIgnoreCase(timeService.getMinSimpleHour())){
                break;
            }
            tmp = timeService.lessMinutes(tmp);
            cnt++;
        }
        availableStartTimes.add(timeService.convertComplexHourToSimpleHour(event.getStart()));
        return availableStartTimes;
    }

    public List<String> getAvailableEndTimes(Event event){
        List<String> availableEndTimes = new ArrayList<>();
        String simpleHour;
        int cnt = 0;
        String tmp = event.getEnd();
        while (! eventService.getEventMapper().containsKey(tmp) && cnt < 8){
            simpleHour = timeService.convertComplexHourToSimpleHour(tmp);
            availableEndTimes.add(timeService.convertComplexHourToSimpleHour(tmp));
            if(simpleHour.equalsIgnoreCase(timeService.getMaxSimpleHour())){
                break;
            }
            tmp = timeService.addMinutes(tmp);
            cnt++;
        }
        return availableEndTimes;
    }


    public List<String> getAvailableMinutes(String date){

        List<String> availableMinutes = new ArrayList<>();
        int minutes = timeService.getMinMin();

        String limitDate = timeService.resetHoursStringDate(date);
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

    public String getStringHeaderCalendar(){
        Map<String,String> startEnd = timeService.getRangeToRequest();
        String start = startEnd.get("start").split("T")[0];
        String end =  startEnd.get("end").split("T")[0];
        String header =  start +"  -  " +end;
        return header;
    }

    public String getEndTimeFromSpinner(String start,String minutes){

        if (minutes == null){
            return timeService.addMinutes(start);
        }
        else{
            int times = Integer.parseInt(minutes.split(" ")[0]) / timeService.getMinMin();
            return timeService.addMinutes(start,times);
        }
    }

    public boolean isToAllDay(String date){

        date = timeService.resetHoursStringDate(date);
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
            Fragment fragment = new CalendarFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commitAllowingStateLoss();
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


    private class CreaterEvents extends AsyncTask<Void, Void, Void> {

        Event event;

        public CreaterEvents(Event event){
            super();
            this.event = event;
        }


        @Override
        protected Void doInBackground(Void... params) {

            eventService.createEvent(event);


            Fragment fragment = new CalendarFragment();
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

    private class UpdaterEvents extends AsyncTask<Void, Void, Void> {

        Event event;

        public UpdaterEvents(Event event){
            super();
            this.event = event;
        }


        @Override
        protected Void doInBackground(Void... params) {

            eventService.updateEvent(event);


            Fragment fragment = new CalendarFragment();
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

    private class DeleterEvents extends AsyncTask<Void, Void, Void> {

        Event event;

        public DeleterEvents(Event event){
            super();
            this.event = event;
        }
        @Override
        protected Void doInBackground(Void... params) {
            eventService.deleteEvent(event);
            Fragment fragment = new CalendarFragment();
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
}

