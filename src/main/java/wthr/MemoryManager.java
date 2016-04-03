package wthr;

import util.Int;
import wthr.model.HistoryArgs;
import wthr.model.WeatherInfo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MemoryManager implements Function<HistoryArgs, List<WeatherInfo>> {

    private ArrayList<WeatherInfo> cache;
    private Function<HistoryArgs, List<WeatherInfo>> backUp;

    public MemoryManager(Function<HistoryArgs, List<WeatherInfo>> backUp){
        this.backUp = backUp;
        cache = new ArrayList<>();
    }

    @Override
    public List<WeatherInfo> apply(HistoryArgs historyArgs) {
        LocalDate startDate = historyArgs.start;
        LocalDate endDate = historyArgs.end;

        //get Index of first element equal or bigger than startDate
        int startIdx = Queries.indexOf(cache, (w) -> w.date.compareTo(historyArgs.start) >= 0);
        //if there was no element equal or bigger than startDate we will end to beginning of cache
        if(startIdx < 0) {
            startIdx = cache.size();
        }

        int endIdx = Queries.indexOf(cache, (w) -> w.date.compareTo(historyArgs.end) >= 0);
        if(endIdx < 0){
            endIdx = cache.size();
        }

        //Generic search
        boolean cacheMissed = false;
        int finalStartIndex = startIdx;

        //Go with start until cacheMiss
        for(; startDate.isBefore(endDate); startDate = startDate.plusDays(1), startIdx += 1) {
            if(startIdx < 0 || startIdx >= cache.size() || !startDate.isEqual(cache.get(startIdx).date)) {
                cacheMissed = true;
                break;
            }
        }

        //Go with end until cacheMiss
        for(; endDate.isAfter(startDate) || endDate.equals(startDate); endDate = endDate.minusDays(1), endIdx -= 1) {
            if(endIdx < 0 || endIdx >= cache.size() || !endDate.isEqual(cache.get(endIdx).date)){
                cacheMissed = true;
                break;
            }
        }

        startIdx = Int.clamp(startIdx, 0, cache.size());
        endIdx = Int.clamp(endIdx, 0, cache.size());

        if(startDate.isBefore(endDate) || cacheMissed) {
            List<WeatherInfo> toAdd = backUp.apply(new HistoryArgs(historyArgs.name, startDate, endDate));
            cache.subList(startIdx, endIdx).clear();
            cache.addAll(startIdx, toAdd);
        }

        return cache.subList(finalStartIndex, Queries.indexOf(cache, (i)->i.date.equals(historyArgs.end)) + 1);
    }

    /* Used for unit Tests */
    public int getCacheSize(){
        return cache.size();
    }
}
