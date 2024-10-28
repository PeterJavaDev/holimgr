package pl.pajakcodes.holimgr.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.pajakcodes.holimgr.dto.CountryHoliday;
import pl.pajakcodes.holimgr.dto.Holiday;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class ExternalHolidayApiService implements HolidayApiService {

    private static final Logger log = LogManager.getLogger(ExternalHolidayApiService.class);

    @Value("${holimgr.external-holiday-api-service.months-to-check:12}")
    private Long monthsToCheck;

    public List<Holiday> getCountryHolidays(LocalDate startDate, String countryCode) {
        log.debug("invoked: ExternalHolidayApiService.getCountryHolidays(" + startDate + ", " + countryCode + ")");

        log.debug("monthsToCheck: " + monthsToCheck);
        LocalDate endDate = startDate.plusMonths(monthsToCheck);
        String responseJSon = getStringResponseEntity(startDate, countryCode, endDate);
        List<CountryHoliday> countryHolidays = mapToCountryHolidays(responseJSon);

        return toHolidayList(countryHolidays);
    }

    private String getStringResponseEntity(LocalDate startDate, String countryCode, LocalDate endDate) {
        String uri = "https://openholidaysapi.org/PublicHolidays?countryIsoCode="
                + countryCode
                + "&validFrom="
                + startDate.getYear() + "-" + startDate.getMonth() + "-" + startDate.getDayOfMonth()
                + "&validTo="
                + endDate.getYear() + "-" + endDate.getMonth() + "-" + endDate.getDayOfMonth();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                uri,
                String.class
        );

        String responseJSon = responseEntity.getBody();
        log.debug("API response: " + responseJSon);
        return responseJSon;
    }

    private static List<CountryHoliday> mapToCountryHolidays(String responseJSon) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<CountryHoliday> countryHolidays;
        try {
            countryHolidays = objectMapper.readValue(responseJSon, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return countryHolidays;
    }

    private List<Holiday> toHolidayList(List<CountryHoliday> countryHolidays) {
        List<Holiday> holidayList = new ArrayList<>();
        for (CountryHoliday countryHoliday : countryHolidays) {
            String name = countryHoliday.name().getFirst().text();
            LocalDate startDate = LocalDate.parse(countryHoliday.startDate());
            holidayList.add(new Holiday(name, startDate));
        }
        return holidayList;
    }
}
