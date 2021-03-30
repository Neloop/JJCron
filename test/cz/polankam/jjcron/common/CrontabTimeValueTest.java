package cz.polankam.jjcron.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.TestWatcher;

/**
 *
 * @author Neloop
 */
public class CrontabTimeValueTest {

    private CrontabTimeValueType listValueType;
    private CrontabTimeValueType asteriskValueType;
    private List<Integer> valuesList;
    private Integer[] valuesArray;

    @Rule
    public TestWatcher watcher = new PrintMethodNameTestWatcher();

    @BeforeClass
    public static void setUpClass() {
        System.out.println(">>> CrontabTimeValueTest <<<");
    }

    @Before
    public void setUp() {
        listValueType = CrontabTimeValueType.LIST;
        asteriskValueType = CrontabTimeValueType.ASTERISK;
        valuesList = Arrays.asList(1, 2, 3);
        valuesArray = new Integer[]{1, 2, 3};
    }

    @Test(expected = FormatException.class)
    public void testConstructorArray_NullValueType() throws FormatException {
        CrontabTimeValue value = new CrontabTimeValue(null, valuesArray);
    }

    @Test(expected = FormatException.class)
    public void testConstructorArray_NullArray() throws FormatException {
        Integer[] arr = null;
        CrontabTimeValue value = new CrontabTimeValue(listValueType, arr);
    }

    @Test(expected = FormatException.class)
    public void testConstructorList_NullValueType() throws FormatException {
        CrontabTimeValue value = new CrontabTimeValue(null, valuesList);
    }

    @Test(expected = FormatException.class)
    public void testConstructorList_NullList() throws FormatException {
        List<Integer> list = null;
        CrontabTimeValue value = new CrontabTimeValue(listValueType, list);
    }

    @Test
    public void test_Correct() throws FormatException {
        CrontabTimeValue value = new CrontabTimeValue(listValueType,
                valuesArray);
        assertEquals(value.valueType, listValueType);
        assertEquals(value.values, Arrays.asList(valuesArray));

        value = new CrontabTimeValue(listValueType, valuesList);
        assertEquals(value.valueType, listValueType);
        assertEquals(value.values, valuesList);
    }

    @Test
    public void testEquals_False() throws FormatException {
        CrontabTimeValue firstValue = new CrontabTimeValue(listValueType,
                valuesList);
        CrontabTimeValue secondValue = new CrontabTimeValue(asteriskValueType,
                valuesList);
        assertFalse(firstValue.equals(secondValue));

        Integer[] asc = {1, 2, 3};
        Integer[] desc = {4, 3, 2, 1};
        firstValue = new CrontabTimeValue(listValueType, asc);
        secondValue = new CrontabTimeValue(listValueType, desc);
        assertFalse(firstValue.equals(secondValue));
    }

    @Test
    public void testEquals_True() throws FormatException {
        // equals not depend on list order
        Integer[] asc = {1, 2, 3};
        Integer[] desc = {3, 2, 1};
        CrontabTimeValue firstValue = new CrontabTimeValue(listValueType, asc);
        CrontabTimeValue secondValue
                = new CrontabTimeValue(listValueType, desc);
        assertTrue(firstValue.equals(secondValue));
    }
}
