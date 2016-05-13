package jjcron.polankam.ms.mff.cuni.cz;

/**
 * Example task which can be loaded through reflection.
 * It does nothing usefull, just print dot on the standard output.
 * @author Neloop
 */
public class PrintDotTask extends Task {

    /**
     * Construct this task using {@link TaskMetadata} information.
     * @param taskMetadata handed over to {@link Task} parent
     */
    public PrintDotTask(TaskMetadata taskMetadata) {
        super(taskMetadata);
    }

    /**
     * Prints dot on the standard output and then flush it.
     * @throws Exception if task failed (should never happen)
     */
    @Override
    public void run() throws Exception {
        System.out.print(".");
        System.out.flush();
    }

}
