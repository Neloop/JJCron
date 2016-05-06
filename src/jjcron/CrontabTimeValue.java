package jjcron;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Martin
 */
public class CrontabTimeValue {

    public CrontabTimeValueType valueType;
    public List<Integer> values;

    public CrontabTimeValue(CrontabTimeValueType valueType, Integer[] values) {
        this.valueType = valueType;
        this.values = new ArrayList<>(Arrays.asList(values));
    }

    public CrontabTimeValue(CrontabTimeValueType valueType, List<Integer> values) {
        this.valueType = valueType;
        this.values = values;
    }
}
