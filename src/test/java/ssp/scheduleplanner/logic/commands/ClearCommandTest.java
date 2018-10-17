package ssp.scheduleplanner.logic.commands;

import static ssp.scheduleplanner.logic.commands.CommandTestUtil.assertCommandSuccess;
import static ssp.scheduleplanner.testutil.TypicalTasks.getTypicalSchedulePlanner;

import org.junit.Test;

import ssp.scheduleplanner.logic.CommandHistory;
import ssp.scheduleplanner.model.Model;
import ssp.scheduleplanner.model.ModelManager;
import ssp.scheduleplanner.model.SchedulePlanner;
import ssp.scheduleplanner.model.UserPrefs;

public class ClearCommandTest {

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_emptySchedulePlanner_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();
        expectedModel.commitSchedulePlanner();

        assertCommandSuccess(new ClearCommand(), model, commandHistory, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptySchedulePlanner_success() {
        Model model = new ModelManager(getTypicalSchedulePlanner(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalSchedulePlanner(), new UserPrefs());
        expectedModel.resetData(new SchedulePlanner());
        expectedModel.commitSchedulePlanner();

        assertCommandSuccess(new ClearCommand(), model, commandHistory, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

}
