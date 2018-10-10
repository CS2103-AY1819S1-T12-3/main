package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.tag.Tag;
import seedu.address.model.task.Date;
import seedu.address.model.task.Name;
import seedu.address.model.task.Priority;
import seedu.address.model.task.Task;
import seedu.address.model.task.Venue;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Task objects.
 */
public class TaskBuilder {

    public static final String DEFAULT_NAME = "Alice Pauline";
    public static final String DEFAULT_PHONE = "85355255";
    //public static final String DEFAULT_EMAIL = "alice@gmail.com";
    public static final String DEFAULT_EMAIL = "1";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";

    private Name name;
    private Date date;
    private Priority priority;
    private Venue venue;
    private Set<Tag> tags;

    public TaskBuilder() {
        name = new Name(
                DEFAULT_NAME);
        date = new Date(DEFAULT_PHONE);
        priority = new Priority(DEFAULT_EMAIL);
        venue = new Venue(DEFAULT_ADDRESS);
        tags = new HashSet<>();
    }

    /**
     * Initializes the TaskBuilder with the data of {@code taskToCopy}.
     */
    public TaskBuilder(Task taskToCopy) {
        name = taskToCopy.getName();
        date = taskToCopy.getDate();
        priority = taskToCopy.getPriority();
        venue = taskToCopy.getVenue();
        tags = new HashSet<>(taskToCopy.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code Task} that we are building.
     */
    public TaskBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Task} that we are building.
     */
    public TaskBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Venue} of the {@code Task} that we are building.
     */
    public TaskBuilder withAddress(String address) {
        this.venue = new Venue(address);
        return this;
    }

    /**
     * Sets the {@code Date} of the {@code Task} that we are building.
     */
    public TaskBuilder withPhone(String phone) {
        this.date = new Date(phone);
        return this;
    }

    /**
     * Sets the {@code Priority} of the {@code Task} that we are building.
     */
    public TaskBuilder withEmail(String email) {
        this.priority = new Priority(email);
        return this;
    }

    public Task build() {
        return new Task(name, date, priority, venue, tags);
    }

}
