package ssp.scheduleplanner.model.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.VALID_DATE_BOB;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static ssp.scheduleplanner.testutil.TypicalTasks.ALICE;
import static ssp.scheduleplanner.testutil.TypicalTasks.BOB;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import ssp.scheduleplanner.testutil.TaskBuilder;

public class TaskTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Task task = new TaskBuilder().build();
        thrown.expect(UnsupportedOperationException.class);
        task.getTags().remove(0);
    }

    @Test
    public void isSameTask() {
        // same object -> returns true
        assertTrue(ALICE.isSameTask(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSameTask(null));

        // different date and email -> returns false
        Task editedAlice = new TaskBuilder(ALICE).withDate(VALID_DATE_BOB).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.isSameTask(editedAlice));

        // different name -> returns false
        editedAlice = new TaskBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSameTask(editedAlice));

        // same name, same date, different attributes -> returns true
        // same name, same date, different attributes -> return false as task are unique
        editedAlice = new TaskBuilder(ALICE).withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB)
                .withTags(VALID_TAG_HUSBAND).build();
        //assertTrue(ALICE.isSameTask(editedAlice));
        assertFalse(ALICE.isSameTask(editedAlice));

        // same name, same email, different attributes -> returns true
        // same name, same date, different attributes -> return false as task are unique
        editedAlice = new TaskBuilder(ALICE).withDate(VALID_DATE_BOB).withAddress(VALID_ADDRESS_BOB)
                .withTags(VALID_TAG_HUSBAND).build();
        //assertTrue(ALICE.isSameTask(editedAlice));
        assertFalse(ALICE.isSameTask(editedAlice));

        // same fields -> returns true
        // commented off as the following test below check if alice is same as alice
        //editedAlice = new TaskBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND).build();
        //assertFalse(ALICE.isSameTask(editedAlice));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Task aliceCopy = new TaskBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different task -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Task editedAlice = new TaskBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different date -> returns false
        editedAlice = new TaskBuilder(ALICE).withDate(VALID_DATE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new TaskBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new TaskBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new TaskBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));
    }
}
