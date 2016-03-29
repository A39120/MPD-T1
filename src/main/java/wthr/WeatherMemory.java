package wthr;

import wthr.model.WeatherInfo;
import wthr.model.WeatherRegion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by João on 24/03/2016.
 */
public class WeatherMemory {

    private static HashMap<WeatherRegion, List<WeatherInfo>> memoryData;

    public WeatherMemory(){
        memoryData = new HashMap<>();
    }

    public void put(WeatherRegion wr, List<WeatherInfo> list){
        memoryData.put(wr,list);
    }

    public static HashMap<WeatherRegion, List<WeatherInfo>> getMemoryData() {
        return memoryData;
    }
}
