package seedu.address.commons.util;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.SchedulePlanner;
import seedu.address.storage.XmlAdaptedTag;
import seedu.address.storage.XmlAdaptedTask;
import seedu.address.storage.XmlSerializableSchedulePlanner;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.TaskBuilder;
import seedu.address.testutil.TestUtil;

public class XmlUtilTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "XmlUtilTest");
    private static final Path EMPTY_FILE = TEST_DATA_FOLDER.resolve("empty.xml");
    private static final Path MISSING_FILE = TEST_DATA_FOLDER.resolve("missing.xml");
    private static final Path VALID_FILE = TEST_DATA_FOLDER.resolve("validSchedulePlanner.xml");
    private static final Path MISSING_TASK_FIELD_FILE = TEST_DATA_FOLDER.resolve("missingTaskField.xml");
    private static final Path INVALID_TASK_FIELD_FILE = TEST_DATA_FOLDER.resolve("invalidTaskField.xml");
    private static final Path VALID_TASK_FILE = TEST_DATA_FOLDER.resolve("validTask.xml");
    private static final Path TEMP_FILE = TestUtil.getFilePathInSandboxFolder("tempSchedulePlanner.xml");

    private static final String INVALID_PHONE = "9482asf424";

    private static final String VALID_NAME = "Hans Muster";
    private static final String VALID_PHONE = "9482424";
    private static final String VALID_EMAIL = "1";
    private static final String VALID_ADDRESS = "4th street";
    private static final List<XmlAdaptedTag> VALID_TAGS = Collections.singletonList(new XmlAdaptedTag("friends"));

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getDataFromFile_nullFile_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.getDataFromFile(null, SchedulePlanner.class);
    }

    @Test
    public void getDataFromFile_nullClass_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.getDataFromFile(VALID_FILE, null);
    }

    @Test
    public void getDataFromFile_missingFile_fileNotFoundException() throws Exception {
        thrown.expect(FileNotFoundException.class);
        XmlUtil.getDataFromFile(MISSING_FILE, SchedulePlanner.class);
    }

    @Test
    public void getDataFromFile_emptyFile_dataFormatMismatchException() throws Exception {
        thrown.expect(JAXBException.class);
        XmlUtil.getDataFromFile(EMPTY_FILE, SchedulePlanner.class);
    }

    @Test
    public void getDataFromFile_validFile_validResult() throws Exception {
        SchedulePlanner dataFromFile =
                XmlUtil.getDataFromFile(VALID_FILE, XmlSerializableSchedulePlanner.class).toModelType();
        assertEquals(9, dataFromFile.getTaskList().size());
    }

    @Test
    public void xmlAdaptedPersonFromFile_fileWithMissingPersonField_validResult() throws Exception {
        XmlAdaptedTask actualPerson = XmlUtil.getDataFromFile(
                MISSING_TASK_FIELD_FILE, XmlAdaptedTaskWithRootElement.class);
        XmlAdaptedTask expectedPerson = new XmlAdaptedTask(
                null, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        assertEquals(expectedPerson, actualPerson);
    }

    @Test
    public void xmlAdaptedPersonFromFile_fileWithInvalidPersonField_validResult() throws Exception {
        XmlAdaptedTask actualPerson = XmlUtil.getDataFromFile(
                INVALID_TASK_FIELD_FILE, XmlAdaptedTaskWithRootElement.class);
        XmlAdaptedTask expectedPerson = new XmlAdaptedTask(
                VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        assertEquals(expectedPerson, actualPerson);
    }

    @Test
    public void xmlAdaptedPersonFromFile_fileWithValidPerson_validResult() throws Exception {
        XmlAdaptedTask actualPerson = XmlUtil.getDataFromFile(
                VALID_TASK_FILE, XmlAdaptedTaskWithRootElement.class);
        XmlAdaptedTask expectedPerson = new XmlAdaptedTask(
                VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        assertEquals(expectedPerson, actualPerson);
    }

    @Test
    public void saveDataToFile_nullFile_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.saveDataToFile(null, new SchedulePlanner());
    }

    @Test
    public void saveDataToFile_nullClass_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.saveDataToFile(VALID_FILE, null);
    }

    @Test
    public void saveDataToFile_missingFile_fileNotFoundException() throws Exception {
        thrown.expect(FileNotFoundException.class);
        XmlUtil.saveDataToFile(MISSING_FILE, new SchedulePlanner());
    }

    @Test
    public void saveDataToFile_validFile_dataSaved() throws Exception {
        FileUtil.createFile(TEMP_FILE);
        XmlSerializableSchedulePlanner dataToWrite = new XmlSerializableSchedulePlanner(new SchedulePlanner());
        XmlUtil.saveDataToFile(TEMP_FILE, dataToWrite);
        XmlSerializableSchedulePlanner dataFromFile = XmlUtil.getDataFromFile(TEMP_FILE,
                XmlSerializableSchedulePlanner.class);
        assertEquals(dataToWrite, dataFromFile);

        AddressBookBuilder builder = new AddressBookBuilder(new SchedulePlanner());
        dataToWrite = new XmlSerializableSchedulePlanner(
                builder.withPerson(new TaskBuilder().build()).build());

        XmlUtil.saveDataToFile(TEMP_FILE, dataToWrite);
        dataFromFile = XmlUtil.getDataFromFile(TEMP_FILE, XmlSerializableSchedulePlanner.class);
        assertEquals(dataToWrite, dataFromFile);
    }

    /**
     * Test class annotated with {@code XmlRootElement} to allow unmarshalling of .xml data to {@code XmlAdaptedTask}
     * objects.
     */
    @XmlRootElement(name = "task")
    private static class XmlAdaptedTaskWithRootElement extends XmlAdaptedTask {}
}
