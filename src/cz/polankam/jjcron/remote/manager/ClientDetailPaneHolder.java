package cz.polankam.jjcron.remote.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Pair;

/**
 * Holder for all necesary parts located in client information area. This area
 * contains information about client, client action buttons and task list with
 * task details.
 *
 * @author Neloop
 */
public class ClientDetailPaneHolder {

    /**
     * Text visible on button in case of running client which is not paused.
     */
    private static final String PAUSE_BTN_TEXT = "Pause";
    /**
     * Text visible on button in case of paused client.
     */
    private static final String UNPAUSE_BTN_TEXT = "Unpause";
    /**
     * Text on list tasks button if tasks werent listed yet.
     */
    private static final String LIST_TASKS_BTN_TEXT = "List Cron Tasks";
    /**
     * Visible on list tasks button if tasks are already listed.
     */
    private static final String UNLIST_TASKS_BTN_TEXT = "Close Cron Tasks";
    /**
     * Shutdown button text.
     */
    private static final String SHUTDOWN_BTN_TEXT = "Shutdown";
    /**
     * Disconnect button text.
     */
    private static final String DISCONNECT_BTN_TEXT = "Disconnect";

    /**
     * Standard padding used in application.
     */
    private static final int STANDARD_PADDING = 10;
    /**
     * Width of all buttons in client actions area.
     */
    private static final double CLIENT_ACTION_BUTTON_WIDTH = 150;

    /**
     * Running status textual description.
     */
    private static final String RUNNING_STATUS = "Running";
    /**
     * Running status color description.
     */
    private static final Color RUNNING_STATUS_COLOR = Color.LIMEGREEN;
    /**
     * Paused status textual description.
     */
    private static final String PAUSED_STATUS = "Paused";
    /**
     * Paused status color description.
     */
    private static final Color PAUSED_STATUS_COLOR = Color.ORANGE;
    /**
     * Disconnected status textual description.
     */
    private static final String DISCONNECTED_STATUS = "Disconnected";
    /**
     * Showed color in case of disconnected status.
     */
    private static final Color DISCONNECTED_STATUS_COLOR = Color.GREY;

    /**
     * Standard Java logger.
     */
    private static final Logger logger
            = Logger.getLogger(ClientDetailPaneHolder.class.getName());

    /**
     * Root pane which should contain whole client area.
     */
    private Pane rootPane;
    /**
     * Field which contains not editable registry address of active client.
     */
    private TextField registryAddressText;
    /**
     * Field containing not editable registry identification of active client.
     */
    private TextField clientIdentificationText;
    /**
     * Circle which shows active client status.
     */
    private Circle clientStatusCircle;
    /**
     * Client actions menu part which is located in standard top application
     * menu.
     */
    private Menu clientActionsMenu;

    /**
     * Holder for all necesary panes and parts from task list area.
     */
    private final TaskListPaneHolder taskListPaneHolder;
    /**
     * Loading screen which can be shown and hidden through whole application.
     */
    private final LoadingScreen loadingScreen;
    /**
     * Factory for alert dialogs.
     */
    private final AlertDialogFactory alertDialogFactory;

    /**
     * Reference for pause client button.
     */
    private Button pauseClientButton;
    /**
     * Reference for pause client menu item.
     */
    private MenuItem pauseClientMenuItem;
    /**
     * List tasks button pointer.
     */
    private Button listTasksButton;
    /**
     * Menu item for list tasks action.
     */
    private MenuItem listTasksMenuItem;
    /**
     * List of buttons which are currently in client actions area.
     */
    private final List<Button> clientActionsButtonsList;

    /**
     * Contains all necessary things about clients.
     */
    private final ClientsHolder clientsList;
    /**
     * Reference to currently active client.
     */
    private Pair<String, ClientWrapper> activeClient;

    /**
     * Constructor which needs to have some information during construction
     * otherwise it will fail. Whole pane area is constructed in here.
     *
     * @param clientsHolder container for all actively connected clients
     * @param loadingScreen loading screen which can be shown and hidden
     * @throws ManagerException in case of any null argument
     */
    public ClientDetailPaneHolder(ClientsHolder clientsHolder,
            LoadingScreen loadingScreen) throws ManagerException {
        if (clientsHolder == null) {
            throw new ManagerException("ClientsHolder cannot be null");
        }
        if (loadingScreen == null) {
            throw new ManagerException("LoadingScreen cannot be null");
        }

        clientsList = clientsHolder;
        this.loadingScreen = loadingScreen;
        clientActionsButtonsList = new ArrayList<>();
        taskListPaneHolder = new TaskListPaneHolder(loadingScreen);
        alertDialogFactory = new AlertDialogFactory();
        initRootPane();
        initClientActionsMenu();
    }

    /**
     * Gets already constructed root pane.
     *
     * @return reference to whole client detail area
     */
    public Pane getRootPane() {
        return rootPane;
    }

    /**
     * Gets client actions menu items which can be added to the main menu at the
     * top of the page.
     *
     * @return one menu dropdown item
     */
    public Menu getClientMenu() {
        return clientActionsMenu;
    }

    /**
     * Switch active client to the one with given identification. This includes
     * change of every possible text field.
     *
     * @param id unique identification of client
     */
    public void switchToConnectionDetail(String id) {
        ClientWrapper client = clientsList.getClient(id);

        if (client == null) {
            return;
        }

        if (activeClient == null) {
            for (Button btn : clientActionsButtonsList) {
                btn.setDisable(false);
            }
        }

        activeClient = new Pair<>(id, client);
        registryAddressText.setText(
                client.getClientAddress().registryAddress);
        clientIdentificationText.setText(
                client.getClientAddress().clientIdentification);

        if (client.isListOpened()) {
            taskListPaneHolder.displayTaskList(client);
            listTasksButton.setText(UNLIST_TASKS_BTN_TEXT);
            listTasksMenuItem.setText(UNLIST_TASKS_BTN_TEXT);
        } else {
            taskListPaneHolder.clearTaskList();
            listTasksButton.setText(LIST_TASKS_BTN_TEXT);
            listTasksMenuItem.setText(LIST_TASKS_BTN_TEXT);
        }

        try {
            if (client.isPaused()) {
                clientStatusCircle.setFill(PAUSED_STATUS_COLOR);
                Tooltip.install(clientStatusCircle, new Tooltip(PAUSED_STATUS));
                pauseClientButton.setText(UNPAUSE_BTN_TEXT);
                pauseClientMenuItem.setText(UNPAUSE_BTN_TEXT);
            } else {
                clientStatusCircle.setFill(RUNNING_STATUS_COLOR);
                Tooltip.install(clientStatusCircle,
                        new Tooltip(RUNNING_STATUS));
                pauseClientButton.setText(PAUSE_BTN_TEXT);
                pauseClientMenuItem.setText(PAUSE_BTN_TEXT);
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error during isPaused() function", ex);
        }
    }

    /**
     * There is no actively connected clients or none of them is selected, then
     * all information about old active one has to be cleared.
     */
    private void clearConnectionDetail() {
        activeClient = null;
        registryAddressText.clear();
        clientIdentificationText.clear();
        taskListPaneHolder.clearTaskList();

        clientStatusCircle.setFill(DISCONNECTED_STATUS_COLOR);
        Tooltip.install(clientStatusCircle, new Tooltip(DISCONNECTED_STATUS));

        for (Button btn : clientActionsButtonsList) {
            btn.setDisable(true);
        }
    }

    /**
     * Remove currently selected client from clients container.
     */
    private void removeActiveClient() {
        clientsList.deleteClient(activeClient.getKey());
        if (clientsList.isEmpty()) {
            clearConnectionDetail();
        }
    }

    /**
     * Action which is triggered on click of disconnect button.
     */
    private void disconnectClientButtonAction() {
        if (activeClient == null) {
            return;
        }

        Alert alert = alertDialogFactory.createConfirmationDialog(
                "JJCron instance will be disconnected!");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK) {
            return;
        }

        // user confirmed dialog... proceed to actual action
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                activeClient.getValue().disconnect();
                return null;
            }
        };

        task.setOnRunning((event) -> {
            loadingScreen.show("Disconnecting ...");
        });
        task.setOnSucceeded((event) -> {
            removeActiveClient();
            loadingScreen.hide();
        });
        task.setOnFailed((event) -> {
            loadingScreen.hide();
            if (task.getException() != null) {
                logger.log(Level.SEVERE, task.getException().getMessage());
                alertDialogFactory.createErrorDialog(
                        task.getException().getMessage()).show();
            }
        });

        new Thread(task).start();
    }

    /**
     * Triggered after clicking on shutdown button.
     */
    private void shutdownClientButtonAction() {
        if (activeClient == null) {
            return;
        }

        Alert alert = alertDialogFactory.createConfirmationDialog(
                "Connected instance of JJCron will be shutted down!");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK) {
            return;
        }

        // user confirmed dialog... proceed to actual action
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                activeClient.getValue().shutdown();
                return null;
            }
        };

        task.setOnRunning((event) -> {
            loadingScreen.show("Disconnecting ...");
        });
        task.setOnSucceeded((event) -> {
            removeActiveClient();
            loadingScreen.hide();
        });
        task.setOnFailed((event) -> {
            loadingScreen.hide();
            if (task.getException() != null) {
                logger.log(Level.SEVERE, task.getException().getMessage());
                alertDialogFactory.createErrorDialog(
                        task.getException().getMessage()).show();
            }
        });

        new Thread(task).start();
    }

    /**
     * Action which is launched after clicking on pause button. There has to be
     * two branches of computation one on pausing the other one on unpausing.
     */
    private void pauseClientButtonAction() {
        if (activeClient == null) {
            return;
        }

        boolean paused = activeClient.getValue().isPaused();

        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                if (paused) {
                    activeClient.getValue().unpause();
                } else {
                    activeClient.getValue().pause();
                }
                return null;
            }
        };

        task.setOnRunning((event) -> {
            loadingScreen.show();
        });
        task.setOnSucceeded((event) -> {
            if (paused) {
                pauseClientButton.setText(PAUSE_BTN_TEXT);
                pauseClientMenuItem.setText(PAUSE_BTN_TEXT);
                clientStatusCircle.setFill(RUNNING_STATUS_COLOR);
                Tooltip.install(clientStatusCircle,
                        new Tooltip(RUNNING_STATUS));
            } else {
                pauseClientButton.setText(UNPAUSE_BTN_TEXT);
                pauseClientMenuItem.setText(UNPAUSE_BTN_TEXT);
                clientStatusCircle.setFill(PAUSED_STATUS_COLOR);
                Tooltip.install(clientStatusCircle, new Tooltip(PAUSED_STATUS));
            }

            loadingScreen.hide();
        });
        task.setOnFailed((event) -> {
            loadingScreen.hide();
            if (task.getException() != null) {
                logger.log(Level.SEVERE, task.getException().getMessage());
                alertDialogFactory.createErrorDialog(
                        task.getException().getMessage()).show();
            }
        });

        new Thread(task).start();
    }

    /**
     * Triggered after clicking on list tasks button. There has to be two paths
     * of execution one on opening list and the other one on closing list.
     */
    private void listTasksButtonAction() {
        if (activeClient == null) {
            return;
        }

        if (activeClient.getValue().isListOpened()) {
            // loading screen is not needed here...
            //   list should be closed immediatelly
            activeClient.getValue().closeTaskList();
            taskListPaneHolder.clearTaskList();
            listTasksButton.setText(LIST_TASKS_BTN_TEXT);
            listTasksMenuItem.setText(LIST_TASKS_BTN_TEXT);
        } else {
            Task task = new Task() {
                @Override
                protected Object call() throws Exception {
                    activeClient.getValue().openTaskList();
                    return null;
                }
            };

            task.setOnRunning((event) -> {
                loadingScreen.show();
            });
            task.setOnSucceeded((event) -> {
                activeClient.getValue().fillTaskObservableList();
                taskListPaneHolder.displayTaskList(activeClient.getValue());
                listTasksButton.setText(UNLIST_TASKS_BTN_TEXT);
                listTasksMenuItem.setText(UNLIST_TASKS_BTN_TEXT);
                loadingScreen.hide();
            });
            task.setOnFailed((event) -> {
                loadingScreen.hide();
                if (task.getException() != null) {
                    logger.log(Level.SEVERE, task.getException().getMessage());
                    alertDialogFactory.createErrorDialog(
                            task.getException().getMessage()).show();
                }
            });

            new Thread(task).start();
        }
    }

    /**
     * Populates client actions area with appropriate buttons.
     *
     * @param buttonsArea area in which button are visible
     */
    private void populateClientButtonsArea(VBox buttonsArea) {
        Button disconnButton = new Button(DISCONNECT_BTN_TEXT);
        disconnButton.setDisable(true);
        disconnButton.setMinWidth(CLIENT_ACTION_BUTTON_WIDTH);
        disconnButton.setOnAction((ActionEvent event) -> {
            disconnectClientButtonAction();
        });

        Button shutdownButton = new Button(SHUTDOWN_BTN_TEXT);
        shutdownButton.setDisable(true);
        shutdownButton.setMinWidth(CLIENT_ACTION_BUTTON_WIDTH);
        shutdownButton.setOnAction((ActionEvent event) -> {
            shutdownClientButtonAction();
        });

        pauseClientButton = new Button(PAUSE_BTN_TEXT);
        pauseClientButton.setDisable(true);
        pauseClientButton.setMinWidth(CLIENT_ACTION_BUTTON_WIDTH);
        pauseClientButton.setOnAction((ActionEvent event) -> {
            pauseClientButtonAction();
        });

        listTasksButton = new Button(LIST_TASKS_BTN_TEXT);
        listTasksButton.setDisable(true);
        listTasksButton.setMinWidth(CLIENT_ACTION_BUTTON_WIDTH);
        listTasksButton.setOnAction((ActionEvent event) -> {
            listTasksButtonAction();
        });

        buttonsArea.setStyle("-fx-border-width: 0 0 0 1;"
                + "-fx-border-color: grey;"
                + "-fx-border-style: solid;");
        buttonsArea.setSpacing(STANDARD_PADDING);
        buttonsArea.setAlignment(Pos.TOP_CENTER);
        buttonsArea.setPadding(new Insets(0, 0, 0, STANDARD_PADDING));
        buttonsArea.getChildren().addAll(listTasksButton, pauseClientButton,
                shutdownButton, disconnButton);

        clientActionsButtonsList.add(listTasksButton);
        clientActionsButtonsList.add(pauseClientButton);
        clientActionsButtonsList.add(shutdownButton);
        clientActionsButtonsList.add(disconnButton);
    }

    /**
     * Populates client information area with appropriate labels and fields.
     *
     * @param infoArea area in which information about client are stored
     */
    private void populateInfoArea(GridPane infoArea) {
        Label reg = new Label("Registry URL:");
        registryAddressText = new TextField();
        registryAddressText.setEditable(false);
        infoArea.add(reg, 0, 0);
        infoArea.add(registryAddressText, 1, 0);

        Label cli = new Label("Client ID:");
        clientIdentificationText = new TextField();
        clientIdentificationText.setEditable(false);
        infoArea.add(cli, 0, 1);
        infoArea.add(clientIdentificationText, 1, 1);

        Label stat = new Label("Status:");
        clientStatusCircle = new Circle(10);
        clientStatusCircle.setFill(Color.GREY);
        clientStatusCircle.setStroke(Color.BLACK);
        Tooltip.install(clientStatusCircle, new Tooltip(DISCONNECTED_STATUS));
        infoArea.add(stat, 2, 0);
        infoArea.add(clientStatusCircle, 3, 0);

        infoArea.setHgap(10);
        infoArea.setVgap(10);
        infoArea.setPadding(new Insets(0, 0, STANDARD_PADDING, 0));
    }

    /**
     * Construct and populates root pane of client detail area.
     */
    private void initRootPane() {
        rootPane = new AnchorPane();
        HBox centerHBox = new HBox();
        VBox detailAndListVBox = new VBox();
        GridPane infoArea = new GridPane();
        VBox buttonsArea = new VBox();

        // populate separated parts
        populateClientButtonsArea(buttonsArea);
        populateInfoArea(infoArea);

        AnchorPane.setTopAnchor(centerHBox, 0.0);
        AnchorPane.setBottomAnchor(centerHBox, 0.0);
        AnchorPane.setRightAnchor(centerHBox, 0.0);
        AnchorPane.setLeftAnchor(centerHBox, 0.0);

        HBox.setHgrow(detailAndListVBox, Priority.ALWAYS);
        VBox.setVgrow(taskListPaneHolder.getRootPane(), Priority.ALWAYS);

        detailAndListVBox.getChildren().addAll(infoArea,
                taskListPaneHolder.getRootPane());
        detailAndListVBox.setPadding(new Insets(STANDARD_PADDING,
                STANDARD_PADDING, 0, STANDARD_PADDING));
        detailAndListVBox.setStyle("-fx-border-width: 0 0 0 1;"
                + "-fx-border-color: grey;"
                + "-fx-border-style: solid;");

        centerHBox.setPadding(new Insets(STANDARD_PADDING, STANDARD_PADDING,
                STANDARD_PADDING, 0));
        centerHBox.getChildren().addAll(detailAndListVBox, buttonsArea);
        rootPane.getChildren().add(centerHBox);
    }

    /**
     * Construct and initialize all menu items visible in client actions menu.
     */
    private void initClientActionsMenu() {
        clientActionsMenu = new Menu("Client");

        listTasksMenuItem = new MenuItem(LIST_TASKS_BTN_TEXT);
        listTasksMenuItem.setOnAction((e) -> {
            listTasksButtonAction();
        });

        pauseClientMenuItem = new MenuItem(PAUSE_BTN_TEXT);
        pauseClientMenuItem.setOnAction((e) -> {
            pauseClientButtonAction();
        });

        MenuItem shutdown = new MenuItem(SHUTDOWN_BTN_TEXT);
        shutdown.setOnAction((e) -> {
            shutdownClientButtonAction();
        });

        MenuItem disconn = new MenuItem(DISCONNECT_BTN_TEXT);
        disconn.setOnAction((e) -> {
            disconnectClientButtonAction();
        });

        clientActionsMenu.getItems().addAll(listTasksMenuItem,
                pauseClientMenuItem, shutdown, disconn);
    }
}
