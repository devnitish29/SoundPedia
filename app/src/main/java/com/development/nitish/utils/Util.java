package com.development.nitish.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Nitish Singh Rathore on 25/6/17.
 */

public class Util {


    public static String convertTime(Integer milliseconds) {

        String mins = TimeUnit.MILLISECONDS.toMinutes(milliseconds) + "m";
        String secs = (TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds))) + "s";

        return mins.concat(" " + secs);
    }


    public static String convertDate(String dateformat) {

        String splitDate = dateformat.substring(0,dateformat.indexOf("T"));
        String normalFormat = "";
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy");
        Date date = null;
        try {
            date = parser.parse(splitDate);
            normalFormat = formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return normalFormat;
    }
}