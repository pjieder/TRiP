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
import java.util.Date;
import java.util.List;
import javafx.util.Duration;

/**
 *
 * @author ander
 */
public class TimeConverter {

    /**
     * Converts an amount of seconds to a string representing the time.
     * @param seconds The seconds to be represented as a string.
     * @return A string representing the amount of time in a HH:mm:ss format.
     */
    public static String convertSecondsToString(int seconds) {
        Duration duration = Duration.seconds(seconds);
        String timeString = String.format("%02d:%02d:%02d",
                (int) duration.toHours(),
                (int) duration.toMinutes() % 60,
                (int) duration.toSeconds() % 60);
        return timeString;
    }
    
    /**
     * Converts a string value representing time to an int value containing the total amount of time in seconds.
     * @param string The string representing the amount of time in a HH:mm:ss format.
     * @return 
     */
    public static int convertStringToSeconds(String string) {
        String[] time = string.split(":");
        int hours = Integer.parseInt(time[0]);
        int minutes = Integer.parseInt(time[1]);
        int seconds = Integer.parseInt(time[2]);

        return (hours * 60 * 60) + (minutes * 60) + seconds;
    }

    /**
     * Converts a date object to a string based on the format dd-MM-yyyy HH:mm
     * @param date The date to be converted
     * @return A string representing the date.
     */
    public static String convertDateToString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        return simpleDateFormat.format(date);
    }

    /**
     * Converts a string to a date object based on the format dd-MM-yyyy HH:mm
     * @param dateString The string to be converted
     * @return A date object of the entered date.
     */
    public static Date convertStringToDate(String dateString) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            return simpleDateFormat.parse(dateString);
        } catch (ParseException exception) {

        }
        return null;
    }

    /**
     * Converts a date object to a string based on the format yyyy-MM-dd HH:mm for easier sorting in
     * the database.
     * @param date The date to be converted.
     * @return A string representing the date..
     */
    public static String convertDateToStringDB(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return simpleDateFormat.format(date);
    }

    /**
     * Converts a string to a date object based on the format yyyy-MM-dd HH:mm.
     * @param dateString The string to be converted.
     * @return A date object of the entered date.
     */
    public static Date convertStringToDateDB(String dateString) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return simpleDateFormat.parse(dateString);
        } catch (ParseException exception) {

        }
        return null;
    }

    /**
     * Adds the amount of to the localdate object.
     * @param date The date that the calculation is based upon.
     * @param days The amount of days to be added (or subtracted).
     * @return 
     */
    public static LocalDate addDays(LocalDate date, int days) {
        return date.plusDays(days);
    }

    /**
     * Returns a list containing all the days between and indcluding the entered days.
     * @param date1 The first day.
     * @param date2 The last day.
     * @return A list containing all days between and uncluding the entered days.
     */
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
