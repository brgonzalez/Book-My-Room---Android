package com.snaptechnology.bgonzalez.bookmyroomandroid.services;

import android.provider.Settings;
import android.util.Log;
import android.util.Pair;

import com.snaptechnology.bgonzalez.bookmyroomandroid.activity.CalendarFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bgonzalez on 14/09/2016.
 */
public class TimeService {

    private int timeZone = 6;

    private static String[][] dates = new String[24][6];

    private int intWeek = 0;

    public TimeService(){

    }

    public Map<String,String> getStartEndCurrentWeek(){
        Map<String,String> startAndEndDateWeek = new HashMap<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        cal.add(Calendar.DAY_OF_WEEK,1);
        cal.add(Calendar.HOUR, 6 + timeZone);
        String startDate = dateFormat.format(cal.getTime());

        cal.add(Calendar.DAY_OF_YEAR, 4);
        cal.add(Calendar.HOUR,12);

        String endDate = dateFormat.format(cal.getTime());

        startAndEndDateWeek.put("start",startDate +"Z");
        startAndEndDateWeek.put("end", endDate +"Z");

        return startAndEndDateWeek;
    }

    public String getInitialID(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);


        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        cal.add(Calendar.DAY_OF_WEEK,1);
        cal.add(Calendar.HOUR,  6 + timeZone);
        String initialID = dateFormat.format(cal.getTime());
        return initialID +"Z";
    }

    public String changeToNextDay(String date){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date d = null;
        try {
            d = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("Error", "Error parsing date");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.set(Calendar.HOUR,0);
        //cal.add(Calendar.DAY_OF_WEEK, 1);
        cal.add(Calendar.HOUR, 6 + timeZone );

        String newTime = df.format(cal.getTime());
        return newTime +"Z";
    }

    public String addMinutes(String date){
        Date d = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        try {
            d = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("Error", "Error parsing date");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.MINUTE, 30);
        String newTime = df.format(cal.getTime());
        return newTime +"Z";
    }

    public String addMinutes(String date, int times){
        for(int i = 0 ; i < times; i++){
            date = addMinutes(date);
        }
        return date;
    }

    public String rest15MinutesToDate(String date){
        Date d = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        try {
            d = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("Error", "Error parsing date");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.MINUTE, -15);
        String newTime = df.format(cal.getTime());
        return newTime +"Z";
    }

    public String[][] updateDatesToCalendar(){
        String[][] datesTemp = new String[24][6];

        String date = getInitialID();


        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date temp = null;
        try {
            temp = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(temp);
        int tempWeek =cal.get(Calendar.WEEK_OF_YEAR);
        if ( tempWeek != intWeek){
            for(int j = 0; j < 5; j++){
                for(int i = 0; i < 24; i++){
                    datesTemp[i][j] = date ;
                    date = addMinutes(date);
                }
                date = changeToNextDay(date);
            }
            setDates(datesTemp);

        }
        return dates;
    }

    public String resetHoursOfDate(String date){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date d = null;
        try {
            d = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("Error", "Error parsing date");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.set(Calendar.HOUR,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.HOUR_OF_DAY,0);

        String newTime = df.format(cal.getTime());
        return newTime+"Z";
    }

    public String addADay(String date){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date d = null;
        try {
            d = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("Error", "Error parsing date");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.DAY_OF_WEEK, 1);

        String newTime = df.format(cal.getTime());
        return newTime+"Z";
    }

    public boolean isGreaterDate(String stringDate, String stringEndDate) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        Date date = null;
        Date endDate = null;
        try {
            date = dateFormat.parse(stringDate);
            endDate = dateFormat.parse(stringEndDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (date.compareTo(endDate) < 0) ? false : true;

    }

    public synchronized String[][] getDates() {
        return dates;
    }

    public synchronized void setDates(String[][] dates) {
        this.dates = dates;
    }

    public static void main(String[] args){
        TimeService timeService = new TimeService();
        //System.out.println(timeService.getStartEndCurrentWeek().get("end"));




    }
}
