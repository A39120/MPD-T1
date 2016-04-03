package wthr;

import wthr.model.HistoryArgs;
import wthr.model.WeatherInfo;

import java.util.List;
import java.util.function.Function;

public class FilePlan implements Function<HistoryArgs, List<WeatherInfo>> {

    private Function<HistoryArgs, List<WeatherInfo>> backUp;

    public FilePlan(Function<HistoryArgs, List<WeatherInfo>> backUp){
        this.backUp = backUp;
    }

    @Override
    public List<WeatherInfo> apply(HistoryArgs historyArgs) {
        return ResourcesManager.managementAlgorithm(
                historyArgs,
                WeatherFileGetterFromCsv.getHistory(historyArgs.name, (i) -> true),
                backUp,
                (infos) -> WeatherFileCreator.createFile(historyArgs, infos, Parsers::parseWeatherInfoToCsv));
    }

}
