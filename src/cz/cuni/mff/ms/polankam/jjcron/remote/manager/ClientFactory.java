package cz.cuni.mff.ms.polankam.jjcron.remote.manager;

import cz.cuni.mff.ms.polankam.jjcron.remote.Client;

/**
 *
 * @author Neloop
 */
public interface ClientFactory {
    /**
     *
     * @param addr
     * @return
     * @throws java.lang.Exception
     */
    public Client create(ClientAddress addr) throws Exception;
}
