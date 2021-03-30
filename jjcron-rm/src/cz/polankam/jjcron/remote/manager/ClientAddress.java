package cz.polankam.jjcron.remote.manager;

/**
 * Represents RMI client address and holds every possible detail which is needed
 * for proper connection to it.
 *
 * @author Neloop
 */
public class ClientAddress {

    /**
     * Delimiter which is used between address details.
     */
    private static final String ID_DELIMITER = "/";

    /**
     * Address of RMI registry, should contain port as well if needed.
     */
    public final String registryAddress;
    /**
     * Identification of {@link cz.polankam.jjcron.remote.Client} object in RMI
     * registry.
     */
    public final String clientIdentification;

    /**
     * Constructor with all needed information about RMI connection.
     *
     * @param addr address of RMI registry
     * @param id identification of {@link cz.polankam.jjcron.remote.Client}
     * object
     */
    public ClientAddress(String addr, String id) {
        registryAddress = addr;
        clientIdentification = id;
    }

    /**
     * Gets human readable representation of client address. Can be used as
     * unique identifier of connection.
     *
     * @return textual representation of address
     */
    @Override
    public String toString() {
        return registryAddress + ID_DELIMITER + clientIdentification;
    }
}
