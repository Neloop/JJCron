package cz.cuni.mff.ms.polankam.jjcron.rm;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
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

    private static final double CLIENT_ACTION_BUTTON_WIDTH = 150;

    private static final String RUNNING_STATUS = "Running";
    private static final Color RUNNING_STATUS_COLOR = Color.LIMEGREEN;
    private static final String PAUSED_STATUS = "Paused";
    private static final Color PAUSED_STATUS_COLOR = Color.ORANGE;
    private static final String DISCONNECTED_STATUS = "Disconnected";
    private static final Color DISCONNECTED_STATUS_COLOR = Color.GREY;

    private Pane rootAnchorPane;
    private TextField registryAddressTextArea;
    private TextField clientIdentificationTextArea;
    private Circle clientStatusCircle;
    private final TaskListPaneHolder taskListPaneHolder;

    private Button clientPauseButton;
    private Button listTasksButton;
    private final List<Button> clientActionButtonsList;

    private final ClientsList clientsList;
    private Pair<String, ClientHolder> activeClient;

    public ClientDetailPaneHolder(ClientsList connList) {
        clientsList = connList;
        clientActionButtonsList = new ArrayList<>();
        taskListPaneHolder = new TaskListPaneHolder();
        initRootPane();
    }

    public Pane getRootPane() {
        return rootAnchorPane;
    }

    public void switchToConnectionDetail(String name) {
        ClientHolder client = clientsList.getConnection(name);

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
        } else {
            taskListPaneHolder.clearTaskList();
            listTasksButton.setText(LIST_TASKS_BTN_TEXT);
        }

        if (client.isPaused()) {
            clientStatusCircle.setFill(PAUSED_STATUS_COLOR);
            Tooltip.install(clientStatusCircle, new Tooltip(PAUSED_STATUS));
            clientPauseButton.setText(UNPAUSE_BTN_TEXT);
        } else {
            clientStatusCircle.setFill(RUNNING_STATUS_COLOR);
            Tooltip.install(clientStatusCircle, new Tooltip(RUNNING_STATUS));
            clientPauseButton.setText(PAUSE_BTN_TEXT);
        }
    }

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

    private void removeActiveClient() {
        clientsList.deleteConnection(activeClient.getKey());
        if (clientsList.isEmpty()) {
            clearConnectionDetail();
        }
    }

    private void disconnectClientButtonAction() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("JJCron instance will be disconnected!");
        alert.setContentText("Continue?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK){
            return;
        }

        activeClient.getValue().disconnect();
        removeActiveClient();
    }

    private void shutdownClientButtonAction() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Connected instance of JJCron will be shutted down!");
        alert.setContentText("Continue?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK){
            return;
        }

        activeClient.getValue().shutdown();
        removeActiveClient();
    }

    private void pauseClientButtonAction() {
        if (activeClient.getValue().isPaused()) {
            activeClient.getValue().unpause();
            clientPauseButton.setText(PAUSE_BTN_TEXT);
            clientStatusCircle.setFill(RUNNING_STATUS_COLOR);
            Tooltip.install(clientStatusCircle, new Tooltip(RUNNING_STATUS));
        } else {
            activeClient.getValue().pause();
            clientPauseButton.setText(UNPAUSE_BTN_TEXT);
            clientStatusCircle.setFill(PAUSED_STATUS_COLOR);
            Tooltip.install(clientStatusCircle, new Tooltip(PAUSED_STATUS));
        }
    }

    private void listTasksButtonAction() {
        if (activeClient.getValue().isListOpened()) {
            activeClient.getValue().closeTaskList();
            taskListPaneHolder.clearTaskList();
            listTasksButton.setText(LIST_TASKS_BTN_TEXT);
        } else {
            activeClient.getValue().openTaskList();
            taskListPaneHolder.displayTaskList(activeClient.getValue());
            listTasksButton.setText(UNLIST_TASKS_BTN_TEXT);
        }
    }

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

        clientPauseButton = new Button(PAUSE_BTN_TEXT);
        clientPauseButton.setDisable(true);
        clientPauseButton.setMinWidth(CLIENT_ACTION_BUTTON_WIDTH);
        clientPauseButton.setOnAction((ActionEvent event) -> {
            pauseClientButtonAction();
        });

        listTasksButton = new Button(LIST_TASKS_BTN_TEXT);
        listTasksButton.setDisable(true);
        listTasksButton.setMinWidth(CLIENT_ACTION_BUTTON_WIDTH);
        listTasksButton.setOnAction((ActionEvent event) -> {
            listTasksButtonAction();
        });

        buttonsArea.setStyle("-fx-border-width: 0 0 0 1;" +
                "-fx-border-color: grey;" +
                "-fx-border-style: solid;");
        buttonsArea.setSpacing(10);
        buttonsArea.setAlignment(Pos.TOP_CENTER);
        buttonsArea.setPadding(new Insets(0, 0, 0, 10));
        buttonsArea.getChildren().addAll(listTasksButton, clientPauseButton,
                shutdownButton, disconnButton);


        clientActionButtonsList.add(listTasksButton);
        clientActionButtonsList.add(clientPauseButton);
        clientActionButtonsList.add(shutdownButton);
        clientActionButtonsList.add(disconnButton);
    }

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
        detailArea.setPadding(new Insets(0, 0, 10, 0));
    }

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
        detailAndListVBox.setPadding(new Insets(10, 10, 0, 10));
        detailAndListVBox.setStyle("-fx-border-width: 0 0 0 1;" +
                "-fx-border-color: grey;" +
                "-fx-border-style: solid;");

        centerHBox.setPadding(new Insets(10, 10, 10, 0));
        centerHBox.getChildren().addAll(detailAndListVBox, buttonsArea);
        rootAnchorPane.getChildren().add(centerHBox);
    }
}
