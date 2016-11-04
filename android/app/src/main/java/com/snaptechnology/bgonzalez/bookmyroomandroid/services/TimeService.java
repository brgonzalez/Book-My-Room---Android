package com.snaptechnology.bgonzalez.bookmyroomandroid.services;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * TimeService is a service to make different functions about dates
 * @author Brayan González
 * @since 26/09/2016
 */
public class TimeService {
    
    private int timeZone = 6;
    private static String[][] dates = new String[24][6];
    private static int INT_WEEK = 0;
    private final int minMin = 15;
    private static final int SCHEDULE_ROWS = 40;
    private static final int SCHEDULE_COLUMNS = 6;
    private static final int AMOUNT_HOURS_DAY = 10;
    private static final int SCHEDULE_START_TIME = 7;
    private String minSimpleHour = "07:00";
    private String maxSimpleHour = "17:00";

    public TimeService(){

    }

    /**
      Get the time range to request body
     *
     * @return map with startDate and End Date
     */
    public Map<String,String> getRangeDays(){
        Map<String,String> startAndEndDateWeek = new HashMap<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(convertStringToDate(getInitialID()));
        String startDate = convertDateToString(cal.getTime());
        cal.add(Calendar.DAY_OF_YEAR, 5);
        cal.add(Calendar.HOUR, AMOUNT_HOURS_DAY);
        String endDate = convertDateToString(cal.getTime());
        startAndEndDateWeek.put("start",startDate);
        startAndEndDateWeek.put("end", endDate);
        return startAndEndDateWeek;
    }

    /**
     * Get the initial ID to the Weekly Calendar Table
     * @return initial ID
     */
    private String getInitialID(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); //
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        cal.add(Calendar.HOUR,  SCHEDULE_START_TIME + timeZone);
        return convertDateToString(cal.getTime());
    }

    /**
     * Get the first Hour according to the weekly calendar
     * @return first hour
     */
    public String getInitialTimeDay(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); //
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.add(Calendar.HOUR,  SCHEDULE_START_TIME + timeZone);
        return  convertDateToString(cal.getTime());
    }

    /**
     * Change a Date to the next day and reset the hours to the first hour according to weekly calendar
     * @param dateInString to be changed
     * @return date changed
     */
    private String getFirstHourNextDat(String dateInString){
        Date date = convertStringToDate(dateInString);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.add(Calendar.DAY_OF_WEEK,1);
        cal.add(Calendar.HOUR, SCHEDULE_START_TIME + timeZone );
        return convertDateToString(cal.getTime());
    }

    /**
     * Add minutes to Date according of the minutes of the weekly calendar
     * @param dateInString whatever date
     * @return Date with minutes added
     */
    public String addMinutes(String dateInString){
        Date date = convertStringToDate(dateInString);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minMin);
        return convertDateToString(cal.getTime());
    }

    /**
     * Add minutes n time to Date according of the minutes of the weekly calendar
     * @param date some Date
     * @param n quantity to add minutes
     * @return Date with minutes added
     */
    public String addMinutes(String date, int n){
        for(int i = 0 ; i < n; i++){
            date = addMinutes(date);
        }
        return date;
    }

    /**
     * Redice minutes to Date according of the weekly calendar
     * @param dateInString whatever date
     * @return Date with minutes reduced
     */
    public String lessMinutes(String dateInString){
        Date date = convertStringToDate(dateInString);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, -minMin);
        return  convertDateToString(cal.getTime());
    }

    /** Todo : Find a format Date to eliminate this method
     * Customize date (the date needs to finish with letter Z)
     * @param dateInString some Date
     * @return Date customized
     */
    private String customizeDate(String dateInString){
        return dateInString+"Z";
    }


    public String[][] updateIdsWeeklyCalendar(){
        String[][] datesTemp = new String[SCHEDULE_ROWS][SCHEDULE_COLUMNS];
        String date = getInitialID();
        Date temp = convertStringToDate(getInitialID());
        Calendar cal = Calendar.getInstance();
        cal.setTime(temp);
        int tempWeek =cal.get(Calendar.WEEK_OF_YEAR);
        if ( tempWeek != INT_WEEK){ //just if the week change
            INT_WEEK = tempWeek;
            for(int j = 0; j < SCHEDULE_COLUMNS; j++){
                for(int i = 0; i < SCHEDULE_ROWS; i++){
                    datesTemp[i][j] = date ;
                    date = addMinutes(date);
                }
                date = getFirstHourNextDat(date);
            }
            setDates(datesTemp);
        }
        return dates;
    }

    /**
     * Reset hour of a Date in String
     * @param dateInString some Date in String
     * @return Date cleaned
     */
    public String cleanDate(String dateInString){
        Date date = convertStringToDate(dateInString);
        date = cleanDate(date);
        return convertDateToString(date);
    }
    /**
     * Reset hour of a Date i
     * @param date some Date
     * @return Date cleaned
     */
    private Date cleanDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        return cal.getTime();
    }

    /**
     * Add a day to a Date
     * @param dateInString some Date
     * @return date in string modified
     */
    public String addADay(String dateInString){
        Date date = convertStringToDate(dateInString);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_WEEK, 1);
        return convertDateToString(cal.getTime());
    }

    /**
     * Method to compare 2 Dates in String
     * @param stringDate some date
     * @param stringEndDate some date
     * @return true if the fist date is higher or equal to the second day
     */
    public boolean isHigherOrEqual(String stringDate, String stringEndDate) {
        Date date =convertStringToDate(stringDate);
        Date endDate = convertStringToDate(stringEndDate);
        return (date.compareTo(endDate) >= 0) ;
    }

    /**
     * Get the difference of two dates
     * @param lowerDate some date
     * @param higherDate some date
     * @return String with the difference in text
     */
    public String getDifferenceInText(String lowerDate, String higherDate){

        if(higherDate == null){
            return "Next Week";
        }

        Date dateless = convertStringToDate(lowerDate);
        Date dateHigher = convertStringToDate(higherDate);

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
            @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("EEE, HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateHigher);
            cal.add(Calendar.HOUR_OF_DAY, - timeZone);
            return df.format(cal.getTime());
        }
    }

    /**
     * Get the difference between two dates
     * @param startDateInString some date
     * @param endDateInString some date
     * @return difference
     */
    public long getDifference(String startDateInString, String endDateInString){
        Date startDate = convertStringToDate(startDateInString);
        Date endDate = convertStringToDate(endDateInString);
        long diff = endDate.getTime() - startDate.getTime();
        return diff / 1000;
    }

    /**
     * Convert a date in String to a Object Date
     * @param dateInString some date
     * @return Object Date
     */
    public Date convertStringToDate(String dateInString){
        @SuppressLint("SimpleDateFormat")
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

    /**
     * Convert a Object Date to a String Date
     * @param date some date
     * @return  date in string
     */
    private String convertDateToString(Date date){
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return customizeDate(dateFormat.format(date));
    }

    /**
     * Round up a date in String to the next block of minutes according to the weekly calendar
     * @param dateInString some date
     * @return date modified
     */
    public String roundUp(String dateInString){
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

    /**
     * Round down a date in String to the next block of minutes according to the weekly calendar
     * @param dateInString some date
     * @return date modified
     */
    public String roundDown(String dateInString){
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

    /**
     * Get the current time
     * @return current date
     */
    private Date getTime(){
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, timeZone );
        return  calendar.getTime();
    }

    /**
     * Get the current time in string
     * @return current date ins tring
     */
    public String getTimeInString(){
        return convertDateToString(getTime());
    }


    /**
     * Get the int of a day from a Object Date according Calendar
     * @param dateInString some date
     * @return the int day
     */
    public int getIntCurrentDay(String dateInString){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(convertStringToDate(dateInString));
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * Convert a simple hour (07:00) to complex hour according to the date format used
     * @param simpleHour simple hour
     * @param day int of a day calculate with the method getIntCurrentDay()
     * @return complex hour
     */
    public String convertSimpleToComplexHour(String simpleHour, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( cleanDate(calendar.getTime() ));
        calendar.set(Calendar.DAY_OF_WEEK, day);
        String[] values = simpleHour.split(":");
        calendar.add(Calendar.HOUR_OF_DAY, timeZone + Integer.parseInt(values[0]));
        calendar.add(Calendar.MINUTE, Integer.parseInt(values[1]));
        return convertDateToString(calendar.getTime());
    }

    /**
     * Convert a complex hour according to the date format used to simple hour (07:00)
     * @param complexHour complex hour
     * @return complex hour
     */
    public String convertComplexToSimpleHour(String complexHour){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( convertStringToDate(complexHour) );
        calendar.add(Calendar.HOUR_OF_DAY, - timeZone );
        String tmp = convertDateToString(calendar.getTime());
        String[] values = tmp.split("T")[1].split(":");
        return values[0]+":"+values[1];
    }

    private synchronized void setDates(String[][] dates) {
        TimeService.dates = dates;
    }

    public int getMinMin() {
        return minMin;
    }

    public String getMaxSimpleHour() {
        return maxSimpleHour;
    }

    public String getMinSimpleHour() {
        return minSimpleHour;
    }

    public synchronized String[][] getDates() {
        return dates;
    }






}
