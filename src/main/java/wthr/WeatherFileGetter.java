package wthr;

import util.FileGetter;
import wthr.model.HistoryArgs;
import wthr.model.WeatherInfo;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class WeatherFileGetter {

    private static final String RESOURCES_PATH = "src/main/resources/data/";

    private static final String NAME_FORMAT = "%s_%s_%s.info";

    public static List<WeatherInfo> getHistory(HistoryArgs args){
        return getHistory(args.name, args.start, args.end);
    }

    public static List<WeatherInfo> getHistory(String location, LocalDate start, LocalDate end) {
        String uri = RESOURCES_PATH + String.format(NAME_FORMAT, location, start, end);
        try {
            return FileGetter.fileGet(uri, WeatherHttpGetterFromCsv::parseWeatherInfo);
        } catch (IOException e) {
            throw new Error(e);
        }
    }
}
