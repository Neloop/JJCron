package jjcron;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author Martin
 */
public class CrontabTime {
    private final String minute;
    private final String hour;
    private final String dayOfMonth;
    private final String month;
    private final String dayOfWeek;

    public CrontabTime(String minute, String hour, String dayOfMonth,
            String month, String dayOfWeek) {
        this.minute = minute;
        this.hour = hour;
        this.dayOfMonth = dayOfMonth;
        this.month = month;
        this.dayOfWeek = dayOfWeek;
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
