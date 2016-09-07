package cz.cuni.mff.ms.polankam.jjcron.rm;

import cz.cuni.mff.ms.polankam.jjcron.common.Client;

/**
 *
 * @author Neloop
 */
public interface ClientFactory {
    public Client create(ClientAddress addr);
}
