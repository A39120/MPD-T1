package wthr;

import wthr.model.HistoryArgs;
import wthr.model.WeatherInfo;
import wthr.model.WeatherRegion;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class Parsers {

    public static List<WeatherRegion> parseWeatherRegion(
            BufferedReader reader,
            String last,
            Function<HistoryArgs, List<WeatherInfo>> historyGetter
    ) {
        List<WeatherRegion> res = new ArrayList<WeatherRegion>();
        while(last != null){
            res.add(WeatherRegion.valueOf(last, historyGetter));
            try {
                last = reader.readLine();
            } catch (IOException e) {
                throw new Error(e);
            }
        }
        return res;
    }

    public static List<WeatherInfo> parseWeatherInfo(BufferedReader reader, String last) {
        return parseWeatherInfo(reader, last, (i)->true);
    }

    public static List<WeatherInfo> parseWeatherInfo(BufferedReader reader, String last, Predicate<WeatherInfo> p) {
        List<WeatherInfo> res = new ArrayList<>();
        try {
            while(last != null) {
                WeatherInfo info = WeatherInfo.valueOf(last);
                if(p.test(info))
                    res.add(info);
                last = reader.readLine();
            }
        } catch (IOException | ParseException e) {
            throw new Error(e);
        }
        return res;
    }



}