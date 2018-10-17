package ssp.scheduleplanner;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import ssp.scheduleplanner.commons.core.Config;
import ssp.scheduleplanner.commons.core.EventsCenter;
import ssp.scheduleplanner.commons.core.LogsCenter;
import ssp.scheduleplanner.commons.core.Version;
import ssp.scheduleplanner.commons.events.ui.ExitAppRequestEvent;
import ssp.scheduleplanner.commons.exceptions.DataConversionException;
import ssp.scheduleplanner.commons.util.ConfigUtil;
import ssp.scheduleplanner.commons.util.StringUtil;
import ssp.scheduleplanner.logic.Logic;
import ssp.scheduleplanner.logic.LogicManager;
import ssp.scheduleplanner.model.Model;
import ssp.scheduleplanner.model.ModelManager;
import ssp.scheduleplanner.model.ReadOnlySchedulePlanner;
import ssp.scheduleplanner.model.SchedulePlanner;
import ssp.scheduleplanner.model.UserPrefs;
import ssp.scheduleplanner.model.util.SampleDataUtil;
import ssp.scheduleplanner.storage.JsonUserPrefsStorage;
import ssp.scheduleplanner.storage.SchedulePlannerStorage;
import ssp.scheduleplanner.storage.Storage;
import ssp.scheduleplanner.storage.StorageManager;
import ssp.scheduleplanner.storage.UserPrefsStorage;
import ssp.scheduleplanner.storage.XmlSchedulePlannerStorage;
import ssp.scheduleplanner.ui.Ui;
import ssp.scheduleplanner.ui.UiManager;

/**
 * The main entry point to the application.
 */
public class MainApp extends Application {
    public static final Version VERSION = new Version(0, 6, 0, true);

    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected Model model;
    protected Config config;
    protected UserPrefs userPrefs;


    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing SchedulePlanner ]===========================");
        super.init();

        AppParameters appParameters = AppParameters.parse(getParameters());
        config = initConfig(appParameters.getConfigPath());

        UserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(config.getUserPrefsFilePath());
        userPrefs = initPrefs(userPrefsStorage);
        SchedulePlannerStorage schedulePlannerStorage =
                new XmlSchedulePlannerStorage(userPrefs.getSchedulePlannerFilePath());
        storage = new StorageManager(schedulePlannerStorage, userPrefsStorage);

        initLogging(config);

        model = initModelManager(storage, userPrefs);

        logic = new LogicManager(model);

        ui = new UiManager(logic, config, userPrefs);

        initEventsCenter();
    }

    /**
     * Returns a {@code ModelManager} with the data from {@code storage}'s schedule planner and {@code userPrefs}. <br>
     * The data from the sample schedule planner will be used instead if {@code storage}'s schedule planner is not
     * found, or an empty schedule planner will be used instead if errors occur when reading {@code storage}'s
     * schedule planner.
     */
    private Model initModelManager(Storage storage, UserPrefs userPrefs) {
        Optional<ReadOnlySchedulePlanner> schedulePlannerOptional;
        ReadOnlySchedulePlanner initialData;
        try {
            schedulePlannerOptional = storage.readSchedulePlanner();
            if (!schedulePlannerOptional.isPresent()) {
                logger.info("Data file not found. Will be starting with a sample SchedulePlanner");
            }
            initialData = schedulePlannerOptional.orElseGet(SampleDataUtil::getSampleSchedulePlanner);
        } catch (DataConversionException e) {
            logger.warning("Data file not in the correct format. Will be starting with an empty SchedulePlanner");
            initialData = new SchedulePlanner();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty SchedulePlanner");
            initialData = new SchedulePlanner();
        }

        return new ModelManager(initialData, userPrefs);
    }

    private void initLogging(Config config) {
        LogsCenter.init(config);
    }

    /**
     * Returns a {@code Config} using the file at {@code configFilePath}. <br>
     * The default file path {@code Config#DEFAULT_CONFIG_FILE} will be used instead
     * if {@code configFilePath} is null.
     */
    protected Config initConfig(Path configFilePath) {
        Config initializedConfig;
        Path configFilePathUsed;

        configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

        if (configFilePath != null) {
            logger.info("Custom Config file specified " + configFilePath);
            configFilePathUsed = configFilePath;
        }

        logger.info("Using config file : " + configFilePathUsed);

        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataConversionException e) {
            logger.warning("Config file at " + configFilePathUsed + " is not in the correct format. "
                    + "Using default config properties");
            initializedConfig = new Config();
        }

        //Update config file in case it was missing to begin with or there are new/unused fields
        try {
            ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }
        return initializedConfig;
    }

    /**
     * Returns a {@code UserPrefs} using the file at {@code storage}'s user prefs file path,
     * or a new {@code UserPrefs} with default configuration if errors occur when
     * reading from the file.
     */
    protected UserPrefs initPrefs(UserPrefsStorage storage) {
        Path prefsFilePath = storage.getUserPrefsFilePath();
        logger.info("Using prefs file : " + prefsFilePath);

        UserPrefs initializedPrefs;
        try {
            Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
            initializedPrefs = prefsOptional.orElse(new UserPrefs());
        } catch (DataConversionException e) {
            logger.warning("UserPrefs file at " + prefsFilePath + " is not in the correct format. "
                    + "Using default user prefs");
            initializedPrefs = new UserPrefs();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty SchedulePlanner");
            initializedPrefs = new UserPrefs();
        }

        //Update prefs file in case it was missing to begin with or there are new/unused fields
        try {
            storage.saveUserPrefs(initializedPrefs);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }

        return initializedPrefs;
    }

    private void initEventsCenter() {
        EventsCenter.getInstance().registerHandler(this);
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting SchedulePlanner " + MainApp.VERSION);
        ui.start(primaryStage);
    }

    @Override
    public void stop() {
        logger.info("============================ [ Stopping Schedule Planner ] =============================");
        ui.stop();
        try {
            storage.saveUserPrefs(userPrefs);
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
        Platform.exit();
        System.exit(0);
    }

    @Subscribe
    public void handleExitAppRequestEvent(ExitAppRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
