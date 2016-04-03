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

public class TestFileHttp {

    private static final String NAME = "Porto";

    @Test
    public void getFromHttpAndWriteToFile(){
        new File(String.format("src/main/resources/data/history_%s.csv", NAME)).delete();
        WeatherRegion region = new WeatherRegion(NAME, 10, WeatherHttpGetterFromCsv::getHistory);
        List<WeatherInfo> expected = region.getHistory(LocalDate.of(2016, 3, 1),
                LocalDate.of(2016, 3, 31));

        WeatherFileCreator.createFile(NAME, expected, Parsers::parseWeatherInfoToCsv);

        region = new WeatherRegion(NAME, 10, WeatherFileGetterFromCsv::getHistory);
        List<WeatherInfo> actual = region.getHistory(LocalDate.of(2016, 3, 1),
                LocalDate.of(2016, 3, 31));

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void check_with_multiple_gaps() {
        new File(String.format("src/main/resources/data/history_%s.csv", NAME)).delete();
        FunctionCounter<HistoryArgs, List<WeatherInfo>> counter
                = new FunctionCounter<>(WeatherHttpGetterFromCsv::getHistory);
        FilePlan m = new FilePlan(counter);
        WeatherRegion region = new WeatherRegion(NAME, 10, m);

        /* Insert elements in empty cache */
        List<WeatherInfo> infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 5));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 1);

        infos = region.getHistory(LocalDate.of(2016, 3, 8),
                LocalDate.of(2016, 3, 9));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 2);

        infos = region.getHistory(LocalDate.of(2016, 3, 11),
                LocalDate.of(2016, 3, 12));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 3);

        infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 12));

        Assert.assertEquals(9, infos.size());
        Assert.assertEquals(counter.getCount(), 4);
    }

    @Test
    public void check_with_big_gap() {
        new File(String.format("src/main/resources/data/history_%s.csv", NAME)).delete();
        FunctionCounter<HistoryArgs, List<WeatherInfo>> counter
                = new FunctionCounter<>(WeatherHttpGetterFromCsv::getHistory);
        FilePlan m = new FilePlan(counter);
        WeatherRegion region = new WeatherRegion(NAME, 10, m);

        /* Insert elements in empty cache */
        List<WeatherInfo> infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 5));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 1);

        infos = region.getHistory(LocalDate.of(2016, 3, 8),
                LocalDate.of(2016, 3, 9));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 2);

        infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 9));

        Assert.assertEquals(6, infos.size());
        Assert.assertEquals(counter.getCount(), 3);
    }

    @Test
    public void check_with_single_gap() {
        new File(String.format("src/main/resources/data/history_%s.csv", NAME)).delete();
        FunctionCounter<HistoryArgs, List<WeatherInfo>> counter
                = new FunctionCounter<>(WeatherHttpGetterFromCsv::getHistory);
        FilePlan m = new FilePlan(counter);
        WeatherRegion region = new WeatherRegion(NAME, 10, m);

        /* Insert elements in empty cache */
        List<WeatherInfo> infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 5));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 1);

        infos = region.getHistory(LocalDate.of(2016, 3, 7),
                LocalDate.of(2016, 3, 8));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 2);

        infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 8));

        Assert.assertEquals(5, infos.size());
        Assert.assertEquals(counter.getCount(), 3);
    }

    @Test
    public void check_after_no_overlapping() {
        new File(String.format("src/main/resources/data/history_%s.csv", NAME)).delete();
        FunctionCounter<HistoryArgs, List<WeatherInfo>> counter
                = new FunctionCounter<>(WeatherHttpGetterFromCsv::getHistory);
        FilePlan m = new FilePlan(counter);
        WeatherRegion region = new WeatherRegion(NAME, 10, m);

        /* Insert elements in empty cache */
        List<WeatherInfo> infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 5));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 1);

        infos = region.getHistory(LocalDate.of(2016, 3, 6),
                LocalDate.of(2016, 3, 7));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 2);
    }

    @Test
    public void check_multiple_last() {
        new File(String.format("src/main/resources/data/history_%s.csv", NAME)).delete();
        FunctionCounter<HistoryArgs, List<WeatherInfo>> counter
                = new FunctionCounter<>(WeatherHttpGetterFromCsv::getHistory);
        FilePlan m = new FilePlan(counter);
        WeatherRegion region = new WeatherRegion(NAME, 10, m);

        /* Insert elements in empty cache */
        List<WeatherInfo> infos = region.getHistory(LocalDate.of(2016, 3, 6),
                LocalDate.of(2016, 3, 10));

        Assert.assertEquals(5, infos.size());
        Assert.assertEquals(counter.getCount(), 1);

        infos = region.getHistory(LocalDate.of(2016, 3, 9),
                LocalDate.of(2016, 3, 10));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 1);
    }

    @Test
    public void check_last() {
        new File(String.format("src/main/resources/data/history_%s.csv", NAME)).delete();
        FunctionCounter<HistoryArgs, List<WeatherInfo>> counter
                = new FunctionCounter<>(WeatherHttpGetterFromCsv::getHistory);
        FilePlan m = new FilePlan(counter);
        WeatherRegion region = new WeatherRegion(NAME, 10, m);

        /* Insert elements in empty cache */
        List<WeatherInfo> infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 5));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 1);

        infos = region.getHistory(LocalDate.of(2016, 3, 5),
                LocalDate.of(2016, 3, 5));

        Assert.assertEquals(1, infos.size());
        Assert.assertEquals(counter.getCount(), 1);
    }

    @Test
    public void insert_multiple_before_with_overlapping() {
        new File(String.format("src/main/resources/data/history_%s.csv", NAME)).delete();
        FunctionCounter<HistoryArgs, List<WeatherInfo>> counter
                = new FunctionCounter<>(WeatherHttpGetterFromCsv::getHistory);
        FilePlan m = new FilePlan(counter);
        WeatherRegion region = new WeatherRegion(NAME, 10, m);

        /* Insert elements in empty cache */
        List<WeatherInfo> infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 5));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 1);

        infos = region.getHistory(LocalDate.of(2016, 3, 2),
                LocalDate.of(2016, 3, 4));

        Assert.assertEquals(3, infos.size());
        Assert.assertEquals(counter.getCount(), 2);
    }

    @Test
    public void insert_multiple_before_without_overlapping() {
        new File(String.format("src/main/resources/data/history_%s.csv", NAME)).delete();
        FunctionCounter<HistoryArgs, List<WeatherInfo>> counter
                = new FunctionCounter<>(WeatherHttpGetterFromCsv::getHistory);
        FilePlan m = new FilePlan(counter);
        WeatherRegion region = new WeatherRegion(NAME, 10, m);

        /* Insert elements in empty cache */
        List<WeatherInfo> infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 5));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 1);

        infos = region.getHistory(LocalDate.of(2016, 3, 2),
                LocalDate.of(2016, 3, 3));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 2);
    }

    @Test
    public void insert_one_before() {
        new File(String.format("src/main/resources/data/history_%s.csv", NAME)).delete();
        FunctionCounter<HistoryArgs, List<WeatherInfo>> counter
                = new FunctionCounter<>(WeatherHttpGetterFromCsv::getHistory);
        FilePlan m = new FilePlan(counter);
        WeatherRegion region = new WeatherRegion(NAME, 10, m);

        /* Insert elements in empty cache */
        List<WeatherInfo> infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 5));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 1);

        infos = region.getHistory(LocalDate.of(2016, 3, 3),
                LocalDate.of(2016, 3, 3));

        Assert.assertEquals(1, infos.size());
        Assert.assertEquals(counter.getCount(), 2);
    }

    @Test
    public void check_first() {
        new File(String.format("src/main/resources/data/history_%s.csv", NAME)).delete();
        FunctionCounter<HistoryArgs, List<WeatherInfo>> counter
                = new FunctionCounter<>(WeatherHttpGetterFromCsv::getHistory);
        FilePlan m = new FilePlan(counter);
        WeatherRegion region = new WeatherRegion(NAME, 10, m);

        /* Insert elements in empty cache */
        List<WeatherInfo> infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 5));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 1);

        infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 4));

        Assert.assertEquals(1, infos.size());
        Assert.assertEquals(counter.getCount(), 1);
    }

    @Test
    public void check_entire_cache() {
        new File(String.format("src/main/resources/data/history_%s.csv", NAME)).delete();
        FunctionCounter<HistoryArgs, List<WeatherInfo>> counter
                = new FunctionCounter<>(WeatherHttpGetterFromCsv::getHistory);
        FilePlan m = new FilePlan(counter);
        WeatherRegion region = new WeatherRegion(NAME, 10, m);

        /* Insert elements in empty cache */
        List<WeatherInfo> infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 5));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 1);

        infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 5));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 1);
    }

    @Test
    public void general_tests() {
        new File(String.format("src/main/resources/data/history_%s.csv", NAME)).delete();
        FunctionCounter<HistoryArgs, List<WeatherInfo>> counter
                = new FunctionCounter<>(WeatherHttpGetterFromCsv::getHistory);
        FilePlan m = new FilePlan(counter);
        WeatherRegion region =  new WeatherRegion(NAME, 10, m);

        /* Insert elements in empty cache */
        List<WeatherInfo> infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 5));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 1);

        /* Insert multiple elements at end of cache (with overlapping) */
        infos = region.getHistory(LocalDate.of(2016, 3, 5),
                LocalDate.of(2016, 3, 7));

        Assert.assertEquals(3, infos.size());
        Assert.assertEquals(counter.getCount(), 2);

        /* Check for entire cache */
        infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 7));

        Assert.assertEquals(4, infos.size());
        Assert.assertEquals(counter.getCount(), 2);

        /* Check for first element of cache */
        infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 4));

        Assert.assertEquals(1, infos.size());
        Assert.assertEquals(counter.getCount(), 2);

        /* Check for last element of cache */
        infos = region.getHistory(LocalDate.of(2016, 3, 7),
                LocalDate.of(2016, 3, 7));

        Assert.assertEquals(1, infos.size());
        Assert.assertEquals(counter.getCount(), 2);

        /* Insert multiple elements at end of cache(no overlapping) */
        infos = region.getHistory(LocalDate.of(2016, 3, 8),
                LocalDate.of(2016, 3, 9));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 3);

        /*Insert multiple elements at beginning of cache(with overlapping) */
        infos = region.getHistory(LocalDate.of(2016, 3, 2),
                LocalDate.of(2016, 3, 4));

        Assert.assertEquals(3, infos.size());
        Assert.assertEquals(counter.getCount(), 4);

        /*Insert one element at beginning of cache */
        infos = region.getHistory(LocalDate.of(2016, 3, 1),
                LocalDate.of(2016, 3, 1));

        Assert.assertEquals(1, infos.size());
        Assert.assertEquals(counter.getCount(), 5);

        /* Insert One element at end of cache */
        infos = region.getHistory(LocalDate.of(2016, 3, 10),
                LocalDate.of(2016, 3, 10));

        Assert.assertEquals(1, infos.size());
        Assert.assertEquals(counter.getCount(), 6);
    }
}
