/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.utilities;

import javafx.beans.property.SimpleStringProperty;
import javafx.util.Duration;

/**
 *
 * @author ander
 */
public class TimeConverter {
    
    public static String convertSecondsToString(int seconds)
    {
        Duration duration = Duration.seconds(seconds);
            String timeString = String.format("%02d:%02d:%02d",
                    (int) duration.toHours() % 60,
                    (int) duration.toMinutes() % 60,
                    (int) duration.toSeconds() % 60);
            return timeString;
    
    }
    
//        public static String convertStringToSeconds(String seconds)
//    {
//        
//    
//    }
    
}
