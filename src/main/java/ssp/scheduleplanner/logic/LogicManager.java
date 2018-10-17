package ssp.scheduleplanner.logic;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import ssp.scheduleplanner.commons.core.ComponentManager;
import ssp.scheduleplanner.commons.core.LogsCenter;
import ssp.scheduleplanner.logic.commands.Command;
import ssp.scheduleplanner.logic.commands.CommandResult;
import ssp.scheduleplanner.logic.commands.exceptions.CommandException;
import ssp.scheduleplanner.logic.parser.SchedulePlannerParser;
import ssp.scheduleplanner.logic.parser.exceptions.ParseException;
import ssp.scheduleplanner.model.Model;
import ssp.scheduleplanner.model.task.Task;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final CommandHistory history;
    private final SchedulePlannerParser schedulePlannerParser;

    public LogicManager(Model model) {
        this.model = model;
        history = new CommandHistory();
        schedulePlannerParser = new SchedulePlannerParser();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        try {
            Command command = schedulePlannerParser.parseCommand(commandText);
            return command.execute(model, history);
        } finally {
            history.add(commandText);
        }
    }

    @Override
    public ObservableList<Task> getFilteredTaskList() {
        return model.getFilteredTaskList();
    }

    @Override
    public ListElementPointer getHistorySnapshot() {
        return new ListElementPointer(history.getHistory());
    }
}
