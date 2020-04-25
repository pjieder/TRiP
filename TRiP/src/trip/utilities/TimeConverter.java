/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.util.Duration;

/**
 *
 * @author ander
 */
public class TimeConverter {

    public static String convertSecondsToString(int seconds) {
        Duration duration = Duration.seconds(seconds);
        String timeString = String.format("%02d:%02d:%02d",
                (int) duration.toHours() % 60,
                (int) duration.toMinutes() % 60,
                (int) duration.toSeconds() % 60);
        return timeString;
    }
    
//If we need converted to days too
//    
//    public static String convertSecondsToString(int seconds) {
//        Duration duration = Duration.seconds(seconds);
//        String timeString = String.format("%02d:%02d:%02d:%02d",
//                (int) (duration.toHours()/24) % 60,
//                (int) duration.toHours() % 60,
//                (int) duration.toMinutes() % 60,
//                (int) duration.toSeconds() % 60);
//        return timeString;
//    }

    public static String convertDateToString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        return simpleDateFormat.format(date);
    }

    public static Date convertStringToDate(String dateString) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            return simpleDateFormat.parse(dateString);
        } catch (ParseException exception) {

        }
        return null;
    }

    public static String convertDateToStringDB(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return simpleDateFormat.format(date);
    }

    public static Date convertStringToDateDB(String dateString) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return simpleDateFormat.parse(dateString);
        } catch (ParseException exception) {

        }
        return null;
    }
    
    public static int convertStringToSeconds(String string)
    {
        String[] time = string.split(":");
        int hours = Integer.parseInt(time[0]);
        int minutes = Integer.parseInt(time[1]);
        int seconds = Integer.parseInt(time[2]);
        
        return (hours * 60 * 60) + (minutes * 60) + seconds;
    }

}
