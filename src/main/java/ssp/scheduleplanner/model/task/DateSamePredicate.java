package ssp.scheduleplanner.model.task;

import java.util.function.Predicate;

/**
 * Tests that a {@code Task}'s {@code Date} matches the current system date.
 */
public class DateSamePredicate implements Predicate<Task> {
    private final String date;

    public DateSamePredicate(String date) {
        this.date = date;
    }

    @Override
    public boolean test(Task task) {
        return date.equals(task.getDate().value);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DateSamePredicate // instanceof handles nulls
                && date.equals(((DateSamePredicate) other).date)); // state check
    }

}
