package ssp.scheduleplanner.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ssp.scheduleplanner.commons.exceptions.IllegalValueException;
import ssp.scheduleplanner.model.ReadOnlySchedulePlanner;
import ssp.scheduleplanner.model.SchedulePlanner;
import ssp.scheduleplanner.model.task.Task;

/**
 * An Immutable SchedulePlanner that is serializable to XML format
 */
@XmlRootElement(name = "scheduleplanner")
public class XmlSerializableSchedulePlanner {

    public static final String MESSAGE_DUPLICATE_TASK = "Tasks list contains duplicate task(s).";

    @XmlElement
    private List<XmlAdaptedTask> tasks;

    /**
     * Creates an empty XmlSerializableSchedulePlanner.
     * This empty constructor is required for marshalling.
     */
    public XmlSerializableSchedulePlanner() {
        tasks = new ArrayList<>();
    }

    /**
     * Conversion
     */
    public XmlSerializableSchedulePlanner(ReadOnlySchedulePlanner src) {
        this();
        tasks.addAll(src.getTaskList().stream().map(XmlAdaptedTask::new).collect(Collectors.toList()));
    }

    /**
     * Converts this addressbook into the model's {@code SchedulePlanner} object.
     *
     * @throws IllegalValueException if there were any data constraints violated or duplicates in the
     * {@code XmlAdaptedTask}.
     */
    public SchedulePlanner toModelType() throws IllegalValueException {
        SchedulePlanner addressBook = new SchedulePlanner();
        for (XmlAdaptedTask p : tasks) {
            Task task = p.toModelType();
            if (addressBook.hasTask(task)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_TASK);
            }
            addressBook.addTask(task);
        }
        return addressBook;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlSerializableSchedulePlanner)) {
            return false;
        }
        return tasks.equals(((XmlSerializableSchedulePlanner) other).tasks);
    }
}
