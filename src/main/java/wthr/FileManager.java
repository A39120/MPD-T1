package wthr;

import util.Int;
import wthr.model.HistoryArgs;
import wthr.model.WeatherInfo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class FileManager implements Function<HistoryArgs, List<WeatherInfo>> {

    private Function<HistoryArgs, List<WeatherInfo>> backUp;

    public FileManager(Function<HistoryArgs, List<WeatherInfo>> backUp){
        this.backUp = backUp;
    }

    @Override
    public List<WeatherInfo> apply(HistoryArgs historyArgs) {
        List<WeatherInfo> infos = WeatherFileGetterFromCsv.getHistory(historyArgs.name, (i) -> true); //get all infos for easier write after
        if(infos == null){
            infos = new ArrayList<>();
        }
        LocalDate startDate = historyArgs.start;
        LocalDate endDate = historyArgs.end;

        //get Index of first element equal or bigger than startDate
        int startIdx = Queries.indexOf(infos, (w) -> w.date.compareTo(historyArgs.start) >= 0);
        //if there was no element equal or bigger than startDate we will end to beginning of cache
        if(startIdx < 0) {
            startIdx = infos.size();
        }

        int endIdx = Queries.indexOf(infos, (w) -> w.date.compareTo(historyArgs.end) >= 0);
        if(endIdx < 0){
            endIdx = infos.size();
        }

        boolean cacheMissed = false;
        int finalStartIndex = startIdx;

        //Go with start until cacheMiss
        for(; startDate.isBefore(endDate); startDate = startDate.plusDays(1), startIdx += 1) {
            if(startIdx < 0 || startIdx >= infos.size() || !startDate.isEqual(infos.get(startIdx).date)) {
                cacheMissed = true;
                break;
            }
        }

        //Go with end until cacheMiss
        for(; endDate.isAfter(startDate) || endDate.equals(startDate); endDate = endDate.minusDays(1), endIdx -= 1) {
            if(endIdx < 0 || endIdx >= infos.size() || !endDate.isEqual(infos.get(endIdx).date)){
                cacheMissed = true;
                break;
            }
        }

        startIdx = Int.clamp(startIdx, 0, infos.size());
        endIdx = Int.clamp(endIdx, 0, infos.size());

        if(startDate.isBefore(endDate) || cacheMissed) {
            List<WeatherInfo> toAdd = backUp.apply(new HistoryArgs(historyArgs.name, startDate, endDate));
            infos.subList(startIdx, endIdx).clear();
            infos.addAll(startIdx, toAdd);
            WeatherFileCreator.createFile(historyArgs, infos, Parsers::parseWeatherInfoToCsv);
        }

        return infos.subList(finalStartIndex, Queries.indexOf(infos, (i)->i.date.equals(historyArgs.end)) + 1);
    }

}
