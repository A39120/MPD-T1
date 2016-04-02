package wthr;

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
        final LocalDate finalStartDate = startDate;
        int startIdx = Queries.indexOf(cache, (w) -> w.date.compareTo(finalStartDate) >= 0);
        //if there was no element equal or bigger than startDate we will end to beginning of cache
        if(startIdx < 0) {
            startIdx = cache.size();
        }

        final LocalDate finalEndDate = endDate;
        int endIdx = Queries.indexOf(cache, (w) -> w.date.compareTo(finalEndDate) >= 0);
        if(endIdx < 0){
            endIdx = cache.size();
        }

        //Generic search
        boolean cacheMissed = false;

        int finalStartIndex = startIdx;
        //Go with start until cacheMiss
        for(; startDate.isBefore(endDate); startDate = startDate.plusDays(1), startIdx += 1) {
            if(startIdx < 0
                    || startIdx >= cache.size()
                    || !startDate.isEqual(cache.get(startIdx).date)) {
                cacheMissed = true;
                break;
            }
        }

        //Go with end until cacheMiss
        for(; endDate.isAfter(startDate); endDate = endDate.minusDays(1), endIdx -= 1) {
            if(endIdx < 0
                    || endIdx >= cache.size()
                    || !endDate.isEqual(cache.get(endIdx).date)){
                cacheMissed = true;
                break;
            }
        }


        //TODO: Bug with last element, either doesn't appear OR dupplicates (right now)
        //TODO: Also seems to bug with first elements
        if(startDate.isBefore(endDate) || cacheMissed) {
            List<WeatherInfo> toAdd = backUp.apply(new HistoryArgs(historyArgs.name, startDate, endDate));
            cache.subList(startIdx, endIdx).clear();
            cache.addAll(startIdx, toAdd);
        }

        return cache.subList(finalStartIndex, Queries.indexOf(cache, (i)->i.date.equals(finalEndDate)) + 1);
    }

}
