package wthr;

import util.FileGetter;
import wthr.model.HistoryArgs;
import wthr.model.WeatherInfo;
import wthr.model.WeatherRegion;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class WeatherFileGetterFromCsv {

    private static final String RESOURCES_PATH = "src/main/resources/data/";

    private static final String NAME_FORMAT_HISTORY = "history_%s.csv";
    private static final String NAME_FORMAT_REGION = "region_%s.csv";


    public static List<WeatherRegion> getRegions(
            String query,
            Function<HistoryArgs, List<WeatherInfo>> historyGetter
    ) throws IOException, ParseException {
        String uri = RESOURCES_PATH + String.format(NAME_FORMAT_REGION, query);
        return FileGetter.fileGet(uri, (reader, line) -> Parsers.parseWeatherRegion(reader, line, historyGetter));
    }

    public static List<WeatherInfo> getHistory(HistoryArgs args){
        return getHistory(args.name, args.start, args.end);
    }

    public static List<WeatherInfo> getHistory(String location, LocalDate start, LocalDate end) {
        // predicate is date being in between the [start..end] interval));
        return getHistory(location,  (i) -> start.compareTo(i.date) * i.date.compareTo(end) >= 0);
    }

    public static List<WeatherInfo> getHistory(String location, Predicate<WeatherInfo> predicate){
        String uri = RESOURCES_PATH + String.format(NAME_FORMAT_HISTORY, location);
        try {
            return FileGetter.fileGet(uri, (br, s) -> Parsers.parseFileWeatherInfo(br, s, predicate));
        } catch (IOException e) {
            throw new Error(e);
        }
    }


}
