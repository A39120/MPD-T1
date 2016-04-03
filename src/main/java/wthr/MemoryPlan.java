package wthr;

import wthr.model.HistoryArgs;
import wthr.model.WeatherInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MemoryPlan implements Function<HistoryArgs, List<WeatherInfo>> {

    private ArrayList<WeatherInfo> cache;
    private Function<HistoryArgs, List<WeatherInfo>> backUp;

    public MemoryPlan(Function<HistoryArgs, List<WeatherInfo>> backUp){
        this.backUp = backUp;
        cache = new ArrayList<>();
    }

    @Override
    public List<WeatherInfo> apply(HistoryArgs historyArgs) {
        return ResourcesManager.managementAlgorithm(historyArgs, cache, backUp, (weatherInfoList)-> { });
    }

    /* Used for unit Tests */
    public int getCacheSize(){
        return cache.size();
    }
}
