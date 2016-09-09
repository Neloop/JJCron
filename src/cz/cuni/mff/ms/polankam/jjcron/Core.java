package cz.cuni.mff.ms.polankam.jjcron;

import cz.cuni.mff.ms.polankam.jjcron.common.TaskMetadata;
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
 * Main class of JJCron and also entry point with main method.
 * It constructs and runs all things necessary to execute cron functionality.
 * @author Neloop
 */
public class Core {

    /**
     * Standard Java logger.
     */
    private static final Logger logger =
            Logger.getLogger(Core.class.getName());

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
     * Only possible constructor with given command line arguments.
     * Arguments are parsed directly in this constructor.
     * @param args command line arguments
     * @throws TaskException if {@link TaskScheduler} creation failed
     */
    public Core(String[] args) throws TaskException {
        logger.log(Level.INFO, "*** JJCron was created ***");

        taskScheduler = new TaskScheduler(new TaskFactoryImpl());
        this.args = args;
        parseArguments();
    }

    /**
     * Runs all the fun and blocks until task scheduling is not interrupted.
     * @throws TaskException if there are problems concerning task scheduling
     * @throws ParserException if there were problem with parsing crontab
     */
    public final void run() throws TaskException, ParserException {
        logger.log(Level.INFO, "*** Croning started ***");

        List<TaskMetadata> tasks = CrontabParser.parseFile(crontabFilename);
        taskScheduler.startCroning(tasks);
        taskScheduler.justWait();
    }

    /**
     * Parse command line arguments into internal variables.
     * Program can be exitted inside this method.
     * Apache Commons CLI is used for parsing.
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

        // add options
        options.addOption(help);
        options.addOption(config);

        // create parser and parse arguments
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            // parse the command line arguments
            cmd = parser.parse(options, args);
        } catch(ParseException e) {
            // oops, something went wrong
            System.err.println("Parsing failed. Reason: " + e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("JJCron", options);
            System.exit(1);
        }

        if(cmd.hasOption("help")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("JJCron", options);
            System.exit(0);
        }

        if (cmd.hasOption("config")) {
            crontabFilename = cmd.getOptionValue("config");
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
        } catch (TaskException | ParserException e) {
            System.err.println(e.getMessage());
        }
    }
}
