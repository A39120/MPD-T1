package wthr.test;

import org.junit.Assert;
import org.junit.Test;
import wthr.MemoryManager;
import wthr.WeatherFileGetterFromCsv;
import wthr.model.HistoryArgs;
import wthr.model.WeatherInfo;
import wthr.model.WeatherRegion;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

public class TestCache {

    private class FunctionCounter<T, R> implements Function<T, R> {

        private int count;
        private Function<T, R> countable;

        public FunctionCounter(Function<T, R> countable){
            this.countable = countable;
        }

        @Override
        public R apply(T args) {
            count++;
            return countable.apply(args);
        }
    }

    @Test
    public void simple_test() throws ParseException {
        Function<HistoryArgs, List<WeatherInfo>> fileManager = WeatherFileGetterFromCsv::getHistory;
        FunctionCounter<HistoryArgs, List<WeatherInfo>> counter = new FunctionCounter<>(fileManager);
        MemoryManager m = new MemoryManager(counter);
        WeatherRegion region =  new WeatherRegion("Lisboa", 10, m);

        /* Insert elements in empty cache */
        List<WeatherInfo> infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 5));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.count, 1);
        Assert.assertEquals(2, m.getCacheSize());

        /* Insert multiple elements at end of cache (with overlapping) */
        infos = region.getHistory(LocalDate.of(2016, 3, 5),
                LocalDate.of(2016, 3, 7));

        Assert.assertEquals(3, infos.size());
        Assert.assertEquals(counter.count, 2);
        Assert.assertEquals(4, m.getCacheSize());

        /* Check for entire cache */
        infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 7));

        Assert.assertEquals(4, infos.size());
        Assert.assertEquals(counter.count, 2);
        Assert.assertEquals(4, m.getCacheSize());

        /* Check for first element of cache */
        infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 4));

        Assert.assertEquals(1, infos.size());
        Assert.assertEquals(counter.count, 2);
        Assert.assertEquals(4, m.getCacheSize());

        /* Check for last element of cache */
        infos = region.getHistory(LocalDate.of(2016, 3, 7),
                LocalDate.of(2016, 3, 7));

        Assert.assertEquals(1, infos.size());
        Assert.assertEquals(counter.count, 2);
        Assert.assertEquals(4, m.getCacheSize());

        /* Insert multiple elements at end of cache(no overlapping) */
        infos = region.getHistory(LocalDate.of(2016, 3, 8),
                LocalDate.of(2016, 3, 9));

        Assert.assertEquals(2, infos.size());
        Assert.assertEquals(counter.count, 3);
        Assert.assertEquals(6, m.getCacheSize());

        /*Insert multiple elements at beginning of cache(with overlapping) */
        infos = region.getHistory(LocalDate.of(2016, 3, 2),
                LocalDate.of(2016, 3, 4));

        Assert.assertEquals(3, infos.size());
        Assert.assertEquals(counter.count, 4);
        Assert.assertEquals(8, m.getCacheSize());

        /*Insert one element at beginning of cache */
        infos = region.getHistory(LocalDate.of(2016, 3, 1),
                LocalDate.of(2016, 3, 1));

        Assert.assertEquals(1, infos.size());
        Assert.assertEquals(counter.count, 5);
        Assert.assertEquals(9, m.getCacheSize());

        /* Insert One element at end of cache */
        infos = region.getHistory(LocalDate.of(2016, 3, 10),
                LocalDate.of(2016, 3, 10));

        Assert.assertEquals(1, infos.size());
        Assert.assertEquals(counter.count, 6);
        Assert.assertEquals(10, m.getCacheSize());
    }
}
