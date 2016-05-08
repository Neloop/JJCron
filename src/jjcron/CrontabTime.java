package jjcron;

import java.sql.Timestamp;
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

    public CrontabTime(String second, String minute, String hour,
            String dayOfMonth, String month, String dayOfWeek) throws FormatException {
        this.second = CrontabTimeUnit.createCrontabTimeSecond(second);
        this.minute = CrontabTimeUnit.createCrontabTimeMinute(minute);
        this.hour = CrontabTimeUnit.createCrontabTimeHour(hour);
        this.dayOfMonth = CrontabTimeUnit.createCrontabTimeHour(dayOfMonth); // TODO
        this.month = CrontabTimeUnit.createCrontabTimeMonth(month); // TODO
        this.dayOfWeek = CrontabTimeUnit.createCrontabTimeHour(dayOfWeek); // TODO
    }

    public final long delay() {
        LocalDateTime localNow = LocalDateTime.now();
        Timestamp localTimestamp = Timestamp.valueOf(localNow);

        LocalDateTime next = localNow;
        next = next.plusSeconds(second.delay(localNow.getSecond(), localTimestamp.getTime(), false));
        next = next.plusMinutes(minute.delay(localNow.getMinute(), localTimestamp.getTime() / 60, second.lastChanged()));
        next = next.plusHours(hour.delay(localNow.getHour(), localTimestamp.getTime() / (60 * 60), minute.lastChanged()));

        // TODO: may be problems with scheduling on next minute by second unit and scheduling to that minute also by minute unit

        // TODO: day of week, month, day of month

        Duration duration = Duration.between(localNow, next);
        System.out.println(">>> delay: " + duration.getSeconds());
        return duration.getSeconds();
    }

    public final TimeUnit timeUnit() {
        return TimeUnit.SECONDS;
    }
}
