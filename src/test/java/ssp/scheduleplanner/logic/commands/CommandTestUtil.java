package ssp.scheduleplanner.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ssp.scheduleplanner.logic.parser.CliSyntax.PREFIX_DATE;
import static ssp.scheduleplanner.logic.parser.CliSyntax.PREFIX_NAME;
import static ssp.scheduleplanner.logic.parser.CliSyntax.PREFIX_PRIORITY;
import static ssp.scheduleplanner.logic.parser.CliSyntax.PREFIX_TAG;
import static ssp.scheduleplanner.logic.parser.CliSyntax.PREFIX_VENUE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ssp.scheduleplanner.commons.core.index.Index;
import ssp.scheduleplanner.logic.CommandHistory;
import ssp.scheduleplanner.logic.commands.exceptions.CommandException;
import ssp.scheduleplanner.model.Model;
import ssp.scheduleplanner.model.SchedulePlanner;
import ssp.scheduleplanner.model.task.NameContainsKeywordsPredicate;
import ssp.scheduleplanner.model.task.Task;
import ssp.scheduleplanner.testutil.EditTaskDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_DATE_AMY = "111111";
    public static final String VALID_DATE_BOB = "221222";
    public static final String VALID_EMAIL_AMY = "1";
    public static final String VALID_EMAIL_BOB = "2";
    public static final String VALID_VENUE_AMY = "Block 312, Amy Street 1";
    public static final String VALID_ADDRESS_BOB = "Block 123, Bobby Street 3";
    public static final String VALID_TAG_HUSBAND = "husband";
    public static final String VALID_TAG_FRIEND = "friend";

    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;
    public static final String DATE_DESC_AMY = " " + PREFIX_DATE + VALID_DATE_AMY;
    public static final String DATE_DESC_BOB = " " + PREFIX_DATE + VALID_DATE_BOB;
    public static final String EMAIL_DESC_AMY = " " + PREFIX_PRIORITY + VALID_EMAIL_AMY;
    public static final String EMAIL_DESC_BOB = " " + PREFIX_PRIORITY + VALID_EMAIL_BOB;
    public static final String
            VENUE_DESC_AMY = " " + PREFIX_VENUE + VALID_VENUE_AMY;
    public static final String VENUE_DESC_BOB = " " + PREFIX_VENUE + VALID_ADDRESS_BOB;
    public static final String TAG_DESC_FRIEND = " " + PREFIX_TAG + VALID_TAG_FRIEND;
    public static final String TAG_DESC_HUSBAND = " " + PREFIX_TAG + VALID_TAG_HUSBAND;

    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    public static final String INVALID_DATE_DESC = " " + PREFIX_DATE + "911a"; // 'a' not allowed in dates
    public static final String INVALID_EMAIL_DESC = " " + PREFIX_PRIORITY + "bob!yahoo"; // missing '@' symbol
    public static final String INVALID_VENUE_DESC = " " + PREFIX_VENUE; // empty string not allowed for addresses
    public static final String INVALID_TAG_DESC = " " + PREFIX_TAG + "hubby*"; // '*' not allowed in tags

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditCommand.EditTaskDescriptor DESC_AMY;
    public static final EditCommand.EditTaskDescriptor DESC_BOB;

    static {
        DESC_AMY = new EditTaskDescriptorBuilder().withName(VALID_NAME_AMY)
                .withDate(VALID_DATE_AMY).withEmail(VALID_EMAIL_AMY).withAddress(VALID_VENUE_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        DESC_BOB = new EditTaskDescriptorBuilder().withName(VALID_NAME_BOB)
                .withDate(VALID_DATE_BOB).withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB)
                .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the result message matches {@code expectedMessage} <br>
     * - the {@code actualModel} matches {@code expectedModel} <br>
     * - the {@code actualCommandHistory} remains unchanged.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandHistory actualCommandHistory,
            String expectedMessage, Model expectedModel) {
        CommandHistory expectedCommandHistory = new CommandHistory(actualCommandHistory);
        try {
            CommandResult result = command.execute(actualModel, actualCommandHistory);
            assertEquals(expectedMessage, result.feedbackToUser);
            assertEquals(expectedModel, actualModel);
            assertEquals(expectedCommandHistory, actualCommandHistory);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the schedule planner and the filtered task list in the {@code actualModel} remain unchanged <br>
     * - {@code actualCommandHistory} remains unchanged.
     */
    public static void assertCommandFailure(Command command, Model actualModel, CommandHistory actualCommandHistory,
            String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        SchedulePlanner expectedSchedulePlanner = new SchedulePlanner(actualModel.getSchedulePlanner());
        List<Task> expectedFilteredList = new ArrayList<>(actualModel.getFilteredTaskList());

        CommandHistory expectedCommandHistory = new CommandHistory(actualCommandHistory);

        try {
            command.execute(actualModel, actualCommandHistory);
            throw new AssertionError("The expected CommandException was not thrown.");
        } catch (CommandException e) {
            assertEquals(expectedMessage, e.getMessage());
            assertEquals(expectedSchedulePlanner, actualModel.getSchedulePlanner());
            assertEquals(expectedFilteredList, actualModel.getFilteredTaskList());
            assertEquals(expectedCommandHistory, actualCommandHistory);
        }
    }

    /**
     * Updates {@code model}'s filtered list to show only the task at the given {@code targetIndex} in the
     * {@code model}'s schedule planner.
     */
    public static void showTaskAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredTaskList().size());

        Task task = model.getFilteredTaskList().get(targetIndex.getZeroBased());
        final String[] splitName = task.getName().fullName.split("\\s+");
        model.updateFilteredTaskList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredTaskList().size());
    }

    /**
     * Deletes the first task in {@code model}'s filtered list from {@code model}'s schedule planner.
     */
    public static void deleteFirstTask(Model model) {
        Task firstTask = model.getFilteredTaskList().get(0);
        model.deleteTask(firstTask);
        model.commitSchedulePlanner();
    }

}