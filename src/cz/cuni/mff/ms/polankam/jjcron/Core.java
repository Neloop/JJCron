package cz.cuni.mff.ms.polankam.jjcron;

import cz.cuni.mff.ms.polankam.jjcron.common.TaskMetadata;
import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;

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
     * Stored command line arguments.
     */
    private final String[] args;
    /**
     * Filename where crontab configuration should be searched for.
     */
    private String crontabFilename = "crontab.txt";
    /**
     * Component which does loading of tasks, their scheduling and running.
     */
    private final TaskScheduler taskScheduler;

    /**
     * Port where rmiregistry will listen.
     */
    private int rmPort = 1099;
    /**
     * Name of registered JJCron instance.
     *
     * @note If this name is stated as cmd option, then rmiregistry will be
     * created.
     */
    private String rmName;
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

        clientManager = null;
        taskScheduler = new TaskScheduler(new TaskFactoryImpl());
        this.args = args;
        parseArguments();
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

        List<TaskMetadata> tasks = CrontabParser.parseFile(crontabFilename);
        taskScheduler.startCroning(tasks);
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
        if (rmName.isEmpty()) {
            return;
        }

        logger.log(Level.INFO, "Remote management of JJCron requested, RMI will be initialized");
        clientManager = new ClientImpl(rmPort, rmName, taskScheduler);
        logger.log(Level.INFO, "RMI registry successfully created, listening...");
    }

    /**
     * Parse command line arguments into internal variables. Program can be
     * exitted inside this method. Apache Commons CLI is used for parsing.
     */
    private void parseArguments() {
        // create Options object
        Options options = new Options();

        // create options
        Option help = Option.builder("h")
                .longOpt("help")
                .desc("print this message")
                .build();
        Option config = Option.builder("c")
                .longOpt("config")
                .argName("file")
                .hasArg()
                .desc("crontab file configuration")
                .build();
        Option portOption = Option.builder("p")
                .longOpt("rm-port")
                .argName("port")
                .hasArg()
                .desc("specifies port of remote management, which is allowed only if rm-name is given")
                .build();
        Option nameOption = Option.builder("n")
                .longOpt("rm-name")
                .argName("name")
                .hasArg()
                .desc("remote management name of registered object, if stated remote management will be allowed")
                .build();

        // add options
        options.addOption(help);
        options.addOption(config);
        options.addOption(portOption);
        options.addOption(nameOption);

        // create parser and parse arguments
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            // parse the command line arguments
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            // oops, something went wrong
            System.err.println("Parsing failed. Reason: " + e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("JJCron", options);
            System.exit(1);
        }

        if (cmd.hasOption("help")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("JJCron", options);
            System.exit(0);
        }

        if (cmd.hasOption("config")) {
            crontabFilename = cmd.getOptionValue("config");
        }
        if (cmd.hasOption("rm-port")) {
            rmPort = Integer.parseInt(cmd.getOptionValue("rm-port"));
        }
        if (cmd.hasOption("rm-name")) {
            rmName = cmd.getOptionValue("rm-name");
        }

        logger.log(Level.FINE, "Crontab file which will be loaded: {0}",
                crontabFilename);
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
