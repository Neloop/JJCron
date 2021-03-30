package cz.polankam.jjcron.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents single time unit stored in one of the time columns in crontab.
 * Type of this unit is determined by {@link CrontabTimeValueType} structure.
 * Values which were parsed from crontab are stored in internal array.
 *
 * @author Neloop
 */
public class CrontabTimeValue implements Serializable {

    /**
     * Type of stored values.
     */
    public final CrontabTimeValueType valueType;
    /**
     * Particular time values loaded from crontab.
     */
    public final List<Integer> values;

    /**
     * Type and raw array are received as parameters and stored.
     *
     * @param valueType type of values given as parameter
     * @param values particular time values
     * @throws FormatException if parameter was null
     */
    public CrontabTimeValue(CrontabTimeValueType valueType, Integer[] values)
            throws FormatException {
        if (valueType == null) {
            throw new FormatException("Value type cannot be null");
        }

        if (values == null) {
            throw new FormatException("List of values cannot be null");
        }

        this.valueType = valueType;
        this.values = new ArrayList<>(Arrays.asList(values));
    }

    /**
     * Type and list are received as parameters and stored.
     *
     * @param valueType type of values given as parameter
     * @param values particular time values
     * @throws FormatException if parameter was null
     */
    public CrontabTimeValue(CrontabTimeValueType valueType,
            List<Integer> values) throws FormatException {
        if (valueType == null) {
            throw new FormatException("Value type cannot be null");
        }

        if (values == null) {
            throw new FormatException("List of values cannot be null");
        }

        this.valueType = valueType;
        this.values = values;
    }

    /**
     * Returns text which represents this value. Basically this will be same as
     * in loaded crontab file.
     *
     * @return textual representation of this value
     */
    @Override
    public String toString() {
        switch (valueType) {
            case ASTERISK:
                return "*";
            case PERIOD:
                return "*/" + values.get(0);
            default:
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < values.size(); ++i) {
                    if (i > 0) {
                        builder.append(",");
                    }
                    builder.append(values.get(i));
                }
                return builder.toString();
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof CrontabTimeValue)) {
            return false;
        }

        CrontabTimeValue cast = (CrontabTimeValue) other;
        if (!cast.valueType.equals(valueType)
                || cast.values.size() != values.size()) {
            return false;
        }

        List<Integer> thisValues = new ArrayList<>(values);
        List<Integer> otherValues = new ArrayList<>(cast.values);
        Collections.sort(thisValues);
        Collections.sort(otherValues);

        for (int i = 0; i < thisValues.size(); ++i) {
            if (!thisValues.get(i).equals(otherValues.get(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.valueType);
        hash = 71 * hash + Objects.hashCode(this.values);
        return hash;
    }
}
