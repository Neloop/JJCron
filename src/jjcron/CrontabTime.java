package jjcron;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 *
 * @author Martin
 */
public class CrontabTime {

    private static final Logger logger = Logger.getLogger(CrontabTime.class.getName());

    private final String second;
    private final String minute;
    private final String hour;
    private final String dayOfMonth;
    private final String month;
    private final String dayOfWeek;

    private void checkValues() throws FormatException {
    }

    public CrontabTime(String second, String minute, String hour,
            String dayOfMonth, String month, String dayOfWeek) throws FormatException {
        this.second = second;
        this.minute = minute;
        this.hour = hour;
        this.dayOfMonth = dayOfMonth;
        this.month = month;
        this.dayOfWeek = dayOfWeek;

        checkValues();
    }

    public long initialDelay() {
        // TODO
        return 0;
    }

    public long period() {
        // TODO
        return 0;
    }

    public TimeUnit timeUnit() {
        return TimeUnit.MINUTES;
    }
}
