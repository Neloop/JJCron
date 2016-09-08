package cz.cuni.mff.ms.polankam.jjcron.remote.manager;

import cz.cuni.mff.ms.polankam.jjcron.common.TaskMetadata;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.util.Pair;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * Using of official JavaFX Dialogs... JDK 8u40 needed
 * @author Neloop
 */
public class AddTaskDialogFactory {
    public Dialog<TaskMetadata> createAddTaskDialog() {
        Dialog<TaskMetadata> dialog = new Dialog<>();
        dialog.setTitle("Add Task Dialog");
        dialog.setHeaderText("Please fill new cron task information");

        ButtonType addButtonType = new ButtonType("Add", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField secondText = new TextField();
        secondText.setPromptText("s");
        TextField minuteText = new TextField();
        minuteText.setPromptText("m");
        TextField hourText = new TextField();
        hourText.setPromptText("s");
        TextField dayOfMonthText = new TextField();
        dayOfMonthText.setPromptText("m");
        TextField monthText = new TextField();
        monthText.setPromptText("s");
        TextField dayOfWeekText = new TextField();
        dayOfWeekText.setPromptText("m");

        TextField commandText = new TextField();
        commandText.setPromptText("Command");

        grid.add(new Label("Sec:"), 0, 0);
        grid.add(secondText, 1, 0);
        grid.add(new Label("Min:"), 2, 0);
        grid.add(minuteText, 3, 0);
        grid.add(new Label("Hour:"), 0, 1);
        grid.add(hourText, 1, 1);
        grid.add(new Label("Day of Month:"), 2, 1);
        grid.add(dayOfMonthText, 3, 1);
        grid.add(new Label("Month:"), 0, 2);
        grid.add(monthText, 1, 2);
        grid.add(new Label("Day of Week:"), 2, 2);
        grid.add(dayOfWeekText, 3, 2);

        grid.add(new Label("Command:"), 0, 3);
        grid.add(commandText, 1, 3, 4, 1);

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
                //return new TaskMetadata();
            }
            return null;
        });

        return dialog;
    }
}
