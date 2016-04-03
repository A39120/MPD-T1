package wthr.test;

import org.junit.Assert;
import org.junit.Test;
import wthr.FilePlan;
import wthr.MemoryPlan;
import wthr.WeatherHttpGetterFromCsv;
import wthr.model.HistoryArgs;
import wthr.model.WeatherInfo;
import wthr.model.WeatherRegion;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

public class TestCacheFileHttp {

    @Test
    public void simple_tests(){
        String nome = "Azambuja";

        Function<HistoryArgs, List<WeatherInfo>> http = WeatherHttpGetterFromCsv::getHistory;
        FunctionCounter<HistoryArgs, List<WeatherInfo>> httpCounter = new FunctionCounter<>(http);

        FilePlan file = new FilePlan(httpCounter);
        FunctionCounter<HistoryArgs, List<WeatherInfo>> fileCounter = new FunctionCounter<>(file);

        MemoryPlan m = new MemoryPlan(fileCounter);

        WeatherRegion region = new WeatherRegion(nome, 14, m);

        /* Maybe more robust? */
        new File(String.format("src/main/resources/data/history_%s.csv", nome)).delete();

        /* Insert elements in empty cache */
        List<WeatherInfo> infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 5));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(1, httpCounter.getCount());


        /* Insert multiple elements at end of cache (with overlapping) */
        infos = region.getHistory(LocalDate.of(2016, 3, 5),
                LocalDate.of(2016, 3, 7));

        Assert.assertEquals(3, infos.size());
        Assert.assertEquals(2, httpCounter.getCount());


        /* Check for entire cache */
        infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 7));

        Assert.assertEquals(4, infos.size());
        Assert.assertEquals(2, httpCounter.getCount());


        /* Check for first element of cache */
        infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 4));

        Assert.assertEquals(1, infos.size());
        Assert.assertEquals(2, httpCounter.getCount());


        /* Check for last element of cache */
        infos = region.getHistory(LocalDate.of(2016, 3, 7),
                LocalDate.of(2016, 3, 7));

        Assert.assertEquals(1, infos.size());
        Assert.assertEquals(2, httpCounter.getCount());


        /* Insert multiple elements at end of cache(no overlapping) */
        infos = region.getHistory(LocalDate.of(2016, 3, 8),
                LocalDate.of(2016, 3, 9));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(3, httpCounter.getCount());


        /*Insert multiple elements at beginning of cache(with overlapping) */
        infos = region.getHistory(LocalDate.of(2016, 3, 2),
                LocalDate.of(2016, 3, 4));

        Assert.assertEquals(3, infos.size());
        Assert.assertEquals(4, httpCounter.getCount());


        /*Insert one element at beginning of cache */
        infos = region.getHistory(LocalDate.of(2016, 3, 1),
                LocalDate.of(2016, 3, 1));

        Assert.assertEquals(1, infos.size());
        Assert.assertEquals(5, httpCounter.getCount());


        /* Insert One element at end of cache */
        infos = region.getHistory(LocalDate.of(2016, 3, 10),
                LocalDate.of(2016, 3, 10));

        Assert.assertEquals(1, infos.size());
        Assert.assertEquals(6, httpCounter.getCount());

        /* ******************* RESET CACHE ******************* */
        httpCounter = new FunctionCounter<>(http);
        fileCounter = new FunctionCounter<>(file);
        m = new MemoryPlan(fileCounter);
        region = new WeatherRegion(nome, 14, m);

        /* Insert elements in empty cache */
        infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 5));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(1, fileCounter.getCount());
        Assert.assertEquals(0, httpCounter.getCount());


        /* Insert multiple elements at end of cache (with overlapping) */
        infos = region.getHistory(LocalDate.of(2016, 3, 5),
                LocalDate.of(2016, 3, 7));

        Assert.assertEquals(3, infos.size());
        Assert.assertEquals(2, fileCounter.getCount());
        Assert.assertEquals(0, httpCounter.getCount());


        /* Check for entire cache */
        infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 7));

        Assert.assertEquals(4, infos.size());
        Assert.assertEquals(2, fileCounter.getCount());
        Assert.assertEquals(0, httpCounter.getCount());


        /* Check for first element of cache */
        infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 4));

        Assert.assertEquals(1, infos.size());
        Assert.assertEquals(2, fileCounter.getCount());
        Assert.assertEquals(0, httpCounter.getCount());


        /* Check for last element of cache */
        infos = region.getHistory(LocalDate.of(2016, 3, 7),
                LocalDate.of(2016, 3, 7));

        Assert.assertEquals(1, infos.size());
        Assert.assertEquals(2, fileCounter.getCount());
        Assert.assertEquals(0, httpCounter.getCount());


        /* Insert multiple elements at end of cache(no overlapping) */
        infos = region.getHistory(LocalDate.of(2016, 3, 8),
                LocalDate.of(2016, 3, 9));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(3, fileCounter.getCount());
        Assert.assertEquals(0, httpCounter.getCount());


        /*Insert multiple elements at beginning of cache(with overlapping) */
        infos = region.getHistory(LocalDate.of(2016, 3, 2),
                LocalDate.of(2016, 3, 4));

        Assert.assertEquals(3, infos.size());
        Assert.assertEquals(4, fileCounter.getCount());
        Assert.assertEquals(0, httpCounter.getCount());


        /*Insert one element at beginning of cache */
        infos = region.getHistory(LocalDate.of(2016, 3, 1),
                LocalDate.of(2016, 3, 1));

        Assert.assertEquals(1, infos.size());
        Assert.assertEquals(5, fileCounter.getCount());
        Assert.assertEquals(0, httpCounter.getCount());


        /* Insert One element at end of cache */
        infos = region.getHistory(LocalDate.of(2016, 3, 10),
                LocalDate.of(2016, 3, 10));

        Assert.assertEquals(1, infos.size());
        Assert.assertEquals(6, fileCounter.getCount());
        Assert.assertEquals(0, httpCounter.getCount());
    }

}
