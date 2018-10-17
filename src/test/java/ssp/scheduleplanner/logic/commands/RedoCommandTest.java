package ssp.scheduleplanner.logic.commands;

import static ssp.scheduleplanner.logic.commands.CommandTestUtil.assertCommandFailure;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.assertCommandSuccess;
import static ssp.scheduleplanner.logic.commands.CommandTestUtil.deleteFirstTask;
import static ssp.scheduleplanner.testutil.TypicalTasks.getTypicalSchedulePlanner;

import org.junit.Before;
import org.junit.Test;

import ssp.scheduleplanner.logic.CommandHistory;
import ssp.scheduleplanner.model.Model;
import ssp.scheduleplanner.model.ModelManager;
import ssp.scheduleplanner.model.UserPrefs;

public class RedoCommandTest {

    private final Model model = new ModelManager(getTypicalSchedulePlanner(), new UserPrefs());
    private final Model expectedModel = new ModelManager(getTypicalSchedulePlanner(), new UserPrefs());
    private final CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        // set up of both models' undo/redo history
        deleteFirstTask(model);
        deleteFirstTask(model);
        model.undoSchedulePlanner();
        model.undoSchedulePlanner();

        deleteFirstTask(expectedModel);
        deleteFirstTask(expectedModel);
        expectedModel.undoSchedulePlanner();
        expectedModel.undoSchedulePlanner();
    }

    @Test
    public void execute() {
        // multiple redoable states in model
        expectedModel.redoSchedulePlanner();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);

        // single redoable state in model
        expectedModel.redoSchedulePlanner();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);

        // no redoable state in model
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }
}
