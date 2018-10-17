package ssp.scheduleplanner.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static ssp.scheduleplanner.testutil.TypicalTasks.getTypicalSchedulePlanner;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ssp.scheduleplanner.model.Model;
import ssp.scheduleplanner.model.ModelManager;
import ssp.scheduleplanner.model.UserPrefs;
import ssp.scheduleplanner.model.task.DateWeekSamePredicate;
import ssp.scheduleplanner.model.task.Task;
import ssp.scheduleplanner.testutil.TaskBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListWeekCommand.
 */
public class ListWeekCommandTest {

    @Test
    public void task_remain_afterFilter() {
        //after update the filteredtasklist with a specific date predicate, model would have that task(s) from that date
        //till the closest sunday.
        //131018 is a saturday -> task from 131018 and 141018 will remain.
        Model model = new ModelManager(getTypicalSchedulePlanner(), new UserPrefs());
        Model modelCheck = new ModelManager();
        Task validTaskSat = new TaskBuilder().withDate("131018").build();
        Task validTaskSun = new TaskBuilder().withDate("141018").build();

        List<String> dateList = new ArrayList<String>();
        dateList.add("131018");
        dateList.add("141018");

        model.addTask(validTaskSat);
        model.addTask(validTaskSun);
        modelCheck.addTask(validTaskSat);
        modelCheck.addTask(validTaskSun);

        model.updateFilteredTaskList(new DateWeekSamePredicate(dateList));

        assertTrue(model.getFilteredTaskList().equals(modelCheck.getFilteredTaskList()));
        assertTrue(model.getFilteredTaskList().contains(validTaskSat));
        assertTrue(model.getFilteredTaskList().contains(validTaskSun));
    }

    @Test
    public void task_gone_afterFilter() {
        //after update the filteredtasklist with a specific date predicate, model would have that task(s) from that date
        //till the closest sunday and not other days.
        //131018 is a saturday -> task from 131018 and 141018 will remain
        //131018 is a saturday -> task from 081018(monday) till 121018(friday) will not remain
        Model model = new ModelManager();
        Model modelCheck = new ModelManager();
        Task validTaskMon = new TaskBuilder().withDate("081018").build();
        Task validTaskTue = new TaskBuilder().withDate("091018").build();
        Task validTaskWed = new TaskBuilder().withDate("101018").build();
        Task validTaskThu = new TaskBuilder().withDate("111018").build();
        Task validTaskFri = new TaskBuilder().withDate("121018").build();
        Task validTaskSat = new TaskBuilder().withDate("131018").build();
        Task validTaskSun = new TaskBuilder().withDate("141018").build();

        model.addTask(validTaskMon);
        model.addTask(validTaskTue);
        model.addTask(validTaskWed);
        model.addTask(validTaskThu);
        model.addTask(validTaskFri);
        model.addTask(validTaskSat);
        model.addTask(validTaskSun);
        modelCheck.addTask(validTaskMon);
        modelCheck.addTask(validTaskTue);
        modelCheck.addTask(validTaskWed);
        modelCheck.addTask(validTaskThu);
        modelCheck.addTask(validTaskFri);

        List<String> dateList = new ArrayList<String>();
        dateList.add("131018");
        dateList.add("141018");
        model.updateFilteredTaskList(new DateWeekSamePredicate(dateList));

        System.out.println(model.getFilteredTaskList());
        assertFalse(model.getFilteredTaskList().contains(validTaskMon));
        assertFalse(model.getFilteredTaskList().contains(validTaskTue));
        assertFalse(model.getFilteredTaskList().contains(validTaskWed));
        assertFalse(model.getFilteredTaskList().contains(validTaskThu));
        assertFalse(model.getFilteredTaskList().contains(validTaskFri));

        assertTrue(model.getFilteredTaskList().contains(validTaskSat));
        assertTrue(model.getFilteredTaskList().contains(validTaskSun));
    }
}
