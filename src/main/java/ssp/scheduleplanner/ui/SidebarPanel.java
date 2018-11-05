package ssp.scheduleplanner.ui;

import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import ssp.scheduleplanner.commons.core.LogsCenter;
import ssp.scheduleplanner.commons.events.ui.NewResultAvailableEvent;
import ssp.scheduleplanner.logic.ListElementPointer;
import ssp.scheduleplanner.logic.Logic;
import ssp.scheduleplanner.logic.commands.CommandResult;
import ssp.scheduleplanner.logic.commands.exceptions.CommandException;
import ssp.scheduleplanner.logic.parser.exceptions.ParseException;

/**
 * The Sidebar Panel of the App.
 */
public class SidebarPanel extends UiPart<Region> {

    private static final String FXML = "SidebarPanel.fxml";
    private final Logic logic;
    private ListElementPointer historySnapshot;

    private final Logger logger = LogsCenter.getLogger(getClass());

    @FXML
    private ToggleButton today;

    @FXML
    private ToggleButton week;

    @FXML
    private Accordion modules;

    @FXML
    private Text categorieslabel;

    private ToggleGroup group;

    public SidebarPanel(Logic logic) {
        super(FXML);
        this.logic = logic;
        this.historySnapshot = logic.getHistorySnapshot();
        this.group = new ToggleGroup();
        today.setToggleGroup(group);
        week.setToggleGroup(group);
        categorieslabel.setFill(Color.WHITE);
        today.setAlignment(Pos.BASELINE_LEFT);
        week.setAlignment(Pos.BASELINE_LEFT);
    }

    /**
     * Handles the sidebar button pressed event.
     */
    @FXML
    private void handleShowView(ActionEvent event) {
        String view = (String) ((Node) event.getSource()).getUserData();
        switch (view) {
        case "today":
            try {
                CommandResult commandResult = logic.execute("listday");
                initHistory();
                historySnapshot.next();
                logger.info("Result: " + commandResult.feedbackToUser);
                raise(new NewResultAvailableEvent(commandResult.feedbackToUser));
            } catch (CommandException | ParseException e) {
                logger.info("Invalid command: " + "listday");
            }
            break;
        case "week":
            try {
                CommandResult commandResult = logic.execute("listweek");
                initHistory();
                historySnapshot.next();
                logger.info("Result: " + commandResult.feedbackToUser);
                raise(new NewResultAvailableEvent(commandResult.feedbackToUser));
            } catch (CommandException | ParseException e) {
                logger.info("Invalid command: " + "listweek");
            }
            break;
        default:
            break;
        }
    }

    /**
     * Initializes the history snapshot.
     */
    private void initHistory() {
        historySnapshot = logic.getHistorySnapshot();
        // add an empty string to represent the most-recent end of historySnapshot, to be shown to
        // the user if she tries to navigate past the most-recent end of the historySnapshot.
        historySnapshot.add("");
    }

}
