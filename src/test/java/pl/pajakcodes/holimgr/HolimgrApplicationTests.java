package pl.pajakcodes.holimgr;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HolimgrApplicationTests {

	private String uri;

	@LocalServerPort
	private int port;

	@BeforeEach
	public void beforeEach() {
		uri = "http://localhost:" + port + "/";
	}

	@Test
	void testGetOverlappingSuccess() throws IOException, JSONException, URISyntaxException {
		String expectedJson = Files.readString(Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("testGetOverlappingSuccess.json")).toURI()));
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<String> responseEntity = restTemplate.getForEntity(
				uri + "holimgr/getOverlapping?date=2022-12-20&countryCode1=PL&countryCode2=DE",
				String.class
		);

		JSONAssert.assertEquals(expectedJson, responseEntity.getBody(), false);
	}

	@Test
	void testGetOverlappingNoCountryCode() throws IOException, JSONException, URISyntaxException {
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<String> responseEntity = restTemplate.getForEntity(
				uri + "holimgr/getOverlapping?date=2022-12-20&countryCode1=PL&countryCode2=US",
				String.class
		);

		assertEquals("Wrong status code", HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
	}

	@Test
	void testGetOverlappingValidationFailure() throws IOException, JSONException, URISyntaxException {
		RestTemplate restTemplate = new RestTemplate();

		Exception exception = assertThrows(HttpClientErrorException.class, () -> {
			restTemplate.getForEntity(
					uri + "holimgr/getOverlapping?date=2022-12-204232&countryCode1=PL&countryCode2=US",
					String.class
			);
		});

		assertTrue(exception.getMessage().contains("Validation failure"));
	}

}
