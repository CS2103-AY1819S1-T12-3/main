package ssp.scheduleplanner.testutil;

import ssp.scheduleplanner.model.SchedulePlanner;
import ssp.scheduleplanner.model.task.Task;

/**
 * A utility class to help with building Addressbook objects.
 * Example usage: <br>
 *     {@code SchedulePlanner ab = new SchedulePlannerBuilder().withTask("John", "Doe").build();}
 */
public class SchedulePlannerBuilder {

    private SchedulePlanner schedulePlanner;

    public SchedulePlannerBuilder() {
        schedulePlanner = new SchedulePlanner();
    }

    public SchedulePlannerBuilder(SchedulePlanner schedulePlanner) {
        this.schedulePlanner = schedulePlanner;
    }

    /**
     * Adds a new {@code Task} to the {@code SchedulePlanner} that we are building.
     */
    public SchedulePlannerBuilder withTask(Task task) {
        schedulePlanner.addTask(task);
        return this;
    }

    public SchedulePlanner build() {
        return schedulePlanner;
    }
}
