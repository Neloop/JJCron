package cz.cuni.mff.ms.polankam.jjcron.remote.manager;

import cz.cuni.mff.ms.polankam.jjcron.remote.Client;
import cz.cuni.mff.ms.polankam.jjcron.remote.FakeClient;

/**
 *
 * @author Neloop
 */
public class RMIClientFactory implements ClientFactory {

    /**
     * 
     * @param addr
     * @return
     * @throws Exception
     */
    @Override
    public Client create(ClientAddress addr) throws Exception {
        return new FakeClient();
    }
}
