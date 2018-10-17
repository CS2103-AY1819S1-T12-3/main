package ssp.scheduleplanner.ui;

import static guitests.guihandles.WebViewUtil.waitUntilBrowserLoaded;
import static org.junit.Assert.assertEquals;
import static ssp.scheduleplanner.testutil.EventsUtil.postNow;
import static ssp.scheduleplanner.testutil.TypicalTasks.ALICE;
import static ssp.scheduleplanner.ui.BrowserPanel.DEFAULT_PAGE;
import static ssp.scheduleplanner.ui.UiPart.FXML_FILE_FOLDER;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.BrowserPanelHandle;
import ssp.scheduleplanner.MainApp;
import ssp.scheduleplanner.commons.events.ui.TaskPanelSelectionChangedEvent;

public class BrowserPanelTest extends GuiUnitTest {
    private TaskPanelSelectionChangedEvent selectionChangedEventStub;

    private BrowserPanel browserPanel;
    private BrowserPanelHandle browserPanelHandle;

    @Before
    public void setUp() {
        selectionChangedEventStub = new TaskPanelSelectionChangedEvent(ALICE);

        guiRobot.interact(() -> browserPanel = new BrowserPanel());
        uiPartRule.setUiPart(browserPanel);

        browserPanelHandle = new BrowserPanelHandle(browserPanel.getRoot());
    }

    @Test
    public void display() throws Exception {
        // default web page
        URL expectedDefaultPageUrl = MainApp.class.getResource(FXML_FILE_FOLDER + DEFAULT_PAGE);
        assertEquals(expectedDefaultPageUrl, browserPanelHandle.getLoadedUrl());

        // associated web page of a task
        postNow(selectionChangedEventStub);
        URL expectedTaskUrl = new URL(BrowserPanel.SEARCH_PAGE_URL + ALICE.getName().fullName.replaceAll(" ", "%20"));

        waitUntilBrowserLoaded(browserPanelHandle);
        assertEquals(expectedTaskUrl, browserPanelHandle.getLoadedUrl());
    }
}
