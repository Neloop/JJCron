package cz.polankam.jjcron.remote.manager;

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
 * Entry point of whole JJCronRM which serves as initial JavaFX application.
 *
 * @author Neloop
 */
public final class Core extends Application {

    /**
     * Delimiter used in application description bottom panel.
     */
    private static final String DESC_DELIM = " - ";
    /**
     * Text on new connection button.
     */
    private static final String NEW_CONNECTION_BTN_TEXT = "New Connection";

    /**
     * Standard padding used in whole application.
     */
    private static final int STANDARD_PADDING = 10;

    /**
     * Standard Java logger.
     */
    private static final Logger logger = Logger.getLogger(Core.class.getName());

    /**
     * Menu bar which is shown on the top of the application layout.
     */
    private MenuBar menuBar;
    /**
     * Left area which contains list of actively connected clients.
     */
    private Pane leftPane;
    /**
     * Bottom area of layout which contains some short information about
     * application.
     */
    private Pane descriptionPane;
    /**
     * Contains all actively connected clients.
     */
    private ListView<String> clientsListView;

    /**
     * Holder of clients collections.
     */
    private final ClientsHolder clientsList;
    /**
     * Factory for new connection dialog.
     */
    private final ConnectionDialogFactory connectionDialogFactory;
    /**
     * Factory for alert dialogs.
     */
    private final AlertDialogFactory alertDialogFactory;
    /**
     * Holder for all necesary panes and parts from client information area.
     */
    private final ClientDetailPaneHolder clientDetailPaneHolder;
    /**
     * Loading screen instance which can be shown and hidden.
     */
    private final LoadingScreen loadingScreen;
    /**
     * Factory for about dialog window which show further information about
     * application.
     */
    private final AboutDialogFactory aboutDialogFactory;

    /**
     * Contains some basic application information.
     */
    private final AppInfo appInfo;
    /**
     * JavaFX host services.
     */
    private final HostServices hostServices;

    /**
     * Initializes all panes and variables.
     *
     * @throws ManagerException in case of construction error
     */
    public Core() throws ManagerException {
        appInfo = new AppInfo();
        hostServices = getHostServices();
        clientsList = new ClientsHolder();
        connectionDialogFactory = new ConnectionDialogFactory();
        loadingScreen = new LoadingScreen();
        alertDialogFactory = new AlertDialogFactory();
        clientDetailPaneHolder = new ClientDetailPaneHolder(clientsList,
                loadingScreen);
        aboutDialogFactory = new AboutDialogFactory(appInfo, hostServices);

        initMenu();
        initLeftPane();
        initDescriptionPane();
    }

    /**
     * Action binded with new connection buttons. Tries to connect to remote
     * client and if successful then add him into internal structures.
     */
    private void newConnectionButtonAction() {
        Dialog<Pair<String, String>> dialog
                = connectionDialogFactory.createLoginDialog();
        Optional<Pair<String, String>> result = dialog.showAndWait();

        Task<ClientWrapper> task = new Task<ClientWrapper>() {
            @Override
            protected ClientWrapper call() throws Exception {
                if (result.isPresent()) {
                    Pair<String, String> value = result.get();
                    ClientAddress addr = new ClientAddress(value.getKey(),
                            value.getValue());
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
                alertDialogFactory.createErrorDialog(
                        task.getException().getMessage()).show();
            }
        });

        new Thread(task).start();
    }

    /**
     * Constructs menu bar located at the top of the page.
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
     * Constructs left area containing list of active connections.
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
     * Constructs description area located at the bottom of overall layout.
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
        descPane.setPadding(new Insets(STANDARD_PADDING, 0,
                STANDARD_PADDING, 0));
        descPane.setStyle("-fx-border-width: 2 0 0 0;"
                + "-fx-border-color: grey;"
                + "-fx-border-style: dotted;");
        descPane.getChildren().addAll(description);

        descriptionPane = descPane;
    }

    /**
     * Starts JavaFX application.
     *
     * @param stage primary stage
     */
    @Override
    public final void start(Stage stage) {
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
        stage.setMaximized(true);
        stage.show();
    }

    /**
     * Entry point of application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
