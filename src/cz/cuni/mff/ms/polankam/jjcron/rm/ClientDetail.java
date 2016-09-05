package cz.cuni.mff.ms.polankam.jjcron.rm;

/**
 *
 * @author Neloop
 */
public class ClientDetail {
    private final String registryAddress;
    private final String clientIdentification;
    private boolean paused = false;

    public ClientDetail(String addr, String id) {
        registryAddress = addr;
        clientIdentification = id;

        // TODO: actual connection to client should be placed here
    }

    public String getRegistryAddress() {
        return registryAddress;
    }

    public String getClientIdentification() {
        return clientIdentification;
    }

    public void pause() {
        paused = true;
        // TODO: actual pause
    }

    public void unpause() {
        paused = false;
        // TODO: actual unpause
    }
}
