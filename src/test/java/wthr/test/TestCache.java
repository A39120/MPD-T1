package wthr.test;

import org.junit.Assert;
import org.junit.Test;
import wthr.MemoryManager;
import wthr.WeatherHttpGetterFromCsv;
import wthr.model.HistoryArgs;
import wthr.model.WeatherInfo;
import wthr.model.WeatherRegion;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

public class TestCache {

    @Test
    public void simple_test() throws ParseException {
//        List<WeatherInfo> infos = new ArrayList<>();
//        infos.add(new WeatherInfo(LocalDate.of(2016, 04, 01), 4, "Muita", 2.16, 3));
//        infos.add(new WeatherInfo(LocalDate.of(2016, 04, 02), 3, "Muita", 2.16, 3));
//        infos.add(new WeatherInfo(LocalDate.of(2016, 04, 03), 5, "Muita", 2.16, 3));
        MemoryManager m = new MemoryManager(WeatherHttpGetterFromCsv::getHistory);
        HistoryArgs ha1, ha2;
        ha1 = new HistoryArgs("Lisboa",
                LocalDate.of(2016, 4, 1),
                LocalDate.of(2016, 4, 3));
        ha2 = new HistoryArgs("Lisboa",
                LocalDate.of(2016, 4, 1),
                LocalDate.of(2016, 4, 3));
        WeatherRegion region =  new WeatherRegion("Lisboa", 10, m);
        List<WeatherInfo> infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 6));
        Assert.assertEquals(3, infos.size());
        infos = region.getHistory(LocalDate.of(2016, 3, 5),
                LocalDate.of(2016, 3, 7));
        Assert.assertEquals(3, infos.size());
        infos = region.getHistory(LocalDate.of(2016, 3, 4),
                LocalDate.of(2016, 3, 7));
        Assert.assertEquals(4, infos.size());
        infos = region.getHistory(LocalDate.of(2016, 3, 8),
                LocalDate.of(2016, 3, 9));
        Assert.assertEquals(2, infos.size());
        //only one element and before everything that is cache
        infos = region.getHistory(LocalDate.of(2016, 3, 1),
                LocalDate.of(2016, 3, 4));
        Assert.assertEquals(2, infos.size());
    }
}
