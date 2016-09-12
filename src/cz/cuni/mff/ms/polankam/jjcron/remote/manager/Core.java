package cz.cuni.mff.ms.polankam.jjcron.remote.manager;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 *
 * @author Neloop
 */
public class Core extends Application {

    private static final String DESC_DELIM = " - ";
    private static final String NEW_CONNECTION_BTN_TEXT = "New Connection";

    private static final int STANDARD_PADDING = 10;

    private static final Logger logger = Logger.getLogger(Core.class.getName());

    /**
     *
     */
    private MenuBar menuBar;
    /**
     *
     */
    private Pane leftPane;
    /**
     *
     */
    private Pane descriptionPane;
    /**
     *
     */
    private ListView<String> clientsListView;

    /**
     *
     */
    private final ClientsHolder clientsList;
    /**
     *
     */
    private final ConnectionDialogFactory loginDialogFactory;
    /**
     *
     */
    private final AlertDialogFactory alertDialogFactory;
    /**
     *
     */
    private final ClientDetailPaneHolder clientDetailPaneHolder;
    /**
     *
     */
    private final LoadingScreen loadingScreen;
    /**
     *
     */
    private final AboutDialogFactory aboutDialogFactory;

    /**
     *
     */
    private final AppInfo appInfo;
    /**
     *
     */
    private final HostServices hostServices;

    /**
     *
     */
    public Core() {
        appInfo = new AppInfo();
        hostServices = getHostServices();
        clientsList = new ClientsHolder();
        loginDialogFactory = new ConnectionDialogFactory();
        loadingScreen = new LoadingScreen();
        alertDialogFactory = new AlertDialogFactory();
        clientDetailPaneHolder = new ClientDetailPaneHolder(clientsList, loadingScreen);
        aboutDialogFactory = new AboutDialogFactory(appInfo, hostServices);

        initMenu();
        initLeftPane();
        initDescriptionPane();
    }

    /**
     *
     */
    private void newConnectionButtonAction() {
        Dialog<Pair<String, String>> dialog = loginDialogFactory.createLoginDialog();
        Optional<Pair<String, String>> result = dialog.showAndWait();

        Task<ClientWrapper> task = new Task<ClientWrapper>() {
            @Override
            protected ClientWrapper call() throws Exception {
                if (result.isPresent()) {
                    Pair<String, String> value = result.get();
                    ClientAddress addr = new ClientAddress(value.getKey(), value.getValue());
                    return new ClientWrapper(addr, new RMIClientFactory());
                }
                return null;
            }
        };

        task.setOnRunning((event) -> {
            loadingScreen.show("Connecting ...");
        });
        task.setOnSucceeded((event) -> {
            try {
                String id = clientsList.addClient(task.get());
                clientDetailPaneHolder.switchToConnectionDetail(id);
                clientsListView.getSelectionModel().select(id);
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage());
            } finally {
                loadingScreen.hide();
            }
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
    private void initMenu() {
        menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        Menu clientMenu = clientDetailPaneHolder.getClientMenu();
        Menu helpMenu = new Menu("Help");

        MenuItem newConn = new MenuItem("New connection");
        newConn.setOnAction((e) -> {
            newConnectionButtonAction();
        });

        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction((e) -> {
            System.exit(0);
        });

        MenuItem about = new MenuItem("About");
        about.setOnAction((e) -> {
            aboutDialogFactory.createAboutDialog().show();
        });

        fileMenu.getItems().addAll(newConn, new SeparatorMenuItem(), exit);
        helpMenu.getItems().addAll(about);

        menuBar.setPadding(Insets.EMPTY);
        menuBar.getMenus().addAll(fileMenu, clientMenu, helpMenu);
    }

    /**
     *
     */
    private void initLeftPane() {
        leftPane = new AnchorPane();
        VBox leftVBox = new VBox();
        HBox buttonPane = new HBox();

        // construct list view which holds active connections list
        clientsListView = new ListView<>();
        clientsListView.setItems(clientsList.clientsObservableList);
        clientsListView.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    clientDetailPaneHolder.switchToConnectionDetail(newValue);
                }
        );
        VBox.setVgrow(clientsListView, Priority.ALWAYS);

        // add button which will create new connection
        Button newButton = new Button(NEW_CONNECTION_BTN_TEXT);
        newButton.setOnAction((e) -> {
            newConnectionButtonAction();
        });
        buttonPane.setPadding(new Insets(0, 0, STANDARD_PADDING, 0));
        buttonPane.getChildren().add(newButton);
        buttonPane.setAlignment(Pos.CENTER);

        AnchorPane.setTopAnchor(leftVBox, 0.0);
        AnchorPane.setBottomAnchor(leftVBox, 0.0);
        leftVBox.setPadding(new Insets(STANDARD_PADDING));
        leftVBox.getChildren().addAll(buttonPane, clientsListView);
        leftPane.getChildren().add(leftVBox);
    }

    /**
     *
     */
    private void initDescriptionPane() {
        Hyperlink link = new Hyperlink(appInfo.sourceLinkText);
        link.setPadding(Insets.EMPTY);
        link.setOnAction((e) -> {
            hostServices.showDocument(appInfo.sourceLink);
        });

        TextFlow description = new TextFlow();
        description.getChildren().addAll(new Text(appInfo.title),
                new Text(DESC_DELIM), new Text(appInfo.license),
                new Text(DESC_DELIM), link);

        HBox descPane = new HBox();
        descPane.setAlignment(Pos.CENTER);
        descPane.setPadding(new Insets(STANDARD_PADDING, 0, STANDARD_PADDING, 0));
        descPane.setStyle("-fx-border-width: 2 0 0 0;"
                + "-fx-border-color: grey;"
                + "-fx-border-style: dotted;");
        descPane.getChildren().addAll(description);

        descriptionPane = descPane;
    }

    /**
     *
     * @param stage
     */
    @Override
    public void start(Stage stage) {
        // set main stage to loading screen for proper positioning
        loadingScreen.setMainStage(stage);

        BorderPane rootPane = new BorderPane();
        rootPane.setTop(menuBar);
        rootPane.setLeft(leftPane);
        rootPane.setCenter(clientDetailPaneHolder.getRootPane());
        rootPane.setBottom(descriptionPane);

        Scene scene = new Scene(rootPane);

        stage.setTitle(appInfo.title);
        stage.setScene(scene);
        //primaryStage.setMaximized(true);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
