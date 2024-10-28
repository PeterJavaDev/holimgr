package pl.pajakcodes.holimgr.dto;

import java.util.ArrayList;

public record CountryHoliday(
        String id,
        String startDate,
        String endDate,
        String type,
        ArrayList<HolidayName> name,
        boolean nationwide,
        ArrayList<CountrySubdivision> subdivisions
) {
}
