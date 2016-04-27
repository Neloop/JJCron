package jjcron;

import java.time.LocalDateTime;

/**
 *
 * @author Martin
 */
public class CrontabTimeSecond extends CrontabTimeUnit {

    private final String second;

    public CrontabTimeSecond(String second) {
        this.second = second;
    }

    @Override
    public void check() throws FormatException {
        checkCommon(second);
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
