package jjcron;

import java.time.LocalDateTime;

/**
 *
 * @author Martin
 */
public abstract class CrontabTimeUnit {

    public void checkCommon(String unit) throws FormatException {
        // TODO
    }

    public abstract void check() throws FormatException;

    public abstract int initial(LocalDateTime localNow);

    public abstract long period();
}
