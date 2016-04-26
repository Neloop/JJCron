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

    private static TaskMetadata parseLine(String line) throws Exception {
        TaskMetadata result = null;
        String[] splitted = line.split(" ");

        if (splitted.length >= 6) {
            String min, hour, dayOfMonth, month, dayOfWeek, command = "";

            min = splitted[0];
            hour = splitted[1];
            dayOfMonth = splitted[2];
            month = splitted[3];
            dayOfWeek = splitted[4];

            command = splitted[5];
            for (int i = 6; i < splitted.length; ++i) {
                command += " " + splitted[i];
            }

            result = new TaskMetadata(new CrontabTime(min, hour, dayOfMonth, month, dayOfWeek), command);
        } else {
            throw new ParserException("Bad crontab line format: too little columns");
        }

        return result;
    }

    private static List<TaskMetadata> internalParse(InputStream input) throws ParserException {
        List<TaskMetadata> result = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                result.add(parseLine(line));
            }

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
