package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.SchedulePlanner;
import seedu.address.model.task.Task;

/**
 * A utility class containing a list of {@code Task} objects to be used in tests.
 */
public class TypicalTasks {

    public static final Task ALICE = new TaskBuilder().withName("Alice Pauline")
            .withAddress("123, Jurong West Ave 6, #08-111").withEmail("1")
            .withPhone("94351253")
            .withTags("friends").build();
    public static final Task BENSON = new TaskBuilder().withName("Benson Meier")
            .withAddress("311, Clementi Ave 2, #02-25")
            .withEmail("2").withPhone("98765432")
            .withTags("owesMoney", "friends").build();
    public static final Task CARL = new TaskBuilder().withName("Carl Kurz").withPhone("95352563")
            .withEmail("3").withAddress("wall street").build();
    public static final Task DANIEL = new TaskBuilder().withName("Daniel Meier").withPhone("87652533")
            .withEmail("1").withAddress("10th street").withTags("friends").build();
    public static final Task ELLE = new TaskBuilder().withName("Elle Meyer").withPhone("9482224")
            .withEmail("2").withAddress("michegan ave").build();
    public static final Task FIONA = new TaskBuilder().withName("Fiona Kunz").withPhone("9482427")
            .withEmail("3").withAddress("little tokyo").build();
    public static final Task GEORGE = new TaskBuilder().withName("George Best").withPhone("9482442")
            .withEmail("1").withAddress("4th street").build();

    // Manually added
    public static final Task HOON = new TaskBuilder().withName("Hoon Meier").withPhone("8482424")
            .withEmail("1").withAddress("little india").build();
    public static final Task IDA = new TaskBuilder().withName("Ida Mueller").withPhone("8482131")
            .withEmail("2").withAddress("chicago ave").build();

    // Manually added - Task's details found in {@code CommandTestUtil}
    public static final Task AMY = new TaskBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY).withTags(VALID_TAG_FRIEND).build();
    public static final Task BOB = new TaskBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND)
            .build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalTasks() {} // prevents instantiation

    /**
     * Returns an {@code SchedulePlanner} with all the typical persons.
     */
    public static SchedulePlanner getTypicalAddressBook() {
        SchedulePlanner ab = new SchedulePlanner();
        for (Task task : getTypicalPersons()) {
            ab.addTask(task);
        }
        return ab;
    }

    public static List<Task> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
