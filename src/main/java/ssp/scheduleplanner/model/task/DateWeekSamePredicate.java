package ssp.scheduleplanner.model.task;

import java.util.List;
import java.util.function.Predicate;

import ssp.scheduleplanner.commons.util.StringUtil;

/**
 * Tests that a {@code Task}'s {@code Date} matches the current list of system date.
 */
public class DateWeekSamePredicate implements Predicate<Task> {
    private final List<String> date;

    public DateWeekSamePredicate(List<String> date) {
        this.date = date;
    }

    @Override
    public boolean test(Task task) {
        return date.stream()
                .anyMatch(date -> StringUtil.containsWordIgnoreCase(task.getDate().value, date));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DateWeekSamePredicate // instanceof handles nulls
                && date.equals(((DateWeekSamePredicate) other).date)); // state check
    }

}
