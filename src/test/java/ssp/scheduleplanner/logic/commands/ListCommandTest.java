package ssp.scheduleplanner.logic.commands;

import static ssp.scheduleplanner.logic.commands.CommandTestUtil.assertCommandSuccess;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.showTaskAtIndex;
import static ssp.scheduleplanner.testutil.TypicalIndexes.INDEX_FIRST_TASK;
import static ssp.scheduleplanner.testutil.TypicalTasks.getTypicalSchedulePlanner;

import org.junit.Before;
import org.junit.Test;

import ssp.scheduleplanner.logic.CommandHistory;
import ssp.scheduleplanner.model.Model;
import ssp.scheduleplanner.model.ModelManager;
import ssp.scheduleplanner.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;
    private CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalSchedulePlanner(), new UserPrefs());
        expectedModel = new ModelManager(model.getSchedulePlanner(), new UserPrefs());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        assertCommandSuccess(new ListCommand(), model, commandHistory, ListCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        showTaskAtIndex(model, INDEX_FIRST_TASK);
        assertCommandSuccess(new ListCommand(), model, commandHistory, ListCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
