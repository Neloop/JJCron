package cz.cuni.mff.ms.polankam.jjcron.rm;

import cz.cuni.mff.ms.polankam.jjcron.common.Client;
import cz.cuni.mff.ms.polankam.jjcron.common.FakeClient;

/**
 *
 * @author Neloop
 */
public class RMIClientFactory implements ClientFactory {

    @Override
    public Client create(ClientAddress addr) {
        return new FakeClient();
    }
}
