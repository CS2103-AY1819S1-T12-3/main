package ssp.scheduleplanner.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static ssp.scheduleplanner.testutil.TypicalTasks.getTypicalSchedulePlanner;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import ssp.scheduleplanner.commons.events.model.SchedulePlannerChangedEvent;
import ssp.scheduleplanner.commons.events.storage.DataSavingExceptionEvent;
import ssp.scheduleplanner.model.ReadOnlySchedulePlanner;
import ssp.scheduleplanner.model.SchedulePlanner;
import ssp.scheduleplanner.model.UserPrefs;
import ssp.scheduleplanner.ui.testutil.EventsCollectorRule;

public class StorageManagerTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private StorageManager storageManager;

    @Before
    public void setUp() {
        XmlSchedulePlannerStorage schedulePlannerStorage = new XmlSchedulePlannerStorage(getTempFilePath("ab"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        storageManager = new StorageManager(schedulePlannerStorage, userPrefsStorage);
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.getRoot().toPath().resolve(fileName);
    }


    @Test
    public void prefsReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonUserPrefsStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
         */
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(300, 600, 4, 6);
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void schedulePlannerReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link XmlSchedulePlannerStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link XmlSchedulePlannerStorageTest} class.
         */
        SchedulePlanner original = getTypicalSchedulePlanner();
        storageManager.saveSchedulePlanner(original);
        ReadOnlySchedulePlanner retrieved = storageManager.readSchedulePlanner().get();
        assertEquals(original, new SchedulePlanner(retrieved));
    }

    @Test
    public void getSchedulePlannerFilePath() {
        assertNotNull(storageManager.getSchedulePlannerFilePath());
    }

    @Test
    public void handleSchedulePlannerChangedEvent_exceptionThrown_eventRaised() {
        // Create a StorageManager while injecting a stub that  throws an exception when the save method is called
        Storage storage = new StorageManager(new XmlSchedulePlannerStorageExceptionThrowingStub(Paths.get("dummy")),
                                             new JsonUserPrefsStorage(Paths.get("dummy")));
        storage.handleSchedulePlannerChangedEvent(new SchedulePlannerChangedEvent(new SchedulePlanner()));
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof DataSavingExceptionEvent);
    }


    /**
     * A Stub class to throw an exception when the save method is called
     */
    class XmlSchedulePlannerStorageExceptionThrowingStub extends XmlSchedulePlannerStorage {

        public XmlSchedulePlannerStorageExceptionThrowingStub(Path filePath) {
            super(filePath);
        }

        @Override
        public void saveSchedulePlanner(ReadOnlySchedulePlanner schedulePlanner, Path filePath) throws IOException {
            throw new IOException("dummy exception");
        }
    }


}
