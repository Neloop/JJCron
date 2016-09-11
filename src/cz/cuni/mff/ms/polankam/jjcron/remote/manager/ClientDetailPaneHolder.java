package cz.cuni.mff.ms.polankam.jjcron.remote.manager;

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
 *
 * @author Neloop
 */
public class ClientDetailPaneHolder {

    private static final String REGISTRY_URL_LABEL_TEXT = "Registry URL:";
    private static final String CLIENT_ID_LABEL_TEXT = "Client ID:";
    private static final String STATUS_LABEL_TEXT = "Status:";

    private static final String PAUSE_BTN_TEXT = "Pause";
    private static final String UNPAUSE_BTN_TEXT = "Unpause";
    private static final String LIST_TASKS_BTN_TEXT = "List Cron Tasks";
    private static final String UNLIST_TASKS_BTN_TEXT = "Close Cron Tasks";
    private static final String SHUTDOWN_BTN_TEXT = "Shutdown";
    private static final String DISCONNECT_BTN_TEXT = "Disconnect";

    private static final int STANDARD_PADDING = 10;
    private static final double CLIENT_ACTION_BUTTON_WIDTH = 150;

    private static final String RUNNING_STATUS = "Running";
    private static final Color RUNNING_STATUS_COLOR = Color.LIMEGREEN;
    private static final String PAUSED_STATUS = "Paused";
    private static final Color PAUSED_STATUS_COLOR = Color.ORANGE;
    private static final String DISCONNECTED_STATUS = "Disconnected";
    private static final Color DISCONNECTED_STATUS_COLOR = Color.GREY;

    private static final Logger logger = Logger.getLogger(ClientDetailPaneHolder.class.getName());

    /**
     *
     */
    private Pane rootAnchorPane;
    /**
     *
     */
    private TextField registryAddressTextArea;
    /**
     *
     */
    private TextField clientIdentificationTextArea;
    /**
     *
     */
    private Circle clientStatusCircle;
    /**
     *
     */
    private Menu clientActionsMenu;

    /**
     *
     */
    private final TaskListPaneHolder taskListPaneHolder;
    /**
     *
     */
    private final LoadingScreen loadingScreen;
    /**
     *
     */
    private final AlertDialogFactory alertDialogFactory;

    /**
     *
     */
    private Button pauseClientButton;
    /**
     *
     */
    private MenuItem pauseClientMenuItem;
    /**
     *
     */
    private Button listTasksButton;
    /**
     *
     */
    private MenuItem listTasksMenuItem;
    /**
     *
     */
    private final List<Button> clientActionButtonsList;

    /**
     *
     */
    private final ClientsHolder clientsList;
    /**
     *
     */
    private Pair<String, ClientWrapper> activeClient;

    /**
     *
     * @param connList
     * @param loadingScreen
     */
    public ClientDetailPaneHolder(ClientsHolder connList, LoadingScreen loadingScreen) {
        clientsList = connList;
        this.loadingScreen = loadingScreen;
        clientActionButtonsList = new ArrayList<>();
        taskListPaneHolder = new TaskListPaneHolder(loadingScreen);
        alertDialogFactory = new AlertDialogFactory();
        initRootPane();
        initClientActionsMenu();
    }

    /**
     *
     * @return
     */
    public Pane getRootPane() {
        return rootAnchorPane;
    }

    public Menu getClientMenu() {
        return clientActionsMenu;
    }

    /**
     *
     * @param name
     */
    public void switchToConnectionDetail(String name) {
        ClientWrapper client = clientsList.getClient(name);

        if (client == null) {
            return;
        }

        if (activeClient == null) {
            for (Button btn : clientActionButtonsList) {
                btn.setDisable(false);
            }
        }

        activeClient = new Pair<>(name, client);
        registryAddressTextArea.setText(client.getClientAddress().registryAddress);
        clientIdentificationTextArea.setText(client.getClientAddress().clientIdentification);

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
                Tooltip.install(clientStatusCircle, new Tooltip(RUNNING_STATUS));
                pauseClientButton.setText(PAUSE_BTN_TEXT);
                pauseClientMenuItem.setText(PAUSE_BTN_TEXT);
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error during isPaused() function", ex);
        }
    }

    /**
     *
     */
    private void clearConnectionDetail() {
        activeClient = null;
        registryAddressTextArea.clear();
        clientIdentificationTextArea.clear();
        taskListPaneHolder.clearTaskList();

        clientStatusCircle.setFill(DISCONNECTED_STATUS_COLOR);
        Tooltip.install(clientStatusCircle, new Tooltip(DISCONNECTED_STATUS));

        for (Button btn : clientActionButtonsList) {
            btn.setDisable(true);
        }
    }

    /**
     *
     */
    private void removeActiveClient() {
        clientsList.deleteClient(activeClient.getKey());
        if (clientsList.isEmpty()) {
            clearConnectionDetail();
        }
    }

    /**
     *
     */
    private void disconnectClientButtonAction() {
        if (activeClient == null) {
            return;
        }

        Alert alert = alertDialogFactory.createConfirmationDialog("JJCron instance will be disconnected!");

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
                alertDialogFactory.createErrorDialog(task.getException().getMessage()).show();
            }
        });

        new Thread(task).start();
    }

    /**
     *
     */
    private void shutdownClientButtonAction() {
        if (activeClient == null) {
            return;
        }

        Alert alert = alertDialogFactory.createConfirmationDialog("Connected instance of JJCron will be shutted down!");

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
                alertDialogFactory.createErrorDialog(task.getException().getMessage()).show();
            }
        });

        new Thread(task).start();
    }

    /**
     *
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
                Tooltip.install(clientStatusCircle, new Tooltip(RUNNING_STATUS));
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
                alertDialogFactory.createErrorDialog(task.getException().getMessage()).show();
            }
        });

        new Thread(task).start();
    }

    /**
     *
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
                    alertDialogFactory.createErrorDialog(task.getException().getMessage()).show();
                }
            });

            new Thread(task).start();
        }
    }

    /**
     *
     * @param buttonsArea
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

        clientActionButtonsList.add(listTasksButton);
        clientActionButtonsList.add(pauseClientButton);
        clientActionButtonsList.add(shutdownButton);
        clientActionButtonsList.add(disconnButton);
    }

    /**
     *
     * @param detailArea
     */
    private void populateDetailArea(GridPane detailArea) {
        Label reg = new Label(REGISTRY_URL_LABEL_TEXT);
        registryAddressTextArea = new TextField();
        registryAddressTextArea.setEditable(false);
        detailArea.add(reg, 0, 0);
        detailArea.add(registryAddressTextArea, 1, 0);

        Label cli = new Label(CLIENT_ID_LABEL_TEXT);
        clientIdentificationTextArea = new TextField();
        clientIdentificationTextArea.setEditable(false);
        detailArea.add(cli, 0, 1);
        detailArea.add(clientIdentificationTextArea, 1, 1);

        Label stat = new Label(STATUS_LABEL_TEXT);
        clientStatusCircle = new Circle(10);
        clientStatusCircle.setFill(Color.GREY);
        clientStatusCircle.setStroke(Color.BLACK);
        Tooltip.install(clientStatusCircle, new Tooltip(DISCONNECTED_STATUS));
        detailArea.add(stat, 2, 0);
        detailArea.add(clientStatusCircle, 3, 0);

        detailArea.setHgap(10);
        detailArea.setVgap(10);
        detailArea.setPadding(new Insets(0, 0, STANDARD_PADDING, 0));
    }

    /**
     *
     */
    private void initRootPane() {
        rootAnchorPane = new AnchorPane();
        HBox centerHBox = new HBox();
        VBox detailAndListVBox = new VBox();
        GridPane detailArea = new GridPane();
        VBox buttonsArea = new VBox();

        // populate separated parts
        populateClientButtonsArea(buttonsArea);
        populateDetailArea(detailArea);

        AnchorPane.setTopAnchor(centerHBox, 0.0);
        AnchorPane.setBottomAnchor(centerHBox, 0.0);
        AnchorPane.setRightAnchor(centerHBox, 0.0);
        AnchorPane.setLeftAnchor(centerHBox, 0.0);

        HBox.setHgrow(detailAndListVBox, Priority.ALWAYS);
        VBox.setVgrow(taskListPaneHolder.getRootPane(), Priority.ALWAYS);

        detailAndListVBox.getChildren().addAll(detailArea, taskListPaneHolder.getRootPane());
        detailAndListVBox.setPadding(new Insets(STANDARD_PADDING, STANDARD_PADDING, 0, STANDARD_PADDING));
        detailAndListVBox.setStyle("-fx-border-width: 0 0 0 1;"
                + "-fx-border-color: grey;"
                + "-fx-border-style: solid;");

        centerHBox.setPadding(new Insets(STANDARD_PADDING, STANDARD_PADDING, STANDARD_PADDING, 0));
        centerHBox.getChildren().addAll(detailAndListVBox, buttonsArea);
        rootAnchorPane.getChildren().add(centerHBox);
    }

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

        clientActionsMenu.getItems().addAll(listTasksMenuItem, pauseClientMenuItem, shutdown, disconn);
    }
}
