package ssp.scheduleplanner.model;

import javafx.collections.ObservableList;
import ssp.scheduleplanner.model.task.Task;

/**
 * Unmodifiable view of an Schedule Planner
 */
public interface ReadOnlySchedulePlanner {

    /**
     * Returns an unmodifiable view of the tasks list.
     * This list will not contain any duplicate tasks.
     */
    ObservableList<Task> getTaskList();

}
