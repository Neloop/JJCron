package cz.polankam.jjcron.remote.manager;

import cz.polankam.jjcron.remote.Client;
import java.rmi.Naming;

/**
 *
 * @author Neloop
 */
public class RMIClientFactory implements ClientFactory {

    private static final String DELIM = "/";

    /**
     *
     * @param addr
     * @return
     * @throws Exception
     */
    @Override
    public Client connect(ClientAddress addr) throws Exception {
        Client client = (Client) Naming.lookup(addr.registryAddress + DELIM
                + addr.clientIdentification);
        return client;
    }

    /**
     *
     * @param client
     * @throws Exception
     */
    @Override
    public void disconnect(Client client) throws Exception {
        // nothing to do here
    }
}
