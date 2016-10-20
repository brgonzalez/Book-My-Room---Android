package com.snaptechnology.bgonzalez.bookmyroomandroid.services;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bgonzalez on 14/09/2016.
 */
public class TimeService {


    private int timeZone = 6;

    private static String[][] dates = new String[24][6];

    private int intWeek = 0;

    private int minMin = 15;

    private int scheduleRows = 48;
    private int scheduleColumns = 6;

    private final int startTime = 6;
    private String minSimpleHour = "06:00";
    private String maxSimpleHour = "18:00";

    public TimeService(){

    }

    public Map<String,String> getRangeToRequest(){
        Map<String,String> startAndEndDateWeek = new HashMap<>();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); //
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        cal.add(Calendar.DAY_OF_WEEK,1);
        cal.add(Calendar.HOUR, 6 + timeZone);
        String startDate = convertDateToString(cal.getTime());

        cal.add(Calendar.DAY_OF_YEAR, 5);
        cal.add(Calendar.HOUR,12);

        String endDate = convertDateToString(cal.getTime());

        startAndEndDateWeek.put("start",startDate);
        startAndEndDateWeek.put("end", endDate);

        return startAndEndDateWeek;
    }


    public String getInitialID(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); //
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        cal.add(Calendar.DAY_OF_WEEK,1);
        cal.add(Calendar.HOUR,  startTime + timeZone);
        String initialID = convertDateToString(cal.getTime());
        return initialID;
    }

    public String getInitialTimeDay(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); //
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        cal.add(Calendar.HOUR,  startTime + timeZone);
        String initialID = convertDateToString(cal.getTime());
        return initialID;
    }

    public String changeToNextDay(String dateInString){
        Date date = convertStringToDate(dateInString);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR,0);
        cal.add(Calendar.HOUR, 6 + timeZone );

        String output = convertDateToString(cal.getTime());
        return output;
    }

    public String addMinutes(String dateInString){
        Date date = convertStringToDate(dateInString);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minMin);
        String output = convertDateToString(cal.getTime());
        return output;
    }


    public String lessMinutes(String dateInString){
        Date date = convertStringToDate(dateInString);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, -minMin);
        String output = convertDateToString(cal.getTime());
        return output;
    }

    public String customizeDate(String dateInString){
        return dateInString+"Z";

    }

    public String addMinutes(String date, int times){
        for(int i = 0 ; i < times; i++){
            date = addMinutes(date);
        }
        return date;
    }



    public String[][] updateDatesToCalendar(){
        String[][] datesTemp = new String[scheduleRows][scheduleColumns];

        String date = getInitialID();
        Date temp = convertStringToDate(getInitialID());

        Calendar cal = Calendar.getInstance();
        cal.setTime(temp);
        int tempWeek =cal.get(Calendar.WEEK_OF_YEAR);
        if ( tempWeek != intWeek){
            for(int j = 0; j < scheduleColumns; j++){
                for(int i = 0; i < scheduleRows; i++){
                    datesTemp[i][j] = date ;
                    date = addMinutes(date);
                }
                date = changeToNextDay(date);
            }
            setDates(datesTemp);

        }
        return dates;
    }

    public String resetHoursStringDate(String dateInString){
        Date date = convertStringToDate(dateInString);
        date = resetHoursDate(date);
        String output = convertDateToString(date);
        return output;
    }

    public Date resetHoursDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        return cal.getTime();
    }

    public String addADay(String dateInString){
        Date date = convertStringToDate(dateInString);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_WEEK, 1);

        String output = convertDateToString(cal.getTime());
        return output;

    }

    public boolean isGreaterDate(String stringDate, String stringEndDate) {
        Date date =convertStringToDate(stringDate);
        Date endDate = convertStringToDate(stringEndDate);

        return (date.compareTo(endDate) < 0) ? false : true;

    }

    public synchronized String[][] getDates() {
        return dates;
    }

    public String calculateDifferenceInString(String dateLessInString,String dateHigherInString){

        if(dateHigherInString == null){
            return "Next Week";
        }

        Date dateless = convertStringToDate(dateLessInString);
        Date dateHigher = convertStringToDate(dateHigherInString);

        long diff = dateHigher.getTime() - dateless.getTime();
        long diffSeconds = diff / 1000;
        long diffMinutes = diff / (60 * 1000) ;
        long diffHours = diff / (60 * 60 * 1000) ;

        if (diffSeconds < 60){
            return "less than a minute";
        }
        else if (diffMinutes < 60){
            return String.valueOf(diffMinutes)+ " min";
        }
        else if (diffHours < 6){
            return String.valueOf(diffHours) +" hours  "+ String.valueOf(diffMinutes % 60 )+ " min";
        }
        else{
            DateFormat df = new SimpleDateFormat("EEE, HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateHigher);
            cal.add(Calendar.HOUR_OF_DAY, - timeZone);
            return df.format(cal.getTime());
        }
    }

    public long calculateDifferenceDates(String startDateInString, String endDateInString){
        Date startDate = convertStringToDate(startDateInString);
        Date endDate = convertStringToDate(endDateInString);
        long diff = endDate.getTime() - startDate.getTime();
        long diffSeconds = diff / 1000;
        return diffSeconds;
    }

    public Date convertStringToDate(String dateInString){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;

        try {
            date = dateFormat.parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("Parsing date","Error while parsing date from TimeService");
        }
        return date;
    }

    public String convertDateToString(Date date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return customizeDate(dateFormat.format(date));
    }

    public String roundDateToHigherInString(String dateInString){
        Date date = convertStringToDate(dateInString);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int minutes = calendar.get(Calendar.MINUTE);
        int mod = minutes % minMin;
        calendar.add(Calendar.MINUTE, (minutes != 0 && minutes != minMin) ? minMin -mod : 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return convertDateToString(calendar.getTime());
    }

    public String roundDateToLessInString(String dateInString){
        Date date = convertStringToDate(dateInString);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int minutes = calendar.get(Calendar.MINUTE);
        int mod = minutes % minMin;
        calendar.add(Calendar.MINUTE, -mod);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return convertDateToString(calendar.getTime());
    }

    public Date getActualTime(){
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, timeZone );
        return  calendar.getTime();
    }

    public int getIntOfActualDay(String dateInString){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(convertStringToDate(dateInString));
        return calendar.get(Calendar.DAY_OF_WEEK);
    }


    public String convertSimpleHourToComplexHour(String simpleHour,int day){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( resetHoursDate(calendar.getTime() ));
        calendar.set(Calendar.DAY_OF_WEEK, day);

        String[] values = simpleHour.split(":");
        calendar.add(Calendar.HOUR_OF_DAY, timeZone + Integer.parseInt(values[0]));
        calendar.add(Calendar.MINUTE, Integer.parseInt(values[1]));

        return convertDateToString(calendar.getTime());
    }

    public String convertComplexHourToSimpleHour(String complexHour){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( convertStringToDate(complexHour) );

        calendar.add(Calendar.HOUR_OF_DAY, - timeZone );

        String tmp = convertDateToString(calendar.getTime());
        String[] values = tmp.split("T")[1].split(":");
        return values[0]+":"+values[1];
    }

    public String getActualTimeInString(){
        return convertDateToString(getActualTime());
    }

    public synchronized void setDates(String[][] dates) {
        this.dates = dates;
    }

    public int getMinMin() {
        return minMin;
    }

    public String getMaxSimpleHour() {
        return maxSimpleHour;
    }

    public void setMaxSimpleHour(String maxSimpleHour) {
        this.maxSimpleHour = maxSimpleHour;
    }

    public String getMinSimpleHour() {
        return minSimpleHour;
    }

    public void setMinSimpleHour(String minSimpleHour) {
        this.minSimpleHour = minSimpleHour;
    }


    public int getTimeZone() {
        return timeZone;
    }



}
