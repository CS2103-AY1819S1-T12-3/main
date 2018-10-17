package ssp.scheduleplanner.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ssp.scheduleplanner.testutil.TypicalTasks.AMY;
import static ssp.scheduleplanner.testutil.TypicalTasks.BOB;
import static ssp.scheduleplanner.testutil.TypicalTasks.CARL;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import ssp.scheduleplanner.testutil.SchedulePlannerBuilder;

public class VersionedSchedulePlannerTest {

    private final ReadOnlySchedulePlanner schedulePlannerWithAmy = new SchedulePlannerBuilder().withTask(AMY).build();
    private final ReadOnlySchedulePlanner schedulePlannerWithBob = new SchedulePlannerBuilder().withTask(BOB).build();
    private final ReadOnlySchedulePlanner schedulePlannerWithCarl = new SchedulePlannerBuilder().withTask(CARL).build();
    private final ReadOnlySchedulePlanner emptySchedulePlanner = new SchedulePlannerBuilder().build();

    @Test
    public void commit_singleSchedulePlanner_noStatesRemovedCurrentStateSaved() {
        VersionedSchedulePlanner versionedSchedulePlanner = prepareSchedulePlannerList(emptySchedulePlanner);

        versionedSchedulePlanner.commit();
        assertSchedulePlannerListStatus(versionedSchedulePlanner,
                Collections.singletonList(emptySchedulePlanner),
                emptySchedulePlanner,
                Collections.emptyList());
    }

    @Test
    public void commit_multipleSchedulePlannerPointerAtEndOfStateList_noStatesRemovedCurrentStateSaved() {
        VersionedSchedulePlanner versionedSchedulePlanner = prepareSchedulePlannerList(
                emptySchedulePlanner, schedulePlannerWithAmy, schedulePlannerWithBob);

        versionedSchedulePlanner.commit();
        assertSchedulePlannerListStatus(versionedSchedulePlanner,
                Arrays.asList(emptySchedulePlanner, schedulePlannerWithAmy, schedulePlannerWithBob),
                schedulePlannerWithBob,
                Collections.emptyList());
    }

    @Test
    public void commit_multipleSchedulePlannerPointerNotAtEndOfStateList_statesAfterPointerRemovedCurrentStateSaved() {
        VersionedSchedulePlanner versionedSchedulePlanner = prepareSchedulePlannerList(
                emptySchedulePlanner, schedulePlannerWithAmy, schedulePlannerWithBob);
        shiftCurrentStatePointerLeftwards(versionedSchedulePlanner, 2);

        versionedSchedulePlanner.commit();
        assertSchedulePlannerListStatus(versionedSchedulePlanner,
                Collections.singletonList(emptySchedulePlanner),
                emptySchedulePlanner,
                Collections.emptyList());
    }

    @Test
    public void canUndo_multipleSchedulePlannerPointerAtEndOfStateList_returnsTrue() {
        VersionedSchedulePlanner versionedSchedulePlanner = prepareSchedulePlannerList(
                emptySchedulePlanner, schedulePlannerWithAmy, schedulePlannerWithBob);

        assertTrue(versionedSchedulePlanner.canUndo());
    }

    @Test
    public void canUndo_multipleSchedulePlannerPointerAtStartOfStateList_returnsTrue() {
        VersionedSchedulePlanner versionedSchedulePlanner = prepareSchedulePlannerList(
                emptySchedulePlanner, schedulePlannerWithAmy, schedulePlannerWithBob);
        shiftCurrentStatePointerLeftwards(versionedSchedulePlanner, 1);

        assertTrue(versionedSchedulePlanner.canUndo());
    }

    @Test
    public void canUndo_singleSchedulePlanner_returnsFalse() {
        VersionedSchedulePlanner versionedSchedulePlanner = prepareSchedulePlannerList(emptySchedulePlanner);

        assertFalse(versionedSchedulePlanner.canUndo());
    }

    @Test
    public void canUndo_multipleSchedulePlannerPointerAtStartOfStateList_returnsFalse() {
        VersionedSchedulePlanner versionedSchedulePlanner = prepareSchedulePlannerList(
                emptySchedulePlanner, schedulePlannerWithAmy, schedulePlannerWithBob);
        shiftCurrentStatePointerLeftwards(versionedSchedulePlanner, 2);

        assertFalse(versionedSchedulePlanner.canUndo());
    }

    @Test
    public void canRedo_multipleSchedulePlannerPointerNotAtEndOfStateList_returnsTrue() {
        VersionedSchedulePlanner versionedSchedulePlanner = prepareSchedulePlannerList(
                emptySchedulePlanner, schedulePlannerWithAmy, schedulePlannerWithBob);
        shiftCurrentStatePointerLeftwards(versionedSchedulePlanner, 1);

        assertTrue(versionedSchedulePlanner.canRedo());
    }

    @Test
    public void canRedo_multipleSchedulePlannerPointerAtStartOfStateList_returnsTrue() {
        VersionedSchedulePlanner versionedSchedulePlanner = prepareSchedulePlannerList(
                emptySchedulePlanner, schedulePlannerWithAmy, schedulePlannerWithBob);
        shiftCurrentStatePointerLeftwards(versionedSchedulePlanner, 2);

        assertTrue(versionedSchedulePlanner.canRedo());
    }

    @Test
    public void canRedo_singleSchedulePlanner_returnsFalse() {
        VersionedSchedulePlanner versionedSchedulePlanner = prepareSchedulePlannerList(emptySchedulePlanner);

        assertFalse(versionedSchedulePlanner.canRedo());
    }

    @Test
    public void canRedo_multipleSchedulePlannerPointerAtEndOfStateList_returnsFalse() {
        VersionedSchedulePlanner versionedSchedulePlanner = prepareSchedulePlannerList(
                emptySchedulePlanner, schedulePlannerWithAmy, schedulePlannerWithBob);

        assertFalse(versionedSchedulePlanner.canRedo());
    }

    @Test
    public void undo_multipleSchedulePlannerPointerAtEndOfStateList_success() {
        VersionedSchedulePlanner versionedSchedulePlanner = prepareSchedulePlannerList(
                emptySchedulePlanner, schedulePlannerWithAmy, schedulePlannerWithBob);

        versionedSchedulePlanner.undo();
        assertSchedulePlannerListStatus(versionedSchedulePlanner,
                Collections.singletonList(emptySchedulePlanner),
                schedulePlannerWithAmy,
                Collections.singletonList(schedulePlannerWithBob));
    }

    @Test
    public void undo_multipleSchedulePlannerPointerNotAtStartOfStateList_success() {
        VersionedSchedulePlanner versionedSchedulePlanner = prepareSchedulePlannerList(
                emptySchedulePlanner, schedulePlannerWithAmy, schedulePlannerWithBob);
        shiftCurrentStatePointerLeftwards(versionedSchedulePlanner, 1);

        versionedSchedulePlanner.undo();
        assertSchedulePlannerListStatus(versionedSchedulePlanner,
                Collections.emptyList(),
                emptySchedulePlanner,
                Arrays.asList(schedulePlannerWithAmy, schedulePlannerWithBob));
    }

    @Test
    public void undo_singleSchedulePlanner_throwsNoUndoableStateException() {
        VersionedSchedulePlanner versionedSchedulePlanner = prepareSchedulePlannerList(emptySchedulePlanner);

        assertThrows(VersionedSchedulePlanner.NoUndoableStateException.class, versionedSchedulePlanner::undo);
    }

    @Test
    public void undo_multipleSchedulePlannerPointerAtStartOfStateList_throwsNoUndoableStateException() {
        VersionedSchedulePlanner versionedSchedulePlanner = prepareSchedulePlannerList(
                emptySchedulePlanner, schedulePlannerWithAmy, schedulePlannerWithBob);
        shiftCurrentStatePointerLeftwards(versionedSchedulePlanner, 2);

        assertThrows(VersionedSchedulePlanner.NoUndoableStateException.class, versionedSchedulePlanner::undo);
    }

    @Test
    public void redo_multipleSchedulePlannerPointerNotAtEndOfStateList_success() {
        VersionedSchedulePlanner versionedSchedulePlanner = prepareSchedulePlannerList(
                emptySchedulePlanner, schedulePlannerWithAmy, schedulePlannerWithBob);
        shiftCurrentStatePointerLeftwards(versionedSchedulePlanner, 1);

        versionedSchedulePlanner.redo();
        assertSchedulePlannerListStatus(versionedSchedulePlanner,
                Arrays.asList(emptySchedulePlanner, schedulePlannerWithAmy),
                schedulePlannerWithBob,
                Collections.emptyList());
    }

    @Test
    public void redo_multipleSchedulePlannerPointerAtStartOfStateList_success() {
        VersionedSchedulePlanner versionedSchedulePlanner = prepareSchedulePlannerList(
                emptySchedulePlanner, schedulePlannerWithAmy, schedulePlannerWithBob);
        shiftCurrentStatePointerLeftwards(versionedSchedulePlanner, 2);

        versionedSchedulePlanner.redo();
        assertSchedulePlannerListStatus(versionedSchedulePlanner,
                Collections.singletonList(emptySchedulePlanner),
                schedulePlannerWithAmy,
                Collections.singletonList(schedulePlannerWithBob));
    }

    @Test
    public void redo_singleSchedulePlanner_throwsNoRedoableStateException() {
        VersionedSchedulePlanner versionedSchedulePlanner = prepareSchedulePlannerList(emptySchedulePlanner);

        assertThrows(VersionedSchedulePlanner.NoRedoableStateException.class, versionedSchedulePlanner::redo);
    }

    @Test
    public void redo_multipleSchedulePlannerPointerAtEndOfStateList_throwsNoRedoableStateException() {
        VersionedSchedulePlanner versionedSchedulePlanner = prepareSchedulePlannerList(
                emptySchedulePlanner, schedulePlannerWithAmy, schedulePlannerWithBob);

        assertThrows(VersionedSchedulePlanner.NoRedoableStateException.class, versionedSchedulePlanner::redo);
    }

    @Test
    public void equals() {
        VersionedSchedulePlanner versionedSchedulePlanner = prepareSchedulePlannerList(schedulePlannerWithAmy,
                schedulePlannerWithBob);

        // same values -> returns true
        VersionedSchedulePlanner copy = prepareSchedulePlannerList(schedulePlannerWithAmy, schedulePlannerWithBob);
        assertTrue(versionedSchedulePlanner.equals(copy));

        // same object -> returns true
        assertTrue(versionedSchedulePlanner.equals(versionedSchedulePlanner));

        // null -> returns false
        assertFalse(versionedSchedulePlanner.equals(null));

        // different types -> returns false
        assertFalse(versionedSchedulePlanner.equals(1));

        // different state list -> returns false
        VersionedSchedulePlanner differentSchedulePlannerList = prepareSchedulePlannerList(schedulePlannerWithBob,
                schedulePlannerWithCarl);
        assertFalse(versionedSchedulePlanner.equals(differentSchedulePlannerList));

        // different current pointer index -> returns false
        VersionedSchedulePlanner differentCurrentStatePointer = prepareSchedulePlannerList(
                schedulePlannerWithAmy, schedulePlannerWithBob);
        shiftCurrentStatePointerLeftwards(versionedSchedulePlanner, 1);
        assertFalse(versionedSchedulePlanner.equals(differentCurrentStatePointer));
    }

    /**
     * Asserts that {@code versionedSchedulePlanner} is currently pointing at {@code expectedCurrentState}, states
     * before {@code versionedSchedulePlanner#currentStatePointer} is equal to {@code
     * expectedStatesBeforePointer}, and states after {@code versionedSchedulePlanner#currentStatePointer} is
     * equal to {@code expectedStatesAfterPointer}.
     */
    private void assertSchedulePlannerListStatus(VersionedSchedulePlanner versionedSchedulePlanner,
                                                 List<ReadOnlySchedulePlanner> expectedStatesBeforePointer,
                                                 ReadOnlySchedulePlanner expectedCurrentState,
                                                 List<ReadOnlySchedulePlanner> expectedStatesAfterPointer) {
        // check state currently pointing at is correct
        assertEquals(new SchedulePlanner(versionedSchedulePlanner), expectedCurrentState);

        // shift pointer to start of state list
        while (versionedSchedulePlanner.canUndo()) {
            versionedSchedulePlanner.undo();
        }

        // check states before pointer are correct
        for (ReadOnlySchedulePlanner expectedSchedulePlanner : expectedStatesBeforePointer) {
            assertEquals(expectedSchedulePlanner, new SchedulePlanner(versionedSchedulePlanner));
            versionedSchedulePlanner.redo();
        }

        // check states after pointer are correct
        for (ReadOnlySchedulePlanner expectedSchedulePlanner : expectedStatesAfterPointer) {
            versionedSchedulePlanner.redo();
            assertEquals(expectedSchedulePlanner, new SchedulePlanner(versionedSchedulePlanner));
        }

        // check that there are no more states after pointer
        assertFalse(versionedSchedulePlanner.canRedo());

        // revert pointer to original position
        expectedStatesAfterPointer.forEach(unused -> versionedSchedulePlanner.undo());
    }

    /**
     * Creates and returns a {@code VersionedSchedulePlanner} with the {@code schedulePlannerStates} added into it, and
     * the
     * {@code VersionedSchedulePlanner#currentStatePointer} at the end of list.
     */
    private VersionedSchedulePlanner prepareSchedulePlannerList(ReadOnlySchedulePlanner... schedulePlannerStates) {
        assertFalse(schedulePlannerStates.length == 0);

        VersionedSchedulePlanner versionedSchedulePlanner = new VersionedSchedulePlanner(schedulePlannerStates[0]);
        for (int i = 1; i < schedulePlannerStates.length; i++) {
            versionedSchedulePlanner.resetData(schedulePlannerStates[i]);
            versionedSchedulePlanner.commit();
        }

        return versionedSchedulePlanner;
    }

    /**
     * Shifts the {@code versionedSchedulePlanner#currentStatePointer} by {@code count} to the left of its list.
     */
    private void shiftCurrentStatePointerLeftwards(VersionedSchedulePlanner versionedSchedulePlanner, int count) {
        for (int i = 0; i < count; i++) {
            versionedSchedulePlanner.undo();
        }
    }
}
