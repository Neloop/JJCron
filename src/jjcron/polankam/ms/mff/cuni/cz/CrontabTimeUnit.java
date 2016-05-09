package jjcron.polankam.ms.mff.cuni.cz;

import java.time.LocalDateTime;

/**
 *
 * @author Neloop
 */
public interface CrontabTimeUnit {

    /**
     *
     * @param unit
     * @return
     * @throws FormatException
     */
    public static CrontabTimeUnit createSecond(String unit) throws FormatException {
        return new CrontabTimeGeneralUnit(unit, 0, 59, 60, CrontabTimeValueParser.createGeneral());
    }

    /**
     *
     * @param unit
     * @return
     * @throws FormatException
     */
    public static CrontabTimeUnit createMinute(String unit) throws FormatException {
        return new CrontabTimeGeneralUnit(unit, 0, 59, 60, CrontabTimeValueParser.createGeneral());
    }

    /**
     *
     * @param unit
     * @return
     * @throws FormatException
     */
    public static CrontabTimeUnit createHour(String unit) throws FormatException {
        return new CrontabTimeGeneralUnit(unit, 0, 23, 24, CrontabTimeValueParser.createGeneral());
    }

    /**
     *
     * @param unit
     * @return
     * @throws FormatException
     */
    public static CrontabTimeUnit createDayOfMonth(String unit) throws FormatException {
        return new CrontabTimeDayOfMonthUnit(unit, CrontabTimeValueParser.createGeneral());
    }

    /**
     *
     * @param unit
     * @return
     * @throws FormatException
     */
    public static CrontabTimeUnit createMonth(String unit) throws FormatException {
        return new CrontabTimeGeneralUnit(unit, 1, 12, 12, CrontabTimeValueParser.createMonth());
    }

    /**
     *
     * @param unit
     * @return
     * @throws FormatException
     */
    public static CrontabTimeUnit createDayOfWeek(String unit) throws FormatException {
        return new CrontabTimeGeneralUnit(unit, 1, 7, 7, CrontabTimeValueParser.createDayOfWeek());
    }


    // --- INTERFACE DEFINITION ---

    /**
     *
     * @return
     */
    public boolean isChanged();

    /**
     *
     * @param current
     * @param currentValue
     * @param previousChanged
     * @return
     */
    public int delay(LocalDateTime current, int currentValue, boolean previousChanged);
}
