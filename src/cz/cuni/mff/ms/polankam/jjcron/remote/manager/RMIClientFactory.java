package cz.cuni.mff.ms.polankam.jjcron.remote.manager;

import cz.cuni.mff.ms.polankam.jjcron.remote.Client;

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
    public Client connect(ClientAddress addr) throws Exception {
        throw new Exception();
    }

    @Override
    public void disconnect(Client client) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
