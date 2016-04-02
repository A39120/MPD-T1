package util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Created by João on 02/04/2016.
 */
public class FileUtils {

    public static boolean fileExist(String filePath){
        File f = new File(filePath);
        return(f.exists() && !f.isDirectory());
    }

    public static Optional<List<String>> checkFiesWith(String filePath, Predicate<String> criteria){
        //Pode ser usado para ver ficheiros de uma certa região/data
        File folder = new File(filePath);
        File[] listOfFiles = folder.listFiles();
        List<String> list = new ArrayList<>();

        for (int i = 0; i < listOfFiles.length; i++) {
            String filename = listOfFiles[i].getName();
            if(criteria.test(filename)) list.add(filename);
        }

        if(list.isEmpty()) return Optional.empty();
        return Optional.of(list);
    }
}
