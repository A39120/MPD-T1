package util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.function.BiFunction;

public class FileGetter {

    /**
     *
     * @param uri File uri
     * @param conv Bifunction that uses BufferedReader and String to retrieve the objects
     * @param <T> Type of objects in the file
     * @return List of objects found(that can be empty) or null in case file was not found
     * @throws IOException
     */
    public static <T> List<T> fileGet(String uri, BiFunction<BufferedReader, String, List<T>> conv) throws IOException {
        try (InputStream in = new FileInputStream(uri)) {
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                String line;
                while ((line = reader.readLine()).startsWith("#")) ;
                return conv.apply(reader, line);
            }
        }catch (FileNotFoundException e){
            return null;
        }
    }


}
