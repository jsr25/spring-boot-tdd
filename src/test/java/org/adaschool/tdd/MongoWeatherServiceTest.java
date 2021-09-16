package org.adaschool.tdd;

import org.adaschool.tdd.controller.weather.dto.WeatherReportDto;
import org.adaschool.tdd.exception.WeatherReportNotFoundException;
import org.adaschool.tdd.repository.WeatherReportRepository;
import org.adaschool.tdd.repository.document.GeoLocation;
import org.adaschool.tdd.repository.document.WeatherReport;
import org.adaschool.tdd.service.MongoWeatherService;
import org.adaschool.tdd.service.WeatherService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MongoWeatherServiceTest
{
    WeatherService weatherService;

    @Mock
    WeatherReportRepository repository;

    @BeforeEach()
    public void setup()
    {
        weatherService = new MongoWeatherService( repository );
    }

    @Test
    void createWeatherReportCallsSaveOnRepository()
    {
        double lat = 4.7110;
        double lng = 74.0721;
        GeoLocation location = new GeoLocation( lat, lng );
        WeatherReportDto weatherReportDto = new WeatherReportDto( location, 35f, 22f, "tester", new Date() );
        weatherService.report( weatherReportDto );
        verify( repository ).save( any( WeatherReport.class ) );
    }

    @Test
    void weatherReportIdFoundTest()
    {
        String weatherReportId = "awae-asd45-1dsad";
        double lat = 4.7110;
        double lng = 74.0721;
        GeoLocation location = new GeoLocation( lat, lng );
        WeatherReport weatherReport = new WeatherReport( location, 35f, 22f, "tester", new Date() );
        when( repository.findById( weatherReportId ) ).thenReturn( Optional.of( weatherReport ) );
        WeatherReport foundWeatherReport = weatherService.findById( weatherReportId );
        Assertions.assertEquals( weatherReport, foundWeatherReport );
    }

    @Test
    void weatherReportIdNotFoundTest()
    {
        String weatherReportId = "dsawe1fasdasdoooq123";
        when( repository.findById( weatherReportId ) ).thenReturn( Optional.empty() );
        Assertions.assertThrows( WeatherReportNotFoundException.class, () -> {
            weatherService.findById( weatherReportId );
        } );
    }

    @Test
    void weatherFindNearLocationTest(){
        double lat = 4.7110;
        double lng = 74.0721;
        GeoLocation location = new GeoLocation( lat, lng );
        WeatherReport weatherReport = new WeatherReport( location, 35f, 22f, "tester", new Date() );
        WeatherReport weatherReport2 = new WeatherReport( location, 35f, 22f, "tester", new Date() );
        List<WeatherReport> list = new ArrayList<>();
        list.add(weatherReport);
        list.add(weatherReport2);
        when(weatherService.findNearLocation(weatherReport.getGeoLocation(),(float)0.0) ).thenReturn(list);
        List<WeatherReport> result = weatherService.findNearLocation(weatherReport.getGeoLocation(),(float)0.0);
        Assertions.assertEquals( list, result );
    }


    @Test
    void weatherFindByReporter(){
        double lat = 4.7110;
        double lng = 74.0721;
        GeoLocation location = new GeoLocation( lat, lng );
        WeatherReport weatherReport = new WeatherReport( location, 35f, 22f, "tester", new Date() );
        WeatherReport weatherReport2 = new WeatherReport( location, 35f, 22f, "tester", new Date() );
        WeatherReport weatherReport3 = new WeatherReport( location, 35f, 22f, "tester", new Date() );
        WeatherReport weatherReport4 = new WeatherReport( location, 35f, 22f, "tester", new Date() );
        List<WeatherReport> list = new ArrayList<>();
        list.add(weatherReport);
        list.add(weatherReport2);
        list.add(weatherReport3);
        list.add(weatherReport4);
        when(weatherService.findWeatherReportsByName("tester") ).thenReturn(list);
        List<WeatherReport> result =weatherService.findWeatherReportsByName("tester");
        Assertions.assertEquals( list, result );
    }

}
