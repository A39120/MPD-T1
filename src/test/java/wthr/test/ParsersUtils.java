package wthr.test;

import wthr.model.WeatherInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by João on 02/04/2016.
 */
public class ParsersUtils {

    public static List<String> HttpReader(BufferedReader reader, String last){
        List<String> res = new ArrayList<>();
        try {
            last = reader.readLine();
            while((last = reader.readLine()) != null) {
                //last = reader.readLine();
                //WeatherInfo info = WeatherInfo.valueOf(last);
                res.add(last);
                last = reader.readLine();
            }
        } catch (IOException e) {
            throw new Error(e);
        }
        return res;
    }

    public static List<String> fileReader(BufferedReader reader, String last){
        List<String> res = new ArrayList<>();
        try {
            while(last != null) {
                res.add(last);
                last = reader.readLine();
            }
        } catch (IOException e) {
            throw new Error(e);
        }
        return res;
    }
}
