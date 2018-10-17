package ssp.scheduleplanner.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import ssp.scheduleplanner.commons.core.ComponentManager;
import ssp.scheduleplanner.commons.core.LogsCenter;
import ssp.scheduleplanner.commons.events.model.SchedulePlannerChangedEvent;
import ssp.scheduleplanner.commons.events.storage.DataSavingExceptionEvent;
import ssp.scheduleplanner.commons.exceptions.DataConversionException;
import ssp.scheduleplanner.model.ReadOnlySchedulePlanner;
import ssp.scheduleplanner.model.UserPrefs;

/**
 * Manages storage of SchedulePlanner data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private SchedulePlannerStorage schedulePlannerStorage;
    private UserPrefsStorage userPrefsStorage;


    public StorageManager(SchedulePlannerStorage schedulePlannerStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.schedulePlannerStorage = schedulePlannerStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ SchedulePlanner methods ==============================

    @Override
    public Path getSchedulePlannerFilePath() {
        return schedulePlannerStorage.getSchedulePlannerFilePath();
    }

    @Override
    public Optional<ReadOnlySchedulePlanner> readSchedulePlanner() throws DataConversionException, IOException {
        return readSchedulePlanner(schedulePlannerStorage.getSchedulePlannerFilePath());
    }

    @Override
    public Optional<ReadOnlySchedulePlanner> readSchedulePlanner(Path filePath) throws DataConversionException,
            IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return schedulePlannerStorage.readSchedulePlanner(filePath);
    }

    @Override
    public void saveSchedulePlanner(ReadOnlySchedulePlanner schedulePlanner) throws IOException {
        saveSchedulePlanner(schedulePlanner, schedulePlannerStorage.getSchedulePlannerFilePath());
    }

    @Override
    public void saveSchedulePlanner(ReadOnlySchedulePlanner schedulePlanner, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        schedulePlannerStorage.saveSchedulePlanner(schedulePlanner, filePath);
    }


    @Override
    @Subscribe
    public void handleSchedulePlannerChangedEvent(SchedulePlannerChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveSchedulePlanner(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

}
