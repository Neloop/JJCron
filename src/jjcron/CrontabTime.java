package jjcron;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 *
 * @author Martin
 */
public class CrontabTime {

    private static final Logger logger = Logger.getLogger(CrontabTime.class.getName());

    private final CrontabTimeUnit second;
    private final CrontabTimeUnit minute;
    private final CrontabTimeUnit hour;
    private final CrontabTimeUnit dayOfMonth;
    private final CrontabTimeUnit month;
    private final CrontabTimeUnit dayOfWeek;

    private void checkValues() throws FormatException {
        second.check();
        minute.check();
        hour.check();
        dayOfMonth.check();
        month.check();
        dayOfWeek.check();
    }

    public CrontabTime(String second, String minute, String hour,
            String dayOfMonth, String month, String dayOfWeek) throws FormatException {
        this.second = new CrontabTimeSecond(second);
        this.minute = new CrontabTimeMinute(minute);
        this.hour = new CrontabTimeHour(hour);
        this.dayOfMonth = new CrontabTimeDayOfMonth(dayOfMonth);
        this.month = new CrontabTimeMonth(month);
        this.dayOfWeek = new CrontabTimeDayOfWeek(dayOfWeek);

        checkValues();
    }

    public long initialDelay() {
        // TODO
        LocalDateTime localNow = LocalDateTime.now();
        LocalDateTime next = localNow
                .withSecond(second.initial(localNow))
                .withMinute(minute.initial(localNow))
                .withHour(hour.initial(localNow))
                .withDayOfMonth(dayOfMonth.initial(localNow))
                .withMonth(month.initial(localNow));
        /*if(localNow.compareTo(next) > 0) {
            next = next.plusDays(1);
        }*/

        Duration duration = Duration.between(localNow, next);
        return duration.getSeconds();
    }

    public long period() {
        // TODO
        return 1;
    }

    public TimeUnit timeUnit() {
        return TimeUnit.SECONDS;
    }
}
