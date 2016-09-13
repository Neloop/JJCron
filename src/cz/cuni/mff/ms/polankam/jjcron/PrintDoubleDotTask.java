package cz.cuni.mff.ms.polankam.jjcron;

import cz.cuni.mff.ms.polankam.jjcron.common.TaskMetadata;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * Example task which can be loaded through reflection. It does nothing usefull,
 * just print double dot on the standard output.
 *
 * @author Neloop
 */
public class PrintDoubleDotTask extends Task {

    /**
     *
     * @param taskMeta
     */
    public PrintDoubleDotTask(TaskMetadata taskMeta) {
        super(taskMeta);
    }

    /**
     * Prints dot on the standard output and then flush it.
     *
     * @throws Exception if task failed (should never happen)
     */
    @Override
    public void run() throws Exception {
        System.out.print(":");
        System.out.flush();
    }

    @Override
    public String name() {
        return "<class>PrintDoubleDotTask</class>";
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
