package pl.pajakcodes.holimgr.service;

import pl.pajakcodes.holimgr.dto.Holiday;

import java.time.LocalDate;
import java.util.List;

public interface HolidayApiService {

    List<Holiday> getCountryHolidays(LocalDate startDate, String countryCode);

}
