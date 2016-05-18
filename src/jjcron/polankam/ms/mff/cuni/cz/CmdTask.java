package jjcron.polankam.ms.mff.cuni.cz;

/**
 * Classical cron task which is used to execute cmdline commands.
 * @author Neloop
 */
public class CmdTask extends Task {

    /**
     * Construct task from given {@link TaskMetadata} information.
     * @param taskMetadata handed over to {@link Task} parent
     */
    public CmdTask(TaskMetadata taskMetadata) {
        super(taskMetadata);
    }

    /**
     * Create and execute cmdline command and than wait for it (blocking).
     * @throws Exception if command execution failed
     */
    @Override
    public void run() throws Exception {
        Process p = Runtime.getRuntime().exec(taskMeta.command());
        p.waitFor();
    }

}
