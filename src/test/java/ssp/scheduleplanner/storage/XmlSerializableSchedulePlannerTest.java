package ssp.scheduleplanner.storage;

import static org.junit.Assert.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import ssp.scheduleplanner.commons.exceptions.IllegalValueException;
import ssp.scheduleplanner.commons.util.XmlUtil;
import ssp.scheduleplanner.model.SchedulePlanner;
import ssp.scheduleplanner.testutil.TypicalTasks;

public class XmlSerializableSchedulePlannerTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "XmlSerializableSchedulePlannerTest");
    private static final Path TYPICAL_TASK_FILE = TEST_DATA_FOLDER.resolve("typicalTasksSchedulePlanner.xml");
    private static final Path INVALID_TASK_FILE = TEST_DATA_FOLDER.resolve("invalidTaskSchedulePlanner.xml");
    private static final Path DUPLICATE_TASK_FILE = TEST_DATA_FOLDER.resolve("duplicateTaskSchedulePlanner.xml");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void toModelType_typicalTasksFile_success() throws Exception {
        XmlSerializableSchedulePlanner dataFromFile = XmlUtil.getDataFromFile(TYPICAL_TASK_FILE,
                XmlSerializableSchedulePlanner.class);
        SchedulePlanner schedulePlannerFromFile = dataFromFile.toModelType();
        SchedulePlanner typicalTasksSchedulePlanner = TypicalTasks.getTypicalSchedulePlanner();
        assertEquals(schedulePlannerFromFile, typicalTasksSchedulePlanner);
    }

    @Test
    public void toModelType_invalidTaskFile_throwsIllegalValueException() throws Exception {
        XmlSerializableSchedulePlanner dataFromFile = XmlUtil.getDataFromFile(INVALID_TASK_FILE,
                XmlSerializableSchedulePlanner.class);
        thrown.expect(IllegalValueException.class);
        dataFromFile.toModelType();
    }

    @Test
    public void toModelType_duplicateTasks_throwsIllegalValueException() throws Exception {
        XmlSerializableSchedulePlanner dataFromFile = XmlUtil.getDataFromFile(DUPLICATE_TASK_FILE,
                XmlSerializableSchedulePlanner.class);
        thrown.expect(IllegalValueException.class);
        thrown.expectMessage(XmlSerializableSchedulePlanner.MESSAGE_DUPLICATE_TASK);
        dataFromFile.toModelType();
    }

}
