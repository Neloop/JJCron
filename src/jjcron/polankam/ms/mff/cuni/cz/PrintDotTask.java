package jjcron.polankam.ms.mff.cuni.cz;

/**
 *
 * @author Neloop
 */
public class PrintDotTask extends Task {

    public PrintDotTask(TaskMetadata taskMetadata) {
        super(taskMetadata);
    }

    @Override
    public void run() throws Exception {
        System.out.print(".");
        System.out.flush();
    }

}
