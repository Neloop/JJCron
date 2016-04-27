package jjcron;

import java.time.LocalDateTime;

/**
 *
 * @author Martin
 */
public class CrontabTimeHour extends CrontabTimeUnit {

    private final String hour;

    public CrontabTimeHour(String hour) {
        this.hour = hour;
    }

    @Override
    public void check() throws FormatException {
        checkCommon(hour);
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
