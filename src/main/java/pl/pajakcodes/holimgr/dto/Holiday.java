package pl.pajakcodes.holimgr.dto;

import java.time.LocalDate;

public record Holiday(
        String name,
        LocalDate date
) {
}
