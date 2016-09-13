package cz.cuni.mff.ms.polankam.jjcron;

import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author Neloop
 */
public class CmdOptions {

    /**
     * Standard Java logger.
     */
    private static final Logger logger
            = Logger.getLogger(CmdOptions.class.getName());

    /**
     * Stored command line arguments.
     */
    private final String[] args;
    /**
     * Commons CLI holder for all cmd options.
     */
    private final Options options;

    /**
     * Filename where crontab configuration should be searched for.
     */
    public String crontab = "crontab.txt";
    /**
     * Port where rmiregistry will listen.
     */
    public int rmPort = 1099;
    /**
     * Name of registered JJCron instance.
     *
     * <p>
     * If this name is stated as cmd option, then rmiregistry will be
     * created.</p>
     */
    public String rmName;

    public CmdOptions(String[] args) {
        this.args = args;
        options = new Options();

        init();
        parse();
    }

    /**
     * Initialize all possible command line options.
     */
    private void init() {
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
    }

    /**
     * Parse command line arguments into internal variables. Program can be
     * exitted inside this method. Apache Commons CLI is used for parsing.
     */
    private void parse() {
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
            crontab = cmd.getOptionValue("config");
        }
        if (cmd.hasOption("rm-port")) {
            rmPort = Integer.parseInt(cmd.getOptionValue("rm-port"));
        }
        if (cmd.hasOption("rm-name")) {
            rmName = cmd.getOptionValue("rm-name");
        }
    }
}
