package cz.polankam.jjcron.remote.manager;

/**
 *
 * @author Neloop
 */
public class AppInfo {

    /**
     *
     */
    public final String title;
    /**
     *
     */
    public final String license;
    /**
     *
     */
    public final String version;
    /**
     *
     */
    public final String sourceLinkText;
    /**
     *
     */
    public final String sourceLink;

    /**
     *
     */
    public AppInfo() {
        title = "JJCronRM";
        license = "MIT";
        version = getClass().getPackage().getImplementationVersion();
        sourceLinkText = "github.com/JJCron";
        sourceLink = "https://github.com/JJCron";
    }
}
