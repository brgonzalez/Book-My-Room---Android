package com.snaptechnology.bgonzalez.bookmyroomandroid.services;

import android.util.Pair;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bgonzalez on 14/09/2016.
 */
public class TimeService {

    public Map<String,String> getStartEndCurrrentWeek(){
        Map<String,String> startAndEndDateWeek = new HashMap<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        cal.add(Calendar.HOUR, 6);
        String startDate = dateFormat.format(cal.getTime());

        cal.add(Calendar.DAY_OF_YEAR, 4);
        cal.add(Calendar.HOUR,12);

        String endDate = dateFormat.format(cal.getTime());

        startAndEndDateWeek.put("start",startDate + ".Z");
        startAndEndDateWeek.put("end", endDate + ".Z");

        return startAndEndDateWeek;
    }

    public static void main(String[] args){
        TimeService timeService = new TimeService();
        System.out.println(timeService.getStartEndCurrrentWeek().get("end"));

    }
}
