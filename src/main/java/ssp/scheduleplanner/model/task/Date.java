package ssp.scheduleplanner.model.task;

import static java.util.Objects.requireNonNull;
import static ssp.scheduleplanner.commons.util.AppUtil.checkArgument;


/**
 * Represents a Task's date in the Schedule Planner.
 * Guarantees: immutable; is valid as declared in {@link #isValidDate(String)}
 */
public class Date {


    public static final String MESSAGE_DATE_CONSTRAINTS =
            "Date should only contain numbers, and it should be exactly 6 digits long";
    public static final String DATE_VALIDATION_REGEX = "\\d{6}";
    public final String value;
    public final int day;
    public final int month;
    public final int year;

    /**
     * Constructs a {@code Date}.
     *
     * @param date A valid date.
     */
    public Date(String date) {
        requireNonNull(date);
        checkArgument(isValidDate(date), MESSAGE_DATE_CONSTRAINTS);
        value = date;

        String dayString = date.substring(0, 2);
        day = Integer.parseInt(dayString);
        String monthString = date.substring(2, 4);
        month = Integer.parseInt(monthString);
        String yearString = date.substring(4, 6);
        year = Integer.parseInt(yearString) + 2000;
    }

    /**
     * Returns true if a given string is a valid date.
     */
    public static boolean isValidDate(String test) {
        if (!test.matches(DATE_VALIDATION_REGEX)) {
            return false;
        }
        String dayString = test.substring(0, 2);
        int day = Integer.parseInt(dayString);
        String monthString = test.substring(2, 4);
        int month = Integer.parseInt(monthString);
        String yearString = test.substring(4, 6);
        int year = Integer.parseInt(yearString) + 2000;
        if (month > 12 || month == 0) {
            return false;
        } else if (day == 0) {
            return false;
        } else if (isLargeMonth(month)) {
            return day <= 31;
        } else if (month == 2) {
            if (isLeapYear(year)) {
                return day <= 29;
            } else {
                return day <= 28;
            }
        } else {
            return day <= 30;
        }
    }

    /**
     * Returns true if a given year is a leap year.
     * @param year A year
     */
    private static boolean isLeapYear(int year) {
        if (year % 4 != 0) {
            return false;
        } else if (year % 400 == 0) {
            return true;
        } else if (year % 100 == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Returns true if a given month has 31 days.
     * @param month A month
     */
    private static boolean isLargeMonth(int month) {
        return month == 1 || month == 3 || month == 5
                || month == 7 || month == 8
                || month == 10 || month == 12;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Date // instanceof handles nulls
                && value.equals(((Date) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
