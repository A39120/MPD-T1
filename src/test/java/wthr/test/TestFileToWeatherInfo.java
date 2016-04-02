package wthr.test;

import org.junit.Assert;
import org.junit.Test;
import util.HttpGetter;
import wthr.Parsers;
import wthr.WeatherFileCreator;
import wthr.WeatherFileGetterFromCsv;
import wthr.WeatherHttpGetterFromCsv;
import wthr.model.WeatherInfo;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by João on 02/04/2016.
 */
public class TestFileToWeatherInfo {

    public void init() throws IOException {
        List<String> lines = HttpGetter.httpGet("http://api.worldweatheronline.com/" +
                "free/v2/past-weather.ashx?key=25781444d49842dc5be040ff259c5" +
                "&q=Lisbon&format=csv" +
                "&date=2016-03-01" +
                "&enddate=2016-03-10" +
                "&tp=24", ParsersUtils::HttpReader );
        WeatherFileCreator.createFile("Lisbon", LocalDate.of(2016,03,01), LocalDate.of(2016,03,10),
                                      lines);

    }

    public void end(){
        new File("src\\main\\resources\\data\\Lisbon_2016-03-01_2016-03-10.csv").delete();
    }

    @Test
    public void FileToWeatherInfoTest() throws IOException {
        init();
        List<WeatherInfo> expected= WeatherHttpGetterFromCsv.getHistory("Lisbon", LocalDate.of(2016,03,01), LocalDate.of(2016,03,10));
        List<WeatherInfo> actual = WeatherFileGetterFromCsv.getHistory("Lisbon", LocalDate.of(2016,03,01), LocalDate.of(2016,03,10));
        for(WeatherInfo ewi:expected){
            WeatherInfo awi = actual.remove(0);
            Assert.assertEquals(ewi.date, awi.date);
            Assert.assertEquals(ewi.feelsLikeC, awi.feelsLikeC);
            //Assert.assertEquals(ewi.precipMM, awi.precipMM);
            Assert.assertEquals(ewi.tempC, awi.tempC);
            Assert.assertEquals(ewi.weatherDesc, awi.weatherDesc);
        }
        end();
    }
}
