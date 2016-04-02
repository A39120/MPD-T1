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

public class Parsers {
    public static List<WeatherRegion> parseFileWeatherRegion(
            BufferedReader reader,
            String last,
            Function<HistoryArgs, List<WeatherInfo>> historyGetter
    ) {
        List<WeatherRegion> res = new ArrayList<WeatherRegion>();
        while(last != null){
            res.add(WeatherRegion.valueOf(last, historyGetter)); //TODO: Function ?
            try {
                last = reader.readLine();
            } catch (IOException e) {
                throw new Error(e);
            }
        }
        return res;
    }

    public static List<WeatherInfo> parseHttpWeatherInfo(BufferedReader reader, String last) {
        /*List<WeatherInfo> res = new ArrayList<>();
        try {
            while((last = reader.readLine()) != null) {
                last = reader.readLine(); // Skip Not Available or Daily Info
                WeatherInfo info = WeatherInfo.valueOf(last);
                res.add(info);
            }
        } catch (IOException | ParseException e) {
            throw new Error(e);
        }
        return res;*/
        List<WeatherInfo> res = new ArrayList<>();
        try {
            last = reader.readLine();
            while((last = reader.readLine()) != null) {
                //last = reader.readLine();
                WeatherInfo info = WeatherInfo.valueOf(last);
                res.add(info);
                last = reader.readLine();
            }
        } catch (IOException | ParseException e) {
            throw new Error(e);
        }
        return res;

    }


    public static List<WeatherInfo> parseFileWeatherInfo(BufferedReader reader, String last) {
        /*List<WeatherInfo> res = new ArrayList<>();
        try {
            while((last = reader.readLine()) != null){
                WeatherInfo info = WeatherInfo.valueOf(last);
                res.add(info);
            }
        } catch (IOException | ParseException e) {
            throw new Error(e);
        }
        return res;*/
        List<WeatherInfo> res = new ArrayList<>();
        try {
            while(last != null) {
                WeatherInfo info = WeatherInfo.valueOf(last);
                res.add(info);
                last = reader.readLine();
            }
        } catch (IOException | ParseException e) {
            throw new Error(e);
        }
        return res;
    }



}