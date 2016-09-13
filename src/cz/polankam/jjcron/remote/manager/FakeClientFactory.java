package cz.polankam.jjcron.remote.manager;

import cz.polankam.jjcron.remote.Client;

/**
 *
 * @author Neloop
 */
public class FakeClientFactory implements ClientFactory {

    @Override
    public Client connect(ClientAddress addr) throws Exception {
        return new FakeClient();
    }

    @Override
    public void disconnect(Client client) throws Exception {
        // nothing to do here
    }
}
