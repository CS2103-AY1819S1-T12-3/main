
package ssp.scheduleplanner.testutil;

import static ssp.scheduleplanner.logic.parser.CliSyntax.PREFIX_DATE;
import static ssp.scheduleplanner.logic.parser.CliSyntax.PREFIX_NAME;
import static ssp.scheduleplanner.logic.parser.CliSyntax.PREFIX_PRIORITY;
import static ssp.scheduleplanner.logic.parser.CliSyntax.PREFIX_TAG;
import static ssp.scheduleplanner.logic.parser.CliSyntax.PREFIX_VENUE;

import java.util.Set;

import ssp.scheduleplanner.logic.commands.AddCommand;
import ssp.scheduleplanner.logic.commands.EditCommand;
import ssp.scheduleplanner.model.tag.Tag;
import ssp.scheduleplanner.model.task.Task;

/**
 * A utility class for Task.
 */
public class TaskUtil {

    /**
     * Returns an add command string for adding the {@code task}.
     */
    public static String getAddCommand(Task task) {
        return AddCommand.COMMAND_WORD + " " + getTaskDetails(task);
    }

    /**
     * Returns the part of command string for the given {@code task}'s details.
     */
    public static String getTaskDetails(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + task.getName().fullName + " ");
        sb.append(PREFIX_DATE + task.getDate().value + " ");
        sb.append(PREFIX_PRIORITY + task.getPriority().value + " ");
        sb.append(PREFIX_VENUE + task.getVenue().value + " ");
        task.getTags().stream().forEach(s -> sb.append(PREFIX_TAG + s.tagName + " "));
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditTaskDescriptor}'s details.
     */
    public static String getEditTaskDescriptorDetails(EditCommand.EditTaskDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(PREFIX_NAME).append(name.fullName).append(" "));
        descriptor.getDate().ifPresent(date -> sb.append(PREFIX_DATE).append(date.value).append(" "));
        descriptor.getPriority().ifPresent(email -> sb.append(PREFIX_PRIORITY).append(email.value).append(" "));
        descriptor.getVenue().ifPresent(address -> sb.append(PREFIX_VENUE).append(address.value).append(" "));
        if (descriptor.getTags().isPresent()) {
            Set<Tag> tags = descriptor.getTags().get();
            if (tags.isEmpty()) {
                sb.append(PREFIX_TAG);
            } else {
                tags.forEach(s -> sb.append(PREFIX_TAG).append(s.tagName).append(" "));
            }
        }
        return sb.toString();
    }
}
