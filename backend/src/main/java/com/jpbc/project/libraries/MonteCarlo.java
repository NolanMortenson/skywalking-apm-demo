package com.jpbc.project.libraries;

import com.jpbc.project.models.HistoricalData;
import com.jpbc.project.models.Project;
import com.jpbc.project.models.SimulationResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class MonteCarlo {


    public static int calcWorkDays(Date startDate, int sprintDuration) {
        int workdays = 0;

        for (int i = 0; i < sprintDuration; i++) {
            Date currDate = DateUtils.addDays(startDate, i);

            Calendar cal = Calendar.getInstance();
            cal.setTime(currDate);
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

            if (!isDayHoliday(currDate) && dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                workdays++;
            }
        }
        return workdays;
    }

    public static float calcSprintCycleTime(HistoricalData dataPoint) {
        final String AMERICA_NEW_YORK = "America/New_York";

        int storiesCompleted = dataPoint.getStoriesPoints();
        LocalDate startDate = dataPoint.getCreationDate();
        LocalDate endDate = dataPoint.getResolvedDate();

        int dif = Period.between(startDate, endDate).getDays();

        Date startDateAsDate = Date.from(startDate.atStartOfDay(ZoneId.of(AMERICA_NEW_YORK)).toInstant());

        int workDays = calcWorkDays(startDateAsDate, dif);

        if (storiesCompleted == 0) {
            return workDays;
        }

        return (float) workDays / storiesCompleted;
    }

    public static boolean isDayHoliday(Date date) {
        LocalDate localDate = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        HolidayCalendar holidayCalendar = new HolidayCalendar();
        return holidayCalendar.isTodayAHoliday(localDate);
    }

    // Give us the # of sprints we need in order to complete the project
    public static int calcNumOfSprints(Integer totalWorkdaysNeeded, Integer sprintDuration, Date startDate) {
        // take in startDate & total workdays needed
        int days = totalWorkdaysNeeded;
        int totalDaysElapsed = 0;
        Date currDate = startDate;

        while (days >= 0) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(currDate);
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY && !isDayHoliday(currDate)) {
                days--;
            }

            totalDaysElapsed++;
            currDate = DateUtils.addDays(currDate, 1);
        }

        return (int) Math.ceil((float) totalDaysElapsed / sprintDuration);
    }

    // Find expected end date of project
    public static LocalDate calcEndDate(Date startDate, Integer numberOfSprints, Integer sprintDuration) {
        Date temp = DateUtils.addDays(startDate, numberOfSprints * sprintDuration);
        return temp.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static double decimalToRoundedPercentage(double value, int precision) {
        final int PERCENT_MULTIPLIER = 100;
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * PERCENT_MULTIPLIER * scale) / scale;
    }

    public ArrayList<SimulationResult> giveMeTheMonte(List<HistoricalData> data, Project project) {
        final int SIMULATION_AVG_COUNT = 10;
        int storiesToComplete = project.getStoryCount();
        int simulationsCount = project.getProjectSimulations();
        int sprintDuration = project.getSprintDuration();
        Date startDate = project.getForecastStartDate();

        if (data.size() == 0) {
            return null;
        }

        ArrayList<Float> sprintCycleTime = new ArrayList<>();

        //Calculating Sprint Cycle Times and storing them
        for (HistoricalData datum : data) {
            float x = calcSprintCycleTime(datum);
            sprintCycleTime.add(x);
        }

        //Generating averages for Monte Carlo Simulation
        ArrayList<Float> averageSprintCycleTime = new ArrayList<>();
        for (int i = 0; i < simulationsCount; i++) {
            float sum = 0;
            for (int j = 0; j < SIMULATION_AVG_COUNT; j++) {
                sum += sprintCycleTime.get((int) (Math.random() * (sprintCycleTime.size() - 1)));
            }
            averageSprintCycleTime.add(sum / SIMULATION_AVG_COUNT);
        }

        // Counting the occurrences of each estimated Sprint number
        HashMap<Integer, Integer> sprintOccurrences = new HashMap<>();
        for (int i = 0; i < simulationsCount; i++) {
            // x = num of workdays needed to complete the project
            int x = (int) Math.ceil(storiesToComplete * averageSprintCycleTime.get(i));

            // y = total sprints needed to complete the project
            int y = calcNumOfSprints(x, sprintDuration, startDate);

            sprintOccurrences.merge(y, 1, Integer::sum);
        }

        ArrayList<Integer> keys = new ArrayList<>(sprintOccurrences.keySet());
        Collections.sort(keys);
        ArrayList<SimulationResult> results = new ArrayList<>();

        //Converting data to GraphPoints
        float cumulativeSuccessRate = 0;
        for (Integer key : keys) {
            float partialSuccessRate = (sprintOccurrences.get(key)) / (float) simulationsCount;
            cumulativeSuccessRate += partialSuccessRate;
            SimulationResult point = new SimulationResult(
                    project,
                    key,
                    sprintOccurrences.get(key),
                    decimalToRoundedPercentage(partialSuccessRate, 1),
                    decimalToRoundedPercentage(cumulativeSuccessRate, 1),
                    calcEndDate(startDate, key, sprintDuration));
            results.add(point);
        }

        return results;
    }
}




