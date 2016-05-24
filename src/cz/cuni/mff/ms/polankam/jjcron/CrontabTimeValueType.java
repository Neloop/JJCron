package cz.cuni.mff.ms.polankam.jjcron;

/**
 * Represents all value types which can be stored in crontab in one time column.
 * There is shortcut for single value which can be viewed
 *   as list of values with one element.
 * @author Neloop
 */
public enum CrontabTimeValueType {
    /**
     * May represent single value or list written in crontab.
     */
    LIST,
    /**
     * Represents asterisk in crontab, can be expressed as list of all values.
     */
    ASTERISK,
    /**
     * Represents periodical execution with given period within one unit.
     */
    PERIOD
}
