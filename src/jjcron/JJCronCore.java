package jjcron;

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
 *
 * @author Martin
 */
public class JJCronCore {

    private static final Logger logger = Logger.getLogger(JJCronCore.class.getName());

    private final String[] args;
    private String crontabFilename = "crontab.txt";
    private final TaskManager taskManager;

    public JJCronCore(String[] args) {
        taskManager = new TaskManager(new TaskFactoryImpl());
        this.args = args;
        parseArguments();
    }

    public final void run() throws Exception {
        List<TaskMetadata> tasks = CrontabParser.parseFile(crontabFilename);
        taskManager.startCroning(tasks);
        taskManager.justWait();
    }

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
