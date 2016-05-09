package jjcron.polankam.ms.mff.cuni.cz;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 *
 * @author Neloop
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
        this.second = CrontabTimeUnit.createSecond(second);
        this.minute = CrontabTimeUnit.createMinute(minute);
        this.hour = CrontabTimeUnit.createHour(hour);
        this.dayOfMonth = CrontabTimeUnit.createDayOfMonth(month);
        this.month = CrontabTimeUnit.createMonth(month);
        this.dayOfWeek = CrontabTimeUnit.createDayOfWeek(dayOfWeek);
    }

    public final long delay() {
        LocalDateTime localNow = LocalDateTime.now();

        LocalDateTime next = localNow;
        next = next.plusSeconds(second.delay(next, next.getSecond(), false));
        next = next.plusMinutes(minute.delay(next, next.getMinute(), second.isChanged()));
        next = next.plusHours(hour.delay(next, next.getHour(), minute.isChanged()));
        next = next.plusDays(dayOfWeek.delay(next, next.getDayOfWeek().getValue(), hour.isChanged()));
        next = next.plusDays(dayOfMonth.delay(next, next.getDayOfMonth(), dayOfWeek.isChanged()));
        next = next.plusMonths(month.delay(next, next.getMonthValue(), dayOfMonth.isChanged()));

        Duration duration = Duration.between(localNow, next);
        System.out.println(">>> delay: " + duration.getSeconds() + "s; next timepoint: " + next);
        return duration.getSeconds();
    }

    public final TimeUnit timeUnit() {
        return TimeUnit.SECONDS;
    }
}
