package cz.cuni.mff.ms.polankam.jjcron;

import cz.cuni.mff.ms.polankam.jjcron.common.TaskMetadata;
import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class of JJCron and also entry point with main method. It constructs and
 * runs all things necessary to execute cron functionality.
 *
 * @author Neloop
 */
public class Core {

    /**
     * Standard Java logger.
     */
    private static final Logger logger
            = Logger.getLogger(Core.class.getName());

    /**
     * Command line options separate handler.
     */
    private final CmdOptions options;

    /**
     * Component which does loading of tasks, their scheduling and running.
     */
    private final TaskScheduler taskScheduler;

    /**
     * Server side implementation of this instance manager.
     */
    private ClientImpl clientManager;

    /**
     * Only possible constructor with given command line arguments. Arguments
     * are parsed directly in this constructor.
     *
     * @param args command line arguments
     * @throws TaskException if {@link TaskScheduler} creation failed
     */
    public Core(String[] args) throws TaskException {
        logger.log(Level.INFO, "*** JJCron was created ***");

        options = new CmdOptions(args);
        clientManager = null;
        taskScheduler = new TaskScheduler(new TaskFactoryImpl());
    }

    /**
     * Runs all the fun and blocks until task scheduling is not interrupted.
     *
     * @throws TaskException if there are problems concerning task scheduling
     * @throws ParserException if there were problem with parsing crontab
     * @throws java.rmi.RemoteException
     */
    public final void run() throws TaskException, ParserException, RemoteException {
        logger.log(Level.INFO, "*** Croning started ***");

        logger.log(Level.FINE, "Loading tasks from crontab: {0}",
                options.crontab);
        List<TaskMetadata> tasks = CrontabParser.loadFile(options.crontab);
        taskScheduler.start(tasks);
        runClientIfStated();
        taskScheduler.justWait();
    }

    /**
     * If user requested, server management will be engaged on this JJCron
     * instance.
     *
     * @throws RemoteException
     */
    private void runClientIfStated() throws RemoteException {
        if (options.rmName.isEmpty()) {
            return;
        }

        logger.log(Level.INFO, "Remote management of JJCron requested, RMI will be initialized");
        clientManager = new ClientImpl(options, taskScheduler);
        logger.log(Level.INFO, "RMI registry successfully created, listening...");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Core core = new Core(args);
            core.run();
        } catch (TaskException | ParserException | RemoteException e) {
            System.err.println(e.getMessage());
        }
    }
}
