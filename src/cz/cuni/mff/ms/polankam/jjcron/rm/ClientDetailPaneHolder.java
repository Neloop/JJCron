package cz.cuni.mff.ms.polankam.jjcron.rm;

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
    private Pane rootAnchorPane;
    private TextField registryAddressTextArea;
    private TextField clientIdentificationTextArea;
    private Circle clientStatusCircle;

    private final ClientsList clientsList;
    private Pair<String, ClientDetail> activeClient;

    public ClientDetailPaneHolder(ClientsList connList) {
        clientsList = connList;
        initRootPane();
    }

    public Pane getRootPane() {
        return rootAnchorPane;
    }

    public void switchToConnectionDetail(String name) {
        ClientDetail conn = clientsList.getConnection(name);

        if (conn == null) {
            return;
        }

        activeClient = new Pair<>(name, conn);
        registryAddressTextArea.setText(conn.getRegistryAddress());
        clientIdentificationTextArea.setText(conn.getClientIdentification());

        clientStatusCircle.setFill(Color.LIMEGREEN); // TODO: read actual state
        Tooltip.install(clientStatusCircle, new Tooltip("Running"));
    }

    private void clearConnectionDetail() {
        activeClient = null;
        registryAddressTextArea.clear();
        clientIdentificationTextArea.clear();
        clientStatusCircle.setFill(Color.GREY);
    }

    private void removeActiveClient() {
        clientsList.deleteConnection(activeClient.getKey());
        if (clientsList.isEmpty()) {
            clearConnectionDetail();
        }
    }

    private void disconnectActiveClient() {
        if (activeClient == null) {
            return;
        }

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
        if (activeClient == null) {
            return;
        }

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
        // TODO: pause
    }

    private void listCronJobsInActiveClient() {
        // TODO:
    }

    private void displayAddCronJob() {
        // TODO:
    }

    private void displayDeleteCronJob() {
        // TODO:
    }

    private void populateButtonsArea(VBox buttonsArea) {
        Button disconnButton = new Button("Disconnect");
        disconnButton.setOnAction((ActionEvent event) -> {
            disconnectActiveClient();
        });

        Button shutdownButton = new Button("Shutdown");
        shutdownButton.setOnAction((ActionEvent event) -> {
            shutdownActiveClient();
        });

        Button pauseButton = new Button("Pause");
        pauseButton.setOnAction((ActionEvent event) -> {
            pauseActiveClient();
        });

        Button deleteButton = new Button("Delete Cron Job");
        deleteButton.setOnAction((ActionEvent event) -> {
            displayDeleteCronJob();
        });

        Button addButton = new Button("Add Cron Job");
        addButton.setOnAction((ActionEvent event) -> {
            displayAddCronJob();
        });

        Button listButton = new Button("List Cron Jobs");
        listButton.setOnAction((ActionEvent event) -> {
            listCronJobsInActiveClient();
        });

        buttonsArea.setStyle("-fx-border-width: 0 0 0 1;" +
                "-fx-border-color: grey;" +
                "-fx-border-style: none none none solid;");
        buttonsArea.setSpacing(10);
        buttonsArea.setAlignment(Pos.TOP_CENTER);
        buttonsArea.setPadding(new Insets(10));
        buttonsArea.getChildren().addAll(listButton, addButton, deleteButton,
                pauseButton, shutdownButton, disconnButton);
    }

    private void populateDetailArea(GridPane detailArea) {
        Label reg = new Label("Registry URL:");
        registryAddressTextArea = new TextField();
        registryAddressTextArea.setEditable(false);
        detailArea.add(reg, 0, 0);
        detailArea.add(registryAddressTextArea, 1, 0);

        Label cli = new Label("Client ID: ");
        clientIdentificationTextArea = new TextField();
        clientIdentificationTextArea.setEditable(false);
        detailArea.add(cli, 0, 1);
        detailArea.add(clientIdentificationTextArea, 1, 1);

        Label stat = new Label("Status: ");
        clientStatusCircle = new Circle(10);
        clientStatusCircle.setFill(Color.GREY);
        clientStatusCircle.setStroke(Color.BLACK);
        Tooltip.install(clientStatusCircle, new Tooltip("Disconnected"));
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
        populateButtonsArea(buttonsArea);
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
