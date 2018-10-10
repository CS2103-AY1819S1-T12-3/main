package seedu.address.model.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class DateTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Date(null));
    }

    @Test
    public void constructor_invalidDate_throwsIllegalArgumentException() {
        String invalidDate = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Date(invalidDate));
    }

    @Test
    public void isValidDate() {
        // null date
        Assert.assertThrows(NullPointerException.class, () -> Date.isValidDate(null));

        // invalid dates
        assertFalse(Date.isValidDate("")); // empty string
        assertFalse(Date.isValidDate(" ")); // spaces only
        assertFalse(Date.isValidDate("91")); // not 6 numbers
        assertFalse(Date.isValidDate("phone")); // non-numeric
        assertFalse(Date.isValidDate("9011p0")); // alphabets within digits
        assertFalse(Date.isValidDate("9312 1")); // spaces within digits
        assertFalse(Date.isValidDate("441112")); // 44 not valid date
        assertFalse(Date.isValidDate("141312")); // 13 not valid month
        assertFalse(Date.isValidDate("001112")); // 0 not valid date
        assertFalse(Date.isValidDate("210012")); // 0 not valid month
        assertFalse(Date.isValidDate("310412")); // april only has 30 days

        // valid dates
        assertTrue(Date.isValidDate("280222"));
        assertTrue(Date.isValidDate("290220")); // leap year
        assertTrue(Date.isValidDate("310112"));
    }
}
