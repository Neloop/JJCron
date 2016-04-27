package jjcron;

import java.time.LocalDateTime;

/**
 *
 * @author Martin
 */
public class CrontabTimeDayOfWeek extends CrontabTimeUnit {

    private final String dayOfWeek;

    public CrontabTimeDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @Override
    public void check() throws FormatException {
        checkCommon(dayOfWeek);
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
