package jjcron;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Martin
 */
public class CrontabParser {

    private static List<TaskMetadata> internalParse(InputStream input) throws ParserException {
        List<TaskMetadata> result = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        try {
        } catch (Exception e) {
            throw new ParserException(e.getMessage(), e);
        }

        return result;
    }

    public static List<TaskMetadata> parseFile(String filename) throws ParserException {
        InputStream file = null;
        try {
            file = new FileInputStream(filename);
        } catch (Exception e) {
            throw new ParserException(e.getMessage(), e);
        }

        return internalParse(file);
    }

    public static List<TaskMetadata> parse(InputStream input) throws ParserException {
        return internalParse(input);
    }

    public static List<TaskMetadata> parse(String input) throws ParserException {
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        return internalParse(inputStream);
    }
}
