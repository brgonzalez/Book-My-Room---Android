package com.snaptechnology.bgonzalez.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by bgonzalez on 28/09/2016.
 */
public class Test {
    public static void main() throws ParseException {
        int inputDate = 20121220;
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        Date date = df.parse(String.valueOf(inputDate));

        Calendar calendar = Calendar.getInstance();


    }
}
