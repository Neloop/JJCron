package jjcron.polankam.ms.mff.cuni.cz;

import java.util.List;
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
public class JJCronCore {

    /**
     * Standard Java logger.
     */
    private static final Logger logger = Logger.getLogger(JJCronCore.class.getName());

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
    private final TaskManager taskManager;

    /**
     * Only possible constructor with given command line arguments.
     * Arguments are parsed directly in this constructor.
     * @param args command line arguments
     */
    public JJCronCore(String[] args) {
        taskManager = new TaskManager(new TaskFactoryImpl());
        this.args = args;
        parseArguments();
    }

    /**
     * Runs all the fun and blocks until task scheduling is not interrupted.
     * @throws jjcron.polankam.ms.mff.cuni.cz.TaskException
     * if there are problems concerning task scheduling
     * @throws jjcron.polankam.ms.mff.cuni.cz.ParserException
     * if there were problem with parsing crontab
     */
    public final void run() throws TaskException, ParserException {
        List<TaskMetadata> tasks = CrontabParser.parseFile(crontabFilename);
        taskManager.startCroning(tasks);
        taskManager.justWait();
    }

    /**
     * Parse command line arguments into internal variables.
     * Program can be exitted inside this method.
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
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            JJCronCore core = new JJCronCore(args);
            core.run();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
