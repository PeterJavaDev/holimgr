package pl.pajakcodes.holimgr.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.pajakcodes.holimgr.dto.Holiday;
import pl.pajakcodes.holimgr.dto.OverlappingHolidaysResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class HolidayServiceTest {

    @Mock
    ExternalHolidayApiService externalHolidayApiService;

    @Test
    void testGetOverlappingHolidays() {
        List<Holiday> polishHolidays = new ArrayList<>();
        polishHolidays.add(new Holiday("Boże Narodzenie", LocalDate.parse("2022-12-25")));
        polishHolidays.add(new Holiday("Nowy Rok", LocalDate.parse("2023-01-01")));

        List<Holiday> germanHolidays = new ArrayList<>();
        germanHolidays.add(new Holiday("1. Weihnachtsfeiertag", LocalDate.parse("2022-12-25")));
        germanHolidays.add(new Holiday("Neujahr", LocalDate.parse("2023-01-01")));

        Mockito.when(externalHolidayApiService.getCountryHolidays(LocalDate.parse("2022-12-21"), "PL")).thenReturn(polishHolidays);
        Mockito.when(externalHolidayApiService.getCountryHolidays(LocalDate.parse("2022-12-21"), "DE")).thenReturn(germanHolidays);

        HolidayService holidayService = new HolidayService(externalHolidayApiService);
        OverlappingHolidaysResponse response = holidayService.getOverlappingHolidays("2022-12-20", "PL", "DE");

        assertEquals(LocalDate.parse("2022-12-25"), response.date());
        assertEquals("Boże Narodzenie", response.name1());
        assertEquals("1. Weihnachtsfeiertag", response.name2());
    }
}