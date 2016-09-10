package cz.cuni.mff.ms.polankam.jjcron.remote.manager;

import cz.cuni.mff.ms.polankam.jjcron.common.CrontabTime;
import cz.cuni.mff.ms.polankam.jjcron.remote.TaskDetail;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 *
 * @author Neloop
 */
public class TaskDetailPaneHolder {

    private static final double UNIT_TEXT_WIDTH = 70;

    /**
     *
     */
    private final GridPane rootPane;

    /**
     *
     */
    private final TextField idText;
    /**
     *
     */
    private final TextField nameText;
    /**
     *
     */
    private final TextField nextText;
    /**
     *
     */
    private final TextField timeUnitText;
    /**
     *
     */
    private final TextField lastExecutionText;
    /**
     *
     */
    private final TextField lastDurationText;

    /**
     *
     */
    private final GridPane metadataPane;
    /**
     *
     */
    private final TextField secondText;
    /**
     *
     */
    private final TextField minuteText;
    /**
     *
     */
    private final TextField hourText;
    /**
     *
     */
    private final TextField dayOfMonthText;
    /**
     *
     */
    private final TextField monthText;
    /**
     *
     */
    private final TextField dayOfWeekText;
    /**
     *
     */
    private final TextField commandText;

    /**
     *
     */
    public TaskDetailPaneHolder() {
        rootPane = new GridPane();
        idText = new TextField();
        nameText = new TextField();
        nextText = new TextField();
        timeUnitText = new TextField();
        lastExecutionText = new TextField();
        lastDurationText = new TextField();
        metadataPane = new GridPane();
        secondText = new TextField();
        minuteText = new TextField();
        hourText = new TextField();
        dayOfMonthText = new TextField();
        monthText = new TextField();
        dayOfWeekText = new TextField();
        commandText = new TextField();

        idText.setEditable(false);
        nameText.setEditable(false);
        nextText.setEditable(false);
        timeUnitText.setEditable(false);
        lastExecutionText.setEditable(false);
        lastDurationText.setEditable(false);

        rootPane.add(new Label("UUID:"), 0, 0);
        rootPane.add(idText, 1, 0, 3, 1);
        rootPane.add(new Label("Name:"), 0, 1);
        rootPane.add(nameText, 1, 1, 3, 1);
        rootPane.add(new Label("Next Execution:"), 0, 2);
        rootPane.add(nextText, 1, 2);
        rootPane.add(new Label("Time Unit:"), 2, 2);
        rootPane.add(timeUnitText, 3, 2);
        rootPane.add(new Label("Last Execution:"), 0, 3);
        rootPane.add(lastExecutionText, 1, 3);
        rootPane.add(new Label("Last Duration:"), 2, 3);
        rootPane.add(lastDurationText, 3, 3);
        rootPane.add(metadataPane, 4, 0, 1, 4);

        secondText.setMaxWidth(UNIT_TEXT_WIDTH);
        minuteText.setMaxWidth(UNIT_TEXT_WIDTH);
        hourText.setMaxWidth(UNIT_TEXT_WIDTH);
        dayOfMonthText.setMaxWidth(UNIT_TEXT_WIDTH);
        monthText.setMaxWidth(UNIT_TEXT_WIDTH);
        dayOfWeekText.setMaxWidth(UNIT_TEXT_WIDTH);

        rootPane.setHgap(5);
        rootPane.setVgap(5);
        metadataPane.setHgap(5);
        metadataPane.setVgap(5);
        metadataPane.setPadding(new Insets(0, 0, 0, 10));
    }

    /**
     *
     * @return
     */
    public Pane getRootPane() {
        return rootPane;
    }

    /**
     *
     * @param task
     */
    public void switchToTaskDetail(TaskDetail task) {
        if (task == null) {
            return;
        }

        idText.setText(task.id);
        nameText.setText(task.name);
        nextText.setText(task.nextExecutionTime.toString());
        timeUnitText.setText(task.timeUnit.toString());
        if (task.stats.getLastExecution() != null) {
            lastExecutionText.setText(task.stats.getLastExecution().toString());
        }
        if (task.stats.getLastDuration() != null) {
            lastDurationText.setText(task.stats.getLastDuration().toString());
        }

        metadataPane.getChildren().clear();
        if (task.metadata != null) {
            metadataPane.add(new Label("Sec:"), 0, 0);
            metadataPane.add(secondText, 1, 0);
            metadataPane.add(new Label("Min:"), 2, 0);
            metadataPane.add(minuteText, 3, 0);
            metadataPane.add(new Label("Hour:"), 4, 0);
            metadataPane.add(hourText, 5, 0);
            metadataPane.add(new Label("Day of Month:"), 0, 1);
            metadataPane.add(dayOfMonthText, 1, 1);
            metadataPane.add(new Label("Month:"), 2, 1);
            metadataPane.add(monthText, 3, 1);
            metadataPane.add(new Label("Day of Week:"), 4, 1);
            metadataPane.add(dayOfWeekText, 5, 1);
            metadataPane.add(new Label("Command:"), 0, 2);
            metadataPane.add(commandText, 1, 2, 5, 1);

            CrontabTime time = task.metadata.time();
            secondText.setText(time.second.value().toString());
            minuteText.setText(time.minute.value().toString());
            hourText.setText(time.hour.value().toString());
            dayOfMonthText.setText(time.dayOfMonth.value().toString());
            monthText.setText(time.month.value().toString());
            dayOfWeekText.setText(time.dayOfWeek.value().toString());
            commandText.setText(task.metadata.command());
        }
    }

    /**
     * 
     */
    public void clearTaskDetail() {
        idText.clear();
        nameText.clear();
        nextText.clear();
        timeUnitText.clear();
        lastExecutionText.clear();
        lastDurationText.clear();
        metadataPane.getChildren().clear();
    }
}
