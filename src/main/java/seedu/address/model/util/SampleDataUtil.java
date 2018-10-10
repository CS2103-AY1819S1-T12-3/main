package seedu.address.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.ReadOnlySchedulePlanner;
import seedu.address.model.SchedulePlanner;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Date;
import seedu.address.model.task.Name;
import seedu.address.model.task.Priority;
import seedu.address.model.task.Task;
import seedu.address.model.task.Venue;

/**
 * Contains utility methods for populating {@code SchedulePlanner} with sample data.
 */
public class SampleDataUtil {
    public static Task[] getSampleTasks() {
        return new Task[] {
            new Task(new Name("Alex Yeoh"), new Date("87438807"), new Priority("1"),
                new Venue("Blk 30 Geylang Street 29, #06-40"),
                getTagSet("friends")),
            new Task(new Name("Bernice Yu"), new Date("99272758"), new Priority("1"),
                new Venue("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                getTagSet("colleagues", "friends")),
            new Task(new Name("Charlotte Oliveiro"), new Date("93210283"), new Priority("1"),
                new Venue("Blk 11 Ang Mo Kio Street 74, #11-04"),
                getTagSet("neighbours")),
            new Task(new Name("David Li"), new Date("91031282"), new Priority("1"),
                new Venue("Blk 436 Serangoon Gardens Street 26, #16-43"),
                getTagSet("family")),
            new Task(new Name("Irfan Ibrahim"), new Date("92492021"), new Priority("1"),
                new Venue("Blk 47 Tampines Street 20, #17-35"),
                getTagSet("classmates")),
            new Task(new Name("Roy Balakrishnan"), new Date("92624417"), new Priority("1"),
                new Venue("Blk 45 Aljunied Street 85, #11-31"),
                getTagSet("colleagues"))
        };
    }

    public static ReadOnlySchedulePlanner getSampleSchedulePlanner() {
        SchedulePlanner sampleAb = new SchedulePlanner();
        for (Task sampleTask : getSampleTasks()) {
            sampleAb.addTask(sampleTask);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
