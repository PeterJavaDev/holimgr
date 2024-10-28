package pl.pajakcodes.holimgr.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.pajakcodes.holimgr.dto.OverlappingHolidaysResponse;
import pl.pajakcodes.holimgr.service.ExternalHolidayApiService;
import pl.pajakcodes.holimgr.service.HolidayService;

@Controller
@RequestMapping("holimgr")
@AllArgsConstructor
public class HolidayController {

    private static final Logger log = LogManager.getLogger(ExternalHolidayApiService.class);

    private final HolidayService holidayService;

    @GetMapping("getOverlapping")
    public ResponseEntity<OverlappingHolidaysResponse> getOverlappingHolidays(
            @RequestParam @NotNull @Size(min = 10, max = 10) String date,
            @RequestParam @NotNull @Size(min = 2, max = 2) String countryCode1,
            @RequestParam @NotNull @Size(min = 2, max = 2) String countryCode2
    ) {
        log.info("invoked: HolidayController.getOverlappingHolidays(" + date + ", " + countryCode1 + ", " + countryCode2 + ")");

        OverlappingHolidaysResponse overlappingHolidays = holidayService.getOverlappingHolidays(date, countryCode1, countryCode2);
        return new ResponseEntity<>(overlappingHolidays, HttpStatus.OK);
    }


}
