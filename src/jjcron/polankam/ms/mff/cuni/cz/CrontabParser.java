package jjcron.polankam.ms.mff.cuni.cz;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Neloop
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

    private static final String LINE_SPLITTER = " ";
    private static final char COMMENT_DELIM = '#';

    private static String removeComment(String line) {
        String result = line;
        int pos = line.indexOf(COMMENT_DELIM);
        if (pos != -1) {
            result = result.substring(0, pos);
        }

        return result;
    }

    private static TaskMetadata parseLine(int lineNumber, String line) throws ParserException {
        TaskMetadata result = null;
        List<String> splitted = new ArrayList<>(Arrays.asList(line.split(LINE_SPLITTER)));
        splitted.removeAll(Arrays.asList("", null));

        if (splitted.size() >= CRONTAB_COLUMNS) {
            String sec, min, hour, dayOfMonth, month, dayOfWeek, command;

            sec = splitted.get(SEC_COLUMN);
            min = splitted.get(MIN_COLUMN);
            hour = splitted.get(HOUR_COLUMN);
            dayOfMonth = splitted.get(DAY_OF_MONTH_COLUMN);
            month = splitted.get(MONTH_COLUMN);
            dayOfWeek = splitted.get(DAY_OF_WEEK_COLUMN);

            command = splitted.get(CMD_COLUMN);
            for (int i = CMD_COLUMN + 1; i < splitted.size(); ++i) {
                command += " " + splitted.get(i);
            }

            try {
                result = new TaskMetadata(new CrontabTime(sec, min, hour, dayOfMonth, month, dayOfWeek), command);
            } catch (Exception e) {
                throw new ParserException(e.getMessage(), e);
            }
        } else {
            throw new ParserException("Bad crontab line " + lineNumber + " format: too little columns");
        }

        return result;
    }

    private static List<TaskMetadata> internalParse(InputStream input) throws ParserException {
        List<TaskMetadata> result = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            // check for utf-8 encoding
            reader.mark(4);
            if ('\ufeff' != reader.read()) {
                reader.reset(); // first character is not bom... reseting
            }

            String line;
            int i = 1;
            while ((line = reader.readLine()) != null) {
                // remove comments from crontab line
                line = removeComment(line);

                result.add(parseLine(i, line));
                ++i;
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
