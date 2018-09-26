package bhanupro.hackernews.Helpers;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Constants {
    public static final String BASE_URL= "https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty";
    public static final String ITEM_URL = "";


    public static String convertDate(String s) throws ParseException {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd HH");
        Date date = originalFormat.parse(s);
        Log.e("date","formatted date :: "+date);
        return date.toString();
    }
    public static String convertToDaysHoursMinutes(long minutes) {

        int day = (int) TimeUnit.MINUTES.toDays(minutes);
        long hours = TimeUnit.MINUTES.toHours(minutes) - (day *24);
        long minute = TimeUnit.MINUTES.toMinutes(minutes) - (TimeUnit.MINUTES.toHours(minutes)* 60);

        String result = "";

        if (day != 0){
            result += day;
            if (day == 1){
                result += " day ";
            }
            else{
                result += " days ";
            }

            return result;
        }

        if (hours != 0){
            result += hours;

            if (hours == 1){
                result += " hr ";
            }
            else{
                result += " hrs ";
            }
        }

        if (minute != 0){
            result += minute;

            if (minute == 1){
                result += " min";
            }
            else{
                result += " mins";
            }
        }

        return result;
    }
}
