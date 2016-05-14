package jjcron.polankam.ms.mff.cuni.cz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents single time unit stored in one of the time columns in crontab.
 * Type of this unit is determined by {@link CrontabTimeValueType} structure.
 * Values which were parsed from crontab are stored in internal array.
 * @author Neloop
 */
public class CrontabTimeValue {

    /**
     * Type of stored values.
     */
    public CrontabTimeValueType valueType;
    /**
     * Particular time values loaded from crontab.
     */
    public List<Integer> values;

    /**
     * Type and raw array are received as parameters and stored.
     * @param valueType type of values given as parameter
     * @param values particular time values
     */
    public CrontabTimeValue(CrontabTimeValueType valueType, Integer[] values) {
        this.valueType = valueType;
        this.values = new ArrayList<>(Arrays.asList(values));
    }

    /**
     * Type and list are received as parameters and stored.
     * @param valueType type of values given as parameter
     * @param values particular time values
     */
    public CrontabTimeValue(CrontabTimeValueType valueType, List<Integer> values) {
        this.valueType = valueType;
        this.values = values;
    }
}
