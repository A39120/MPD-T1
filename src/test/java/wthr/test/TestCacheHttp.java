package wthr.test;

import org.junit.Assert;
import org.junit.Test;
import wthr.MemoryPlan;
import wthr.WeatherHttpGetterFromCsv;
import wthr.model.HistoryArgs;
import wthr.model.WeatherInfo;
import wthr.model.WeatherRegion;

import java.time.LocalDate;
import java.util.List;

public class TestCacheHttp {

    @Test
    public void check_with_multiple_gaps() {
        FunctionCounter<HistoryArgs, List<WeatherInfo>> counter
                = new FunctionCounter<>(WeatherHttpGetterFromCsv::getHistory);
        MemoryPlan m = new MemoryPlan(counter);
        WeatherRegion region = new WeatherRegion("Lisboa", 10, m);

        /* Insert elements in empty cache */
        List<WeatherInfo> infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 5));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 1);
        Assert.assertEquals(2, m.getCacheSize());

        infos = region.getHistory(LocalDate.of(2016, 3, 8),
                LocalDate.of(2016, 3, 9));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 2);
        Assert.assertEquals(4, m.getCacheSize());

        infos = region.getHistory(LocalDate.of(2016, 3, 11),
                LocalDate.of(2016, 3, 12));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 3);
        Assert.assertEquals(6, m.getCacheSize());

        infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 12));

        Assert.assertEquals(9, infos.size());
        Assert.assertEquals(counter.getCount(), 4);
        Assert.assertEquals(9, m.getCacheSize());
    }

    @Test
    public void check_with_big_gap() {
        FunctionCounter<HistoryArgs, List<WeatherInfo>> counter
                = new FunctionCounter<>(WeatherHttpGetterFromCsv::getHistory);
        MemoryPlan m = new MemoryPlan(counter);
        WeatherRegion region = new WeatherRegion("Lisboa", 10, m);

        /* Insert elements in empty cache */
        List<WeatherInfo> infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 5));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 1);
        Assert.assertEquals(2, m.getCacheSize());

        infos = region.getHistory(LocalDate.of(2016, 3, 8),
                LocalDate.of(2016, 3, 9));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 2);
        Assert.assertEquals(4, m.getCacheSize());

        infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 9));

        Assert.assertEquals(6, infos.size());
        Assert.assertEquals(counter.getCount(), 3);
        Assert.assertEquals(6, m.getCacheSize());
    }

    @Test
    public void check_with_single_gap() {
        FunctionCounter<HistoryArgs, List<WeatherInfo>> counter
                = new FunctionCounter<>(WeatherHttpGetterFromCsv::getHistory);
        MemoryPlan m = new MemoryPlan(counter);
        WeatherRegion region = new WeatherRegion("Lisboa", 10, m);

        /* Insert elements in empty cache */
        List<WeatherInfo> infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 5));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 1);
        Assert.assertEquals(2, m.getCacheSize());

        infos = region.getHistory(LocalDate.of(2016, 3, 7),
                LocalDate.of(2016, 3, 8));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 2);
        Assert.assertEquals(4, m.getCacheSize());

        infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 8));

        Assert.assertEquals(5, infos.size());
        Assert.assertEquals(counter.getCount(), 3);
        Assert.assertEquals(5, m.getCacheSize());
    }

    @Test
    public void check_after_no_overlapping() {
        FunctionCounter<HistoryArgs, List<WeatherInfo>> counter
                = new FunctionCounter<>(WeatherHttpGetterFromCsv::getHistory);
        MemoryPlan m = new MemoryPlan(counter);
        WeatherRegion region = new WeatherRegion("Lisboa", 10, m);

        /* Insert elements in empty cache */
        List<WeatherInfo> infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 5));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 1);
        Assert.assertEquals(2, m.getCacheSize());

        infos = region.getHistory(LocalDate.of(2016, 3, 6),
                LocalDate.of(2016, 3, 7));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 2);
        Assert.assertEquals(4, m.getCacheSize());
    }

    @Test
    public void check_multiple_last() {
        FunctionCounter<HistoryArgs, List<WeatherInfo>> counter
                = new FunctionCounter<>(WeatherHttpGetterFromCsv::getHistory);
        MemoryPlan m = new MemoryPlan(counter);
        WeatherRegion region = new WeatherRegion("Lisboa", 10, m);

        /* Insert elements in empty cache */
        List<WeatherInfo> infos = region.getHistory(LocalDate.of(2016, 3, 6),
                LocalDate.of(2016, 3, 10));

        Assert.assertEquals(5, infos.size());
        Assert.assertEquals(counter.getCount(), 1);
        Assert.assertEquals(5, m.getCacheSize());

        infos = region.getHistory(LocalDate.of(2016, 3, 9),
                LocalDate.of(2016, 3, 10));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 1);
        Assert.assertEquals(5, m.getCacheSize());
    }

    @Test
    public void check_last() {
        FunctionCounter<HistoryArgs, List<WeatherInfo>> counter
                = new FunctionCounter<>(WeatherHttpGetterFromCsv::getHistory);
        MemoryPlan m = new MemoryPlan(counter);
        WeatherRegion region = new WeatherRegion("Lisboa", 10, m);

        /* Insert elements in empty cache */
        List<WeatherInfo> infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 5));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 1);
        Assert.assertEquals(2, m.getCacheSize());

        infos = region.getHistory(LocalDate.of(2016, 3, 5),
                LocalDate.of(2016, 3, 5));

        Assert.assertEquals(1, infos.size());
        Assert.assertEquals(counter.getCount(), 1);
        Assert.assertEquals(2, m.getCacheSize());
    }

    @Test
    public void insert_multiple_before_with_overlapping() {
        FunctionCounter<HistoryArgs, List<WeatherInfo>> counter
                = new FunctionCounter<>(WeatherHttpGetterFromCsv::getHistory);
        MemoryPlan m = new MemoryPlan(counter);
        WeatherRegion region = new WeatherRegion("Lisboa", 10, m);

        /* Insert elements in empty cache */
        List<WeatherInfo> infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 5));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 1);
        Assert.assertEquals(2, m.getCacheSize());

        infos = region.getHistory(LocalDate.of(2016, 3, 2),
                LocalDate.of(2016, 3, 4));

        Assert.assertEquals(3, infos.size());
        Assert.assertEquals(counter.getCount(), 2);
        Assert.assertEquals(4, m.getCacheSize());
    }

    @Test
    public void insert_multiple_before_without_overlapping() {
        FunctionCounter<HistoryArgs, List<WeatherInfo>> counter
                = new FunctionCounter<>(WeatherHttpGetterFromCsv::getHistory);
        MemoryPlan m = new MemoryPlan(counter);
        WeatherRegion region = new WeatherRegion("Lisboa", 10, m);

        /* Insert elements in empty cache */
        List<WeatherInfo> infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 5));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 1);
        Assert.assertEquals(2, m.getCacheSize());

        infos = region.getHistory(LocalDate.of(2016, 3, 2),
                LocalDate.of(2016, 3, 3));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 2);
        Assert.assertEquals(4, m.getCacheSize());
    }

    @Test
    public void insert_one_before() {
        FunctionCounter<HistoryArgs, List<WeatherInfo>> counter
                = new FunctionCounter<>(WeatherHttpGetterFromCsv::getHistory);
        MemoryPlan m = new MemoryPlan(counter);
        WeatherRegion region = new WeatherRegion("Lisboa", 10, m);

        /* Insert elements in empty cache */
        List<WeatherInfo> infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 5));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 1);
        Assert.assertEquals(2, m.getCacheSize());

        infos = region.getHistory(LocalDate.of(2016, 3, 3),
                LocalDate.of(2016, 3, 3));

        Assert.assertEquals(1, infos.size());
        Assert.assertEquals(counter.getCount(), 2);
        Assert.assertEquals(3, m.getCacheSize());
    }

    @Test
    public void check_first() {
        FunctionCounter<HistoryArgs, List<WeatherInfo>> counter
                = new FunctionCounter<>(WeatherHttpGetterFromCsv::getHistory);
        MemoryPlan m = new MemoryPlan(counter);
        WeatherRegion region = new WeatherRegion("Lisboa", 10, m);

        /* Insert elements in empty cache */
        List<WeatherInfo> infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 5));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 1);
        Assert.assertEquals(2, m.getCacheSize());

        infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 4));

        Assert.assertEquals(1, infos.size());
        Assert.assertEquals(counter.getCount(), 1);
        Assert.assertEquals(2, m.getCacheSize());
    }

    @Test
    public void check_entire_cache() {
        FunctionCounter<HistoryArgs, List<WeatherInfo>> counter
                = new FunctionCounter<>(WeatherHttpGetterFromCsv::getHistory);
        MemoryPlan m = new MemoryPlan(counter);
        WeatherRegion region = new WeatherRegion("Lisboa", 10, m);

        /* Insert elements in empty cache */
        List<WeatherInfo> infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 5));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 1);
        Assert.assertEquals(2, m.getCacheSize());

        infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 5));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 1);
        Assert.assertEquals(2, m.getCacheSize());
    }

    @Test
    public void general_tests() {
        FunctionCounter<HistoryArgs, List<WeatherInfo>> counter
                = new FunctionCounter<>(WeatherHttpGetterFromCsv::getHistory);
        MemoryPlan m = new MemoryPlan(counter);
        WeatherRegion region =  new WeatherRegion("Lisboa", 10, m);

        /* Insert elements in empty cache */
        List<WeatherInfo> infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 5));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 1);
        Assert.assertEquals(2, m.getCacheSize());

        /* Insert multiple elements at end of cache (with overlapping) */
        infos = region.getHistory(LocalDate.of(2016, 3, 5),
                LocalDate.of(2016, 3, 7));

        Assert.assertEquals(3, infos.size());
        Assert.assertEquals(counter.getCount(), 2);
        Assert.assertEquals(4, m.getCacheSize());

        /* Check for entire cache */
        infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 7));

        Assert.assertEquals(4, infos.size());
        Assert.assertEquals(counter.getCount(), 2);
        Assert.assertEquals(4, m.getCacheSize());

        /* Check for first element of cache */
        infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 4));

        Assert.assertEquals(1, infos.size());
        Assert.assertEquals(counter.getCount(), 2);
        Assert.assertEquals(4, m.getCacheSize());

        /* Check for last element of cache */
        infos = region.getHistory(LocalDate.of(2016, 3, 7),
                LocalDate.of(2016, 3, 7));

        Assert.assertEquals(1, infos.size());
        Assert.assertEquals(counter.getCount(), 2);
        Assert.assertEquals(4, m.getCacheSize());

        /* Insert multiple elements at end of cache(no overlapping) */
        infos = region.getHistory(LocalDate.of(2016, 3, 8),
                LocalDate.of(2016, 3, 9));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.getCount(), 3);
        Assert.assertEquals(6, m.getCacheSize());

        /*Insert multiple elements at beginning of cache(with overlapping) */
        infos = region.getHistory(LocalDate.of(2016, 3, 2),
                LocalDate.of(2016, 3, 4));

        Assert.assertEquals(3, infos.size());
        Assert.assertEquals(counter.getCount(), 4);
        Assert.assertEquals(8, m.getCacheSize());

        /*Insert one element at beginning of cache */
        infos = region.getHistory(LocalDate.of(2016, 3, 1),
                LocalDate.of(2016, 3, 1));

        Assert.assertEquals(1, infos.size());
        Assert.assertEquals(counter.getCount(), 5);
        Assert.assertEquals(9, m.getCacheSize());

        /* Insert One element at end of cache */
        infos = region.getHistory(LocalDate.of(2016, 3, 10),
                LocalDate.of(2016, 3, 10));

        Assert.assertEquals(1, infos.size());
        Assert.assertEquals(counter.getCount(), 6);
        Assert.assertEquals(10, m.getCacheSize());
    }

}
