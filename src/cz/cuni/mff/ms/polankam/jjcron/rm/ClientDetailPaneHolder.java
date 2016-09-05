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
    private static final String LIST_JOBS_BTN_TEXT = "List Cron Jobs";
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
    private Button clientPauseButton;
    private final List<Button> clientActionButtonsList;

    private final ClientsList clientsList;
    private Pair<String, ClientHolder> activeClient;

    public ClientDetailPaneHolder(ClientsList connList) {
        clientsList = connList;
        clientActionButtonsList = new ArrayList<>();
        initRootPane();
    }

    public Pane getRootPane() {
        return rootAnchorPane;
    }

    public void switchToConnectionDetail(String name) {
        ClientHolder conn = clientsList.getConnection(name);

        if (conn == null) {
            return;
        }

        if (activeClient == null) {
            for (Button btn : clientActionButtonsList) {
                btn.setDisable(false);
            }
        }

        activeClient = new Pair<>(name, conn);
        registryAddressTextArea.setText(conn.getRegistryAddress());
        clientIdentificationTextArea.setText(conn.getClientIdentification());

        if (activeClient.getValue().isPaused()) {
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

    private void disconnectActiveClient() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("JJCron instance will be disconnected!");
        alert.setContentText("Continue?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK){
            return;
        }

        removeActiveClient();
    }

    private void shutdownActiveClient() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Connected instance of JJCron will be shutted down!");
        alert.setContentText("Continue?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK){
            return;
        }

        // TODO: have to send stop message to client which will be ended
        removeActiveClient();
    }

    private void pauseActiveClient() {
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

    private void listCronJobsInActiveClient() {
        // TODO:
    }

    private void populateClientButtonsArea(VBox buttonsArea) {
        Button disconnButton = new Button(DISCONNECT_BTN_TEXT);
        disconnButton.setDisable(true);
        disconnButton.setMinWidth(CLIENT_ACTION_BUTTON_WIDTH);
        disconnButton.setOnAction((ActionEvent event) -> {
            disconnectActiveClient();
        });

        Button shutdownButton = new Button(SHUTDOWN_BTN_TEXT);
        shutdownButton.setDisable(true);
        shutdownButton.setMinWidth(CLIENT_ACTION_BUTTON_WIDTH);
        shutdownButton.setOnAction((ActionEvent event) -> {
            shutdownActiveClient();
        });

        clientPauseButton = new Button(PAUSE_BTN_TEXT);
        clientPauseButton.setDisable(true);
        clientPauseButton.setMinWidth(CLIENT_ACTION_BUTTON_WIDTH);
        clientPauseButton.setOnAction((ActionEvent event) -> {
            pauseActiveClient();
        });

        Button listButton = new Button(LIST_JOBS_BTN_TEXT);
        listButton.setDisable(true);
        listButton.setMinWidth(CLIENT_ACTION_BUTTON_WIDTH);
        listButton.setOnAction((ActionEvent event) -> {
            listCronJobsInActiveClient();
        });

        buttonsArea.setStyle("-fx-border-width: 0 0 0 1;" +
                "-fx-border-color: grey;" +
                "-fx-border-style: none none none solid;");
        buttonsArea.setSpacing(10);
        buttonsArea.setAlignment(Pos.TOP_CENTER);
        buttonsArea.setPadding(new Insets(10));
        buttonsArea.getChildren().addAll(listButton, clientPauseButton,
                shutdownButton, disconnButton);


        clientActionButtonsList.add(listButton);
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
        VBox centerVBox = new VBox();
        GridPane detailArea = new GridPane();
        GridPane lowerPane = new GridPane();
        VBox buttonsArea = new VBox();

        // populate separated parts
        populateClientButtonsArea(buttonsArea);
        populateDetailArea(detailArea);

        lowerPane.setPadding(new Insets(10, 0, 0, 0));
        lowerPane.setStyle("-fx-border-width: 1 0 0 0;" +
                "-fx-border-color: grey;" +
                "-fx-border-style: solid none none none;");
        lowerPane.add(new Label("Lower Pane"), 0, 0);

        AnchorPane.setTopAnchor(centerHBox, 0.0);
        AnchorPane.setBottomAnchor(centerHBox, 0.0);
        AnchorPane.setRightAnchor(centerHBox, 0.0);
        AnchorPane.setLeftAnchor(centerHBox, 0.0);

        HBox.setHgrow(centerVBox, Priority.ALWAYS);
        centerVBox.getChildren().addAll(detailArea, lowerPane);
        centerVBox.setPadding(new Insets(10));
        centerHBox.setPadding(new Insets(10, 0, 10, 0));
        centerHBox.getChildren().addAll(centerVBox, buttonsArea);
        rootAnchorPane.getChildren().add(centerHBox);
    }
}
