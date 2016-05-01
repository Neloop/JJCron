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
        this.second = CrontabTimeGeneralUnit.createCrontabTimeSecond(second);
        this.minute = CrontabTimeGeneralUnit.createCrontabTimeMinute(minute);
        this.hour = CrontabTimeGeneralUnit.createCrontabTimeHour(hour);
        this.dayOfMonth = new CrontabTimeDayOfMonth(dayOfMonth);
        this.month = CrontabTimeGeneralUnit.createCrontabTimeMonth(month);
        this.dayOfWeek = new CrontabTimeDayOfWeek(dayOfWeek);
    }

    public long delay() {
        LocalDateTime localNow = LocalDateTime.now();
        LocalDateTime next = localNow
                .withSecond(localNow.getSecond() + second.delay(localNow.getSecond()))
                .withMinute(localNow.getMinute() + minute.delay(localNow.getMinute()))
                .withHour(localNow.getHour() + hour.delay(localNow.getHour()))
                .withDayOfMonth(localNow.getDayOfMonth() + dayOfMonth.delay(localNow.getDayOfMonth()))
                .withMonth(localNow.getMonthValue() + month.delay(localNow.getMonthValue()));

        // TODO: day of week

        Duration duration = Duration.between(localNow, next);
        return duration.getSeconds();
    }

    public TimeUnit timeUnit() {
        return TimeUnit.SECONDS;
    }
}
