package cz.polankam.jjcron.remote.manager;

import cz.polankam.jjcron.common.CrontabTime;
import cz.polankam.jjcron.common.TaskMetadata;
import cz.polankam.jjcron.remote.TaskDetail;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Holder for all necesary parts located in task list and task detail area. This
 * area contains list of tasks of active client and detail of selected task. To
 * be visible task list has to be opened, on closure task list with all info is
 * hidden.
 *
 * @author Neloop
 */
public class TaskListPaneHolder {

    /**
     * Refresh button text located in tasks list buttons area.
     */
    private static final String REFRESH_BTN_TEXT = "Refresh";
    /**
     * Add task button text in buttons area.
     */
    private static final String ADD_TASK_BTN_TEXT = "Add task";
    /**
     * Delete task button text.
     */
    private static final String DELETE_TASK_BTN_TEXT = "Delete task";
    /**
     * Text on reload crontab button.
     */
    private static final String RELOAD_CRONTAB_BTN_TEXT = "Reload Crontab";
    /**
     * Description of save to crontab button.
     */
    private static final String SAVE_TASKS_BTN_TEXT = "Save to Crontab";

    /**
     * Recommended width of all tasks list action buttons.
     */
    private static final double TASK_ACTION_BTN_WIDTH = 130;
    /**
     * Standard padding in whole application.
     */
    private static final int STANDARD_PADDING = 10;

    /**
     * Standard Java logger.
     */
    private static final Logger logger = Logger.getLogger(Core.class.getName());

    /**
     * Root pane which contains whole tasks list and task detail area and its
     * elements.
     */
    private HBox rootPane;
    /**
     * Only children of root pane which can be dynamically added or deleted in
     * case of tasks list closure.
     */
    private VBox contentPane;
    /**
     * List view which contains tasks from active client.
     */
    private ListView<TaskDetail> tasksListView;

    /**
     * Task detail area has its separate holder.
     */
    private final TaskDetailPaneHolder detailHolder;
    /**
     * Loading screen which can be used anywhere in application.
     */
    private final LoadingScreen loadingScreen;
    /**
     * Factory for add task dialog windows popup.
     */
    private final AddTaskDialogFactory addTaskDialogFactory;
    /**
     * Factory which creates various numbers of alert dialogs.
     */
    private final AlertDialogFactory alertDialogFactory;
    /**
     * Wrapper of selected active client.
     */
    private ClientWrapper activeClient;

    /**
     * Constructs tasks list pane holder with all appropriate datas. Given
     * references cannot be null.
     *
     * @param loadingScreen loading screen holder
     * @throws ManagerException in case of null parameter
     */
    public TaskListPaneHolder(LoadingScreen loadingScreen) throws ManagerException {
        if (loadingScreen == null) {
            throw new ManagerException("LoadingScreen cannot be null");
        }

        this.loadingScreen = loadingScreen;
        addTaskDialogFactory = new AddTaskDialogFactory();
        alertDialogFactory = new AlertDialogFactory();
        detailHolder = new TaskDetailPaneHolder();
        initContentPane();
        initRootPane();
    }

    /**
     * Gets root pane which should contain whole tasks list and task detail area
     * and its components.
     *
     * @return reference to root pane
     */
    public Pane getRootPane() {
        return rootPane;
    }

    /**
     * Displays tasks list and task details from given client.
     *
     * @param client active client, if null nothing will happen
     */
    public void displayTaskList(ClientWrapper client) {
        if (client == null) {
            return;
        }

        activeClient = client;
        tasksListView.setItems(client.tasksObservableList);
        tasksListView.getSelectionModel().selectFirst();
        rootPane.getChildren().clear();
        rootPane.getChildren().add(contentPane);
    }

    /**
     * Tells that tasks list was closed and whole area has to be cleared.
     */
    public void clearTaskList() {
        activeClient = null;
        rootPane.getChildren().clear();
    }

    /**
     * Constructs root pane with some default settings.
     */
    private void initRootPane() {
        rootPane = new HBox();
        rootPane.setPadding(new Insets(STANDARD_PADDING, 0, 0, 0));
        rootPane.setStyle("-fx-border-width: 1 0 0 0;"
                + "-fx-border-color: grey;"
                + "-fx-border-style: solid;");
    }

    /**
     * Populate given tasks list actions area with appropriate buttons.
     *
     * @param buttonsArea area which will be populated
     */
    private void populateTaskListButtonsArea(VBox buttonsArea) {
        buttonsArea.setSpacing(5);
        buttonsArea.setAlignment(Pos.BOTTOM_RIGHT);

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
            deleteTaskButtonAction(
                    tasksListView.getSelectionModel().getSelectedItem());
        });

        Button reloadButton = new Button(RELOAD_CRONTAB_BTN_TEXT);
        reloadButton.setMinWidth(TASK_ACTION_BTN_WIDTH);
        reloadButton.setOnAction((ActionEvent event) -> {
            reloadCrontabButtonAction();
        });

        Button saveButton = new Button(SAVE_TASKS_BTN_TEXT);
        saveButton.setMinWidth(TASK_ACTION_BTN_WIDTH);
        saveButton.setOnAction((ActionEvent event) -> {
            saveToCrontabButtonAction();
        });

        buttonsArea.getChildren().addAll(refreshButton, addButton, deleteButton,
                reloadButton, saveButton);
    }

    /**
     * Constructs and populate content pane which can be plugged/unplugged from
     * root pane.
     */
    private void initContentPane() {
        contentPane = new VBox();
        HBox taskDetailAndButtonsArea = new HBox();
        VBox taskListButtons = new VBox();

        tasksListView = new ListView<>();
        tasksListView.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    switchToTaskDetailAction(newValue);
                }
        );
        tasksListView.setCellFactory(param -> new ListCell<TaskDetail>() {
            @Override
            protected void updateItem(TaskDetail item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.name);
                }
            }
        });

        populateTaskListButtonsArea(taskListButtons);

        taskDetailAndButtonsArea.setSpacing(10);
        taskDetailAndButtonsArea.getChildren().addAll(
                detailHolder.getRootPane(), taskListButtons);

        VBox.setVgrow(tasksListView, Priority.ALWAYS);
        HBox.setHgrow(contentPane, Priority.ALWAYS);
        HBox.setHgrow(detailHolder.getRootPane(), Priority.ALWAYS);
        contentPane.setSpacing(10);
        contentPane.getChildren().addAll(tasksListView,
                taskDetailAndButtonsArea);
    }

    /**
     * Action triggered after clicking on refresh button.
     */
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

        task.setOnRunning((event) -> {
            loadingScreen.show();
        });
        task.setOnSucceeded((event) -> {
            activeClient.fillTaskObservableList();
            tasksListView.getSelectionModel().selectFirst();
            loadingScreen.hide();
            if (activeClient.tasksObservableList.isEmpty()) {
                detailHolder.clearTaskDetail();
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
     * Performed after user clicks on add task button.
     */
    private void addTaskButtonAction() {
        if (activeClient == null) {
            return;
        }

        Dialog<AddTaskDialogFactory.TaskInfo> dialog
                = addTaskDialogFactory.createAddTaskDialog();
        Optional<AddTaskDialogFactory.TaskInfo> result = dialog.showAndWait();

        // user gives us information needed for task creation so use them
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                if (result.isPresent()) {
                    AddTaskDialogFactory.TaskInfo info = result.get();
                    CrontabTime time = new CrontabTime(info.second, info.minute,
                            info.hour, info.dayOfMonth, info.month,
                            info.dayOfWeek);
                    TaskMetadata meta = new TaskMetadata(time, info.command);
                    activeClient.addTask(meta);
                }
                activeClient.refreshTasks();
                return null;
            }
        };

        task.setOnRunning((event) -> {
            loadingScreen.show("Adding ...");
        });
        task.setOnSucceeded((event) -> {
            activeClient.fillTaskObservableList();
            tasksListView.getSelectionModel().selectFirst();
            loadingScreen.hide();
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
     * Executed in case of click on delete task button.
     *
     * @param detail provides information about task which should be deleted
     */
    private void deleteTaskButtonAction(TaskDetail detail) {
        if (activeClient == null) {
            return;
        }

        // we have active client!!! so delete task
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                activeClient.deleteTask(detail.id);
                return null;
            }
        };

        task.setOnRunning((event) -> {
            loadingScreen.show("Removing  ...");
        });
        task.setOnSucceeded((event) -> {
            activeClient.fillTaskObservableList();
            tasksListView.getSelectionModel().selectFirst();
            loadingScreen.hide();
            if (activeClient.tasksObservableList.isEmpty()) {
                detailHolder.clearTaskDetail();
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
     * Action launched after clicking on save to crontab button.
     */
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

        task.setOnRunning((event) -> {
            loadingScreen.show("Saving ...");
        });
        task.setOnSucceeded((event) -> {
            loadingScreen.hide();
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
     * After clicking on reload button, then it tells client that tasks from
     * crontab has to be reloaded.
     */
    private void reloadCrontabButtonAction() {
        if (activeClient == null) {
            return;
        }

        // we have active client!!! so reload crontab
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                activeClient.reloadCrontab();
                return null;
            }
        };

        task.setOnRunning((event) -> {
            loadingScreen.show("Reloading ...");
        });
        task.setOnSucceeded((event) -> {
            activeClient.fillTaskObservableList();
            loadingScreen.hide();
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
     * Action performed if different task is selected in tasks list. Should
     * display further details about selected task.
     *
     * @param detail task details which should be displayed
     */
    private void switchToTaskDetailAction(TaskDetail detail) {
        if (activeClient == null) {
            return;
        }

        detailHolder.switchToTaskDetail(detail);
    }
}
