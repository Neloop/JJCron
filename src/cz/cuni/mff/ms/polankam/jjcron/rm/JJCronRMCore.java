package cz.cuni.mff.ms.polankam.jjcron.rm;

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
public class JJCronRMCore extends Application {

    private static final String PRG_TITLE = "JJCronRM";
    private static final String PRG_DESC = "JJCronRM";

    private ObservableList<String> activeConnections;
    private Pane leftPane;
    private Pane descriptionPane;
    private Pane centerPane;
    private final LoginDialogFactory loginDialogFactory = new LoginDialogFactory();

    public JJCronRMCore() {
        initLeftPane();
        initCenterPane();
        initDescriptionPane();
    }

    private void newConnectionAction(ActionEvent event) {

        Dialog<Pair<String, String>> dialog = loginDialogFactory.createLoginDialog();
        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(value -> {
            activeConnections.add(value.getKey() + " " + value.getValue());
        });
    }

    private Pane initLeftPane() {
        AnchorPane leftAnchor = new AnchorPane();
        VBox leftVBox = new VBox();
        HBox buttonPane = new HBox();

        activeConnections = FXCollections.observableArrayList();
        ListView<String> connListView = new ListView<>();
        connListView.setItems(activeConnections);
        VBox.setVgrow(connListView, Priority.ALWAYS);

        Button newButton = new Button("New connection");
        newButton.setOnAction((ActionEvent event) -> {
            newConnectionAction(event);
        });
        buttonPane.setPadding(new Insets(0, 0, 10, 0));
        buttonPane.getChildren().add(newButton);
        buttonPane.setAlignment(Pos.CENTER);

        AnchorPane.setTopAnchor(leftVBox, 0.0);
        AnchorPane.setBottomAnchor(leftVBox, 0.0);
        leftVBox.setPadding(new Insets(10));
        leftVBox.getChildren().addAll(buttonPane, connListView);
        leftAnchor.getChildren().add(leftVBox);

        this.leftPane = leftAnchor;
        return leftAnchor;
    }

    private Pane initCenterPane() {
        AnchorPane centerAnchor = new AnchorPane();
        HBox centerHBox = new HBox();

        centerAnchor.getChildren().add(centerHBox);

        this.centerPane = centerAnchor;
        return centerAnchor;
    }

    private Pane initDescriptionPane() {
        Label descLabel = new Label(PRG_DESC);
        StackPane descPane = new StackPane();
        descPane.setAlignment(Pos.CENTER);
        descPane.setStyle("-fx-padding: 10 0 10 0;" +
                "-fx-border-width: 2 0 0 0; -fx-border-color: grey;" +
                "-fx-border-style: dotted none none none;");
        descPane.getChildren().add(descLabel);

        descriptionPane = descPane;
        return descPane;
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane rootPane = new BorderPane();
        rootPane.setLeft(leftPane);
        rootPane.setCenter(centerPane);
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
