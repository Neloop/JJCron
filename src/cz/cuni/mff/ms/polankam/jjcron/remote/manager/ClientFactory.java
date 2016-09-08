package cz.cuni.mff.ms.polankam.jjcron.remote.manager;

import cz.cuni.mff.ms.polankam.jjcron.remote.Client;

/**
 *
 * @author Neloop
 */
public interface ClientFactory {
    public Client create(ClientAddress addr);
}
