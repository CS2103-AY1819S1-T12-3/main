package ssp.scheduleplanner.logic.commands;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ssp.scheduleplanner.logic.CommandHistory;
import ssp.scheduleplanner.model.Model;
import ssp.scheduleplanner.model.task.DateWeekSamePredicate;

/**
 * List all tasks till Sunday.
 */
public class ListWeekCommand extends Command {
    public static final String COMMAND_WORD = "listweek";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": List all task(s) till Sunday.\n";
    public static final String MESSAGE_SUCCESS = "This week task(s) are listed as followed";

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        List<String> dateList = new ArrayList<String>();
        String dateName = LocalDate.now().getDayOfWeek().name();
        appendDateList(dateList, numDaysTillSunday(dateName));
        model.updateFilteredTaskList(new DateWeekSamePredicate(dateList));

        return new CommandResult(MESSAGE_SUCCESS);
    }

    /**
     * This method append the date of days till nearest sunday into 'dateList'
     */
    private void appendDateList (List<String> dateList, int numDays) {
        dateList.clear();

        //solution below adapted from:
        //https://stackoverflow.com/questions/428918/how-can-i-increment-a-date-by-one-day-in-java
        //if current day is sunday, just append current date into dateList
        //also doubled up to add the base current date into dateList
        Calendar c = Calendar.getInstance();
        dateList.add(new SimpleDateFormat("ddMMyy").format(c.getTime()));

        //for monday to saturday, append the corresponding date(s) till sunday into dateList
        //will not work for sunday as intended as numDays for sunday till sunday is 0
        for (int i = 0; i < numDays; i++) {
            c.add(Calendar.DATE, 1);
            dateList.add(new SimpleDateFormat("ddMMyy").format(c.getTime()));
        }
    }

    /**
     * This method check and return the number of positive days left from current date till the nearest sunday.
     * @param currentDate the name of today date
     * @return numDays
     */
    private int numDaysTillSunday (String currentDate) {
        int numDays;

        switch(currentDate) {
        case "SUNDAY":
            numDays = 0;
            break;
        case "MONDAY":
            numDays = 6;
            break;
        case "TUESDAY":
            numDays = 5;
            break;
        case "WEDNESDAY":
            numDays = 4;
            break;
        case "THURSDAY":
            numDays = 3;
            break;
        case "FRIDAY":
            numDays = 2;
            break;
        case"SATURDAY":
            numDays = 1;
            break;
        default:
            numDays = 0;
            break;
        }
        return numDays;
    }
}
