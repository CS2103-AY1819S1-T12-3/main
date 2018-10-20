package ssp.scheduleplanner.storage;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * An Immutable rangeOfWeek for schedule planner that is serializable to XML format
 */
@XmlRootElement(name = "rangeofweek")
public class XmlSerializableRangeOfWeek {

    public static final String MESSAGE_DUPLICATE_TASK = "Tasks list contains duplicate task(s).";
    private static final int WEEKS_IN_SEMESTER = 17;

    @XmlElement
    private List<XmlAdaptedRangeOfWeek> rangeOfWeeks;

    /**
     * Creates an empty XmlSerializableRangeOfWeek.
     * This empty constructor is required for marshalling.
     */
    public XmlSerializableRangeOfWeek() {
        rangeOfWeeks = new ArrayList<>();
    }

    /**
     * Conversion
     */
    public XmlSerializableRangeOfWeek(String[][] src) {
        this();
        for (int i = 0; i < WEEKS_IN_SEMESTER; i++) {
            rangeOfWeeks.add(new XmlAdaptedRangeOfWeek(src[i][0], src[i][1], src[i][2]));
        }
    }

    /**
     * Conversion from RangeOfWeeks to 2d array
     */
    public String[][] convertRangeOfWeeksToString2dArray(XmlSerializableRangeOfWeek rangeOfWeek) {
        String[][] string2dArray = new String[WEEKS_IN_SEMESTER][3];
        for (int i = 0; i < WEEKS_IN_SEMESTER; i++) {
            string2dArray[i][0] = rangeOfWeeks.get(i).getStartOfWeekDate();
            string2dArray[i][1] = rangeOfWeeks.get(i).getEndOfWeekDate();
            string2dArray[i][2] = rangeOfWeeks.get(i).getDescription();
        }
        return string2dArray;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < WEEKS_IN_SEMESTER; i++) {
            sb.append(rangeOfWeeks.get(i).toString());
            sb.append("\n");
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlSerializableRangeOfWeek)) {
            return false;
        }
        return rangeOfWeeks.equals(((XmlSerializableRangeOfWeek) other).rangeOfWeeks);
    }
}
