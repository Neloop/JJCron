package cz.cuni.mff.ms.polankam.jjcron.rm;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 *
 * @author Neloop
 */
public class Core extends Application {

    private static final String PRG_TITLE = "JJCronRM";
    private static final String PRG_DESC = "JJCronRM";

    private Pane leftPane;
    private Pane descriptionPane;
    private final Map<String, ConnectionDetail> connectionsDetailMap;
    private final ObservableList<String> activeConnections;
    private final LoginDialogFactory loginDialogFactory;
    private final ConnectionDetailPaneHolder connectionDetailPaneHolder;

    public Core() {
        this.connectionsDetailMap = new HashMap<>();
        this.activeConnections = FXCollections.observableArrayList();
        this.loginDialogFactory = new LoginDialogFactory();
        this.connectionDetailPaneHolder = new ConnectionDetailPaneHolder(connectionsDetailMap);

        initLeftPane();
        initDescriptionPane();
    }

    private void newConnectionAction() {

        Dialog<Pair<String, String>> dialog = loginDialogFactory.createLoginDialog();
        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(value -> {
            String concat = value.getKey() + "/" + value.getValue();
            activeConnections.add(concat);
            connectionsDetailMap.put(concat, new ConnectionDetail(value.getKey(), value.getValue()));
            connectionDetailPaneHolder.switchToConnectionDetail(concat);
        });
    }

    private void initLeftPane() {
        leftPane = new AnchorPane();
        VBox leftVBox = new VBox();
        HBox buttonPane = new HBox();

        // construct list view which holds active connections list
        ListView<String> connListView = new ListView<>();
        connListView.setItems(activeConnections);
        connListView.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    connectionDetailPaneHolder.switchToConnectionDetail(newValue);
                }
        );
        VBox.setVgrow(connListView, Priority.ALWAYS);

        // add button which will create new connection
        Button newButton = new Button("New connection");
        newButton.setOnAction((ActionEvent event) -> {
            newConnectionAction();
        });
        buttonPane.setPadding(new Insets(0, 0, 10, 0));
        buttonPane.getChildren().add(newButton);
        buttonPane.setAlignment(Pos.CENTER);

        AnchorPane.setTopAnchor(leftVBox, 0.0);
        AnchorPane.setBottomAnchor(leftVBox, 0.0);
        leftVBox.setPadding(new Insets(10));
        leftVBox.getChildren().addAll(buttonPane, connListView);
        leftPane.getChildren().add(leftVBox);
    }

    private void initDescriptionPane() {
        StackPane descPane = new StackPane();

        Label descLabel = new Label(PRG_DESC);
        descPane.setAlignment(Pos.CENTER);
        descPane.setStyle("-fx-padding: 10 0 10 0;" +
                "-fx-border-width: 2 0 0 0; -fx-border-color: grey;" +
                "-fx-border-style: dotted none none none;");
        descPane.getChildren().add(descLabel);

        descriptionPane = descPane;
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane rootPane = new BorderPane();
        rootPane.setLeft(leftPane);
        rootPane.setCenter(connectionDetailPaneHolder.getRootPane());
        rootPane.setBottom(descriptionPane);

        Scene scene = new Scene(rootPane);

        primaryStage.setTitle(PRG_TITLE);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
