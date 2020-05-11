/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;


import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import trip.utilities.TimeConverter;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author NLens
 */
public class TimeConverterTest {
    
    public TimeConverterTest() {
        
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    
    @Test
    public void testConvertSecondsToString () {
        System.out.println("Testing convertSecondsToString()");
        int duration = new Integer(3602);
        
        String expResult = "01:00:02";
        String result = TimeConverter.convertSecondsToString(duration);
        assertEquals(expResult, result);
        
    }
    
    @Test
    public void testConvertStringToSeconds () {
        System.out.println("Testing convertStringToSeconds()");
        String time = new String("01:00:02");
        
        int expResult = 3602;
        int result = TimeConverter.convertStringToSeconds(time);
        assertEquals(expResult, result);
        
    }
    
    //TODO later
    @Test
    public void testGetDaysBetweenDates () {
        System.out.println("Testing getDaysBetweenDates()");
        
        LocalDate date1 = LocalDate.of(2020, Month.MARCH, 25);
        LocalDate date2 = LocalDate.of(2020, Month.MARCH, 26);
        LocalDate date3 = LocalDate.of(2020, Month.MARCH, 27);
        LocalDate date4 = LocalDate.of(2020, Month.MARCH, 28);
        LocalDate date5 = LocalDate.of(2020, Month.MARCH, 29);
        
        List<LocalDate> allList = new ArrayList();
        List<LocalDate> revertedList = new ArrayList();
        
        allList.add(date1);
        allList.add(date2);
        allList.add(date3);
        allList.add(date4);
        allList.add(date5);
        
        revertedList.add(date5);
        revertedList.add(date4);
        revertedList.add(date3);
        revertedList.add(date2);
        revertedList.add(date1);
        
        //TODO
        
        int expResultSize = 5;
        List<LocalDate> dates = TimeConverter.getDaysBetweenDates(date1, date5);
        List<LocalDate> revertedDates = TimeConverter.getDaysBetweenDates(date5, date1);
        
        assertEquals(expResultSize, dates.size());
        assertArrayEquals(allList.toArray(), dates.toArray());
        assertArrayEquals(revertedList.toArray(), revertedDates.toArray());
  
    }
    
}
