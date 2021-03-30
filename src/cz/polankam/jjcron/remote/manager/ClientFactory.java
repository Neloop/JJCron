package cz.polankam.jjcron.remote.manager;

import cz.polankam.jjcron.remote.Client;

/**
 * Defines methods for connecting and disconnecting clients. There can be used
 * any transfer path or technology as connection between UI and actual cron.
 * Only requirement is that cron instance on the other side has to be accessible
 * through {@link Client} interface.
 *
 * @author Neloop
 */
public interface ClientFactory {

    /**
     * Connects to actual client and return its representation in form of
     * {@link Client} interface.
     *
     * @param addr address of client which is remotely accessible
     * @return interface which should be somehow connected to remote client
     * @throws Exception in case of any connection error
     */
    public Client connect(ClientAddress addr) throws Exception;

    /**
     * Disconnects given client instance, after this client cannot be used
     * anymore.
     *
     * @param client client instance which should be disconnected
     * @throws Exception in case of any connection error
     */
    public void disconnect(Client client) throws Exception;
}
