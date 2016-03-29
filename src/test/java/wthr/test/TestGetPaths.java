package wthr.test;

import util.FileGetter;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import wthr.*;
import wthr.model.WeatherInfo;
import wthr.model.WeatherRegion;

import java.io.File;

/**
 * Created by João on 24/03/2016.
 */
public class TestGetPaths {

    @Test
    public void get_paths_from_data_resources1(){
        boolean isFileOrDirectory = FileGetter.listPath("src/main/resources/data/");
        Assert.assertTrue(isFileOrDirectory);
    }


}
