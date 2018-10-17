package ssp.scheduleplanner.storage;

import static java.util.Objects.requireNonNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import ssp.scheduleplanner.commons.core.LogsCenter;
import ssp.scheduleplanner.commons.exceptions.DataConversionException;
import ssp.scheduleplanner.commons.exceptions.IllegalValueException;
import ssp.scheduleplanner.commons.util.FileUtil;
import ssp.scheduleplanner.model.ReadOnlySchedulePlanner;

/**
 * A class to access SchedulePlanner data stored as an xml file on the hard disk.
 */
public class XmlSchedulePlannerStorage implements SchedulePlannerStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlSchedulePlannerStorage.class);

    private Path filePath;

    public XmlSchedulePlannerStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getSchedulePlannerFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlySchedulePlanner> readSchedulePlanner() throws DataConversionException, IOException {
        return readSchedulePlanner(filePath);
    }

    /**
     * Similar to {@link #readSchedulePlanner()}
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlySchedulePlanner> readSchedulePlanner(Path filePath) throws DataConversionException,
                                                                                 FileNotFoundException {
        requireNonNull(filePath);

        if (!Files.exists(filePath)) {
            logger.info("SchedulePlanner file " + filePath + " not found");
            return Optional.empty();
        }

        XmlSerializableSchedulePlanner xmlAddressBook = XmlFileStorage.loadDataFromSaveFile(filePath);
        try {
            return Optional.of(xmlAddressBook.toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveSchedulePlanner(ReadOnlySchedulePlanner schedulePlanner) throws IOException {
        saveSchedulePlanner(schedulePlanner, filePath);
    }

    /**
     * Similar to {@link #saveSchedulePlanner(ReadOnlySchedulePlanner)}
     * @param filePath location of the data. Cannot be null
     */
    public void saveSchedulePlanner(ReadOnlySchedulePlanner schedulePlanner, Path filePath) throws IOException {
        requireNonNull(schedulePlanner);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        XmlFileStorage.saveDataToFile(filePath, new XmlSerializableSchedulePlanner(schedulePlanner));
    }

}
