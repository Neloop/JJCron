package cz.cuni.mff.ms.polankam.jjcron.rm;

import cz.cuni.mff.ms.polankam.jjcron.common.TaskDetail;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

/**
 *
 * @author Neloop
 */
public class TaskListPaneHolder {

    private static final String REFRESH_BTN_TEXT = "Refresh";
    private static final String ADD_TASK_BTN_TEXT = "Add task";
    private static final String DELETE_TASK_BTN_TEXT = "Delete task";

    private static final double TASK_ACTION_BTN_WIDTH = 120;

    private static final Logger logger = Logger.getLogger(Core.class.getName());

    private HBox rootPane;
    private VBox contentPane;
    private ListView<String> tasksListView;

    private final LoadingScreen loadingScreen;
    private final AddTaskDialogFactory addTaskDialogFactory;
    private final AlertDialogFactory alertDialogFactory;
    private ClientHolder activeClient;

    public TaskListPaneHolder(LoadingScreen loadingScreen){
        this.loadingScreen = loadingScreen;
        addTaskDialogFactory = new AddTaskDialogFactory();
        alertDialogFactory = new AlertDialogFactory();
        initContentPane();
        initRootPane();
    }

    public Pane getRootPane() {
        return rootPane;
    }

    public void displayTaskList(ClientHolder client) {
        activeClient = client;

        tasksListView.setItems(client.tasksObservableList);
        tasksListView.getSelectionModel().selectFirst();
        rootPane.getChildren().clear();
        rootPane.getChildren().add(contentPane);
    }

    public void clearTaskList() {
        activeClient = null;
        rootPane.getChildren().clear();
    }

    private void initRootPane() {
        rootPane = new HBox();
        rootPane.setPadding(new Insets(10, 0, 0, 0));
        rootPane.setStyle("-fx-border-width: 1 0 0 0;" +
                "-fx-border-color: grey;" +
                "-fx-border-style: solid;");
    }

    private void populateTaskDetailArea(GridPane detailArea) {
        detailArea.getChildren().add(new Label("TaskDetail"));
    }

    private void populateTaskListButtonsArea(VBox buttonsArea) {
        buttonsArea.setSpacing(5);
        buttonsArea.setAlignment(Pos.BOTTOM_RIGHT);
        HBox.setHgrow(buttonsArea, Priority.ALWAYS);

        Button refreshButton = new Button(REFRESH_BTN_TEXT);
        refreshButton.setMinWidth(TASK_ACTION_BTN_WIDTH);
        refreshButton.setOnAction((ActionEvent event) -> {
            refreshButtonAction();
        });

        Button addButton = new Button(ADD_TASK_BTN_TEXT);
        addButton.setMinWidth(TASK_ACTION_BTN_WIDTH);
        addButton.setOnAction((ActionEvent event) -> {
            addTaskButtonAction();
        });

        Button deleteButton = new Button(DELETE_TASK_BTN_TEXT);
        deleteButton.setMinWidth(TASK_ACTION_BTN_WIDTH);
        deleteButton.setOnAction((ActionEvent event) -> {
            deleteTaskButtonAction(tasksListView.getSelectionModel().getSelectedItem());
        });

        buttonsArea.getChildren().addAll(refreshButton, addButton, deleteButton);
    }

    private void initContentPane() {
        contentPane = new VBox();
        GridPane taskDetail = new GridPane();
        HBox taskDetailAndButtonsArea = new HBox();
        VBox taskListButtons = new VBox();

        tasksListView = new ListView<>();
        tasksListView.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    switchToTaskDetailAction(newValue);
                }
        );

        populateTaskDetailArea(taskDetail);
        populateTaskListButtonsArea(taskListButtons);

        taskDetailAndButtonsArea.setSpacing(10);
        taskDetailAndButtonsArea.getChildren().addAll(taskDetail, taskListButtons);

        VBox.setVgrow(tasksListView, Priority.ALWAYS);
        HBox.setHgrow(contentPane, Priority.ALWAYS);
        contentPane.setSpacing(10);
        contentPane.getChildren().addAll(tasksListView, taskDetailAndButtonsArea);
    }

    private void refreshButtonAction() {
        if (activeClient == null) {
            return;
        }

        // we have active client!!! so refresh tasks list
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                activeClient.refreshTasks();
                return null;
            }
        };

        task.setOnRunning((event) -> { loadingScreen.show(); });
        task.setOnSucceeded((event) -> {
            activeClient.fillTaskObservableList();
            tasksListView.getSelectionModel().selectFirst();
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

    private void addTaskButtonAction() {
        Dialog<Pair<String, String>> dialog = addTaskDialogFactory.createAddTaskDialog();
        Optional<Pair<String, String>> result = dialog.showAndWait();

        // user gives us information needed for task creation so use them
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                if (result.isPresent()) {
                    activeClient.addTask(new TaskDetail("hehe"));
                }
                activeClient.refreshTasks();
                return null;
            }
        };

        task.setOnRunning((event) -> { loadingScreen.show(); });
        task.setOnSucceeded((event) -> {
            activeClient.fillTaskObservableList();
            tasksListView.getSelectionModel().selectFirst();
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

    private void deleteTaskButtonAction(String taskId) {
        if (activeClient == null) {
            return;
        }

        // we have active client!!! so delete task
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                activeClient.deleteTask(taskId);
                return null;
            }
        };

        task.setOnRunning((event) -> { loadingScreen.show(); });
        task.setOnSucceeded((event) -> {
            activeClient.fillTaskObservableList();
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

    private void switchToTaskDetailAction(String taskId) {
        TaskDetail task = activeClient.getTaskDetail(taskId);
        if (task == null) {
            return;
        }

        // TODO: fill data
    }
}
