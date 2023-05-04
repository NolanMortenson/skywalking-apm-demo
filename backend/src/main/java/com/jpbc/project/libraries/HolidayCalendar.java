package com.jpbc.project.libraries;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Component
@NoArgsConstructor
public class HolidayCalendar {
    
    /**
     * Assess if current date is holiday
     *
     * @param holidays map of holiday names to holiday dates
     * @param currDate current date
     * @return assertion that current date is holiday day
     */
    private boolean isHoliday(Map<HolidayCodes, LocalDate> holidays, LocalDate currDate) {
        List<LocalDate> holidayDates = new ArrayList<LocalDate>(holidays.values());
        return holidayDates.contains(currDate);
    }

    /**
     * Set holidays.
     *
     * @param year current year
     * @return map of holiday names to holiday dates
     */
    private Map<HolidayCodes, LocalDate> setHolidays(int year) {
        if (year < 2021) {
            return new EnumMap<>(Map.ofEntries(
                    Map.entry(HolidayCodes.NYD, LocalDate.of(year, 1, 1)),
                    Map.entry(HolidayCodes.MLK, getMLK(year)),
                    Map.entry(HolidayCodes.PRED, getPRED(year)),
                    Map.entry(HolidayCodes.MEM, getMEM(year)),
                    Map.entry(HolidayCodes.IDEP, LocalDate.of(year, 7, 4)),
                    Map.entry(HolidayCodes.LAB, getLAB(year)),
                    Map.entry(HolidayCodes.CIPD, getCIPD(year)),
                    Map.entry(HolidayCodes.VET, LocalDate.of(year, 11, 11)),
                    Map.entry(HolidayCodes.THK, getTHK(year)),
                    Map.entry(HolidayCodes.CHR, LocalDate.of(year, 12, 25))
            ));
        } else {
            return new EnumMap<>(Map.ofEntries(
                    Map.entry(HolidayCodes.NYD, LocalDate.of(year, 1, 1)),
                    Map.entry(HolidayCodes.MLK, getMLK(year)),
                    Map.entry(HolidayCodes.PRED, getPRED(year)),
                    Map.entry(HolidayCodes.MEM, getMEM(year)),
                    Map.entry(HolidayCodes.JTH, LocalDate.of(year, 6, 19)),
                    Map.entry(HolidayCodes.IDEP, LocalDate.of(year, 7, 4)),
                    Map.entry(HolidayCodes.LAB, getLAB(year)),
                    Map.entry(HolidayCodes.CIPD, getCIPD(year)),
                    Map.entry(HolidayCodes.VET, LocalDate.of(year, 11, 11)),
                    Map.entry(HolidayCodes.THK, getTHK(year)),
                    Map.entry(HolidayCodes.CHR, LocalDate.of(year, 12, 25))
            ));
        }
    }

    /**
     * Converts holidays to public holiday dates.
     *
     * @param holidays map of holiday names to dates
     * @return map of holiday names to public holiday dates
     */
    private Map<HolidayCodes, LocalDate> setPublicHolidays(Map<HolidayCodes, LocalDate> holidays) {
        DayOfWeek day = null;
        for (Entry<HolidayCodes, LocalDate> holiday : holidays.entrySet()) {
            day = holiday.getValue().getDayOfWeek();
            if (day == DayOfWeek.SATURDAY) {
                holidays.put(holiday.getKey(), holiday.getValue().minusDays(1));
            } else if (day == DayOfWeek.SUNDAY) {
                holidays.put(holiday.getKey(), holiday.getValue().plusDays(1));
            }
        }
        return holidays;
    }

    /**
     * Get date of Martin Luther King Jr. Day.
     *
     * @param year current year
     * @return third monday of january
     */
    private LocalDate getMLK(int year) {
        int mondays = 0;
        DayOfWeek day = null;
        LocalDate currDate = LocalDate.of(year, 1, 1);
        while (currDate.getMonthValue() == 1) {
            day = currDate.getDayOfWeek();
            if (day == DayOfWeek.MONDAY) {
                mondays += 1;
                if (mondays == 3) {
                    return currDate;
                }
            }
            currDate = currDate.plusDays(1);
        }
        return null;
    }

    /**
     * Get date of President's Day.
     *
     * @param year current year
     * @return third monday of february
     */
    private LocalDate getPRED(int year) {
        int mondays = 0;
        DayOfWeek day = null;
        LocalDate currDate = LocalDate.of(year, 2, 1);
        while (currDate.getMonthValue() == 2) {
            day = currDate.getDayOfWeek();
            if (day == DayOfWeek.MONDAY) {
                mondays += 1;
                if (mondays == 3) {
                    return currDate;
                }
            }
            currDate = currDate.plusDays(1);
        }
        return null;
    }

    /**
     * Get date of Memorial Day.
     *
     * @param year current year
     * @return last monday of may
     */
    private LocalDate getMEM(int year) {
        DayOfWeek day = null;
        LocalDate currDate = LocalDate.of(year, 5, 1);
        while (currDate.getMonthValue() == 5) {
            day = currDate.getDayOfWeek();
            if (day == DayOfWeek.MONDAY) {
                if (currDate.plusDays(7).getMonthValue() != 5) {
                    return currDate;
                }
            }
            currDate = currDate.plusDays(1);
        }
        return null;
    }

    /**
     * Get date of Labor Day.
     *
     * @param year current year
     * @return first monday of september
     */
    private LocalDate getLAB(int year) {
        LocalDate currDate = LocalDate.of(year, 9, 1);
        DayOfWeek day = currDate.getDayOfWeek();
        while (day != DayOfWeek.MONDAY) {
            currDate = currDate.plusDays(1);
            day = currDate.getDayOfWeek();
        }
        return currDate;
    }

    /**
     * Get date of Columbus/Indigenous Peoples' Day.
     *
     * @param year current year
     * @return second monday of october
     */
    private LocalDate getCIPD(int year) {
        int mondays = 0;
        DayOfWeek day = null;
        LocalDate currDate = LocalDate.of(year, 10, 1);
        while (currDate.getMonthValue() == 10) {
            day = currDate.getDayOfWeek();
            if (day == DayOfWeek.MONDAY) {
                mondays += 1;
                if (mondays == 2) {
                    return currDate;
                }
            }
            currDate = currDate.plusDays(1);
        }
        return null;
    }

    /**
     * Get date of Thanksgiving Day.
     *
     * @param year current year
     * @return fourth thursday of november
     */
    private LocalDate getTHK(int year) {
        int thursdays = 0;
        DayOfWeek day = null;
        LocalDate currDate = LocalDate.of(year, 11, 1);
        while (currDate.getMonthValue() == 11) {
            day = currDate.getDayOfWeek();
            if (day == DayOfWeek.THURSDAY) {
                thursdays += 1;
                if (thursdays == 4) {
                    return currDate;
                }
            }
            currDate = currDate.plusDays(1);
        }
        return null;
    }

    public boolean isTodayAHoliday(LocalDate date) {
        int currYear = date.getYear();
        Map<HolidayCodes, LocalDate> holidays = setPublicHolidays(setHolidays(currYear));
        return isHoliday(holidays, date);
    }

    enum HolidayCodes {NYD, MLK, PRED, MEM, JTH, IDEP, LAB, CIPD, VET, THK, CHR}
}
