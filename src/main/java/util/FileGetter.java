package util;

import wthr.model.WeatherInfo;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by João on 24/03/2016.
 */
public class FileGetter {

    String filePath;

    public static <T> List<T> FileGet(String filePath, Function<BufferedReader, List<T>> conv) throws IOException, URISyntaxException {
        /*Path p = Paths.get(ClassLoader.getSystemResource(filePath).toURI());
        Iterator<String> lines = Files.readAllLines(p).iterator();
        while(lines.next().startsWith("#"));
        return conv.apply(lines, new );*/
        try (InputStream in = new FileInputStream(filePath)) {
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                String line;
                while ((line = reader.readLine()).startsWith("#"));
                return conv.apply(reader);
            }
        }
    }

    public static boolean listPath(String path){
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile() || listOfFiles[i].isDirectory()) {
                System.out.println("File " + listOfFiles[i].getName());
                return true;
            }
        }
        return false;
    }
}
