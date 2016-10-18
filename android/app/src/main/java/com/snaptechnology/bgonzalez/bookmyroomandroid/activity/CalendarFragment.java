package com.snaptechnology.bgonzalez.bookmyroomandroid.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.snaptechnology.bgonzalez.bookmyroomandroid.utils.UtilProperties;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class CalendarFragment extends Fragment {


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        /**Set header of calendar*/
        TextView week = (TextView) rootView.findViewById(R.id.week);
        week.setText(getStringHeaderCalendar());

        /**Update dates according dates of week*/
        eventService.getTimeService().updateDatesToCalendar();

        /**Store date to assign to each cell(TextView) of */
        String[][] ids = eventService.getTimeService().getDates();

        final TableLayout table = (TableLayout) rootView.findViewById(R.id.table_calendar);
        for(int i = 0 ; i < table.getChildCount(); i++){

            final TableRow row = (TableRow) table.getChildAt(i);
            for( int j = 1 ; j < row.getChildCount(); j++){

                final TextView column = (TextView) row.getChildAt(j);

                /**-1 because you need to exclude the days and hours in the calendar*/
                String tag = ids[i][j-1];
                column.setTag(tag);

                final boolean isThereEvent =eventService.getEventMapper().containsKey(tag);

                if(isThereEvent){
                    Event event = eventService.getEventMapper().get(tag);
                    column.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    column.setText(event.getSubject());
                }

                column.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View cell){

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

    private void showDialogToCreateEvent(final View cell){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView =  inflater.inflate(R.layout.dialog_book_room,null);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

        TextView titleBookRoom = (TextView) dialogView.findViewById(R.id.title_book_room);
        titleBookRoom.setText("Book Room - "+ timeService.convertComplexHourToSimpleHour(cell.getTag().toString()));

        /** Setting Spinner */
        final MaterialBetterSpinner spinnerBook = (MaterialBetterSpinner)dialogView.findViewById(R.id.spinner_time_book_room);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(dialogView.getContext(), android.R.layout.simple_dropdown_item_1line, getAvailableMinutes(cell.getTag().toString()));
        spinnerBook.setAdapter(adapter);

        /**Check if the location is available to all day*/
        if( ! isToAllDay(cell.getTag().toString())){
            ((CheckBox) dialogView.findViewById(R.id.all_day_book_room)).setEnabled(false);
        }

        /**Define the checkbox and if this is pressed, the visibility of the spinner change*/
        final CheckBox checkbox =(CheckBox) dialogView.findViewById(R.id.all_day_book_room);

        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox c = (CheckBox) v;
                if (c.isChecked()) {
                    spinnerBook.setVisibility(View.INVISIBLE);
                } else {
                    spinnerBook.setVisibility(View.VISIBLE);
                }
            }
        });

        Button buttonBookRoom = (Button) dialogView.findViewById(R.id.btn_book_room);
        buttonBookRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                Event event = new Event();
                event.setId("id");
                event.setSubject((((TextInputLayout) dialogView.findViewById(R.id.subject_book_room)).getEditText().getText()).toString());
                event.setLocation(new Location(UtilProperties.getLocationProperty(getActivity())));
                event.setAttendees(new ArrayList<Attendee>());
                event.setIsAllDay(checkbox.isChecked());

                if( checkbox.isChecked()){
                    event.setStart(timeService.resetHoursStringDate(cell.getTag().toString()));
                    event.setEnd(timeService.addADay(event.getStart()));
                }else{
                    event.setStart(cell.getTag().toString());
                    String duration = spinnerBook.getEditableText().toString();
                    if(  duration.equalsIgnoreCase("")){
                        event.setEnd(getEndTimeFromSpinner(event.getStart() , "15 min"));
                    }else{
                        event.setEnd(getEndTimeFromSpinner(event.getStart() , spinnerBook.getEditableText().toString()));
                    }
                }
                new CreaterEvents(event).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                dialog.cancel();

                Toast.makeText(getActivity(), "Meeting added, wait a moment to see it ", Toast.LENGTH_LONG).show();
            }
        });

        dialog.show();
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

        TextView titleBookRoom = (TextView) dialogView.findViewById(R.id.title_book_room);
        titleBookRoom.setText("Meeting:   "+ event.getSubject());
        ((TextInputLayout) dialogView.findViewById(R.id.subject_update_delete)).getEditText().setText(event.getSubject());


        /** Setting Spinner */
        final MaterialBetterSpinner spinnerStartTimes = (MaterialBetterSpinner)dialogView.findViewById(R.id.start_time_meeting);
        final ArrayAdapter<String> startTimes = new ArrayAdapter<String>(dialogView.getContext(), android.R.layout.simple_dropdown_item_1line, getAvailableStartTimes(event));
        spinnerStartTimes.setAdapter(startTimes);
        spinnerStartTimes.setText(timeService.convertComplexHourToSimpleHour(event.getStart()));

        /** Setting Spinner */
        final MaterialBetterSpinner spinnerEndTimes = (MaterialBetterSpinner)dialogView.findViewById(R.id.end_time_meeting);
        final ArrayAdapter<String> endTimes = new ArrayAdapter<String>(dialogView.getContext(), android.R.layout.simple_dropdown_item_1line, getAvailableEndTimes(event));
        spinnerEndTimes.setAdapter(endTimes);
        spinnerEndTimes.setText(timeService.convertComplexHourToSimpleHour(event.getEnd()));


        Button buttonUpdateEvent = (Button) dialogView.findViewById(R.id.btn_update_meeting);
        buttonUpdateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                event.setSubject((((TextInputLayout) dialogView.findViewById(R.id.subject_update_delete)).getEditText().getText()).toString());
                event.setStart(timeService.convertSimpleHourToComplexHour(spinnerStartTimes.getEditableText().toString(), timeService.getIntOfActualDay(event.getStart())));
                event.setEnd(timeService.convertSimpleHourToComplexHour(spinnerEndTimes.getEditableText().toString(), timeService.getIntOfActualDay(event.getStart())));

                new UpdaterEvents(event).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                dialog.cancel();

                Toast.makeText(getActivity(), "Updating Meeting, wait a moment to see it ", Toast.LENGTH_LONG).show();
            }
        });

        Button buttonDeleteEvent = (Button) dialogView.findViewById(R.id.btn_delete_meeting);
        buttonDeleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {

                new DeleterEvents(eventService.getEventMapper().get(cell.getTag())).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                dialog.cancel();

                Toast.makeText(getActivity(), "Delete Meeting, wait a moment ", Toast.LENGTH_LONG).show();
            }
        });

        dialog.show();

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

