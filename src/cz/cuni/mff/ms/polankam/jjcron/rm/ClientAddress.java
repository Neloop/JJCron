package cz.cuni.mff.ms.polankam.jjcron.rm;

/**
 *
 * @author Neloop
 */
public class ClientAddress {

    private static final String ID_DELIMITER = "/";

    public final String registryAddress;
    public final String clientIdentification;

    public ClientAddress(String addr, String id) {
        registryAddress = addr;
        clientIdentification = id;
    }

    @Override
    public String toString(){
        return registryAddress + ID_DELIMITER + clientIdentification;
    }
}
