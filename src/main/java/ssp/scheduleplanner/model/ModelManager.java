package ssp.scheduleplanner.model;

import static java.util.Objects.requireNonNull;
import static ssp.scheduleplanner.commons.util.CollectionUtil.requireAllNonNull;

import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import ssp.scheduleplanner.commons.core.ComponentManager;
import ssp.scheduleplanner.commons.core.LogsCenter;
import ssp.scheduleplanner.commons.events.model.SchedulePlannerChangedEvent;
import ssp.scheduleplanner.model.task.Task;

/**
 * Represents the in-memory model of the Schedule Planner data.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final VersionedSchedulePlanner versionedSchedulePlanner;
    private final FilteredList<Task> filteredTasks;

    /**
     * Initializes a ModelManager with the given Schedule Planner and userPrefs.
     */
    public ModelManager(ReadOnlySchedulePlanner schedulePlanner, UserPrefs userPrefs) {
        super();
        requireAllNonNull(schedulePlanner, userPrefs);

        logger.fine("Initializing with Schedule Planner: " + schedulePlanner + " and user prefs " + userPrefs);

        versionedSchedulePlanner = new VersionedSchedulePlanner(schedulePlanner);
        filteredTasks = new FilteredList<>(versionedSchedulePlanner.getTaskList());
    }

    public ModelManager() {
        this(new SchedulePlanner(), new UserPrefs());
    }

    @Override
    public void resetData(ReadOnlySchedulePlanner newData) {
        versionedSchedulePlanner.resetData(newData);
        indicateSchedulePlannerChanged();
    }

    @Override
    public ReadOnlySchedulePlanner getSchedulePlanner() {
        return versionedSchedulePlanner;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateSchedulePlannerChanged() {
        raise(new SchedulePlannerChangedEvent(versionedSchedulePlanner));
    }

    @Override
    public boolean hasTask(Task task) {
        requireNonNull(task);
        return versionedSchedulePlanner.hasTask(task);
    }

    @Override
    public void deleteTask(Task target) {
        versionedSchedulePlanner.removeTask(target);
        indicateSchedulePlannerChanged();
    }

    @Override
    public void addTask(Task task) {
        versionedSchedulePlanner.addTask(task);
        updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
        indicateSchedulePlannerChanged();
    }

    @Override
    public void updateTask(Task target, Task editedTask) {
        requireAllNonNull(target, editedTask);

        versionedSchedulePlanner.updateTask(target, editedTask);
        indicateSchedulePlannerChanged();
    }

    //=========== Filtered Task List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Task} backed by the internal list of
     * {@code versionedSchedulePlanner}
     */
    @Override
    public ObservableList<Task> getFilteredTaskList() {
        return FXCollections.unmodifiableObservableList(filteredTasks);
    }

    @Override
    public void updateFilteredTaskList(Predicate<Task> predicate) {
        requireNonNull(predicate);
        filteredTasks.setPredicate(predicate);
    }

    //=========== Undo/Redo =================================================================================

    @Override
    public boolean canUndoSchedulePlanner() {
        return versionedSchedulePlanner.canUndo();
    }

    @Override
    public boolean canRedoSchedulePlanner() {
        return versionedSchedulePlanner.canRedo();
    }

    @Override
    public void undoSchedulePlanner() {
        versionedSchedulePlanner.undo();
        indicateSchedulePlannerChanged();
    }

    @Override
    public void redoSchedulePlanner() {
        versionedSchedulePlanner.redo();
        indicateSchedulePlannerChanged();
    }

    @Override
    public void commitSchedulePlanner() {
        versionedSchedulePlanner.commit();
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return versionedSchedulePlanner.equals(other.versionedSchedulePlanner)
                && filteredTasks.equals(other.filteredTasks);
    }

}
