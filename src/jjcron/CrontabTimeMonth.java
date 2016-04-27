package jjcron;

import java.time.LocalDateTime;

/**
 *
 * @author Martin
 */
public class CrontabTimeMonth extends CrontabTimeUnit {

    private final String month;

    public CrontabTimeMonth(String month) {
        this.month = month;
    }

    @Override
    public void check() throws FormatException {
        checkCommon(month);
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
