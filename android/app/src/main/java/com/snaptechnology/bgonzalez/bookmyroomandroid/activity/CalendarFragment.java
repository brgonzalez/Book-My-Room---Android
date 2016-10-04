package com.snaptechnology.bgonzalez.bookmyroomandroid.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snaptechnology.bgonzalez.bookmyroomandroid.R;
import com.snaptechnology.bgonzalez.bookmyroomandroid.model.Attendee;
import com.snaptechnology.bgonzalez.bookmyroomandroid.model.Event;
import com.snaptechnology.bgonzalez.bookmyroomandroid.model.Location;
import com.snaptechnology.bgonzalez.bookmyroomandroid.services.EventService;
import com.snaptechnology.bgonzalez.bookmyroomandroid.services.TimeService;
import com.snaptechnology.bgonzalez.bookmyroomandroid.utils.UtilProperties;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class CalendarFragment extends Fragment {

    MaterialBetterSpinner materialBetterSpinner ;

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

        for(int i = 1 ; i < table.getChildCount(); i++){

            final TableRow row = (TableRow) table.getChildAt(i);

            for( int j = 1 ; j < row.getChildCount(); j++){

                final TextView column = (TextView) row.getChildAt(j);

                /**-1 because you need to exclude the days and hours in the calendar*/
                String tag = ids[i-1][j-1];

                column.setTag(tag);

                column.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v){

                        /** Instantiate an AlertDialog.Builder with its constructor */
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                        /** Get the layout inflater */
                        LayoutInflater inflater = getActivity().getLayoutInflater();

                        /** Indexing dialog*/
                        final View dialogView =  inflater.inflate(R.layout.dialog_book_room,null);
                        builder.setView(dialogView);

                        final AlertDialog dialog = builder.create();

                        EditText editText = (EditText) dialogView.findViewById(R.id.edit_meeting_name_book_room);

                        //editText.setInputType(InputType.TYPE_NULL);


                        /** Setting Spinner */
                        final MaterialBetterSpinner spinnerBook = (MaterialBetterSpinner)dialogView.findViewById(R.id.spinner_time_book_room);
                        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(dialogView.getContext(), android.R.layout.simple_dropdown_item_1line, getAvailableMinutes(v.getTag().toString()));
                        spinnerBook.setAdapter(adapter);

                        /**Check if the location is available to all day*/
                        if( ! isToAllDay(v.getTag().toString())){
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
                        spinnerBook.setText(adapter.getItem(0));
                        Button buttonBookRoom = (Button) dialogView.findViewById(R.id.btn_book_room);
                        buttonBookRoom.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v2) {
                                final String id = "id";
                                final String subject = (((TextInputLayout) dialogView.findViewById(R.id.subject_book_room)).getEditText().getText()).toString();
                                final String start;
                                final String end;
                                final Boolean isAllDay = checkbox.isChecked();

                                if (isAllDay){
                                    start = timeService.resetHoursOfDate(column.getTag().toString());
                                    end = timeService.addADay(start);
                                }
                                else{
                                    start = column.getTag().toString();
                                    String duration = spinnerBook.getEditableText().toString();
                                    if(  duration.equalsIgnoreCase("")){
                                        end = getEndTimeFromSpinner(start , "30 min");
                                    }else{
                                        end = getEndTimeFromSpinner(start , spinnerBook.getEditableText().toString());
                                    }
                                }

                                Event event = null;
                                try {
                                    event = new Event(id, subject, new Location(new UtilProperties().getLocationProperty(getActivity())),new ArrayList<Attendee>(), isAllDay,  start,  end);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                new CreaterEvents(event).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                Toast.makeText(getActivity(), "Meeting added, wait a moment to see it ", Toast.LENGTH_LONG).show();
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                dialog.cancel();
                            }
                        });

                        dialog.show();
                    }
                });
            }


        }

        List<Event> eventsFromServer = eventService.getEvent();

        for(Event e : eventsFromServer){
            String date = e.getStart();
            TextView test;

            while( isGreaterDate(date, e.getEnd()) == false){
                test = (TextView) rootView.findViewWithTag(date);
                if( test != null){
                    test.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    test.setText(e.getSubject());
                }
                date = timeService.addMinutes(date);
            }
        }

        return rootView;
    }

    public boolean isGreaterDate(String stringDate, String stringEndDate){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        Date date = null;
        Date endDate = null;
        try {
            date =  dateFormat.parse(stringDate);
            endDate =  dateFormat.parse(stringEndDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (date.compareTo(endDate) < 0 )? false : true;
    }

    public List<String> getAvailableMinutes(String date){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        List<String> availableMinutes = new ArrayList<>();
        int minutes = 30;

        String limitDate = timeService.resetHoursOfDate(date);
        limitDate = timeService.addADay(limitDate);

        Date actual;
        Date nextDay;

        try {
            actual= df.parse(date);
            nextDay = df.parse(limitDate);

            while(!eventService.getEventMapper().containsKey(date) && actual.compareTo(nextDay) < 0){

                availableMinutes.add(Integer.toString(minutes)+ " min");
                minutes+=30;
                date = timeService.addMinutes(date);
                actual = df.parse(date);

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return availableMinutes;
    }

    public String getStringHeaderCalendar(){
        Map<String,String> startEnd = timeService.getStartEndCurrentWeek();
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
            int times = Integer.parseInt(minutes.split(" ")[0]) / 30;
            return timeService.addMinutes(start,times);
        }
    }

    public boolean isToAllDay(String date){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        date = timeService.resetHoursOfDate(date);
        String nextDay = timeService.addADay(date);

        Date actualDayDate;
        Date nextDayDate;
        try {
            actualDayDate= df.parse(date);
            nextDayDate = df.parse(nextDay);
            System.out.println(eventService.getEventMapper().toString());
            while( actualDayDate.compareTo(nextDayDate) < 0 ){
                Log.e("Compare", date +"------>"+ nextDay);
                if(eventService.getEventMapper().containsKey(date)){
                    return false;
                }
                date = timeService.addMinutes(date);
                actualDayDate = df.parse(date);

            }
        } catch (ParseException e) {
            e.printStackTrace();
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

    private class UpdaterCalendar extends AsyncTask<Void, Void, Void> {




        @Override
        protected Void doInBackground(Void... params) {

            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

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

    public static void main(String[] args){

    }
}

