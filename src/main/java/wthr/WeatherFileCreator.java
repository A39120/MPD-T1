package wthr;

import util.FileWriter;
import util.HttpGetter;
import wthr.model.HistoryArgs;

import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Created by João on 01/04/2016.
 */
public class WeatherFileCreator {

    private static final String RESOURCES_PATH = "src/main/resources/data/";
    private static final String NAME_FORMAT = "%s_%s_%s.csv";

    public static <T> void createFile(HistoryArgs ha,List<T> lines/*,
                                      BiConsumer<BufferedWriter, List<T>> writer*/){
        createFile(ha.name, ha.start, ha.end, lines/*, writer*/);
    }

    public static <T> void createFile(String location, LocalDate start, LocalDate end, List<T> lines/*,
                                      BiConsumer<BufferedWriter, List<T>> writer*/){
        String file_path = RESOURCES_PATH + String.format(NAME_FORMAT, location, start.toString(), end.toString());
        try {
            FileWriter.fileWrite(file_path, lines, WeatherFileCreator::writer);
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    public static<T> void writer(BufferedWriter bw, List<T> lines){
        try {
            for(T o : lines){
                if(o == null) break;
                bw.write(o.toString());
                bw.newLine();
            }
        }catch(IOException e){
            throw new Error(e);
        }
    }//TODO: Will receive a list of strings
}
