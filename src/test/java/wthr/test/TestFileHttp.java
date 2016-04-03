package wthr.test;

import org.junit.Assert;
import org.junit.Test;
import wthr.*;
import wthr.model.HistoryArgs;
import wthr.model.WeatherInfo;
import wthr.model.WeatherRegion;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

public class TestFileHttp {

    @Test
    public void getFromHttpAndWriteToFile(){
        WeatherRegion region = new WeatherRegion("Porto", 10, WeatherHttpGetterFromCsv::getHistory);
        List<WeatherInfo> expected = region.getHistory(LocalDate.of(2016, 3, 1),
                LocalDate.of(2016, 3, 31));

        WeatherFileCreator.createFile("Porto", expected, Parsers::parseWeatherInfoToCsv);

        region = new WeatherRegion("Porto", 10, WeatherFileGetterFromCsv::getHistory);
        List<WeatherInfo> actual = region.getHistory(LocalDate.of(2016, 3, 1),
                LocalDate.of(2016, 3, 31));

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void simple_tests(){
        String nome = "Cartaxo";

        Function<HistoryArgs, List<WeatherInfo>> http = WeatherHttpGetterFromCsv::getHistory;
        FunctionCounter<HistoryArgs, List<WeatherInfo>> counter = new FunctionCounter<>(http);

        FilePlan m = new FilePlan(counter);

        WeatherRegion region = new WeatherRegion(nome, 14, m);

        /* Maybe more robust? */
        new File(String.format("src/main/resources/data/history_%s.csv", nome)).delete();

        /* Insert elements in empty cache */
        List<WeatherInfo> infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 5));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(1, counter.getCount());


        /* Insert multiple elements at end of cache (with overlapping) */
        infos = region.getHistory(LocalDate.of(2016, 3, 5),
                LocalDate.of(2016, 3, 7));

        Assert.assertEquals(3, infos.size());
        Assert.assertEquals(2, counter.getCount());


        /* Check for entire cache */
        infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 7));

        Assert.assertEquals(4, infos.size());
        Assert.assertEquals(2, counter.getCount());


        /* Check for first element of cache */
        infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 4));

        Assert.assertEquals(1, infos.size());
        Assert.assertEquals(2, counter.getCount());


        /* Check for last element of cache */
        infos = region.getHistory(LocalDate.of(2016, 3, 7),
                LocalDate.of(2016, 3, 7));

        Assert.assertEquals(1, infos.size());
        Assert.assertEquals(counter.getCount(), 2);


        /* Insert multiple elements at end of cache(no overlapping) */
        infos = region.getHistory(LocalDate.of(2016, 3, 8),
                LocalDate.of(2016, 3, 9));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(3, counter.getCount());


        /*Insert multiple elements at beginning of cache(with overlapping) */
        infos = region.getHistory(LocalDate.of(2016, 3, 2),
                LocalDate.of(2016, 3, 4));

        Assert.assertEquals(3, infos.size());
        Assert.assertEquals(4, counter.getCount());


        /*Insert one element at beginning of cache */
        infos = region.getHistory(LocalDate.of(2016, 3, 1),
                LocalDate.of(2016, 3, 1));

        Assert.assertEquals(1, infos.size());
        Assert.assertEquals(5, counter.getCount());


        /* Insert One element at end of cache */
        infos = region.getHistory(LocalDate.of(2016, 3, 10),
                LocalDate.of(2016, 3, 10));

        Assert.assertEquals(1, infos.size());
        Assert.assertEquals(6, counter.getCount());

    }

}
