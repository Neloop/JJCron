package cz.polankam.jjcron.remote.manager;

import cz.polankam.jjcron.remote.Client;
import java.rmi.Naming;

/**
 * Connects and disconnects clients using RMI technology. Disconnect is in case
 * of RMI not needed and this call is empty.
 *
 * @author Neloop
 */
public class RMIClientFactory implements ClientFactory {

    /**
     * Delimiter used between address parts.
     */
    private static final String DELIM = "/";

    @Override
    public Client connect(ClientAddress addr) throws Exception {
        Client client = (Client) Naming.lookup(addr.registryAddress + DELIM
                + addr.clientIdentification);
        return client;
    }

    @Override
    public void disconnect(Client client) throws Exception {
        // nothing to do here
    }
}
