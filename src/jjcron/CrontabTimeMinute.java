package jjcron;

import java.time.LocalDateTime;

/**
 *
 * @author Martin
 */
public class CrontabTimeMinute extends CrontabTimeUnit {

    private final String minute;

    public CrontabTimeMinute(String minute) {
        this.minute = minute;
    }

    @Override
    public void check() throws FormatException {
        checkCommon(minute);
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
