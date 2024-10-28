# Holiday Manager
This example application uses Java 21 and Spring-Boot

# How to run this project locally 
Application requires java 21, JAVA_HOME env variable set <br />
From inside holimgr main directory build project using maven:

```.\mvnw clean package```

Start application using spring-boot maven plugin

```.\mvnw spring-boot:run```

There is no swagger or any documentation since it's only one simple endpoint:

http://localhost:8080/holimgr/getOverlapping?date=2022-12-26&countryCode1=PL&countryCode2=DE

# About the external API
The external API of my choice was random and I see it's limitations now like: <br />
- Very few country codes supported (PL, DE, CZ etc.)
- There is start and end date of each holiday however I haven't been able to find one holiday that was lasting for more than one day (under the same name) so I've presumed that start date is the only date of given holiday
- There could be bugs in API (for Czechia it doesn't find Easter Sunday but finds Easter Monday)
- Holidays for each country are returned chronologically however I presume random order to have more flexibility when adding other external API's 
- Because API doesn't return values for missing country codes so most of the wrong input data returns 204 http code as there in no holidays data to check

# Other points to consider
- <b>holimgr.external-holiday-api-service.months-to-check</b> property is to define how many months after the input date the holidays data is considered
- Tests would require a little more attention. I'm using live external service because it's good to have integration tests to check if external API haven't changed. However, we could consider mocked version of external API
- I've skipped docker-compose since it's only one service without database