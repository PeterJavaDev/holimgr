package pl.pajakcodes.holimgr.dto;

import java.time.LocalDate;

public record OverlappingHolidaysResponse(

        LocalDate date,

        String name1,

        String name2
) {
}
