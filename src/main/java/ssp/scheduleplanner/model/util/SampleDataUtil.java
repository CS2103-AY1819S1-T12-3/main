package ssp.scheduleplanner.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import ssp.scheduleplanner.model.ReadOnlySchedulePlanner;
import ssp.scheduleplanner.model.SchedulePlanner;
import ssp.scheduleplanner.model.tag.Tag;
import ssp.scheduleplanner.model.task.Date;
import ssp.scheduleplanner.model.task.Name;
import ssp.scheduleplanner.model.task.Priority;
import ssp.scheduleplanner.model.task.Task;
import ssp.scheduleplanner.model.task.Venue;

/**
 * Contains utility methods for populating {@code SchedulePlanner} with sample data.
 */
public class SampleDataUtil {
    public static Task[] getSampleTasks() {
        return new Task[] {
            new Task(new Name("Alex Yeoh"), new Date("121107"), new Priority("1"),
                new Venue("Blk 30 Geylang Street 29, #06-40"),
                getTagSet("friends")),
            new Task(new Name("Bernice Yu"), new Date("141258"), new Priority("1"),
                new Venue("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                getTagSet("colleagues", "friends")),
            new Task(new Name("Charlotte Oliveiro"), new Date("150983"), new Priority("1"),
                new Venue("Blk 11 Ang Mo Kio Street 74, #11-04"),
                getTagSet("neighbours")),
            new Task(new Name("David Li"), new Date("120782"), new Priority("1"),
                new Venue("Blk 436 Serangoon Gardens Street 26, #16-43"),
                getTagSet("family")),
            new Task(new Name("Irfan Ibrahim"), new Date("230721"), new Priority("1"),
                new Venue("Blk 47 Tampines Street 20, #17-35"),
                getTagSet("classmates")),
            new Task(new Name("Roy Balakrishnan"), new Date("080717"), new Priority("1"),
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
