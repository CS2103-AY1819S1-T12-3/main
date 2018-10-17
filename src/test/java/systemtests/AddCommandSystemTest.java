package systemtests;

import static ssp.scheduleplanner.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.DATE_DESC_AMY;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.DATE_DESC_BOB;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.INVALID_DATE_DESC;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
//import static CommandTestUtil.VALID_ADDRESS_BOB;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.VALID_DATE_BOB;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.VALID_NAME_BOB;
//import static CliSyntax.PREFIX_TAG;
import static ssp.scheduleplanner.testutil.TypicalTasks.ALICE;
import static ssp.scheduleplanner.testutil.TypicalTasks.AMY;
import static ssp.scheduleplanner.testutil.TypicalTasks.BOB;
import static ssp.scheduleplanner.testutil.TypicalTasks.CARL;
import static ssp.scheduleplanner.testutil.TypicalTasks.HOON;
import static ssp.scheduleplanner.testutil.TypicalTasks.IDA;
import static ssp.scheduleplanner.testutil.TypicalTasks.KEYWORD_MATCHING_MEIER;

import org.junit.Test;

import ssp.scheduleplanner.commons.core.Messages;
import ssp.scheduleplanner.commons.core.index.Index;
import ssp.scheduleplanner.logic.commands.AddCommand;
import ssp.scheduleplanner.logic.commands.RedoCommand;
import ssp.scheduleplanner.logic.commands.UndoCommand;
import ssp.scheduleplanner.model.Model;
import ssp.scheduleplanner.model.tag.Tag;
import ssp.scheduleplanner.model.task.Date;
import ssp.scheduleplanner.model.task.Name;
import ssp.scheduleplanner.model.task.Priority;
import ssp.scheduleplanner.model.task.Task;
import ssp.scheduleplanner.model.task.Venue;
import ssp.scheduleplanner.testutil.TaskBuilder;
import ssp.scheduleplanner.testutil.TaskUtil;


public class AddCommandSystemTest extends SchedulePlannerSystemTest {

    @Test
    public void add() {
        Model model = getModel();

        /* ------------------------ Perform add operations on the shown unfiltered list ----------------------------- */

        /* Case: add a task without tags to a non-empty address book, command with leading spaces and trailing spaces
         * -> added
         */
        Task toAdd = AMY;
        String command = "   " + AddCommand.COMMAND_WORD + "  " + NAME_DESC_AMY + "  " + DATE_DESC_AMY + " "
                + EMAIL_DESC_AMY + "   " + ADDRESS_DESC_AMY + "   " + TAG_DESC_FRIEND + " ";
        assertCommandSuccess(command, toAdd);

        /* Case: undo adding Amy to the list -> Amy deleted */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: redo adding Amy to the list -> Amy added again */
        command = RedoCommand.COMMAND_WORD;
        model.addTask(toAdd);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: add a task with all fields same as another task in the address book except name -> added */
        toAdd = new TaskBuilder(AMY).withName(VALID_NAME_BOB).build();
        command = AddCommand.COMMAND_WORD + NAME_DESC_BOB + DATE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + TAG_DESC_FRIEND;
        assertCommandSuccess(command, toAdd);

        /* Case: add a task with all fields same as another task in the address book except date and email
         * -> added
         */
        toAdd = new TaskBuilder(AMY).withDate(VALID_DATE_BOB).withEmail(VALID_EMAIL_BOB).build();
        command = TaskUtil.getAddCommand(toAdd);
        assertCommandSuccess(command, toAdd);

        /* Case: add to empty address book -> added */
        deleteAllTasks();
        assertCommandSuccess(ALICE);

        /* Case: add a task with tags, command with parameters in random order -> added */
        toAdd = BOB;
        command = AddCommand.COMMAND_WORD + TAG_DESC_FRIEND + DATE_DESC_BOB + ADDRESS_DESC_BOB + NAME_DESC_BOB
                + TAG_DESC_HUSBAND + EMAIL_DESC_BOB;
        assertCommandSuccess(command, toAdd);

        /* Case: add a task, missing tags -> added */
        assertCommandSuccess(HOON);

        /* -------------------------- Perform add operation on the shown filtered list ------------------------------ */

        /* Case: filters the task list before adding -> added */
        showTasksWithName(KEYWORD_MATCHING_MEIER);
        assertCommandSuccess(IDA);

        /* ------------------------ Perform add operation while a task card is selected --------------------------- */

        /* Case: selects first card in the task list, add a task -> added, card selection remains unchanged */
        selectTask(Index.fromOneBased(1));
        assertCommandSuccess(CARL);

        /* ----------------------------------- Perform invalid add operations --------------------------------------- */

        // OLD TESTS
        /* Case: add a duplicate task -> rejected */
        //command = TaskUtil.getAddCommand(HOON);
        //assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_TASK);

        /* Case: add a duplicate task except with different date -> rejected */
        //commented off as task are unique when one field is different
        //toAdd = new TaskBuilder(HOON).withDate(VALID_DATE_BOB).build();
        //command = TaskUtil.getAddCommand(toAdd);
        //assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_TASK);

        /* Case: add a duplicate task except with different email -> rejected */
        //toAdd = new TaskBuilder(HOON).withEmail(VALID_EMAIL_BOB).build();
        //command = TaskUtil.getAddCommand(toAdd);
        //assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_TASK);

        /* Case: add a duplicate task except with different address -> rejected */
        //commented off as task are unique when one field is different
        //toAdd = new TaskBuilder(HOON).withAddress(VALID_ADDRESS_BOB).build();
        //command = TaskUtil.getAddCommand(toAdd);
        //assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_TASK);

        /* Case: add a duplicate task except with different tags -> rejected */
        //commented off as task are unique when one field is different
        //command = TaskUtil.getAddCommand(HOON) + " " + PREFIX_TAG.getPrefix() + "friends";
        //assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_TASK);
        // OLD TESTS

        /* Case: missing name -> rejected */
        command = AddCommand.COMMAND_WORD + DATE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: missing date -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: missing email -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + DATE_DESC_AMY + ADDRESS_DESC_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: missing address -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + DATE_DESC_AMY + EMAIL_DESC_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: invalid keyword -> rejected */
        command = "adds " + TaskUtil.getTaskDetails(toAdd);
        assertCommandFailure(command, Messages.MESSAGE_UNKNOWN_COMMAND);

        /* Case: invalid name -> rejected */
        command = AddCommand.COMMAND_WORD + INVALID_NAME_DESC + DATE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY;
        assertCommandFailure(command, Name.MESSAGE_NAME_CONSTRAINTS);

        /* Case: invalid date -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + INVALID_DATE_DESC + EMAIL_DESC_AMY + ADDRESS_DESC_AMY;
        assertCommandFailure(command, Date.MESSAGE_DATE_CONSTRAINTS);

        /* Case: invalid email -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + DATE_DESC_AMY + INVALID_EMAIL_DESC + ADDRESS_DESC_AMY;
        assertCommandFailure(command, Priority.MESSAGE_PRIORITY_CONSTRAINTS);

        /* Case: invalid address -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + DATE_DESC_AMY + EMAIL_DESC_AMY + INVALID_ADDRESS_DESC;
        assertCommandFailure(command, Venue.MESSAGE_ADDRESS_CONSTRAINTS);

        /* Case: invalid tag -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + DATE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + INVALID_TAG_DESC;
        assertCommandFailure(command, Tag.MESSAGE_TAG_CONSTRAINTS);
    }

    /**
     * Executes the {@code AddCommand} that adds {@code toAdd} to the model and asserts that the,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing {@code AddCommand} with the details of
     * {@code toAdd}.<br>
     * 4. {@code Storage} and {@code TaskListPanel} equal to the corresponding components in
     * the current model added with {@code toAdd}.<br>
     * 5. Browser url and selected card remain unchanged.<br>
     * 6. Status bar's sync status changes.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code SchedulePlannerSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see SchedulePlannerSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(Task toAdd) {
        assertCommandSuccess(TaskUtil.getAddCommand(toAdd), toAdd);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(Task)}. Executes {@code command}
     * instead.
     * @see AddCommandSystemTest#assertCommandSuccess(Task)
     */
    private void assertCommandSuccess(String command, Task toAdd) {
        Model expectedModel = getModel();
        expectedModel.addTask(toAdd);
        String expectedResultMessage = String.format(AddCommand.MESSAGE_SUCCESS, toAdd);

        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Task)} except asserts that
     * the,<br>
     * 1. Result display box displays {@code expectedResultMessage}.<br>
     * 2. {@code Storage} and {@code TaskListPanel} equal to the corresponding components in
     * {@code expectedModel}.<br>
     * @see AddCommandSystemTest#assertCommandSuccess(String, Task)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays {@code command}.<br>
     * 2. Command box has the error style class.<br>
     * 3. Result display box displays {@code expectedResultMessage}.<br>
     * 4. {@code Storage} and {@code TaskListPanel} remain unchanged.<br>
     * 5. Browser url, selected card and status bar remain unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code SchedulePlannerSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see SchedulePlannerSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
