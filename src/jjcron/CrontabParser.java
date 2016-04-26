package jjcron;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Martin
 */
public class CrontabParser {

    private static final Logger logger = Logger.getLogger(CrontabParser.class.getName());

    private static final int CRONTAB_COLUMNS = 7;
    private static final int SEC_COLUMN = 0;
    private static final int MIN_COLUMN = 1;
    private static final int HOUR_COLUMN = 2;
    private static final int DAY_OF_MONTH_COLUMN = 3;
    private static final int MONTH_COLUMN = 4;
    private static final int DAY_OF_WEEK_COLUMN = 5;
    private static final int CMD_COLUMN = 6;

    private static TaskMetadata parseLine(String line) throws Exception {
        TaskMetadata result = null;
        String[] splitted = line.split(" ");

        if (splitted.length >= CRONTAB_COLUMNS) {
            String sec, min, hour, dayOfMonth, month, dayOfWeek, command;

            sec = splitted[SEC_COLUMN];
            min = splitted[MIN_COLUMN];
            hour = splitted[HOUR_COLUMN];
            dayOfMonth = splitted[DAY_OF_MONTH_COLUMN];
            month = splitted[MONTH_COLUMN];
            dayOfWeek = splitted[DAY_OF_WEEK_COLUMN];

            command = splitted[CMD_COLUMN];
            for (int i = CMD_COLUMN + 1; i < splitted.length; ++i) {
                command += " " + splitted[i];
            }

            try {
                result = new TaskMetadata(new CrontabTime(sec, min, hour, dayOfMonth, month, dayOfWeek), command);
            } catch (Exception e) {
                throw new ParserException(e.getMessage(), e);
            }
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
