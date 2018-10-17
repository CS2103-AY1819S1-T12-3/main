package ssp.scheduleplanner.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import ssp.scheduleplanner.commons.events.model.SchedulePlannerChangedEvent;
import ssp.scheduleplanner.commons.events.storage.DataSavingExceptionEvent;
import ssp.scheduleplanner.commons.exceptions.DataConversionException;
import ssp.scheduleplanner.model.ReadOnlySchedulePlanner;
import ssp.scheduleplanner.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends SchedulePlannerStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(UserPrefs userPrefs) throws IOException;

    @Override
    Path getSchedulePlannerFilePath();

    @Override
    Optional<ReadOnlySchedulePlanner> readSchedulePlanner() throws DataConversionException, IOException;

    @Override
    void saveSchedulePlanner(ReadOnlySchedulePlanner schedulePlanner) throws IOException;

    /**
     * Saves the current version of the Schedule Planner to the hard disk.
     *   Creates the data file if it is missing.
     * Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     */
    void handleSchedulePlannerChangedEvent(SchedulePlannerChangedEvent abce);
}
