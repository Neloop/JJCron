package cz.cuni.mff.ms.polankam.jjcron.rm;

/**
 *
 * @author Neloop
 */
public class ConnectionDetail {
    private final String registryAddress;
    private final String clientIdentification;

    public ConnectionDetail(String addr, String id) {
        registryAddress = addr;
        clientIdentification = id;

        // TODO: actual connection should be placed here
    }
}
