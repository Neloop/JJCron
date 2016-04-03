package jjcron;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author Martin
 */
public class JJCronCore {

    private final String[] args;

    public JJCronCore(String[] args) {
        this.args = args;
        parseArguments();
    }

    public void run() {
    }

    private void parseArguments() {
        // create Options object
        Options options = new Options();

        // create options
        Option help = new Option("help", "print this message");
        Option config = OptionBuilder.withArgName("file")
                .hasArg()
                .withDescription("use given file for configuration")
                .create("config");


        // add options
        options.addOption(help);
        options.addOption(config);

        // create parser and parse arguments
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try {
            // parse the command line arguments
            cmd = parser.parse( options, args );
        } catch(ParseException e) {
            // oops, something went wrong
            System.err.println("Parsing failed. Reason: " + e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("JJCron", options);
            return;
        }

        if(cmd.hasOption("help")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("JJCron", options);
            return;
        }
    }
}
