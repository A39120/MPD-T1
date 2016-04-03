package wthr;

import util.FileCreator;
import wthr.model.HistoryArgs;
import wthr.model.WeatherInfo;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

/**
 * Created by Jo�o on 01/04/2016.
 */
public class WeatherFileCreator {

    private static final String RESOURCES_PATH = "src/main/resources/data/";
    private static final String NAME_FORMAT = "history_%s.csv";

    public static void createFile(HistoryArgs ha, List<WeatherInfo> infos, Function<WeatherInfo, String> parser){
        createFile(ha.name, infos, parser);
    }

    public static void createFile(String location, List<WeatherInfo> infos, Function<WeatherInfo, String> parser){
        String file_path = RESOURCES_PATH + String.format(NAME_FORMAT, location);
        try {
            FileCreator.create(file_path, infos, parser);
        } catch (IOException e) {
            throw new Error(e);
        }
    }
}
