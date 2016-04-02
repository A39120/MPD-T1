package wthr.test;

import org.junit.Assert;
import org.junit.Test;
import util.FileGetter;
import util.FileUtils;
import util.HttpGetter;
import wthr.WeatherFileCreator;
import wthr.model.HistoryArgs;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by João on 02/04/2016.
 */
public class WeatherFileCreatorTest {

    private String file = "%s_%s_%s.csv";
    private String path = "src\\main\\resources\\data\\";
    private String uri =  "http://api.worldweatheronline.com/" +
            "free/v2/past-weather.ashx?key=25781444d49842dc5be040ff259c5" +
            "&q=Lisboa&format=csv" +
            "&date=2016-03-01" +
            "&enddate=2016-03-28" +
            "&tp=24";

    @Test
    public void createFile() throws IOException {

        LocalDate start = LocalDate.of(2016, 3, 1);
        LocalDate end = LocalDate.of(2016, 3, 10);
        String location = "Lisbon";

        HistoryArgs ha = new HistoryArgs(location, start, end);
        String currPath = path+String.format(file,location,start.toString(),end.toString());
        if(FileUtils.fileExist(currPath)){
            new File(currPath).delete();
        }

        List<String> expected = HttpGetter.httpGet(uri, ParsersUtils::HttpReader);

        WeatherFileCreator.createFile(ha, expected);
        Assert.assertTrue(FileUtils.fileExist(currPath));
        List<String> actual = FileGetter.fileGet(currPath, ParsersUtils::fileReader);
        Assert.assertEquals(actual, expected);

        if(FileUtils.fileExist(currPath))
            new File(currPath).delete();
    }


}
