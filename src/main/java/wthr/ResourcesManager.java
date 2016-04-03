package wthr;

import util.Int;
import wthr.model.HistoryArgs;
import wthr.model.WeatherInfo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class ResourcesManager {

    static List<WeatherInfo> managementAlgorithm(
            HistoryArgs historyArgs,
            List<WeatherInfo> source,
            Function<HistoryArgs, List<WeatherInfo>> backUp,
            Consumer<List<WeatherInfo>> finalDataConsumer)
    {
        if(source == null){
            source = new ArrayList<>();
        }
        LocalDate startDate = historyArgs.start;
        LocalDate endDate = historyArgs.end;

        //get Index of first element equal or bigger than startDate
        int startIdx = Queries.indexOf(source, (w) -> w.date.compareTo(historyArgs.start) >= 0);
        //if there was no element equal or bigger than startDate we will end to beginning of cache
        if(startIdx < 0) {
            startIdx = source.size();
        }

        int endIdx = Queries.indexOf(source, (w) -> w.date.compareTo(historyArgs.end) >= 0);
        if(endIdx < 0){
            endIdx = source.size();
        }

        boolean cacheMissed = false;
        int finalStartIndex = startIdx;

        //Go with start until cacheMiss
        for(; startDate.isBefore(endDate); startDate = startDate.plusDays(1), startIdx += 1) {
            if(startIdx < 0 || startIdx >= source.size() || !startDate.isEqual(source.get(startIdx).date)) {
                cacheMissed = true;
                break;
            }
        }

        //Go with end until cacheMiss
        for(; endDate.isAfter(startDate) || endDate.equals(startDate); endDate = endDate.minusDays(1), endIdx -= 1) {
            if(endIdx < 0 || endIdx >= source.size() || !endDate.isEqual(source.get(endIdx).date)){
                cacheMissed = true;
                break;
            }
        }

        startIdx = Int.clamp(startIdx, 0, source.size());
        endIdx = Int.clamp(endIdx, 0, source.size());

        if(startDate.isBefore(endDate) || cacheMissed) {
            List<WeatherInfo> toAdd = backUp.apply(new HistoryArgs(historyArgs.name, startDate, endDate));
            source.subList(startIdx, endIdx).clear();
            source.addAll(startIdx, toAdd);
            finalDataConsumer.accept(source);
        }

        return source.subList(finalStartIndex, Queries.indexOf(source, (i)->i.date.equals(historyArgs.end)) + 1);
    }
}
