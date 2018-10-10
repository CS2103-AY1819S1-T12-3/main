package seedu.address.model.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class PriorityTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Priority(null));
    }

    @Test
    public void constructor_invalidEmail_throwsIllegalArgumentException() {
        String invalidEmail = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Priority(invalidEmail));
    }

    @Test
    public void isValidEmail() {

        /*
        // null email
        Assert.assertThrows(NullPointerException.class, () -> Priority.isValidPriority(null));

        // blank email
        assertFalse(Priority.isValidPriority("")); // empty string
        assertFalse(Priority.isValidPriority(" ")); // spaces only

        // missing parts
        assertFalse(Priority.isValidPriority("@example.com")); // missing local part
        assertFalse(Priority.isValidPriority("peterjackexample.com")); // missing '@' symbol
        assertFalse(Priority.isValidPriority("peterjack@")); // missing domain name

        // invalid parts
        assertFalse(Priority.isValidPriority("peterjack@-")); // invalid domain name
        assertFalse(Priority.isValidPriority("peterjack@exam_ple.com")); // underscore in domain name
        assertFalse(Priority.isValidPriority("peter jack@example.com")); // spaces in local part
        assertFalse(Priority.isValidPriority("peterjack@exam ple.com")); // spaces in domain name
        assertFalse(Priority.isValidPriority(" peterjack@example.com")); // leading space
        assertFalse(Priority.isValidPriority("peterjack@example.com ")); // trailing space
        assertFalse(Priority.isValidPriority("peterjack@@example.com")); // double '@' symbol
        assertFalse(Priority.isValidPriority("peter@jack@example.com")); // '@' symbol in local part
        assertFalse(Priority.isValidPriority("peterjack@example@com")); // '@' symbol in domain name
        assertFalse(Priority.isValidPriority("peterjack@.example.com")); // domain name starts with a period
        assertFalse(Priority.isValidPriority("peterjack@example.com.")); // domain name ends with a period
        assertFalse(Priority.isValidPriority("peterjack@-example.com")); // domain name starts with a hyphen
        assertFalse(Priority.isValidPriority("peterjack@example.com-")); // domain name ends with a hyphen

        // valid email
        assertTrue(Priority.isValidPriority("PeterJack_1190@example.com"));
        assertTrue(Priority.isValidPriority("a@bc")); // minimal
        assertTrue(Priority.isValidPriority("test@localhost")); // alphabets only
        assertTrue(Priority.isValidPriority("!#$%&'*+/=?`{|}~^.-@example.org")); // special characters local part
        assertTrue(Priority.isValidPriority("123@145")); // numeric local part and domain name
        assertTrue(Priority.isValidPriority("a1+be!@example1.com")); // mixture of alphanumeric and special characters
        assertTrue(Priority.isValidPriority("peter_jack@very-very-very-long-example.com")); // long domain name
        assertTrue(Priority.isValidPriority("if.you.dream.it_you.can.do.it@example.com")); // long local part
        */


        // null priority
        Assert.assertThrows(NullPointerException.class, () -> Priority.isValidPriority(null));

        // blank priority
        assertFalse(Priority.isValidPriority("")); // empty string
        assertFalse(Priority.isValidPriority(" ")); // spaces only

        // invalid priority
        assertFalse(Priority.isValidPriority("a")); // alphabet is invalid
        assertFalse(Priority.isValidPriority("@")); // special character is invalid
        assertFalse(Priority.isValidPriority("~")); // special character is invalid
        assertFalse(Priority.isValidPriority("4")); // numerical value outside 1,2,3 is invalid.
        assertFalse(Priority.isValidPriority("11")); // valid numerical values but more than one digit
        assertFalse(Priority.isValidPriority("22")); // valid numerical values but more than one digit
        assertFalse(Priority.isValidPriority("33")); // valid numerical values but more than one digit
        assertFalse(Priority.isValidPriority("a1")); // mix of alphanumerical
        assertFalse(Priority.isValidPriority("a@")); // mix of alphabet and special character
        assertFalse(Priority.isValidPriority("1@")); // mix of numerical and special character
        assertFalse(Priority.isValidPriority("a1@")); // mix of alphanumerical and special character

        // valid priority
        assertTrue(Priority.isValidPriority("1"));
        assertTrue(Priority.isValidPriority("2"));
        assertTrue(Priority.isValidPriority("3"));

    }
}
