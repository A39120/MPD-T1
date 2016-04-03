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
        String uri = RESOURCES_PATH + String.format(NAME_FORMAT_HISTORY, location);
        try {
            return FileGetter.fileGet(uri, (br, s) -> Parsers.parseWeatherInfo(
                    br, s,
                    (i) -> start.compareTo(i.date) * i.date.compareTo(end) >= 0) // date is in [start..end] interval));
            );
        } catch (IOException e) {
            throw new Error(e);
        }
    }


}
