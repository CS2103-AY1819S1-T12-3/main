package systemtests;

import static ssp.scheduleplanner.ui.testutil.GuiTestAssert.assertListMatching;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;

import ssp.scheduleplanner.model.SchedulePlanner;
import ssp.scheduleplanner.model.task.Task;
import ssp.scheduleplanner.model.util.SampleDataUtil;
import ssp.scheduleplanner.testutil.TestUtil;

public class SampleDataTest extends SchedulePlannerSystemTest {
    /**
     * Returns null to force test app to load data of the file in {@code getDataFileLocation()}.
     */
    @Override
    protected SchedulePlanner getInitialData() {
        return null;
    }

    /**
     * Returns a non-existent file location to force test app to load sample data.
     */
    @Override
    protected Path getDataFileLocation() {
        Path filePath = TestUtil.getFilePathInSandboxFolder("SomeFileThatDoesNotExist1234567890.xml");
        deleteFileIfExists(filePath);
        return filePath;
    }

    /**
     * Deletes the file at {@code filePath} if it exists.
     */
    private void deleteFileIfExists(Path filePath) {
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException ioe) {
            throw new AssertionError(ioe);
        }
    }

    @Test
    public void schedulePlanner_dataFileDoesNotExist_loadSampleData() {
        Task[] expectedList = SampleDataUtil.getSampleTasks();
        assertListMatching(getTaskListPanel(), expectedList);
    }
}
