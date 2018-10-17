package ssp.scheduleplanner.model;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code SchedulePlanner} that keeps track of its own history.
 */
public class VersionedSchedulePlanner extends SchedulePlanner {

    private final List<ReadOnlySchedulePlanner> schedulePlannerStateList;
    private int currentStatePointer;

    public VersionedSchedulePlanner(ReadOnlySchedulePlanner initialState) {
        super(initialState);

        schedulePlannerStateList = new ArrayList<>();
        schedulePlannerStateList.add(new SchedulePlanner(initialState));
        currentStatePointer = 0;
    }

    /**
     * Saves a copy of the current {@code SchedulePlanner} state at the end of the state list.
     * Undone states are removed from the state list.
     */
    public void commit() {
        removeStatesAfterCurrentPointer();
        schedulePlannerStateList.add(new SchedulePlanner(this));
        currentStatePointer++;
    }

    private void removeStatesAfterCurrentPointer() {
        schedulePlannerStateList.subList(currentStatePointer + 1, schedulePlannerStateList.size()).clear();
    }

    /**
     * Restores the address book to its previous state.
     */
    public void undo() {
        if (!canUndo()) {
            throw new NoUndoableStateException();
        }
        currentStatePointer--;
        resetData(schedulePlannerStateList.get(currentStatePointer));
    }

    /**
     * Restores the address book to its previously undone state.
     */
    public void redo() {
        if (!canRedo()) {
            throw new NoRedoableStateException();
        }
        currentStatePointer++;
        resetData(schedulePlannerStateList.get(currentStatePointer));
    }

    /**
     * Returns true if {@code undo()} has address book states to undo.
     */
    public boolean canUndo() {
        return currentStatePointer > 0;
    }

    /**
     * Returns true if {@code redo()} has address book states to redo.
     */
    public boolean canRedo() {
        return currentStatePointer < schedulePlannerStateList.size() - 1;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof VersionedSchedulePlanner)) {
            return false;
        }

        VersionedSchedulePlanner otherVersionedSchedulePlanner = (VersionedSchedulePlanner) other;

        // state check
        return super.equals(otherVersionedSchedulePlanner)
                && schedulePlannerStateList.equals(otherVersionedSchedulePlanner.schedulePlannerStateList)
                && currentStatePointer == otherVersionedSchedulePlanner.currentStatePointer;
    }

    /**
     * Thrown when trying to {@code undo()} but can't.
     */
    public static class NoUndoableStateException extends RuntimeException {
        private NoUndoableStateException() {
            super("Current state pointer at start of schedulePlannerState list, unable to undo.");
        }
    }

    /**
     * Thrown when trying to {@code redo()} but can't.
     */
    public static class NoRedoableStateException extends RuntimeException {
        private NoRedoableStateException() {
            super("Current state pointer at end of schedulePlannerState list, unable to redo.");
        }
    }
}
