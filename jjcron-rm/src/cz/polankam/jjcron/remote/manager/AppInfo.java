package cz.polankam.jjcron.remote.manager;

/**
 * Stores some basic information about application. Has to be distributed
 * amongst classes which needs this info.
 *
 * @author Neloop
 */
public class AppInfo {

    /**
     * Title of UI application.
     */
    public final String title;
    /**
     * License under which is this program released.
     */
    public final String license;
    /**
     * Version of whole application.
     */
    public final String version;
    /**
     * Text in short form which can be used as visible part of source link.
     */
    public final String sourceLinkText;
    /**
     * Link to source code of this application.
     */
    public final String sourceLink;
    /**
     * Comma separated contributors list.
     */
    public final String contributors;

    /**
     * Parameterless constructor which initializes all possible values.
     */
    public AppInfo() {
        title = "JJCronRM";
        license = "MIT";
        version = getClass().getPackage().getImplementationVersion();
        sourceLinkText = "github.com/JJCron";
        sourceLink = "https://github.com/JJCron";
        contributors = "Martin Polanka";
    }
}
