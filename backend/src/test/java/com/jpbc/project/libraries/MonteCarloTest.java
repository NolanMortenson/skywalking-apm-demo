package com.jpbc.project.libraries;

import com.jpbc.project.models.HistoricalData;
import com.jpbc.project.models.Project;
import com.jpbc.project.models.SimulationResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.jpbc.project.libraries.MonteCarlo.calcEndDate;
import static com.jpbc.project.libraries.MonteCarlo.calcSprintCycleTime;
import static com.jpbc.project.libraries.MonteCarlo.calcWorkDays;
import static com.jpbc.project.libraries.MonteCarlo.decimalToRoundedPercentage;
import static com.jpbc.project.libraries.MonteCarlo.isDayHoliday;

class MonteCarloTest {

    @Test
    void calcWorkDaysWithoutHoliday() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        Date startDate = formatter.parse("5-Jul-2022");

        Assertions.assertEquals(5, calcWorkDays(startDate, 7));
    }

    @Test
    void calcWorkDaysWithHoliday() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        Date startDate = formatter.parse("29-Jun-2022");

        Assertions.assertEquals(4, calcWorkDays(startDate, 7));
    }

    @Test
    void calcWorkDaysWithMultipleHolidays() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        Date startDate = formatter.parse("23-Dec-2024");

        Assertions.assertEquals(8, calcWorkDays(startDate, 14));
    }

    @Test
    void calcSprintCycleTimeTest() {
        HistoricalData dataPoint = new HistoricalData(null, null, null, LocalDate.parse("2022-06-01"), LocalDate.parse("2022-06-15"), 100);

        Assertions.assertEquals(0.1F, calcSprintCycleTime(dataPoint));
    }

    @Test
    void calcSprintCycleTimeTest_withoutHoliday() {
        HistoricalData dataPoint = new HistoricalData(null, null, null, LocalDate.parse("2022-06-01"), LocalDate.parse("2022-06-15"), 0);

        Assertions.assertEquals(10.0, calcSprintCycleTime(dataPoint));
    }

    @Test
    void calcSprintCycleTimeTest_withHoliday() {
        HistoricalData dataPoint = new HistoricalData(null, null, null, LocalDate.parse("2022-10-03"), LocalDate.parse("2022-10-17"), 90);

        Assertions.assertEquals(0.1F, calcSprintCycleTime(dataPoint));
    }

    @Test
    void calcNumOfSprints() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        Date startDate = formatter.parse("1-Mar-2022");

        Assertions.assertEquals(6, MonteCarlo.calcNumOfSprints(51, 14, startDate));
    }

    @Test
    void calcNumOfSprints_2() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        Date startDate = formatter.parse("1-Jan-2024");

        Assertions.assertNotEquals(10, MonteCarlo.calcNumOfSprints(48, 7, startDate));
    }

    @Test
    void calcNumOfSprints_3() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        Date startDate = formatter.parse("1-Mar-2022");

        Assertions.assertEquals(8, MonteCarlo.calcNumOfSprints(37, 7, startDate));
    }

    @Test
    void calcEndDateOfProject() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        Date startDate = formatter.parse("5-Jul-2022");

        int numOfSprints = 2;
        int sprintDuration = 14;

        Assertions.assertEquals(LocalDate.parse("2022-08-02"), calcEndDate(startDate, numOfSprints, sprintDuration));
    }

    @Test
    void calcEndDateOfProject_2() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        Date startDate = formatter.parse("23-May-2022");

        int numOfSprints = 3;
        int sprintDuration = 7;

        Assertions.assertEquals(LocalDate.parse("2022-06-13"), calcEndDate(startDate, numOfSprints, sprintDuration));
    }

    @Test
    void roundsDecimalsCorrectly_1() {
        Assertions.assertEquals(1.5, decimalToRoundedPercentage(0.0151, 1));
    }

    @Test
    void roundsDecimalsCorrectly_2() {
        Assertions.assertEquals(100.0, decimalToRoundedPercentage(0.9999, 1));
    }

    @Test
    void roundsDecimalsCorrectly_3() {
        Assertions.assertEquals(0.0, decimalToRoundedPercentage(0.0001, 1));
    }

    @Test
    void roundsDecimalsCorrectly_4() {
        Assertions.assertEquals(55.56, decimalToRoundedPercentage(0.555555, 2));
    }

    @Test
    void checkIsTodayHoliday() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        Date date = formatter.parse("04-Jul-2022");

        Assertions.assertTrue(isDayHoliday(date));
    }

    @Test
    void checkIsTodayNotAHoliday() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        Date date = formatter.parse("05-Jul-2022");

        Assertions.assertFalse(isDayHoliday(date));
    }


    @Test
    void giveMeTheMonteTest() throws ParseException {
        List<HistoricalData> data = new ArrayList<>();
        data.add(new HistoricalData(1L, null, null, LocalDate.parse("2022-10-03"), LocalDate.parse("2022-10-17"), 4));
        data.add(new HistoricalData(2L, null, null, LocalDate.parse("2022-11-03"), LocalDate.parse("2022-11-17"), 8));
        data.add(new HistoricalData(3L, null, null, LocalDate.parse("2022-12-03"), LocalDate.parse("2022-12-17"), 2));
        data.add(new HistoricalData(4L, null, null, LocalDate.parse("2023-01-03"), LocalDate.parse("2023-01-17"), 7));
        data.add(new HistoricalData(5L, null, null, LocalDate.parse("2022-02-03"), LocalDate.parse("2022-02-17"), 4));
        data.add(new HistoricalData(6L, null, null, LocalDate.parse("2022-03-03"), LocalDate.parse("2022-03-17"), 9));
        data.add(new HistoricalData(7L, null, null, LocalDate.parse("2022-04-03"), LocalDate.parse("2022-04-17"), 3));
        data.add(new HistoricalData(8L, null, null, LocalDate.parse("2022-05-03"), LocalDate.parse("2022-05-17"), 1));
        data.add(new HistoricalData(9L, null, null, LocalDate.parse("2022-06-03"), LocalDate.parse("2022-06-17"), 0));
        data.add(new HistoricalData(10L, null, null, LocalDate.parse("2022-07-03"), LocalDate.parse("2022-07-17"), 11));

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        Date startDate = formatter.parse("1-Mar-2024");


        MonteCarlo monteCarlo = new MonteCarlo();
        ArrayList<SimulationResult> result = monteCarlo.giveMeTheMonte(data, new Project(null, null, null, startDate, 20, 14, null, null, null, null, 100, null));

        for (SimulationResult point : result) {
            Integer numOfSprints = point.getNumberOfSprints();
            Assertions.assertTrue(numOfSprints > 0);
        }
    }
}