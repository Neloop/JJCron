package cz.cuni.mff.ms.polankam.jjcron.rm;

/**
 *
 * @author Neloop
 */
public class ClientAddress {
    public final String registryAddress;
    public final String clientIdentification;

    public ClientAddress(String addr, String id) {
        registryAddress = addr;
        clientIdentification = id;
    }

    @Override
    public String toString(){
        return registryAddress + "/" + clientIdentification;
    }
}
