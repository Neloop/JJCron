package jjcron;

import java.time.LocalDateTime;

/**
 *
 * @author Martin
 */
public class CrontabTimeDayOfMonth extends CrontabTimeUnit {

    private final String dayOfMonth;

    public CrontabTimeDayOfMonth(String dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    @Override
    public void check() throws FormatException {
        checkCommon(dayOfMonth);
    }

    @Override
    public int initial(LocalDateTime localNow) {
        // TODO
        return 1;
    }

    @Override
    public long period() {
        // TODO
        return 1;
    }
}
