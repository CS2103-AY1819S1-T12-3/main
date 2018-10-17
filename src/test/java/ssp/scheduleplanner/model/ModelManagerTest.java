package ssp.scheduleplanner.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static ssp.scheduleplanner.model.Model.PREDICATE_SHOW_ALL_TASKS;
import static ssp.scheduleplanner.testutil.TypicalTasks.ALICE;
import static ssp.scheduleplanner.testutil.TypicalTasks.BENSON;

import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import ssp.scheduleplanner.model.task.NameContainsKeywordsPredicate;
import ssp.scheduleplanner.testutil.SchedulePlannerBuilder;

public class ModelManagerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ModelManager modelManager = new ModelManager();

    @Test
    public void hasTask_nullTask_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        modelManager.hasTask(null);
    }

    @Test
    public void hasTask_taskNotInSchedulePlanner_returnsFalse() {
        assertFalse(modelManager.hasTask(ALICE));
    }

    @Test
    public void hasTask_taskInSchedulePlanner_returnsTrue() {
        modelManager.addTask(ALICE);
        assertTrue(modelManager.hasTask(ALICE));
    }

    @Test
    public void getFilteredTaskList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        modelManager.getFilteredTaskList().remove(0);
    }

    @Test
    public void equals() {
        SchedulePlanner schedulePlanner = new SchedulePlannerBuilder().withTask(ALICE).withTask(BENSON).build();
        SchedulePlanner differentSchedulePlanner = new SchedulePlanner();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(schedulePlanner, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(schedulePlanner, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different schedulePlanner -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentSchedulePlanner, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredTaskList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(schedulePlanner, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);

        // different userPrefs -> returns true
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setSchedulePlannerFilePath(Paths.get("differentFilePath"));
        assertTrue(modelManager.equals(new ModelManager(schedulePlanner, differentUserPrefs)));
    }
}
