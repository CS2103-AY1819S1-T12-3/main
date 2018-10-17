package ssp.scheduleplanner.logic.commands;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ssp.scheduleplanner.logic.CommandHistory;
import ssp.scheduleplanner.model.Model;
import ssp.scheduleplanner.model.task.DateSamePredicate;

/**
 * List all tasks with the current date.
 */
public class ListDayCommand extends Command {
    public static final String COMMAND_WORD = "listday";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": List all task(s) with the current date.\n";
    public static final String MESSAGE_SUCCESS = "Today's task(s) are listed as followed";

    //Solution below adapted from:
    //https://stackoverflow.com/questions/5175728/how-to-get-the-current-date-time-in-java
    private static final String systemDate =
            new SimpleDateFormat("ddMMyy").format(Calendar.getInstance().getTime());

    @Override
    public CommandResult execute(Model model, CommandHistory history) {

        model.updateFilteredTaskList(new DateSamePredicate(systemDate));
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
