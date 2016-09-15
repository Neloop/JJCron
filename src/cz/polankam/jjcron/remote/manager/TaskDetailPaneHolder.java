package cz.polankam.jjcron.remote.manager;

import cz.polankam.jjcron.common.CrontabTime;
import cz.polankam.jjcron.remote.TaskDetail;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * Holder for all parts located in task detail area, this includes mainly text
 * fields with datas and their labels. Can be switched to selected/active task
 * or cleared.
 *
 * @author Neloop
 */
public class TaskDetailPaneHolder {

    /**
     * Width of crontab units text fields.
     */
    private static final double UNIT_TEXT_WIDTH = 70;
    /**
     * Extended padding used in application.
     */
    private static final int EXTENDED_PADDING = 20;

    /**
     * Root pane which contains all possible area components.
     */
    private final GridPane rootPane;

    /**
     * Field representing identification of task.
     */
    private final TextField idText;
    /**
     * Field which represents name of active task.
     */
    private final TextField nameText;
    /**
     * Represents next execution time point of active task.
     */
    private final TextField nextText;
    /**
     * Contains textual description of last execution time point.
     */
    private final TextField lastExecutionText;
    /**
     * Field containing last execution duration time.
     */
    private final TextField lastDurationText;

    /**
     * Area which contains metadata of task stated in crontab file.
     */
    private final GridPane metadataPane;
    /**
     * Field representing seconds value from crontab.
     */
    private final TextField secondText;
    /**
     * Minute field containing minutes value from crontab.
     */
    private final TextField minuteText;
    /**
     * Represents hours value from crontab.
     */
    private final TextField hourText;
    /**
     * Contains day of month value from crontab.
     */
    private final TextField dayOfMonthText;
    /**
     * Field representing month value from crontab.
     */
    private final TextField monthText;
    /**
     * Field which contains day of week value from crontab.
     */
    private final TextField dayOfWeekText;
    /**
     * Command text which was loaded from crontab file.
     */
    private final TextField commandText;

    /**
     * Formats time into standard year first date and hour first time.
     */
    private final DateTimeFormatter dateTimeFormatter
            = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Construct and initializes all possible text fields and labels.
     */
    public TaskDetailPaneHolder() {
        rootPane = new GridPane();
        idText = new TextField();
        nameText = new TextField();
        nextText = new TextField();
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
        lastExecutionText.setEditable(false);
        lastDurationText.setEditable(false);
        secondText.setEditable(false);
        minuteText.setEditable(false);
        hourText.setEditable(false);
        dayOfMonthText.setEditable(false);
        monthText.setEditable(false);
        dayOfWeekText.setEditable(false);
        commandText.setEditable(false);

        rootPane.add(new Label("UUID:"), 0, 0);
        rootPane.add(idText, 1, 0, 3, 1);
        rootPane.add(new Label("Name:"), 0, 1);
        rootPane.add(nameText, 1, 1, 3, 1);
        rootPane.add(new Label("Next Execution:"), 0, 2);
        rootPane.add(nextText, 1, 2, 3, 1);
        rootPane.add(new Label("Last Execution:"), 0, 3);
        rootPane.add(lastExecutionText, 1, 3);
        rootPane.add(new Label("Last Duration (ms):"), 2, 3);
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
        metadataPane.setPadding(new Insets(0, 0, 0, EXTENDED_PADDING));
    }

    /**
     * Gets root pane containing active task details.
     *
     * @return reference to root pane of task detail area
     */
    public Pane getRootPane() {
        return rootPane;
    }

    /**
     * Displays all possible information about given task.
     *
     * @param task task which should be displayed in appropriate area
     */
    public void switchToTaskDetail(TaskDetail task) {
        if (task == null) {
            return;
        }

        idText.setText(task.id);
        nameText.setText(task.name);
        nextText.setText(task.nextExecutionTime.format(dateTimeFormatter));
        if (task.stats.getLastExecution() != null) {
            lastExecutionText.setText(
                    task.stats.getLastExecution().format(dateTimeFormatter));
        }
        if (task.stats.getLastDuration() != null) {
            long duration = task.stats.getLastDuration();
            duration = TimeUnit.SECONDS.convert(duration,
                    TimeUnit.MILLISECONDS);
            lastDurationText.setText(String.valueOf(duration));
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
     * Clears task detail area, can be used when there is no active task or no
     * task whatsoever.
     */
    public void clearTaskDetail() {
        idText.clear();
        nameText.clear();
        nextText.clear();
        lastExecutionText.clear();
        lastDurationText.clear();
        metadataPane.getChildren().clear();
    }
}
