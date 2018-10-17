package ssp.scheduleplanner.logic.commands;

import static java.util.Objects.requireNonNull;
import static ssp.scheduleplanner.model.Model.PREDICATE_SHOW_ALL_TASKS;

import ssp.scheduleplanner.logic.CommandHistory;
import ssp.scheduleplanner.model.Model;

/**
 * Lists all tasks in the Schedule Planner to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all tasks";


    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        model.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
