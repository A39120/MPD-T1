package wthr.test;

import org.junit.Assert;
import org.junit.Test;
import util.FileUtils;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Created by João on 02/04/2016.
 */
public class TestFileUtils {

    @Test
    public void checkIfFileExists(){
        Assert.assertTrue(FileUtils.fileExist("src\\test\\resources\\data\\lisbon-date1-date2.csv"));
        Assert.assertTrue(FileUtils.fileExist("src\\test\\resources\\data\\lisbon-weather-history.csv"));
        Assert.assertTrue(FileUtils.fileExist("src\\test\\resources\\data\\lisbon-weather-history1.csv"));
        Assert.assertTrue(FileUtils.fileExist("src\\test\\resources\\data\\porto-date1-date2.csv"));
    }

    @Test
    public void checkFileDoesNotExist(){
        Assert.assertFalse(FileUtils.fileExist("src\\test\\resources\\data\\novayork-date1-date2.csv"));
        Assert.assertFalse(FileUtils.fileExist("src\\test\\resources\\data\\barcelona.csv"));
        Assert.assertFalse(FileUtils.fileExist("src\\test\\resources\\data\\faro.csv"));
        Assert.assertFalse(FileUtils.fileExist("src\\test\\resources\\data\\braga.csv"));
    }

    class PredicateCounter<String> implements Predicate<String>{
        private final Predicate<String> handler;
        public int nr;
        public PredicateCounter(Predicate<String> handler){
            this.handler = handler;
            this.nr = 0;
        }
        @Override
        public boolean test(String string) {
            nr++;
            return handler.test(string);
        }
    }

    @Test
    public void checkFileThatStartWithLisbon(){
        Predicate<String> stringsThatStartWithLisbon = (s) -> s.startsWith("lisbon");
        PredicateCounter<String> predicateCounter = new PredicateCounter<>(stringsThatStartWithLisbon);
        Optional<List<String>> op = FileUtils.checkFiesWith("src\\test\\resources\\data", predicateCounter);

        List<String> list = op.get();
        Assert.assertFalse(list.isEmpty());
        Assert.assertEquals(5, predicateCounter.nr);
        Assert.assertTrue(list.contains("lisbon-date1-date2.csv"));
        Assert.assertTrue(list.contains("lisbon-weather-history.csv"));
        Assert.assertTrue(list.contains("lisbon-weather-history1.csv"));
    }

    @Test
    public void checkNonexistingFile(){
        Predicate<String> stringsThatStartWithBarcelona = (s) -> s.startsWith("barcelona");
        PredicateCounter<String> predicateCounter = new PredicateCounter<>(stringsThatStartWithBarcelona);
        Optional<List<String>> op = FileUtils.checkFiesWith("src\\test\\resources\\data", predicateCounter);

        Assert.assertFalse(op.isPresent());
        Assert.assertEquals(5, predicateCounter.nr);
    }
}
