package wthr.test;

import org.junit.Assert;
import org.junit.Test;
import wthr.MemoryManager;
import wthr.model.HistoryArgs;
import wthr.model.WeatherInfo;
import wthr.model.WeatherRegion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestCache {

    @Test
    public void simple_test() throws ParseException {
//        List<WeatherInfo> infos = new ArrayList<>();
//        infos.add(new WeatherInfo(LocalDate.of(2016, 04, 01), 4, "Muita", 2.16, 3));
//        infos.add(new WeatherInfo(LocalDate.of(2016, 04, 02), 3, "Muita", 2.16, 3));
//        infos.add(new WeatherInfo(LocalDate.of(2016, 04, 03), 5, "Muita", 2.16, 3));
        MemoryManager m = new MemoryManager();
        HistoryArgs ha1, ha2;
        ha1 = new HistoryArgs("Lisboa",
                LocalDate.of(2016, 4, 1),
                LocalDate.of(2016, 4, 3));
        ha2 = new HistoryArgs("Lisboa",
                LocalDate.of(2016, 4, 1),
                LocalDate.of(2016, 4, 3));
        WeatherRegion region =  new WeatherRegion("Lisboa", 10, new MemoryManager());
        List<WeatherInfo> infos = region.getHistory(LocalDate.of(2016, 2, 1),
                LocalDate.of(2016, 2, 29));
        Assert.assertNull(infos);
    }
}
