package pl.pajakcodes.holimgr.service;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import pl.pajakcodes.holimgr.dto.Holiday;
import pl.pajakcodes.holimgr.dto.OverlappingHolidaysResponse;
import pl.pajakcodes.holimgr.exception.NoOverlappingHolidaysException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class HolidayService {

    private static final Logger log = LogManager.getLogger(HolidayService.class);

    private final HolidayApiService holidayApiService;

    public OverlappingHolidaysResponse getOverlappingHolidays(String date, String countryCode1, String countryCode2) {
        LocalDate startDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE).plusDays(1);

        List<Holiday> firstHolidays = holidayApiService.getCountryHolidays(startDate, countryCode1);
        List<Holiday> secondHolidays = holidayApiService.getCountryHolidays(startDate, countryCode2);

        if (firstHolidays.isEmpty() || secondHolidays.isEmpty()) {
            throw new NoOverlappingHolidaysException();
        }

        return findEarliestOverlappingHoliday(firstHolidays, secondHolidays);
    }

    private OverlappingHolidaysResponse findEarliestOverlappingHoliday(List<Holiday> firstHolidays, List<Holiday> secondHolidays) {
        log.debug("invoked: HolidayService.findEarliestOverlappingHoliday(" + firstHolidays + ", " + secondHolidays + ")");

        Set<LocalDate> firstHolidayDates = new HashSet<>();
        for (Holiday holiday : firstHolidays) {
            firstHolidayDates.add(holiday.date());
        }

        Holiday firstEarliestHoliday = null;
        Holiday secondEarliestHoliday = null;

        for (Holiday holiday : secondHolidays) {
            LocalDate date = holiday.date();
            if (firstHolidayDates.contains(date)) {
                Holiday matchingHolidayInFirstList = firstHolidays.stream()
                        .filter(h -> h.date().equals(date))
                        .findFirst()
                        .orElse(null);

                if (matchingHolidayInFirstList != null) {
                    if (firstEarliestHoliday == null || date.isBefore(firstEarliestHoliday.date())) {
                        firstEarliestHoliday = matchingHolidayInFirstList;
                        secondEarliestHoliday = holiday;
                    }
                }
            }
        }
        return new OverlappingHolidaysResponse(firstEarliestHoliday.date(), firstEarliestHoliday.name(), secondEarliestHoliday.name());
    }

}
