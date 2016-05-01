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
        this.second = new CrontabTimeGeneralUnit(second, 0, 60);
        this.minute = new CrontabTimeGeneralUnit(minute, 0, 60);
        this.hour = new CrontabTimeGeneralUnit(hour, 0, 24);
        this.dayOfMonth = new CrontabTimeDayOfMonth(dayOfMonth);
        this.month = new CrontabTimeGeneralUnit(month, 0, 12);
        this.dayOfWeek = new CrontabTimeDayOfWeek(dayOfWeek);
    }

    public long delay() {
        LocalDateTime localNow = LocalDateTime.now();
        LocalDateTime next = localNow
                .withSecond(localNow.getSecond() + second.delay(localNow))
                .withMinute(localNow.getMinute() + minute.delay(localNow))
                .withHour(localNow.getHour() + hour.delay(localNow))
                .withDayOfMonth(localNow.getDayOfMonth() + dayOfMonth.delay(localNow))
                .withMonth(localNow.getMonthValue() + month.delay(localNow));

        // TODO: day of week

        Duration duration = Duration.between(localNow, next);
        return duration.getSeconds();
    }

    public TimeUnit timeUnit() {
        return TimeUnit.SECONDS;
    }
}
