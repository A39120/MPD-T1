package wthr.test;


import org.junit.Assert;
import org.junit.Test;
import util.FileGetter;
import util.HttpGetter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

/**
 * Created by João on 02/04/2016.
 */
public class TestGetFile {

    class CounterFuncCall<T,E, R> implements BiFunction<T,E, R> {
        private final BiFunction<T, E, R> handler;
        int nr;

        CounterFuncCall(BiFunction<T, E, R> handler) {
            this.handler = handler;
        }

        @Override
        public R apply(T t, E e) {
            nr++;
            return handler.apply(t, e);
        }
    }

    @Test
    public void testGetFileWithFewLines() throws IOException{

        BiFunction<BufferedReader, String, List<String>> w = TestGetFile::fileReader;
        CounterFuncCall<BufferedReader, String, List<String>> cfc = new CounterFuncCall(w);

        List<String> allLines = FileGetter.fileGet("src/test/resources/data/lisbon-weather-history.csv",
                cfc
                );

        Assert.assertTrue(!allLines.isEmpty());
        Assert.assertEquals(cfc.nr, 1);
    }


    public static List<String> fileReader(BufferedReader bufferedReader, String line){
        List<String> lines = new ArrayList<>();
        try{
            while((line = bufferedReader.readLine()) != null){
                lines.add(line);
            }
        }catch(IOException e){
            throw new Error(e);
        }
        return lines;
    }

    public static List<String> httpReader(BufferedReader bufferedReader, String line){
        List<String> lines = new ArrayList<>();
        try{
            while((line = bufferedReader.readLine()) != null){
                line = bufferedReader.readLine();
                lines.add(line);
            }
        }catch(IOException e){
            throw new Error(e);
        }
        return lines;
    }

    @Test
    public void testGetFileWithManyLines() throws IOException {
        List<String> actual = FileGetter.fileGet("src/test/resources/data/Lisbon_2016-03-01_2016-03-10.csv", ParsersUtils::fileReader);
        List<String> expected = HttpGetter.httpGet("http://api.worldweatheronline.com/" +
                "free/v2/past-weather.ashx?key=25781444d49842dc5be040ff259c5" +
                "&q=Lisbon&format=csv" +
                "&date=2016-03-01" +
                "&enddate=2016-03-10" +
                "&tp=24", ParsersUtils::HttpReader);

        Assert.assertEquals(expected, actual);
    }


}
