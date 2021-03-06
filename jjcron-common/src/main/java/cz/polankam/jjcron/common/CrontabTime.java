package cz.polankam.jjcron.common;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Represents full information about time scheduling for one task. It contains
 * specification of day of week, month, day of month, hour, minute and second,
 * these specifications are stored as structures which implements
 * {@link CrontabTimeUnit} interface. This units are self computable and can
 * compute its own delays.
 *
 * @author Neloop
 */
public class CrontabTime implements Serializable {

    /**
     * Structure representing second time column from crontab.
     */
    public final CrontabTimeUnit second;
    /**
     * Structure representing minute time column from crontab.
     */
    public final CrontabTimeUnit minute;
    /**
     * Structure representing hour time column from crontab.
     */
    public final CrontabTimeUnit hour;
    /**
     * Structure representing day of month time column from crontab.
     */
    public final CrontabTimeUnit dayOfMonth;
    /**
     * Structure representing month time column from crontab.
     */
    public final CrontabTimeUnit month;
    /**
     * Structure representing day of week time column from crontab.
     */
    public final CrontabTimeUnit dayOfWeek;

    /**
     * Constructor with splitted particular unit extracted from crontab. Textual
     * description of these units is parsed and {@link CrontabTimeUnit}
     * structures are created.
     *
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
     *
     * @param second representing seconds column
     * @param minute representing minutes column
     * @param hour representing hours column
     * @param dayOfMonth representing day of months column
     * @param month representing months column
     * @param dayOfWeek representing day of weeks column
     * @throws FormatException if parameter was null
     */
    public CrontabTime(CrontabTimeUnit second, CrontabTimeUnit minute,
            CrontabTimeUnit hour, CrontabTimeUnit dayOfMonth,
            CrontabTimeUnit month, CrontabTimeUnit dayOfWeek) throws FormatException {
        if (second == null || minute == null || hour == null
                || dayOfMonth == null || month == null || dayOfWeek == null) {
            throw new FormatException("One of the units was null");
        }

        this.second = second;
        this.minute = minute;
        this.hour = hour;
        this.dayOfMonth = dayOfMonth;
        this.month = month;
        this.dayOfWeek = dayOfWeek;
    }

    /**
     * Compute delay from now to next scheduled time point which is determined
     * from all internal time units.
     *
     * @param localNow base time point
     * @return count of specific units from now to next scheduled point.
     * Returned unit is specified by timeUnit() return type.
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
     * Time unit in which delay is returned. Should be used for scheduling
     * purposes.
     *
     * @return internal Java structure representing time unit
     */
    public final TimeUnit timeUnit() {
        return TimeUnit.SECONDS;
    }

    /**
     * Returns textual representation of crontab time format. Basically result
     * is the same as the one loaded from initial crontab file.
     *
     * @return textual representation of crontab time
     */
    @Override
    public final String toString() {
        String delim = " ";
        StringBuilder builder = new StringBuilder();
        builder.append(second.value().toString());
        builder.append(delim);
        builder.append(minute.value().toString());
        builder.append(delim);
        builder.append(hour.value().toString());
        builder.append(delim);
        builder.append(dayOfMonth.value().toString());
        builder.append(delim);
        builder.append(month.value().toString());
        builder.append(delim);
        builder.append(dayOfWeek.value().toString());
        return builder.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof CrontabTime)) {
            return false;
        }

        CrontabTime cast = (CrontabTime) other;
        return cast.second.equals(second) && cast.minute.equals(minute)
                && cast.hour.equals(hour) && cast.dayOfMonth.equals(dayOfMonth)
                && cast.month.equals(month) && cast.dayOfWeek.equals(dayOfWeek);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.second);
        hash = 73 * hash + Objects.hashCode(this.minute);
        hash = 73 * hash + Objects.hashCode(this.hour);
        hash = 73 * hash + Objects.hashCode(this.dayOfMonth);
        hash = 73 * hash + Objects.hashCode(this.month);
        hash = 73 * hash + Objects.hashCode(this.dayOfWeek);
        return hash;
    }
}
