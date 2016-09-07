package cz.cuni.mff.ms.polankam.jjcron.rm;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
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
    private static final String NEW_CONNECTION_BTN_TEXT = "New Connection";

    private Pane leftPane;
    private Pane descriptionPane;
    private ListView<String> clientsListView;
    private final ClientsList clientsList;
    private final LoginDialogFactory loginDialogFactory;
    private final ClientDetailPaneHolder clientDetailPaneHolder;

    public Core() {
        clientsList = new ClientsList();
        loginDialogFactory = new LoginDialogFactory();
        clientDetailPaneHolder = new ClientDetailPaneHolder(clientsList);

        initLeftPane();
        initDescriptionPane();
    }

    private void newConnectionButtonAction() {
        Dialog<Pair<String, String>> dialog = loginDialogFactory.createLoginDialog();
        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(value -> {
            ClientAddress addr = new ClientAddress(value.getKey(), value.getValue());
            String id = clientsList.addConnection(addr);
            clientDetailPaneHolder.switchToConnectionDetail(id);
            clientsListView.getSelectionModel().select(id);
        });
    }

    private void initLeftPane() {
        leftPane = new AnchorPane();
        VBox leftVBox = new VBox();
        HBox buttonPane = new HBox();

        // construct list view which holds active connections list
        clientsListView = new ListView<>();
        clientsListView.setItems(clientsList.getObservableList());
        clientsListView.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    clientDetailPaneHolder.switchToConnectionDetail(newValue);
                }
        );
        VBox.setVgrow(clientsListView, Priority.ALWAYS);

        // add button which will create new connection
        Button newButton = new Button(NEW_CONNECTION_BTN_TEXT);
        newButton.setOnAction((ActionEvent event) -> {
            newConnectionButtonAction();
        });
        buttonPane.setPadding(new Insets(0, 0, 10, 0));
        buttonPane.getChildren().add(newButton);
        buttonPane.setAlignment(Pos.CENTER);

        AnchorPane.setTopAnchor(leftVBox, 0.0);
        AnchorPane.setBottomAnchor(leftVBox, 0.0);
        leftVBox.setPadding(new Insets(10));
        leftVBox.getChildren().addAll(buttonPane, clientsListView);
        leftPane.getChildren().add(leftVBox);
    }

    private void initDescriptionPane() {
        HBox descPane = new HBox();

        descPane.setAlignment(Pos.CENTER);
        descPane.setStyle("-fx-padding: 10 0 10 0;" +
                "-fx-border-width: 2 0 0 0;" +
                "-fx-border-color: grey;" +
                "-fx-border-style: dotted;");

        Label descLabel = new Label(PRG_DESC);
        descPane.getChildren().addAll(descLabel);

        descriptionPane = descPane;
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane rootPane = new BorderPane();
        rootPane.setLeft(leftPane);
        rootPane.setCenter(clientDetailPaneHolder.getRootPane());
        rootPane.setBottom(descriptionPane);

        Scene scene = new Scene(rootPane);

        primaryStage.setTitle(PRG_TITLE);
        primaryStage.setScene(scene);
        //primaryStage.setMaximized(true);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
