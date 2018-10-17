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

public class UndoCommandTest {

    private final Model model = new ModelManager(getTypicalSchedulePlanner(), new UserPrefs());
    private final Model expectedModel = new ModelManager(getTypicalSchedulePlanner(), new UserPrefs());
    private final CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        // set up of models' undo/redo history
        deleteFirstTask(model);
        deleteFirstTask(model);

        deleteFirstTask(expectedModel);
        deleteFirstTask(expectedModel);
    }

    @Test
    public void execute() {
        // multiple undoable states in model
        expectedModel.undoSchedulePlanner();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // single undoable state in model
        expectedModel.undoSchedulePlanner();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // no undoable states in model
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
    }
}
