package wthr;

import wthr.model.HistoryArgs;
import wthr.model.WeatherInfo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class MemoryManager implements Function<HistoryArgs, List<WeatherInfo>> {

    private List<WeatherInfo> cache = new ArrayList<>();

    //TODO: List of Getters? Implement our Function?

    @Override
    public List<WeatherInfo> apply(HistoryArgs historyArgs) {
        List<WeatherInfo> infos;
        //get from cache
        infos = getHistory(historyArgs);
        if(infos != null)
            return infos;
        //get from disk
        infos = WeatherFileGetter.getHistory(historyArgs);
        if(infos != null)
            return infos;
        //get from web
        infos = WeatherHttpGetterFromCsv.getHistory(historyArgs);
        return infos;
    }

    private List<WeatherInfo> getHistory(HistoryArgs args){
        int startIndex = binarySearch(args.start);
        //TODO: Nicer date comparasion
        int day = args.start.getDayOfMonth();
        List<WeatherInfo> infos = new ArrayList<>(); //TODO: Allocate space between both dates
        for(int i = startIndex + 1; i < cache.size(); ++i){
            if(cache.get(i).date.getDayOfMonth() == day + 1){
                day = cache.get(i).date.getDayOfMonth();
                infos.add(cache.get(i));
            } // TODO: cache miss euristics
            else
                return null;
        }
        return infos;
    }

    private int binarySearch(LocalDate date){
        //TODO: Tests
        int min = 0;
        int max = cache.size() - 1;
        int i = min + (max - min)/2;
        while(i >= min && i <= max){
            WeatherInfo info = cache.get(i);
            int comparasion = info.date.compareTo(date);
            if(comparasion > 0)
                max = i;
            else if(comparasion < 0)
                min = i;
            else
                return i;

        }
        return -1;
    }
}
