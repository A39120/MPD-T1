package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

/**
 * Created by Jo�o on 31/03/2016.
 */
public class FileCreator {

    public static <T> void create(String file, List<T> list, Function<T, String> parser) throws IOException
    {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            for(T item : list){
                writer.write(parser.apply(item));
                writer.newLine();
            }
        }
    }
}
