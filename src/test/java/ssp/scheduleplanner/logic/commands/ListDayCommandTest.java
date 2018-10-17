package ssp.scheduleplanner.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static ssp.scheduleplanner.testutil.TypicalTasks.getTypicalSchedulePlanner;

import org.junit.Test;

import ssp.scheduleplanner.model.Model;
import ssp.scheduleplanner.model.ModelManager;
import ssp.scheduleplanner.model.UserPrefs;
import ssp.scheduleplanner.model.task.DateSamePredicate;
import ssp.scheduleplanner.model.task.Task;
import ssp.scheduleplanner.testutil.TaskBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListDayCommand.
 */
public class ListDayCommandTest {

    @Test
    public void task_remain_afterFilter() {
        //after update the filteredtasklist with a specific date predicate, model would have that task remaining
        Model model = new ModelManager(getTypicalSchedulePlanner(), new UserPrefs());
        Task validTask = new TaskBuilder().withDate("111111").build();

        model.addTask(validTask);
        model.updateFilteredTaskList(new DateSamePredicate(validTask.getDate().value));

        assertTrue(model.hasTask(validTask));
        assertTrue(model.getFilteredTaskList().contains(validTask));
    }

    @Test
    public void task_gone_afterFilter() {
        //after update the filteredtasklist with a specific date predicate, model would not have other task remaining
        Model model = new ModelManager();
        Model modelCheck = new ModelManager();
        Task validTask = new TaskBuilder().withDate("111111").build();
        Task validTaskCheck = new TaskBuilder().withDate("121212").build();

        model.addTask(validTask);
        model.addTask(validTaskCheck);
        modelCheck.addTask(validTaskCheck);
        model.updateFilteredTaskList(new DateSamePredicate(validTask.getDate().value));

        assertFalse(model.getFilteredTaskList().equals(modelCheck.getFilteredTaskList()));
        assertFalse(model.getFilteredTaskList().contains(validTaskCheck));
    }
}
