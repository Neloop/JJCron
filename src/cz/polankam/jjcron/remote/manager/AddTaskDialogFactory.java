package cz.polankam.jjcron.remote.manager;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * Factory which will create dialog used for adding task to remote client
 * instance. Form is designed to contain all information like crontab entry.
 * <p>
 * Using of official JavaFX Dialogs, JDK 8u40 needed.</p>
 *
 * @author Neloop
 */
public class AddTaskDialogFactory {

    /**
     * Hints selection none option.
     */
    private static final String HINTS_NONE = "None";
    /**
     * Hints selection every second option.
     */
    private static final String HINTS_EVERY_SECOND = "Every second";
    /**
     * Hints selection on midnight option.
     */
    private static final String HINTS_MIDNIGHT = "Midnight";

    /**
     * Special structure which is used as return value from dialog.
     */
    public class TaskInfo {

        /**
         * Text from second field from dialog.
         */
        public final String second;
        /**
         * Text from minute field from dialog.
         */
        public final String minute;
        /**
         * Text from hour field from dialog.
         */
        public final String hour;
        /**
         * Text from day of month field from dialog.
         */
        public final String dayOfMonth;
        /**
         * Text from month field from dialog.
         */
        public final String month;
        /**
         * Text from day of week field from dialog.
         */
        public final String dayOfWeek;
        /**
         * Text from command field from dialog.
         */
        public final String command;

        /**
         * Constructor which takes all possible information from dialog.
         *
         * @param sec second crontab value
         * @param min minute crontab value
         * @param hour hour crontab value
         * @param dayOfMonth day of month crontab value
         * @param month month crontab value
         * @param dayOfWeek day of week crontab value
         * @param cmd runnable crontab command
         */
        public TaskInfo(String sec, String min, String hour, String dayOfMonth,
                String month, String dayOfWeek, String cmd) {
            this.second = sec;
            this.minute = min;
            this.hour = hour;
            this.dayOfMonth = dayOfMonth;
            this.month = month;
            this.dayOfWeek = dayOfWeek;
            this.command = cmd;
        }
    }

    /**
     * Creates and return ComboBox which contains some hints for crontab time
     * units. Units TextFields has to be provided for proper function.
     *
     * @param secondText seconds field
     * @param minuteText minutes field
     * @param hourText hours field
     * @param dayOfMonthText day of months field
     * @param monthText months field
     * @param dayOfWeekText day of week field
     * @return created selection combo box
     */
    private ComboBox createHintsComboBox(TextField secondText,
            TextField minuteText, TextField hourText, TextField dayOfMonthText,
            TextField monthText, TextField dayOfWeekText) {

        ObservableList<String> hints = FXCollections.observableArrayList(
                HINTS_NONE, HINTS_EVERY_SECOND, HINTS_MIDNIGHT
        );
        ComboBox comboBox = new ComboBox(hints);
        comboBox.getSelectionModel().selectFirst();
        comboBox.setMaxWidth(Double.MAX_VALUE);

        // set hints actions
        comboBox.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    if (HINTS_NONE.equals(newValue)) {
                        secondText.clear();
                        minuteText.clear();
                        hourText.clear();
                        dayOfMonthText.clear();
                        monthText.clear();
                        dayOfWeekText.clear();
                    } else if (HINTS_EVERY_SECOND.equals(newValue)) {
                        secondText.setText("*");
                        minuteText.setText("*");
                        hourText.setText("*");
                        dayOfMonthText.setText("*");
                        monthText.setText("*");
                        dayOfWeekText.setText("*");
                    } else if (HINTS_MIDNIGHT.equals(newValue)) {
                        secondText.setText("0");
                        minuteText.setText("0");
                        hourText.setText("0");
                        dayOfMonthText.setText("*");
                        monthText.setText("*");
                        dayOfWeekText.setText("*");
                    }
                }
        );

        return comboBox;
    }

    /**
     * Creates and returns dialog which prompts user for fill in crontab entry.
     *
     * @return created add task dialog
     */
    public Dialog<TaskInfo> createAddTaskDialog() {
        Dialog<TaskInfo> dialog = new Dialog<>();
        dialog.setTitle("Add Task Dialog");
        dialog.setHeaderText("Please fill new cron task information");

        ButtonType addButtonType = new ButtonType("Add", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType,
                ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        // construct all needed labels
        Label hintLabel = new Label("Hints:");
        Label secondLabel = new Label("Sec:");
        Label minuteLabel = new Label("Min:");
        Label hourLabel = new Label("Hour:");
        Label dayOfMonthLabel = new Label("Day of Month:");
        Label monthLabel = new Label("Month:");
        Label dayOfWeekLabel = new Label("Day of Week:");
        Label commandLabel = new Label("Command:");

        // align all labels to right
        GridPane.setHalignment(hintLabel, HPos.RIGHT);
        GridPane.setHalignment(secondLabel, HPos.RIGHT);
        GridPane.setHalignment(minuteLabel, HPos.RIGHT);
        GridPane.setHalignment(hourLabel, HPos.RIGHT);
        GridPane.setHalignment(dayOfMonthLabel, HPos.RIGHT);
        GridPane.setHalignment(monthLabel, HPos.RIGHT);
        GridPane.setHalignment(dayOfWeekLabel, HPos.RIGHT);
        GridPane.setHalignment(commandLabel, HPos.RIGHT);

        // construct all textfields
        TextField secondText = new TextField();
        secondText.setPromptText("sec");
        TextField minuteText = new TextField();
        minuteText.setPromptText("min");
        TextField hourText = new TextField();
        hourText.setPromptText("hour");
        TextField dayOfMonthText = new TextField();
        dayOfMonthText.setPromptText("day of month");
        TextField monthText = new TextField();
        monthText.setPromptText("month");
        TextField dayOfWeekText = new TextField();
        dayOfWeekText.setPromptText("day of week");
        TextField commandText = new TextField();
        commandText.setPromptText("command");

        ComboBox hintsComboBox = createHintsComboBox(secondText, minuteText,
                hourText, dayOfMonthText, monthText, dayOfWeekText);

        // and add all to grid pane
        grid.add(hintLabel, 0, 0);
        grid.add(hintsComboBox, 1, 0, 3, 1);
        grid.add(secondLabel, 0, 2);
        grid.add(secondText, 1, 2);
        grid.add(minuteLabel, 2, 2);
        grid.add(minuteText, 3, 2);
        grid.add(hourLabel, 0, 3);
        grid.add(hourText, 1, 3);
        grid.add(dayOfMonthLabel, 2, 3);
        grid.add(dayOfMonthText, 3, 3);
        grid.add(monthLabel, 0, 4);
        grid.add(monthText, 1, 4);
        grid.add(dayOfWeekLabel, 2, 4);
        grid.add(dayOfWeekText, 3, 4);
        grid.add(commandLabel, 0, 6);
        grid.add(commandText, 1, 6, 3, 1);

        // Login button is disabled by default
        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);

        // Invalidate login button in case of empty strings in both fields
        addButton.disableProperty().bind(
                Bindings.isEmpty(secondText.textProperty())
                .or(Bindings.isEmpty(minuteText.textProperty()))
                .or(Bindings.isEmpty(hourText.textProperty()))
                .or(Bindings.isEmpty(dayOfMonthText.textProperty()))
                .or(Bindings.isEmpty(monthText.textProperty()))
                .or(Bindings.isEmpty(dayOfWeekText.textProperty()))
                .or(Bindings.isEmpty(commandText.textProperty())));

        dialog.getDialogPane().setContent(grid);

        // Request focus on the registryAddress field by default
        Platform.runLater(() -> secondText.requestFocus());

        // Convert results to pair of server address and client identification
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {

                TaskInfo result = new TaskInfo(secondText.getText(),
                        minuteText.getText(), hourText.getText(),
                        dayOfMonthText.getText(), monthText.getText(),
                        dayOfWeekText.getText(), commandText.getText());
                return result;
            }
            return null;
        });

        return dialog;
    }
}
