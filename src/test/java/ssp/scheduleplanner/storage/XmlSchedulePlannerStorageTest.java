package ssp.scheduleplanner.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static ssp.scheduleplanner.testutil.TypicalTasks.ALICE;
import static ssp.scheduleplanner.testutil.TypicalTasks.HOON;
import static ssp.scheduleplanner.testutil.TypicalTasks.IDA;
import static ssp.scheduleplanner.testutil.TypicalTasks.getTypicalSchedulePlanner;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import ssp.scheduleplanner.commons.exceptions.DataConversionException;
import ssp.scheduleplanner.model.ReadOnlySchedulePlanner;
import ssp.scheduleplanner.model.SchedulePlanner;

public class XmlSchedulePlannerStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "XmlSchedulePlannerStorageTest");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readSchedulePlanner_nullFilePath_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        readSchedulePlanner(null);
    }

    private java.util.Optional<ReadOnlySchedulePlanner> readSchedulePlanner(String filePath) throws Exception {
        return new XmlSchedulePlannerStorage(Paths.get(filePath))
                .readSchedulePlanner(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readSchedulePlanner("NonExistentFile.xml").isPresent());
    }

    @Test
    public void read_notXmlFormat_exceptionThrown() throws Exception {

        thrown.expect(DataConversionException.class);
        readSchedulePlanner("NotXmlFormatSchedulePlanner.xml");

        /* IMPORTANT: Any code below an exception-throwing line (like the one above) will be ignored.
         * That means you should not have more than one exception test in one method
         */
    }

    @Test
    public void readSchedulePlanner_invalidTaskSchedulePlanner_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readSchedulePlanner("invalidTaskSchedulePlanner.xml");
    }

    @Test
    public void readSchedulePlanner_invalidAndValidTaskSchedulePlanner_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readSchedulePlanner("invalidAndValidTaskSchedulePlanner.xml");
    }

    @Test
    public void readAndSaveSchedulePlanner_allInOrder_success() throws Exception {
        Path filePath = testFolder.getRoot().toPath().resolve("TempSchedulePlanner.xml");
        SchedulePlanner original = getTypicalSchedulePlanner();
        XmlSchedulePlannerStorage xmlSchedulePlannerStorage = new XmlSchedulePlannerStorage(filePath);

        //Save in new file and read back
        xmlSchedulePlannerStorage.saveSchedulePlanner(original, filePath);
        ReadOnlySchedulePlanner readBack = xmlSchedulePlannerStorage.readSchedulePlanner(filePath).get();
        assertEquals(original, new SchedulePlanner(readBack));

        //Modify data, overwrite exiting file, and read back
        original.addTask(HOON);
        original.removeTask(ALICE);
        xmlSchedulePlannerStorage.saveSchedulePlanner(original, filePath);
        readBack = xmlSchedulePlannerStorage.readSchedulePlanner(filePath).get();
        assertEquals(original, new SchedulePlanner(readBack));

        //Save and read without specifying file path
        original.addTask(IDA);
        xmlSchedulePlannerStorage.saveSchedulePlanner(original); //file path not specified
        readBack = xmlSchedulePlannerStorage.readSchedulePlanner().get(); //file path not specified
        assertEquals(original, new SchedulePlanner(readBack));

    }

    @Test
    public void saveSchedulePlanner_nullSchedulePlanner_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveSchedulePlanner(null, "SomeFile.xml");
    }

    /**
     * Saves {@code schedulePlanner} at the specified {@code filePath}.
     */
    private void saveSchedulePlanner(ReadOnlySchedulePlanner schedulePlanner, String filePath) {
        try {
            new XmlSchedulePlannerStorage(Paths.get(filePath))
                    .saveSchedulePlanner(schedulePlanner, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveSchedulePlanner_nullFilePath_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveSchedulePlanner(new SchedulePlanner(), null);
    }


}
