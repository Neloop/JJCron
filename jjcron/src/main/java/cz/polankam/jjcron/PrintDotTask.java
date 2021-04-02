package cz.polankam.jjcron;

import cz.polankam.jjcron.common.CrontabTime;
import cz.polankam.jjcron.common.FormatException;
import cz.polankam.jjcron.common.TaskMetadata;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * Example task which can be loaded through reflection. It does nothing usefull,
 * just print dot on the standard output.
 *
 * @author Neloop
 */
public class PrintDotTask extends Task {

    /**
     * Default parameterless constructor which will make up some
     * {@link TaskMetadata} structure needed for construction of {@link Task}.
     *
     * @throws FormatException in case of format error (should never happen)
     * @throws TaskException if task metadata was null (should never happen)
     */
    public PrintDotTask() throws FormatException, TaskException {
        super(new TaskMetadata(new CrontabTime("*", "*", "*", "*", "*", "*"),
                "<class>cz.polankam.jjcron.PrintDotTask</class>"));
    }

    /**
     * Constructor which needs {@link TaskMetadata} structure for this class
     * parent.
     *
     * @param taskMeta information about this task
     * @throws TaskException if task metadata was null
     */
    public PrintDotTask(TaskMetadata taskMeta) throws TaskException {
        super(taskMeta);
    }

    /**
     * Prints dot on the standard output and then flush it.
     *
     * @throws Exception if task failed (should never happen here)
     */
    @Override
    public void run() throws Exception {
        System.out.print(".");
        System.out.flush();
    }

    @Override
    public String name() {
        return "<class>PrintDotTask</class>";
    }

    @Override
    public long delay(LocalDateTime localNow) {
        return 1;
    }

    @Override
    public TimeUnit timeUnit() {
        return TimeUnit.SECONDS;
    }

}
