package ssp.scheduleplanner.ui;

import static java.time.Duration.ofMillis;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static ssp.scheduleplanner.testutil.EventsUtil.postNow;
import static ssp.scheduleplanner.testutil.TypicalIndexes.INDEX_SECOND_TASK;
import static ssp.scheduleplanner.testutil.TypicalTasks.getTypicalTasks;
import static ssp.scheduleplanner.ui.testutil.GuiTestAssert.assertCardDisplaysTask;
import static ssp.scheduleplanner.ui.testutil.GuiTestAssert.assertCardEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import guitests.guihandles.TaskCardHandle;
import guitests.guihandles.TaskListPanelHandle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ssp.scheduleplanner.commons.events.ui.JumpToListRequestEvent;
import ssp.scheduleplanner.commons.util.FileUtil;
import ssp.scheduleplanner.commons.util.XmlUtil;
import ssp.scheduleplanner.model.task.Task;
import ssp.scheduleplanner.storage.XmlSerializableSchedulePlanner;

public class TaskListPanelTest extends GuiUnitTest {
    private static final ObservableList<Task> TYPICAL_TASKS = FXCollections.observableList(getTypicalTasks());

    private static final JumpToListRequestEvent JUMP_TO_SECOND_EVENT = new JumpToListRequestEvent(INDEX_SECOND_TASK);

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "sandbox");

    private static final long CARD_CREATION_AND_DELETION_TIMEOUT = 2500;

    private TaskListPanelHandle taskListPanelHandle;

    @Test
    public void display() {
        initUi(TYPICAL_TASKS);

        for (int i = 0; i < TYPICAL_TASKS.size(); i++) {
            taskListPanelHandle.navigateToCard(TYPICAL_TASKS.get(i));
            Task expectedTask = TYPICAL_TASKS.get(i);
            TaskCardHandle actualCard = taskListPanelHandle.getTaskCardHandle(i);

            assertCardDisplaysTask(expectedTask, actualCard);
            assertEquals(Integer.toString(i + 1) + ". ", actualCard.getId());
        }
    }

    @Test
    public void handleJumpToListRequestEvent() {
        initUi(TYPICAL_TASKS);
        postNow(JUMP_TO_SECOND_EVENT);
        guiRobot.pauseForHuman();

        TaskCardHandle expectedTask = taskListPanelHandle.getTaskCardHandle(INDEX_SECOND_TASK.getZeroBased());
        TaskCardHandle selectedTask = taskListPanelHandle.getHandleToSelectedCard();

        assertCardEquals(expectedTask, selectedTask);
    }

    /**
     * Verifies that creating and deleting large number of tasks in {@code TaskListPanel} requires lesser than
     * {@code CARD_CREATION_AND_DELETION_TIMEOUT} milliseconds to execute.
     */
    @Test
    public void performanceTest() throws Exception {
        ObservableList<Task> backingList = createBackingList(10000);

        assertTimeoutPreemptively(ofMillis(CARD_CREATION_AND_DELETION_TIMEOUT), () -> {
            initUi(backingList);
            guiRobot.interact(backingList::clear);
        }, "Creation and deletion of task cards exceeded time limit");
    }

    /**
     * Returns a list of tasks containing {@code taskCount} tasks that is used to populate the
     * {@code TaskListPanel}.
     */
    private ObservableList<Task> createBackingList(int taskCount) throws Exception {
        Path xmlFile = createXmlFileWithTasks(taskCount);
        XmlSerializableSchedulePlanner xmlSchedulePlanner =
                XmlUtil.getDataFromFile(xmlFile, XmlSerializableSchedulePlanner.class);
        return FXCollections.observableArrayList(xmlSchedulePlanner.toModelType().getTaskList());
    }

    /**
     * Returns a .xml file containing {@code taskCount} tasks. This file will be deleted when the JVM terminates.
     */
    private Path createXmlFileWithTasks(int taskCount) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
        builder.append("<scheduleplanner>\n");
        for (int i = 0; i < taskCount; i++) {
            builder.append("<tasks>\n");
            builder.append("<name>").append(i).append("a</name>\n");
            builder.append("<date>030201</date>\n");
            //builder.append("<priority>a@aa</priority>\n");
            builder.append("<priority>1</priority>\n");
            builder.append("<venue>a</venue>\n");
            builder.append("</tasks>\n");
        }
        builder.append("</scheduleplanner>\n");

        Path manyTasksFile = Paths.get(TEST_DATA_FOLDER + "manyTasks.xml");
        FileUtil.createFile(manyTasksFile);
        FileUtil.writeToFile(manyTasksFile, builder.toString());
        manyTasksFile.toFile().deleteOnExit();
        return manyTasksFile;
    }

    /**
     * Initializes {@code taskListPanelHandle} with a {@code TaskListPanel} backed by {@code backingList}.
     * Also shows the {@code Stage} that displays only {@code TaskListPanel}.
     */
    private void initUi(ObservableList<Task> backingList) {
        TaskListPanel taskListPanel = new TaskListPanel(backingList);
        uiPartRule.setUiPart(taskListPanel);

        taskListPanelHandle = new TaskListPanelHandle(getChildNode(taskListPanel.getRoot(),
                TaskListPanelHandle.TASK_LIST_VIEW_ID));
    }
}
