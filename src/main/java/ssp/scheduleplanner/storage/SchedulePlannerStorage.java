package ssp.scheduleplanner.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import ssp.scheduleplanner.commons.exceptions.DataConversionException;
import ssp.scheduleplanner.model.ReadOnlySchedulePlanner;
import ssp.scheduleplanner.model.SchedulePlanner;

/**
 * Represents a storage for {@link SchedulePlanner}.
 */
public interface SchedulePlannerStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getSchedulePlannerFilePath();

    /**
     * Returns SchedulePlanner data as a {@link ReadOnlySchedulePlanner}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlySchedulePlanner> readSchedulePlanner() throws DataConversionException, IOException;

    /**
     * @see #getSchedulePlannerFilePath()
     */
    Optional<ReadOnlySchedulePlanner> readSchedulePlanner(Path filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlySchedulePlanner} to the storage.
     * @param schedulePlanner cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveSchedulePlanner(ReadOnlySchedulePlanner schedulePlanner) throws IOException;

    /**
     * @see #saveSchedulePlanner(ReadOnlySchedulePlanner)
     */
    void saveSchedulePlanner(ReadOnlySchedulePlanner schedulePlanner, Path filePath) throws IOException;

}
