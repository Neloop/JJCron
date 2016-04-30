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

    private final CrontabTimeUnitBase second;
    private final CrontabTimeUnitBase minute;
    private final CrontabTimeUnitBase hour;
    private final CrontabTimeUnitBase dayOfMonth;
    private final CrontabTimeUnitBase month;
    private final CrontabTimeUnitBase dayOfWeek;

    public CrontabTime(String second, String minute, String hour,
            String dayOfMonth, String month, String dayOfWeek) throws FormatException {
        this.second = new CrontabTimeSecond(second);
        this.minute = new CrontabTimeMinute(minute);
        this.hour = new CrontabTimeHour(hour);
        this.dayOfMonth = new CrontabTimeDayOfMonth(dayOfMonth);
        this.month = new CrontabTimeMonth(month);
        this.dayOfWeek = new CrontabTimeDayOfWeek(dayOfWeek);
    }

    public long delay() {
        LocalDateTime localNow = LocalDateTime.now();
        LocalDateTime next = localNow
                .withSecond(second.delay(localNow))
                .withMinute(minute.delay(localNow))
                .withHour(hour.delay(localNow))
                .withDayOfMonth(dayOfMonth.delay(localNow))
                .withMonth(month.delay(localNow));

        // TODO: day of week

        Duration duration = Duration.between(localNow, next);
        return duration.getSeconds();
    }

    public TimeUnit timeUnit() {
        return TimeUnit.SECONDS;
    }
}
