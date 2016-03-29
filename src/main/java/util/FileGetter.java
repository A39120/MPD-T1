package util;

import java.io.*;
import java.util.List;
import java.util.function.BiFunction;

public class FileGetter {
    /* TODO: unify with httpGetter */
    public static <T> List<T> fileGet(String uri, BiFunction<BufferedReader, String, List<T>> conv) throws IOException {
        try (InputStream in = new FileInputStream(uri)) {
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                String line;
                while ((line = reader.readLine()).startsWith("#")) ;
                return conv.apply(reader, line);
            }
        }
    }
}
