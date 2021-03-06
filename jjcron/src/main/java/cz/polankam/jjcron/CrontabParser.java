package cz.polankam.jjcron;

import cz.polankam.jjcron.common.CrontabTime;
import cz.polankam.jjcron.common.FormatException;
import cz.polankam.jjcron.common.TaskMetadata;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Logically static class which is used as container for crontab parsing and
 * saving methods.
 *
 * @author Neloop
 */
public class CrontabParser {

    /**
     * Standard Java logger.
     */
    private static final Logger logger
            = Logger.getLogger(CrontabParser.class.getName());

    /**
     * Column count of crontab line
     */
    private static final int CRONTAB_COLUMNS = 7;
    /**
     * Column number which contains seconds identifier
     */
    private static final int SEC_COLUMN = 0;
    /**
     * Column number which contains minutes identifier
     */
    private static final int MIN_COLUMN = 1;
    /**
     * Column number which contains hours identifier
     */
    private static final int HOUR_COLUMN = 2;
    /**
     * Column number which contains day of month identifier
     */
    private static final int DAY_OF_MONTH_COLUMN = 3;
    /**
     * Column number which contains months identifier
     */
    private static final int MONTH_COLUMN = 4;
    /**
     * Column number which contains day of week identifier
     */
    private static final int DAY_OF_WEEK_COLUMN = 5;
    /**
     * Column number which contains command identifier
     */
    private static final int CMD_COLUMN = 6;

    /**
     * Delimiter which splits line into tokens
     */
    private static final String LINE_SPLITTER = " ";
    /**
     * Delimiter with which comment section starts
     */
    private static final char COMMENT_DELIM = '#';
    /**
     * Unicode Byte Order Mark
     */
    private static final char UTF_BOM = '\ufeff';

    /**
     * From given string remove comment if any present on the end of line.
     *
     * @param line searched string
     * @return newly created string without comment section
     */
    private static String removeComment(String line) {
        String result = line;
        int pos = line.indexOf(COMMENT_DELIM);
        if (pos != -1) {
            result = result.substring(0, pos);
        }

        return result;
    }

    /**
     * Given line is parsed into {@link TaskMetadata} structure.
     *
     * @param line soon to be parsed line
     * @param lineNumber number of given line
     * @return {@link TaskMetadata} structure created from given line
     * @throws ParserException if parsing error occurs
     */
    private static TaskMetadata parseLine(String line, int lineNumber)
            throws ParserException {
        TaskMetadata result = null;
        List<String> splitted
                = new ArrayList<>(Arrays.asList(line.split(LINE_SPLITTER)));
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
                result = new TaskMetadata(new CrontabTime(sec, min, hour,
                        dayOfMonth, month, dayOfWeek), command);
            } catch (FormatException e) {
                logger.log(Level.SEVERE, e.getMessage());
                throw new ParserException(e.getMessage(), e);
            }
        } else {
            logger.log(Level.SEVERE, "Bad crontab line {0} format:"
                    + " too little columns", lineNumber);
            throw new ParserException("Bad crontab line " + lineNumber
                    + " format: too little columns");
        }

        return result;
    }

    /**
     * General internal method for task information parsing.
     *
     * @param input input stream containing crontab
     * @return array of newly created {@link TaskMetadata} structures
     * @throws ParserException if parsing failed
     */
    private static List<TaskMetadata> internalLoad(InputStream input)
            throws ParserException {
        if (input == null) {
            throw new ParserException("Input is null");
        }

        List<TaskMetadata> result = new ArrayList<>();

        try (BufferedReader reader
                = new BufferedReader(new InputStreamReader(input))) {
            // check for utf-8 encoding
            reader.mark(4);
            if (UTF_BOM != reader.read()) {
                reader.reset(); // first character is not bom... reseting
            }

            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                // trim line and check for emptyness
                line = line.trim();
                if (line.equals("")) {
                    continue;
                }

                // remove comments from crontab line
                line = removeComment(line);

                result.add(parseLine(line, lineNumber));
                ++lineNumber;
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw new ParserException(e.getMessage(), e);
        }

        return result;
    }

    /**
     * Parse tasks information from given crontab file.
     *
     * @param filename name of file which will be loaded
     * @return array of {@link TaskMetadata} structures loaded from file
     * @throws ParserException if parsing failed
     */
    public static List<TaskMetadata> loadFile(String filename)
            throws ParserException {
        if (filename == null) {
            throw new ParserException("Filename is null");
        }

        try (InputStream file = new FileInputStream(filename)) {
            return internalLoad(file);
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw new ParserException(e.getMessage(), e);
        }
    }

    /**
     * Parse tasks information from given input stream.
     *
     * @param input input stream containing task information in crontab format
     * @return array of {@link TaskMetadata} structures
     * @throws ParserException if parsing failed
     */
    public static List<TaskMetadata> load(InputStream input)
            throws ParserException {
        return internalLoad(input);
    }

    /**
     * Parse tasks information from given string.
     *
     * @param input input string containing task information in crontab format
     * @return array of {@link TaskMetadata} structures
     * @throws ParserException if parsing failed
     */
    public static List<TaskMetadata> load(String input)
            throws ParserException {
        if (input == null) {
            throw new ParserException("Input string is null");
        }

        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        return internalLoad(inputStream);
    }

    /**
     * Saves given tasks into output stream in crontab format.
     *
     * @param tasks list of tasks which will be outputted
     * @param output stream for write operations
     * @throws ParserException in case of any error
     */
    private static void internalSave(List<TaskMetadata> tasks,
            OutputStream output) throws ParserException {
        if (tasks == null) {
            throw new ParserException("List of tasks cannot be null");
        }
        if (output == null) {
            throw new ParserException("OutputStream cannot be null");
        }

        try (PrintWriter writer = new PrintWriter(output)) {
            for (TaskMetadata task : tasks) {
                writer.println(task.time().toString() + " " + task.command());
            }
        }
    }

    /**
     * Saves given tasks into output stream in crontab format.
     *
     * @param tasks list of tasks which will be written
     * @param output stream for write operations
     * @throws ParserException in case of any error
     */
    public static void save(List<TaskMetadata> tasks, OutputStream output)
            throws ParserException {
        internalSave(tasks, output);
    }

    /**
     * Saves given tasks into given crontab file.
     *
     * @param tasks list of tasks which will be written into crontab
     * @param crontab filename of crontab
     * @throws ParserException in case of any writing error
     */
    public static void save(List<TaskMetadata> tasks, String crontab)
            throws ParserException {
        if (crontab == null) {
            throw new ParserException("Crontab file cannot be null");
        }

        try (OutputStream file = new FileOutputStream(crontab)) {
            internalSave(tasks, file);
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw new ParserException(e.getMessage(), e);
        }
    }
}
