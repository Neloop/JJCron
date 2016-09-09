package cz.cuni.mff.ms.polankam.jjcron.remote.manager;

import cz.cuni.mff.ms.polankam.jjcron.common.CrontabTime;
import cz.cuni.mff.ms.polankam.jjcron.common.TaskMetadata;
import cz.cuni.mff.ms.polankam.jjcron.remote.TaskDetail;
import java.util.List;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 * @author Neloop
 */
public class TaskListPaneHolder {

    private static final String REFRESH_BTN_TEXT = "Refresh";
    private static final String ADD_TASK_BTN_TEXT = "Add task";
    private static final String DELETE_TASK_BTN_TEXT = "Delete task";
    private static final String SAVE_TASKS_BTN_TEXT = "Save to Crontab";

    private static final double TASK_ACTION_BTN_WIDTH = 130;

    private static final Logger logger = Logger.getLogger(Core.class.getName());

    private HBox rootPane;
    private VBox contentPane;
    private ListView<String> tasksListView;

    private TextField taskDetailIdText;
    private TextField taskDetailNameText;

    private final LoadingScreen loadingScreen;
    private final AddTaskDialogFactory addTaskDialogFactory;
    private final AlertDialogFactory alertDialogFactory;
    private ClientWrapper activeClient;

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

    public void displayTaskList(ClientWrapper client) {
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
        taskDetailIdText = new TextField();
        taskDetailIdText.setEditable(false);
        taskDetailNameText = new TextField();
        taskDetailNameText.setEditable(false);
        taskDetailNameText.autosize();

        detailArea.add(new Label("UUID:"), 0, 0);
        detailArea.add(taskDetailIdText, 1, 0, 3, 1);
        detailArea.add(new Label("Name:"), 0, 1);
        detailArea.add(taskDetailNameText, 1, 1, 3, 1);

        detailArea.setHgap(5);
        detailArea.setVgap(5);
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

        Button saveButton = new Button(SAVE_TASKS_BTN_TEXT);
        saveButton.setMinWidth(TASK_ACTION_BTN_WIDTH);
        saveButton.setOnAction((ActionEvent event) -> {
            saveToCrontabButtonAction();
        });

        buttonsArea.getChildren().addAll(refreshButton, addButton, deleteButton, saveButton);
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
            if (activeClient.tasksObservableList.isEmpty()) {
                clearTaskDetailAction();
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

    private void addTaskButtonAction() {
        Dialog<List<String>> dialog = addTaskDialogFactory.createAddTaskDialog();
        Optional<List<String>> result = dialog.showAndWait();

        // user gives us information needed for task creation so use them
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                if (result.isPresent()) {
                    List<String> list = result.get();
                    CrontabTime time = new CrontabTime(list.get(0),
                            list.get(1), list.get(2), list.get(3),
                            list.get(4), list.get(5));
                    TaskMetadata meta = new TaskMetadata(time, list.get(6));
                    activeClient.addTask(meta);
                }
                activeClient.refreshTasks();
                return null;
            }
        };

        task.setOnRunning((event) -> { loadingScreen.show("Adding ..."); });
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

        task.setOnRunning((event) -> { loadingScreen.show("Removing  ..."); });
        task.setOnSucceeded((event) -> {
            activeClient.fillTaskObservableList();
            tasksListView.getSelectionModel().selectFirst();
            loadingScreen.hide();
            if (activeClient.tasksObservableList.isEmpty()) {
                clearTaskDetailAction();
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

    private void saveToCrontabButtonAction() {
        if (activeClient == null) {
            return;
        }

        // we have active client!!! so save changes to crontab
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                activeClient.saveToCrontab();
                return null;
            }
        };

        task.setOnRunning((event) -> { loadingScreen.show("Saving ..."); });
        task.setOnSucceeded((event) -> {
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
        if (activeClient == null) {
            return;
        }

        TaskDetail task = activeClient.getTaskDetail(taskId);
        if (task == null) {
            return;
        }

        taskDetailIdText.setText(task.id);
        taskDetailNameText.setText(task.name);
    }

    private void clearTaskDetailAction() {
        taskDetailIdText.clear();
        taskDetailNameText.clear();
    }
}
