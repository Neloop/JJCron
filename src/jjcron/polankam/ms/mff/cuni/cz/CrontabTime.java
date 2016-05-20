package jjcron.polankam.ms.mff.cuni.cz;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * Represents full information about time scheduling for one task.
 * It contains specification of day of week, month, day of month, hour, minute
 *   and second, these specifications are stored as structures
 *   which implements {@link CrontabTimeUnit} interface.
 * This units are self computable and can compute its own delays.
 * @author Neloop
 */
public class CrontabTime {

    /**
     * Structure representing second time column from crontab.
     */
    private final CrontabTimeUnit second;
    /**
     * Structure representing minute time column from crontab.
     */
    private final CrontabTimeUnit minute;
    /**
     * Structure representing hour time column from crontab.
     */
    private final CrontabTimeUnit hour;
    /**
     * Structure representing day of month time column from crontab.
     */
    private final CrontabTimeUnit dayOfMonth;
    /**
     * Structure representing month time column from crontab.
     */
    private final CrontabTimeUnit month;
    /**
     * Structure representing day of week time column from crontab.
     */
    private final CrontabTimeUnit dayOfWeek;

    /**
     * Constructor with splitted particular unit extracted from crontab.
     * Textual description of these units is parsed and {@link CrontabTimeUnit}
     *   structures are created.
     * @param second representing seconds column
     * @param minute representing minutes column
     * @param hour representing hours column
     * @param dayOfMonth representing day of months column
     * @param month representing months column
     * @param dayOfWeek representing day of weeks column
     * @throws FormatException if parsing/creating of units failed
     */
    public CrontabTime(String second, String minute, String hour,
            String dayOfMonth, String month, String dayOfWeek)
            throws FormatException {
        this.second = CrontabTimeUnit.createSecond(second);
        this.minute = CrontabTimeUnit.createMinute(minute);
        this.hour = CrontabTimeUnit.createHour(hour);
        this.dayOfMonth = CrontabTimeUnit.createDayOfMonth(dayOfMonth);
        this.month = CrontabTimeUnit.createMonth(month);
        this.dayOfWeek = CrontabTimeUnit.createDayOfWeek(dayOfWeek);
    }

    /**
     * Constructor with all supplied information no parsing needed.
     * @param second representing seconds column
     * @param minute representing minutes column
     * @param hour representing hours column
     * @param dayOfMonth representing day of months column
     * @param month representing months column
     * @param dayOfWeek representing day of weeks column
     */
    public CrontabTime(CrontabTimeUnit second, CrontabTimeUnit minute,
            CrontabTimeUnit hour, CrontabTimeUnit dayOfMonth,
            CrontabTimeUnit month, CrontabTimeUnit dayOfWeek) {
        this.second = second;
        this.minute = minute;
        this.hour = hour;
        this.dayOfMonth = dayOfMonth;
        this.month = month;
        this.dayOfWeek = dayOfWeek;
    }

    /**
     * Compute delay from now to next scheduled time point
     *   which is determined from all internal time units.
     * @param localNow base time point
     * @return count of specific units from now to next scheduled point.
     *   Returned unit is specified by timeUnit() return type.
     */
    public final long delay(LocalDateTime localNow) {
        LocalDateTime next = localNow;
        next = next.plusSeconds(second.delay(next, next.getSecond(),
                false));
        next = next.plusMinutes(minute.delay(next, next.getMinute(),
                second.isChanged()));
        next = next.plusHours(hour.delay(next, next.getHour(),
                minute.isChanged()));
        next = next.plusDays(dayOfWeek.delay(next,
                next.getDayOfWeek().getValue(), hour.isChanged()));
        next = next.plusDays(dayOfMonth.delay(next, next.getDayOfMonth(),
                dayOfWeek.isChanged()));
        next = next.plusMonths(month.delay(next, next.getMonthValue(),
                dayOfMonth.isChanged()));

        Duration duration = Duration.between(localNow, next);
        return duration.getSeconds();
    }

    /**
     * Time unit in which delay is returned.
     * Should be used for scheduling purposes.
     * @return internal Java structure representing time unit
     */
    public final TimeUnit timeUnit() {
        return TimeUnit.SECONDS;
    }
}
