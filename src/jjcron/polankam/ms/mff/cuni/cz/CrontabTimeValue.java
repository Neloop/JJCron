package jjcron.polankam.ms.mff.cuni.cz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Neloop
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
