package util;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by João on 31/03/2016.
 */
public class FileWriter {

    public static <T> void fileWrite(String file, List<T> lines,  BiConsumer<BufferedWriter, List<T>> conv) throws IOException {
        try (OutputStream out = new FileOutputStream(file)) {
            try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out))) {
                conv.accept(writer, lines);
            }
        }
    }
}
