package jjcron;

/**
 *
 * @author Martin Polanka
 */
public class JJCron {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JJCronCore core = new JJCronCore(args);
        core.run();
    }

}
