package jjcron;

import java.util.logging.Logger;

/**
 *
 * @author Martin
 */
public class CmdTask extends TaskBase {

    private static final Logger logger = Logger.getLogger(CmdTask.class.getName());

    public CmdTask(TaskMetadata taskMetadata) {
        super(taskMetadata);
    }

    @Override
    public void run() throws Exception {
        Process p = Runtime.getRuntime().exec(taskMeta.command());
        p.waitFor();
    }

}
