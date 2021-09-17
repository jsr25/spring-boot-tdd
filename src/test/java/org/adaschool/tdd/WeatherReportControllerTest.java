package org.adaschool.tdd;

import org.adaschool.tdd.controller.weather.dto.WeatherReportDto;
import org.adaschool.tdd.repository.WeatherReportRepository;
import org.adaschool.tdd.repository.document.GeoLocation;
import org.adaschool.tdd.repository.document.WeatherReport;
import org.adaschool.tdd.service.MongoWeatherService;
import org.adaschool.tdd.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WeatherReportControllerTest {

    WeatherService weatherService;

    @MockBean
    WeatherReportRepository repository;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach()
    public void setup() {
        weatherService = new MongoWeatherService(repository);


    }



    @Test
    void weatherCreateTest() throws Exception {
        double lat = 4.7110;
        double lng = 74.0721;
        GeoLocation location = new GeoLocation(lat, lng);
        WeatherReportDto weatherReportDto = new WeatherReportDto(location, 35f, 22f, "tester", new Date());
        WeatherReport weatherReport = new WeatherReport(weatherReportDto);
        when(repository.save(any(WeatherReport.class))).thenReturn(weatherReport);
        baseUrl = "http://localhost:" + port + "/v1/weather";
        URI uri = new URI(baseUrl);

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<WeatherReportDto> request = new HttpEntity<>(weatherReportDto, headers);

        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);

        assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());
    }

    @Test
    void weatherFindByIdTest() throws URISyntaxException {
        String idWeather="asda185212asd123";
        double lat = 4.7110;
        double lng = 74.0721;
        GeoLocation location = new GeoLocation(lat, lng);
        WeatherReportDto weatherReportDto = new WeatherReportDto(location, 35f, 22f, "tester", new Date());
        WeatherReport weatherReport = new WeatherReport(weatherReportDto);
        baseUrl = "http://localhost:" + port + "/v1/weather";

        when(repository.findById(any(String.class))).thenReturn(java.util.Optional.of(weatherReport));
        URI uri = new URI(baseUrl+"/asda185212asd123");

        ResponseEntity<WeatherReport> result = this.restTemplate.getForEntity(uri, WeatherReport.class);

        assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());
    }
    
}
