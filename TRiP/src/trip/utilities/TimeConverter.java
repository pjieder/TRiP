/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import static java.util.concurrent.TimeUnit.DAYS;
import javafx.util.Duration;

/**
 *
 * @author ander
 */
public class TimeConverter {

    public static String convertSecondsToString(int seconds) {
        Duration duration = Duration.seconds(seconds);
        String timeString = String.format("%02d:%02d:%02d",
                (int) duration.toHours(),
                (int) duration.toMinutes() % 60,
                (int) duration.toSeconds() % 60);
        return timeString;
    }

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

    public static String convertLocalDateToStringDB(LocalDate date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }
    
    public static int convertStringToSeconds(String string) {
        String[] time = string.split(":");
        int hours = Integer.parseInt(time[0]);
        int minutes = Integer.parseInt(time[1]);
        int seconds = Integer.parseInt(time[2]);

        return (hours * 60 * 60) + (minutes * 60) + seconds;
    }

    public static LocalDate addDays(LocalDate date, int days) {
        return date.plusDays(days);
    }

    public static List<LocalDate> getDaysBetweenDates(LocalDate date1, LocalDate date2) {
        
        List<LocalDate> datesBetween = new ArrayList();
        datesBetween.add(date1);

        long daysBetween = ChronoUnit.DAYS.between(date1, date2);

        if (daysBetween > 0) {

            for (int i = 1; i < daysBetween; i++) {
                datesBetween.add(TimeConverter.addDays(date1, i));
            }
            datesBetween.add(date2);
        }
        if (daysBetween < 0) {

            for (int i = -1; i > daysBetween; i--) {
                datesBetween.add(TimeConverter.addDays(date1, i));
            }
            datesBetween.add(date2);
        }
        
        return datesBetween;
    }
}
