package ssp.scheduleplanner.logic.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ssp.scheduleplanner.commons.core.Messages;
import ssp.scheduleplanner.logic.commands.AddCommand;
import ssp.scheduleplanner.logic.commands.ClearCommand;
import ssp.scheduleplanner.logic.commands.Command;
import ssp.scheduleplanner.logic.commands.DeleteCommand;
import ssp.scheduleplanner.logic.commands.EditCommand;
import ssp.scheduleplanner.logic.commands.ExitCommand;
import ssp.scheduleplanner.logic.commands.FindCommand;
import ssp.scheduleplanner.logic.commands.HelpCommand;
import ssp.scheduleplanner.logic.commands.HistoryCommand;
import ssp.scheduleplanner.logic.commands.ListCommand;
import ssp.scheduleplanner.logic.commands.ListDayCommand;
import ssp.scheduleplanner.logic.commands.ListWeekCommand;
import ssp.scheduleplanner.logic.commands.RedoCommand;
import ssp.scheduleplanner.logic.commands.SelectCommand;
import ssp.scheduleplanner.logic.commands.UndoCommand;
import ssp.scheduleplanner.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class SchedulePlannerParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
            return new AddCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case SelectCommand.COMMAND_WORD:
            return new SelectCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
            return new FindCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case ListDayCommand.COMMAND_WORD:
            return new ListDayCommand();

        case ListWeekCommand.COMMAND_WORD:
            return new ListWeekCommand();

        case HistoryCommand.COMMAND_WORD:
            return new HistoryCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case UndoCommand.COMMAND_WORD:
            return new UndoCommand();

        case RedoCommand.COMMAND_WORD:
            return new RedoCommand();

        default:
            throw new ParseException(Messages.MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
