package ssp.scheduleplanner.model.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ssp.scheduleplanner.testutil.Assert;

public class VenueTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Venue(null));
    }

    @Test
    public void constructor_invalidAddress_throwsIllegalArgumentException() {
        String invalidAddress = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Venue(invalidAddress));
    }

    @Test
    public void isValidAddress() {
        // null address
        Assert.assertThrows(NullPointerException.class, () -> Venue.isValidAddress(null));

        // invalid addresses
        assertFalse(Venue.isValidAddress("")); // empty string
        assertFalse(Venue.isValidAddress(" ")); // spaces only

        // valid addresses
        assertTrue(Venue.isValidAddress("Blk 456, Den Road, #01-355"));
        assertTrue(Venue.isValidAddress("-")); // one character
        assertTrue(Venue.isValidAddress("Leng Inc; 1234 Market St; San Francisco CA 2349879; USA")); // long address
    }
}
