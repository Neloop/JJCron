package cz.cuni.mff.ms.polankam.jjcron.remote.manager;

/**
 *
 * @author Neloop
 */
public class ClientAddress {

    private static final String ID_DELIMITER = "/";

    /**
     *
     */
    public final String registryAddress;
    /**
     *
     */
    public final String clientIdentification;

    /**
     *
     * @param addr
     * @param id
     */
    public ClientAddress(String addr, String id) {
        registryAddress = addr;
        clientIdentification = id;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return registryAddress + ID_DELIMITER + clientIdentification;
    }
}
