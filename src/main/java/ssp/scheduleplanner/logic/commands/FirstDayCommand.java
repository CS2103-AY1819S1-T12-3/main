package ssp.scheduleplanner.logic.commands;

import static ssp.scheduleplanner.commons.util.AppUtil.checkArgument;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import ssp.scheduleplanner.commons.exceptions.DataConversionException;
import ssp.scheduleplanner.logic.CommandHistory;
import ssp.scheduleplanner.logic.commands.exceptions.CommandException;
import ssp.scheduleplanner.model.Model;
import ssp.scheduleplanner.model.task.Date;
import ssp.scheduleplanner.storage.XmlFileStorage;
import ssp.scheduleplanner.storage.XmlSerializableRangeOfWeek;

/**
 * Mark the first day of the semester to be able to compute the range of date of all semester weeks and save the range.
 */
public class FirstDayCommand extends Command {

    public static final String COMMAND_WORD = "firstday";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Mark the first monday of the semester. "
            + "Date should be in ddmmyy format\nExample: " + COMMAND_WORD + " " + "130818";
    public static final String MESSAGE_SUCCESS = "First day of semester saved successfully";
    public static final String MESSAGE_ONLY_ONE_ARGUMENT = "FirstDay command only accept one set of date";
    public static final String MESSAGE_INVALID_DATE = "Invalid date or date format\n"
            + "Date should be in ddmmyy format\nExample: " + COMMAND_WORD + " " + "130818";;
    public static final String MESSAGE_NOT_MONDAY = "Date given is not a Monday";
    public static final String MESSAGE_FILE_DOES_NOT_EXIST = "Unable to save range of dates of semester as "
            + "default file is missing";
    public static final String MESSAGE_DATA_UNABLE_CONVERT = "Data unable to convert from saved file";

    public static final int WEEKS_IN_SEMESTER = 17;
    public static final String DEFAULT_MONDAY_DATE = "010118";

    private String inputDate = "";
    private String[][] rangeOfWeek = new String[WEEKS_IN_SEMESTER][3];
    private String weekDescription = "";
    private Path path = Paths.get("rangeofweek.xml");

    //To allow other class to use the methods without causing any changes to the storage
    public FirstDayCommand() {}

    public FirstDayCommand(String userInputDate) {
        checkArgument(isMonday(userInputDate), MESSAGE_NOT_MONDAY);
        checkArgument(Date.isValidDate(userInputDate), MESSAGE_INVALID_DATE);
        this.inputDate = userInputDate;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        computeRangeOfWeeks(inputDate);
        saveRangeOfWeeks(rangeOfWeek);

        if (isWithinDateRange(rangeOfWeek[0][0], rangeOfWeek[16][1])) {
            weekDescription = retrieveWeekDescription(rangeOfWeek);
        }

        if (weekDescription != "") {
            return new CommandResult(String.format(MESSAGE_SUCCESS + "\n" + "Current week: %s", weekDescription
            + "\n" + "The week description will be reflected when you relaunch the application"));
        }

        return new CommandResult(MESSAGE_SUCCESS);
    }

    /**
     * Check if system date is within the semester date
     * @param firstDayOfSem
     * @param lastDayOfSem
     * @return true or false
     */
    public boolean isWithinDateRange(String firstDayOfSem, String lastDayOfSem) {
        LocalDate firstDate = LocalDate.parse(firstDayOfSem, DateTimeFormatter.ofPattern("ddMMyy"));
        LocalDate lastDate = LocalDate.parse(lastDayOfSem, DateTimeFormatter.ofPattern("ddMMyy"));
        LocalDate systemDate = LocalDate.now();
        return (systemDate.isEqual(firstDate) || systemDate.isAfter(firstDate) && (systemDate.isBefore(lastDate)
                || systemDate.isEqual(lastDate)));
    }

    /**
     * Retrieve the description of a particular week. If week can't be found, return empty string
     * @param rangeOfWeek
     * @return description of week
     */
    public String retrieveWeekDescription(String[][] rangeOfWeek) {
        LocalDate systemDate = LocalDate.now();
        for (int i = 0; i < WEEKS_IN_SEMESTER; i++) {
            LocalDate firstDate = LocalDate.parse(rangeOfWeek[i][0], DateTimeFormatter.ofPattern("ddMMyy"));
            LocalDate lastDate = LocalDate.parse(rangeOfWeek[i][1], DateTimeFormatter.ofPattern("ddMMyy"));
            if (systemDate.isEqual(firstDate) || systemDate.isAfter(firstDate) && (systemDate.isBefore(lastDate)
                    || systemDate.isEqual(lastDate))) {
                return rangeOfWeek[i][2];
            }
        }
        return "";
    }

    /**
     * This method save the rangeOfWeeks into storage
     * @param rangeOfWeek
     * @throws CommandException
     */
    public void saveRangeOfWeeks (String[][] rangeOfWeek) throws CommandException {
        try {
            XmlFileStorage.saveWeekDataToFile(path, new XmlSerializableRangeOfWeek(rangeOfWeek));
        } catch (FileNotFoundException e) {
            throw new CommandException(MESSAGE_FILE_DOES_NOT_EXIST);
        }
    }

    /**
     * This method retrieve the rangeOfWeeks from storage
     * @throws DataConversionException
     * @throws FileNotFoundException
     */
    public String[][] retrieveRangeOfWeeks (String[][] storeRangeOfWeeks) throws CommandException {
        try {
            XmlSerializableRangeOfWeek range = XmlFileStorage.loadWeekDataFromSaveFile(path);
            storeRangeOfWeeks = range.convertRangeOfWeeksToString2dArray(range);
        } catch (DataConversionException e) {
            throw new CommandException(MESSAGE_DATA_UNABLE_CONVERT);
        } catch (FileNotFoundException e) {
            throw new CommandException(MESSAGE_FILE_DOES_NOT_EXIST);
        }
        return storeRangeOfWeeks;
    }

    /**
     * This method compute the range of weeks for one semester based on the first Monday.
     * @param firstDay
     * @return the 2d string array
     */
    public String[][] computeRangeOfWeeks(String firstDay) {
        for (int i = 0; i < WEEKS_IN_SEMESTER; i++) {
            String startOfWeekDate = LocalDate.parse(firstDay, DateTimeFormatter.ofPattern("ddMMyy")).plusDays(7 * i)
                    .format(DateTimeFormatter.ofPattern("ddMMyy"));
            String endOfWeekDate = LocalDate.parse(firstDay, DateTimeFormatter.ofPattern("ddMMyy")).plusDays(7 * i + 6)
                    .format(DateTimeFormatter.ofPattern("ddMMyy"));
            rangeOfWeek[i][0] = startOfWeekDate;
            rangeOfWeek[i][1] = endOfWeekDate;
        }

        addDescriptionForWeeks(rangeOfWeek);
        return rangeOfWeek;
    }

    /**
     * Helper method to insert description of each week in the 2d String array
     * @param rangeOfWeek
     */
    public void addDescriptionForWeeks(String[][] rangeOfWeek) {
        rangeOfWeek[0][2] = "Week 1";
        rangeOfWeek[1][2] = "Week 2";
        rangeOfWeek[2][2] = "Week 3";
        rangeOfWeek[3][2] = "Week 4";
        rangeOfWeek[4][2] = "Week 5";
        rangeOfWeek[5][2] = "Week 6";
        rangeOfWeek[6][2] = "Recess Week";
        rangeOfWeek[7][2] = "Week 7";
        rangeOfWeek[8][2] = "Week 8";
        rangeOfWeek[9][2] = "Week 9";
        rangeOfWeek[10][2] = "Week 10";
        rangeOfWeek[11][2] = "Week 11";
        rangeOfWeek[12][2] = "Week 12";
        rangeOfWeek[13][2] = "Week 13";
        rangeOfWeek[14][2] = "Study Week";
        rangeOfWeek[15][2] = "Examination Week";
        rangeOfWeek[16][2] = "Examination Week";
    }

    public boolean isMonday(String inputDate) {
        return (LocalDate.parse(inputDate, DateTimeFormatter.ofPattern("ddMMyy")).getDayOfWeek().name() == "MONDAY");
    }

    /**
     * This method compute the correct application title and return the string value
     * @return application title
     * @throws CommandException
     */
    public String computeAppTitle() throws CommandException {
        String appTitle = "";

        String[][] retrieveData = new String[WEEKS_IN_SEMESTER][3];
        retrieveData = retrieveRangeOfWeeks(retrieveData);

        if (isWithinDateRange(retrieveData[0][0], retrieveData[16][1])) {
            appTitle = "Schedule Planner" + "  - " + retrieveWeekDescription(retrieveData);
        } else {
            appTitle = "Schedule Planner";
        }

        return appTitle;
    }

    /**
     * This method create default storage file if not exist.
     * solution below adapted from
     * https://stackoverflow.com/questions/1816673/how-do-i-check-if-a-file-exists-in-java
     * @throws CommandException
     */
    public void createDefaultFileIfNotExist () throws CommandException {
        File checkFileExist = new File("rangeofweek.xml");
        if (!checkFileExist.exists()) {
            try {
                checkFileExist.createNewFile();
                saveRangeOfWeeks(computeRangeOfWeeks(DEFAULT_MONDAY_DATE));
            } catch (java.io.IOException e) {
                throw new CommandException("Failed to create rangeofweek.xml");
            }
        }
    }

    public String returnUserDate () {
        return inputDate;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FirstDayCommand // instanceof handles nulls
                && inputDate.equals(((FirstDayCommand) other).returnUserDate()));
    }
}
